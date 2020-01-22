package ex2DataStructure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import ex2utils.Point3D;

public class NodeData implements node_data,Serializable {



	private int key=0,tag=0;
	public static int num =0;
	public static final double INFINITY= Double.POSITIVE_INFINITY;

	private double weight = INFINITY ;
	private Point3D location;
	private String Info="";

	public  HashMap<Integer, edge_data> OutEdges = new HashMap<Integer, edge_data>();

	/////////////////////////////////////////////////////////////////////////////////////////


	/////////////////////////////////////////////////////////////////
	///////////////////     Constructor     /////////////////////////
	/////////////////////////////////////////////////////////////////

	
	public NodeData(int key, Point3D p) {
		this.key = key;
		setLocation(p);
		OutEdges = new HashMap<Integer, edge_data>();

	}
	public NodeData( Point3D p) {
		this.key = num++;
		setLocation(p);
		OutEdges = new HashMap<Integer, edge_data>();

	}
	public 	NodeData(NodeData n) {
		this.key=n.getKey();
		this.Info=n.getInfo();
		this.location = n.getLocation();
		this.weight = n.getWeight();
		this.tag = n.getTag();
		this.OutEdges = n.OutEdges;


	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public int getKey() {
		return this.key;
	}

	@Override
	public Point3D getLocation() {
		if(this.location != null) {
			return this.location;
		}
		return null;
	}

	@Override
	public void setLocation(Point3D p) {
		this.location = p;

	}

	@Override
	public double getWeight() {

		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		this.weight = w;
	}

	@Override
	public String getInfo() {

		return this.Info;
	}

	@Override
	public void setInfo(String s) {
		this.Info = s;
	}

	@Override
	public int getTag() {

		return this.tag;
	}

	@Override
	public void setTag(int t) {
		this.tag=t;
	}

}
