package ca.liqwidice.pong.state;

import java.awt.Graphics;
import java.util.ArrayList;

import ca.liqwidice.pong.Pong;

public class StateManager {

	public static final int MAIN_MENU_STATE = 0;
	public static final int GAME_STATE = 1;
	public static final int SERVER_BROWSER_STATE = 2;

	private ArrayList<BasicState> states = new ArrayList<>();
	private BasicState currentState;

	public void update() {
		currentState.update();
	}

	public void render(Graphics g) {
		currentState.render(g);
	}

	public void enterState(int stateID) {
		Pong.keyboard.releaseAll();
		Pong.mouse.releaseAll();
		currentState = states.get(stateID);
	}

	public int getCurrnetStateID() {
		return currentState.getID();
	}

	public BasicState getCurrentState() {
		return currentState;
	}

	public void addState(BasicState state) {
		if (states.size() == 0) currentState = state;
		states.add(state);
	}

}
