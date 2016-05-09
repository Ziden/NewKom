/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.rankings;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author User
 */
public class RankCache {
    
    public static HashMap<UUID, RankCache> players = new HashMap<UUID, RankCache>();
    
    public HashMap<Estatistica, Integer> ganhos = new HashMap<Estatistica, Integer>();
    public String nome;
    public RankCache(String s) {
        nome = s;
    }
}
