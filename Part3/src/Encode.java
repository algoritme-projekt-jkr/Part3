
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Robin
 */
public class Encode {

    public static void main(String[] args) {
        String nameOfOriginalFile = args[0];
        String nameOfCompressedFile = args[1];
        FileInputStream input = null;
        FileOutputStream output = null;

        int entries[] = new int[256];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = 0;
        }

        try {
            input = new FileInputStream(new File(nameOfOriginalFile));
            int i;
            while ((i = input.read()) != -1) {
                entries[i]++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("inputStream file exception");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }

        System.out.println(Arrays.toString(entries));
    }

    
     public Element createHoffmanTree(int[] c){
         int n = c.length;
         PQ q = new PQHeap(n);
         for (int i = 0; i < c.length; i++) {
             q.insert(new Element(i, c[i]));
         }
         for (int i = 0; i < n-2; i++) { //måske <=   -----------------------------------kig her
             Node z = new  Node(c[i]);
             Node x = new Node((int)q.extractMin().getData());
             z.setLeft(x);
             
             Node y = new Node((int)q.extractMin().getData());
             z.setLeft(y);
             
             z.setFrequency(x.getFrequency() + y.getFrequency());
             
             q.insert(new Element(i, z));
             
         }
         
         return q.extractMin();
     }
    
}
