/**
 * Crisrael Lucero
 * Salman Hashimi
 * 
 * Group 2 - Clustering Smartphones
 * 
 * CS 499 - Machine Learning - Dr. Manna
 */

/**
 * The Point class holds all the data of one Smartphone
 * @author Crisrael
 */
public class Point {
	
	//Name,release year,volume(mm^3),weight(g),screen size(in),total resolution,OSType,number of cores,
	//core clock rate(gHz),ram,primary camera(MP),battery(mAh)
	protected String name;
	protected double releaseYear;
	protected double volume;
	protected double weight;
	protected double screenSize;
	protected double totalRes;
	protected double OSType;
	protected double numOfCores;
	protected double clockRate;
	protected double RAM;
	protected double megaPixels;
	protected double battery;
	protected String manufacturer;
	protected Cluster clusterBelong; //Cluster this data point belongs to
	String[] raw; //The raw data string
	
	/**
	 * Constructor that also takes in which cluster the data point belongs to
	 * @param dataLine
	 * @param belong
	 */
	Point(String[] dataLine, Cluster belong) {
		this.clusterBelong = belong;
		this.raw = dataLine;
		this.name = dataLine[0];
		this.releaseYear = Double.valueOf(dataLine[1]);
		this.volume = Double.valueOf(dataLine[2]);
		this.weight = Double.valueOf(dataLine[3]);
		this.screenSize = Double.valueOf(dataLine[4]);
		this.totalRes = Double.valueOf(dataLine[5]);
		this.OSType = Double.valueOf(dataLine[6]);
		this.numOfCores = Double.valueOf(dataLine[7]);
		this.clockRate = Double.valueOf(dataLine[8]);
		this.RAM = Double.valueOf(dataLine[9]);
		this.megaPixels = Double.valueOf(dataLine[10]);
		this.battery = Double.valueOf(dataLine[11]);
		
		this.manufacturer = this.name.split(" ")[0]; //Gets first part of name for manufacturer
	}

	/**
	 * Constructor
	 * @param dataLine
	 */
	Point(String[] dataLine) {
		this.raw = dataLine;
		this.name = dataLine[0];
		this.releaseYear = Double.valueOf(dataLine[1]);
		this.volume = Double.valueOf(dataLine[2]);
		this.weight = Double.valueOf(dataLine[3]);
		this.screenSize = Double.valueOf(dataLine[4]);
		this.totalRes = Double.valueOf(dataLine[5]);
		this.OSType = Double.valueOf(dataLine[6]);
		this.numOfCores = Double.valueOf(dataLine[7]);
		this.clockRate = Double.valueOf(dataLine[8]);
		this.RAM = Double.valueOf(dataLine[9]);
		this.megaPixels = Double.valueOf(dataLine[10]);
		this.battery = Double.valueOf(dataLine[11]);
		
		this.manufacturer = this.name.split(" ")[0]; //Gets first part of name for manufacturer
	}
	
	/**
	 * Return feature value based on the index
	 * @param feature
	 * @return
	 */
	public double getValue(int feature) {
		if(feature == 2) {
			return this.volume;
		} else if(feature == 4) {
			return this.screenSize;
		} else if(feature == 5) {
			return this.totalRes;
		} else if(feature == 6) {
			return this.OSType;
		} else if(feature == 9) {
			return this.RAM;
		} else if(feature == 7) {
			return this.numOfCores;
		} else if(feature == 8) {
			return this.clockRate;
		} else if(feature == 10) {
			return this.megaPixels;
		} else if(feature == 11) {
			return this.battery;
		} else {
			return 9000.1;
		}
	}
	
	/**
	 * Get the cluster that this point belongs to
	 * @return
	 */
	public Cluster getCluster() {
		return this.clusterBelong;
	}
}