import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final byte[] sites;

    private final int size;

    private final WeightedQuickUnionUF algorithm;

    private boolean percolates;

    private int counterOpened;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException();
        size = n;
        sites = new byte[n * n];
        algorithm = new WeightedQuickUnionUF(n * n);
        counterOpened = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        throwIAEWhenArgumentsAreBad(row, col);
        if ((getArraySiteValue(row, col) & 0b100) == 0b100) return;
        setArraySiteOpen(row, col);
        counterOpened++;
        int previous = getIndexInAlgorithmFromRowAndCol(row, col);
        unionAdjacentSiteWithCheck(previous, getIndexInAlgorithmFromRowAndCol(row - 1, col));
        unionAdjacentSiteWithCheck(previous, getIndexInAlgorithmFromRowAndCol(row, col + 1));
        unionAdjacentSiteWithCheck(previous, getIndexInAlgorithmFromRowAndCol(row + 1, col));
        unionAdjacentSiteWithCheck(previous, getIndexInAlgorithmFromRowAndCol(row, col - 1));
        if (sites[algorithm.find(previous)] == 0b111) {
            percolates = true;
        }
    }

    private void unionAdjacentSiteWithCheck(int previous, int adjacent) {
        if (adjacent >= 0 && adjacent < size * size && ((sites[adjacent] & 0b100) == 0b100)) {
            byte prevRoot = sites[algorithm.find(previous)];
            byte adjRoot = sites[algorithm.find(adjacent)];
            algorithm.union(previous, adjacent);
            sites[algorithm.find(previous)] |= prevRoot | adjRoot;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        throwIAEWhenArgumentsAreBad(row, col);
        return (getArraySiteValue(row, col) & 0b100) == 0b100;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        throwIAEWhenArgumentsAreBad(row, col);
        return (sites[algorithm.find(getIndexInAlgorithmFromRowAndCol(row, col))] & 0b010) == 0b010;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return counterOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    private void throwIAEWhenArgumentsAreBad(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();
    }

    private int getIndexInAlgorithmFromRowAndCol(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) return -1;
        return (row - 1) * size + col - 1;
    }

    private byte getArraySiteValue(int row, int col) {
        return sites[getIndexInAlgorithmFromRowAndCol(row, col)];
    }

    private void setArraySiteOpen(int row, int col) {
        int index = getIndexInAlgorithmFromRowAndCol(row, col);
        if (size == 1) {
            sites[getIndexInAlgorithmFromRowAndCol(row, col)] = 0b111;
        } else if (index < size) {
            sites[getIndexInAlgorithmFromRowAndCol(row, col)] = 0b110;
        } else if (index >= size * (size - 1)) {
            sites[getIndexInAlgorithmFromRowAndCol(row, col)] = 0b101;
        } else {
            sites[getIndexInAlgorithmFromRowAndCol(row, col)] = 0b100;
        }

    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 1000;
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates) {
            percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
        }
        System.out.println((double) percolation.counterOpened / (n * n));
    }
}

