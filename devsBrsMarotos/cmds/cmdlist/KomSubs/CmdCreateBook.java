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
public class CmdCreateBook extends SubCmd {
    
    public CmdCreateBook() {
        super("createbook", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String bookTypes = "";
        for(BookTypes type : BookTypes.values())
            bookTypes += ChatColor.GREEN+"|"+ChatColor.YELLOW+type.name();
       if(args.length!=2) {
           cs.sendMessage("Use /kom createbook "+bookTypes);
       } else {
           String bookType = args[1];
           try {
               BookTypes type = BookTypes.valueOf(bookType);
               ItemStack book = RecipeBook.createBook(type);
               ((Player)cs).getInventory().addItem(book);
               cs.sendMessage("Created !");
           } catch(Exception e) {
               cs.sendMessage("Use /kom createbook "+bookTypes);
               e.printStackTrace();
               return;
           }
       }
    }
}
