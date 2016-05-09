/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
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
public class CmdRecipes extends SubCmd {
    
    public CmdRecipes() {
        super("recipes", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String bookTypes = "";
        for(BookTypes type : BookTypes.values())
            bookTypes += ChatColor.GREEN+"|"+ChatColor.YELLOW+type.name();
       if(args.length!=2) {
           cs.sendMessage("Use /kom recipes "+bookTypes);
       } else {
           String bookType = args[1];
           try {
               BookTypes type = BookTypes.valueOf(bookType);
               if(type==BookTypes.Alchemy) {
                   CustomPotion.showRecipes(((Player)cs));
               } else if(type==BookTypes.Magery) {
                   MageSpell.showRecipes(((Player)cs));
               } else {
                   cs.sendMessage("This book have no known recipes...yet..");
               }
           } catch(Exception e) {
               cs.sendMessage("Use /kom recipes "+bookTypes);
               e.printStackTrace();
               return;
           }
       }
    }
}
