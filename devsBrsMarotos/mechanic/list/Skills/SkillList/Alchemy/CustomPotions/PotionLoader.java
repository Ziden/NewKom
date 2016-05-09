package devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions;

import devsBrsMarotos.mechanic.list.CustomItems.*;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.RecipeBooks.RecipePage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author vntgasl
 */
public class PotionLoader
{

    public static HashMap<String, CustomPotion> customItems = new HashMap<String, CustomPotion>();
    public static HashMap<Class, CustomPotion> customItemsClass = new HashMap<Class, CustomPotion>();
 
    public static void load()
    {
        File f = new File(NewKom.class.getProtectionDomain().getCodeSource().getLocation().getFile().replaceAll("%20", " ")).getAbsoluteFile();
        JarFile jf;
        try
        {
            jf = new JarFile(f);
        } catch (IOException ex)
        {
            ex.printStackTrace();
            return;
        }
        Enumeration en = jf.entries();

        while (en.hasMoreElements())
        {

            Object entry = en.nextElement();
            Object addon = getCarta(entry);

            if (addon != null)
            {
                if (addon instanceof CustomPotion)
                {
                    CustomPotion h = (CustomPotion) addon;
                    NewKom.log.info("Carregou custom potion " + h.name + " !");
                    customItems.put(h.name, h);
                    customItemsClass.put(h.getClass(), h);
                    RecipePage.recipes.set(h.name, h.generateRecipe());
                }
            }
        }

    }

    private static Object getCarta(Object ne)
    {
        JarEntry entry = (JarEntry) ne;
        String name = entry.getName();
        name = name.replaceAll("/", ".");
        if (!name.endsWith(".class"))
        {
            return null;
        }
        name = name.replace(".class", "");
        if (!name.contains("CustomPotions.potionlist"))
        {
            return null;
        }
        Class c;
        try
        {
            c = Class.forName(name);
        } catch (ClassNotFoundException ex)
        {

            ex.printStackTrace();
            return null;
        }
        if (CustomPotion.class.isAssignableFrom(c))
        {
            CustomPotion w = null;
            try
            {

                w = (CustomPotion) c.newInstance();
                return w;

            } catch (Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
        }

        return null;
    }

}
