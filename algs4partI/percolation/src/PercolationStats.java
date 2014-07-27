/**
 * Created by iyurinok.
 */
public class PercolationStats {

    private final double[] results;
    private final int numExperiments;


    /**
     * perform T independent computational experiments on an N-by-N grid
     *
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        //determine if T and N are both positive
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        numExperiments = T;
        results = new double[T];

        //for T experiments, create Percolation
        //and run the required tests
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            runPercolationTests(p, N, i);
        }
    }

    private void runPercolationTests(Percolation p, int N, int i) {
        // perform T independent computational experiments on an N-by-N grid
        int openSiteCounter = 0;
        while (!p.percolates()) {
            //While Percolation Object has not yet percolated

            //create two random i and j numbers within N*N grid
            int randomI = StdRandom.uniform(N) + 1;
            int randomJ = StdRandom.uniform(N) + 1;

            //determine if a site at random i,j coordinates is open
            //if not, open site and increment openSiteCounterer
            if (!p.isOpen(randomI, randomJ)) {
                p.open(randomI, randomJ);
                openSiteCounter++;
            }
        }

        //using OpenSitesCounter, calculate and return % of open sites
        //needed for N*N grid to percolation
        results[i] = (double) openSiteCounter / (N * N);
    }


    /**
     * sample mean of percolation threshold
     *
     * @return
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * sample standard deviation of percolation threshold
     *
     * @return
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * returns lower bound of the 95% confidence interval
     *
     * @return
     */
    public double confidenceLo() {
        return StdStats.mean(results) - (1.96 * StdStats.stddev(results))
                / Math.sqrt(numExperiments);

    }

    /**
     * returns upper bound of the 95% confidence interval
     *
     * @return
     */
    public double confidenceHi() {
        return StdStats.mean(results) + (1.96 * StdStats.stddev(results))
                / Math.sqrt(numExperiments);
    }

    /**
     * test client, described below
     *
     * @param args
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);

        StdOut.print(stats.mean());
        StdOut.print(stats.stddev());
        StdOut.printf("%f, %f", stats.confidenceLo(), stats.confidenceHi());
    }

}
