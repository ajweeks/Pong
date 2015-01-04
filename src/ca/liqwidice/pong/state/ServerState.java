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
import ca.liqwidice.pong.input.Keyboard.Key;
import ca.liqwidice.pong.level.Ball;
import ca.liqwidice.pong.level.GameObject;
import ca.liqwidice.pong.level.Level;
import ca.liqwidice.pong.paddle.Paddle;
import ca.liqwidice.pong.paddle.PlayerPaddle;

/** Does everything, tells Client what's up */
public class ServerState extends NetworkedState {
	private ServerSocket serverSocket;
	private Socket clientSocket;

	public ServerState(Pong pong) {
		super(pong);
		try {
			serverSocket = new ServerSocket(0, 1, InetAddress.getLocalHost());
		} catch (IOException e) {
			System.err.println("A server is already running on this port!!!");
			terminate();
			return;
		}

		recieve = new Thread() {
			public void run() {
				try {
					clientSocket = serverSocket.accept();
					////send client seed
					//byte seed = (byte) (Math.random() * 127);
					//System.out.println("server choose the seed: " + seed);
					//clientSocket.getOutputStream().write(seed);

					byte seed = 15;
					game = new GameObject(pong, new Level(new PlayerPaddle(Paddle.DEFAULT_X_1, true, true, true),
							new PlayerPaddle(Paddle.DEFAULT_X_2, false, false, false), seed, Ball.newBall(false, seed)));
					while (true) {
						byte[] msg = new byte[3];
						clientSocket.getInputStream().read(msg);
						byte msgType = ByteBuffer.wrap(msg).get(0);
						msgContent = ByteBuffer.wrap(msg).getShort(1);
						switch (msgType) {
						case PADDLE_Y_POS_CHANGE:
							game.getLevel().getPlayer2().setY(msgContent);
							break;
						case PAUSE:
							boolean paused = msgContent == 1; //1 represents paused, 0 represents not paused
							game.getLevel().setPaused(paused);
							break;
						default:
							throw new IllegalArgumentException("Unknown message type: " + msgType);
						}
					}
				} catch (IOException e) {
					System.out.println("Client disconnected! :(");
					pong.getStateManager().enterPreviousState();
				}
			}
		};

		recieve.start();
	}

	public void render(Graphics g) {
		if (clientSocket == null || game == null) {
			g.setColor(Color.white);
			g.setFont(Pong.font32);
			if (serverSocket != null) {
				g.drawString("Waiting for someone to join at ", 25, Pong.SIZE.height / 2 + 10);
				try {
					g.drawString("host name: " + ("" + InetAddress.getLocalHost()).split("/")[0] + ", port: "
							+ serverSocket.getLocalPort(), 25, Pong.SIZE.height / 2 + 40);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				g.drawString("Port in use, Press esc", 150, 230);
			}
		} else {
			game.render(g);
		}
	}

	@Override
	public void update() {
		if (clientSocket == null || game == null) {
			if (Key.ESC.clicked) terminate();
			return;
		}

		game.update();

		//Send
		if (clientSocket != null) {
			//BALL_X_POS_CHANGE
			short ourBallX = (short) game.getLevel().getBall().getX();
			short newBallX = (short) (Pong.SIZE.width - ourBallX);
			//Our Ball's x pos is on the wrong side of the screen for the client 
			//TODO make client play on right side of screen?
			if (lastBallX != newBallX) {
				sendMessage(BALL_X_POS_CHANGE, newBallX);
				lastBallX = newBallX;
			}
			//BALL_Y_POS_CHANGE
			short newBallY = (short) game.getLevel().getBall().getY();
			if (lastBallY != newBallY) {
				sendMessage(BALL_Y_POS_CHANGE, newBallY);
				lastBallY = newBallY;
			}
			short ourNewY = (short) game.getLevel().getPlayer1().getY();
			//PADDLE_Y_POS_CHANGE
			if (ourNewY != ourLastY) {
				sendMessage(PADDLE_Y_POS_CHANGE, ourNewY);
				ourLastY = ourNewY;
			}
			//PAUSE
			boolean isPaused = game.getLevel().isPaused();
			if (wasPaused != isPaused) {
				byte msg = isPaused ? (byte) 1 : 0;
				sendMessage(PAUSE, msg);
				wasPaused = isPaused; //Equivalent to wasPaused = !wasPaused;
			}
			//PLAYER_1_SCORE_CHANGE (the client's player 2)
			short player1Score = game.getLevel().getPlayer1Score();
			if (lastPlayer1Score != player1Score) {
				sendMessage(PLAYER_2_SCORE_CHANGE, player1Score);
				lastPlayer1Score = player1Score;
			}
			//PLAYER_2_SCORE_CHANGE (the client's player 1)
			short player2Score = game.getLevel().getPlayer2Score();
			if (lastPlayer2Score != player2Score) {
				sendMessage(PLAYER_1_SCORE_CHANGE, player2Score);
				lastPlayer2Score = player2Score;
			}
			//RESET_GAME
			gameWasReset = game.getLevel().wasReset();
			if (gameWasReset) {
				sendMessage(RESET_GAME, (short) 0);
				game.getLevel().setWasReset(false);
			}
		}
	}

	private void sendMessage(byte type, short content) {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.put(0, type);
		buffer.putShort(1, content);
		byte[] msg = buffer.array();
		try {
			clientSocket.getOutputStream().write(msg);
		} catch (IOException e) {
			System.out.println("client disconnected! :(");
			terminate();
		}
	}

	private void terminate() {
		//TODO make threads die after either the client or server quits a game
		pong.getStateManager().enterPreviousState();
	}
}
