package net.minecraft.src;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;

public class DedicatedServer extends MinecraftServer implements IServer
{
    private final List pendingCommandList = Collections.synchronizedList(new ArrayList());
    private RConThreadQuery field_71342_m;
    private RConThreadMain field_71339_n;
    private PropertyManager settings;
    private boolean canSpawnStructures;
    private EnumGameType gameType;
    private NetworkListenThread networkThread;
    private boolean guiIsEnabled = false;

    public DedicatedServer(File par1File)
    {
        super(par1File);
        new DedicatedServerSleepThread(this);
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        DedicatedServerCommandThread var1 = new DedicatedServerCommandThread(this);
        var1.setDaemon(true);
        var1.start();
        ConsoleLogManager.init();
        logger.info("Starting minecraft server version 1.3.2");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        FMLCommonHandler.instance().onServerStart(this);

        logger.info("Loading properties");
        this.settings = new PropertyManager(new File("server.properties"));

        if (this.isSinglePlayer())
        {
            this.getHostName("127.0.0.1");
        }
        else
        {
            this.setOnlineMode(this.settings.getOrSetBoolProperty("online-mode", true));
            this.getHostName(this.settings.getOrSetProperty("server-ip", ""));
        }

        this.setSpawnAnimals(this.settings.getOrSetBoolProperty("spawn-animals", true));
        this.setSpawnNpcs(this.settings.getOrSetBoolProperty("spawn-npcs", true));
        this.setAllowPvp(this.settings.getOrSetBoolProperty("pvp", true));
        this.setAllowFlight(this.settings.getOrSetBoolProperty("allow-flight", false));
        this.setTexturePack(this.settings.getOrSetProperty("texture-pack", ""));
        this.setMOTD(this.settings.getOrSetProperty("motd", "A Minecraft Server"));
        spawnProtectionSize = this.settings.getOrSetIntProperty("spawn-protection-size", 16);
        this.canSpawnStructures = this.settings.getOrSetBoolProperty("generate-structures", true);
        int var2 = this.settings.getOrSetIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
        this.gameType = WorldSettings.getGameTypeById(var2);
        logger.info("Default game type: " + this.gameType);
        InetAddress var3 = null;

        if (this.getHostname().length() > 0)
        {
            var3 = InetAddress.getByName(this.getHostname());
        }

        if (this.getServerPort() < 0)
        {
            this.setServerPort(this.settings.getOrSetIntProperty("server-port", 25565));
        }

        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.createNewKeyPair());
        logger.info("Starting Minecraft server on " + (this.getHostname().length() == 0 ? "*" : this.getHostname()) + ":" + this.getServerPort());

        try
        {
            this.networkThread = new DedicatedServerListenThread(this, var3, this.getServerPort());
        }
        catch (IOException var15)
        {
            logger.warning("**** FAILED TO BIND TO PORT!");
            logger.log(Level.WARNING, "The exception was: " + var15.toString());
            logger.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.isServerInOnlineMode())
        {
            logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            logger.warning("The server will make no attempt to authenticate usernames. Beware.");
            logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            logger.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        FMLCommonHandler.instance().onServerStarted();
        this.setConfigurationManager(new DedicatedPlayerList(this));
        long var4 = System.nanoTime();

        if (this.getFolderName() == null)
        {
            this.setFolderName(this.settings.getOrSetProperty("level-name", "world"));
        }

        String var6 = this.settings.getOrSetProperty("level-seed", "");
        String var7 = this.settings.getOrSetProperty("level-type", "DEFAULT");
        long var8 = (new Random()).nextLong();

        if (var6.length() > 0)
        {
            try
            {
                long var10 = Long.parseLong(var6);

                if (var10 != 0L)
                {
                    var8 = var10;
                }
            }
            catch (NumberFormatException var14)
            {
                var8 = (long)var6.hashCode();
            }
        }

        WorldType var16 = WorldType.parseWorldType(var7);

        if (var16 == null)
        {
            var16 = WorldType.DEFAULT;
        }

        this.setBuildLimit(this.settings.getOrSetIntProperty("max-build-height", 256));
        this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
        this.setBuildLimit(MathHelper.clamp_int(this.getBuildLimit(), 64, 256));
        this.settings.setArbitraryProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
        logger.info("Preparing level \"" + this.getFolderName() + "\"");
        this.loadAllDimensions(this.getFolderName(), this.getFolderName(), var8, var16);
        long var11 = System.nanoTime() - var4;
        String var13 = String.format("%.3fs", new Object[] {Double.valueOf((double)var11 / 1.0E9D)});
        logger.info("Done (" + var13 + ")! For help, type \"help\" or \"?\"");

        if (this.settings.getOrSetBoolProperty("enable-query", false))
        {
            logger.info("Starting GS4 status listener");
            this.field_71342_m = new RConThreadQuery(this);
            this.field_71342_m.startThread();
        }

        if (this.settings.getOrSetBoolProperty("enable-rcon", false))
        {
            logger.info("Starting remote control listener");
            this.field_71339_n = new RConThreadMain(this);
            this.field_71339_n.startThread();
        }
        FMLCommonHandler.instance().handleServerStarting(this);
        return true;
    }

