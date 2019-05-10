
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
        Encode encode = new Encode(); //instantiate the class as an object we can use
        String nameOfOriginalFile = args[0]; //the name of the input file that we get from the commandline
        String nameOfCompressedFile = args[1]; //the name of the output file that we get from the commandline
        FileInputStream input = null; //we initialice the input steam as a null value because we need it later
        String[] huffmanPathTable; // a string array that will hold the 0/1 paths through the tree to the leaf/node with a character
        BitOutputStream bitOutputStream = null; // we instantiate to null because we need it later

        int entries[] = new int[256]; // we make an array for the frequencies of each byte (from 0 to 255)
        for (int i = 0; i < entries.length; i++) {
            entries[i] = 0; //we make sure that all indexes has the value 0
        }
        
        //we have a try catch block because the different steams can throw exceptions
        try {
            input = new FileInputStream(new File(nameOfOriginalFile)); // now we instantiate the input stream with the input file
            while (input.available() > 0) { //while there is anything left to read from the input
                int i = input.read(); //we read a byte
                entries[i]++; //we increment the value at the index that corresponts to the byte
            }
            //we catch different exceptions
        } catch (FileNotFoundException ex) {
            System.out.println("inputStream file exception");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.printStackTrace();
        } finally { //when it is done we close the input. finally is called automatically when the try is done.
            try {
                input.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }
        input = null; // we set the input to null to be 100% sure that we don't use the file anymore. Also because we are going to need to load the file again later

        Element huffmanTree = encode.createHoffmanTree(entries); //we create the huffman tree using our PQHeap from part 1. createHoffmanTree returns the root element where we can get the root node.
        huffmanPathTable = encode.huffmanTable((Node) huffmanTree.getData()); //we create the huffman table. we get the root node by casting the getData() to a node.

        try {
            bitOutputStream = new BitOutputStream(new FileOutputStream(nameOfCompressedFile)); //we instaniate the BitOutputStream that we received as part of the assignment
            for (int i = 0; i < entries.length; i++) {
                bitOutputStream.writeInt(entries[i]); //we write each value from the frequency table as an int to the output file
            }

            input = new FileInputStream(new File(nameOfOriginalFile)); //we read the input file again
            while (input.available() > 0) { // while there is anything left to read from the input 
                int i = input.read(); //we read a byte
                //we look in the huffmanPathTable at the index of the byte we just read
                //for each char at that index, we write that char (either 1 or 0) to the output.
                for (int j = 0; j < huffmanPathTable[i].length(); j++) {
                    char c = huffmanPathTable[i].charAt(j);
                    int o = Integer.parseInt(String.valueOf(c)); //we parse the char as an int, by using the valueOf in String
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
            q.insert(new Element(c[i], new Node(i, c[i]))); //we insert elements with the frequency as key and a new node with the character and frequency
        }
        for (int i = 0; i <= n - 2; i++) {
            Node z = new Node(c[i]); //the new node of the hoffman tree
            Node x = (Node) q.extractMin().getData(); //we create a node x by extractMin.getData(), from q
            z.setLeft(x); //we set the left node of z to be x

            //here we do the same thing with y as we did with x
            Node y = (Node) q.extractMin().getData();
            z.setRight(y);

            //we set the frequency of z to be the sum of x and y'z frequency
            z.setFrequency(x.getFrequency() + y.getFrequency());

            q.insert(new Element(z.getFrequency(), z)); // we insert z back into the q
        }
        return q.extractMin();//we return the root
    }
    
    /**
     * this method is responsible for calling the recursive tree walk of the huffman tree
     * @param root the root node of the huffman tree
     * @return an array of strings for the path to each character in the huffman tree
     */
    public String[] huffmanTable(Node root) {
        String huffmanCodes[] = new String[256]; //a string array with an index for each byte
        StringBuilder sb = new StringBuilder(); //a stringBuilder for making the paths
        return huffmanWalk(root, huffmanCodes, sb); //return the call to the recursive tree walk
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
