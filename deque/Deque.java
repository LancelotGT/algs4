import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int count;
    
    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    
    public Deque() {
        // constuct an empty deque
        first = null;
        last = null;
        count = 0;
    }
    
    public boolean isEmpty() {
        // is the deque empty?
        return (first == null || last == null);
    }
    
    public int size() {
        // return the number of items on the deque
        return count;
    }
    
    public void addFirst(Item item) {
        // insert the item at the front
        if (item == null) throw new NullPointerException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (oldfirst != null) oldfirst.prev = first;
        else last = first;
        count++;
    }
    
    public void addLast(Item item) {
        // insert the item at the end
        if (item == null) throw new NullPointerException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (oldlast == null) first = last;
        else oldlast.next = last;
        count++;
    }
    
    public Item removeFirst() {
        // delete and return the item at the front
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (!isEmpty()) first.prev = null;
        count--;
        return item;
    }
    
    public Item removeLast() {
        // delete and return the item at the end
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (!isEmpty()) last.next = null;
        else first = null;
        count--;
        return item;
    }
    
    public Iterator<Item> iterator() {
        // return an iterator over items at the end
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() {
            return current != null;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Method not supported.");
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    public static void main(String[] args) {
        // unit testing client
        Deque<String> s = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) s.addLast(item);
            else if (!s.isEmpty()) StdOut.print(s.removeFirst() + " ");
        }
        StdOut.println("(" + s.size() + " left on deque)");
        for (String str : s)
            StdOut.println(str);
    }
}