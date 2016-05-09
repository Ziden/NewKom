package devsBrsMarotos.DB.Models;

import java.util.HashMap;
import org.bukkit.enchantments.Enchantment;

/**
 *
 * @author Carlos
 */
public class EnchantmentTable {

    public HashMap<Enchantment, Boolean> enchants;

    public EnchantmentTable() {
        enchants = new HashMap<>();
    }

}
