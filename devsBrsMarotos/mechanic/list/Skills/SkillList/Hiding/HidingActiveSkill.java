/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Hiding;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Events.UpdateEvent;
import devsBrsMarotos.mechanic.list.Events.UpdateType;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.ActiveSkill;
import devsBrsMarotos.mechanic.list.Skills.PlayerMana;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Vanish;
import devsBrsMarotos.util.Cooldown;
import devsBrsMarotos.util.ParticleEffect;
import devsBrsMarotos.util.UtilBlock;
import devsBrsMarotos.util.GeneralUtils;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Carlos
 */
public class HidingActiveSkill extends Mecanica implements ActiveSkill {

    public static HashMap<UUID, Integer> hiding = new HashMap();
    public static HidingActiveSkill instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public boolean activeSkill(Player p, SkillFormulas.SkillResult sr) {
        final UUID uid = p.getUniqueId();
        if (hiding.containsKey(uid)) {
            p.sendMessage("?");
            return false;
        }

        if (sr == SkillFormulas.SkillResult.EPIC_SUCCESS || sr == SkillFormulas.SkillResult.SUCCESS) {
            Vanish.vanish(p);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
            p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1);
            p.sendMessage("§c" + L.m("You disappear in the shadows."));
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

                @Override
                public void run() {
                    Player pl = Bukkit.getPlayer(uid);
                    if (pl != null && pl.isOnline() && hiding.containsKey(pl.getUniqueId()) && !renova(pl)) {
                        pl.sendMessage("§c" + L.m("You are visible now."));
                        Bukkit.getScheduler().cancelTask(hiding.remove(uid));
                        Cooldown.removeCooldown(pl, "hideandando");
                    }
                }
            }, 20 * 5, 20 * 5);

            hiding.put(p.getUniqueId(), task);
        } else {
            p.sendMessage("§4" + L.m("You lost the concentration."));
            p.playSound(p.getLocation(), Sound.FIZZ, 1, 1);
        }
        SkillFormulas.levelUpSkill(p, SkillEnum.Hiding, getMinimumSkill(p), 1);

        return true;
    }

    public boolean renova(Player p) {
        PlayerMana mana = PlayerMana.getMana(p);
        if (mana.mana < 10) {
            p.playSound(p.getLocation(), Sound.FIZZ, 1, 1);
            p.sendMessage(ChatColor.BLUE + L.m("You need more mana: ") + mana.mana + "/" + mana.maxMana + " - " + ChatColor.RED + 10);
            return false;
        }
        SkillFormulas.SkillResult sr = SkillFormulas.hasSucess(getSkill(), p, getMinimumSkill(p));
        if (sr == SkillFormulas.SkillResult.SUCCESS || sr == SkillFormulas.SkillResult.EPIC_SUCCESS) {
            PlayerMana.spendMana(p, 10);
            SkillFormulas.levelUpSkill(p, SkillEnum.Hiding, getMinimumSkill(p), 1);
            return true;
        } else {
            p.sendMessage("§4" + L.m("You lost the concentration."));
            p.playSound(p.getLocation(), Sound.FIZZ, 1, 1);
        }
        return false;
    }

    public static void remove(Player p) {
        if (hiding.containsKey(p.getUniqueId())) {
            int task = hiding.get(p.getUniqueId());
            Bukkit.getScheduler().cancelTask(task);
            Vanish.appear(p);
            Cooldown.removeCooldown(p, "hideandando");
        }

    }

    @EventHandler
    public void quit(PlayerQuitEvent ev) {
        remove(ev.getPlayer());
    }

    @EventHandler
    public void move(PlayerMoveEvent ev) {
        if (hiding.containsKey(ev.getPlayer().getUniqueId())) {
            if (mudou(ev.getTo(), ev.getFrom())) {
                if (UtilBlock.solid(ev.getTo().getBlock().getRelative(0, -1, 0))) {

                    if (Cooldown.isCooldown(ev.getPlayer(), "hidepara")) {
                        Location to = ev.getFrom().clone();
                        to.setX(to.getBlockX() + .5);
                        to.setZ(to.getBlockZ() + .5);
                        ev.setTo(to);
                        return;
                    }
                    if (!Cooldown.isCooldown(ev.getPlayer(), "hideandando")) {
                        SkillFormulas.SkillResult sr = SkillFormulas.hasSucess(SkillEnum.Stealth, ev.getPlayer(), 60);

                        Cooldown.setMetaCooldown(ev.getPlayer(), "hideandando", 5000);

                        Player p = ev.getPlayer();
                        if (sr == SkillFormulas.SkillResult.EPIC_SUCCESS || sr == SkillFormulas.SkillResult.SUCCESS) {
                            SkillFormulas.levelUpSkill(p, SkillEnum.Stealth, getMinimumSkill(p), 1);
                            return;
                        }
                        Cooldown.setMetaCooldown(ev.getPlayer(), "hidepara", 5000);
                        Location to = ev.getFrom().clone();
                        to.setX(to.getBlockX() + .5);
                        to.setZ(to.getBlockZ() + .5);
                        ev.setTo(to);
                        p.playSound(p.getLocation(), Sound.FIZZ, 1, 1);
                        p.sendMessage("§c" + L.m("You failed to move stealth."));

                    }
                }

            }
        }

    }

    public static boolean mudou(Location to, Location from) {
        if (to.getBlockX() != from.getBlockX()) {
            return true;
        }
        if (to.getBlockZ() != from.getBlockZ()) {
            return true;
        }
        return false;

    }

    @EventHandler
    public void kick(PlayerKickEvent ev) {
        remove(ev.getPlayer());
    }

    @EventHandler
    public void death(PlayerDeathEvent ev) {
        remove(ev.getEntity());
    }

    /*
     NIVEL 60 para baixo fica colocando fumaça onde o nego ta
     */
    @EventHandler
    public void update(UpdateEvent ev) {
        if (ev.getType() == UpdateType.FAST) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (hiding.containsKey(p.getUniqueId())) {
                    Skills s = NewKom.database.skills.getSkills(p);
                    if (s.get(SkillEnum.Hiding).getLvl() < 60) {
                        ParticleEffect.SMOKE_LARGE.display(0.3F, 0.3F, 0.3F, 0, 5, p.getLocation().clone().add(0, 1, 0), 32);

                    }
                    String msg = "§a§lHIDING";
                    if (Cooldown.isCooldown(p, "hideandando") && !Cooldown.isCooldown(p, "hidepara")) {
                        msg += " §e§l- §c§lSTEALTHY";
                    }
                    GeneralUtils.sendActionBar(p, msg);
                }
            }
        }
    }

    @Override
    public int getMana() {
        return 30;
    }

    @Override
    public SkillEnum getSkill() {
        return SkillEnum.Hiding;
    }

    @Override
    public int getCooldown() {
        return 10000;
    }

    @Override
    public String getSkillName() {
        return "Hiding";
    }

    @Override
    public int getMinimumSkill(Player p) {
        int luz = p.getLocation().getBlock().getLightLevel();
        int x = 20 + luz * 2;
        for (Entity t : p.getNearbyEntities(4, 4, 4)) {
            if (t instanceof Player) {
                x *= 2;
                break;
            }
        }
        return x;
    }

    @Override
    public String getName() {
        return "Hiding";
    }

}
