/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Dungeon;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.RedstoneTorch;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author vntgasl
 */
public class PassagemSecreta extends Mecanica {

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void abrePassagem(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        Block tocha = null;
        final Block suposTocha = event.getClickedBlock().getRelative(BlockFace.DOWN, 3);
        if (suposTocha.getType().equals(Material.REDSTONE_BLOCK)) {
            tocha = suposTocha;
        } else if (suposTocha.getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
            tocha = suposTocha.getRelative(BlockFace.DOWN);
        } else {
            return;
        }
        Block piston = tocha.getRelative(BlockFace.UP);
        if (piston.getType() != Material.PISTON_BASE && piston.getType() != Material.PISTON_STICKY_BASE && piston.getType() != Material.PISTON_EXTENSION) {
            return;
        }
        Block air = tocha.getRelative(BlockFace.UP, 4);
        final int ida = air.getTypeId();
        final byte datai = air.getData();
        final Block txx = tocha;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                txx.setTypeIdAndData(ida, datai, false);

            }
        },
                1
        );
        tocha.getState()
                .update();
        tocha.getRelative(
                0, 1, 0).getState().update();
        //PlayEffect.play(VisualEffect.SPELL, air.getLocation(), "num:2");
        tocha.setType(air.getType());
        air.setType(Material.AIR);

        delayFechar(tocha);
    }

    @Override
    public String getName() {
        return "Passagens Secretas";

    }

    public class fechaPassagem implements Runnable {

        private Block tox;

        public fechaPassagem(Block t) {
            tox = t;
        }

        @Override
        public void run() {
            //PlayEffect.play(VisualEffect.SPARK, tox.getLocation().getBlock().getRelative(BlockFace.UP, 4).getLocation(), "num:2");
            tox.getLocation().getBlock().getRelative(BlockFace.UP, 4).setTypeIdAndData(tox.getTypeId(), tox.getData(), false);
            tox.getLocation().getBlock().setType(Material.REDSTONE_BLOCK);

        }
    }

    @Override
    public void onDisable() {
        for (BukkitTask t : Bukkit.getScheduler().getPendingTasks()) {
            if (t instanceof fechaPassagem) {
                ((fechaPassagem) t).run();
                t.cancel();
            }
        }
    }

    public void delayFechar(final Block tocha) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, new fechaPassagem(tocha), 40l);
    }

}
