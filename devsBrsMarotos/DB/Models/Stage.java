/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB.Models;

import java.util.HashSet;

/**
 *
 * @author vntgasl
 */
public class Stage {
    
    public Stage() {
        stagesCompleted = new HashSet<String>();
    }
    
    public HashSet<String> stagesCompleted = null;
   
    public static enum PredefinedStages {
        GOT_FIRST_HEART,
        SEEN_INTRO;
    }
    
}
