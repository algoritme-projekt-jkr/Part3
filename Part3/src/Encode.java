
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
            while (input.available() > 0) { //if there is anything left to read from the input
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
        input = null; // we set the input to null to be 100% sure that we don't use the file anymore. this could be omitted because we close the input.

        Element huffmanTree = encode.createHoffmanTree(entries); //we create the huffman tree using our PQHeap from part 1. createHoffmanTree returns the root element where we can get the root node.
        huffmanPathTable = encode.huffmanTable((Node) huffmanTree.getData()); //we create the huffman table. we get the root node by casting the getData() to a node.

        try {
            bitOutputStream = new BitOutputStream(new FileOutputStream(nameOfCompressedFile));
            for (int i = 0; i < entries.length; i++) {
                bitOutputStream.writeInt(entries[i]);
            }

            input = new FileInputStream(new File(nameOfOriginalFile));
            while (input.available() > 0) {
                int i = input.read();
                for (int j = 0; j < huffmanPathTable[i].length(); j++) {
                    char c = huffmanPathTable[i].charAt(j);
                    int o = Integer.parseInt(String.valueOf(c));
                    bitOutputStream.writeBit(o);
                }

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
                bitOutputStream.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }

    }

    public Element createHoffmanTree(int[] c) {
        int n = c.length;
        PQ q = new PQHeap(n);
        for (int i = 0; i < c.length; i++) {
            q.insert(new Element(c[i], new Node(i, c[i])));
        }
        for (int i = 0; i <= n - 2; i++) {
            Node z = new Node(c[i]); //the new node of the hoffman tree
            Node x = (Node) q.extractMin().getData(); //we create a node x by extractMin.getData(), from q, to get the frequency
            z.setLeft(x); //we set the left node of z to be x

            //here we do the same thing with y as we did with x
            Node y = (Node) q.extractMin().getData();
            z.setRight(y);

            //we set the frequency of z to be the sum of x and y'z frequency
            z.setFrequency(x.getFrequency() + y.getFrequency());

            q.insert(new Element(z.getFrequency(), z)); // we insert z back into the q

        }

        return q.extractMin();
    }

    public String[] huffmanTable(Node root) {
        String huffmanCodes[] = new String[256];
        StringBuilder sb = new StringBuilder();
        return huffmanWalk(root, huffmanCodes, sb);
    }

    private String[] huffmanWalk(Node node, String[] a, StringBuilder sb) {
        if (node != null) {
            huffmanWalk(node.getLeft(), a, sb.append("0"));
            sb.deleteCharAt(sb.length() - 1);

            if (node.getCharacter() != -1) {
                a[node.getCharacter()] = sb.toString();
            }

            huffmanWalk(node.getRight(), a, sb.append("1"));
            sb.deleteCharAt(sb.length() - 1);
        }
        return a;
    }

}
