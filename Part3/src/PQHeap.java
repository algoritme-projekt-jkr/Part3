/**
 * @author Robin Lausten Petersen   - ropet17
 * @author Jeppe Enevold Jensen - jeppj17
 * @author Kim Christensen - kichr17
 */
public class PQHeap implements PQ {

    private Element[] elements; //Element Array for sorting
    private int heapSize; //Counter for amount of Elements currently in the heap

    //Constructor for the class, initiating the array and setting the size counter to 0
    public PQHeap(int i) {
        elements = new Element[i];
        heapSize = 0;
    }

    @Override 
    public Element extractMin() {
        Element min = elements[0]; //Element to be extracted, extracted from the root of the heap.
        elements[0] = elements[heapSize-1]; //Replaces the root with the last element in the heap.
        heapSize--; //Count down heapsize to visualize the removal of an element from the heap.
        minHeapify(0); //Reorder the heap after having disturbed the heap structure with the replacement of the root.
        return min;
    }

    @Override
    public void insert(Element e) {    
        int i = heapSize; //define index for insertion as being at the bottum of the heap
        elements[i] = e; //inserting the element
        while(i > 0 && elements[parent(i)].getKey()> elements[i].getKey()){ //As long as current index isn't at the root and Parent is greater than the current element the following loop is repeated.
            Element temp = elements[i]; //Placeholder for swapping.
            elements[i] = elements[parent(i)]; //Element at current idex is replaced with it's parent.
            elements[parent(i)] = temp; //Parent is replaced with the placeholder which contains the Element from the original index.
            i = parent(i); //Changes current index to be the index of its parent
        }
        heapSize++; //Count up the heapzise to visualize the addition of an element to the heap
    }

    //Find parent index for an index i
    private int parent(int i) {
        return (int) Math.floor((i-1) / 2); //The parent index can be found be deviding the index by 2 and rounding down. Since we use a 0-indexed array/heap we subtract one from the index i
    }

    //Find the left child of the parent at index i
    private int left(int i) {
        return (i * 2) + 1; //The left child can be found be multiplying the parent index by 2. Since we use a 0-indexed array/heap we add one to the product of the multiplication.
    }

    //Find the right child of the parent at index i
    private int right(int i) {
        return (i + 1) * 2; //The right child can be found by multplying the parent index by 2 and adding one to the product of that multiplication. Since we use a 0-indexed array/heap we add one to the parent index before the multiplication instead of adding one to the product of the multiplication
    }
    
    //Heapify the Elements in the array from index i
    private void minHeapify(int i){ 
        int l = left(i); //index of left child
        int r = right(i);//index of right child
        int smallest; //index of the Element with the smallest value amongst parent and its children
        if(l < heapSize && elements[l].getKey() < elements[i].getKey()){ //if left child index is within the heap and the child is smaller than its parent, the smallest value is set to be the index of the left child. Else the smallest value is the current index (the parent).
            smallest = l;
        }else {
            smallest = i;
        }
        if(r < heapSize && elements[r].getKey() < elements[smallest].getKey()){ //if the right child index is within the heap and the child is smaller than the currently defined smallest element, the smallest value is set to be the index of the right child.
            smallest = r;
        }
        if (smallest != i){ //If the current index is not the smallest, it is swapped with the child that is the smallest.
            Element temp = elements[i]; //Placeholder for swapping
            elements[i] = elements[smallest]; //Replaces the current element with the smallest child.
            elements[smallest] = temp; //Replaces the child with the placeholder element which contains the current index Element
            minHeapify(smallest); //Recursively call this method from the index of the smallest Element
        } 
    }   
}
