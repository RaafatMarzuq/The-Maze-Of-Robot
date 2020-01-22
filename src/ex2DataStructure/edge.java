package ex2DataStructure;

import java.io.Serializable;

public class edge implements edge_data,Serializable {

	private int src,dest,tag=0;
	private double weight;
	private String Info="";

	///////////////////////////////////////////////////////////////////////////////


	/////////////////////////////////////////////////////////////////
	///////////////////     Constructor     /////////////////////////
	/////////////////////////////////////////////////////////////////



	public edge(int src,int des ,double w) {
		this.dest = des;
		this.src = src;
		this.weight= w;

	}

	public edge() {
		this.dest=0;
		this.weight = Double.POSITIVE_INFINITY;

	}

	@Override
	public int getSrc() {
		return this.src;
	}

	@Override
	public int getDest() {
		return this.dest;
	}

	@Override
	public double getWeight() {
		return this.weight;
	}

	@Override
	public String getInfo() {
		return this.Info;
	}

	@Override
	public void setInfo(String s) {
		this.Info= s;

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
