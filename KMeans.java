/**
 * Crisrael Lucero
 * Salman Hashimi
 * 
 * Group 2 - Clustering Smartphones
 * 
 * CS 499 - Machine Learning - Dr. Manna
 */
import java.util.ArrayList;
import java.util.Random;

/**
 * The K-Means algorithm.
 * @author Crisrael
 *
 */
public class KMeans {
	
	public Point pointOfInterest;
	public Cluster clusterOfInterest = new Cluster();

	public KMeans(String[][] X, int feature, String phoneName) {
		
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>(); //Clusters
		Random r = new Random();
		int minIndex, k = 5, iterations = 50;
		//double cost, costHolder = Double.MAX_VALUE; //Used for SSE approach
		clusterOfInterest = new Cluster(feature);
		
		//Initialize every cluster based on K
		//Each centroid will be a randomized Smartphone data point
		for(int i = 0; i < k; i++) {
			clusterList.add(new Cluster(feature));
			clusterList.get(i).setCentroid(Double.valueOf(X[r.nextInt(X.length)][feature]));
		}
		
		//Iterate. Before we iterated through convergence but outliers would make
		//converging through SSE difficult. Hardcoded iteration calls for less
		//iterations and overall decent clusters.
		for(int i = 0; i < iterations; i++) {
			//Find centroid closest to point
			for(int j = 0; j < X.length; j++) {
				//Find the minimum index
				minIndex = findMinIndex(Double.valueOf(X[j][feature]), clusterList);
				//Place point onto the cluster closest to the point
				clusterList.get(minIndex).addPoint(X[j]);
			}

			//Readjust Centroids
			for(int j = 0; j < clusterList.size(); j++) {
				clusterList.get(j).readjustCentroid();
				if(i < iterations - 1) {
					//Destroy the data points for the cluster
					//Preserve the centroid
					clusterList.get(j).destroyData();
				}
			}
			//System.out.println(costFunction(clusterList));
		}
		//Get cluster of interest
		for(int i = 0; i < clusterList.size(); i++) {
			for(int j = 0; j < clusterList.get(i).pointList.size(); j++) {
				if(clusterList.get(i).pointList.get(j).name.contains(phoneName)) {
					clusterOfInterest = clusterList.get(i);
				}
			}
		}
	}
	
	/**
	 * This method is used to find the Sum Squared Error
	 * @param clusterList
	 * @return
	 */
	private double costFunction(ArrayList<Cluster> clusterList) {
		double cost = 0;
		
		for(Cluster c : clusterList) {
			for(int i = 0; i < c.getSize(); i++) {
				cost += Math.pow(c.getPointValue(i) - c.getCentroid(), 2);
			}
		}
		
		return cost;
	}
	
	/**
	 * This method is used to find the minimum index between a feature from a
	 * smartphone and all the centroids in the cluster list.
	 * @param x
	 * @param clusterList
	 * @return
	 */
	private int findMinIndex(double x, ArrayList<Cluster> clusterList) {
		int minIn = 0;
		double temp;
		double value = Double.MAX_VALUE;
		
		for(int i = 0; i < clusterList.size(); i++) {
			temp = Math.abs(clusterList.get(i).getCentroid() - x);
			if(temp < value) {
				minIn = i;
				value = temp;
			}
		}
		
		return minIn;
	}
}
