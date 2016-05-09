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
package devsBrsMarotos.cmds.cmdlist;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.mechanic.list.Dungeon.DungeonManager;
import devsBrsMarotos.mechanic.list.Land.ClanLand;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.rankings.Estatistica;
import devsBrsMarotos.mechanic.list.rankings.RankDB;
import java.util.List;
import java.util.UUID;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import java.util.HashMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Land extends Comando {

    public Land() {
        super("Land", CommandType.PLAYER);
    }
    
    public static HashMap<Location, String> rewindChunks = new HashMap<Location, String>();

    private static void sendHelp(CommandSender cs, boolean leader) {
        ClanLand.msg(cs, L.m("Use: /land friend add (nome)"));
        ClanLand.msg(cs, L.m("Use: /land friend remove (nome)"));
        ClanLand.msg(cs, L.m("Use: /land friend clean"));
        ClanLand.msg(cs, L.m("Use: /land info"));
        if (leader) {
            ClanLand.msg(cs, ChatColor.YELLOW + L.m("|____________Leader____________|"));
            ClanLand.msg(cs, ChatColor.YELLOW + L.m("Use: /land claim"));
            ClanLand.msg(cs, ChatColor.YELLOW + L.m("Use: /land unclaim"));
            ClanLand.msg(cs, ChatColor.YELLOW + L.m("Use: /land public"));
            ClanLand.msg(cs, ChatColor.YELLOW + L.m("Use: /land owner (name)"));
        }
    }

    public static boolean temGuildaPerto(Player p, ClanPlayer c, Location l) {
        String minhaTag = c.getTag();
        int x = p.getLocation().getChunk().getBlock(1, 0, 1).getLocation().getBlockX();
        int y = p.getLocation().getChunk().getBlock(1, 0, 1).getLocation().getBlockZ();
        Location up = new Location(p.getWorld(), x + 16, 0, y);
        Clan cUp = ClanLand.getClanAt(up);
        // nao tem clan em cima ou não é meu clan
        if (cUp == null || !cUp.getTag().equalsIgnoreCase(minhaTag)) {
            if (cUp != null && !cUp.getTag().equalsIgnoreCase(minhaTag)) {
                return true;
            }
            Location down = new Location(p.getWorld(), x - 16, 0, y);
            Clan cDown = ClanLand.getClanAt(down);
            if (cDown == null || !cDown.getTag().equalsIgnoreCase(minhaTag)) {
                if (cDown != null && !cDown.getTag().equalsIgnoreCase(minhaTag)) {
                    return true;
                }
                Location left = new Location(p.getWorld(), x, 0, y + 16);
                Clan cLeft = ClanLand.getClanAt(left);
                if (cLeft == null || !cLeft.getTag().equalsIgnoreCase(minhaTag)) {
                    if (cLeft != null && !cLeft.getTag().equalsIgnoreCase(minhaTag)) {
                        return true;
                    }
                    Location right = new Location(p.getWorld(), x, 0, y - 16);
                    Clan cRight = ClanLand.getClanAt(right);
                    if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                        if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                            return true;
                        }
                        right = new Location(p.getWorld(), x - 16, 0, y - 16);
                        cRight = ClanLand.getClanAt(right);
                        if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                            if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                return true;
                            }
                            right = new Location(p.getWorld(), x + 16, 0, y - 16);
                            cRight = ClanLand.getClanAt(right);
                            if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                    return true;
                                }
                                right = new Location(p.getWorld(), x - 16, 0, y + 16);
                                cRight = ClanLand.getClanAt(right);
                                if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                    if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                        return true;
                                    }
                                    right = new Location(p.getWorld(), x + 16, 0, y + 16);
                                    cRight = ClanLand.getClanAt(right);
                                    if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                        if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    
    @Override
    public void usouComando(CommandSender cs, String[] args) {
        if (cs instanceof ConsoleCommandSender) {
            ClanLand.msg(cs, L.m("Admin, favor entrar no jogo."));
            return;
        }
        boolean leader = ClanLand.manager.getClanPlayer((Player) cs) == null ? false : ClanLand.manager.getClanPlayer((Player) cs).isLeader();
        if (args.length == 0 || args.length > 3) {
            sendHelp(cs, leader);
            return;
        }
        Player p = (Player) cs;
        if (DungeonManager.getDungeon(p.getLocation())!=null) {
            p.sendMessage(ChatColor.RED + L.m("This command wont work here !"));
            return;
        }
        Clan c = ClanLand.getClanAt(p.getLocation());
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
        if (ClanLand.isSafeZone(p.getLocation())) {
            if (!p.isOp()) {
                ClanLand.msg(p, L.m("You got no rights on a SafeZone."));
                return;
            }
        }
        else if (ClanLand.isWarZone(p.getLocation())) {
            if (!p.isOp()) {
                ClanLand.msg(p, L.m("You got no rights on a WarZone."));
                return;
            }
        }else if (ClanLand.isAden(p.getLocation())) {
            if (!p.isOp()) {
                ClanLand.msg(p, L.m("You got no rights on Aden Lands."));
                return;
            }
        }
        if (args.length == 3) {
            if (!args[0].equals("mem") && !args[0].equals(L.m("friend"))) {
                sendHelp(cs, leader);
                return;
            }
            if (c == null || cp == null) {
                ClanLand.msg(p, L.m("Your not on a guild."));
                return;
            }
            if (!c.getTag().equals(cp.getClan().getTag())) {
                ClanLand.msg(p, L.m("This is not your guild lands."));
                return;
            }
            if (ClanLand.getOwnerAt(p.getLocation()).equals("none")) {
                ClanLand.msg(p, L.m("This is public guild land."));
                return;
            }
            if (!ClanLand.getOwnerAt(p.getLocation()).equals(p.getUniqueId().toString()) && !leader) {
                ClanLand.msg(p, L.m("You are not this land owner."));
                return;
            }
            String player = args[2];
            Player p2 = Bukkit.getPlayer(player);
            if (p2 != null) {
                String uuid = p2.getUniqueId().toString();
                if (!c.getAllMembers().contains(ClanLand.manager.getClanPlayer(player))) {
                    ClanLand.msg(p, L.m("This player is not from your guild."));
                    return;
                }
                if (args[1].equals(L.m("add"))) {
                    if (ClanLand.isMemberAt(p.getLocation(), uuid)) {
                        ClanLand.msg(p, L.m("This player is already a land member."));
                        return;
                    }
                    ClanLand.addMemberAt(p.getLocation(), uuid);
                    ClanLand.msg(p, L.m("The player %"  + ChatColor.GREEN + " now is a member of this land.",player));
                    return;
                } else if (args[1].equals("rem") || args[1].equals(L.m("remove"))) {
                    if (!ClanLand.isMemberAt(p.getLocation(), uuid)) {
                        ClanLand.msg(p, L.m("This player is not a member of this land."));
                        return;
                    }
                    ClanLand.removeMemberAt(p.getLocation(), uuid);
                    ClanLand.msg(p, L.m("The player %" + ChatColor.GREEN + " is not a member of this land anymore.",player));
                    return;
                } else {
                    sendHelp(cs, leader);
                    return;
                }
            } else {
                ClanLand.msg(p, L.m("The player %"+ ChatColor.GREEN + " is offline.",player));
                return;
            }
        } else if (args.length == 2) {
            if (args[0].equals(L.m("members")) || args[0].equals("mem")) {
                if (ClanLand.getOwnerAt(p.getLocation()).equals("none")) {
                    ClanLand.msg(p, L.m("This is a public guild land."));
                    return;
                }
                if (args[1].equals("clean")) {
                    if (cp == null) {
                        ClanLand.msg(p, L.m("Your not on a guild."));
                        return;
                    }
                    if (!c.getTag().equals(cp.getClan().getTag())) {
                        ClanLand.msg(p, L.m("This land does not belong to your guild."));
                        return;
                    }
                    if (!ClanLand.getOwnerAt(p.getLocation()).equals(p.getUniqueId().toString()) && !leader) {
                        ClanLand.msg(p, L.m("This land does not belong to you."));
                        return;
                    }
                    ClanLand.clearMembersAt(p.getLocation());
                    ClanLand.msg(p, L.m("You land have no more members."));
                    return;
                } else {
                    sendHelp(cs, leader);
                    return;
                }
            } else if (args[0].equals(L.m("owner"))) {
                if (cp == null) {
                    ClanLand.msg(p, L.m("Your not on a guild."));
                    return;
                } else if (!leader) {
                    ClanLand.msg(p, L.m("Your not the guild leader."));
                    return;
                }
                if (c == null || !c.getTag().equals(cp.getClan().getTag())) {
                    ClanLand.msg(p, L.m("This land does not belong to your guild."));
                    return;
                }
                Player alvo = Bukkit.getPlayer((args[1]));
                if (c != null && c.getAllAllyMembers() != null && !c.getAllMembers().contains(ClanLand.manager.getClanPlayer(alvo))) {
                    ClanLand.msg(p, L.m("This player is not from your guild."));
                    return;
                }
                String owner = ClanLand.getOwnerAt(p.getLocation());
                if (owner != null && owner.equals(alvo.getUniqueId().toString())) {
                    ClanLand.msg(p, L.m("This lands already belongs to %",args[1]));
                    return;
                }
                ClanLand.setOwnerAt(p.getLocation(), alvo.getUniqueId().toString());
                ClanLand.msg(p, L.m("Now this lands belong to %"+ ChatColor.GREEN + ", of your guild.",args[1]));
                ClanLand.update(p, p.getLocation());
                return;
            } else {
                sendHelp(cs, leader);
                return;
            }
        } else if (args[0].equals(L.m("public")) || args[0].equals("pub")) {
            if (cp == null) {
                ClanLand.msg(p, L.m("You dont have a guild."));
                return;
            } else if (!leader) {
                ClanLand.msg(p, L.m("Your not the guild leader."));
                return;
            }
            if (c == null || !c.getTag().equals(cp.getClan().getTag())) {
                ClanLand.msg(p, L.m("This land does not belong to your guild."));
                return;
            }
            if (ClanLand.getOwnerAt(p.getLocation()).equals("none")) {
                ClanLand.msg(p, L.m("This land is already public."));
                return;
            }
            ClanLand.msg(p, L.m("This land is public now."));
            ClanLand.setOwnerAt(p.getLocation(), null);
            ClanLand.update(p, p.getLocation());
            return;
        } else if (args[0].equals(L.m("unclaim"))) {
            if (cp == null) {
                ClanLand.msg(p, L.m("You do not have a guild."));
                return;
            } else if (!leader) {
                ClanLand.msg(p, L.m("Your not the guild leader."));
                return;
            }
            if (c == null) {
                ClanLand.msg(p, L.m("This land have no owner."));
                return;
            } else if (!c.getTag().equals(cp.getTag())) {
                ClanLand.msg(p, L.m("You cannot unclaim lands from %" + ChatColor.GREEN + ".",c.getColorTag()));
                return;
            }
            ClanLand.removeClanAt(p.getLocation());
            ClanLand.msg(p, L.m("This land does not belong to your guild anymore."));
            ClanLand.update(p, p.getLocation());
            return;
        } else if (args[0].equals(L.m("claim")) || args[0].equals("conq")) {
            if (DungeonManager.getDungeon(p.getLocation())!=null) {
                p.sendMessage(ChatColor.RED + L.m("You cannot claim lands here !"));
                return;
            }
            if (ClanLand.isSafeZone(p.getLocation()) || ClanLand.isWarZone(p.getLocation()) ||  ClanLand.isAden(p.getLocation()) ) {
                p.sendMessage(ChatColor.RED + L.m("You cannot claim here !"));
                return;
            }
            if (cp == null || cp.getClan() == null) {
                ClanLand.msg(p, L.m("You have no guild."));
                return;
            } else if (!leader) {
                ClanLand.msg(p, L.m("You are not the guild leader."));
                return;
            }
            ////// SE JA TEM UM CLAN AQUI
            if (c != null) {
                if (c.getTag().equals(cp.getTag())) {
                    ClanLand.msg(p, L.m("Your guild owns this land."));
                    return;
                }
                ClanLand.msg(p, L.m("The guild %" + ChatColor.GREEN + " owns this land.",c.getColorTag()));
                if (c.isRival(cp.getTag())) {
                    int ptosPilhagem = ClanLand.getPtosPilagem(cp.getTag(), c.getTag());
                    if (ptosPilhagem < 15) {
                        ClanLand.msg(p, L.m("You need % Plunder Points to steal this lands",15));
                        return;
                    }
                    ptosPilhagem -= 15;
                    String tagInimiga = c.getTag();
                    int x = p.getLocation().getChunk().getBlock(1, 0, 1).getLocation().getBlockX();
                    int y = p.getLocation().getChunk().getBlock(1, 0, 1).getLocation().getBlockZ();
                    Location up = new Location(p.getWorld(), x + 16, 0, y);
                    Clan cUp = ClanLand.getClanAt(up);
                    // se naõ tem clan em cima
                    if (cUp != null && cUp.getTag().equalsIgnoreCase(tagInimiga)) {
                        Location down = new Location(p.getWorld(), x - 16, 0, y);
                        Clan cDown = ClanLand.getClanAt(down);
                        // se nao tem clan em baixo
                        if (cDown != null && cDown.getTag().equalsIgnoreCase(tagInimiga)) {
                            Location left = new Location(p.getWorld(), x, 0, y + 16);
                            Clan cLeft = ClanLand.getClanAt(left);
                            if (cLeft != null && cLeft.getTag().equalsIgnoreCase(tagInimiga)) {
                                Location right = new Location(p.getWorld(), x, 0, y - 16);
                                Clan cRight = ClanLand.getClanAt(right);
                                if (cRight != null && cRight.getTag().equalsIgnoreCase(tagInimiga)) {
                                    ClanLand.msg(p, L.m("You have to conquer by the borders !"));
                                    return;
                                }
                            }
                        }
                    }
                    if (p.getEnderChest().contains(new ItemStack(Material.EMERALD, 50))) {
                        p.sendMessage(ChatColor.RED + L.m("You need 50 emeralds on your enderchest to do that !!"));
                        return;
                    }
                    ClanLand.removeClanAt(p.getLocation());
                    p.getEnderChest().removeItem(new ItemStack(Material.EMERALD, 50));
                    ClanLand.setClanAt(p.getLocation(), cp.getTag());
                    ClanLand.msg(p, ChatColor.GREEN + L.m("You conquest this lands for an hour !"));
                    ClanLand.setPtosPilhagem(cp.getTag(), c.getTag(), ptosPilhagem);
                    RankDB.addPontoCache(p, Estatistica.DOMINADOR, 1);
                    for (ClanPlayer p2 : c.getOnlineMembers()) {
                        p2.toPlayer().sendMessage(ChatColor.RED + L.m("A land from your guild was claimed by % for one hour !!",cp.getTag()));
                    }
                    ClanLand.update(p, p.getLocation());
                    final Location loc = p.getLocation();
                    final Clan c2 = c;

                    Runnable r;
                    r = new Runnable() {
                        public void run() {
                            ClanLand.removeClanAt(loc);
                            ClanLand.setClanAt(loc, c2.getTag());
                            for (ClanPlayer p : c2.getOnlineMembers()) {
                                if (p != null && p.toPlayer() != null) {
                                    p.toPlayer().sendMessage(ChatColor.GREEN + L.m("The claimed land returns to your guild !"));
                                }
                            }
                        }
                    };
                    rewindChunks.put(p.getLocation(), cp.getTag());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 20 * 60 * 60);
                }
                return;
            }
            c = cp.getClan();
            int qtdTerrenos = ClanLand.getQtdTerrenos(c.getTag());
            int poder = ClanLand.getPoder(c.getTag());

            int preco = qtdTerrenos * 5;
            if (!ClanLand.econ.has(p.getName(), preco)) {
                ClanLand.msg(p, L.m("You need % emeralds to claim a new land !",preco));
                return;
            }
            int numeroMembrosGuilda = c.getSize();
            if (qtdTerrenos >= 4 + poder) {
                ClanLand.msg(p, L.m("Your guild can only have % lands (chunks) !",((int) 4 + (int) poder)));
                ClanLand.msg(p, L.m("To get more lands, find Power Stones !!"));
                return;
            }
            // if (qtdTerrenos > 0) {
            String minhaTag = c.getTag();
            int x = p.getLocation().getChunk().getBlock(1, 0, 1).getLocation().getBlockX();
            int y = p.getLocation().getChunk().getBlock(1, 0, 1).getLocation().getBlockZ();
            Location up = new Location(p.getWorld(), x + 16, 0, y);
            Clan cUp = ClanLand.getClanAt(up);
            // nao tem clan em cima ou não é meu clan
            if (cUp == null || !cUp.getTag().equalsIgnoreCase(minhaTag)) {
                if (cUp != null && !cUp.getTag().equalsIgnoreCase(minhaTag)) {
                    p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                    return;
                }
                Location down = new Location(p.getWorld(), x - 16, 0, y);
                Clan cDown = ClanLand.getClanAt(down);
                if (cDown == null || !cDown.getTag().equalsIgnoreCase(minhaTag)) {
                    if (cDown != null && !cDown.getTag().equalsIgnoreCase(minhaTag)) {
                       p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                        return;
                    }
                    Location left = new Location(p.getWorld(), x, 0, y + 16);
                    Clan cLeft = ClanLand.getClanAt(left);
                    if (cLeft == null || !cLeft.getTag().equalsIgnoreCase(minhaTag)) {
                        if (cLeft != null && !cLeft.getTag().equalsIgnoreCase(minhaTag)) {
                            p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                            return;
                        }
                        Location right = new Location(p.getWorld(), x, 0, y - 16);
                        Clan cRight = ClanLand.getClanAt(right);
                        if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                            if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                 p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                                return;
                            }
                            right = new Location(p.getWorld(), x - 16, 0, y - 16);
                            cRight = ClanLand.getClanAt(right);
                            if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                     p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                                    return;
                                }
                                right = new Location(p.getWorld(), x + 16, 0, y - 16);
                                cRight = ClanLand.getClanAt(right);
                                if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                    if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                         p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                                        return;
                                    }
                                    right = new Location(p.getWorld(), x - 16, 0, y + 16);
                                    cRight = ClanLand.getClanAt(right);
                                    if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                        if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                             p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                                            return;
                                        }
                                        right = new Location(p.getWorld(), x + 16, 0, y + 16);
                                        cRight = ClanLand.getClanAt(right);
                                        if (cRight == null || !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                            if (cRight != null && !cRight.getTag().equalsIgnoreCase(minhaTag)) {
                                                 p.sendMessage(ChatColor.RED + L.m("You cannot claim near other guilds !"));
                                                return;
                                            }
                                            if (qtdTerrenos > 0) {
                                                ClanLand.msg(p, L.m("You can only claim near your land or enemy lands !"));
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // }
            ClanLand.econ.withdrawPlayer(p.getName(), preco);
            p.playSound(p.getLocation(), Sound.DIG_GRASS, 10, 0);
            ClanLand.setClanAt(p.getLocation(), cp.getClan().getTag());
            ClanLand.msg(p, L.m("Now this lands belongs to your guild !."));
            ClanLand.update(p, p.getLocation());
            return;
        } else if (args[0].equals(L.m("info")) || args[0].equals("i")) {
            if (c == null) {
                ClanLand.msg(p, L.m("This land has no owner."));
                return;
            }
            if (cp != null && c.getTag().equals(cp.getClan().getTag())) {
                String owner = ClanLand.getOwnerAt(p.getLocation());
                if (owner.equals("none")) {
                    ClanLand.msg(p, L.m("This land is public, from your guild."));
                    return;
                } else {
                    List<String> membros = ClanLand.getMembersAt(p.getLocation());
                    ClanLand.msg(p, "This lands belongs to " + owner + ChatColor.GREEN + ", from your guild.");
                    if (membros.size() == 1 && membros.get(0).isEmpty()) {
                        ClanLand.msg(p, L.m("This land has no members."));
                    } else {
                        ClanLand.msg(p, L.m("Land Members:"));
                        for (String s : membros) {
                            UUID id = UUID.fromString(s);
                            OfflinePlayer mem = Bukkit.getPlayer(id);
                            if (mem == null) {
                                mem = Bukkit.getOfflinePlayer(id);
                            }
                            if (mem != null) {
                                ClanLand.msg(p, "- " + mem.getName());
                            }
                        }
                    }
                    return;
                }
            }
            ClanLand.msg(p, L.m("This lands belongs to the guild % ", c.getColorTag()));
            return;
        } else if (args[0].equals("safe")) {
            if (p.isOp()) {
                if (ClanLand.isSafeZone(p.getLocation())) {
                    ClanLand.setClanAt(p.getLocation(), "WILD");
                    ClanLand.msg(p, L.m("NOT SAFE ANYMORE"));
                } else {
                    ClanLand.setClanAt(p.getLocation(), "SAFE");
                    ClanLand.msg(p, L.m("NOW IS SAFE"));
                }
                ClanLand.update(p, p.getLocation());
                return;
            } else {
                sendHelp(cs, leader);
                return;
            }
        } else if (args[0].equals("war")) {
            if (p.isOp()) {
                if (ClanLand.isWarZone(p.getLocation())) {
                    ClanLand.setClanAt(p.getLocation(), "WILD");
                    ClanLand.msg(p, L.m("NOT WAR ANYMORE"));
                } else {
                    ClanLand.setClanAt(p.getLocation(), "WARZ");
                    ClanLand.msg(p, L.m("NOW IS WAR"));
                }
                ClanLand.update(p, p.getLocation());
                return;
            } else {
                sendHelp(cs, leader);
                return;
            }
        } else if (args[0].equals("aden")) {
            if (p.isOp()) {
                if (ClanLand.isWarZone(p.getLocation())) {
                    ClanLand.setClanAt(p.getLocation(), "ADEN");
                    ClanLand.msg(p, L.m("NOT ADEN ANYMORE"));
                } else {
                    ClanLand.setClanAt(p.getLocation(), "ADEN");
                    ClanLand.msg(p, L.m("NOW IS ADEN"));
                }
                ClanLand.update(p, p.getLocation());
                return;
            } else {
                sendHelp(cs, leader);
                return;
            }
        }else {
            sendHelp(cs, leader);
            return;
        }
    }

}
