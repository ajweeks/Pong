package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.level.Level;

public class ClientState extends BasicState {

	private Socket socket;
	private LocalGameState gs;

	private Thread recieve;
	private Thread send;

	protected short ourLastY = 0;
	protected short ourNewY = 0;
	protected short theirLastY = 0;
	protected short theirNewY = 0;

	public ClientState(Pong pong, int port) {
		gs = new LocalGameState(pong, Level.getDefaultNetworkPVPLevel());
		try {
			socket = new Socket("localhost", 63400);
		} catch (IOException e) {
			e.printStackTrace();
		}

		send = new Thread() {
			public void run() {
				while (true) {
					try {
						if (ourNewY != ourLastY) {
							ByteBuffer buffer = ByteBuffer.allocate(4);
							buffer.putInt(ourNewY);
							byte[] msg = buffer.array();
							socket.getOutputStream().write(msg);
						}
					} catch (IOException e) {
						System.out.println("server disconnected! :(");
						endThreads();
						pong.getStateManager().enterPreviousState();
					}
				}
			}
		};

		recieve = new Thread() {
			public void run() {
				try {
					while (true) {
						byte[] msg = new byte[4];
						socket.getInputStream().read(msg);
						theirNewY = (short) ByteBuffer.wrap(msg).getInt();
						if (theirLastY != theirNewY) { //They move since the last frame
							gs.getGameObject().getLevel().movePlayer2(theirNewY);
							theirLastY = theirNewY;
						}
					}
				} catch (IOException e) {
					System.out.println("server disconnected! :(");
					endThreads();
					pong.getStateManager().enterPreviousState();
				}
			}
		};

		send.start();
		recieve.start();
	}

	public void render(Graphics g) {
		if (!socket.isConnected()) {
			g.setColor(Color.WHITE);
			g.setFont(Pong.font32);
			g.drawString("connecting...", 200, 200);
		} else {
			gs.render(g);
		}
	}

	@Override
	public void update() {
		if (socket != null && socket.isConnected()) {
			gs.update();

			ourLastY = ourNewY;
			ourNewY = (short) gs.getGameObject().getLevel().getPlayer1Y();
		}
	}

	private void endThreads() {
		try {
			gs.getGameObject().getLevel().setPaused(true);
			recieve.join();
			send.join();
		} catch (InterruptedException e) {
			System.err.println("threads could not be joined!! : " + e.getMessage());
		}
	}
}
