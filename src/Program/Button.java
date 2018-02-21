package Program;

import java.awt.Graphics;

public class Button {
	int xpos, ypos;
	int type;
	public Button(int type, int x, int y) {
		this.type = type;
		xpos = x;
		ypos = y;
	}
	public boolean mouseOverThis() {
		if(Controller.mousePos.x>=xpos&&Controller.mousePos.x<=xpos+20) {
			if(Controller.mousePos.y>=ypos&&Controller.mousePos.y<=ypos+20) {
				return true;
			}
		}
		return false;
	}
	public void Draw(Graphics g) {
		if(mouseOverThis())
			g.drawImage(GamePanel.buttonIcons[type][1], xpos, ypos, null);
		else
			g.drawImage(GamePanel.buttonIcons[type][0], xpos, ypos, null);
	}
}
