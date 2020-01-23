package gameClient;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import ex2Algo.*;
import ex2DataStructure.*;
import ex2GUI.*;
import ex2utils.*;
import Server.game_service;

public class KML_save {
	/*

	 * private data types of the class
	 * int level
	 * StringBuffer str- the string will be written there.
	 */
	private int level;
	public StringBuffer str;
	private game_service game;
	/**
	 * simple constructor
	 * @param level
	 */
	public KML_save(int level, game_service game) {

		this.level = level;
		str = new StringBuffer();
		this.game=game;
		KML_Start();
	}
	/**
	 * this function initialize the working platform to KML
	 */
	private void KML_Start(){
		str.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
						"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
						"  <Document>\r\n" +
						"    <name>" + "Game stage :"+level + "</name>" +"\r\n"
				);

		str.append("<Style id=\"yellowLineGreenPoly\">\r\n" + 
				"      <LineStyle>\r\n" + 
				"        <color>7f00ffff</color>\r\n" + 
				"        <width>4</width>\r\n" + 
				"      </LineStyle>\r\n" + 
				"      <PolyStyle>\r\n" + 
				"        <color>7f00ff00</color>\r\n" + 
				"      </PolyStyle>\r\n" + 
				"    </Style>");

		KML_node();
	}
	/**
	 * this function initialize the node icon to KML
	 */
	private void KML_node(){
		str.append(" <Style id=\"node\">\r\n" +
				"      <IconStyle>\r\n" +
				"        <Icon>\r\n" +
				"          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n" +
				"        </Icon>\r\n" +
				"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
				"      </IconStyle>\r\n" +
				"    </Style>"
				);
		KML_Fruit();
	}
	/**
	 * this function initialize the Fruits icon to KML (Type 1 and -1)
	 */
	private void KML_Fruit(){
		str.append(
				" <Style id=\"fruit_-1\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/paddle/purple-stars.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>" +
						" <Style id=\"fruit_1\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/paddle/red-stars.png</href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>"
				);
		KML_Robot();
	}
	/**
	 * this function initialize the Robots icon to KML
	 */
	private void KML_Robot(){
		str.append(
				" <Style id=\"robot\">\r\n" +
						"      <IconStyle>\r\n" +
						"        <Icon>\r\n" +
						"          <href>http://maps.google.com/mapfiles/kml/shapes/motorcycling.png></href>\r\n" +
						"        </Icon>\r\n" +
						"        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" +
						"      </IconStyle>\r\n" +
						"    </Style>"
				);
	}





	/**
	 * this function is used in "paint"
	 * after painting each element
	 * the function enters the kml the location of each element
	 * @param style
	 * @param location
	 */
	void Place_Mark(String style, String location)
	{
		LocalDateTime Present_time = LocalDateTime.now();
str.append(   "      <TimeStamp>\r\n" +
				"        <when>" + Present_time+ "</when>\r\n" +
				"      </TimeStamp>\r\n" +
				 "    <Placemark>\r\n" +
				"      <TimeSpan>\r\n" +
				
				 "        <begin>"+Present_time+ "</begin>\r\n" +
				 "        <end>" + Present_time + "</end>\r\n" +
				 "      </TimeSpan>\r\n" +
				 "      <styleUrl>#" + style + "</styleUrl>\r\n" +
				 "      <Point>\r\n" +
				 "        <coordinates>" + location + "</coordinates>\r\n" +
				 "      </Point>\r\n" +
				 "    </Placemark>\r\n"
				);
	}


	public void KML_Stop()
	{
		str.append("  \r\n</Document>\r\n" +
				"</kml>");
		SaveFile();
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////

	

	private void SaveFile(){
		System.out.println(level);
		System.out.println(str.toString());

		try
		{
			File file=new File("Kml//"+level+".kml");
			PrintWriter pw=new PrintWriter(file);
			pw.write(str.toString());
			pw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	


}