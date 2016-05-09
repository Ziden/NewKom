/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Dungeon;

import devsBrsMarotos.MetaShit;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 */
public class DungeonEvents extends Mecanica {

    @Override
    public String getName() {
        return "Dungeon Puzzles";
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public static void textActivateRedstoneOnAltars(final AsyncPlayerChatEvent ev) {
      if(DungeonManager.getDungeon(ev.getPlayer().getLocation())!=null) {
            if (ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {
                if (ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                    ItemStack book = BookUtil.getBookAt(ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN));
                    if (book != null) {
                        BookMeta meta = (BookMeta) book.getItemMeta();
                        if (meta.getPages().size() > 0) {
                            if (ev.getMessage().equalsIgnoreCase(meta.getPages().get(0))) {
                                Runnable r = new Runnable() {

                                    public void run() {
                                        final Block toxa = ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                                        if (!toxa.hasMetadata("redstone") && toxa.getType() == Material.REDSTONE_BLOCK) {
                                            toxa.setType(Material.AIR);
                                            ev.setCancelled(true);
                                            return;
                                        }
                                        if (toxa.hasMetadata("redstone")) {
                                            ev.setCancelled(true);
                                            return;
                                        }
                                        Rewind.add(toxa, Material.AIR);
                                        toxa.setType(Material.REDSTONE_BLOCK);

                                        MetaShit.setMetaString("redstone", toxa, "1");
                                        int task = Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia,
                                                new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        toxa.setType(Material.AIR);
                                                        toxa.removeMetadata("redstone", NewKom.instancia);
                                                    }
                                                }, 20 * 10);
                                    }
                                };
                                Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 5);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void itemActivatesRedstone(PlayerInteractEvent ev) {
        if (DungeonManager.getDungeon(ev.getPlayer().getLocation())!=null) {
            if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clicado = ev.getClickedBlock();
                if (clicado != null && ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() != Material.AIR) {
                    Block emBaixo = clicado.getRelative(BlockFace.DOWN);
                    for (int x = 0; x < 3; x++) {
                        emBaixo = emBaixo.getRelative(BlockFace.DOWN);
                        if (emBaixo.getType() == Material.GOLD_BLOCK) {
                            if (emBaixo.getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                                Chest c = (Chest) emBaixo.getRelative(BlockFace.DOWN).getState();
                                for (ItemStack ss : c.getBlockInventory().getContents()) {
                                    if (ss == null) {
                                        continue;
                                    }
                                    if (ss.getType() == ev.getPlayer().getItemInHand().getType()) {
                                        ItemMeta m1 = ss.getItemMeta();
                                        ItemMeta m2 = ev.getPlayer().getItemInHand().getItemMeta();
                                        if (((m1.getLore() == null && m2.getLore() == null)
                                                || m1.getLore().size() == m2.getLore().size() && m1.getLore().equals(m2.getLore())) && (m1.getDisplayName() != null && m2.getDisplayName() != null && m1.getDisplayName().equalsIgnoreCase(m2.getDisplayName()))) {

                                            if(!m2.getDisplayName().contains("[Quest]")) {
                                                int qtd = ev.getPlayer().getItemInHand().getAmount();
                                                if(qtd==1)
                                                    ev.getPlayer().setItemInHand(null);
                                                else
                                                    ev.getPlayer().getItemInHand().setAmount((qtd-1));
                                            }
                                            final Block bbb = emBaixo;
                                           // PlayEffect.play(VisualEffect.SMOKE, clicado.getLocation(), "num:3");
                                            emBaixo.setType(Material.REDSTONE_BLOCK);
                                            Runnable r = new Runnable() {
                                                @Override
                                                public void run() {
                                                    Block bloco = bbb.getLocation().getBlock();
                                                    if (bloco.getType() == Material.REDSTONE_BLOCK) {
                                                        bloco.setType(Material.GOLD_BLOCK);
                                                    }
                                                }
                                            };
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 20 * 20);
                                            Rewind.add(emBaixo, Material.GOLD_BLOCK);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void pushBlock(PlayerInteractEvent ev) {
        if (DungeonManager.getDungeon(ev.getPlayer().getLocation())!=null) {
            if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clicked = ev.getClickedBlock();
                if (clicked != null) {

                    if (clicked.getType().getId() == 70) {
                        ev.getPlayer().sendMessage(ChatColor.RED + "Esta pedra e muito pesada para ser empurrada !");
                        return;
                    }

                    if (clicked.getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {

                        if (ev.getBlockFace() == BlockFace.DOWN || ev.getBlockFace() == BlockFace.UP) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }

                        String frase = L.m("Voce empurrou o bloco !");
                        Block novo = clicked.getRelative(ev.getBlockFace().getOppositeFace());
                        if (ev.getPlayer().isSneaking()) {
                            frase = L.m("Voce puxou o bloco !");
                            novo = clicked.getRelative(ev.getBlockFace());
                        }
                        if (novo.getType() != Material.AIR || novo.getRelative(BlockFace.DOWN).getType() != Material.DIAMOND_BLOCK) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }
                        novo.setType(clicked.getType());
                        novo.getState().setData(clicked.getState().getData());
                        clicked.setType(Material.AIR);
                      //  PlayEffect.play(VisualEffect.SMOKE, clicked.getLocation(), "num:3");
                      //  PlayEffect.play(VisualEffect.SMOKE, novo.getLocation(), "num:3");
                        ev.getPlayer().sendMessage(ChatColor.GREEN + frase);
                    } else if (clicked.getRelative(BlockFace.DOWN).getType() == Material.ICE) {
                        if (ev.getBlockFace() == BlockFace.DOWN || ev.getBlockFace() == BlockFace.UP) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }
                        BlockFace direcao = ev.getBlockFace().getOppositeFace();
                        Block abaixo = ev.getClickedBlock().getRelative(direcao).getRelative(BlockFace.DOWN);
                        Block afrente = ev.getClickedBlock().getRelative(direcao);
                        if (afrente.getType() == Material.AIR && abaixo.getType() == Material.ICE) {
                            int max = 10;
                            List<Block> passar = new ArrayList<Block>();
                            Block ultimo = null;
                            while (max > 0) {
                                abaixo = abaixo.getRelative(direcao);
                                afrente = afrente.getRelative(direcao);
                                if (afrente.getType() == Material.AIR && abaixo.getType() == Material.ICE) {
                                    passar.add(afrente);
                                    ultimo = afrente;
                                } else {
                                    break;
                                }
                                max--;
                            }
                            if (ultimo != null) {
                                ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce empurrou o bloco !"));
                                ultimo.setType(clicked.getType());
                                ultimo.getState().setData(clicked.getState().getData());
                                clicked.setType(Material.AIR);
                                for (Block b : passar) {
                                //    PlayEffect.play(VisualEffect.SMOKE, b.getLocation(), "num:3");
                                //    PlayEffect.play(VisualEffect.SMOKE, clicked.getLocation(), "num:3");
                                }
                            }

                        } else {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void interact(PlayerInteractEvent ev) {
        if (ev.getPlayer().isOp()) {
            return;
        }
        if (ev.getPlayer().getItemInHand() != null && (ev.getPlayer().getItemInHand().getType() == Material.WATER_BUCKET || ev.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public static void blockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) {
            return;
        }

        if(DungeonManager.getDungeon(event.getPlayer().getLocation())==null) 
            return;
        
        Location l = event.getBlock().getLocation();
        l.setY(0); // 1910 4 998
        if (l.getBlock().getType() == Material.DIAMOND_BLOCK) {
            return;
        }

        if (event.getBlockReplacedState().getType() == Material.STATIONARY_WATER || event.getBlockReplacedState().getType() == Material.WATER || event.getBlockReplacedState().getType() == Material.LAVA) {
            event.setCancelled(true);
            return;
        }
        if (event.getBlock().getType() == Material.TORCH) {
            event.getPlayer().sendMessage(ChatColor.GREEN + L.m("You placed a temporary torch."));
            final Block b = event.getBlock();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    Block bloco = b.getLocation().getBlock();
                    if (bloco.getType() == Material.TORCH) {
                        bloco.setType(Material.AIR);
                    }
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 20 * 20);
            // NativeLevel.rewind.put(event.getBlock(), Material.AIR);
        } else {
            // foi pro SimpleClanKom.java
            // event.setCancelled(true);
            // event.getPlayer().sendMessage(ChatColor.RED + "Nada de tentar colocar blocos aqui !");
            // event.getPlayer().damage(5D);
        }
    }
    
}
