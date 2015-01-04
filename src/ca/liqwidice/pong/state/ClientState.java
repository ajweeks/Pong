package ca.liqwidice.pong.state;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.level.Ball;
import ca.liqwidice.pong.level.GameObject;
import ca.liqwidice.pong.level.Level;
import ca.liqwidice.pong.paddle.Paddle;
import ca.liqwidice.pong.paddle.PlayerPaddle;

/** Does nothing but poll for this user's input and send that to Server */
public class ClientState extends NetworkedState {

	private Socket socket;

	public ClientState(Pong pong, int port) {
		super(pong);
		try {
			socket = new Socket("localhost", 63400);
		} catch (IOException e) {
			e.printStackTrace();
		}

		recieve = new Thread() {
			public void run() {
				try {
					//receive seed from server
					//byte seedByte = 0b0000;
					//System.out.println("client attempting to read a message from server");
					//seedByte = (byte) socket.getInputStream().read();
					//ByteBuffer buffer = ByteBuffer.allocate(1);
					//buffer.put(seedByte);
					//byte seed = buffer.get();
					//System.out.println("client received the seed: " + seed);
					byte seed = 15; //TODO use an actual seed
					game = new GameObject(pong, new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, true, true, true),
							new PlayerPaddle(Paddle.DEFAULT_X_2, false, false, false), seed, Ball.newBall(true, seed)));
					game.getLevel().updateBall = false;
					while (true) {
						byte[] msg = new byte[3];
						socket.getInputStream().read(msg);
						ByteBuffer buffer = ByteBuffer.wrap(msg);
						byte msgType = buffer.get(0);
						msgContent = buffer.getShort(1);
						switch (msgType) {
						case BALL_X_POS_CHANGE:
							game.getLevel().getBall().setX(msgContent);
							break;
						case BALL_Y_POS_CHANGE:
							game.getLevel().getBall().setY(msgContent);
							break;
						case PADDLE_Y_POS_CHANGE:
							game.getLevel().getPlayer2().setY(msgContent);
							break;
						case PAUSE:
							boolean paused = msgContent == 1; //1 represents paused, 0 represents not paused
							game.getLevel().setPaused(paused);
							break;
						case PLAYER_1_SCORE_CHANGE:
							game.getLevel().setPlayer1Score(msgContent);
							break;
						case PLAYER_2_SCORE_CHANGE:
							game.getLevel().setPlayer2Score(msgContent);
							break;
						case RESET_GAME:
							game.getLevel().resetGame();
							break;
						default:
							throw new IllegalArgumentException("Unknown message type: " + msgType);
						}
					}
				} catch (IOException e) {
					System.out.println("Server disconnected! :(");
					pong.getStateManager().enterPreviousState();
				}
			}
		};

		recieve.start();
	}

	public void render(Graphics g) {
		if (!socket.isConnected() || game == null) {
			g.setColor(Color.WHITE);
			g.setFont(Pong.font32);
			g.drawString("connecting...", 200, 200);
		} else {
			game.render(g);
		}
	}

	@Override
	public void update() {
		if (socket == null || game == null) {
			if (Key.ESC.clicked) pong.getStateManager().enterPreviousState();
			return;
		}

		game.update();

		//Send
		try {
			if (socket != null) {
				//PADDLE_POS_CHANGE
				short ourNewY = (short) game.getLevel().getPlayer1().getY();
				if (ourNewY != ourLastY) {
					ByteBuffer buffer = ByteBuffer.allocate(3);
					buffer.put(PADDLE_Y_POS_CHANGE);
					buffer.putShort(ourNewY);
					byte[] msg = buffer.array();
					socket.getOutputStream().write(msg);

					ourLastY = ourNewY;
				}
				//PAUSE
				boolean isPaused = game.getLevel().isPaused();
				if (wasPaused != isPaused) {
					ByteBuffer buffer = ByteBuffer.allocate(3);
					buffer.put(PAUSE);
					if (isPaused) buffer.put(2, (byte) 1);
					else buffer.put(2, (byte) 0);
					byte[] msg = buffer.array();
					socket.getOutputStream().write(msg);

					wasPaused = isPaused; //Equivalent to wasPaused = !wasPaused;
				}
			}
		} catch (IOException e) {
			System.out.println("server disconnected! :(");
			pong.getStateManager().enterPreviousState();
		}
	}
}
