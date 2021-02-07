package shapes;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Polygon2 extends Polygon {

	private static final long serialVersionUID = 1L;
	private Color c;
	private boolean fill;

	public static void main(String[] args) {
		var p = new Polygon2("filledpolygon,3,2,2,3,3,4,4,5,5");
		System.out.println(p.toString());
	}

	public static Polygon2 create(boolean fill, Color c, int... points) {
		StringBuilder sb = new StringBuilder(fill ? "filledPolygon," : "polygon,");
		sb.append(c.getRGB());
		Arrays.asList(points).forEach(p -> sb.append(",").append(p));
		return new Polygon2(sb.toString());
	}

	public Polygon2(String line) {
		Scanner sc = new Scanner(line.strip());
		sc.useDelimiter(",\\s*");
		String type = sc.next();
		this.fill = type.equalsIgnoreCase("filledPolygon");
		this.c = new Color(Integer.parseInt(sc.next()));

		var xPointsList = new ArrayList<Integer>();
		var yPointsList = new ArrayList<Integer>();

		boolean one = true;
		while (sc.hasNext()) {
			if (one) {
				xPointsList.add(sc.nextInt());
			} else {
				yPointsList.add(sc.nextInt());
			}
			one = !one;
		}

		int[] xPoints = new int[xPointsList.size()];
		for (int i = 0; i < xPoints.length; i++) {
			xPoints[i] = xPointsList.get(i);
		}

		int[] yPoints = new int[yPointsList.size()];
		for (int i = 0; i < yPoints.length; i++) {
			yPoints[i] = yPointsList.get(i);
		}

		this.xpoints = xPoints;
		this.ypoints = yPoints;
		this.npoints = xPoints.length > yPoints.length ? xPoints.length : yPoints.length; // npoints is valid number of
																							// points

	}

	public Color getDrawColor() {
		return c;
	}

	public boolean isFill() {
		return fill;
	}

	public String toString() {
		String typeColor = (fill ? "filledPolygon," : "polygon,") + c.getRGB();
		String xy = "";
		for (int i = 0; i < npoints; i++) {
			xy += "," + xpoints[i] + "," + ypoints[i];
		}
		return String.format("%s%s%n", typeColor, xy);
	}

}
