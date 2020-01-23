package gameElements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ex2DataStructure.DGraph;
import ex2DataStructure.edge;
import ex2DataStructure.edge_data;
import ex2DataStructure.graph;
import ex2DataStructure.node_data;
import ex2utils.Point3D;
import gameClient.MyGameGUI;

public class Fruits implements fruit{
	private edge _edge;
	private int type,src=-2 ,dest=-2;
	public double value;
	public Point3D location;
	private final double EPSELON= 0.000000000000000002;
	
	public Fruits(String fruit, DGraph g)
	{
		String new_fr= fruit.substring(10, fruit.length()-2);
		String c[] = new_fr.split(",");
		String v=c[0].substring(8);
		double value=Double.parseDouble(v);
		this.value=value;
		String _ty=c[1].substring(7);
		int _type = Integer.parseInt(_ty);
		this.type=_type;
		String _posX=c[2].substring(7);
		double _x=Double.parseDouble(_posX);
		double _y=Double.parseDouble(c[3]);
		Point3D fruit_location = new Point3D(_x, _y);
		this.location= fruit_location;
	}
	
	public void fruit_between_nodes(graph g)
	{
	
		Collection<node_data> points = g.getV();
		for(node_data nodes: points) 
		{
			Collection<edge_data> e = g.getE(nodes.getKey());
			for (edge_data edge : e) 
			{
				Point3D node_location = nodes.getLocation();
				Point3D node_dest_location = g.getNode(edge.getDest()).getLocation();
				
				double node_src_to_fruit = node_location.distance2D(this.getLocation());
				double fruit_to_node_dst = this.getLocation().distance2D(node_dest_location);
				double node_src_to_node_dst = node_location.distance2D(node_dest_location);
				double d=(node_src_to_fruit + fruit_to_node_dst);
				if((d - (node_src_to_node_dst))<=0+EPSELON ) {
					//if(edge.getSrc() < edge.getDest() && this.getType() == 1) {
						this.dest=edge.getDest();
						this.src=edge.getSrc();
						return;
	//					}
//					else if(edge.getSrc() > edge.getDest() && this.getType() == -1) {
//						this.set_edge((ex2DataStructure.edge) edge);
//					}
					
				}
		
			}
		}
	}
	
	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public edge get_edge() {
		return _edge;
	}


	public void set_edge(edge _edge) {
		this._edge = _edge;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}
	@Override
	public edge getEgde() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setEdge(edge e) 
	{
		this._edge = e;
	}
}
