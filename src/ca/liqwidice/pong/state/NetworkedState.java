package ca.liqwidice.pong.state;

import ca.liqwidice.pong.Pong;
import ca.liqwidice.pong.level.GameObject;

public abstract class NetworkedState extends BasicState {
	public static final byte BALL_X_POS_CHANGE = 0b0000; //0
	public static final byte BALL_Y_POS_CHANGE = 0b0001; //1
	public static final byte PADDLE_Y_POS_CHANGE = 0b0010; //2
	public static final byte PAUSE = 0b0011; //3 - TOGGLES PAUSE
	public static final byte PLAYER_1_SCORE_CHANGE = 0b0100; //4
	public static final byte PLAYER_2_SCORE_CHANGE = 0b0101; //5
	public static final byte RESET_GAME = 0b0110; //6 - USED AFTER A GAME HAS FINSISHED

	//Messages are 3 bytes long:
	//First byte = message type
	//Second and third bytes are message info

	protected GameObject game;
	protected Thread recieve;

	protected short ourLastY = 0;
	protected short theirLastY = 0;
	protected short msgContent = 0;
	protected boolean wasPaused = false; //true if we were paused last frame
	protected short lastBallX = 0;
	protected short lastBallY = 0;
	protected short lastPlayer1Score = 0;
	protected short lastPlayer2Score = 0;
	protected boolean gameWasReset = false;

	public NetworkedState(Pong pong) {
		super(pong);
	}
}
