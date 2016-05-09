package devsBrsMarotos.mechanic.list.CustomCraftRecipes;

import devsBrsMarotos.NewKom;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

/**
 *
 * @author vntgasl
 */
public class RecipeLoader {

    public static HashMap<String, CustomRecipe> craftRecipes = new HashMap<String, CustomRecipe>();
    public static HashMap<Class, CustomRecipe> craftRecipesByClass = new HashMap<Class, CustomRecipe>();

    public static void showRecipes(Player p) {
        Inventory i = Bukkit.createInventory(p, 54, "Custom Recipes");
        for(CustomRecipe recipe : craftRecipes.values()) {
            i.addItem(recipe.result);
        }
        p.openInventory(i);
    }
    
    private static int letterIndex = 0;
    private static char [] letters = {'A','B','C','D','E','F','G','H','I','F','G'};
    
    public static void load() {
        File f = new File(NewKom.class.getProtectionDomain().getCodeSource().getLocation().getFile().replaceAll("%20", " ")).getAbsoluteFile();
        JarFile jf;
        try {
            jf = new JarFile(f);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        Enumeration en = jf.entries();

        while (en.hasMoreElements()) {

            Object entry = en.nextElement();
            Object addon = getCarta(entry);

            if (addon != null) {
                if (addon instanceof CustomRecipe) {
                    CustomRecipe h = (CustomRecipe) addon;
                    NewKom.log.info("Carregou craft recipe " + h.name + " !");
                    craftRecipes.put(h.name, h);
                    craftRecipesByClass.put(h.getClass(), h);
                    ShapedRecipe recipe = new ShapedRecipe(h.result);
                    List<String> lines = new ArrayList<String>();
                    HashMap<Character, MaterialData> ingredKeys = new HashMap();
                    lines.add("");
                    lines.add("");
                    lines.add("");
                    ItemStack [][] craftRecipe = h.getRecipe();
                    for(int line = 0 ; line < craftRecipe.length ; line++) {
                        ItemStack [] itemLine = craftRecipe[line];
                        for(ItemStack itemInLine : itemLine) {
                            String craftLine = lines.get(line);
                            char itemLetter = getLetter();
                            if(itemInLine==null)
                                craftLine += " ";
                            else {
                                craftLine += itemLetter;
                                ingredKeys.put(itemLetter, itemInLine.getData());
                            }
                            lines.set(line, craftLine);
                        }
                    }
               
                    recipe.shape(lines.get(0),lines.get(1),lines.get(2));
                    for(Character chara : ingredKeys.keySet()) {
                        recipe.setIngredient(chara, ingredKeys.get(chara));
                    }
                    Bukkit.addRecipe(recipe);
                   // 
                    
                    
                }
            }
            letterIndex = 0;
        }

    }
    
    private static char getLetter() {
        char letter = letters[letterIndex];
        letterIndex++;
        return letter;
    }

    private static Object getCarta(Object ne) {
        JarEntry entry = (JarEntry) ne;
        String name = entry.getName();
        name = name.replaceAll("/", ".");
        if (!name.endsWith(".class")) {
            return null;
        }
        name = name.replace(".class", "");
        if (!name.contains("CustomCraftRecipes.list")) {
            return null;
        }
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException ex) {

            ex.printStackTrace();
            return null;
        }
        if (CustomRecipe.class.isAssignableFrom(c)) {
            CustomRecipe w = null;
            try {

                w = (CustomRecipe) c.newInstance();
                return w;

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return null;
    }

}
