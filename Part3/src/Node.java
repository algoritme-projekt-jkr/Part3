
/**
 * @author Robin Lausten Petersen   - ropet17
 * @author Jeppe Enevold Jensen - jeppj17
 * @author Kim Christensen - kichr17
 */
public class Node {
    private Node left; //left child
    private Node right; // right child
    private int frequency; //the frequency of this node
    private int character; // the character of this node
    
    //a constructor where you only set the frequency
    public Node(int frequency) {
        this.left = null;
        this.right = null;
        this.frequency = frequency;
        this.character = -1; //a node without a character has character -1
    }
    
    //a constructor to set the character and the frequency
    public Node(int character, int frequency) {
        this(frequency); //a call to the other constructor
        this.character = character;
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

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }
    
    @Override
    public String toString(){
        return "frequency: " + this.frequency + " character: " + this.character;
    }
    
}