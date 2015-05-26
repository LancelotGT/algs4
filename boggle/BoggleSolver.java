import java.util.HashSet;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private TrieSETR dict;
    private HashSet<String> validWords;

    public BoggleSolver(String[] dictionary) {
        dict = new TrieSETR();
        for (String word : dictionary) {
            dict.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int rows = board.rows();
        int cols = board.cols();
        validWords = new HashSet<String>();
        for (int i = 0; i < rows * cols; i++) {
            boolean[] marked = new boolean[rows * cols];
            dfs(board, i, "", 0, marked);
        }
        Bag<String> clone = new Bag<String>();
        for (String word : validWords) {
            clone.add(word);
        }
        return clone;
    }

    // private method to search for valid words and add to a list
    private void dfs(BoggleBoard board, int v, String curr, int d, boolean[] marked) {
        int cols = board.cols();
        int rows = board.rows();
        marked[v] = true;
        String temp = curr + board.getLetter(v / cols, v % cols);

        // if the letter is Q, add a U to the last letter
        if (board.getLetter(v / cols, v % cols) == 'Q') {
            temp = temp + "U";
        }
        
        if (d >= 2) {
            if (dict.contains(temp))
                validWords.add(temp);
            else {
                if (dict.keysWithPrefix(temp).size() == 0)
                    return;
            }
        }
        
        Bag<Integer> adjs = adj(board, v);
        for (int i : adjs) {
            if (!marked[i]) {
                if (board.getLetter(v / cols, v % cols) == 'Q') {
                    dfs(board, i, temp, d + 2, marked.clone());
                }
                else {
                    dfs(board, i, temp, d + 1, marked.clone());
                }
            }
        }
    }

    // Find all the indexes adjacent to v and return a bag of integers
    private Bag<Integer> adj(BoggleBoard board, int v) {
        int rows = board.rows();
        int cols = board.cols();
        Bag<Integer> adjacents = new Bag<Integer>();
        int x = v % cols;
        int y = v / cols;
        if (x + 1 <= cols - 1) adjacents.add(y * cols + x + 1);
        if (x - 1 >= 0) adjacents.add(y * cols + x - 1);
        if (y - 1 >= 0) adjacents.add((y - 1) * cols + x);
        if (y + 1 <= rows - 1) adjacents.add((y + 1) * cols + x);
        if (x + 1 <= cols - 1 && y + 1 <= rows - 1) adjacents.add((y + 1) * cols + x + 1);
        if (x - 1 >= 0 && y + 1 <= rows - 1) adjacents.add((y + 1) * cols + x - 1);
        if (x + 1 <= cols - 1 && y - 1 >= 0) adjacents.add((y - 1) * cols + x + 1);
        if (x - 1 >= 0 && y - 1 >= 0) adjacents.add((y - 1) * cols + x - 1);
        return adjacents;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dict.contains(word)) {
            return 0;
        }
        int length = word.length();
        if (length <= 2) return 0;
        else if (length >= 3 && length <= 4) return 1;
        else if (length == 5) return 2;
        else if (length == 6) return 3;
        else if (length == 7) return 5;
        else return 11;
    }

    // unit test client
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}