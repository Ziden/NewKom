/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Integration;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.events.AllyClanAddEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.AllyClanRemoveEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.DisbandClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerJoinedClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.PlayerKickedClanEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.RivalClanAddEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.RivalClanRemoveEvent;
import net.sacredlabyrinth.phaed.simpleclans.events.CreateClanEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

/**
 *
 * @author User
 */
public class KomSimpleClans extends Mecanica{

    @Override
    public String getName() {
        return "SimpleClans Events";
    }
    
    @EventHandler
    public void createClan(CreateClanEvent ev) {
        for(ClanPlayer cp : ev.getClan().getLeaders()) {
            if(cp!=null && cp.toPlayer()!=null) {
                cp.toPlayer().sendMessage(NewKom.TAG+ChatColor.GREEN+"Use "+ChatColor.YELLOW+"/land to claim your guild lands on Free Zones !");
            }
        }     
    }
    
    
    
    
}
