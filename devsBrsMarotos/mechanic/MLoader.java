package devsBrsMarotos.mechanic;

import devsBrsMarotos.NewKom;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;

public class MLoader {

    private static List<Mecanica> loaded = new ArrayList<Mecanica>();

    public static void disable() {
        for (Mecanica m : loaded) {
            m.onDisable();
        }
    }

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
            String mecanicas = "";
            if (addon != null) {
                if (addon instanceof Mecanica) {
                    Mecanica h = (Mecanica) addon;
                    h.plugin = NewKom.instancia;
                    try {
                        h.onEnable();
                    } catch (Exception ex) {
                        NewKom.log.info("Erro ao carregar Mecanica: " + h.getName());
                        ex.printStackTrace();
                    }
                    mecanicas = mecanicas + h.getName() + ",";
                    if (mecanicas.split(",").length >= 15) {
                        NewKom.log.info("Mecanicas Carregadas: " + mecanicas);
                        mecanicas = "";
                    }
                    NewKom.instancia.getServer().getPluginManager().registerEvents(h, NewKom.instancia);
                    loaded.add(h);
                }
            }
        }

    }

    private static Object getCarta(Object ne) {
        JarEntry entry = (JarEntry) ne;
        String name = entry.getName();
        name = name.replaceAll("/", ".");
        if (!name.endsWith(".class")) {
            return null;
        }
        name = name.replace(".class", "");
        if (!name.contains("mechanic.list")) {
            return null;
        }
        Class c;
        try {
            c = Class.forName(name);
        } catch (ClassNotFoundException ex) {

            ex.printStackTrace();
            return null;
        }
        if (Mecanica.class.isAssignableFrom(c)) {
            Mecanica w = null;
            try {

                w = (Mecanica) c.newInstance();
                return w;

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
