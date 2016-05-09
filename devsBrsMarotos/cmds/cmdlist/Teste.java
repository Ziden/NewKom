package devsBrsMarotos.cmds.cmdlist;

import devsBrsMarotos.cmds.Comando;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Carlos
 */
public class Teste extends Comando{

    
    public Teste(){
        super("teste",CommandType.OP);
    }
    @Override
    public void usouComando(CommandSender cs, String[] args) {
        Player p = (Player)cs;
        p.getInventory().setHelmet(p.getItemInHand());
        
    }
  
    
    
}
