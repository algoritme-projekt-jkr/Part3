
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Robin Lausten Petersen - ropet17
 * @author Jeppe Enevold Jensen - jeppj17
 * @author Kim Christensen - kichr17
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
        Element huffmanTree = encode.createHoffmanTree(entries);
        System.out.println("test key" + huffmanTree.getKey() + "    data " + ((Node) huffmanTree.getData()).getFrequency());
        System.out.println("huffmanTable: " + Arrays.toString(encode.huffmanTable((Node)huffmanTree.getData())));
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
            
            //denne if statement er måske forkert-------------------------------<<<<<<KIG HER
            if (node.getCharacter() != -1) {
                a[node.getCharacter()] = sb.toString();
                System.out.println("node: " + node.toString() +" sb: " + sb.toString());
//                sb.deleteCharAt(sb.length()-1);
            }
            //sb.deleteCharAt(sb.length()-1);
            
            huffmanWalk(node.getRight(), a, sb.append("1"));
        }
        sb.deleteCharAt(sb.length()-1);
        return a;
    }

}
