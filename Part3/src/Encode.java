
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
        Encode encode = new Encode();
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
        Element test = encode.createHoffmanTree(entries);
        System.out.println("test key" + test.getKey() + "    data " + ((Node)test.getData()).getFrequency());
        
    }

    public Element createHoffmanTree(int[] c) {
        int tal= 0;
        for (int i = 0; i < c.length; i++) {
            tal += c[i];
        }
        System.out.println("tal: " + tal);
        int n = c.length;
        PQ q = new PQHeap(n);
        for (int i = 0; i < c.length; i++) {
            q.insert(new Element(c[i], null)); //<--------------------------måske: her skal null være en new node med frekvensen c[i]?????????????????? node skal både have bogstavet og frequency
        }
        for (int i = 0; i < n - 2; i++) { //måske <=   -----------------------------------kig her
            Node z = new Node(c[i]); //the new node of the hoffman tree
            Node x = new Node(q.extractMin().getKey()); //we create a node x by extractMin.getData(), from q, to get the frequency
            z.setLeft(x); //we set the left node of z to be x

            //here we do the same thing with y as we did with x
            Node y = new Node(q.extractMin().getKey());
            z.setLeft(y);

            //we set the frequency of z to be the sum of x and y'z frequency
            z.setFrequency(x.getFrequency() + y.getFrequency());

            q.insert(new Element(i, z)); // we insert z back into the q

        }

        return q.extractMin();
    }

}
