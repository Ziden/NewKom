/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.CustomCraftRecipes.RecipeLoader;
import devsBrsMarotos.mechanic.list.CustomItems.list.RecipeBook;
import devsBrsMarotos.mechanic.list.Harvesting.BlockInfoDisplay;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.PotionLoader;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Magery.MageSpell;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class CmdCraftRecipes extends SubCmd {
    
    public CmdCraftRecipes() {
        super("craftrecipes", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
       RecipeLoader.showRecipes((Player)cs);
    }
}
