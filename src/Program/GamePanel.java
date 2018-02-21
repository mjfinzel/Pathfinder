package Program;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JPanel;



public class GamePanel extends JPanel{

	public static boolean paused = false;

	static Point mousePos = new Point(-1,-1);

	static Robot robot;
	static boolean alreadyPressed=false;
	static boolean alreadyReleased=true;
	static boolean debug = false;

	long delay = 0;

	static BufferedImage screen = null;

	public static BufferedImage mask = Images.load("/Textures/Mask.png");
	public static BufferedImage present = Images.load("/Textures/Present-Box.png");
	//BufferedImage beinAden = Images.load("/Textures/Bein_Aden.png");
	static BufferedImage[][] buttonIcons = Images.cut("/Textures/Buttons.png", 20, 20);


	static long soonestAvailableKeyPress = 0;
	static ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
	static String zoneName = "";

	static Button addMarker1 = new Button(2, 285, 2);
	static Button addMarker2 = new Button(1, addMarker1.xpos+21, 2);
	static Button addMarker3 = new Button(0, addMarker2.xpos+21, 2);

	public static Point position = new Point(0,0);
	public static ArrayList<Zone> zones = new ArrayList<Zone>();
	public static int currentZone = 5;

	public GamePanel(){

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//add each of the supported zones
		loadZones();

		//add checkboxes
		checkboxes.add(new CheckBox(2, 2+(checkboxes.size()*11), "ore t5"));
		checkboxes.add(new CheckBox(2, 2+(checkboxes.size()*11), "ore t6"));
		checkboxes.add(new CheckBox(2, 2+(checkboxes.size()*11), "ore t7"));

		checkboxes.add(new CheckBox(2, 7+(checkboxes.size()*11), "wood t5"));
		checkboxes.add(new CheckBox(2, 7+(checkboxes.size()*11), "wood t6"));
		checkboxes.add(new CheckBox(2, 7+(checkboxes.size()*11), "wood t7"));

		checkboxes.add(new CheckBox(2, 12+(checkboxes.size()*11), "rock t5"));
		checkboxes.add(new CheckBox(2, 12+(checkboxes.size()*11), "rock t6"));
		checkboxes.add(new CheckBox(2, 12+(checkboxes.size()*11), "rock t7"));

		checkboxes.add(new CheckBox(2, 17+(checkboxes.size()*11), "fiber t5"));
		checkboxes.add(new CheckBox(2, 17+(checkboxes.size()*11), "fiber t6"));
		checkboxes.add(new CheckBox(2, 17+(checkboxes.size()*11), "fiber t7"));

		checkboxes.add(new CheckBox(2, 22+(checkboxes.size()*11), "hide t5"));
		checkboxes.add(new CheckBox(2, 22+(checkboxes.size()*11), "hide t6"));
		checkboxes.add(new CheckBox(2, 22+(checkboxes.size()*11), "hide t7"));

		//checkboxes.add(new CheckBox(2, 28+(checkboxes.size()*11), "Coward"));
	}
	public static void useKey(int k) {
		robot.keyPress(k);
		sleep(70, 152);
		robot.keyRelease(k);
		sleep(18,32);
	}
	public static boolean colorsEqual(Color c1, Color c2) {
		if(c1.getRed()==c2.getRed()&&c1.getGreen()==c2.getGreen()&&c1.getBlue()==c2.getBlue()) {
			return true;
		}
		return false;
	}
	public static double angleBetween(double x1, double y1, double x2, double y2) {
		return Math.atan2(((double)y1-y2),(((double)x1-x2)));
	}
	public static void loadZones() {
		try {
			CodeSource src = AppletUI.class.getProtectionDomain().getCodeSource();
			if (src != null) {
				
				URL jar = src.getLocation();
				System.out.println(jar+"/..");
				ZipInputStream zip = new ZipInputStream(jar.openStream());
				int tries = 0;
				while(tries<1000) {
					tries++;
					ZipEntry e = zip.getNextEntry();
					if (e == null) {
						System.out.println("breaking");
						break;
					}
					String name = e.getName();
					//if (name.startsWith("src/Nodes")) {
					System.out.println(name);
					// }
				}
			}
			else {
				System.out.println("null src");
			}
		}catch (Exception e) {System.out.println("Failed to load nodes");};
		//		ClassLoader classLoader = AppletUI.class.getClassLoader();
		//		String path = classLoader.getResource("Nodes/Armboth-Fell.txt").toExternalForm();
		//		//String path = file.getAbsolutePath();
		//		System.out.println("Path: "+path);
		//		if(path.contains("bin")) {
		//			path = path.substring(5,path.length()-"bin\\Nodes\\Armboth-Fell.txt".length())+"src/Zones";
		//			System.out.println("Modified Path: "+path);
		//		}
		//		File folder = new File(path);
		//		File[] listOfFiles = folder.listFiles();
		//		ArrayList<String> fileNames = new ArrayList<String>();
		//		//navigate directory to get a list of file names
		//		for (int i = 0; i < listOfFiles.length; i++) {		
		//			if (listOfFiles[i].isFile()) {
		//				String zoneName = listOfFiles[i].getName();
		//				zones.add(new Zone(zoneName.substring(0, zoneName.length()-4)));
		//			}
		//		}
	}
	public static ArrayList<Node> getNodePositions(String zone){
		zoneName = zone;
		Point offset = new Point(0,0);
		double scaler = 183.0/1024.0;
		double yScaler = 98.0/140.0;
		if(!(zoneName.equals("Bein Aden"))) {//if(zoneName.equals("Gorsefire-Pound")||zoneName.equals("Ironhall-Pound")||zoneName.equals("Degenerate-Sump")||zoneName.equals("Femur-Mound")||zoneName.equals("Langdale")||zoneName.equals("Sidbury-Hill")||zoneName.equals("Stonetop-Sink")||zoneName.equals("Heart-of-the-Forest")||zoneName.equals("Darkroot-Hollow")||zoneName.equals("Grave-of-Eternity")||zoneName.equals("Bleak-Moor")||zoneName.equals("Circling-Vultures")) {
			offset.x = -12;
			offset.y = 8;
			scaler = 210.0/1024.0;
		}
		if(zoneName.equals("Hill-of-Hollow-Earth")||zoneName.equals("Armboth-Fell")) {
			offset.x = -20;
			offset.y = 4;
			scaler = 216.0/1024.0;
		}
		String path = Controller.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0,path.length()-4)+"src/Zones/"+zone+".txt";
		InputStream map = Controller.class.getResourceAsStream(("/Nodes/"+zone+".txt")); 
		ArrayList<Node> result = new ArrayList<Node>();


		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(map, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				if((line.contains("\"ore")||line.contains("\"rock")||line.contains("\"wood")||line.contains("\"fiber")||line.contains("\"hide"))) {
					String type = getSubstring(line, "<div class=\"", "\" ");

					//read the node position
					double x = Double.valueOf(getSubstring(line, "left: ", "px"));
					double y = Double.valueOf(getSubstring(line, "top: ", "px"));

					//rotate the node position by 45 degrees
					double distance = distanceBetween(x,y,512.0,512.0);
					double angle = angleBetween(x,y,512.0,512.0);
					x = 512.0+(Math.cos(angle+(Math.PI/4))*distance);
					y = 512.0+(Math.sin(angle+(Math.PI/4))*distance);

					//scale the node coordinates
					x = x*scaler;
					y = y*scaler*yScaler;

					//add the coordinates to the result list
					result.add(new Node(x+offset.x, y+offset.y, type));
				}
			}
			try{reader.close();}catch(Exception ex){}
		}
		catch(Exception ex){
			System.out.println("Failed to read node file.");

		}
		finally{
			try{reader.close();}catch(Exception ex){}
		}
		return result;
	}

	//get a substring given the substring it starts with and the substring it ends with
	public static String getSubstring(String full, String start, String end) {
		String result = "";
		int s=-1;//index of start
		int e=-1;//index of end
		for(int i = 0; i<full.length();i++) {
			//if the start hasn't been found yet
			if(s==-1) {
				for(int j = 0; j<start.length()&&full.charAt(i+j)==start.charAt(j);j++) {
					//if(full.charAt(i+j)==start.charAt(j)) {
					if(j==start.length()-1) {
						s = i+start.length();
						i+=start.length();
						break;
					}
					//}

				}
			}
			else if(e==-1){
				for(int j = 0; j<end.length()&&full.charAt(i+j)==end.charAt(j);j++) {
					//if(full.charAt(i+j)==end.charAt(j)) {
					if(j==end.length()-1) {
						e = i;
						break;
					}
					//}
				}
			}
			else {
				break;
			}
		}
		if(s!=-1&&e!=-1) return full.substring(s, e);
		return result;
	}
	static int randomNumber(int min, int max){
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	public static void click(int x, int y){
		//System.out.println("attempting click at: "+x+","+y);
		mousePos.x = x;
		mousePos.y = y;

		alreadyReleased=true;
		alreadyPressed=false;
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		robot.mouseMove(x,y);
		sleep(20, 50);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleep(120, 150);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		sleep(50, 90);
	}

	public static void shiftClick(int x, int y){
		//System.out.println("attempting click at: "+x+","+y);
		mousePos.x = x;
		mousePos.y = y;

		alreadyReleased=true;
		alreadyPressed=false;

		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseMove(x,y);
		robot.keyPress(KeyEvent.VK_SHIFT);
		sleep(20, 50);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleep(120, 150);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		sleep(50, 90);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		sleep(200,300);
	}
	public static void useKey(int k, boolean typing) {
		if(System.currentTimeMillis()>soonestAvailableKeyPress||typing) {
			robot.keyPress(k);
			if(!typing) sleep(70, 152);
			else sleep(18, 32);
			robot.keyRelease(k);
			if(typing) sleep(18,32);
			soonestAvailableKeyPress = System.currentTimeMillis()+randomNumber(193,257);
		}
	}
	public static void sleep(int min, int max){
		try {
			Thread.sleep(randomNumber(min, max));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Draw(g);
	}
	public int getScreenState(BufferedImage img){
		return -1;
	}
	public static boolean sameColor(Color a, Color b) {
		double rMax = Math.max(a.getRed(), b.getRed());
		double gMax = Math.max(a.getGreen(), b.getGreen());
		double bMax = Math.max(a.getBlue(), b.getBlue());
		double rMin = Math.min(a.getRed(), b.getRed());
		double gMin = Math.min(a.getGreen(), b.getGreen());
		double bMin = Math.min(a.getBlue(), b.getBlue());
		if(rMax-rMin<=160&&rMin/rMax>=.95 && gMin/gMax>=.95 && bMin/bMax>=.95) {
			return true;
		}
		return false;
	}
	public BufferedImage resizeImage(BufferedImage img) {
		BufferedImage result = new BufferedImage(img.getWidth()/2, img.getHeight()/2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		g.drawImage(img, 0, 0, img.getWidth()/2, img.getHeight()/2,null);
		g.dispose();
		return result;
	}
	public static double distanceBetween(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(((double)x1-x2),2)+Math.pow(((double)y1-y2),2));
	}

	public static void drawString(Graphics g, String s, int x, int y) {
		g.setColor(Color.white);
		Font font = new Font("Iwona Heavy",Font.BOLD,12);
		g.setFont(font);
		FontMetrics m = g.getFontMetrics();
		g.drawString(s, x, y);
	}
	public static boolean imagesAreSame(BufferedImage img1, BufferedImage img2) {
		for(int i = 0; i<img1.getWidth();i++) {
			for(int j = 0; j<img2.getHeight();j++) {
				//img1.setRGB(i, j, Color.blue.getRGB());
				Color c1 = new Color(img1.getRGB(i,j));
				Color c2 = new Color(img2.getRGB(i,j));
				if(!sameColor(c1,c2)) {
					return false;
				}
			}
		}
		return true;
	}

	public static BufferedImage copyImage(BufferedImage img) {
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		g.drawImage(img,0,0,null);
		g.dispose();
		return result;
	}
	public static void applyMask() {
		Color black = new Color(20,20,20);
		for(int i = 0; i<mask.getWidth(); i++) {
			for(int j = 0; j<mask.getHeight(); j++) {
				Color current = new Color(mask.getRGB(i, j));
				if(colorsEqual(current, Color.red)) {
					zones.get(currentZone).old.setRGB(i, j, black.getRGB());
				}
			}
		}
	}
	public static boolean nodeIsChecked(Node n) {
		if(n.type.equals("ore") && n.tier.equals("low")&&checkboxes.get(0).checked) return true;
		if(n.type.equals("ore") && n.tier.equals("medium")&&checkboxes.get(1).checked) return true;
		if(n.type.equals("ore") && n.tier.equals("high")&&checkboxes.get(2).checked) return true;

		if(n.type.equals("wood") && n.tier.equals("low")&&checkboxes.get(3).checked) return true;
		if(n.type.equals("wood") && n.tier.equals("medium")&&checkboxes.get(4).checked) return true;
		if(n.type.equals("wood") && n.tier.equals("high")&&checkboxes.get(5).checked) return true;

		if(n.type.equals("rock") && n.tier.equals("low")&&checkboxes.get(6).checked) return true;
		if(n.type.equals("rock") && n.tier.equals("medium")&&checkboxes.get(7).checked) return true;
		if(n.type.equals("rock") && n.tier.equals("high")&&checkboxes.get(8).checked) return true;

		if(n.type.equals("fiber") && n.tier.equals("low")&&checkboxes.get(9).checked) return true;
		if(n.type.equals("fiber") && n.tier.equals("medium")&&checkboxes.get(10).checked) return true;
		if(n.type.equals("fiber") && n.tier.equals("high")&&checkboxes.get(11).checked) return true;

		if(n.type.equals("hide") && n.tier.equals("low")&&checkboxes.get(12).checked) return true;
		if(n.type.equals("hide") && n.tier.equals("medium")&&checkboxes.get(13).checked) return true;
		if(n.type.equals("hide") && n.tier.equals("high")&&checkboxes.get(14).checked) return true;
		return false;
	}
	public void Draw(Graphics g){
		mousePos = MouseInfo.getPointerInfo().getLocation();
		if(zones.size()>0) {
			zones.get(currentZone).Draw(g);

			g.drawImage(zones.get(currentZone).overlay,60,0,null);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 60, AppletUI.windowHeight);
			g.fillRect(0, 193, AppletUI.windowWidth, 48);
			if(zones.get(currentZone).nodes.size()>0) {
				for(int i = 0; i<checkboxes.size();i++) {
					checkboxes.get(i).Draw(g);
				}
			}
			for(int i = 0; i<zones.get(currentZone).markers.size();i++) {
				zones.get(currentZone).markers.get(i).Draw(g, i*(AppletUI.windowWidth/zones.get(currentZone).markers.size()), 193, (AppletUI.windowWidth/zones.get(currentZone).markers.size()));
				//if the marker is more than 100 minutes old, it should be removed
				if(System.currentTimeMillis()-zones.get(currentZone).markers.get(i).creationTime>6000000) 
					zones.get(currentZone).markers.remove(i);
			}
			addMarker1.Draw(g);
			addMarker2.Draw(g);
			addMarker3.Draw(g);
		}
	}

}
