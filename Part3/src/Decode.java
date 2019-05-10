
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
        String nameOfCompressedFile = args[0];
        String nameOfUncompressedFile = args[1];
        Decode decode = new Decode();
        BitInputStream input = null;
        FileOutputStream output = null;
        int entries[] = new int[256];
        int combinedFrequencies = 0;

        try {
            input = new BitInputStream(new FileInputStream(new File(nameOfCompressedFile)));
            output = new FileOutputStream(nameOfUncompressedFile);
            for (int i = 0; i < entries.length; i++) {
                int temp = input.readInt();
                entries[i] = temp;
                combinedFrequencies += temp;
            }
            
            Element theHuffmanTree = decode.createHoffmanTree(entries);
            Node theHuffmanTreeNode = (Node) theHuffmanTree.getData();
            Node tempNode = theHuffmanTreeNode;
            for (int i = 0; i < combinedFrequencies;) {
                if (tempNode.getCharacter() != -1) {
                    i++;
                    output.write(tempNode.getCharacter());
                    tempNode = theHuffmanTreeNode;
                } else {
                    int j = input.readBit();
                    switch (j) {
                        case 0:
                            tempNode = tempNode.getLeft();
                            break;
                        case 1:
                            tempNode = tempNode.getRight();
                            break;
                        default:
                            System.out.println("readBit error");
                            System.out.println("j = " + j);
                            return;
                    }
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
                output.close();
            } catch (IOException ex) {
                System.out.println("can't close");
            }

        }
        input = null;
        output = null;

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

}
