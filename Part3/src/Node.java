
/**
 * @author Robin Lausten Petersen   - ropet17
 * @author Jeppe Enevold Jensen - jeppj17
 * @author Kim Christensen - kichr17
 */
public class Node {
    private Node left;
    private Node right;
    private int frequency;

    //we set the key to the given value and the children to null
    public Node(int frequency) {
        this.left = null;
        this.right = null;
        this.frequency = frequency;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getFrequency() {
        return frequency;
    }   

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    
}