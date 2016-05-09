/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj
 Patrocionio: MinecraftMania

 */
package devsBrsMarotos.mechanic.list.Land;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.sacredlabyrinth.phaed.simpleclans.managers.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author Daniel
 */
public class ClanLand extends Mecanica{

    
    public static ClanManager manager;
    public static StorageManager storage;
    public static ClanLand plug;
    private static Logger log;
    public static Economy econ;
    private static Connection conn;
    private static Statement est;
    public static Permission permission = null;

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = NewKom.instancia.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
    
    @EventHandler
    public void doJoin(PlayerJoinEvent ev) {
         ClanLand.update(ev.getPlayer(), ev.getPlayer().getLocation());
    }
    
    @EventHandler
    public void move(PlayerMoveEvent event) {
         // se mudou de chunk
        if (ClanLand.isSameChunk(event.getFrom(), event.getTo())) {
            return;
        }

        if (event.getTo().getWorld().getName().equalsIgnoreCase("Vila") || event.getTo().getWorld().getName().equalsIgnoreCase("Dungeon")) {
            return;
        }

       // zone level X

        String type = ClanLand.getTypeAt(event.getTo());
        String lastType = ClanLand.getTypeAt(event.getFrom());
        if (type.equals(lastType)) {
            if (type.equals("CLAN") && lastType.equals("CLAN")) {
                String tag = ClanLand.getClanAt(event.getTo()).getTag();
                String lasttag = ClanLand.getClanAt(event.getFrom()).getTag();
                if (!tag.equals(lasttag)) {
                    ClanLand.update(event.getPlayer(), event.getTo());
                } else {
                    String donoVai = ClanLand.getOwnerAt(event.getTo());
                    String dono2 = ClanLand.getOwnerAt(event.getFrom());
                    if (donoVai == null && dono2 != null) {
                        ClanLand.update(event.getPlayer(), event.getTo());
                    } else if (donoVai != null && dono2 == null) {
                        ClanLand.update(event.getPlayer(), event.getTo());
                    } else if (donoVai != null && dono2 != null && !donoVai.equalsIgnoreCase(dono2)) {
                        ClanLand.update(event.getPlayer(), event.getTo());
                    }
                }

            }

        } else {
            ClanLand.update(event.getPlayer(), event.getTo());
        }
    }

    public static boolean isSameChunk(Location one, Location two) {
        if (one.getBlockX() >> 4 != two.getBlockX() >> 4) {
            return false;
        }
        if (one.getBlockZ() >> 4 != two.getBlockZ() >> 4) {
            return false;
        }
        return one.getWorld() == two.getWorld();
    }

    public static void update(Player p, Location to) {
        String type = getTypeAt(to);
        if (type.equalsIgnoreCase("SAFE")) {
            rawMsg(p, ChatColor.AQUA + " ~" + ChatColor.GOLD + "SafeZone");
        } else if (type.equalsIgnoreCase("WARZ")) {
            rawMsg(p, ChatColor.AQUA + " ~" + ChatColor.DARK_RED + "WarZone");
        } else if (type.equalsIgnoreCase("WILD")) {
            rawMsg(p, ChatColor.AQUA + " ~" + ChatColor.DARK_GREEN + L.m("Free Lands"));
        } else if (type.equalsIgnoreCase("ADEN")) {
            rawMsg(p, ChatColor.AQUA + " ~" + ChatColor.GREEN + L.m("Aden Lands"));
        }else {
            Clan c = getClanAt(to);
            if (c.getAllMembers().contains(getPlayer(p.getName()))) {
                String owner = getOwnerAt(to);
                if (owner.equals("none")) {
                    rawMsg(p, ChatColor.AQUA + "~§r" + c.getColorTag() + ChatColor.AQUA
                            + " - " + "§r" + c.getName() + ChatColor.AQUA
                            + " - " + ChatColor.DARK_GREEN + L.m("Public"));
                } else {

                    OfflinePlayer dono = Bukkit.getPlayer(UUID.fromString(owner));
                    if (dono == null) {
                        dono = Bukkit.getOfflinePlayer(UUID.fromString(owner));
                    }
                    if (dono == null) {
                        owner = L.m("ERROR, talk to staff plz !!");
                    }
                    owner = dono.getName();
                    rawMsg(p, ChatColor.AQUA + "~§r" + c.getColorTag() + ChatColor.AQUA
                            + " - " + "§r" + c.getName() + ChatColor.AQUA
                            + " - " + ChatColor.DARK_GREEN + L.m("Lands of")+ "§r" + owner);
                }
            } else {
                rawMsg(p, ChatColor.AQUA + "~§r" + c.getColorTag() + ChatColor.AQUA + " - " + "§r" + c.getName());
            }
        }
    }

