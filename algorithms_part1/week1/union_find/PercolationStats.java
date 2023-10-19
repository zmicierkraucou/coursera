import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // perform independent trials on an n-by-n grid
    private double[] fractions;
    private final static double CONFIDENCE_95 = 1.96;
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and trials cannot be less than or equal 0");
        } else {
            fractions = new double[trials];
            for (int i = 0; i < trials; i++) {
                fractions[i] = calculatePercolation(n);
            }
        }
    }

    private double calculatePercolation(int n) {
        Percolation perc = new Percolation(n);
        while(!perc.percolates()) {
            int row = StdRandom.uniformInt(1, n + 1);
            int col = StdRandom.uniformInt(1, n + 1);
            perc.open(row, col);
        }
        return (double) perc.numberOfOpenSites() / (n * n);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

//     sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();
        return mean - (CONFIDENCE_95 * stddev / Math.sqrt(fractions.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();
        return mean + (CONFIDENCE_95 * stddev / Math.sqrt(fractions.length));
    }

    // test client (see below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        String interval = "95% confidence interval";
        String mean = "mean";
        String stddev = "stddev";

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean" + " ".repeat(interval.length() - mean.length()) + " = " + stats.mean());
        System.out.println("stddev" + " ".repeat(interval.length() - stddev.length()) + " = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}