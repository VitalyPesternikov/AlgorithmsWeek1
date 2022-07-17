import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform independent trials on an n-by-n grid
    private static final double CONST = 1.96;
    
    private final double[] trialsArray;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        Percolation percolation;
        trialsArray = new double[trials];
        for (int i = 0; i < trialsArray.length; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }
            trialsArray[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialsArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONST * stddev() / Math.sqrt(trialsArray.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONST * stddev() / Math.sqrt(trialsArray.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }

}