    public boolean canStructuresSpawn()
    {
        return this.canSpawnStructures;
    }

    public EnumGameType getGameType()
    {
        return this.gameType;
    }

    /**
     * defaults to "1" for the dedicated server
     */
    public int getDifficulty()
    {
        return this.settings.getOrSetIntProperty("difficulty", 1);
    }

    /**
     * defaults to false
     */
    public boolean isHardcore()
    {
        return this.settings.getOrSetBoolProperty("hardcore", false);
    }

    /**
     * called on exit from the main run loop
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        while (this.isServerRunning())
        {
            this.executePendingCommands();

            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException var3)
            {
                var3.printStackTrace();
            }
        }
    }

    /**
     * iterates the worldServers and adds their info also
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.addCrashSectionCallable("Type", new CallableType(this));
        return par1CrashReport;
    }

    /**
     * directly calls system.exit, instantly killing the program
     */
    protected void systemExitNow()
    {
        System.exit(0);
    }

    public void updateTimeLightAndEntities()
    {
		/**DIMENSION API EDIT**/
        //super.updateTimeLightAndEntities();
        this.theProfiler.startSection("levels");

        for (Integer id : net.minecraftforge.common.DimensionManager.getIDs())
		{
			performDimensionUpdates(id, false);
		}
		Integer[] dapiIds = DimensionAPI.numberMap.values().toArray(new Integer[0]);
		for (Integer id : dapiIds)
        {
			performDimensionUpdates(id, true);
		}
		
        this.theProfiler.endStartSection("connection");
        this.getNetworkThread().networkTick();
        this.theProfiler.endStartSection("players");
        this.getConfigurationManager().sendPlayerInfoToAllPlayers();
        this.theProfiler.endStartSection("tickables");
        java.util.Iterator var5 = this.dedicatedPlayersOnline.iterator();

        while (var5.hasNext())
        {
            IUpdatePlayerListBox var6 = (IUpdatePlayerListBox)var5.next();
            var6.update();
        }

        this.theProfiler.endSection();
		/**END EDIT**/
        this.executePendingCommands();
    }
	
	/**DIMENSION API EDIT**/
	private void performDimensionUpdates(int id, boolean flag)
	{
		long var2 = System.nanoTime();

            if (id == 0 || this.getAllowNether())
            {
				WorldServer var4 = null;
				if(flag)
				{
					var4 = theWorldServer[id];
				} else
				{
					var4 = net.minecraftforge.common.DimensionManager.getWorld(id);
				}
                this.theProfiler.startSection(var4.getWorldInfo().getWorldName());

                if (this.getTickCounter() % 20 == 0)
                {
                    this.theProfiler.startSection("timeSync");
                    this.getConfigurationManager().sendPacketToAllPlayersInDimension(new Packet4UpdateTime(var4.getWorldTime()), var4.provider.worldType);
                    this.theProfiler.endSection();
                }

                this.theProfiler.startSection("tick");
                FMLCommonHandler.instance().onPreWorldTick(var4);
                var4.tick();
                FMLCommonHandler.instance().onPostWorldTick(var4);
                this.theProfiler.endStartSection("lights");

                while (true)
                {
                    if (!var4.updatingLighting())
                    {
                        this.theProfiler.endSection();
                        var4.updateEntities();
                        this.theProfiler.startSection("tracker");
                        var4.getEntityTracker().processOutstandingEntries();
                        this.theProfiler.endSection();
                        this.theProfiler.endSection();
                        break;
                    }
                }
            }

            worldTickTimes.get(id)[this.getTickCounter() % 100] = System.nanoTime() - var2;
	}
	
	@SideOnly(Side.SERVER)
	public void addToOnlinePlayerList(IUpdatePlayerListBox par1IUpdatePlayerListBox)
    {
        dedicatedPlayersOnline.add(par1IUpdatePlayerListBox);
    }
	
	private final List dedicatedPlayersOnline = new ArrayList();
	/**END EDIT**/

    public boolean getAllowNether()
    {
        return this.settings.getOrSetBoolProperty("allow-nether", true);
    }

    public boolean allowSpawnMonsters()
    {
        return this.settings.getOrSetBoolProperty("spawn-monsters", true);
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(this.getDedicatedPlayerList().isWhiteListEnabled()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(this.getDedicatedPlayerList().getIPWhiteList().size()));
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.settings.getOrSetBoolProperty("snooper-enabled", true);
    }

