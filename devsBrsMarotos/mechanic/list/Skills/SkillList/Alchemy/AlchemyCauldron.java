/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.MetaShit;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.PotionExtract;
import devsBrsMarotos.mechanic.list.CustomItems.list.RecipeBook;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import devsBrsMarotos.util.ParticleEffect;
import devsBrsMarotos.util.GeneralUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class AlchemyCauldron extends Mecanica {

    @Override
    public String getName() {
        return "Alchemy Cauldron";
    }

    public boolean hasTheRightIngredients(ItemStack[] ingredients, List<ItemStack> ingredientsIn) {
        int ingredientsRight = 0;
        for (ItemStack ingred : ingredients) {
            for (ItemStack used : ingredientsIn) {
                if (used.getType() == ingred.getType() && used.getData().getData() == ingred.getData().getData()) {
                    ingredientsRight += 1;
                }
            }
        }
        if (ingredientsRight == 3) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void interage(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() != Material.AIR && ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.CAULDRON) {
            ev.setCancelled(true);
            int waterLevel = ev.getClickedBlock().getData();
            List<ItemStack> ingredientsIn = new ArrayList<ItemStack>();
           
            Block cauld = ev.getClickedBlock();
            if (cauld.hasMetadata("ingredients")) {
                ingredientsIn = (List<ItemStack>) MetaShit.getMetaObject("ingredients", cauld);
            }
            if(ingredientsIn.size() != waterLevel) {
                waterLevel = ingredientsIn.size();
                cauld.setData((byte)ingredientsIn.size());
            }
            ItemStack inHand = ev.getPlayer().getItemInHand();

            /////////////////////////////////////////////
            // Cauldron is not full, adding ingredient //
            /////////////////////////////////////////////
            if (waterLevel < 3) {

                if (ev.getPlayer().getItemInHand().getType() == Material.HOPPER) {
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You took the cauldron ingredients"));
                    ev.getPlayer().getInventory().addItem(ingredientsIn.toArray(new ItemStack[ingredientsIn.size()]));
                    cauld.removeMetadata("ingredients", NewKom.instancia);
                    cauld.setData((byte) 0);
                    return;
                }

                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You placed the ingredient at the cauldron !"));
                if (inHand.getAmount() > 1) {
                    ingredientsIn.add(new ItemStack(inHand.getType(), 1, inHand.getData().getData()));
                } else {
                    ingredientsIn.add(inHand);
                }

                cauld.setData((byte) ingredientsIn.size());
                MetaShit.setMetaObject("ingredients", cauld, ingredientsIn);
                if (ev.getPlayer().getItemInHand().getAmount() > 1) {
                    ev.getPlayer().getItemInHand().setAmount(ev.getPlayer().getItemInHand().getAmount() - 1);
                } else {
                    ev.getPlayer().setItemInHand(null);
                }
            } else {

                ///////////////////////////////////////////////////////////////
                // Cauldron is full, making potion or gettin ingredients out //
                ///////////////////////////////////////////////////////////////
                if (ev.getPlayer().getItemInHand().getType() == Material.WRITTEN_BOOK) {
                    CustomItem ci = CustomItem.getCustomItem(inHand);
                    if (ci == null || !(ci instanceof RecipeBook) || ((RecipeBook) ci).getBookType(inHand) != BookTypes.Alchemy) {
                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("Use a Alchemy Recipe Book to take out the potion or a Hopper to get the ingredients back !"));
                        return;
                    }
                    RecipeBook book = (RecipeBook) ci;
                    //////////////////////////////////////////
                    // searching for the recipe in the book //
                    //////////////////////////////////////////
                    List<String> recipesInBook = book.getRecipes(inHand);
                    CustomPotion canMake = null;
                    boolean haveRecipe = false;
                    for (String recipe : recipesInBook) {
                        canMake = CustomPotion.getItem(recipe);
                        ItemStack[] ingredients = canMake.getRecipe();
                        if (!this.hasTheRightIngredients(ingredients, ingredientsIn)) {
                            continue;
                        } else {
                            haveRecipe = true;
                            break;
                        }
                    }
                    if (!haveRecipe) {
                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("You dont have the recipe for a potion with this ingredients..."));
                        return;
                    } else {

                        if (!GeneralUtils.hasItem(ev.getPlayer().getInventory(), Material.GLASS_BOTTLE, 1, (byte) 0)) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need a glass bottle to bottle the potion !"));
                            return;
                        }

                        ///////////////////////
                        // Has made a potion //
                        ///////////////////////
                        double minSkill = canMake.getMinimumSkill();
                        Skills s = NewKom.database.skills.getSkills(ev.getPlayer());
                        double alchemySkill = s.get(SkillEnum.Alchemy).getLvl();
                        double expRatio = canMake.getExpRatio();

                        SkillResult result = SkillFormulas.hasSucess(SkillEnum.Alchemy, ev.getPlayer(), minSkill);
                        if (result == SkillResult.EPIC_FAIL) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("The potion has exploded !"));
                            cauld.removeMetadata("ingredients", NewKom.instancia);
                            cauld.setData((byte) 0);
                            double explosionDamage = minSkill / 5;
                            ev.getPlayer().damage(explosionDamage);
                            ev.getPlayer().getWorld().playEffect(ev.getPlayer().getLocation(), Effect.EXPLOSION_LARGE, 0);
                        } else if (result == SkillResult.FAIL || result == SkillResult.ALMOST_FAILED) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("You failed to create the potion."));
                            cauld.removeMetadata("ingredients", NewKom.instancia);
                            cauld.setData((byte) 0);
                        } else if (result == SkillResult.SUCCESS) {
                            SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Alchemy, minSkill, expRatio);
                            GeneralUtils.removeInventoryItems(ev.getPlayer().getInventory(), Material.GLASS_BOTTLE, 1, (byte) 0);
                            ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You created the potion !"));

                            ItemStack extract = ((PotionExtract) CustomItem.getItem(PotionExtract.class)).createPotionExtract(canMake);

                            ev.getPlayer().getInventory().addItem(extract);

                            cauld.removeMetadata("ingredients", NewKom.instancia);
                            cauld.setData((byte) 0);
                        } else if (result == SkillResult.EPIC_SUCCESS) {
                            SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Alchemy, minSkill,  expRatio);
                            GeneralUtils.removeInventoryItems(ev.getPlayer().getInventory(), Material.GLASS_BOTTLE, 1, (byte) 0);
                            ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You created the potion and saved some resources !"));
                            ev.getPlayer().getInventory().addItem(ingredientsIn.get(SkillFormulas.rnd.nextInt(ingredientsIn.size())));
                            ItemStack extract = ((PotionExtract) CustomItem.getItem(PotionExtract.class)).createPotionExtract(canMake);
                            ev.getPlayer().getInventory().addItem(extract);
                            cauld.removeMetadata("ingredients", NewKom.instancia);
                            cauld.setData((byte) 0);
                        }
                    }

                } else if (ev.getPlayer().getItemInHand().getType() == Material.HOPPER) {
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You took the cauldron ingredients"));
                    ev.getPlayer().getInventory().addItem(ingredientsIn.toArray(new ItemStack[ingredientsIn.size()]));
                    cauld.removeMetadata("ingredients", NewKom.instancia);
                    cauld.setData((byte) 0);
                } else {
                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("Use a Alchemy Recipe Book to take out the potion or a Hopper to get the ingredients back !"));
                    return;
                }

            }

        }
    }

}
