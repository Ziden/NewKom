/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Magery;

import devsBrsMarotos.MetaShit;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.RecipeBook;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import devsBrsMarotos.mechanic.list.Skills.PlayerMana;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.util.ScreenTitle;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author User
 */
public class MageEvents extends Mecanica {

    @Override
    public String getName() {
        return "Mage Spells";
    }

    @EventHandler
    public void interact(PlayerInteractEvent ev) {
        if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.WRITTEN_BOOK) {
            CustomItem ci = CustomItem.getCustomItem(ev.getPlayer().getItemInHand());
            if (ci instanceof RecipeBook) {
                RecipeBook book = (RecipeBook) ci;
                if (book.getBookType(ev.getPlayer().getItemInHand()) == BookTypes.Magery) {

                    Elements summoned = null;

                    if (ev.getPlayer().isSneaking() && ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        summoned = Elements.Earth;
                    } else if (!ev.getPlayer().isSneaking() && ev.getAction() == Action.LEFT_CLICK_AIR || ev.getAction() == Action.LEFT_CLICK_BLOCK) {
                        summoned = Elements.Fire;
                    } else if (ev.getPlayer().isSneaking() && ev.getAction() == Action.LEFT_CLICK_AIR || ev.getAction() == Action.LEFT_CLICK_BLOCK) {
                        summoned = Elements.Thunder;
                    }

                    if (summoned != null) {
                        ev.setCancelled(true);
                        List<Elements> elements = new ArrayList<Elements>(3);
                        if (ev.getPlayer().hasMetadata("elements")) {
                            elements = (List<Elements>) MetaShit.getMetaObject("elements", ev.getPlayer());
                        }
                        // summonig elements
                        if (elements.size() < 3) {
                            ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Summoned a ") + summoned.icon + " " + summoned.name());
                            elements.add(summoned);
                            String title = "";
                            for (Elements e : elements) {
                                title += e.icon;
                            }
                            ScreenTitle.show(ev.getPlayer(), ".", title);
                            MetaShit.setMetaObject("elements", ev.getPlayer(), elements);
                        } else {
                            // checking if i have the spell recipe
                            boolean casted = false;
                            List<String> recipes = book.getRecipes(ev.getPlayer().getItemInHand());
                            for (String recipe : recipes) {
                                MageSpell spell = SpellLoader.spells.get(recipe);
                                Elements[] summonedElements = elements.toArray(new Elements[3]);
                                if (spell.getElements()[0].name() == summonedElements[0].name() && spell.getElements()[1].name() == summonedElements[1].name() && spell.getElements()[2].name() == summonedElements[2].name()) {

                                    double requiredMana = spell.getManaCost();

                                    if (!PlayerMana.spendMana(ev.getPlayer(), (int) requiredMana)) {
                                        return;
                                    }

                                    double minSkill = spell.getMinSkill();
                                    double mySkill = NewKom.database.skills.getSkills(ev.getPlayer()).get(SkillEnum.Magery).getLvl();

                                    SkillResult result = SkillFormulas.hasSucess(SkillEnum.Magery, ev.getPlayer(), minSkill);
                                    if (result == SkillResult.SUCCESS || result == SkillResult.EPIC_SUCCESS) {
                                        SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Magery, mySkill, spell.getExpRatio());
                                        spell.cast(ev.getPlayer());
                                    } else {
                                        ev.getPlayer().sendMessage(L.m(ChatColor.RED + "The spell fizzled"));
                                        return;
                                    }

                                    casted = true;
                                    break;

                                }
                            }
                            if (!casted) {
                                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You dont have the scroll to that spell !"));
                            }
                            ev.getPlayer().removeMetadata("elements", NewKom.instancia);
                        }

                    }
                }
            }
        }
    }

    public void onEnable() {
        SpellLoader.load();
    }

}
