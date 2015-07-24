/**
 * Crisrael Lucero
 * Salman Hashimi
 * 
 * Group 2 - Clustering Smartphones
 * 
 * CS 499 - Machine Learning - Dr. Manna
 */
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * GUI Driver Class
 * @author Crisrael
 */
@SuppressWarnings("serial")
public class TestGUI extends JPanel{

	private JFrame frame;
	private JTextField textField;
	private static String[][] X;
	private JTree tree;
	private static Point[] pointArray;
	private JPanel secondImage;
	private JScrollPane imageViewer;

	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("smartphones.csv");
		Scanner s = new Scanner(file);
		X = new String[1023][12]; //Smartphones
		String inputLine;
		inputLine = s.nextLine(); //Clear first line
		pointArray = new Point[1023];
		
		for(int i = 0; i <= X.length-1; i++) {
			try {
				inputLine = s.nextLine();
				X[i] = inputLine.split(",");
				pointArray[i] = new Point(X[i]);
				
			} catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Something Happened");
				break;
			}
		}
		
		s.close();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGUI window = new TestGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public TestGUI() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame("Clustering");
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);		
		
		JPanel panel_1 = new JPanel(); //East
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_1.setBounds(724, 11, 150, 538);
		panel_1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.getContentPane().add(panel_1);
		
		JLabel lblClusterAlgorithm = new JLabel("Cluster Algorithm:");
		panel_1.add(lblClusterAlgorithm);
		
		ButtonGroup algoGrp = new ButtonGroup();
		final JRadioButton rdbtnKmeans = new JRadioButton("KMeans");
		panel_1.add(rdbtnKmeans);
		algoGrp.add(rdbtnKmeans);
		
		final JRadioButton rdbtnEm = new JRadioButton("EM");
		panel_1.add(rdbtnEm);
		algoGrp.add(rdbtnEm);
		
		JLabel lblSearch = new JLabel("Search by Phone:");
		panel_1.add(lblSearch);
		
		textField = new JTextField();
		textField.setColumns(12);
		panel_1.add(textField);
		
		ButtonGroup grp = new ButtonGroup();
		
		JLabel lblFilters = new JLabel("Relation Criterion:");
		panel_1.add(lblFilters);
		
		final JRadioButton rdbtnPhoneSize = new JRadioButton("Phone Size");
		panel_1.add(rdbtnPhoneSize);
		grp.add(rdbtnPhoneSize);
		
		final JRadioButton rdbtnScreenSize = new JRadioButton("Screen Size");
		panel_1.add(rdbtnScreenSize);
		grp.add(rdbtnScreenSize);
		
		final JRadioButton rdbtnWeight = new JRadioButton("Weight");
		panel_1.add(rdbtnWeight);
		grp.add(rdbtnWeight);
		
		final JRadioButton rdbtnResolution = new JRadioButton("Resolution");
		panel_1.add(rdbtnResolution);
		grp.add(rdbtnResolution);
		
		final JRadioButton rdbtnOperatingSystem = new JRadioButton("Operating System");
		panel_1.add(rdbtnOperatingSystem);
		grp.add(rdbtnOperatingSystem);
		
		final JRadioButton rdbtnRam = new JRadioButton("RAM");
		panel_1.add(rdbtnRam);
		grp.add(rdbtnRam);
		
		final JRadioButton rdbtnNumberOfCores = new JRadioButton("Number of Cores");
		panel_1.add(rdbtnNumberOfCores);
		grp.add(rdbtnNumberOfCores);
		
		final JRadioButton rdbtnClockRate = new JRadioButton("Clock Rate");
		panel_1.add(rdbtnClockRate);
		grp.add(rdbtnClockRate);
		
		final JRadioButton rdbtnMegapixels = new JRadioButton("Megapixels");
		panel_1.add(rdbtnMegapixels);
		grp.add(rdbtnMegapixels);
		
		final JRadioButton rdbtnBatteryLife = new JRadioButton("Battery Life");
		panel_1.add(rdbtnBatteryLife);
		grp.add(rdbtnBatteryLife);
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//What happens when this is clicked
				int feature = 0, phonePlacement = 0;
				String name = textField.getText(); 
				boolean canSearch = true;
				tree = new JTree();
				
				if(name.isEmpty()) {
					javax.swing.JOptionPane.showMessageDialog(frame, "Search a Phone!");	
					canSearch = false;
				} else {
					//Check if in system
					boolean isInSystem = false;
					for(int i = 0; i < X.length; i++) {
						if(name.toLowerCase().equals(X[i][0].toLowerCase())) {
							isInSystem = true;
							phonePlacement = i;
							break;
						}
					}
					if(!isInSystem) {
						canSearch = false;
						javax.swing.JOptionPane.showMessageDialog(frame, "Phone doesn't exist!");	
					} 
				}
				
				//See how to cluster
				if(rdbtnPhoneSize.isSelected()) {
					feature = 2;
				} else if (rdbtnBatteryLife.isSelected()) {
					feature = 11;
				} else if(rdbtnClockRate.isSelected()) {
					feature = 8;
				} else if(rdbtnMegapixels.isSelected()) {
					feature = 10;
				} else if(rdbtnNumberOfCores.isSelected()) {
					feature = 7;
				} else if(rdbtnOperatingSystem.isSelected()) {
					feature = 6;
				} else if(rdbtnRam.isSelected()) {
					feature = 9;
				} else if(rdbtnResolution.isSelected()) {
					feature = 5;
				} else if(rdbtnScreenSize.isSelected()) {
					feature = 4;
				} else if(rdbtnWeight.isSelected()) {
					feature = 3;
				} else {
					javax.swing.JOptionPane.showMessageDialog(frame, "Select a Criterion!");
					canSearch = false;
				}
				
				//Determine which algorithm to use
				if(rdbtnKmeans.isSelected()) {
					if(canSearch) {
						KMeans km = new KMeans(X, feature, name);
						try { //KMeans
							createProfile(name, phonePlacement, km.clusterOfInterest.getClusterData(), feature, 0, X);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if(rdbtnEm.isSelected()) {
					if(canSearch) {
						try { //EM
							createProfile(name, phonePlacement, EM.doubleEM(feature, pointArray, 5, name).getClusterData(), feature, 1, X); 
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					javax.swing.JOptionPane.showMessageDialog(frame, "Select an Algorithm!");
				}
			}
			

			private void createProfile(String name, int placement, String[][] newX, int feature, int algorithm, String[][] phones) throws IOException, JSONException {
				
				JPanel panel_2 = new JPanel(); //Image
				FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
				flowLayout_2.setAlignment(FlowLayout.LEFT);
				panel_2.setBounds(10, 11, 212, 212);
				panel_2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				frame.getContentPane().add(panel_2);
				
				String extensions = name.replace(" ", "%20");
				
				URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
		                  "v=1.0&q=" + extensions);
				URLConnection connection = url.openConnection();
				connection.addRequestProperty("Referer", "www.cpp.edu/~cmlucero");
				
				String line;
				StringBuilder builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					builder.append(line);
				}
				
				JSONObject json = new JSONObject(builder.toString());
				String[] a = json.get("responseData").toString().split("\"url\":\"");
				String[] b = a[1].split("\"");
				
				url = new URL(b[0]);
				BufferedImage orgImg;
				try {
					orgImg = ImageIO.read(url);
				} catch (IOException e) {
					orgImg = ImageIO.read(new File("no-image-available-md.png"));
				}
				int type = orgImg.getType() == 0? BufferedImage.TYPE_INT_ARGB 
						: orgImg.getType();
				BufferedImage newImg = resizeImage(orgImg, type, 200, 200);
				
				JLabel imageLabel = new JLabel(new ImageIcon(newImg));
				imageLabel.setSize(300, 300);
				panel_2.add(imageLabel); //FINISH IMAGE
				
				JPanel panel_3 = new JPanel(); //Profile Stats
				panel_3.setBounds(226, 11, 494, 26);
				frame.getContentPane().add(panel_3);
				panel_3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				
				JLabel lblPhoneSpecifications = new JLabel("Phone Specifications");
				panel_3.add(lblPhoneSpecifications);
				
				JPanel panel_4 = new JPanel(); //Left Specs
				FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
				flowLayout_1.setAlignment(FlowLayout.LEFT);
				panel_4.setBounds(226, 42, 240, 181);
				frame.getContentPane().add(panel_4);
				panel_4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				
				JLabel lblPhoneName = new JLabel("Phone Name: " + phones[placement][0].toString());
				panel_4.add(lblPhoneName);
				
				String releaseYear = phones[placement][1].substring(0, 4);
				JLabel lblRelease = new JLabel("Release Year: " + releaseYear);
				panel_4.add(lblRelease);
				
				JLabel lblVolume = new JLabel("Volume (mm^3): " + phones[placement][2].toString());
				panel_4.add(lblVolume);
				
				JLabel lblWeight = new JLabel("Weight (grams): " + phones[placement][3].toString());
				panel_4.add(lblWeight);
				
				JLabel lblScreenSize = new JLabel("Screen Size (in): " + phones[placement][4].toString());
				panel_4.add(lblScreenSize);
				
				JLabel lblResolution = new JLabel("Total Resolution: " + phones[placement][5].toString());
				panel_4.add(lblResolution);
				
				JPanel panel_5 = new JPanel(); //Right Specs
				FlowLayout flowLayout_5 = (FlowLayout) panel_5.getLayout();
				flowLayout_5.setAlignment(FlowLayout.LEFT);
				panel_5.setBounds(470, 42, 250, 181);
				frame.getContentPane().add(panel_5);
				panel_5.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				
				JLabel lblOSType = new JLabel("Operating System: " + phones[placement][6].toString());
				panel_5.add(lblOSType);
				
				JLabel lblCoreNum = new JLabel("Number of Processing Cores: " + phones[placement][7].toString());
				panel_5.add(lblCoreNum);
				
				JLabel lblClockRate = new JLabel("Clock Rate (GHz): " + phones[placement][8].toString());
				panel_5.add(lblClockRate);
				
				JLabel lblRAM = new JLabel("RAM/Memory (GBs): " + phones[placement][9].toString());
				panel_5.add(lblRAM);
				
				JLabel lblMP = new JLabel("Primary Camera MPixels: " + phones[placement][10].toString());
				panel_5.add(lblMP);
				
				JLabel lblBattery = new JLabel("Battery Life (mAh: " + phones[placement][11].toString());
				panel_5.add(lblBattery);
				
				JPanel panel = new JPanel(); //Related Phones
				panel.setBounds(10, 228, 350, 320);
				
				FlowLayout flowLayout__ = (FlowLayout) panel.getLayout();
				flowLayout__.setAlignment(FlowLayout.LEFT);
				frame.getContentPane().add(panel);				
				
				DefaultMutableTreeNode top = new DefaultMutableTreeNode("Related Phones");
				if(algorithm == 0) {
					createNodes(top, phones, 0, name); //pass something else when algorithm makes a new [][] instead of phones
				} else { //i.e. String[][] newY = KMeans.getCluster(name).getClusterData()
					createNodes(top, phones, 1, name); //pass something else when algorithm makes a new [][]
				}

				imageViewer = new JScrollPane(secondImage);

				tree = new JTree(top);
				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				tree.addTreeSelectionListener(new SelectionListener()); //Listen for selection
				secondImage = new JPanel();
				JScrollPane treeViewer = new JScrollPane(tree);
				
				treeViewer.setVisible(true);
				
				tree.setVisible(true);
				treeViewer.setViewportView(tree);
				panel.add(treeViewer);
				panel.add(imageViewer);
			}
		}); 
		panel_1.add(btnSearch);
	}

	/**
	 * Creates nodes for the Tree to display phones in subclusters
	 * @param top
	 * @param Y
	 * @param algorithm
	 * @param name
	 */
	private static void createNodes(DefaultMutableTreeNode top, String[][] Y, int algorithm, String name) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode book = null;

		KMeans km;
		String[][] newY;
		if(algorithm == 0) {
			//KMeans
			//Algo
			category = new DefaultMutableTreeNode("Phone Size");
			top.add(category);
			
			km = new KMeans(Y, 2, name);
			newY = km.clusterOfInterest.getClusterData();
			//System.out.println(newY.toString());
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Size/Volume 2
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 4, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Screen Size");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Screen Size 4
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 3, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Weight");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Screen Size 4
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 5, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Resolution");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Resolution 5
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 6, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Operating System");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //OS 6
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 9, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("RAM");
			top.add(category);
					
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //RAM 9
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 7, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Number of Cores");
			top.add(category);
							
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Cores 7
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 8, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Clock Rate");
			top.add(category);
							
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Clock Rate 8
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 10, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Camera Megapixels");
			top.add(category);
							
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //MP 10
				category.add(book);
			}
			
			//Algo
			km = new KMeans(Y, 11, name);
			newY = km.clusterOfInterest.getClusterData();
			category = new DefaultMutableTreeNode("Battery Life");
			top.add(category);
					
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Battery Life 11
				category.add(book);
			}			
		} else {
			//EM
			//Algo
			category = new DefaultMutableTreeNode("Phone Size");
			top.add(category);
			
			//newY = km.clusterOfInterest.getClusterData();
			newY = EM.doubleEM(2, toPointArray(Y), 5, name).getClusterData(); //Change Y
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Size/Volume 2
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(4, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Screen Size");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Screen Size 4
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(3, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Weight");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Screen Size 4
				category.add(book);
			}
			//Algo
			newY = EM.doubleEM(5, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Resolution");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Resolution 5
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(6, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Operating System");
			top.add(category);
			
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //OS 6
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(9, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("RAM");
			top.add(category);
					
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //RAM 9
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(7, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Number of Cores");
			top.add(category);
							
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Cores 7
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(8, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Clock Rate");
			top.add(category);
							
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Clock Rate 8
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(10, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Camera Megapixels");
			top.add(category);
							
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //MP 10
				category.add(book);
			}
			
			//Algo
			newY = EM.doubleEM(11, toPointArray(Y), 5, name).getClusterData();
			category = new DefaultMutableTreeNode("Battery Life");
			top.add(category);
					
			for(int i = 0; i < newY.length; i++) { 
				book = new DefaultMutableTreeNode(newY[i][0]); //Battery Life 11
				category.add(book);
			}
		}
	}
	
	/**
	 * Used to resize an image
	 * @param originalImage
	 * @param type
	 * @param width
	 * @param height
	 * @return
	 */
	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
		
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
	 
		return resizedImage;
	}
	
	/**
	 * Converts a feature matrix into an array of data points
	 * @param Y
	 * @return
	 */
	private static Point[] toPointArray(String[][] Y) {
		Point[] pointA = new Point[Y.length];
		
		for(int i = 0; i < pointA.length; i++) {
			pointA[i] = new Point(Y[i]);
		}
		
		return pointA;
	}
	
	/**
	 * Listens for when the selected phone is selected in the subcluster
	 * @author Crisrael
	 *
	 */
	class SelectionListener implements TreeSelectionListener {
		
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
			secondImage = new JPanel();
			secondImage.setBounds(300, 228, 710, 320);
			FlowLayout sIFL = (FlowLayout) secondImage.getLayout();
			sIFL.setAlignment(FlowLayout.LEFT);

			frame.getContentPane().add(secondImage);
			
			String extensions = node.toString().replace(" ", "%20");
			try {
				URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
		                  "v=1.0&q=" + extensions);
				URLConnection connection = url.openConnection();
				connection.addRequestProperty("Referer", "www.cpp.edu/~cmlucero");
				
				String line;
				StringBuilder builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					builder.append(line);
				}
				
				JSONObject json = new JSONObject(builder.toString());
				String[] a = json.get("responseData").toString().split("\"url\":\"");
				String[] b = a[1].split("\"");
				
				url = new URL(b[0]);
				BufferedImage orgImg;
				try {
					orgImg = ImageIO.read(url);
				} catch (Exception e2) {
					orgImg = ImageIO.read(new File("no-image-available-md.png"));
				}
				int type = orgImg.getType() == 0? BufferedImage.TYPE_INT_ARGB 
						: orgImg.getType();
				BufferedImage newImg = resizeImage(orgImg, type, 400, 300);
				JLabel imageLabel = new JLabel(new ImageIcon(newImg));
				imageLabel.setSize(300, 300);
				secondImage.add(imageLabel);
				
				imageViewer.setVisible(true);
				secondImage.setVisible(true);

			} catch (Exception ee) {
				
			}
		}
	}
}
