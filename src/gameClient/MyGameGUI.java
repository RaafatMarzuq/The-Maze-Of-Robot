package gameClient;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import gameClient.KML_save;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import ex2Algo.Graph_Algo;
import ex2Algo.graph_algorithms;
import ex2DataStructure.DGraph;
import ex2DataStructure.NodeData;
import ex2DataStructure.edge_data;
import ex2DataStructure.graph;
import ex2DataStructure.node_data;
import gameElements.Fruits;
import gameElements.Robots;
import gameElements.fruit;
import ex2GUI.graphGUI;
import ex2utils.Point3D;
import ex2utils.StdDraw;
public class MyGameGUI implements Runnable {
	public graph g=new DGraph();
	public Graph_Algo algo=new Graph_Algo(this.g);
	public graphGUI graphGUI= new graphGUI(this.g);
	public  List<Robots> game_robots= new ArrayList<Robots>();
	public  List<Fruits> game_Fruits= new ArrayList<Fruits>();
	private boolean flag= false;
	private game_service game_serv; 
	private KML_save kl;
	private int id;
	private long dt;

	public MyGameGUI() 
	{
		String _optins[]= {"Manual Player","Automatic Player"};
		Object selectedMode = JOptionPane.showInputDialog(null, "Choose game mode", "Game Mode",
				JOptionPane.INFORMATION_MESSAGE, null, _optins,_optins[0] );
		StdDraw.setMode((String) selectedMode);
		StdDraw.setMyGui(this);
	}

