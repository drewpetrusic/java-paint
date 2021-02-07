
package shapes;

import java.awt.Color;
import java.awt.geom.Line2D.Double;
import static java.lang.Double.parseDouble;

public class Line extends Double {

	private static final long serialVersionUID = -6414366646134707367L;
	private Color c;

	public Line(int x, int y, int w, int h, Color c) {
		super(x, y, w, h);
		this.c = c;
	}

	public Line(int x, int y, int w, int h, int rgb) {
		super(x, y, w, h);
		c = new Color(rgb);
	}

	public Line(String line) {
		var foo = line.split(",\\s*");
		this.x1 = parseDouble(foo[0]);
		this.y1 = parseDouble(foo[1]);
		this.x2 = parseDouble(foo[2]);
		this.y2 = parseDouble(foo[3]);
		this.c = new Color((Integer.parseInt(foo[4])));
	}

	public Color getDrawColor() {
		return c;
	}

	@Override
	public String toString() {
		return "Line, " + this.x1 + ", " + this.y1 + ", " + this.x2 + ", " + this.y2 + ", " + c.getRGB();
	}

	public static void main(String[] args) {
		Line e = new Line("475, 325, 48, 48, -16776961");
		System.out.print(e);

	}
}
