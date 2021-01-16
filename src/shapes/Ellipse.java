/**
 * 
 */
package shapes;

import java.awt.Color;
import java.awt.geom.Ellipse2D.Double;
import java.util.Scanner;

/**
 * @author dap9697
 *
 */
public class Ellipse extends Double {

	private static final long serialVersionUID = 1L;
	private boolean fill;
	private Color c;

	public Ellipse(int x, int y, int w, int h, Color c) {
		super(x, y, w, h);
		this.c = c;
	}

	public Ellipse(int x, int y, int w, int h, Color c, boolean fill) {
		this(x, y, w, h, c);
		this.fill = fill;
	}

	public Ellipse(String line) {
		Scanner sc = new Scanner(line.strip());
		sc.useDelimiter(",\\s*");
		String type = sc.next();
		if (type.charAt(0) == 'f')
			fill = true;
		x = sc.nextInt();
		y = sc.nextInt();
		width = sc.nextInt();
		height = sc.nextInt();
		int rgb = sc.nextInt();
		c = new Color(rgb);
	}

	public Color getDrawColor() {
		return c;
	}

	public boolean isFill() {
		return fill;
	}

	public String toString() {
		return String.format("%s, %d, %d, %d, %d, %d%n", (fill ? "filledCircle " : "circle "), (int) x, (int) y,
				(int) width, (int) height, c.getRGB());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Ellipse e = new Ellipse("filledCircle, 475, 325, 48, 48, -16776961\n");
		Ellipse e1 = new Ellipse("circle, 375, 425, 47, 47, -16711936\n");
		System.out.print(e);
		System.out.print(e1);

	}

}
