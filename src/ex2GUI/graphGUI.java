package ex2GUI;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.sun.org.apache.xml.internal.security.Init;

import ex2Algo.Graph_Algo;
import ex2DataStructure.DGraph;
import ex2DataStructure.edge_data;
import ex2DataStructure.graph;
import ex2DataStructure.node_data;
import ex2utils.Point3D;
import ex2utils.StdDraw;
import gameClient.MyGameGUI;
import gameElements.Robots;

public class graphGUI  implements Serializable {
	private double MaxX=0 ,MinX=0, MaxY=0, MinY=0;
	public graph g=new DGraph();
	public Graph_Algo algo=new Graph_Algo(this.g);

	
	
	public double getMinX() {
		return MinX;
	}
	public void setMinX(double minX) {
		MinX = minX;
	}
	public double getMaxY() {
		return MaxY;
	}
	public void setMaxY(double maxY) {
		MaxY = maxY;
	}

	

public double getMaxX() {
		return MaxX;
	}
	public void setMaxX(double maxX) {
		MaxX = maxX;
	}
	public double getMinY() {
		return MinY;
	}
	public void setMinY(double minY) {
		MinY = minY;
	}


	
	public graphGUI(graph g) {
		this.g = g;	
		this.algo = new Graph_Algo(g);
		StdDraw.setGui(this);
	}
	public graphGUI() {
		algo=new Graph_Algo(this.g);
		g=new DGraph();
		StdDraw.setGui(this);
	}
	public void init(String file_name) {
		this.algo.init(file_name);
		this.g= algo.g;
		drawAll();
		
		
	}
	
	public void drawAll() {
		
		drawCanvas();
		StdDraw.picture((getMaxX()+getMinX())/2, (getMinY()+getMaxY())/2, "robotsicon\\background.jpg");
		drawEdges();
		drawNodes();
		
		
	}
	
	private double[] findMinX(Collection<node_data> node) {
		double minX=0, maxX=0, minY=0, maxY=0;
		if(node == null) {
			double canv[]= {-350,350,-350,350};
			return canv;
		}
		else {
		minX = node.iterator().next().getLocation().x();
		maxX = node.iterator().next().getLocation().x();

		minY =node.iterator().next().getLocation().y();
		maxY =node.iterator().next().getLocation().y();

		
		for (node_data nodes : node) {
			if(nodes.getLocation().x()>maxX)
				maxX = nodes.getLocation().x();

			if(nodes.getLocation().x()<minX)
				minX = nodes.getLocation().x();

			if(nodes.getLocation().y()>maxY)
				maxY = nodes.getLocation().y();

			if(nodes.getLocation().y()<minY)
				minY = nodes.getLocation().y();
		}
		}
		double canv[]= {minX,maxX,minY,maxY};
		this.setMaxX(maxX);
		this.setMinY(minY);
		this.setMinX(minX);
		this.setMaxY(maxY);
		return canv;
		
	}
	public void drawCanvas() {
		double sc[]= this.findMinX(this.g.getV());
		StdDraw.setCanvasSize((int)(Math.abs(sc[0])+Math.abs(sc[1]))+1200,(int)(Math.abs(sc[2])+Math.abs(sc[3])+1200));
		StdDraw.setXscale(sc[0]-0.001,sc[1]+0.001);
		StdDraw.setYscale(sc[2]-0.006,sc[3]+0.006);


	}
	
	public void drawNodes() {
		StdDraw.setPenColor(Color.BLUE);
		StdDraw.setPenRadius(0.03);
		try {
		for (node_data nodes : g.getV()) {

			StdDraw.point(nodes.getLocation().x(), nodes.getLocation().y());
			StdDraw.setFont(new Font("Ariel", Font.ITALIC, 20));
			StdDraw.text(nodes.getLocation().x(), nodes.getLocation().y()+0.0004, ""+ nodes.getKey());
		}
		}
		catch (Exception e) {
			System.out.println("");
		}
	}
	
	public void drawEdges() {

		StdDraw.setPenRadius(0.008);
		Collection<node_data> points = g.getV();
		try {
		for(node_data nodes: points) {
			Collection<edge_data> e = g.getE(nodes.getKey());
			for (edge_data edge : e) {
				double x0= nodes.getLocation().x();
				double y0= nodes.getLocation().y();
				
				double x1= g.getNode(edge.getDest()).getLocation().x();
				double y1= g.getNode(edge.getDest()).getLocation().y();
				StdDraw.setPenRadius(0.005);

				StdDraw.setPenColor(Color.RED);
				StdDraw.line(x0, y0, x1, y1);

				StdDraw.setFont(new Font("Ariel", Font.BOLD, 15));

				StdDraw.setPenColor(Color.green);
				StdDraw.setPenRadius(0.02);
				StdDraw.point((x0+x1*3)/4, (y0+y1*3)/4);

				StdDraw.setPenColor(Color.black);
				String temp = String.format("%.2f",  edge.getWeight());
				double s= Double.parseDouble(temp);
				StdDraw.text((x0+x1*3)/4, (y0+y1*3)/4, ""+ s);
			}
		}
		}
			catch (Exception ea) {
			}
		}
	}
	
	
