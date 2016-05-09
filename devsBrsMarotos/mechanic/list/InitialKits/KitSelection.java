/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.InitialKits;

import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 *
 * @author User
 */
public class KitSelection extends Mecanica {
   
   
   public List<Kit> kits = new ArrayList<Kit>(); 
    
   public void createKits() {
       
       ///// ALCHEMIST //////
       Kit alchemist = new Kit();
       alchemist.desc = new String[] {
           "Brew potions to aid friends and harm foes",
           "Use hoes to deal damage",
           "Master the arts of poisoning"
       };
       Potion heal = new Potion(PotionType.INSTANT_HEAL);
       Potion str = new Potion(PotionType.STRENGTH);
       alchemist.initialItems.add(heal.toItemStack(1));
       alchemist.initialItems.add(str.toItemStack(1));
       alchemist.initialItems.add(new ItemStack(Material.POTION));
       alchemist.initialItems.add(new ItemStack(Material.POTION));
       alchemist.initialItems.add(new ItemStack(Material.WOOD_HOE));
       alchemist.initialItems.add(new ItemStack(Material.BREWING_STAND_ITEM));
       alchemist.initialItems.add(new ItemStack(Material.NETHER_WARTS,2));
       alchemist.skills.put(SkillEnum.Alchemy, 50d);
       alchemist.skills.put(SkillEnum.Farming, 30d);
       alchemist.skills.put(SkillEnum.Escavation, 30d);
       alchemist.skills.put(SkillEnum.Poisoning, 20d);
       alchemist.skills.put(SkillEnum.Hiding, 20d);
       
       Kit warrior = new Kit();
       warrior.desc = new String[] {
           "Master of combat, deal damage and take damage",
           "Use leaves to heal yourself",
           "Use Swords and Bows to deal damage"
       };
       warrior.initialItems.add(new ItemStack(Material.LEAVES, 32));
       warrior.initialItems.add(new ItemStack(Material.WOOD_SWORD));
       warrior.initialItems.add(new ItemStack(Material.BOW));
       warrior.initialItems.add(new ItemStack(Material.ARROW, 64));
       warrior.skills.put(SkillEnum.Swordsmanship, 50d);
       warrior.skills.put(SkillEnum.Archery, 30d);
       warrior.skills.put(SkillEnum.Healing, 30d);
       warrior.skills.put(SkillEnum.Dodge, 20d);
       warrior.skills.put(SkillEnum.Tactics, 20d);
       
       Kit paladin = new Kit();
       paladin.desc = new String[] {
           "Defensive combatant, use skills to protect",
           "Use leaves to heal yourself",
           "Parry attacks with shields"
       };
       paladin.initialItems.add(new ItemStack(Material.LEAVES, 32));
       paladin.initialItems.add(new ItemStack(Material.WOOD_SWORD));
       paladin.initialItems.add(new ItemStack(Material.IRON_DOOR));
       paladin.skills.put(SkillEnum.Swordsmanship, 50d);
       paladin.skills.put(SkillEnum.Parryng, 30d);
       paladin.skills.put(SkillEnum.Healing, 30d);
       paladin.skills.put(SkillEnum.Resistance, 20d);
       paladin.skills.put(SkillEnum.Chivalry, 20d);
       
       Kit blacksmith = new Kit();
       blacksmith.desc = new String[] {
           "Mine and Craft Equipent",
           "Deal damage with Spades",
       };
       blacksmith.initialItems.add(new ItemStack(Material.WOOD_PICKAXE));
       blacksmith.initialItems.add(new ItemStack(Material.WOOD_SPADE));
       blacksmith.initialItems.add(new ItemStack(Material.COAL, 6));
       blacksmith.skills.put(SkillEnum.Blacksmithing, 50d);
       blacksmith.skills.put(SkillEnum.Minning, 30d);
       blacksmith.skills.put(SkillEnum.Escavation, 30d);
       blacksmith.skills.put(SkillEnum.MaceFighting, 20d);
       blacksmith.skills.put(SkillEnum.Engineering, 20d);
       
       
       Kit lumberjack = new Kit();
       lumberjack.desc = new String[] {
           "Deal heavy damage with axes",
           "Gather wood and craft wood things",
       };
       lumberjack.initialItems.add(new ItemStack(Material.WOOD_AXE));
       lumberjack.skills.put(SkillEnum.Lumberjacking, 50d);
       lumberjack.skills.put(SkillEnum.AxeFighting, 30d);
       lumberjack.skills.put(SkillEnum.Tactics, 30d);
     
       //lumberjack.skills.put(SkillEnum.Carpentry, 20d);
       lumberjack.skills.put(SkillEnum.Bowcrafting, 20d);
       
       
       Kit mage = new Kit();
       mage.desc = new String[] {
           "Deal heavy damage with axes",
           "Gather wood and craft wood things",
       };
       mage.initialItems.add(new ItemStack(Material.WOOD_AXE));
       // need to finish spellbooks and spell system...
       mage.skills.put(SkillEnum.Magery, 50d);
       mage.skills.put(SkillEnum.MagicResistance, 30d);
       mage.skills.put(SkillEnum.Elementalism, 30d);
       mage.skills.put(SkillEnum.Meditation, 20d);
       mage.skills.put(SkillEnum.Poisoning, 20d);
       
       //////// TO-DO FINISH THIS SHIT /////////
   }

    @Override
    public String getName() {
       return "Kit Selection";
    }
    
}
