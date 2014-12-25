package ca.liqwidice.pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ca.liqwidice.pong.input.Keyboard;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.input.Mouse;
import ca.liqwidice.pong.state.MainMenuState;
import ca.liqwidice.pong.state.StateManager;

public class Pong extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final Dimension SIZE = new Dimension(720, 480);
	public static Keyboard keyboard;
	public static Mouse mouse;
	public static Font font32 = new Font("Consolas", Font.BOLD, 32);
	public static Font font16 = font32.deriveFont(16.0f);
	public static Cursor blankCursor;

	private JFrame frame;
	private StateManager sm;
	private Image icon = new ImageIcon("res/icon.png").getImage();

	private boolean renderDebug = false;
	private boolean running = false;
	private int fps = 0;

	public Pong() {
		super();
		setSize(SIZE);
		setFocusable(true);

		frame = new JFrame("Pong");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setIconImage(icon);
		frame.setFocusable(true);

		keyboard = new Keyboard(this);
		mouse = new Mouse(this);

		sm = new StateManager(new MainMenuState(this));

		blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");

		requestFocus();
	}

	public static void main(String[] args) {
		new Thread(new Pong()).start();
	}

	private void update() {
		if (Key.W.clicked && Key.CONTROL.down > -1) stop();
		if (Key.F3.clicked) renderDebug = !renderDebug;

		sm.update();
		keyboard.update();
		mouse.update();
	}

	private void render(Graphics g) {
		g.setColor(Colour.OFF_BLACK);
		g.fillRect(0, 0, Pong.SIZE.width, Pong.SIZE.height);

		sm.render(g);
	}

	public void run() {
		running = true;

		long before = System.nanoTime();
		long elapsed = 0;
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			elapsed += now - before;
			before = now;

			update();
			frames++;

			BufferStrategy buffer = getBufferStrategy();
			if (buffer == null) {
				createBufferStrategy(2);
				continue;
			}
			Graphics g = buffer.getDrawGraphics();

			render(g);

			if (elapsed > 1_000_000_000) {
				elapsed = 0;
				fps = frames;
				frames = 0;
			}

			if (renderDebug) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Consolas", Font.BOLD, 16));
				g.drawString(fps + " FPS", 8, 16);
			}

			g.dispose();
			buffer.show();

			try {
				Thread.sleep(15); //TODO implement an actual game loop
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		frame.dispose();
		System.exit(0);
	}

	/** sets current cursor to c, unless c is null, then to the default cursor */
	public void setCursor(Cursor c) {
		frame.setCursor(c);
	}

	public StateManager getStateManager() {
		return sm;
	}

	public void stop() {
		running = false;
	}
}
