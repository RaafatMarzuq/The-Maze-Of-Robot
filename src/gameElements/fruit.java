package gameElements;

import ex2DataStructure.edge;
import ex2utils.Point3D;
/**
 * This interface represents the fruits in the game client , 
 * its location ,type ,value and the edge that it's located on
 * 
 *
 */
public interface fruit {
	/** 
	 * Return the location of the fruit.
	 * @return
	*/
	public	Point3D getLocation();
	/** 
	 * Return the edge that the fruit is located on.
	 * @return
	*/
	public edge getEgde() ;
	/** 
	 * Allows changing the location of the fruit.
	 * 
	 * @param location -  new location  (position) of this fruit.
	*/
	public void setLocation(Point3D location) ;
	/** 
	 * Allows changing the edge that the fruit is located on.
	 * 
	 * @param e -  new edge of this fruit.
	*/
	public void setEdge(edge e) ;
	/** 
	 * returns the value of the fruit.
	 * @return
	*/
   public double getValue();
 
	/** 
	 * returns the type of the fruit,
	 * -1 if it's a banana and 1 if it's an apple.
	 * @return
	*/
   public int getType();
}
