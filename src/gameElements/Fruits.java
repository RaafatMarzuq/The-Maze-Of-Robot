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
import ex2DataStructure.node_data;
import ex2utils.Point3D;

public class Fruits implements fruit{
	public edge _edge;
	public int type;
	public double value;
	public Point3D location;
	
	
	public Fruits(String fruit, DGraph g) {
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
		//this._edge= getEd(this.location,g);
				
	

	}
	

	private edge getEd(Point3D location2, DGraph g) {
		Iterator<node_data> nodes=g.getV().iterator();
		while(nodes.hasNext()) {
			node_data _node=nodes.next();
			node_data next_node=nodes.next();
			double _d1=_node.getLocation().distance2D(location2);
			double _d2=location2.distance2D(next_node.getLocation());
			double _d3=_node.getLocation().distance2D(next_node.getLocation());
			if( (_d1+_d2) == _d3  ) {
				int src=_node.getKey();
				int dest=next_node.getKey();
				edge e=(edge) g.getEdge(src, dest);
				return e;
			}
		}

		return null;
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
	public void setEdge(edge e) {
		// TODO Auto-generated method stub
		
	}
	
}		
