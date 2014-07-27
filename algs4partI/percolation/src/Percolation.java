/**
 * Created by iyurinok.
 */
public class Percolation {


    public static final int TOP = 0;
    private final boolean[][] sites;
    private final int size;
    private final WeightedQuickUnionUF perc;
    private final WeightedQuickUnionUF full;
    private final int BOTTOM;

    /**
     * Creates N-by-N grid, with all sites blocked
     *
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Wrong N value");
        }
        this.size = N;
        this.BOTTOM = size * size + 1;
        this.sites = new boolean[N][N];
        this.perc = new WeightedQuickUnionUF(N * N + 2);
        this.full = new WeightedQuickUnionUF(N * N + 1);
    }

    // throw exception of i or j is outside of the specified bounds
    private void verify(int i, int j) {
        if (i <= TOP || i > this.size) {
            throw new IndexOutOfBoundsException("row index " + i + " out of bounds");
        }
        if (j <= TOP || j > this.size) {
            throw new IndexOutOfBoundsException("col index " + j + " out of bounds");
        }
    }

    /**
     * open site (row i, column j) if it is not already
     *
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        if (isOpen(i, j)) {
            return;
        }
        sites[i - 1][j - 1] = true;

        int index = getIndex(i, j);
        // if in the first/last row, connect to virtual start/end, respectively
        if (i == 1) {
            perc.union(TOP, index);
            full.union(TOP, index);
        }
        if (i == this.size) {
            perc.union(index, BOTTOM);
        }

        // connect to other open neighbors
        if (i > 1 && isOpen(i - 1, j)) {
            perc.union(getIndex(i - 1, j), index);
            full.union(getIndex(i - 1, j), index);
        }
        if (i < this.size && isOpen(i + 1, j)) {
            perc.union(getIndex(i + 1, j), index);
            full.union(getIndex(i + 1, j), index);
        }
        if (j > 1 && isOpen(i, j - 1)) {
            perc.union(getIndex(i, j - 1), index);
            full.union(getIndex(i, j - 1), index);
        }
        if (j < this.size && isOpen(i, j + 1)) {
            perc.union(getIndex(i, j + 1), index);
            full.union(getIndex(i, j + 1), index);
        }
    }

    /**
     * is site open?
     *
     * @param i row
     * @param j column
     * @return
     */
    public boolean isOpen(int i, int j) {
        verify(i, j);
        return sites[i - 1][j - 1];
    }

    /**
     * is site (row i, column j) full?
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isFull(int i, int j) {
        return isOpen(i, j) && full.connected(TOP, getIndex(i, j));
    }

    private int getIndex(int i, int j) {
        return (i - 1) * size + (j - 1) + 1;
    }

    /**
     * does the system percolate?
     *
     * @return
     */
    public boolean percolates() {
        return perc.connected(TOP, BOTTOM);
    }

}
