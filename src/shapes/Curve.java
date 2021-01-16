package shapes;

import java.awt.Color;
import java.awt.geom.Path2D.Double;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Curve extends Double {
	private static final long serialVersionUID = 1L;
	private Color c;

	// One constructor takes x,y,and a Color. This invokes moveTo with x and y and initializes the field C.
	// The other constructor parses the string data initializing the field c, invokes moveTo with
	// the first two ints, then iterates over the remaining data invoking lineTo with each data pair.
	
	
	public Curve(int x, int y, Color c) { //Constructor takes x, y, and Color.  invokes moveTo with x and y and initializes field C.
		super.moveTo(x, y);
		this.c = c;
	}
					
	
	
	
	
	public Curve(String line) { //constructor two - wrong
		Scanner sc = new Scanner(line.strip());
		sc.useDelimiter(",\\s*");
		String type = sc.next();
									// <-- parses the string data initializing field c, invokes moveTo with the first two ints
									// then iterates over the remaining data invoking lineTo with each data pair.
									// pretty much: do a while loop  "moveTo" for the first pair, then "lineTo" for the rest of points
		int rgb = sc.nextInt();
		c = new Color(rgb);
		super.moveTo(sc.nextInt(), sc.nextInt());
		while(sc.hasNextInt()) {
			super.lineTo(sc.nextInt(), sc.nextInt());
		}
		sc.close();
	}
	
	
	
	
	
	

	public String toString() {
		String typeColor = "curve," + c.getRGB();
		String xy = "";
		PathIterator pathIterator = getPathIterator(null);
		double[] coordinates = new double[6];
		while (!pathIterator.isDone()) {
			int type = pathIterator.currentSegment(coordinates);
			if (type == PathIterator.SEG_LINETO || type == PathIterator.SEG_MOVETO) {
				int x = (int) coordinates[0];
				int y = (int) coordinates[1];
				xy += "," + x + "," + y;
			}
			pathIterator.next();
		}
		return String.format("%s%s%n", typeColor, xy);
	}
	
	public Color getDrawColor() {
		return c;
	}

	public static void main(String[] args) {
		Curve e = new Curve("curve, -16776961, 475, 325, 38, 40, 49, 50,78,100"); // deleted \n off
		System.out.print(e);
	

	}



}
