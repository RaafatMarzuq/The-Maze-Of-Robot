package gameElements;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ex2utils.Point3D;

public class Robots implements robot  {
	public int id;
	public Point3D location;
	private double _points=0;
	private int src;
	private int dest;
	private double speed;
	
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public Robots() {
		this.id=0;
		this.location=null;
	}
	public Robots(int id) {
		this.id=id;
		this.location=null;
		this.dest = -1;
	}
	
	public Robots(String JSORobots) {
		String pos;
		String split[];
		int id, src, dest;
		double value, speed;
		try {
			JSONObject obj = new JSONObject(JSORobots);
			JSONObject robot_param = obj.getJSONObject("Robot");
			id =  (int) robot_param.getInt("id");
			value = (double) robot_param.getDouble("value");
			src = (int) robot_param.getInt("src");
			dest = (int) robot_param.getInt("dest");
			speed = (double) robot_param.getDouble("speed");
			pos = (String) robot_param.getString("pos");
			split = pos.split(",");
			double _x= Double.parseDouble(split[0]);
			double _y= Double.parseDouble(split[1]);

			Point3D new_location = new Point3D(_x,_y);
			
			setLocation(new_location);
			setId(id);
			set_points(value);
			setSrc(src);
			setDest(dest);
			setSpeed(speed);
			
		} catch (JSONException e) {e.printStackTrace();}

				
				} 
	public void setId(int id) {
		this.id = id;
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


	public int getId() {
		return id;
	}
	
	
	public double get_points() {
		return _points;
	}
	public void set_points(double _points) {
		this._points = _points;
	}

	
	
	
	public Point3D getLocation() {
		return location;
	}
	
	public void setLocation(Point3D location) {
		this.location = location;
	}
	
	public Robots(Point3D p) {
		this.location = p;
	}

}
