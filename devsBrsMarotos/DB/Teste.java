/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

/**
 *
 * @author User
 */
public class Teste {
    
    public static void main(String [] args) {
        String teste = "ASD ASD ASD ASD .... ASD ASD... ASDAS ...";
        //teste = teste.replaceAll(".", "");
       teste = teste.replaceAll("\\.", ""); 
       teste = teste.replaceAll("\\ ", ""); 
        System.out.println(teste);
    }
    
}
