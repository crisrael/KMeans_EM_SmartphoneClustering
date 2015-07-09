/**
 * Crisrael Lucero
 * Salman Hashimi
 * 
 * Group 2 - Clustering Smartphones
 * 
 * CS 499 - Machine Learning - Dr. Manna
 */
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The cluster class holds a centroid for the cluster and the pointList, which has
 * all the data points that belong into this cluster.
 * @author Crisrael Lucero
 *
 */
public class Cluster {

	private double centroid;
	protected ArrayList<Point> pointList;
	//private int k;
	private int feature;
	
	Cluster(int feature) {
		this.pointList = new ArrayList<Point>();
		//this.k = K;
		this.feature = feature;
	}
	
	Cluster() {
		this.pointList = new ArrayList<Point>();
	}

	/**
	 * This method is used to readjust the centroid of a cluster after the the
	 * data points have been assigned to this cluster.
	 */
	public void readjustCentroid() {

		double temp = 0;

		for(Point p : pointList) {
			temp += p.getValue(feature); 
		}
		if(pointList.size() != 0) {
			setCentroid(temp / (double)pointList.size());
		}
	}
	
	/**
	 * This method is used to set the initial seeds of the cluster.
	 * @param centroid
	 */
	public void setCentroid(double centroid) {
		this.centroid = centroid;
	}
	
	/**
	 * This method returns an array of points based on the data points that are
	 * in the cluster.
	 * @return
	 */
	public Point[] toPointArray(){
		Point[] temp = new Point[pointList.size()];
		for(int i=0; i<temp.length; i++)temp[i] = pointList.get(i);
		return temp;
	}
	
	/**
	 * Returns the centroid value
	 * @return
	 */
	public double getCentroid() {
		return this.centroid;
	}
	
	/**
	 * Adds a data point to the cluster by taking in a Point object
	 * @param p
	 */
	public void add(Point p){
        pointList.add(p);
    }
	
	/**
	 * Adds a data point to the cluster by taking in an array of features
	 * @param dataLine
	 */
	public void addPoint(String[] dataLine) {
		this.pointList.add(new Point(dataLine, this));
	}
	
	/**
	 * Returns the amount of data points within the cluster.
	 * @return
	 */
	public int getSize() {
		return this.pointList.size();
	}
	
	/**
	 * 
	 * @param i		The point position
	 * @param index	The feature value being queried
	 * @return
	 */
	public double getPointValue(int i) {
		return this.pointList.get(i).getValue(feature);
	}
	
	/**
	 * Converts the Cluster in string format;
	 */
	public String toString() {
		String str = "";
		
		for(Point p : pointList) {
			str += "" + String.valueOf(p.volume) + "\n";
		}
		
		return str;
	}
	
	/**
	 * Converts the Cluster in string format based on the feature parameter
	 * @param attribute
	 * @return
	 */
	public String toString(int attribute) {
		String str = "";
		
                double[] data = new double[pointList.size()];
                
                for(int i=0; i<data.length; i++) data[i] = pointList.get(i).getValue(attribute);
                
                Arrays.sort(data);
                
                
		for(double p : data) {
			str += "" + p + " ";
		}
                str = str +"\n";
		
		return str;
	}
	
	/**
	 * Returns the cluster data in a matrix format
	 * @return
	 */
	public String[][] getClusterData() {
		String[][] Y = new String[pointList.size()][12];
		
		for(int i = 0; i < Y.length; i++) {
			Y[i] = pointList.get(i).raw;
		}
		
		return Y;
	}

	/**
	 * Destroys all the points in the cluster. The centroid value remains the
	 * same. This is used for readjusting the centroid.
	 */
	public void destroyData() {		
		pointList.clear();
		pointList = new ArrayList<Point>();
	}
}
