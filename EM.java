// Written by Salman Hashimi
//
// EM Clustering on different attributres of smartphones.
//
import java.io.*;
import java.util.*;
//////////////////////////////////////////////////////////////////////
class EM {
//--------------------------------------------------------------------

    private static Point[] smartphones;
    private static String[][] X;

    /**
     * Main method is used to test EM Clustering on data from file
     * smartphones.csv
     * 
     * Disregard for use in GUI
     * 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("smartphones.csv");
        Scanner s = new Scanner(file);
        Cluster result;
        X = new String[1023][12]; //Smartphones
        smartphones = new Point[1023];
        //double[] data = new double[100];
        String inputLine;
        inputLine = s.nextLine(); //Clear first line
        for (int i = 0; i <= X.length - 1; i++) {
            try {
                inputLine = s.nextLine();
                X[i] = inputLine.split(",");
                smartphones[i] = new Point(X[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Something Happened");
                break;
            } catch (NullPointerException n) {
                System.out.println("nullp");
            }
        }
        s.close();
        result = doubleEM(2, smartphones, 100, "Samsung Galaxy J1");
        System.out.println("Cluster " + result.toString(2) + "");
    }
//--------------------------------------------------------------------

    public static Cluster doubleEM(int attribute, Point[] dataset, int clusters, String name) {
        int index = 0;
        Cluster[] result = new Cluster[clusters];
        double[] data = getData(dataset, attribute);// Get data into doubles to calculate
        double[] priors = initializePriors(data, clusters);// Initialize all priors to 1
        double[] mean = initializeMeans(data, clusters);// Initialize means to random elements in the set
        double[] variance = initializeVariance(data, clusters);// Initialize variance to an even spread for each cluster
        double[][] prob = new double[data.length][clusters];
        double[][] posterior = new double[data.length][clusters];
        String t = "";

        for (int i = 0; i < dataset.length; i++) {// find index of desired element
            if (dataset[i].name.contains(name)) {
                index = i;
            }
        }

        for (int c = 0; c < clusters; c++) {// Initialize clusters
            result[c] = new Cluster(attribute);
        }
        try {
            for (int c = 0; c < 1000; c++) {// Iterate for 1000 rounds to converge
                for (int i = 0; i < data.length; i++) {// For each element in the dataset:
                    for (int j = 0; j < clusters; j++) {// And each desired cluster:
                        prob[i][j] = getProb(data[i], mean[j], variance[j]);// Get the expected probability of each element being in each cluster
                        posterior[i][j] = getPost(j, prob[i], priors);// Given the expected probabilities, find the new posterior probabilities.
                    }
                }
                // After all the data has been checked with the current mean and variance:
                for (int i = 0; i < clusters; i++) {// For each cluster, update:
                    mean[i] = getMean(i, data, posterior);// the new mean given the new posterior probability
                    variance[i] = getVariance(i, data, mean[i], posterior);// the new variance given the new posterior probability
                }
                priors = updatePriors(posterior);// Update priors given new posterior probabilities
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        };
        for (int i = 0; i < data.length; i++) { //For each element, place into the most appropriate cluster
            dataset[i].clusterBelong = result[getBest(prob[i])];
            result[getBest(prob[i])].add(dataset[i]);
        }
        return result[getBest(prob[index])];// return cluster of the desired element
    }
//--------------------------------------------------------------------    
// Internal print function for debugging
    public static void print2d(double[][] d) {
        String temp;
        for (int i = 0; i < d.length; i++) {
            temp = "";
            for (int j = 0; j < d[i].length; j++) {
                temp = temp + d[i][j] + " ";
            }
            System.out.println(temp);
        }
    }
//--------------------------------------------------------------------    
// Internal print function for debugging
    public static void print1d(double[] d) {
        String temp = "";
        for (int i = 0; i < d.length; i++) {
            temp = temp + d[i] + " ";
        }
        System.out.println(temp);
    }
//--------------------------------------------------------------------    
// Find most appropriate cluster
    public static int getBest(double[] prob) {
        int temp = prob.length - 1;
        for (int i = 0; i < prob.length; i++) {
            if (prob[i] > prob[temp]) {
                temp = i;
            }
        }
        return temp;
    }
//--------------------------------------------------------------------
// Extract data into double form to run calculations
    public static double[] getData(Point[] dataset, int attribute) {
        double[] temp = new double[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
            temp[i] = dataset[i].getValue(attribute);
        }
        return temp;
    }
//--------------------------------------------------------------------    
// Internal initialization method for debugging
    public static double[][] initialize2d(double init, int r, int c) {
        double[][] temp = new double[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                temp[i][j] = init;//*(c*(Math.random()*r+1)%r)/r;
            }
        }
        return temp;
    }
//--------------------------------------------------------------------
// Internal initialization method for debugging
    public static double[] initialize1d(double init, int l) {
        double[] temp = new double[l];
        for (int i = 0; i < l; i++) {
            temp[i] = init;
        }
        return temp;
    }
//--------------------------------------------------------------------
// Initialize all priors to 1 so that each cluster has equal prior
    public static double[] initializePriors(double[] data, int l) {
        double[] temp = new double[l];
        for (int i = 0; i < l; i++) {
            temp[i] = 1.0 / l;
        }
        return temp;
    }
//--------------------------------------------------------------------
// Initialize each cluster with a random mean from the data
    public static double[] initializeMeans(double[] data, int l) {
        double[] temp = new double[l];
        for (int i = 0; i < l; i++) {
            temp[i] = data[(int) (Math.random() * data.length + 1) % data.length];
        }
        return temp;
    }
//--------------------------------------------------------------------
//initialize each cluster with a variance of 1
    public static double[] initializeVariance(double[] data, int l) {
        double[] temp = new double[l];
        for (int i = 0; i < l; i++) {
            temp[i] = 1.0;
        }
        return temp;
    }
//--------------------------------------------------------------------
// Find probability of x in the gaussian of the given mean and variance
    public static double getProb(double x, double mean, double variance) throws Exception {
        double temp = (1 / (Math.PI * 2 * variance)) * (1 / (Math.pow((x - mean), 2) / (2 * variance)));
        if (temp == Double.POSITIVE_INFINITY) {// Debug: recalculate after getting strange result
            temp = getProb(x - 1, mean, variance);
        }
        if (Double.isNaN(temp)) {// Debug: Throw exception after getting strange result
            System.out.println("Error with2: x = " + x + " mean = " + mean + " variance = " + variance);
            throw new Exception("NaN in getProb");
        }
        return temp;
    }
//--------------------------------------------------------------------
// Catculate posterior probability of the element given the prior and probability
    public static double getPost(int index, double[] prob, double[] prior) throws Exception {
        double temp = 0;
        for (int i = 0; i < prob.length; i++) {
            temp = temp + prob[i] * prior[i];
        }
        temp = (prob[index] * prior[index]) / temp;
        if (Double.isNaN(temp)) {// Debug: Throw exception after getting strange result
            System.out.println("Error with post: prob = " + prob[index] + " prior = " + prior[index] + " index = " + index);
            throw new Exception("NaN in getPost");
        }
        return temp;
    }
//--------------------------------------------------------------------
//Calculate new mean of the given cluster
    public static double getMean(int index, double[] data, double[][] post) throws Exception {
        double temp1 = 0;
        double temp2 = 0;
        for (int i = 0; i < data.length; i++) {
            temp1 = temp1 + data[i] * post[i][index];
            temp2 = temp2 + post[i][index];
            if (Double.isNaN(temp1)) {// Debug: Throw exception after getting strange result
                throw new Exception("NaN in getMean");
            }
        }
        temp1 = temp1 / temp2;
        return temp1;
    }
//--------------------------------------------------------------------
//Calculate new variance of the given cluster
    public static double getVariance(int index, double[] data, double mean, double[][] post) throws Exception {
        double temp1 = 0.1;
        double temp2 = 0.1;
        for (int i = 0; i < data.length; i++) {
            temp1 = temp1 + (Math.pow((data[i] - mean), 2) * post[i][index]);
            temp2 = temp2 + post[i][index];
            if (Double.isNaN(temp1)) {// Debug: Throw exception after getting strange result
                throw new Exception("NaN in getVariance inner");
            }
        }
        temp1 = temp1 / temp2;
        if (Double.isNaN(temp1)) {// Debug: Throw exception after getting strange result
            throw new Exception("NaN in getVariance");
        }
        if (temp1 == 0) {// Debug: Throw exception after getting strange result
            System.out.println("Error with: variance = " + temp1 + " denom = " + temp2);
            throw new Exception("Zero in getVariance");
        }
        return temp1;
    }
//--------------------------------------------------------------------
// Update priors of this element for each cluster given new posterior probability
    public static double[] updatePriors(double[][] post) throws Exception {
        double[] temp = new double[post[0].length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = 0;
        }
        for (int i = 0; i < post.length; i++) {
            for (int j = 0; j < post[i].length; j++) {
                temp[j] = temp[j] + post[i][j];
            }
        }
        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i] / post.length;
            if (Double.isNaN(temp[i])) {// Debug: Throw exception after getting strange result
                throw new Exception("NaN in updatePriors");
            }
            if (temp[i] == 0) {// Debug: Throw exception after getting strange result
                System.out.println("Error with prior: index " + i);
                throw new Exception("Zero in updatePriors");
            }
        }
        return temp;
    }
//--------------------------------------------------------------------
} // end class EM
//////////////////////////////////////////////////////////////////////