    public static void rawMsg(Player p, String string) {
        p.sendMessage(string);
    }

    public static void sincroniza() {
        /*
         int ptos = 0;
         try {
         est = conn.createStatement();
         ResultSet rs = est.executeQuery("select ptos from ptosPilhagem where minhaTag='"+minhaTag+"' and tagInimiga='"+tagInimiga+"'");
         if(rs.next()) {
         return rs.getInt("ptos");
         } else {
         est.execute("insert into ptosPilhagem (ptos, minhaTag, tagInimiga) values (0,'"+minhaTag+"' ,'"+tagInimiga+"')"); 
         }
         } catch (SQLException ex) {
         NativeLevel.log.info("ZUOU BANCO:"+ex.getMessage());
         ex.printStackTrace();
         }
         return ptos;
         * 
         */
    }
    
    public static String getGuildaAli(Chunk c) {
         try {
             String tag = null;
             est = conn.createStatement();
             ResultSet rs = est.executeQuery("select tag from kk where world = '"+c.getWorld().getName()+"' and x ="+c.getX()+" and z = "+c.getZ());
             if(rs.next())
                 tag = rs.getString("tag");
             return tag;
         } catch (SQLException ex) {
                        ex.printStackTrace();
         }
         return null;
    }

    public static void limpaGuildaEDesfaz(final String tag, final boolean regen) {
        //if (KnightsOfMania.debugMode) {
           ClanLand.debugTerrenos(tag);
        //}
        try {

            Runnable r = new Runnable() {
                public void run() {
                    try {
                        List<Location> limpar = new ArrayList<Location>();
                        est = conn.createStatement();
                        List<Chunk> removidos = new ArrayList<Chunk>();
                        NewKom.log.info("peski");
                        ResultSet rs = est.executeQuery("select world, x, z from kk where tag = '" + tag + "'");
                        NewKom.log.info("peskizei");
                        while (rs.next()) {
                            NewKom.log.info("achei um terreno do clan "+tag);
                            final World w = Bukkit.getWorld(rs.getString("world"));
                            if (w != null) {

                                if (regen) {
                                    Chunk c = w.getChunkAt(rs.getInt("x"), rs.getInt("z"));
                                    Clan clan = ClanLand.getClanAt(c.getBlock(0, 0, 0).getLocation());
                                    if(clan.getInactiveDays()>7) {
                                        c.load(true);
                                        w.regenerateChunk(rs.getInt("x"), rs.getInt("z"));
                                        c.unload();
                                    }
                                    
                                    if (clan != null && clan.getTag().equalsIgnoreCase(tag)) {
                                            limpar.add(c.getBlock(0, 0, 0).getLocation());
                                       
                                       // if (KnightsOfMania.debugMode) {
                                            NewKom.log.info("defiz um terreno do clan " + tag);
                                        //}
                                    }
                                }

                            }
                        }
                        for(Location l : limpar) {
                             ClanLand.removeClanAt(l);
                        }
                        est = conn.createStatement();
                        est.executeUpdate("delete from kk where tag='" + tag + "'");
                        est = conn.createStatement();
                        est.executeUpdate("delete from ptosPilhagem where minhaTag='" + tag + "'");
                        NewKom.log.info("limpado");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 1);

           
            //conn.commit();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        plug = this;
        manager = ((SimpleClans) Bukkit.getServer().getPluginManager()
                .getPlugin("SimpleClans")).getClanManager();
        NewKom.log.info("SimpleClans INITIALIZED HOOK !"+manager.toString());
        storage = ((SimpleClans) Bukkit.getServer().getPluginManager()
                .getPlugin("SimpleClans")).getStorageManager();
        log = Logger.getLogger("Minecraft");
        setupEconomy();
        setupPermissions();
        String connStr = "jdbc:mysql://127.0.0.1:3306/NewKom?autoReconnect=true";
        NewKom.instancia.getDataFolder().mkdirs();
        //Bukkit.getPluginCommand("terreno").setExecutor(new Terreno());
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(connStr, "root", "komzika");

            est = conn.createStatement();
            est.executeUpdate("CREATE TABLE IF NOT EXISTS kk ("
                    + "tag VARCHAR(3), "
                    + "world VARCHAR(20), "
                    + "x INT, "
                    + "z INT, "
                    + "owner VARCHAR(200), "
                    + "port INT)");

            est.executeUpdate("CREATE TABLE IF NOT EXISTS ptosPilhagem ("
                    + "minhaTag VARCHAR(3), "
                    + "tagInimiga VARCHAR(3), "
                    + "ptos INT ); ");

            est.executeUpdate("CREATE TABLE IF NOT EXISTS poder ("
                    + "minhaTag VARCHAR(3), "
                    + "qtd INT ); ");

        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
            Bukkit.getServer().shutdown();
        }
    }

    public static void limpaChunks(String tag) {
        try {
         
            est = conn.createStatement();
            ResultSet rs = est.executeQuery("select mundo, x, z from kk where tag = '" + tag + "'");
            while (rs.next()) {

                World w = Bukkit.getWorld(rs.getString("mundo"));
                int x = rs.getInt("x");
                int z = rs.getInt("z");
                NewKom.log.info("limpando terreno " + rs.getString("mundo") + " " + x + " " + z);
                if (w != null) {
                    w.regenerateChunk(x, z);
                    NewKom.log.info("regenerado !");
                }
            }
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info("ZUOU BANCO:" + ex.getMessage());
            ex.printStackTrace();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Reiniciando rapidamente !");
        }
    }

    // registrando o metodo de economia do vault
    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static int getQtdTerrenos(String minhaTag) {
        int ptos = 0;
        try {
            est = conn.createStatement();
            ResultSet rs = est.executeQuery("select count(tag) as qtd from kk where tag='" + minhaTag + "'");
            if (rs.next()) {
                return rs.getInt("qtd");
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ptos;
    }
    
      public static int debugTerrenos(String minhaTag) {
        int ptos = 0;
        try {
            est = conn.createStatement();
            ResultSet rs = est.executeQuery("select x,z,world from kk where tag='" + minhaTag + "'");
            if (rs.next()) {
                NewKom.log.info("World: "+rs.getString("world")+rs.getInt("x")+rs.getInt("z"));
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ptos;
    }

    public static int getPoder(String minhaTag) {
        int ptos = 0;
        try {
            est = conn.createStatement();
            ResultSet rs = est.executeQuery("select qtd from poder where minhaTag='" + minhaTag + "'");
            if (rs.next()) {
                return rs.getInt("qtd");
            } else {
                est.execute("insert into poder (minhaTag, qtd) values ('" + minhaTag + "' ,0)");
            }
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info(ex.getMessage());
            ex.printStackTrace();
        }
        return ptos;
    }

    public static void sync(Location l) {
        if (l.getChunk().getBlock(2, 0, 2).getType() == Material.BEDROCK) {
            l.getChunk().getBlock(2, 0, 2).setType(Material.SANDSTONE);
            l.getChunk().getBlock(2, 1, 2).setType(Material.BEDROCK);
        } else {
            return;
        }
        int ptos = 0;
        Clan aqui = ClanLand.getClanAt(l);
        try {
            est = conn.createStatement();
            ResultSet rs = est.executeQuery("select tag,world,x,z from kk where x =" + l.getChunk().getX() + " and z = " + l.getChunk().getZ());
            if (rs.next()) {
                if (aqui == null) {
                    ClanLand.removeClanAt(l);
                    NewKom.log.info("tirei clan de " + l.getChunk().getX() + " " + l.getChunk().getZ());
                } else {
                    if (!aqui.getTag().equalsIgnoreCase(rs.getString("tag"))) {
                        ClanLand.setClanAt(l, aqui.getTag());
                        NewKom.log.info("setei clan de " + l.getChunk().getX() + " " + l.getChunk().getZ() + " para " + aqui.getTag());
                    }
                }
            }
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void setPoder(String minhaTag, int ptos) {
        try {
            est = conn.createStatement();
            est.executeUpdate("update poder set qtd = " + ptos + " where minhaTag='" + minhaTag + "'");
            //conn.commit();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public static int getPtosPilagem(String minhaTag, String tagInimiga) {
        int ptos = 0;
        try {
            est = conn.createStatement();
            ResultSet rs = est.executeQuery("select ptos from ptosPilhagem where minhaTag='" + minhaTag + "' and tagInimiga='" + tagInimiga + "'");
            if (rs.next()) {
                return rs.getInt("ptos");
            } else {
                est.execute("insert into ptosPilhagem (ptos, minhaTag, tagInimiga) values (0,'" + minhaTag + "' ,'" + tagInimiga + "')");
            }
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info("ZUOU BANCO:" + ex.getMessage());
            ex.printStackTrace();
        }
        return ptos;
    }

    public static void setPtosPilhagem(String minhaTag, String tagInimiga, int ptos) {
        try {
            est = conn.createStatement();
            est.executeUpdate("update ptosPilhagem set ptos = " + ptos + " where minhaTag='" + minhaTag + "' and tagInimiga='" + tagInimiga + "'");
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info("ZUOU BANCO:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void onDisable() {
        try {
            conn.close();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public static Clan getClanAt(Location l) {
        BookMeta bm = getBookMeta(l);
        if (bm == null) {
            return null;
        }
        if (bm.getTitle() == null || bm.getTitle().equals("WILD")) {
            return null;
        }
        return getClan(bm.getTitle());
    }

    public static String getOwnerAt(Location l) {
        BookMeta bm = getBookMeta(l);
        return bm.getAuthor();
    }

    public static int[] getChunkLocation(Location l) {
        l = l.getChunk().getBlock(0, 0, 0).getLocation();
        return new int[]{(int) l.getX() / 16, (int) l.getZ() / 16};
    }

    public static Location locOfChunk(String w, int x, int z) {
        return Bukkit.getWorld(w).getChunkAt(x * 16, z * 16).getBlock(0, 0, 0).getLocation();
    }

    public static void setOwnerAt(Location l, String owner) {
        if (owner == null) {
            owner = "none";
        }
        BookMeta bm = getBookMeta(l);
        bm.setAuthor(owner);
        bm.setPage(1, "");
        setBookMeta(l, bm);

        int[] xz = getChunkLocation(l);
        try {
            est.executeUpdate("UPDATE kk SET owner='" + owner + "' WHERE tag='" + bm.getTitle() + "' AND x='" + xz[0] + "' AND z='" + xz[1] + "'  AND port='" + Bukkit.getPort() + "'");
            //conn.commit();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public static void addMemberAt(Location l, String friend) {
        BookMeta bm = getBookMeta(l);
        if (bm.getPage(1).isEmpty()) {
            bm.setPage(1, friend);
            setBookMeta(l, bm);
            return;
        }
        bm.setPage(1, bm.getPage(1) + "\n" + friend);
        setBookMeta(l, bm);
    }

    public static void removeMemberAt(Location l, String friend) {
        BookMeta bm = getBookMeta(l);
        String[] members = bm.getPage(1).split("\n");
        StringBuilder end = new StringBuilder();
        for (String member : members) {
            if (member.equals(friend)) {
                continue;
            }
            end.append(member);
            if (!members[members.length - 1].equals(member)) {
                end.append("\n");
            }
        }
        bm.setPage(1, end.toString());
        setBookMeta(l, bm);
    }

    public static boolean isMemberAt(Location l, String friend) {
        return getMembersAt(l).contains(friend);
    }

    public static void clearMembersAt(Location l) {
        BookMeta bm = getBookMeta(l);
        bm.setPage(1, "");
        setBookMeta(l, bm);
    }

    public static List<String> getMembersAt(Location l) {
        BookMeta bm = getBookMeta(l);
        return Arrays.asList(bm.getPage(1).split("\n"));
    }

    private static BookMeta getBookMeta(Location l) {
        Block b = l.getChunk().getBlock(0, 0, 0);
        if (!b.getType().equals(Material.CHEST)) {
            b.getRelative(BlockFace.UP).setType(Material.BEDROCK);
            b.setType(Material.CHEST);
        }
        BookMeta bm;
        Inventory inv = ((Chest) b.getState()).getBlockInventory();
        if(inv==null) {
            NewKom.log.info("BOOOK SENDO NULL !!!!");
        }
        if (inv.getItem(0) == null || !inv.getItem(0).getType().equals(Material.WRITTEN_BOOK)) {
            log.log(Level.INFO, "creating info for chunk {0}, {1}", new Object[]{l.getChunk().getX(), l.getChunk().getZ()});
            inv.setItem(0, new ItemStack(Material.WRITTEN_BOOK));
            bm = (BookMeta) inv.getItem(0).getItemMeta();
            bm.setAuthor("none");
            bm.addPage("");
            bm.setTitle("WILD");
            inv.getItem(0).setItemMeta(bm);
        }
        return (BookMeta) inv.getItem(0).getItemMeta();
    }

    // metodo magico pra pegar o nivel do mob q nasce aki
    public static int getChunkDistanceFromSpawn(Location l) {
        return ((int) l.getChunk().getBlock(0, 0, 0).getLocation().distance(l.getWorld().getSpawnLocation().getChunk().getBlock(0, 0, 0).getLocation()) >> 4);
    }

    public static int getMobLevel(Location l) {
        ApplicableRegionSet set = NewKom.worldGuard.getRegionManager(l.getWorld()).getApplicableRegions(l);
        if (set == null || set.size() == 0) {
            if (l.getWorld().getName().equalsIgnoreCase("dungeon")) {
                return 1;
            }
            int distancia = getChunkDistanceFromSpawn(l);
            return (distancia >> 3) / 2;
        } else {
            while (set.iterator().hasNext()) {
                ProtectedRegion regiao = set.iterator().next();

                if (regiao.getPriority() > 0 && regiao.getId().contains("mobspawn")) {
                    return regiao.getPriority();
                } else {
                    int distancia = getChunkDistanceFromSpawn(l);
                    return (distancia >> 3) / 2;
                }
            }
            return 0;
        }

    }

    public static boolean isSafeZone(Location l) {
        BookMeta bm = getBookMeta(l);
        if (bm == null || bm.getTitle() == null) {
            return false;
        }
        return bm.getTitle().equals("SAFE");
    }
    
    public static boolean isAden(Location l) {
        BookMeta bm = getBookMeta(l);
        if (bm == null || bm.getTitle() == null) {
            return false;
        }
        return bm.getTitle().equals("ADEN");
    }

    public static boolean isWarZone(Location l) {
        BookMeta bm = getBookMeta(l);
        if (bm == null || bm.getTitle() == null) {
            return false;
        }
        return bm.getTitle().equals("WARZ");
    }

    private static void setBookMeta(Location l, BookMeta bm) {
        Block b = l.getChunk().getBlock(0, 0, 0);
        Inventory inv = ((Chest) b.getState()).getBlockInventory();
        inv.getItem(0).setItemMeta(bm);

        if (bm.getTitle().equals("WILD")) {
            b.setType(Material.BEDROCK);
        }
    }

    public static void setClanAt(Location l, String clan) {
        BookMeta bm = getBookMeta(l);
        bm.setAuthor("none");
        bm.setTitle(clan);
        bm.setPage(1, "");
        setBookMeta(l, bm);
        
        if (clan.equalsIgnoreCase("WILD") || clan.equalsIgnoreCase("SAFE") || clan.equalsIgnoreCase("WARZ") || clan.equalsIgnoreCase("ADEN")) {
            return;
        }
        int[] xz = getChunkLocation(l);
        try {

            est.executeUpdate("INSERT INTO kk (tag ,world, x , z ,owner, port) VALUES ("
                    + "'" + clan + "', '" + l.getWorld().getName() + "','" + xz[0] + "', '" + xz[1] + "', 'none', '" + Bukkit.getPort() + "')");
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void removeClanAt(Location l) {
    try {
        est = conn.createStatement();
        l.getChunk().getBlock(0, 0, 0).setType(Material.BEDROCK);
        int[] xz = getChunkLocation(l);
        
            est.executeUpdate("DELETE FROM kk WHERE world='"
                    + l.getWorld().getName() + "' AND x='" + xz[0] + "' AND z='" + xz[1] + "'  AND port='" + Bukkit.getPort() + "'");
            //conn.commit();
        } catch (SQLException ex) {
            NewKom.log.info(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Clan getClan(String s) {
        Clan clan;
        clan = manager.getClan(s);
        if (clan != null) {
            return clan;
        }
        //Player p = Bukkit.getPlayer(s);
        //if (p != null) {
        //    ClanPlayer cp = manager.getClanPlayer(p);
        //    if (cp == null) {
        //        return null;
        //    }
        //    return manager.getClanPlayer(p).getClan();
        //}
        return null;
    }

    public static ClanPlayer getPlayer(String s) {
        return manager.getClanPlayer(s);
    }

    public static String getTypeAt(Location l) {
        if (l.getWorld().getName().equalsIgnoreCase("woe") || l.getWorld().getName().equalsIgnoreCase("arena")) {
            return "WARZ";
        }
        BookMeta bm = getBookMeta(l);
        Clan c = getClanAt(l);
        if (c == null) {
            if (isSafeZone(l)) {
                return "SAFE";
            } else if (isWarZone(l)) {
                return "WARZ";
            } else if (isAden(l)) {
                return "ADEN";
            } else {
                return "WILD";
            }
        } else {
            return "CLAN";//c.getTag();
        }
    }

    public static void msg(CommandSender cs, String msg) {
        cs.sendMessage(ChatColor.RED + "[Kom] " + ChatColor.GREEN + msg);
    }

    @Override
    public String getName() {
       return "Clan Land";
    }
}
