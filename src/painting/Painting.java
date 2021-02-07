
package painting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import shapes.Curve;
import shapes.Ellipse;
import shapes.Line;
import shapes.Polygon2;
import shapes.Rectangle;
import shapes.RoundRectangle;

public class Painting extends JComponent {

	private static final long serialVersionUID = 1L;

	private Grid grid;
	private ArrayList<Shape> shapes;
	private RoundRectangle2D.Double selectRectangle;
	private float dash1[] = { 10.0f };
	private BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
			0.0f);

	public Painting(int w, int h) {
		setPreferredSize(new Dimension(w, h));
		grid = new Grid(0, 0, w, h);
	}

	public Painting(int w, int h, ArrayList<Shape> shapes) {
		this(w, h);
		this.shapes = shapes;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		grid.display(g2);

		for (Shape shape : shapes) {

			if (shape instanceof Line) {
				var line = (Line) shape;
				g2.setColor(line.getDrawColor());
				g2.draw(line); // g2.draw(s)
			}

			if (shape instanceof Ellipse) {
				var ellipse = (Ellipse) shape;
				g2.setColor(ellipse.getDrawColor());
				if (ellipse.isFill()) {
					g2.fill(ellipse);
				} else {
					g2.draw(ellipse);
				}
			}

			if (shape instanceof Rectangle) {
				var rectangle = (Rectangle) shape;
				g2.setColor(rectangle.getDrawColor());
				if (rectangle.isFill()) {
					g2.fill(rectangle);
				} else {
					g2.draw(rectangle);
				}
			}

			if (shape instanceof RoundRectangle) {
				var roundrectangle = (RoundRectangle) shape;
				g2.setColor(roundrectangle.getDrawColor());
				if (roundrectangle.isFill()) {
					g2.fill(roundrectangle);
				} else {
					g2.draw(roundrectangle);
				}
			}

			if (shape instanceof Polygon2) {
				var polygon = (Polygon2) shape;
				g2.setColor(polygon.getDrawColor());
				if (polygon.isFill()) {
					g2.fill(polygon);
				} else {
					g2.draw(polygon);
				}
			}

			if (shape instanceof Curve) {
				var curve = (Curve) shape;
				g2.setColor(curve.getDrawColor());
				g2.draw(curve);
			}

			if (selectRectangle != null) {
				// draw RoundRectangle2D.Double
				g2.setStroke(dashed);
				g2.setColor(Color.black);
				g2.draw(selectRectangle);
			}
		}

	}

	public static void main(String[] args) {
		Painting canvas = new Painting(800, 800);
		String name = canvas.loadPainting();

		// JFRAME
		JFrame frame = new JFrame();
		frame.setTitle("Painting - " + name);
		frame.getContentPane().setBackground(Color.white);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public String loadPainting() {
		Scanner sc = null; //
		String name = "no file found - running demo string ";
		String defaultDrawing = "circle, 175, 600, 30, 30, -65536\n" + "filledCircle, 290, 520, 40, 40, -256\n"
				+ "circle, 375, 425, 47, 47, -16711936\n" + "filledCircle, 475, 325, 48, 48, -16776961\n"
				+ "circle, 550, 225, 47, 47, -256\n" + "filledCircle, 625, 100, 48, 48, -65536\n"
				+ "50, 50, 587, 100, -13421569\n" + "50, 200, 587, 100, -13421569\n"
				+ "roundRectangle, 407, 247, 144, 109, 20, 20, -13369345\n" + "rectangle, 277, 29, 70, 36, -13369345\n"
				+ "filledRectangle, 125, 100, 48, 48, -65536\n"
				+ "polygon, -256, 250, 250, 200, 300, 250, 350, 300, 300\n"
				+ "filledPolygon, -256, 250, 250, 200, 300, 250, 350, 300, 300\n"
				+ "filledRectangle, 277, 29, 70, 36, -13369345\n"
				+ "filledRoundRectangle, 407, 247, 144, 109, 50, 50, -13369345\n"
				+ "curve,-3407821,167,165,166,165,166,165,166,164,166,163,166,162,166,161,166,160,167,160,167,161,167,161,168,162,169,162,170,162,"
				+ "171,162,171,162,172,162,173,163,174,163,175,163,175,163,176,163,177,163,177,162,177,160,177,158,177,156,177,154,177,153,177,152,177,151\n";

		if (shapes == null)
			shapes = new ArrayList<Shape>();
		JFileChooser jfc = new JFileChooser(".");
		int choice = jfc.showOpenDialog(null);
		try {
			if (choice == JFileChooser.APPROVE_OPTION) {
				name = jfc.getSelectedFile().getName();
				if (grid.isVisible())
					grid.toggleGrid();
				sc = new Scanner(jfc.getSelectedFile());
			} else {
				sc = new Scanner(defaultDrawing);
			}

		} catch (FileNotFoundException e) {
			sc = new Scanner(defaultDrawing);
			e.printStackTrace();
		} finally {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.length() > 1) {
					String type = line.substring(0, line.indexOf(',')).strip();
					switch (type) {
					case "filledCircle":
					case "circle":
						shapes.add(new Ellipse(line));
						break;
					case "filledRectangle":
					case "rectangle":
						shapes.add(new Rectangle(line));
						break;
					case "filledPolygon":
					case "polygon":
						shapes.add(new Polygon2(line));
						break;
					case "curve":
						shapes.add(new Curve(line));
						break;
					case "roundRectangle":
					case "filledRoundRectangle":
						shapes.add(new RoundRectangle(line));
						break;
					default:
						shapes.add(new Line(line));
						break;
					}
				}
			}
			sc.close();
		}
		return name;
	}

	public void removeSelectRectangle() {
		selectRectangle = null;
	}

	public void setSelectRectangle(RoundRectangle2D.Double cr) {
		selectRectangle = cr;
	}

	public void showGrid() {
		if (!grid.isVisible())
			grid.toggleGrid();
	}

	public void hideGrid() {
		if (grid.isVisible())
			grid.toggleGrid();
	}

}
