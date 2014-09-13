package ca.liqwidice.pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Pong extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final Dimension SIZE = new Dimension(720, 480);
	public static Input input;

	private JFrame frame;
	private Level level;

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

		input = new Input(this);

		level = new Level();
		
		requestFocus();
	}

	public static void main(String[] args) {
		new Thread(new Pong()).start();
	}

	private void update() {
		if(input.esc.clicked) level.setPaused(!level.isPaused());
		level.update();
		input.update();
	}

	private void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SIZE.width, SIZE.height);

		level.render(g);
	}

	@Override
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
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
