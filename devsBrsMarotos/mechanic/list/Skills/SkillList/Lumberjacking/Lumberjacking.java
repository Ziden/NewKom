package devsBrsMarotos.mechanic.list.Skills.SkillList.Lumberjacking;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Events.UpdateEvent;
import devsBrsMarotos.mechanic.list.Events.UpdateType;
import devsBrsMarotos.mechanic.list.Harvesting.EpicFailEffect;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestCache;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.BlockHarvestEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.TreeDownEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.util.ParticleEffect;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;
import devsBrsMarotos.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Carlos
 */
public class Lumberjacking extends Mecanica {

    HashMap<KomTree, BukkitTask> tasks = new HashMap();

    @EventHandler(priority = EventPriority.HIGH)
    public void blockbreak(BlockBreakEvent ev) {
        if (ev.isCancelled()) {
            return;
        }
        if (ev.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        for (TreeType wd : TreeType.values()) {
            if (ev.getBlock().getType() == Material.LOG || ev.getBlock().getType() == Material.LOG_2) {

                if ((getRealData(ev.getBlock()) == wd.wdata && ev.getBlock().getType() == wd.wmat)) {

                    final KomTree t = new KomTree(wd, ev.getBlock());
                    if (t.getLogs().size() < 5 || t.getLeaves().size() < 7) {
                        continue;
                    }
                    if (ev.getPlayer().getItemInHand() == null || !ev.getPlayer().getItemInHand().getType().name().contains("_AXE")) {
                        ev.getPlayer().sendMessage(NewKom.alertTag + L.m("You need a axe to chop it!"));
                        ev.setCancelled(true);
                        return;
                    }
                    ev.setCancelled(true);
                    ArrayList<ItemStack> drops = new ArrayList();
                    drops.add(wd.wood.gera(1));
                    TreeDownEvent evento = new TreeDownEvent(ev.getPlayer(), drops, t);
                    Bukkit.getPluginManager().callEvent(evento);
                    if (evento.isCancelled()) {
                        return;
                    }
                    for (Block b : t.getLogs()) {
                        ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(b.getType(), b.getData()), 0.3F, 0.3F, 0.3F, 0, 60, b.getLocation(), 16);

                        b.setType(Material.AIR);

                    }
                    for (Block b : t.getLeaves()) {
                        b.setType(Material.AIR);

                    }
                    t.create();

                    for (ItemStack drop : evento.getDrops()) {
                        ev.getBlock().getWorld().dropItem(t.getDropsLocation(), drop);
                    }
                    t.getSapling().setType(Material.SAPLING);
                    t.getSapling().setData(getSaplingData(wd.wood));
                    if (t.getChao().getType() == Material.DIRT) {
                        t.getChao().setType(Material.GRASS);
                    }
                    ev.getBlock().getWorld().playSound(ev.getBlock().getLocation(), Sound.CHEST_CLOSE, 2F, 0.5F);
                    BukkitTask bt = Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

                        @Override
                        public void run() {
                            List<Chunk> tounload = new ArrayList();
                            for (Block b : t.getDefaultLogs().keySet()) {
                                if (!b.getChunk().isLoaded()) {
                                    b.getChunk().load(true);
                                    tounload.add(b.getChunk());
                                }
                                MaterialData d = t.getDefaultLogs().get(b);
                                b.setType(d.getItemType());
                                b.setData(d.getData());
                            }
                            for (Block b : t.getDefaultLeaves().keySet()) {
                                MaterialData d = t.getDefaultLeaves().get(b);
                                b.setType(d.getItemType());
                                b.setData(d.getData());
                                if (!b.getChunk().isLoaded()) {
                                    b.getChunk().load(true);
                                    tounload.add(b.getChunk());
                                }
                            }
                            for (Chunk ch : tounload) {
                                ch.unload();
                            }
                            if (t.getHolo().isSpawned()) {
                                t.getHolo().removeLineBelow();
                                t.getHolo().despawn();
                            }

                            tasks.remove(t);
                        }
                    }, t.getType().cd);
                    tasks.put(t, bt);
                    break;

                }
            }

        }

    }

    public static byte getSaplingData(WoodType wt) {
        byte x = 0;
        if (wt.m == Material.LOG_2) {
            x += 4;
        }
        x += wt.data;
        return x;
    }

    @Override
    public String getName() {
        return "Woodcutting";
    }

    @EventHandler
    public void grow(StructureGrowEvent ev) {
        ev.setCancelled(true);
    }

    public static int getRealData(Block b) {
        if (b.getType() == Material.LOG || b.getType() == Material.LEAVES) {
            return b.getData() % 4;
        }
        if (b.getType() == Material.LOG_2 || b.getType() == Material.LEAVES_2) {
            return b.getData() % 2;
        }
        return 0;

    }

    @EventHandler
    public void update(UpdateEvent ev) {

        if (ev.getType() == UpdateType.SEC) {
            for (KomTree t : tasks.keySet()) {
                if (!t.getHolo().getLocation().getChunk().isLoaded()) {
                    continue;
                }
                int secods = (int) (((t.getQuebrada() + (t.getType().cd * 50)) - System.currentTimeMillis())) / 1000;
                if (secods < 0) {
                    continue;
                }
                String str = TimeUtils.getTimeToString(secods);

                t.getHolo().getLineBelow().setText("§a" + str);
            }
        }
    }

    @Override
    public void onDisable() {

        for (BukkitTask task : Bukkit.getScheduler().getPendingTasks()) {
            if (tasks.containsValue(task)) {
                ((Runnable) task).run();

            }
        }

        //for (BukkitTask ts : tasks.values()) {
        //    ((Runnable) ts).run();
        //}
    }

    @EventHandler
    public void harv(BlockHarvestEvent ev) {
        if (ev.getHarvestable().skillsNeeded == SkillEnum.Lumberjacking) {
            ev.setDefaultHarvest(false);
        }
    }

    @EventHandler
    public void down(TreeDownEvent ev) {

        double minSkill = ev.getTree().getType().minSkill;
        double expRatio = ev.getTree().getType().expRatio;
        SkillFormulas.SkillResult result = SkillFormulas.hasSucess(SkillEnum.Lumberjacking, ev.getPlayer(), minSkill);
        if (result == SkillFormulas.SkillResult.ALMOST_FAILED || result == SkillFormulas.SkillResult.EPIC_FAIL || result == SkillFormulas.SkillResult.FAIL) {

            ev.getDrops().clear();
            if (result == SkillFormulas.SkillResult.EPIC_FAIL) {
                ev.getPlayer().sendMessage(NewKom.errorTag + EpicFailEffect.getEffect(ev.getPlayer(), SkillEnum.Lumberjacking));
                return;
            }
            ev.getPlayer().sendMessage(NewKom.errorTag + L.m("You failed to chop this tree."));
            return;
        }
        SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Lumberjacking, minSkill, expRatio);

        if (result == SkillFormulas.SkillResult.EPIC_SUCCESS) {
            int rnd = new Random().nextInt(3);
            if (rnd == 1) {
                ev.getDrops().add(ev.getDrops().get(0));
            } else if (rnd == 0) {
                ev.getDrops().add(new ItemStack(Material.APPLE));
            } else if (rnd == 2) {
                ev.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
            }
            ev.getPlayer().sendMessage(NewKom.successTag + L.m("You collected bonus resources from the tree!"));

        }

    }

}
