/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Magery;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.RecipeBooks.RecipePage;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author User
 */
public class SpellLoader {
    
    public static HashMap<String, MageSpell> spells = new HashMap<String, MageSpell>();
     public static HashMap<Class, MageSpell> spellsByClass = new HashMap<Class, MageSpell>();
    
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
                if (addon instanceof MageSpell)
                {
                    MageSpell h = (MageSpell) addon;
                    NewKom.log.info("Carregou mage spell " + h.name + " !");
                    spells.put(h.name, h);
                    spellsByClass.put(h.getClass(), h);
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
        if (!name.contains("Magery.spelllist"))
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
        if (MageSpell.class.isAssignableFrom(c))
        {
            MageSpell w = null;
            try
            {

                w = (MageSpell) c.newInstance();
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
