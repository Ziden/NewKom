/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author root
 */
public class TesteArquivo {

    public static void main(String args[]) {
       Calendar c = Calendar.getInstance();
       System.out.println(c.get(Calendar.MONTH));
    }
}

