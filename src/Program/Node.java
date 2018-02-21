package Program;

import java.awt.Point;

public class Node {
	Point position;
	String type;
	String tier;
	public Node(double x, double y, String name) {
		position = new Point((int)x, (int)y);
		String[] typeAndTier = name.split("-");
		type = typeAndTier[0];
		tier = typeAndTier[1];
	}
}
