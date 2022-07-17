import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final boolean[][] grid;

    private final int size;

    private final WeightedQuickUnionUF algotithm;

    private int counterOpened;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException();
        size = n;
        grid = new boolean[n][n];
        algotithm = new WeightedQuickUnionUF(n * n + 2);
        counterOpened = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        throwIAEWhenArgumentsAreBad(row, col);
        if (getGridCellValue(row, col)) return;
        setGridCellTrue(row, col);
        counterOpened++;
        if (checkCellExistsAndTrue(row - 1, col)) {
            algotithm.union(getIndexInAlgorithmFromRowAndCol(row, col),
                    getIndexInAlgorithmFromRowAndCol(row - 1, col));
        }
        if (checkCellExistsAndTrue(row, col - 1)) {
            algotithm.union(getIndexInAlgorithmFromRowAndCol(row, col),
                    getIndexInAlgorithmFromRowAndCol(row, col - 1));
        }
        if (checkCellExistsAndTrue(row + 1, col)) {
            algotithm.union(getIndexInAlgorithmFromRowAndCol(row, col),
                    getIndexInAlgorithmFromRowAndCol(row + 1, col));
        }
        if (checkCellExistsAndTrue(row, col + 1)) {
            algotithm.union(getIndexInAlgorithmFromRowAndCol(row, col),
                    getIndexInAlgorithmFromRowAndCol(row, col + 1));
        }
    }

    private boolean checkCellExistsAndTrue(int row, int col) {
        if (row == 0 || row == size + 1) return true;
        if (col < 1 || col > size || row < 0 || row > size + 1) return false;
        return getGridCellValue(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        throwIAEWhenArgumentsAreBad(row, col);
        return getGridCellValue(row, col);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        throwIAEWhenArgumentsAreBad(row, col);
        return algotithm.find(getIndexInAlgorithmFromRowAndCol(row, col)) == algotithm.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return counterOpened;
    }

    // does the system percolate?
    public boolean percolates() {
        return algotithm.find(size * size + 1) == algotithm.find(0);
    }

    private void throwIAEWhenArgumentsAreBad(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();
    }

    private int getIndexInAlgorithmFromRowAndCol(int row, int col) {
        if (row == 0) return 0;
        if (row == size + 1) return size * size + 1;
        throwIAEWhenArgumentsAreBad(row, col);
        return (row - 1) * size + col;
    }

    private boolean getGridCellValue(int row, int col) {
        return grid[row - 1][col - 1];
    }

    private void setGridCellTrue(int row, int col) {
        grid[row - 1][col - 1] = true;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 10000;
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
        }
        System.out.println((double) percolation.counterOpened / (n * n));
    }
}

