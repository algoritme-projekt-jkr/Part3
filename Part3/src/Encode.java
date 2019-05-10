
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Robin Lausten Petersen - ropet17
 * @author Jeppe Enevold Jensen - jeppj17
 * @author Kim Christensen - kichr17
 */
public class Encode {

    public static void main(String[] args) {
        //instantiate the class as an object we can use
        Encode encode = new Encode(); 
        //the name of the input file that we get from the commandline
        String nameOfOriginalFile = args[0]; 
        //the name of the output file that we get from the commandline
        String nameOfCompressedFile = args[1]; 
        //we initialice the input steam as a null value because we need it later
        FileInputStream input = null; 
        // a string array that will hold the 0/1 paths through the tree
        // to the leaf/node with a character
        String[] huffmanPathTable; 
        // we instantiate to null because we need it later
        BitOutputStream bitOutputStream = null; 

        // we make an array for the frequencies of each byte (from 0 to 255)
        int entries[] = new int[256]; 
        for (int i = 0; i < entries.length; i++) {
            entries[i] = 0; //we make sure that all indexes has the value 0
        }
        
   //we have a try catch block because the different steams can throw exceptions
        try {
            // now we instantiate the input stream with the input file
            input = new FileInputStream(new File(nameOfOriginalFile)); 
            //while there is anything left to read from the input
            while (input.available() > 0) { 
                int i = input.read(); //we read a byte
                //we increment the value at the index that corresponts to the byte
                entries[i]++; 
            }
            //we catch different exceptions
        } catch (FileNotFoundException ex) {
            System.out.println("inputStream file exception");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.printStackTrace();
        //when it is done we close the input. 
        //finally is called automatically when the try is done.
        } finally { 
            try {
                input.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }
        // we set the input to null to be 100% sure that we don't use the file anymore.
        // Also because we are going to need to load the file again later
        input = null; 

        // we create the huffman tree using our PQHeap from part 1. 
    //createHoffmanTree returns the root element where we can get the root node.
        Element huffmanTree = encode.createHoffmanTree(entries); 
        //we create the huffman table. we get the root node by casting the getData() to a node.
        huffmanPathTable = encode.huffmanTable((Node) huffmanTree.getData()); 

        try {
            //we instaniate the BitOutputStream that we received as part of the assignment
            bitOutputStream = new BitOutputStream(new FileOutputStream(nameOfCompressedFile)); 
            for (int i = 0; i < entries.length; i++) {
                //we write each value from the frequency table as an int to the output file
                bitOutputStream.writeInt(entries[i]); 
            }
            
            //we read the input file again
            input = new FileInputStream(new File(nameOfOriginalFile)); 
            // while there is anything left to read from the input 
            while (input.available() > 0) { 
                int i = input.read(); //we read a byte
            //we look in the huffmanPathTable at the index of the byte we just read
            //for each char at that index, we write that char (either 1 or 0) to the output.
                for (int j = 0; j < huffmanPathTable[i].length(); j++) {
                    char c = huffmanPathTable[i].charAt(j);
                    //we parse the char as an int, by using the valueOf in String
                    int o = Integer.parseInt(String.valueOf(c)); 
                    bitOutputStream.writeBit(o); //we write the bit to the output
                }

            }
            //we catch exceptions and in "finally" we close the streams
        } catch (FileNotFoundException ex) {
            System.out.println("inputStream file exception");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.printStackTrace();
        } finally {
            try {
                input.close();
                bitOutputStream.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }

    }
    
    /**
     * this method creates the huffman tree
     * @param c the frequency array
     * @return the root element
     */
    public Element createHoffmanTree(int[] c) {
        int n = c.length; //the length of c
        PQ q = new PQHeap(n); //we instantiate our queue/PQHeap with the length n
        for (int i = 0; i < c.length; i++) {
            //we insert elements with the frequency as key
            //and a new node with the character and frequency
            q.insert(new Element(c[i], new Node(i, c[i]))); 
        }
        for (int i = 0; i <= n - 2; i++) {
            Node z = new Node(c[i]); //the new node of the hoffman tree
            //we create a node x by extractMin.getData(), from q
            Node x = (Node) q.extractMin().getData(); 
            z.setLeft(x); //we set the left node of z to be x

            //here we do the same thing with y as we did with x
            Node y = (Node) q.extractMin().getData();
            z.setRight(y);

            //we set the frequency of z to be the sum of x and y'z frequency
            z.setFrequency(x.getFrequency() + y.getFrequency());
            
            // we insert z back into the q
            q.insert(new Element(z.getFrequency(), z)); 
        }
        return q.extractMin();//we return the root
    }
    
    /**
     * this method is responsible for calling the recursive tree walk of the huffman tree
     * @param root the root node of the huffman tree
     * @return an array of strings for the path to each character in the huffman tree
     */
    public String[] huffmanTable(Node root) {
        //a string array with an index for each byte
        String huffmanCodes[] = new String[256]; 
        //a stringBuilder for making the paths
        StringBuilder sb = new StringBuilder(); 
        //return the call to the recursive tree walk
        return huffmanWalk(root, huffmanCodes, sb); 
    }
    
    /**
     * a recursive method to find all the paths to the characters
     * @param node the node to look at
     * @param a the string array with the paths
     * @param sb the stringBuilder
     * @return the string array with the paths 
     */
    private String[] huffmanWalk(Node node, String[] a, StringBuilder sb) {
        if (node != null) { //if we are looking at a node
            //recursive call on the left child where we append 0 to the path
            huffmanWalk(node.getLeft(), a, sb.append("0")); 
            sb.deleteCharAt(sb.length() - 1); //delete the last path bit

            //if the node is a leaf with a character
            if (node.getCharacter() != -1) {
                //write the path at the character's index
                a[node.getCharacter()] = sb.toString(); 
            }
            
            //recursive call on the right child where we append 1 to the path
            huffmanWalk(node.getRight(), a, sb.append("1"));
            sb.deleteCharAt(sb.length() - 1); //delete the last path bit
        }
        return a; // return the path array
    }

}
