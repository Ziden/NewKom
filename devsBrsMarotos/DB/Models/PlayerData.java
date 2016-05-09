/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB.Models;

/**
 *
 * @author vntgasl
 */
public class PlayerData {
    
    public PlayerData() {
        this.lives = 10;
        this.life = 6; // 3 hearts
    }
    
    public int lives = 0;
    public int life = 0;
    public int karma = 5000; // initial karma
    public int fame = 0;
    
}
