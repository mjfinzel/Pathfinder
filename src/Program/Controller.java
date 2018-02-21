package Program;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class Controller extends JPanel implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener{
	private JPanel gamePanel;
	public static Point mousePos = new Point(0,0);
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mousePos = new Point(e.getX(),e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mousePos = new Point(e.getX(),e.getY());
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		mousePos = new Point(e.getX(),e.getY());
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePos = new Point(e.getX(),e.getY());
		// TODO Auto-generated method stub
		for(int i = 0; i<GamePanel.checkboxes.size();i++) {
			Point c = GamePanel.checkboxes.get(i).position;
			if(e.getX()>=c.x&&e.getX()<=c.x+10&&e.getY()>=c.y&&e.getY()<=c.y+10) {
				GamePanel.checkboxes.get(i).clicked();
			}
		}
		if(GamePanel.addMarker1.mouseOverThis()) {
			GamePanel.zones.get(GamePanel.currentZone).addShinyMarker(1);
		}
		else if(GamePanel.addMarker2.mouseOverThis()) {
			GamePanel.zones.get(GamePanel.currentZone).addShinyMarker(2);
		}
		else if(GamePanel.addMarker3.mouseOverThis()) {
			GamePanel.zones.get(GamePanel.currentZone).addShinyMarker(3);
		}
		if(mousePos.y>193&&mousePos.y<=206&&GamePanel.zones.get(GamePanel.currentZone).markers.size()>0) {
			int index = mousePos.x/(AppletUI.windowWidth/GamePanel.zones.get(GamePanel.currentZone).markers.size());
			if(e.getButton()==MouseEvent.BUTTON1)
				GamePanel.zones.get(GamePanel.currentZone).markers.remove(index);
			else
				GamePanel.zones.get(GamePanel.currentZone).markers.get(index).creationTime=System.currentTimeMillis();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePos = new Point(e.getX(),e.getY());
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		if(e.getKeyCode()==KeyEvent.VK_UP){
			if(AppletUI.windowX>0) {
				AppletUI.windowX-=2;
				AppletUI.f.setLocation(1920-AppletUI.windowWidth,AppletUI.windowX);
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_DOWN){
			if(AppletUI.windowX+AppletUI.windowHeight<1080) {
				AppletUI.windowX+=2;
				AppletUI.f.setLocation(1920-AppletUI.windowWidth,AppletUI.windowX);
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			if(GamePanel.currentZone<GamePanel.zones.size()-1)
				GamePanel.currentZone++;
			else
				GamePanel.currentZone=0;
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			if(GamePanel.currentZone>0)
				GamePanel.currentZone--;
			else
				GamePanel.currentZone=GamePanel.zones.size()-1;
		}
		if(e.getKeyCode()==KeyEvent.VK_F1){
			GamePanel.zones.get(GamePanel.currentZone).old=GamePanel.copyImage(GamePanel.screen);
			GamePanel.zones.get(GamePanel.currentZone).overlay = new BufferedImage(GamePanel.screen.getWidth(), GamePanel.screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics tmp = GamePanel.zones.get(GamePanel.currentZone).overlay.getGraphics();
			tmp.setColor(new Color(0,0,0,0));
			tmp.fillRect(0, 0, GamePanel.zones.get(GamePanel.currentZone).overlay.getWidth(), GamePanel.zones.get(GamePanel.currentZone).overlay.getHeight());
			tmp.dispose();
			GamePanel.applyMask();
			GamePanel.zones.get(GamePanel.currentZone).markers.clear();
			System.out.println("Map was reset");
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void setGamePanel(JPanel panelRef) {
		gamePanel = panelRef;
		gamePanel.addKeyListener(this);
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener(this);
		gamePanel.addMouseWheelListener(this);
	}

}
