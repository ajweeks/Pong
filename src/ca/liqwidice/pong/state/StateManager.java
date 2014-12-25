package ca.liqwidice.pong.state;

import java.awt.Graphics;
import java.util.Vector;

import ca.liqwidice.pong.Pong;

public class StateManager {

	private Vector<BasicState> states = new Vector<>(); //A stack of all the current states, with the current state being the last element

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
		Pong.mouse.releaseAll();
	}

	public BasicState getCurrentState() {
		return states.lastElement();
	}

	public void addState(BasicState state) {
		states.add(state);
		Pong.mouse.releaseAll();
	}

	//	public void enterState(int stateID) {
	//		Pong.keyboard.releaseAll();
	//		Pong.mouse.releaseAll();
	//		states.add(states.get(stateID));
	//	}
}
