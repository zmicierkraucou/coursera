import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private final int size;
    private WeightedQuickUnionUF qf;
    private int openSites;

    // Byte array for checking if a site is open (0b001), is connected to top (0b010) or bottom (0b100)
    // 0b011 - the site is full
    // The system percolates if there is a site with status 0b111
    private byte[] checkBits;
    private boolean isPercolated;
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Percolation can't be computed for n <= 0");
        } else {
            size = n;
            qf = new WeightedQuickUnionUF(n * n);
            checkBits = new byte[n * n];
            openSites = 0;
        }
    }

    private int resolveId(int row, int col) {
        checkArgs(row, col);
        return (row - 1) * size + (col - 1);
    }

    private void checkArgs(int row, int col) {
        if ((row < 1 || row > size || (col < 1 || col > size))) {
            throw new IllegalArgumentException("row index or column index is out of bounds");
        }
    }

    private byte tryUnion(int p, int row, int col) {
        try {
            int q = resolveId(row, col);
            byte statusQ = checkBits[qf.find(q)];
            if (isOpen(row, col)) {
                qf.union(p, q);
            }
            return statusQ;
        } catch (IllegalArgumentException ex) {
            return 0b000;
        }
    }

    private void unionAdjacentSites(int p, int row, int col) {
        byte northStatus = tryUnion(p, row - 1, col);
        byte southStatus = tryUnion(p, row + 1, col);
        byte westStatus = tryUnion(p, row, col - 1);
        byte eastStatus = tryUnion(p, row, col + 1);
        byte status = checkBits[p];
        int newRoot = qf.find(p);
        checkBits[newRoot] = (byte) (status | northStatus | southStatus | westStatus | eastStatus);
        if(!isPercolated) isPercolated = (checkBits[newRoot] & 0b111) == 0b111;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int p = resolveId(row, col);
        if (!isOpen(row, col)) {
            byte status = 1;
            if (row == 1) {
                status |= 0b010;
            }
            if (row == size) {
                status |= 0b100;
            }
            checkBits[p] = status;
            unionAdjacentSites(p, row, col);
            openSites += 1;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int p = resolveId(row, col);
        return (checkBits[p] & 0b001) == 0b001;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int p = resolveId(row, col);
        return (checkBits[qf.find(p)] & 0b11) == 0b11;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolated;
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
