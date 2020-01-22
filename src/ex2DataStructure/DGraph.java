package ex2DataStructure;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.GapContent;

import org.json.JSONArray;

import org.json.JSONObject;



import ex2Algo.Graph_Algo;
import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;
import sun.net.www.content.audio.wav;
import ex2utils.Point3D;


public class DGraph implements graph,Serializable {
	public int nodeSize=0,edgeSize=0;
	public   int MC=0;
	public HashMap<Integer, HashMap<Integer,edge_data>> R = new HashMap<Integer, HashMap<Integer,edge_data>>();
	public HashMap<Integer,node_data> G= new HashMap<>();

	//////////// Constructor//////////////	
	public DGraph() {

		R =  new HashMap<>();
		G=new HashMap<>(); 
	}

	////////////////////////////////////////////////////
	@Override
	public node_data getNode(int key) {
		return (node_data) G.get(key);
	}
	@Override
	public edge_data getEdge(int src, int dest) {
		if(R.get(src).containsKey(dest)) {
			return R.get(src).get(dest);
		}

		return null;
	}

	@Override
	public void addNode(node_data n) {
		this.G.put(n.getKey(), n);
		nodeSize++;
		MC++;
	}
	public void init(String graph) {

		try {
			JSONObject gr = new JSONObject(graph);

		JSONArray nodes= gr.getJSONArray("Nodes");
		for (int i = 0; i <nodes.length(); i++) {
			JSONObject node = nodes.getJSONObject(i);
			int id = node.getInt("id");
			String location = node.getString("pos");
			String p[]= location.split(",");
			double x=Double.parseDouble(p[0]);
			double y= Double.parseDouble(p[1]);
			double z=Double.parseDouble(p[2]);
			Point3D point = new Point3D(x, y,z);		
			NodeData N= new NodeData(id,point);
			this.addNode(N);
			
		}
		
		JSONArray edges = gr.getJSONArray("Edges");
		for (int j = 0; j < edges.length(); j++) {
			JSONObject edge = edges.getJSONObject(j);
			int src= edge.getInt("src");
			int dest= edge.getInt("dest");
			double w=edge.getDouble("w");
			this.connect(src, dest, w);
		}
		
		}
		catch (Exception e) {
			System.err.println("input is incorrect ");
		}
	}
	@Override
	public void connect(int src, int dest, double w) {

		edge  a= new edge(src,dest,w);
		NodeData  n1= (NodeData) G.get(src);
		if(!R.containsKey(src)) 
		{
			HashMap<Integer, edge_data> hm= new HashMap<Integer,edge_data>();
			hm.put(dest, a);
			R.put(src,hm);
			n1.OutEdges.put(dest, (edge_data) a);
			edgeSize++;
			return;
		}

		if(!R.get(src).containsKey(dest))
		{

			R.get(src).put(a.getDest(), a);
			n1.OutEdges.put(dest, (edge_data) a);
			edgeSize++;
			MC++;
		}
		else if(R.get(src).containsKey(dest))
		{
			return;
		}
	}


	@Override
	public Collection<node_data> getV() {
		if(!G.isEmpty()) {
			Collection<node_data> c= (Collection<node_data>) G.values();
			return c;
		}
		
		return null;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(!R.containsKey(node_id)) {
			return null;
		}

		Collection<edge_data> s= (R.get(node_id).values());
		if(s !=null) {
			return   s;
		}
		return null;

	}


	@Override

	public node_data removeNode(int key) 
	{
		if(G.containsKey(key) ) {

			if(R.get(key) !=null) {
				NodeData  n= (NodeData) getNode(key);
				n.OutEdges.clear();
				Collection<edge_data> all= getE(n.getKey());
				if(all != null ) {
					System.out.println(" ALL = getE " + all.toString());
					for(edge_data a : all) {
						NodeData s =(NodeData) G.get(a.getDest()) ;
						for(HashMap<Integer,edge_data> w: R.values() ) {
							if(w.containsKey(key)) {
								Collection<edge_data> temp = w.values();
								for(edge_data edge: temp) {
									NodeData tempn=(NodeData) getNode(edge.getSrc());
									if(tempn != null) {
										tempn.OutEdges.remove(key, edge);
									}
								}
								if(s != null) {
									if(s.OutEdges.containsValue(a)) {
										s.OutEdges.remove(a.getDest(), a);
									}
								}

							}
						}
						if(R.containsKey(key) || R.containsValue(a)){
							R.get(a.getDest()).remove(a.getSrc());
							edgeSize--;
						}
					}

				}

			}
			NodeData  n= (NodeData) getNode(key);
			G.remove(key, n);
			nodeSize--;
		}
		return null;
	}



	@Override
	public edge_data removeEdge(int src, int dest) {
		edge_data e= (edge) getEdge(src, dest);
		if(R.containsKey(src)) {
			if(R.get(src).containsKey(dest)) {
				NodeData n1=(NodeData) this.R.get(src).get(e.getDest());
				n1.OutEdges.remove(e.getDest());
				R.get(src).values().remove(e);
				MC++;
				edgeSize--;
				return e;
			}
		}

		return null;
	}

	@Override
	public int nodeSize() {
		return this.nodeSize;
	}

	@Override
	public int edgeSize() {
		return this.edgeSize;
	}

	@Override
	public int getMC() {

		return MC;
	}



}