	public void Automatic_Player(int id, int Map ) {
			this.setId(id);	
			Game_Server.login(id);
		game_service game = Game_Server.getServer(Map); // you have [0,23] games
		this.setGame_serv(game);
		String gameMap = game.getGraph();
		((DGraph) this.g).init(gameMap);
		this.algo.init(g);
		this.setKl(kl);
		kl = new KML_save(Map,game);
		this.graphGUI= new graphGUI(this.g);
		this.graphGUI.drawAll();

		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			for(int i=0; i < rs ;i++) 
			{
				Robots new_robot= new Robots(i);
				
			
			this.game_robots.add(new_robot);
				
			}
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {
				Fruits new_fruits= new Fruits(f_iter.next(),(DGraph) this.g);
				game_Fruits.add(new_fruits);
				new_fruits.fruit_between_nodes(this.g);
			}
			for (Robots robot: this.game_robots) {
				Fruits _random= closest_fruit(robot.getSrc());

				robot.setSrc(_random.getSrc());
				robot.setLocation(g.getNode(_random.getSrc()).getLocation());
			game.addRobot(_random.getDest());
				
			}
			updateGraph();
			
			start_automatic_game(this,game);
			
			System.out.println("ssss");

		}
		catch (Exception e) {
			System.out.println("ERROR : Automatic Player");
		}		
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	///SetMap is for the manual mode
	public void setMap(MyGameGUI my_game, int Map ) {

		game_service game = Game_Server.getServer(Map); // you have [0,23] games
		setGame_serv(game);
		String gameMap = game.getGraph();

		((DGraph) my_game.g).init(gameMap);
		my_game.graphGUI= new graphGUI(my_game.g);
		my_game.graphGUI.drawAll();
		kl = new KML_save(Map, game);

		setKl(kl);
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			for(int i=0; i < rs ;i++) {
				Robots new_robot= new Robots(i);
				my_game.game_robots.add(new_robot);
			}

			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {
				Fruits new_fruits= new Fruits(f_iter.next(),(DGraph) my_game.g);
				game_Fruits.add(new_fruits);

			}
			this.graphGUI.drawAll();
			this.drawFruits();
		}
		catch (Exception e) {
			System.out.println("Game Over");
		}
	}

	public void start_automatic_game(MyGameGUI my_game , game_service game ) 
	{
		new Thread()
		{
			public void run() 
			{
				game.startGame();
				updateGraph();
				update_robots_and_fruits();
				int t=0;
				long time=game.timeToEnd();
				while(game.isRunning())
				{	
					StdDraw.enableDoubleBuffering();

					moveRobots(game,(DGraph) my_game.g,my_game);
					if(t%1==0) 
					{
						if((time/1000) >0) 
						{
							updateGraph();
							update_robots_and_fruits();
						try {	
							Thread.sleep(dt);
					}catch (Exception e) {
					}
						}
						
							
					}
					StdDraw.show();
				}
				if(!game.isRunning()) 
				{
					String[] game_over=getScore();
					JOptionPane.showMessageDialog(null, "Game Over \nYour Score : "+game_over[0] + 
							"\nYou made :  "+ game_over[1] +" moves","Game Over",2);
					kl.KML_Stop();
				}
			}
		}.start();
		String remark = kl.toString(); game.sendKML(remark); 
	}
	private String[] getScore() {
		String game_over = getGame_serv().toString();

		String Score = "", moves="";
		try {
			JSONObject obj = new JSONObject(game_over);
			JSONObject robot_param = obj.getJSONObject("GameServer");
			Score =  robot_param.getInt("grade")+"";
			moves =  robot_param.getDouble("moves") +"";

		}catch (Exception e) {

		}
		String[] game_over1= {Score,moves};
		return game_over1 ;
	}
	public void start_manual_game(game_service game ,MyGameGUI my_game ) {
		new Thread()
		{
			public void run() {

				game.startGame();

				while(game.isRunning())
				{

					String[] robots_numbers = new String[my_game.game_robots.size()];
					for (int _get = 0; _get < robots_numbers.length; _get++) {
						robots_numbers[_get]=my_game.game_robots.get(_get).id +"";
					}
					System.out.println("Time to end" + game.timeToEnd()/1000);

					int choosen_robot=0;
					if(robots_numbers.length != 1) {
						Object robot_id= JOptionPane.showInputDialog(null, "Choose your Robot " , "MOVE",
								JOptionPane.INFORMATION_MESSAGE, null, robots_numbers, robots_numbers[0]);
						choosen_robot=Integer.parseInt((String) robot_id);
					}


					Collection<edge_data>  neighbors= g.getE(my_game.game_robots.get(choosen_robot).getSrc());
					String[] arr = new String[neighbors.size()];
					int j=0;
					for (edge_data nextE:neighbors) {
						arr[j++] =nextE.getDest() + "";
					}

					Object _next= JOptionPane.showInputDialog(null, "Choose your next step for the robot " , "MOVE",
							JOptionPane.INFORMATION_MESSAGE, null, arr, arr[0]);
					String	next = _next.toString();

					game.chooseNextEdge(my_game.game_robots.get(choosen_robot).getId(),Integer.parseInt(next));
					game.move();


					while(my_game.game_robots.get(choosen_robot).getSrc() != Integer.parseInt(next)&& game.isRunning())
					{	
						StdDraw.clear();
						StdDraw.enableDoubleBuffering();
						game.move();
						game.chooseNextEdge(my_game.game_robots.get(choosen_robot).getId(),Integer.parseInt(next));
						updateGraph();
						update_robots_and_fruits();
						StdDraw.show();
					}
					String[] game_over=getScore();
					StdDraw.setPenColor(Color.blue);
					StdDraw.setFont(new Font("Ariel", Font.ITALIC, 60));
					StdDraw.text(0, 0, "Score: " +game_over[0] +" seconds");

					System.out.println(99);

				}
				System.out.println(444);


			}
		}.start();

		this.kl.KML_Stop();

	}
	private static int nextNode(DGraph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) 
		{
			itr.next();
			i++;
		}
		ans = itr.next().getDest();
		return ans;
	}
	private  void moveRobots(game_service game, DGraph gg ,MyGameGUI my_game) 
	{
		List<String> log = game.move();
		if(log!=null) 
		{
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) 
			{
				String robot_json = log.get(i);
				try 
				{
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int dest = ttt.getInt("dest");
				
					Fruits f = closest_fruit(game_robots.get(rid).getSrc());
					int fruit_source=f.getSrc();
					
					List<node_data> list = shortPath(rid , fruit_source);
					list.add(new NodeData(fruit_source, f.getLocation()));
					if(dest==-1) 
					{	if(list.size() >1) {
						
						dest = list.get(1).getKey();
					}
					else {dest = f.getDest();}
					
					
					double fromFruit = game_robots.get(rid).location.distance2D(f.getLocation());
					if(fromFruit<0.0013) {
						this.dt=50;
					}
					else {this.dt=109;}
						
						
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
						
						}
					}
				
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	public Fruits closest_fruit(int robot_source)
	{
		int closest_fruit_node = Integer.MAX_VALUE;
		Fruits f = null;
		for (int i = 0; i < game_Fruits.size(); i++) 
		{
			int check = (int) this.algo.shortestPathDist(robot_source, game_Fruits.get(i).getSrc());
			if(check < closest_fruit_node) 
			{
				closest_fruit_node = check;
				f = game_Fruits.get(i);
			}
		}
		return f;
	}
	//////////////////////////Drawing and updeating graph functions\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	private  void update_robots_and_fruits() 
	{	
		this.game_Fruits.clear();
		this.game_robots.clear();
		Iterator<String> f_iter = this.getGame_serv().getFruits().iterator();
		while(f_iter.hasNext()) 
		{
			Fruits new_fruits= new Fruits(f_iter.next(),(DGraph) this.g);
			game_Fruits.add(new_fruits);
			new_fruits.fruit_between_nodes(this.g);
			drawFruits();
		}
		if(this.game_serv.isRunning()) {
		Iterator<String> R_iter = this.getGame_serv().move().iterator();
		while(R_iter.hasNext()) 
		{
			Robots new_robot =new Robots(R_iter.next());
			this.game_robots.add(new_robot);
			drawRobots();
		}
		}
	
	}
	public void drawFruits() {
		try {
			for (Fruits fruit: game_Fruits) {
				StdDraw.setFont(new Font("Ariel", Font.ITALIC, 20));
				StdDraw.text(fruit.getLocation().x(), fruit.getLocation().y()+(0.0004), "");
				String names[] = { "robotsicon\\Banana2.png", "robotsicon\\Apple.png"};
				double _w[]= {(0.003)*0.5,(0.001)*0.5};
				double _h[]= {(0.001)*0.5,(0.001)*0.5};		

				if(fruit.getType()== -1) {
					StdDraw.picture(fruit.getLocation().x(), fruit.getLocation().y()+(0.0001), names[0], _w[0], _h[0]);
					this.kl.Place_Mark("fruit_1",fruit.location+" ");
				}
				if(fruit.getType()== 1) {
					StdDraw.picture(fruit.getLocation().x(), fruit.getLocation().y()+(0.0001), names[1], _w[1], _h[1]);
					this.kl.Place_Mark("fruit_1",fruit.location+" ");

				}
			}
		}
		catch (Exception e) {
		}
	}	
	public void drawRobots() {
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.1);
		try {
			for (Robots panda : game_robots) {

				StdDraw.setFont(new Font("Ariel", Font.ITALIC, 20));
				StdDraw.text(panda.getLocation().x(), panda.getLocation().y()+(0.0009), ""+panda.id);
				String names[] = { "robotsicon\\panda3.jpg", "robotsicon\\master2.png","robotsicon\\Tigress2.png"};
				double _w[]= {(0.003)*0.5,(0.004)*0.5,(0.003)*0.5};
				double _h[]= {(0.002)*0.5,(0.002)*0.5,(0.002)*0.5};		
				StdDraw.picture(panda.getLocation().x(),panda.getLocation().y(), names[panda.id], _w[panda.id], _h[panda.id]);
				this.kl.Place_Mark(names[panda.id],panda.getLocation()+" ");

			}
		}
		catch (Exception e) {
		}
	}
	private void updateGraph() {
		StdDraw.picture((graphGUI.getMaxX()+graphGUI.getMinX())/2, (graphGUI.getMinY()+graphGUI.getMaxY())/2, "robotsicon\\background.jpg");
		this.graphGUI.drawEdges();
		this.graphGUI.drawNodes();
	}
	////////////////////////////////// Getters and Setters \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public game_service getGame_serv() {
		return game_serv;
	}
	public void setGame_serv(game_service game_serv) {
		this.game_serv = game_serv;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public KML_save getKl() {
		return kl;
	}
	public void setKl(KML_save kl) {
		this.kl = kl;
	}
	private List<node_data> shortPath(int id,int dest) 
	{
		for (Robots robot : game_robots) 
		{
			if(robot.getId()==id) 
			{
				int src = robot.getSrc();
				return this.algo.shortestPath(src, dest);
			}
		}
		return null;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}


}
