package solarSystem;

import java.sql.DriverManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SolarSystem {
	
	static JLabel picture;
	final static String jdbcUrl = "jdbc:sqlite:/D:\\SQLite\\solarSystemDB.db";
	static String planetName = "Mercury";
	private static final Logger log = Logger.getLogger(SolarSystem.class);
	public static void createAndShowGUI()
	{
		String[] planets = QueryAllPlanets();
		JFrame jf =  new JFrame("Solar System");
		
		JComboBox planetList = new JComboBox();
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout(3, 3));
		if (planets != null)
		{
			if (planets.length == 0)
			{
				log.error("No planets to display");
			}
			else {
				planetList = new JComboBox(planets);
				planetList.setSelectedIndex(0);
				picture = new JLabel();
				picture.setHorizontalAlignment(JLabel.CENTER);
				picture.setPreferredSize(new Dimension(700, 700));
				showImage(planets[planetList.getSelectedIndex()]);
				jp.add(picture, BorderLayout.CENTER);
			}
		}
		
		JButton moonButton = new JButton("Moon");
		jp.add(planetList, BorderLayout.NORTH);
		jp.add(moonButton, BorderLayout.SOUTH);
		JPanel nestedJP = new JPanel();
		nestedJP.setLayout(new GridLayout(8,0));
		jp.add(nestedJP, BorderLayout.EAST);
		ArrayList<JLabel> jLArray = new ArrayList<JLabel>();
        for ( int i = 0; i < 8; i++)
        {
        	jLArray.add(new JLabel());
        	jLArray.get(i).setPreferredSize(new Dimension(300, 5));
        	jLArray.get(i).setFont(new Font("Times New Roman", Font.BOLD, 18));
            //jL.get(i).setHorizontalAlignment(SwingConstants.LEFT);
            nestedJP.add(jLArray.get(i));
            //jf.getContentPane().add(jL.get(0), BorderLayout.EAST);	//change to iterate
        }
        
        String[] planetDetails = QueryPlanetDetail(planetName);
        jLArray.get(0).setFont(new Font("Times New Roman", Font.BOLD, 30));
        if (planetDetails != null)
        {
        	if (planetDetails.length == 0)
        	{
        		log.error("No results from database");
        	}
	        else {
	        	jLArray.get(0).setText(planetDetails[0]);
	            jLArray.get(1).setText("Radius: " + planetDetails[1] + " km");
	            jLArray.get(2).setText("Distance from the Sun: " + planetDetails[2] + " mln km");
	            jLArray.get(3).setText("Length of the year: " + planetDetails[3] + " days");
	            jLArray.get(4).setText("Orbital velocity: " + planetDetails[4] + " km/h");
	            jLArray.get(5).setText("Equatorial inclination: " + planetDetails[5] + " deg");
	            jLArray.get(6).setText("Surface Gravity: " + planetDetails[6] + " m/s2");
	            jLArray.get(7).setText("Moon count: " + planetDetails[7]);
	        }
        }       
		ActionListener myActionListener = new ActionListener(){
			
		    public void actionPerformed(ActionEvent e) {
		    	
		        if (e.getActionCommand() == "Moon")
		        {
		        	log.info("Moon button was pressed");
		    		Map<String, Integer> moons = QueryMoon(planetName);
		    		if (moons.size() == 0)
		    		{
		    			for (int i = 1; i < jLArray.size(); i++)
		    			{
		    				jLArray.get(i).setText(" ");
		    			}
		    			jLArray.get(1).setText(planetName + " has no moons");
		    			log.info("No moons detected");
		    		}
		    		else 
		    		{
		    			for (int i = 1; i < jLArray.size() - 1; i++)
		    			{
		    				jLArray.get(i).setText(" ");
		    			}
		    			int i = 1;
		    			for(String key : moons.keySet())
		    			{
		    				jLArray.get(i).setText(key + " of radius: " + moons.get(key) + "km");
		    				i++;
		    			}
		    			log.info("Moons displayed");
		    		}
		        }
		        else 
		        {	
		        	log.info("Dropdown changed");
		        	JComboBox<?> cb = (JComboBox<?>)e.getSource();
			        planetName = (String)cb.getSelectedItem();
			        String[] planetDetails = QueryPlanetDetail(planetName);
			        jLArray.get(0).setText(planetDetails[0]);
			        jLArray.get(1).setText("Radius: " + planetDetails[1] + " km");
			        jLArray.get(2).setText("Distance from the Sun: " + planetDetails[2] + " mln km");
			        jLArray.get(3).setText("Length of the year: " + planetDetails[3] + " days");
			        jLArray.get(4).setText("Orbital velocity: " + planetDetails[4] + " km/h");
			        jLArray.get(5).setText("Equatorial inclination: " + planetDetails[5] + " deg");
			        jLArray.get(6).setText("Surface Gravity: " + planetDetails[6] + " m/s2");
			        jLArray.get(7).setText("Moon count: " + planetDetails[7]);
			        showImage(planetName);
		        }
		    }
		};
		
		
		planetList.addActionListener(myActionListener);
		moonButton.addActionListener(myActionListener);
		
		jf.getContentPane().add(jp);
        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    private static void showImage(String name) {
    	name = name.toLowerCase();   
    	String path = "images/" + name + ".jpg";
    	File file = new File(path);
    	if (!file.isFile())
    	{
    		log.warn("Image not found");
    		picture.setIcon(null);
    		picture.setText("Image not found");
    	}
    	else
    	{
    		log.info("Image loaded correctly");
    		picture.setText(null);
    		ImageIcon icon = new ImageIcon(path);
        	Image image = icon.getImage();
        	Image newImage = image.getScaledInstance(700, 700, Image.SCALE_DEFAULT);
        	ImageIcon newIcon = new ImageIcon(newImage);
            picture.setIcon(newIcon);
            picture.setText(null);
    	}
    }
	
	private static String[] QueryAllPlanets()
	{
		try 
		{
			Connection connection = DriverManager.getConnection(jdbcUrl);

			int size = QueryCount();
			int i = size;
			String[] nameList = new String[size];
			
			
			String sql = "SELECT Name FROM PLANET ORDER BY SunDistance DESC";
			Statement statement = connection.createStatement();						
			ResultSet result = statement.executeQuery(sql);
			
			while (result.next() && i > 0) {
				i--;
				nameList[i] = result.getString("Name");
			}
			
			result.close();
			statement.close();
			connection.close();
			if (!result.isClosed())
			{
				log.warn("ResultSet not closed");
			}
			if (!statement.isClosed())
			{
				log.warn("Statement not closed");
			}
			if (!connection.isClosed())
			{
				log.warn("Connection not closed");
			}
			return nameList;
		} 
		catch (SQLException e) 
		{
			log.error(e,e);
			return new String[0];
		}
	}
	private static String[] QueryPlanetDetail(String planetName)
	{
		try 
		{
			Connection connection = DriverManager.getConnection(jdbcUrl);
			String[] planetDetails = new String[8];		//number of columns in PLANET table
			
			String sql = "SELECT Name, Radius, SunDistance, YearLength, OrbitVelocity, EquatorialInclination, SurfaceGravity, MoonCount"
					+ " FROM PLANET WHERE Name = '" + planetName + "'";
			Statement statement = connection.createStatement();						
			ResultSet result = statement.executeQuery(sql);
			
			while (result.next())
			{
				planetDetails[0] = result.getString("Name");
				planetDetails[1] = result.getString("Radius");
				planetDetails[2] = result.getString("SunDistance");
				planetDetails[3] = result.getString("YearLength");
				planetDetails[4] = result.getString("OrbitVelocity");
				planetDetails[5] = result.getString("EquatorialInclination");
				planetDetails[6] = result.getString("SurfaceGravity");
				planetDetails[7] = result.getString("MoonCount");
			}
			result.close();
			statement.close();
			connection.close();
			if (!result.isClosed())
			{
				log.warn("ResultSet not closed");
			}
			if (!statement.isClosed())
			{
				log.warn("Statement not closed");
			}
			if (!connection.isClosed())
			{
				log.warn("Connection not closed");
			}
			return planetDetails;
		} 
		catch (SQLException e) 
		{
			log.error(e,e);
			return new String[0];
		}
	}
	private static int QueryCount()
	{
		int size = 0;
		try
		{
			Connection connection = DriverManager.getConnection(jdbcUrl);
			String sqlCount = "SELECT COUNT(*) FROM PLANET";
			Statement statementCount = connection.createStatement();
			ResultSet resultCount = statementCount.executeQuery(sqlCount);
			size = resultCount.getInt("COUNT(*)");
			resultCount.close();
			statementCount.close();
			connection.close();
			if (!resultCount.isClosed())
			{
				log.warn("ResultSet not closed");
			}
			if (!statementCount.isClosed())
			{
				log.warn("Statement not closed");
			}
			if (!connection.isClosed())
			{
				log.warn("Connection not closed");
			}
		}
		catch (SQLException e) 
		{
			log.error(e,e);
		}
		return size;
	}
	private static Map<String,Integer> QueryMoon(String planetName)
	{
		Map<String, Integer> moons = new HashMap<String, Integer>();
		try
		{
			Connection connection = DriverManager.getConnection(jdbcUrl);
			
			String sql = "SELECT m.Name, m.Radius FROM MOON m "
					+ "JOIN PLANET p ON p.IDPlanet = m.IDPlanet "
					+ "WHERE p.Name = '" + planetName + "' ORDER BY m.Radius";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next())
			{
				moons.put(result.getString("Name"), result.getInt("Radius"));
			}
			result.close();
			statement.close();
			connection.close();
			if (!result.isClosed())
			{
				log.warn("ResultSet not closed");
			}
			if (!statement.isClosed())
			{
				log.warn("Statement not closed");
			}
			if (!connection.isClosed())
			{
				log.warn("Connection not closed");
			}
			return moons;
		}
		catch(SQLException e)
		{
			log.error(e,e);
			return new HashMap<String,Integer>();
		}
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { createAndShowGUI(); }
        });
		
	}

}
