/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Dungeon;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Openable;

/**
 *
 * @author vntgasl
 */
public class DoorKey extends Mecanica {

    @Override
    public String getName() {
        return "Door with Key";
    }
    
    @EventHandler
    public void interage(PlayerInteractEvent ev) {
        if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK) {

            Block testado = ev.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
            final Openable d = (Openable) ev.getClickedBlock().getState().getData();

            if (!d.isOpen()) {

                boolean podeAbrir = false;
                String nomeChave = null;
                for (int x = 0; x < 3; x++) {

                    if (testado.getType() == Material.CHEST) {

                        Chest c = (Chest) testado.getState();
                        for (ItemStack i : c.getBlockInventory().getContents()) {
                            if (i == null) {
                                continue;
                            }
                            // se essa porta tem uma chave
                            if (i.getType() == Material.TRIPWIRE_HOOK) {

                                ItemStack chave = ev.getPlayer().getItemInHand();
                                ItemMeta minha = chave.getItemMeta();
                                ItemMeta porta = i.getItemMeta();
                                // eu to com uma chave na mao ?
                                nomeChave = porta.getDisplayName();
                                if (chave.getType() == Material.TRIPWIRE_HOOK) {

                                    // se as chaves tem nome custom
                                    if (minha.getDisplayName() != null && porta.getDisplayName() != null) {
                                        // se tem o mesmo nome
                                        if (minha.getDisplayName().trim().equalsIgnoreCase(porta.getDisplayName().trim())) {
                                            // se existe lore

                                            if (porta.getLore() != null && porta.getLore().size() > 0) {
                                                if (minha.getLore() == null || minha.getLore().size() != porta.getLore().size()) {
                                                    podeAbrir = false;
                                                } else {
                                                    // se a primeira linha da lore eh igual 
                                                    if (minha.getLore().get(0).trim().equalsIgnoreCase(porta.getLore().get(0).trim())) {
                                                        podeAbrir = true;
                                                    }
                                                }
                                            } else {
                                                // se tem o mermo nome e nao tem lore, abre 
                                                podeAbrir = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!podeAbrir) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("This door is locked !"));
                        } else {
                            ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You opened the door with your key !"));
                            if (!nomeChave.contains("[Quest]")) {
                                if (ev.getPlayer().getItemInHand().getAmount() <= 1) {
                                    ev.getPlayer().setItemInHand(null);
                                } else {
                                    ev.getPlayer().getItemInHand().setAmount(ev.getPlayer().getItemInHand().getAmount() - 1);
                                }
                            }
                            Block cima = null;
                            Block baixo = null;
                            if (ev.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.IRON_DOOR_BLOCK) {
                                cima = ev.getClickedBlock().getRelative(BlockFace.UP);
                                baixo = ev.getClickedBlock();
                            } else if (ev.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_DOOR_BLOCK) {
                                cima = ev.getClickedBlock();
                                baixo = ev.getClickedBlock().getRelative(BlockFace.DOWN);
                            }

                            Block toxa = null;

                            if (baixo.getRelative(BlockFace.EAST).getType() != Material.AIR) {
                                toxa = baixo.getRelative(BlockFace.EAST);
                            } else if (baixo.getRelative(BlockFace.WEST).getType() != Material.AIR) {
                                toxa = baixo.getRelative(BlockFace.WEST);
                            } else if (baixo.getRelative(BlockFace.NORTH).getType() != Material.AIR) {
                                toxa = baixo.getRelative(BlockFace.NORTH);
                            } else if (baixo.getRelative(BlockFace.SOUTH).getType() != Material.AIR) {
                                toxa = baixo.getRelative(BlockFace.SOUTH);
                            }
                            toxa = toxa.getRelative(BlockFace.DOWN);
                            if (toxa.getType() == Material.REDSTONE_TORCH_ON) {
                                toxa.setType(Material.AIR);
                                return;
                            }
                            Rewind.add(toxa, Material.AIR);
                            toxa.setType(Material.REDSTONE_TORCH_ON);
                            final Block t = toxa;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, new Runnable() {

                                @Override
                                public void run() {
                                    t.setType(Material.AIR);
                                }
                            }, 20 * 10);
                        }
                        break;
                    }
                    testado = testado.getRelative(BlockFace.DOWN);
                }
            }
        }
    }
}
