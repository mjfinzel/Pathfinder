package Program;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class CheckBox {
	boolean checked = false;
	Point position;
	String text;
	public CheckBox(int x, int y, String name) {
		position = new Point(x, y);
		text = name;
	}
	public void clicked() {
		if(checked) checked=false;
		else checked = true;
	}
	public void Draw(Graphics g) {
		if(checked) {
			g.setColor(Color.CYAN);
			g.fillRect(position.x, position.y, 10, 10);
		}
		g.setColor(Color.red);
		g.drawRect(position.x, position.y, 9, 9);
		
		GamePanel.drawString(g, text, position.x+12, position.y+9);
	}
}
