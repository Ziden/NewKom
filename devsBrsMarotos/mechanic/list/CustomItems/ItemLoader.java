package devsBrsMarotos.mechanic.list.CustomItems;

import devsBrsMarotos.NewKom;
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
public class ItemLoader
{

    public static HashMap<String, CustomItem> customItems = new HashMap<String, CustomItem>();
     public static HashMap<Class, CustomItem> customItemsClass = new HashMap<Class, CustomItem>();

    
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
                if (addon instanceof CustomItem)
                {
                    CustomItem h = (CustomItem) addon;
                    h.onEnable();
                    NewKom.log.info("Carregou custom item " + h.name + " !");
                    customItems.put(h.name, h);
                    customItemsClass.put(h.getClass(), h);
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
        if (!name.contains("CustomItems.list"))
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
        if (CustomItem.class.isAssignableFrom(c))
        {
            CustomItem w = null;
            try
            {

                w = (CustomItem) c.newInstance();
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
