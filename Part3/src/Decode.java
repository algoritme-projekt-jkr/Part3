
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
public class Decode {

    public static void main(String[] args) {
        String nameOfCompressedFile = args[0]; //The name of the file to decode
        String nameOfUncompressedFile = args[1]; //The name of the decoded file
        Decode decode = new Decode(); //Instance of decode for calling non-static methods
        BitInputStream input = null; //BitInputStream for later use
        FileOutputStream output = null; //FileOutputStream for later use
        int entries[] = new int[256]; //Int array for storing frequencies for each byte
        //All frequencies added together to control amount of data to read
        int combinedFrequencies = 0; 

        try {
            //Instanciate a new BitInputStream based on the file to Decode
            input = new BitInputStream(new FileInputStream(new File(nameOfCompressedFile)));
            //Instanciate a new FileOutputStream based on file to write decoded data to
            output = new FileOutputStream(nameOfUncompressedFile);
            //Read all byte frenquencies from the encoded file
            //and fills the entries array with these values.
            for (int i = 0; i < entries.length; i++) {
                int temp = input.readInt(); //temp = byte frequency
                //set byte representation in the entries array to be
                //the same as in the encoded file
                entries[i] = temp; 
                //increment the combinedFrequencies by the amount added to the entries array.
                combinedFrequencies += temp; 
            }
            
            //Create an element containing the hoffmantree
            //which is based on the loaded frequancy table.
            Element theHuffmanTree = decode.createHoffmanTree(entries);
            //Creates a node which is the root of the HuffmanTree.
            Node theHuffmanTreeNode = (Node) theHuffmanTree.getData();
            //Creates a temp node for traversing the tree.
            Node tempNode = theHuffmanTreeNode;
            //a loop for reading all the characters.
            for (int i = 0; i < combinedFrequencies;) {
                //checks if the current node is a leaf or not
                if (tempNode.getCharacter() != -1) { 
                    i++; //if so, incremeant counter i by 1
                    //then write the character to the output file
                    output.write(tempNode.getCharacter()); 
                    tempNode = theHuffmanTreeNode; //then tempNode returns to the root.
                } else {
                    //if tempNode is not a leaf, read the next bit for traversing the tree
                    int j = input.readBit(); 
                    switch (j) {
                        case 0: //if the bit is 0 we go left in the tree
                            //set tempNode to be it's left child
                            tempNode = tempNode.getLeft(); 
                            break;
                        case 1: //if the bit is 1 we go right in the tree
                            //set tempNode to be it's right child
                            tempNode = tempNode.getRight(); 
                            break;
                        default: //in case something goes wrong, print an error
                            System.out.println("readBit error");
                            System.out.println("j = " + j);
                            return; //return (instead of break) will stop the loop
                    }
                }
            }
            //catches I/O exceptions and finally closes the streams
        } catch (FileNotFoundException ex) {
            System.out.println("inputStream file exception");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IOException");
            ex.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }
        //we set the streams to be null, because we don't need them anymore
        input = null;
        output = null;

    }
    
    /**
     * this method creates the huffman tree
     * 
     * this method is exactly the same as in Encode to ensure that we make
     * the same huffman tree again when we decode
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

}
