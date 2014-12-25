package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.level.Level;
import ca.liqwidice.pong.sound.Sound;

public class ServerState extends BasicState {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private LocalGameState gs;

	private Thread send;
	private Thread recieve;

	protected short ourLastY = 0;
	protected short ourNewY = 0;
	protected short theirLastY = 0;
	protected short theirNewY = 0;

	public ServerState(Pong pong) {
		gs = new LocalGameState(pong, Level.getDefaultNetworkPVPLevel());
		try {
			serverSocket = new ServerSocket(63400);
			System.out.println("local host is: " + InetAddress.getLocalHost());
		} catch (IOException e) {
			e.printStackTrace();
		}

		recieve = new Thread() {
			public void run() {
				try {
					clientSocket = serverSocket.accept();
					while (true) {
						byte[] msg = new byte[4];
						clientSocket.getInputStream().read(msg);
						theirNewY = (short) ByteBuffer.wrap(msg).getInt();
						if (theirNewY != theirLastY) {
							gs.getGameObject().getLevel().movePlayer2(theirNewY);
							theirLastY = theirNewY;
						}
					}
				} catch (IOException e) {
					System.out.println("client disconnected! :(");
					endThreads();
					pong.getStateManager().enterPreviousState();
				}
			}
		};

		send = new Thread() {
			public void run() {
				while (true) {
					try {
						if (clientSocket != null) {
							if (ourNewY != ourLastY) { //we have a message to send!
								ByteBuffer buffer = ByteBuffer.allocate(4);
								buffer.putInt(ourNewY);
								byte[] msg = buffer.array();
								clientSocket.getOutputStream().write(msg);
								//TODO send messages reguarding either the ball's initial pos, or it's pos every frame
							}
						}
					} catch (IOException e) {
						System.out.println("client disconnected! :(");
						endThreads();
						pong.getStateManager().enterPreviousState();
					}
				}
			}
		};

		send.start();
		recieve.start();
	}

	public void render(Graphics g) {
		if (clientSocket == null) {
			g.setColor(Color.white);
			g.setFont(Pong.font32);
			try {
				g.drawString("Waiting for someone to join at", 15, Pong.SIZE.height / 2 - 20);
				g.drawString("" + InetAddress.getLocalHost(), 15, Pong.SIZE.height / 2 + 20);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			gs.render(g);
		}
	}

	@Override
	public void update() {
		if (clientSocket != null && clientSocket.isConnected()) {
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
