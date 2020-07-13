package gameClient;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import ex2DataStructure.DGraph;
import ex2DataStructure.edge_data;
import gameElements.Fruits;
import gameElements.Robots;
import ex2GUI.graphGUI;
import ex2utils.StdDraw;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
/**
 * This class represents a simple example for using the GameServer API:
 * the main file performs the following tasks:
 * 1. Creates a game_service [0,23] (line 36)
 * 2. Constructs the graph from JSON String (lines 37-39)
 * 3. Gets the scenario JSON String (lines 40-41)
 * 4. Prints the fruits data (lines 49-50)
 * 5. Add a set of robots (line 52-53) // note: in general a list of robots should be added
 * 6. Starts game (line 57)
 * 7. Main loop (should be a thread) (lines 59-60)
 * 8. move the robot along the current edge (line 74)
 * 9. direct to the next edge (if on a node) (line 87-88)
 * 10. prints the game results (after "game over"): (line 63)
 *  
 * @author boaz.benmoshe
 *
 */
public class SimpleGameClient {
	public static void main(String[] a) {
		//test1();
		MyGameGUI game= new MyGameGUI();
		//	game.drawGraph();
		//	test2();
	}
	public static void test2()
	{
		int counter = 0;
		while(counter < 50)
		{
			StdDraw.setPenColor(Color.BLACK);
			StdDraw.setPenRadius(0.01);
			if(!StdDraw.isMousePressed())
			{
				StdDraw.point(StdDraw.mouseX(), StdDraw.mouseY());
				System.out.println("X: "+StdDraw.mouseX()+" , Y: "+StdDraw.mouseY());
				StdDraw.setFont(new Font("Ariel", Font.ROMAN_BASELINE, 20));
				counter++;
				StdDraw.isMousePressed = false;
			}
		}
	}

	public static void test1() {
		int scenario_num = 23;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String g = game.getGraph();
		DGraph gg = new DGraph();
		gg.init(g);
		System.out.println(gg.G.get(3).getKey());
		MyGameGUI  graph= new MyGameGUI();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			for(int i=0; i < rs ;i++) {
				Robots new_robot= new Robots(i+1);
				graph.game_robots.add(new_robot);
				game.addRobot(new_robot.id);
			}
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {

			}	
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			for(int a = 0;a<rs;a++) {
				game.addRobot(src_node+a);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		game.startGame();
		// should be a Thread!!!
		while(game.isRunning()) {
			moveRobots(game, gg);
		}
		String results = game.toString();
	}
	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	private static void moveRobots(game_service game, DGraph gg) {

		List<String> log = game.move();
		if(log!=null) {
			long t =game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");

					if(dest==-1) {	
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);

						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
					}

				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(DGraph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

}
