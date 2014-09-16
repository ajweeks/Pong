package ca.liqwidice.pong.state;

import java.awt.Graphics;
import java.util.ArrayList;

public class StateManager {

	public static final int MAIN_MENU_STATE = 0;
	public static final int GAME_STATE = 1;
	
	private ArrayList<BasicState> states = new ArrayList<>();
	private BasicState currentState;

	public void update() {
		currentState.update();
	}

	public void render(Graphics g) {
		currentState.render(g);
	}

	public void enterState(int stateID) {
		currentState = states.get(stateID);
	}

	public BasicState getCurrentState() {
		return currentState;
	}

	public void addState(BasicState state) {
		if (states.size() == 0) currentState = state;
		states.add(state);
	}

}
