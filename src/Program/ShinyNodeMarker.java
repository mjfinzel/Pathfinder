package Program;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.text.DecimalFormat;

public class ShinyNodeMarker implements Comparable {
	public long creationTime;
	Point position;
	int rarity;
	public ShinyNodeMarker(int rarity, double x, double y) {
		this.rarity=rarity;
		position = new Point((int)x,(int)y);
		creationTime=System.currentTimeMillis();
	}
	public void Draw(Graphics g, int x, int y, int w) {
		//calculate minutes since creation
		double minutes = (System.currentTimeMillis()-creationTime)/60000.0;

		//set color of tooltip
		if(rarity==1) g.setColor(new Color(255,255,0));
		else if(rarity==2) g.setColor(Color.cyan);
		else if(rarity==3) g.setColor(new Color(212,100,255));
		
		if(minutes>=10) {
			g.drawLine(x+(w/2), y, position.x, position.y);
			if(rarity==3) {
				g.setColor(new Color(200,0,255));
				g.drawLine(x+(w/2)+1, y, position.x+1, position.y);
				g.setColor(new Color(212,100,255));
			}
			
		}
		g.fillRect(position.x-1, position.y-1, 3, 3);

		//draw tooltip outline
		g.fillRect(x+1, y, w-1, 12);

		//draw inner tooltip
		g.setColor(Color.gray);
		g.fillRect(x+3, y+2, w-4, 8);
		
		String txt = "";
		if(w>=40)
			txt = new DecimalFormat("#.#").format(minutes)+"m";
		else if(w>=30)
			txt = new DecimalFormat("#.#").format(minutes);
		else
			txt = (int)minutes+"";
		g.setColor(Color.white);
		Font font = new Font("Iwona Heavy",Font.BOLD,11);
		g.setFont(font);
		FontMetrics m = g.getFontMetrics();
		g.drawString(txt, (x+(w/2))-(m.stringWidth(txt)/2), y+10);
	}

	public int compareTo(Object o) {
		ShinyNodeMarker other = (ShinyNodeMarker)o;
		return (position.x-other.position.x);
	}
}
