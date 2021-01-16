package shapes;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D.Double;
import java.util.Scanner;

public class RoundRectangle extends Double {

	private static final long serialVersionUID = 1L;
	private boolean fill;
	private Color c;
	
	public RoundRectangle(int x, int y, int w, int h, int arcWidth, int arcHeight, Color c) {
		super(x, y, w, h, arcWidth, arcHeight);
		this.c = c;
	}
	public RoundRectangle(int x, int y, int w, int h, int arcWidth, int arcHeight, Color c, boolean fill) {
		this(x, y, w, h, arcWidth, arcHeight, c);
		this.fill = fill;
	}
	
	public RoundRectangle(String line) {
		Scanner sc = new Scanner(line.strip());
		sc.useDelimiter(",\\s*");
		String type = sc.next();
		if (type.charAt(0) == 'f')
			fill = true;
		x = sc.nextInt();
		y = sc.nextInt();
		width = sc.nextInt();
		height = sc.nextInt();
		arcwidth = sc.nextInt();
		archeight = sc.nextInt();
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
		return String.format("%s, %d, %d, %d, %d, %d, %d, %d%n", (fill ? "filledRoundRectangle" : "roundRectangle"), (int) x, (int) y,
				(int) width, (int) height, (int) arcwidth, (int) archeight, c.getRGB());
	}
	
	public static void main(String[] args) {
		RoundRectangle e = new RoundRectangle("filledRoundRectangle, 407, 247, 144, 109, 50, 50, -13369345\n");
		RoundRectangle e1 = new RoundRectangle("roundRectangle, 407, 247, 144, 109, 20, 20, -13369345\n");
		System.out.print(e);
		System.out.print(e1);

	}

}
