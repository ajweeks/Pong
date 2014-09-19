package ca.liqwidice.pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import ca.liqwidice.pong.input.Keyboard;
import ca.liqwidice.pong.input.Mouse;
import ca.liqwidice.pong.state.GameState;
import ca.liqwidice.pong.state.MainMenuState;
import ca.liqwidice.pong.state.StateManager;

public class Pong extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final Dimension SIZE = new Dimension(720, 480);
	public static Keyboard keyboard;
	public static Mouse mouse;
	public static Font font32 = new Font("Consolas", Font.BOLD, 32);

	private JFrame frame;
	private StateManager sm;

	private boolean running = false;

	public Pong() {
		super();
		setSize(SIZE);

		frame = new JFrame("Pong");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		keyboard = new Keyboard(this);
		mouse = new Mouse(this);

		sm = new StateManager();
		sm.addState(new MainMenuState(this));
		sm.addState(new GameState(Level.EASY));
		sm.addState(new GameState(Level.MED));
		sm.addState(new GameState(Level.HARD));

		requestFocus();
	}

	public static void main(String[] args) {
		new Thread(new Pong()).start();
	}

	private void update() {
		sm.update();
	}

	private void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SIZE.width, SIZE.height);
		sm.render(g);
	}

	public void run() {
		running = true;

		long before = System.nanoTime();
		long elapsed = 0;
		int frames = 0;
		int fps = 0;
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

			g.setColor(Color.WHITE);
			g.setFont(new Font("Consolas", Font.BOLD, 16));
			g.drawString(fps + " FPS", 8, 16);

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

	public StateManager getStateManager() {
		return sm;
	}

	public void stop() {
		running = false;
	}
}
