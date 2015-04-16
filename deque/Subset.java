public class Subset {  
    // Take an array of N strings as input, and randomly (uniformly) print out k of them.
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }
        int N = rq.size();
        int k = Integer.parseInt(args[0]);
        String[] s = new String[k];
        int i = 0;
        while (i < k) {
            boolean inArray = false;
            String temp = rq.sample();
            for (int j = 0; j < s.length; j++) {
                if (s[j] == temp) inArray = true;
            }
            if (!inArray) {
                s[i] = temp;
                StdOut.println(s[i]);
                i++;
            }
        }
    }
}