    public void addPendingCommand(String par1Str, ICommandSender par2ICommandSender)
    {
        this.pendingCommandList.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void executePendingCommands()
    {
        while (!this.pendingCommandList.isEmpty())
        {
            ServerCommand var1 = (ServerCommand)this.pendingCommandList.remove(0);
            this.getCommandManager().executeCommand(var1.sender, var1.command);
        }
    }

    public boolean isDedicatedServer()
    {
        return true;
    }

    public DedicatedPlayerList getDedicatedPlayerList()
    {
        return (DedicatedPlayerList)super.getConfigurationManager();
    }

    public NetworkListenThread getNetworkThread()
    {
        return this.networkThread;
    }

    public int getOrSetIntProperty(String par1Str, int par2)
    {
        return this.settings.getOrSetIntProperty(par1Str, par2);
    }

    public String getOrSetProperty(String par1Str, String par2Str)
    {
        return this.settings.getOrSetProperty(par1Str, par2Str);
    }

    public boolean getOrSetBoolProperty(String par1Str, boolean par2)
    {
        return this.settings.getOrSetBoolProperty(par1Str, par2);
    }

    public void setArbitraryProperty(String par1Str, Object par2Obj)
    {
        this.settings.setArbitraryProperty(par1Str, par2Obj);
    }

    public void saveSettingsToFile()
    {
        this.settings.saveSettingsToFile();
    }

    public String getSettingsFilePath()
    {
        File var1 = this.settings.getFile();
        return var1 != null ? var1.getAbsolutePath() : "No settings file";
    }

    public boolean getGuiEnabled()
    {
        return this.guiIsEnabled;
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.getDedicatedPlayerList();
    }

    @SideOnly(Side.SERVER)
    public void func_79001_aj()
    {
        ServerGUI.func_79003_a(this);
        this.guiIsEnabled = true;
    }
	
	/**DIMENSION API EDIT**/
	protected void loadAllDimensions(String par1Str, String par2Str, long par3, WorldType par5WorldType)
    {
        this.convertMapIfNeeded(par1Str);
        this.setUserMessage("menu.loadingLevel");
        ISaveHandler var6 = this.getActiveAnvilConverter().getSaveLoader(par1Str, true);
        WorldInfo var8 = var6.loadWorldInfo();
        WorldSettings var7;

        if (var8 == null)
        {
            var7 = new WorldSettings(par3, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), par5WorldType);
        }
        else
        {
            var7 = new WorldSettings(var8);
        }

        if (bonusChest)
        {
            var7.enableBonusChest();
        }

        WorldServer overWorld = (isDemo() ? new DemoWorldServer(this, var6, par2Str, 0, theProfiler) : new WorldServer(this, var6, par2Str, 0, var7, theProfiler));
        for (int dim : net.minecraftforge.common.DimensionManager.getIDs())
        {
            WorldServer world = (dim == 0 ? overWorld : new WorldServerMulti(this, var6, par2Str, dim, var7, overWorld, theProfiler));
            world.addWorldAccess(new WorldManager(this, world));
            if (!this.isSinglePlayer())
            {
                world.getWorldInfo().setGameType(this.getGameType());
            }
        }
		
		/**DIMENSION API EDIT**/
		int dimensions = DimensionAPI.numberOfDimensions - 3 + theWorldServer.length;
		Integer[] dapiIds = DimensionAPI.dimensionMap.values().toArray(new Integer[0]);
		WorldServer[] aworldserver = new WorldServer[dimensions];
		DimensionAPI.numberMap.clear();
		for(int i = 0; i < dimensions; i++)
		{
			if(i < theWorldServer.length)
			{
				aworldserver[i] = theWorldServer[i];
			} else
			{
				WorldServer worldserver = new WorldServerMulti(this, var6, par2Str, dapiIds[i - theWorldServer.length], var7, overWorld, theProfiler);
				aworldserver[i] = worldserver;
				DimensionAPI.numberMap.put(dapiIds[i - theWorldServer.length], i);
				worldserver.addWorldAccess(new WorldManager(this, worldserver));
				worldTickTimes.put(i, new long[100]);
				if (!isSinglePlayer())
				{
					worldserver.getWorldInfo().setGameType(getGameType());
				}
			}
		}
		theWorldServer = aworldserver;
		/**END EDIT**/

        this.getConfigurationManager().setPlayerManager(new WorldServer[]{ overWorld });
        this.setDifficultyForAllDimensions(this.getDifficulty());
        this.initialWorldChunkLoad();
    }
	
	public WorldServer worldServerForDimension(int par1)
    {
		/**DIMENSION API EDIT**/
		if (DimensionAPI.numberMap.containsKey(Integer.valueOf(par1)))
        {
            int i = ((Integer)DimensionAPI.numberMap.get(Integer.valueOf(par1))).intValue();
			return theWorldServer[i];
        }
		return super.worldServerForDimension(par1);
		/**END EDIT**/
    }
	
	public void canCreateBonusChest(boolean par1)
    {
        bonusChest = par1;
		super.canCreateBonusChest(par1);
    }
	
	private boolean bonusChest = false;
	/**END EDIT**/
}
