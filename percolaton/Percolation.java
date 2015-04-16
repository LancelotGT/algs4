public class Percolation {
    private int sz;
    private boolean[] op;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufSec;
    
    public Percolation(int N) {
        // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new IllegalArgumentException("Illegal argument N.");
        }
        sz = N;
        op = new boolean[N * N + 2];
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufSec = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 1; i < N * N; i++) {
            op[i] = false;
        }
        op[0] = true;
        op[N * N + 1] = true;
    }

    public void open(int i, int j) {
        // open site (row i, column j) if it is not already
        checkOutOfBound(i, j);
        int index = xyTo1D(i, j);
        if (op[xyTo1D(i, j)] == false) {
            op[xyTo1D(i, j)] = true;
            if ((i - 1 >= 1) && (op[xyTo1D(i - 1, j)])) {
                uf.union(index, xyTo1D(i - 1, j));
                ufSec.union(index, xyTo1D(i - 1, j));
            }
            if ((i + 1 <= sz) && (op[xyTo1D(i + 1, j)])) {
                uf.union(index, xyTo1D(i + 1, j));
                ufSec.union(index, xyTo1D(i + 1, j));
            }
            if ((j - 1 >= 1) && (op[xyTo1D(i, j - 1)])) {
                uf.union(index, xyTo1D(i, j - 1));
                ufSec.union(index, xyTo1D(i, j - 1));
            }
            if ((j + 1 <= sz) && (op[xyTo1D(i, j + 1)])) {
                uf.union(index, xyTo1D(i, j + 1));
                ufSec.union(index, xyTo1D(i, j + 1));
            }
        }
        if (isTopSite(index)) {
            uf.union(0, index);
            ufSec.union(0, index);
        }
        if (isBottomSite(index)) {
            uf.union(sz * sz + 1, index);
        }
    }
    
    public boolean isOpen(int i, int j) {
        // is site (row i, column j) open?
        checkOutOfBound(i, j);
        return op[xyTo1D(i, j)];
    }
    
    public boolean isFull(int i, int j) {
        // is site (row i, column j) full?
        checkOutOfBound(i, j);
        if (i > 1) {
            if (uf.connected(0, xyTo1D(i, j)) && ufSec.connected(0, xyTo1D(i, j))) return true;
            else return false;
        }
        else if (op[j]) {
            return true;
        }
        else return false;
    }

    public boolean percolates() {
        // does the system percolates?
        if (uf.connected(0, sz * sz + 1)) return true;
        return false;
    }

    private void checkOutOfBound(int i, int j) {
        // check whether the indice is out of bound.
        if (i <= 0 || i > sz) throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j <= 0 || j > sz) throw new IndexOutOfBoundsException("row index j out of bounds");
    }

    private int xyTo1D(int i, int j) {
        // convert the xy coordinate to 1D indice.
        return ((i - 1) * sz + j);
    }
    
    private boolean isTopSite(int index) {
        return index <= sz;
    }
    
    private boolean isBottomSite(int index) {
        return index >= (sz - 1) * sz + 1;
    }

    public static void main(String[] args) {
        // This is simple a test client.
        int N = 20;
        Percolation p = new Percolation(N);
        p.open(1, 1);
        p.open(1, 2);
        if (p.uf.connected(2, 200)) {
            System.out.println("These two sites are connected.");
        }
        else {
            System.out.println("These two sites are not connected.");
        }
    }
}
