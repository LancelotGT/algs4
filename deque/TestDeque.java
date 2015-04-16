public class TestDeque {
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        Deque<String> dq = new Deque<String>();
        while (!in.isEmpty()){
            String s = in.readString();
            if (s == null) break;
            if (!s.equals("-")) {
                dq.addFirst(s);
                StdOut.println(s + " was added to the deque.");
            }
            else {
                StdOut.println(dq.removeLast() + " was removed from the deque.");
            }
            StdOut.println("Currently " + dq.size() + " elements on the deque.");
            StdOut.println("");
        }
        for (String str : dq)
            StdOut.println(str);
    }
}