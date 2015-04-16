import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int head;
    private int tail;
    
    public RandomizedQueue() {
        // construct an empty randomized queue
        q = (Item[]) new Object[1];
        head = 0;
        tail = -1;
    }
    
    public boolean isEmpty() {
        // is the queue empty?
        if (size() <= 0) return true;
        else return false;
    }
    
    public int size() {
        // return the number of items on the queue
        return tail - head + 1;
    }
    
    public void enqueue(Item item) {
        // add the item
        if (item == null) throw new NullPointerException();
        if ((tail + 1) == q.length) resize(2 * size());
        q[++tail] = item;
    }
    
    public Item dequeue() {
        // delete and return a random item
        if (size() == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(head, tail + 1);
        Item item = q[index];
        q[index] = q[head++];
        q[head - 1] = null;
        if (size() > 0 && size() == q.length / 4) resize(q.length / 2);
        return item;
    }
    
    public Item sample() {
        // return (but do not delete) a random item
        if (size() == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(head, tail + 1);
        return q[index];
    }
    
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
            return new ReverseArrayIterator();
        }
        
    private class ReverseArrayIterator implements Iterator<Item> {
        private int i;
        
        public ReverseArrayIterator() {
            i = tail;
            if (!isEmpty()) StdRandom.shuffle(q, head, tail);
        }
        
        public boolean hasNext() {
            return i >= head;
        }
            
        public void remove() {
            // method not supported
            throw new UnsupportedOperationException();
        }
            
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return q[i--];
        }
    }
    
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = head, j = 0; i <= tail; i++, j++) {
            copy[j] = q[i];
        }
        q = copy;
        tail = tail - head;
        head = 0;
    }
    
    public static void main(String[] args) {
        // unit testing client
        RandomizedQueue<String> s = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.enqueue(item);
            else if (!s.isEmpty()) StdOut.print(s.dequeue() + " ");
        }
        StdOut.println("(" + s.size() + " left on deque)");
        for (String str : s)
            StdOut.println(str);
        StdOut.println("Sample returns " + s.sample());
    }
}