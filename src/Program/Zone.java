package Program;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class Zone {
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public ArrayList<ShinyNodeMarker> markers = new ArrayList<ShinyNodeMarker>();
	public BufferedImage old = null;
	public BufferedImage overlay = null;
	public String name;
	public BufferedImage identifier = null;
	public long[][] visits = new long[30][30];

	public Zone(String name) {
		this.name = name;
		try {
			System.out.println("Attempting to load "+"/Zones/"+name+".png");
			this.identifier = Images.load("/Zones/"+name+".png");
		}
		catch(IllegalArgumentException e) {
			System.out.println("   FAILED");
		}
		for(int i = 0; i<30;i++) {
			for(int j = 0; j<30; j++) {
				visits[i][j]=-1;
			}
		}
		nodes = GamePanel.getNodePositions(name);
	}

	@SuppressWarnings("unchecked")
	public void addShinyMarker(int rarity) {
		boolean alreadyExists = false;
		for(int i = 0; i<markers.size();i++) {
			if(markers.get(i).position.equals(GamePanel.position)) {
				alreadyExists=true; 
				break;
			}
		}
		if(alreadyExists==false)
			markers.add(new ShinyNodeMarker(rarity, GamePanel.position.x, GamePanel.position.y));

		Collections.sort(markers);
	}
	public void Draw(Graphics g) {
		GamePanel.screen = JNAScreenShot.getScreenshot(new Rectangle(1605,842, 288, 230));
		//GamePanel.screen = GamePanel.robot.createScreenCapture(new Rectangle(1605,842, 288, 230));//h=220
		boolean inventoryOpen = GamePanel.screen.getRGB(GamePanel.screen.getWidth()-1, GamePanel.screen.getHeight()-100)==-6458532;
		if(inventoryOpen) {
			AppletUI.f.setLocation(new Point(1920, 1080));
			if(GamePanel.checkboxes.size()==16&&GamePanel.checkboxes.get(GamePanel.checkboxes.size()-1).checked) {
				BufferedImage chest = JNAScreenShot.getScreenshot(new Rectangle(116,110,150,30));
				//BufferedImage chest = GamePanel.robot.createScreenCapture(new Rectangle(116,110,150,30));
				//check if a valid chest is open
				if(GamePanel.imagesAreSame(GamePanel.present, chest)) {
					//check if the player is mounted
					Color mountInUse = new Color(GamePanel.robot.createScreenCapture(new Rectangle(580,1035,1,1)).getRGB(0, 0));
					if(GamePanel.colorsEqual(mountInUse, new Color(173,52,5))) {
						//dismount
						GamePanel.useKey(KeyEvent.VK_A);
					}

					//check if the player's mount is equipped
					Color mountEquipped = new Color(GamePanel.robot.createScreenCapture(new Rectangle(1692,410,1,1)).getRGB(0, 0));
					if(!GamePanel.colorsEqual(mountEquipped, new Color(199,159,128))) {
						//shift-click mount into the chest
						GamePanel.shiftClick(GamePanel.randomNumber(1684, 1749), GamePanel.randomNumber(406, 471));
					}
					else {
						GamePanel.checkboxes.get(GamePanel.checkboxes.size()-1).checked=false;
					}

				}
			}
		}
		else {
			AppletUI.f.setLocation(AppletUI.location);
		}
		int width = AppletUI.windowWidth/30;
		int height = AppletUI.windowHeight/30;
		//make sure this is the correct zone
		BufferedImage icon = GamePanel.screen.getSubimage(60, 194, 140, AppletUI.windowHeight-184);
		if(identifier!=null) {
			if(GamePanel.imagesAreSame(identifier, icon)==false) {
				for(int i = 0; i<GamePanel.zones.size();i++) {
					if(GamePanel.zones.get(i)!=this&&GamePanel.zones.get(i).identifier!=null) {
						if(GamePanel.imagesAreSame(GamePanel.zones.get(i).identifier, icon)) {
							System.out.println("Set current zone to "+GamePanel.zones.get(i).name);
							GamePanel.currentZone=i;
							return;
						}
					}
				}
			}
		}

		//Images.save(icon);


		long milli = System.currentTimeMillis();
		if(overlay!=null) {
			Color blue = new Color(97,179,255);
			Color ally = new Color(254,190,85);
			for(int i = 1; i<overlay.getWidth()-1; i++) {
				for(int j = 1; j<overlay.getHeight()-1; j++) {

					int amtSame = 0;
					for(int x = i-1; x<=i+1;x++) {
						for(int y = j-1; y<=j+1;y++) {
							Color c = new Color(GamePanel.screen.getRGB(x, y));
							if(GamePanel.sameColor(c,blue)) amtSame++;
						}
					}
					if(amtSame==9) { 
						GamePanel.position = new Point(i+60,j);
					}
					if(amtSame>=7) {
						overlay.setRGB(i, j, new Color(255,0,0,150).getRGB());
						if(i/width<30&&j/height<30)
							visits[i/width][j/height]=milli;
					}
					if(GamePanel.colorsEqual(new Color(GamePanel.screen.getRGB(i, j)),ally)){//if(current.getRed()==ally.getRed()&&current.getGreen()==ally.getGreen()&&current.getBlue()==ally.getBlue()) {
						overlay.setRGB(i, j, new Color(255,0,120,150).getRGB());
						if(i/width<30&&j/height<30)
							visits[i/width][j/height]=milli;
					}
				}
			}
		}
		else {
			old=GamePanel.copyImage(GamePanel.screen);
			overlay = new BufferedImage(GamePanel.screen.getWidth(), GamePanel.screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics tmp = overlay.getGraphics();
			tmp.setColor(new Color(0,0,0,0));
			tmp.fillRect(0, 0, overlay.getWidth(), overlay.getHeight());
			tmp.dispose();
			GamePanel.applyMask();
		}
		//		if(old==null||!imagesAreSame(screen, old)) {
		//			//			//old=copyImage(screen);
		//		}

		g.drawImage(GamePanel.screen, 60,0,null);
		g.drawImage(GamePanel.mask, 60,0,null);


		if(overlay!=null) {
			for(int i =0; i<nodes.size();i++) {
				//if(nodes.get(i).position.x<old.getWidth()&&nodes.get(i).position.x>=0&&nodes.get(i).position.y<old.getHeight()&&nodes.get(i).position.y>=0) {
				if(GamePanel.nodeIsChecked(nodes.get(i))) {
					g.setColor(Color.GREEN);
					g.fillRect(nodes.get(i).position.x+60+55, nodes.get(i).position.y+32, 1,1);
				}
				//}
			}

		}

		GamePanel.drawString(g, name, AppletUI.windowWidth-90, AppletUI.windowHeight-20);

		//clear parts of the overlay that haven't been visited for a long time

		long maxAge = 60000*30;
		for(int i = 0; i<30; i++) {
			for(int j = 0; j<30; j++) {
				if(visits[i][j]>0&&milli-visits[i][j]>maxAge) {
					Graphics2D g2d = (Graphics2D)overlay.getGraphics();
					g2d.setComposite(AlphaComposite.Clear);
					g2d.fillRect(i*width, j*height, width, height);
					g2d.setComposite(AlphaComposite.SrcOver);
					g2d.dispose();
				}
			}
		}

	}
}
