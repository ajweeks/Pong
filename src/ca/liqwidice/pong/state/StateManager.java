package ca.liqwidice.pong.state;

import java.awt.Graphics;
import java.util.Vector;

public class StateManager {

	private Vector<BasicState> states = new Vector<>();

	public StateManager(BasicState firstState) {
		states.add(firstState);
	}

	public void update() {
		states.lastElement().update();
	}

	public void render(Graphics g) {
		states.lastElement().render(g);
	}

	public void enterPreviousState() {
		if (states.size() > 1) states.remove(states.lastElement());
	}

	public BasicState getCurrentState() {
		return states.lastElement();
	}

	public void addState(BasicState state) {
		states.add(state);
	}

	//	public void enterState(int stateID) {
	//		Pong.keyboard.releaseAll();
	//		Pong.mouse.releaseAll();
	//		states.add(states.get(stateID));
	//	}
}
