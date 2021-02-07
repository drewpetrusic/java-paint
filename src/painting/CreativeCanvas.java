package painting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import shapes.Curve;
import shapes.Ellipse;
import shapes.Line;
import shapes.Polygon2;
import shapes.Rectangle;
import shapes.RoundRectangle;

public class CreativeCanvas extends JFrame implements ActionListener, ItemListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private ArrayList<Shape> shapes;
	private String shapeType = "Line";
	private boolean fill;
	private Color color;
	private RoundRectangle arcwidth;
	private RoundRectangle archeight;
	private JTextField[] mouseStates;
	private String[] text = { "Pressed", "Clicked", "Released", "Entered", "Exited", "Dragged", "X:", "Y:" };
	private JPanel pSouth;
	private Painting canvas;
	private List<Shape> clipboard = new ArrayList<>(), selectedShapes;
	private Shape undoClipboard, selectRectangle;
	private Curve drawingCurve;
	private File saveFile;
	private int x, y;
	private List<Integer> xPoints = new ArrayList<>(), yPoints = new ArrayList<>();

	public CreativeCanvas() {
		super("Drawing");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(createMenuBar());
		setLayout(new BorderLayout());
		shapes = new ArrayList<Shape>();
		color = Color.black;
		shapeType = "";
		canvas = new Painting(1200, 800, shapes);
		SwingUtilities.updateComponentTreeUI(canvas);
		canvas.addMouseListener(new MyMouseListener());
		canvas.addMouseMotionListener(new MyMouseMotionListener());
		canvas.addKeyListener(new MyKeyListener());
		add("Center", canvas);
		// Create the array of text fields.
		pSouth = new JPanel();
		mouseStates = new JTextField[8];
		for (int i = 0; i < mouseStates.length; i++) {
			mouseStates[i] = new JTextField(text[i], 10);
			mouseStates[i].setEditable(false);
			pSouth.add(mouseStates[i]);
		}

		add("South", pSouth);
		pack();
		setVisible(true);
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		String[] commands = { "Open", "Save", "Save As..", "Clear Screen" };
		int[] keyEvents = { KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_C };

		for (int i = 0; i < commands.length; i++) {
			menuItem = new JMenuItem(commands[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvents[i], ActionEvent.ALT_MASK));
			menuItem.addActionListener(this);
			fileMenu.add(menuItem);
		}
		menuBar.add(fileMenu);

		JMenu drawMenu = new JMenu("Draw");
		fileMenu.setMnemonic(KeyEvent.VK_D);
		cbMenuItem = new JCheckBoxMenuItem("Grid", true);
		cbMenuItem.addItemListener(this);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke('g'));
		drawMenu.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Fill", false);
		cbMenuItem.setAccelerator(KeyStroke.getKeyStroke('f'));
		cbMenuItem.addItemListener(this);
		drawMenu.add(cbMenuItem);
		menuItem = new JMenuItem("Edit Color");
		menuItem.setAccelerator(KeyStroke.getKeyStroke('c'));
		menuItem.addActionListener(this);
		drawMenu.add(menuItem);

		menuItem = new JMenuItem("Select");
		menuItem.setAccelerator(KeyStroke.getKeyStroke('s'));
		menuItem.addActionListener(this);
		drawMenu.add(menuItem);

		String[] cmds = { "Line", "Ellipse", "Rectangle" };
		int[] drawKeyEvents = { KeyEvent.VK_L, KeyEvent.VK_C, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T,
				KeyEvent.VK_P };
		for (int i = 0; i < cmds.length; i++) {
			menuItem = new JMenuItem(cmds[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(drawKeyEvents[i], ActionEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			drawMenu.add(menuItem);
		}
		menuBar.add(drawMenu);

		JMenu editMenu = new JMenu("Edit");
		String[] commandsedit = { "Cut", "Copy", "Paste", "Undo", "Redo" };
		int[] keyEventsedit = { KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_Z, KeyEvent.VK_R };

		for (int i = 0; i < commandsedit.length; i++) {
			menuItem = new JMenuItem(commandsedit[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEventsedit[i], ActionEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			editMenu.add(menuItem);
		}
		menuBar.add(editMenu);

		return menuBar;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBoxMenuItem source = (JCheckBoxMenuItem) (e.getSource());
		switch (source.getText()) {
		case "Fill":
			fill = source.getState();
			break;
		case "Grid":
			if (source.getState())
				canvas.showGrid();
			else
				canvas.hideGrid();
			repaint();
			break;
		}
		// temporary feedback
		String eventData = "Item event detected.\n" + "    Event source: " + source.getText() + " (an instance of "
				+ source.getClass() + ")\n" + "    New state: "
				+ ((e.getStateChange() == ItemEvent.SELECTED) ? "selected" : "unselected");
		System.out.println(eventData);
		System.out.printf("shapeType : %s, fill : %b, color : %d%n%n", shapeType, fill, color.getRGB());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.contentEquals("Edit Color")) {
			color = JColorChooser.showDialog(this, "Select Color", color);
			pSouth.setBackground(color);
		}

		switch (command) {
		case "Line":
		case "Ellipse":
		case "Rectangle":
		case "Select":
			System.out.println("Foo");
			shapeType = command;
			break;
		}

		switch (command) {
		case "Cut":
			this.clipboard.clear();
			this.clipboard.addAll(selectedShapes);
			selectedShapes.forEach(shapes::remove);
			this.repaint();
			break;
		case "Copy":
			this.clipboard.clear();
			this.clipboard.addAll(selectedShapes);
			break;
		case "Paste":
			this.shapeType = "paste";
//			shapes.addAll(this.clipboard);
			break;
		case "Undo":
			this.undoClipboard = this.shapes.get(this.shapes.size() - 1);
			this.shapes.remove(this.undoClipboard);
			break;
		case "Redo":
			this.shapes.add(undoClipboard);
			break;
		case "Save As..":
			saveFile = null;
		case "Save":
			if (saveFile == null) {
				var chooser = new JFileChooser(System.getProperty("user.dir"));
				var choice = chooser.showSaveDialog(this);
				if (choice == JFileChooser.APPROVE_OPTION) {
					this.saveFile = chooser.getSelectedFile();
					try {
						var writer = new FileWriter(this.saveFile);
						for (var s : this.shapes) {
							writer.write(s.toString());
						}
						writer.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			break;
		}

		// temporary feedback
		Object o = e.getSource();
		String className = o.getClass().getName().substring(o.getClass().getName().lastIndexOf('.') + 1);
		System.out.printf("Event: %s%n    ActionCommand: %s%n   Source class: %s%n", e.getClass(), command, className);
		System.out.printf("shapeType : %s, fill : %b, color : %d%n%n", shapeType, fill, color.getRGB());
	}

	/**
	 * The clearTextFields method sets all of the text backgrounds to light gray.
	 */

	public void clearTextFields() {
		for (int i = 0; i < 6; i++)
			mouseStates[i].setBackground(Color.lightGray);
	}

	final class MouseClickPoint {
		final int x, y;

		public MouseClickPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public boolean fuzzyEquals(int x, int y, int leniency) {
			return (x >= leniency || x <= leniency) && (y >= leniency || y <= leniency);
		}

	}

	class MyKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10 && shapeType.equalsIgnoreCase("polygon")) {
				var intx = xPoints.stream().mapToInt(i -> i).toArray();
				var inty = yPoints.stream().mapToInt(i -> i).toArray();
				Stream.of(intx,inty
				shapes.add(new Polygon2(fill, color, )));
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	// Inner listener classes
	class MyMouseListener implements MouseListener {
		private boolean polygonListener = false;
		private List<MouseClickPoint> clickList = new ArrayList<MouseClickPoint>();
		private int x1, y1;

		public void mousePressed(MouseEvent e) {
			clearTextFields();
			mouseStates[0].setBackground(Color.yellow);
			x = e.getX();
			y = e.getY();

			if (shapeType.equalsIgnoreCase("curve")) {
				drawingCurve = new Curve(x, y, color);
			}

		}

		public void mouseClicked(MouseEvent e) {
			clearTextFields();
			mouseStates[1].setBackground(Color.yellow);
			switch (shapeType) {
			case "Select":
				break;
			case "Paste":
				if (!clipboard.isEmpty()) {
					shapes.addAll(clipboard);
				}
				break;
			case "Polygon":
				xPoints.add(e.getX());
				yPoints.add(e.getY());
				break;
			}

		}

		public void mouseEntered(MouseEvent e) {
			clearTextFields();
			mouseStates[3].setBackground(Color.yellow);
		}

		public void mouseExited(MouseEvent e) {
			clearTextFields();
			mouseStates[4].setBackground(Color.yellow);
		}

		public void mouseReleased(MouseEvent e) {
			clearTextFields();
			mouseStates[2].setBackground(Color.yellow);
			x1 = e.getX();
			y1 = e.getY();
			switch (shapeType) {
			case "Line":
				shapes.add(new Line(x, y, x1, y1, color.getRGB()));
				break;
			case "Ellipse":
				shapes.add(new Ellipse(x, y, x1, y1, color, fill));
				break;
			case "Rectangle":
				shapes.add(new Rectangle(x, y, x1, y1, color, fill));
				break;
			case "Round Rectangle":
				break;
			}
			repaint();
		}
	}

	class MyMouseMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			clearTextFields();
			mouseStates[5].setBackground(Color.yellow);
			mouseStates[6].setText("X: " + e.getX());
			mouseStates[7].setText("Y: " + e.getY());

			switch (shapeType) {
			case "Curve":
				drawingCurve.lineTo(e.getX(), e.getY());
				repaint();
				break;
			case "Select":
				selectRectangle = new Rectangle(x, y, e.getX(), e.getY(), color);
				repaint();
				break;
			}
		}

		public void mouseMoved(MouseEvent e) {
			mouseStates[6].setText("X: " + e.getX());
			mouseStates[7].setText("Y: " + e.getY());
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception unused) {
			;
		}
		new CreativeCanvas();

	}

}
