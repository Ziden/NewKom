/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.KarmaFameCriminal;

import devsBrsMarotos.DB.Models.PlayerData;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.util.GeneralUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class KarmaFameTables {

    /*
     Karma and Fame goes from 0 to 10000
     It will determine your title
    
    Start Karma: 5000
    Start Fame: 0
    
     */
    private static String[][] karmaFameTable = new String[][]{
        /*
        
        + Fame ---->
        + Karma 
          ^
          |
          |
        
        */
        {"Trustworthy", "Estimable", "Great", "Glorious", "Glorious Hero"},
        {"Honest", "Commendable", "Famed", "Illustrious", "Illustrious Hero"},
        {"Good", "Honorable", "Admirable", "Noble", "Noble Hero"},
        {"Kind", "Respectable", "Proper", "Eminent", "Eminent Hero"},
        {"Fair", "Upstanding", "Reputable", "Distinguished", "Distinguished Hero"},
        {"", "Notable", "Prominent", "Renowned", "Hero"},
        {"Rude", "Disreputable", "Notorious", "Infamous", "Dishonored Hero"},
        {"Unsavory", "Dishonorable", "Ignoble", "Sinister", "Sinister Hero"},
        {"Scoundrel", "Malicious", "Vile", "Villainous", "Dark Hero"},
        {"Despicable", "Dastardly", "Wicked", "Evil", "Evil Hero"},
        {"Outcast", "Wretched", "Nefarious", "Dread", "Dread Lord/Lady"}
            
    };
    
    public static String getTitle(int karma, int fame) {
        int fameColumn = fame / 2000; // max 5
        int karmaLine = (karma * 11) / 10000;
        return karmaFameTable[fameColumn][karmaLine];
    }
    
    public static void updateTitle(Player p) {
        PlayerData data = NewKom.database.playerData.getPlayerData(p);
        String title = getTitle(data.karma, data.fame);
        if(!title.equalsIgnoreCase(""))
            title = L.m(title);
        ChatColor color = ChatColor.WHITE;
        if(Criminal.isCriminal(p))
            color = ChatColor.GRAY;
        GeneralUtils.setPrefix(p, title+" ");
        
    }
    
    
}
