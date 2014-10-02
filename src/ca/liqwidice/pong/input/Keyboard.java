package ca.liqwidice.pong.input;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	public static enum Key {
		//letters
		A("a", false), B("b", false), C("c", false), D("d", false), E("e", false), F("f", false), G("g", false), H("h",
				false), I("i", false), J("j", false), K("k", false), L("l", false), M("m", false), N("n", false), O(
				"o", false), P("p", false), Q("q", false), R("r", false), S("s", false), T("t", false), U("u", false), V(
				"v", false), W("w", false), X("x", false), Y("y", false), Z("z", false),

		//numbers
		NUM0("0", ")", false), NUM1("1", "!", false), NUM2("2", "@", false), NUM3("3", "#", false), NUM4("4", "$",
				false), NUM5("5", "%", false), NUM6("6", "^", false), NUM7("7", "&", false), NUM8("8", "*", false), NUM9(
				"9", "(", false),

		//other
		SPACE(" ", false), SLASH("/", "?", false), BACKSLASH("\\", "|", false), COMMA(",", "<", false), PERIOD(".",
				">", false), GRAVE("`", "~", false), LSQUARE("[", "{", false), RSQUARE("]", "}", false), SEMICOLON(";",
				":", false), APOSTROPHE("\'", "\"", false), MINUS("-", "_", false), EQUALS("=", "+", false), TAB(
				"    ", false),

		//function keys
		UP("up", true), DOWN("down", true), LEFT("left", true), RIGHT("right", true), ESC("esc", true), BACKSPACE(
				"bksp", true), DEL("del", true), SHIFT("shift", true), CONTROL("control", true);

		public final String text, TEXT;
		public boolean clicked, isFunctionKey;
		public int down = -1;

		/** @param text - the string to be printed when this key is down
		 * 	@param TEXT - the string to be printed when shift is being held as well as this key  */
		Key(String text, String TEXT, boolean isFunctionKey) {
			this.text = text;
			this.TEXT = TEXT;
			this.isFunctionKey = isFunctionKey;
		}

		Key(String s, boolean isAlphaNumeric) {
			this(s, s.toUpperCase(), isAlphaNumeric);
		}

		public void update() {
			if (clicked) clicked = false;
			if (down != -1) down++;
		}

		public void changed(boolean pressed) {
			if (down == -1 && pressed) clicked = true; //mouse wasn't down last frame but is down this frame
			else clicked = false;

			if (pressed) down++;
			else down = -1;
		}
	}

	public void update() {
		Key[] keys = Key.values();
		for (int i = 0; i < keys.length; i++) {
			keys[i].update();
		}
	}

	public Keyboard(Canvas canvas) {
		canvas.addKeyListener(this);
	}

	public void releaseAll() {
		Key[] keys = Key.values();
		for (int i = 0; i < keys.length; i++) {
			keys[i].clicked = false;
			keys[i].down = -1;
		}
	}

	private void keyUpdated(KeyEvent ke, boolean pressed) {
		Mouse.setStill(true);

		//function keys
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_UP:
			Key.UP.changed(pressed);
			break;
		case KeyEvent.VK_LEFT:
			Key.LEFT.changed(pressed);
			break;
		case KeyEvent.VK_DOWN:
			Key.DOWN.changed(pressed);
			break;
		case KeyEvent.VK_RIGHT:
			Key.RIGHT.changed(pressed);
			break;
		case KeyEvent.VK_ESCAPE:
			Key.ESC.changed(pressed);
			break;
		case KeyEvent.VK_SHIFT:
			Key.SHIFT.changed(pressed);
			break;
		case KeyEvent.VK_CONTROL:
			Key.CONTROL.changed(pressed);
			break;
		case KeyEvent.VK_BACK_SPACE:
			Key.BACKSPACE.changed(pressed);
			break;
		case KeyEvent.VK_DELETE:
			Key.DEL.changed(pressed);
			break;

		//others
		case KeyEvent.VK_SPACE:
			Key.SPACE.changed(pressed);
			break;
		case KeyEvent.VK_BACK_SLASH:
			Key.BACKSLASH.changed(pressed);
			break;
		case KeyEvent.VK_SLASH:
			Key.SLASH.changed(pressed);
			break;
		case KeyEvent.VK_PERIOD:
			Key.PERIOD.changed(pressed);
			break;
		case KeyEvent.VK_COMMA:
			Key.COMMA.changed(pressed);
			break;
		case KeyEvent.VK_QUOTE:
			Key.APOSTROPHE.changed(pressed);
			break;
		case KeyEvent.VK_MINUS:
			Key.MINUS.changed(pressed);
			break;
		case KeyEvent.VK_EQUALS:
			Key.EQUALS.changed(pressed);
			break;
		case KeyEvent.VK_DEAD_TILDE:
			Key.GRAVE.changed(pressed);
			break;
		case KeyEvent.VK_BRACELEFT:
			Key.LSQUARE.changed(pressed);
			break;
		case KeyEvent.VK_BRACERIGHT:
			Key.RSQUARE.changed(pressed);
			break;
		case KeyEvent.VK_TAB:
			Key.TAB.changed(pressed);
			break;

		//letters
		case KeyEvent.VK_A:
			Key.A.changed(pressed);
			break;
		case KeyEvent.VK_B:
			Key.B.changed(pressed);
			break;
		case KeyEvent.VK_C:
			Key.C.changed(pressed);
			break;
		case KeyEvent.VK_D:
			Key.D.changed(pressed);
			break;
		case KeyEvent.VK_E:
			Key.E.changed(pressed);
			break;
		case KeyEvent.VK_F:
			Key.F.changed(pressed);
			break;
		case KeyEvent.VK_G:
			Key.G.changed(pressed);
			break;
		case KeyEvent.VK_H:
			Key.H.changed(pressed);
			break;
		case KeyEvent.VK_I:
			Key.I.changed(pressed);
			break;
		case KeyEvent.VK_J:
			Key.J.changed(pressed);
			break;
		case KeyEvent.VK_K:
			Key.K.changed(pressed);
			break;
		case KeyEvent.VK_L:
			Key.L.changed(pressed);
			break;
		case KeyEvent.VK_M:
			Key.M.changed(pressed);
			break;
		case KeyEvent.VK_N:
			Key.N.changed(pressed);
			break;
		case KeyEvent.VK_O:
			Key.O.changed(pressed);
			break;
		case KeyEvent.VK_P:
			Key.P.changed(pressed);
			break;
		case KeyEvent.VK_Q:
			Key.Q.changed(pressed);
			break;
		case KeyEvent.VK_R:
			Key.R.changed(pressed);
			break;
		case KeyEvent.VK_S:
			Key.S.changed(pressed);
			break;
		case KeyEvent.VK_T:
			Key.T.changed(pressed);
			break;
		case KeyEvent.VK_U:
			Key.U.changed(pressed);
			break;
		case KeyEvent.VK_V:
			Key.V.changed(pressed);
			break;
		case KeyEvent.VK_W:
			Key.W.changed(pressed);
			break;
		case KeyEvent.VK_X:
			Key.X.changed(pressed);
			break;
		case KeyEvent.VK_Y:
			Key.Y.changed(pressed);
			break;
		case KeyEvent.VK_Z:
			Key.Z.changed(pressed);
			break;

		//numbers
		case KeyEvent.VK_0:
		case KeyEvent.VK_NUMPAD0:
			Key.NUM0.changed(pressed);
			break;
		case KeyEvent.VK_1:
		case KeyEvent.VK_NUMPAD1:
			Key.NUM1.changed(pressed);
			break;
		case KeyEvent.VK_2:
		case KeyEvent.VK_NUMPAD2:
			Key.NUM2.changed(pressed);
			break;
		case KeyEvent.VK_3:
		case KeyEvent.VK_NUMPAD3:
			Key.NUM3.changed(pressed);
			break;
		case KeyEvent.VK_4:
		case KeyEvent.VK_NUMPAD4:
			Key.NUM4.changed(pressed);
			break;
		case KeyEvent.VK_5:
		case KeyEvent.VK_NUMPAD5:
			Key.NUM5.changed(pressed);
			break;
		case KeyEvent.VK_6:
		case KeyEvent.VK_NUMPAD6:
			Key.NUM6.changed(pressed);
			break;
		case KeyEvent.VK_7:
		case KeyEvent.VK_NUMPAD7:
			Key.NUM7.changed(pressed);
			break;
		case KeyEvent.VK_8:
		case KeyEvent.VK_NUMPAD8:
			Key.NUM8.changed(pressed);
			break;
		case KeyEvent.VK_9:
		case KeyEvent.VK_NUMPAD9:
			Key.NUM9.changed(pressed);
			break;
		}
	}

	public void keyPressed(KeyEvent e) {
		keyUpdated(e, true);
	}

	public void keyReleased(KeyEvent e) {
		keyUpdated(e, false);
	}

	public void keyTyped(KeyEvent e) {}

}
