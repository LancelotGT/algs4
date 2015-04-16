public class PercolationStats {
    private double[] testRec;
    private int T;
    
    public PercolationStats(int N, int T) {
        // perform T independent computational experiments on N-N grid
        if (N <= 0 || T <= 0) throw new IllegalArgumentException("Illegal N or T argument.");
        this.T = T;
        testRec = new double[T];
        for (int i = 0; i < T; i++) {
            int count = 0;
            Percolation perc = new Percolation(N);
            while (!perc.percolates()) {
                int row = (int) (StdRandom.random() * (double) N) + 1;
                int col = (int) (StdRandom.random() * (double) N) + 1;
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    count++;
                }
            }
            double ratio = (double) count / (double) (N * N);
            this.testRec[i] = ratio;
        }
    }
    
    public double mean() {
        // sample mean of percolation threshhold
        double sum = 0;
        for (int i = 0; i < T; i++) {
            sum += testRec[i];
        }
        return sum / T;
    }
    
    public double stddev() {
        // sample standard deviation
        double sum = 0;
        double mn = mean();
        for (int i = 0; i < T; i++) {
            sum += (testRec[i] - mn) * (testRec[i] - mn);
        }
        return Math.sqrt(sum / (T - 1));
    }
    
    public double confidenceLo() {
        // returns lower bound of the 95% confidence interval
        double mn = mean();
        double dev = stddev();
        return mn - 1.96 * dev / Math.sqrt(T);
    }
    
    public double confidenceHi() {
        // returns upper bound of the 95% confidence interval
        double mn = mean();
        double dev = stddev();
        return mn + 1.96 * dev / Math.sqrt(T);
    }
    
    public static void main(String[] args) {
        // test client, described below
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        if (N <= 0 || T<= 0) throw new IllegalArgumentException("Illegal N or T argument.");
        PercolationStats percStats = new PercolationStats(N, T);
        double mn = percStats.mean();
        double dev = percStats.stddev();
        double lo = percStats.confidenceLo();
        double hi = percStats.confidenceHi();
        System.out.println("mean " + "                    = " + mn);
        System.out.println("stddev " + "                  = " + dev);
        System.out.println("95 % confidence interval = " + lo + ", " + hi);
    }
}