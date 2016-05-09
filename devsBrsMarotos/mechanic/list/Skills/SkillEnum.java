/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills;

import devsBrsMarotos.DB.Models.Skills.SkillInfo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 */
public enum SkillEnum {

    // works skills
    // they should do something good on dungeons
    Lumberjacking(new String[]{"Chop Better Wood", "Increase Axe Damage", "Epic Hax"}, Material.IRON_AXE, 1), // increase axe damage, harvest wood
    Farming(new String[]{"Plant and Harvest Food", "Increase Hoe Damage"}, Material.IRON_HOE, 1), // increase hoe damage, harvest plants
    Fishing(new String[]{"Fish !", "Increase Fishing Pole Damage"}, Material.FISHING_ROD, 1.6), // increase fishing rod damage, fish
    Minning(new String[]{"Mine Ores", "Increase PickAxe Damage"}, Material.IRON_PICKAXE, 0.9), // mining thinigs from the earth
    Blacksmithing(new String[]{"Craft Equipment", "Smelt Ores", "Increase Spade Damage"}, Material.IRON_SPADE, 0.6), // crafting armor, smelting ingots
    Alchemy(new String[]{"Brew Potions", "Toss TnT"}, Material.POTION, 0.7), // brewing potions
    Engineering(new String[]{"Use and Craft Redstone", "Increase Gun Damage"}, Material.REDSTONE, 1), // crafting redstone stuff, placing redstone stuff, detecting lock types.
    Taming(new String[]{"Tame Wild Animals", "Transport Animals in Eggs", "Increase Egg Damage"}, Material.EGG, 1), // taming wild animals and horses and collecting items from them
    Bowcrafting(new String[]{"Craft Better Bows", "Craft Arrows"}, Material.ARROW, 1), // crafting bows and arrows
    Escavation(new String[]{"Dig and gather resources", "Increase Spade Weapons Damage"}, Material.DIRT, 1.8),
    Enchanting(new String[]{"Enchant items"}, Material.ENCHANTMENT_TABLE, 1),
    // random
    Poisoning(new String[]{"Poison Enemies", "Increase Poison Duration"}, Material.POISONOUS_POTATO, 0.5), // poison weapons and people
    Lockpicking(new String[]{"Lockpick dungeon chests", "Open guild chests"}, Material.CHEST, 1), // lockpick locked chests in dungeons and people houses
    Hiding(new String[]{"Hide in shadows"}, Material.GLASS, 2), // can become invisible (iddle)
    Stealth(new String[]{"Move while hiding"}, Material.BARRIER, 1), // can move invisible
    DetectHidden(new String[]{"Detect Hidden Players", "Detect Hidden Treasures and Ores"}, Material.DETECTOR_RAIL, 1), // detect invisible players, and invisible items in dungeons

    // combat skills
    Swordsmanship(new String[]{"Increase Sword Damage", "Increase Block Damage Reduction"}, Material.DIAMOND_SWORD, 1), // swords
    MaceFighting(new String[]{"Increase Spades Damage", "Stunning Strikes"}, Material.DIAMOND_SPADE, 1), // spades
    Fencing(new String[]{"Increase Hoe Damage", "Bleeding Strikes"}, Material.DIAMOND_HOE, 1), // hoes
    AxeFighting(new String[]{"Increase Axe Damage", "Critical Hits"}, Material.DIAMOND_AXE, 1), // axes
    Parryng(new String[]{"Increase Chance to Block with Iron Doors"}, Material.IRON_DOOR, 1), // blocking with a shield
    Tactics(new String[]{"Increase All Physical Damage"}, Material.WOOD_SWORD, 1), // bonus physical damage
    Dodge(new String[]{"Increase Dodge Chance"}, Material.STRING, 1), // dodge attacks
    Resistance(new String[]{"Reduces Physical Damage"}, Material.IRON_CHESTPLATE, 1),
    MagicResistance(new String[]{"Reduces Magical Damage"}, Material.GOLD_CHESTPLATE, 1),
    Healing(new String[]{"Use Bandages to heal"}, Material.PAPER, 1), // band-aids
    Chivalry(new String[]{"Use Paladin spells from Paladin Spellbook"}, Material.BOOK_AND_QUILL, 1), // paladin skills
    Magery(new String[]{"Use Mage Spells from Mage Spellbook"}, Material.BOOK, 1), // mage skills
    Spiritism(new String[]{"Increase Beneficial Spells Effects"}, Material.NETHER_STAR, 1), // + mage spells heals
    Elementalism(new String[]{"Increase Maleficial Spells Damage"}, Material.FIRE, 1), // + mage spells damage
    Wrestling(new String[]{"Increase Bare Handed Damage and Dodge Chance"}, Material.FEATHER, 1), // bare handed and non weapon items damage
    Meditation(new String[]{"Increase Mana Regeneration"}, Material.IRON_AXE, 1), // increase mana regen
    Focusing(new String[]{"Use Monk skills"}, Material.BEACON, 1), // monk skills
    Archery(new String[]{"Increase Bow Damage"}, Material.BOW, 1);         // bow damage

    public String[] desc;
    public Material icon;
    public double expRatio = 1; // increase to make easy to level

    public static DecimalFormat df = new DecimalFormat("#.#");

    public ItemStack getItem(SkillInfo info) {
        ItemStack ss = new ItemStack(icon);
        ItemMeta m = ss.getItemMeta();
        m.setDisplayName(ChatColor.GREEN + this.name());
        List<String> lore = new ArrayList<String>();
        for (String d : desc) {
            lore.add(ChatColor.AQUA + d);
        }
        lore.add(ChatColor.GOLD + "Level: §e" + info.getLvl());
        lore.add(ChatColor.GREEN + "Experience: §e" + info.getXp());
        lore.add(ChatColor.BLUE + "Total Experience: §e" + info.getXpTotal());
        m.setLore(lore);
        ss.setItemMeta(m);
        return ss;
    }

    private SkillEnum(String[] desc, Material icon, double ratio) {
        this.desc = desc;
        this.icon = icon;
        this.expRatio = ratio;
    }

}
