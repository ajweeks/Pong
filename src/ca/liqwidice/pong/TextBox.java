package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ca.liqwidice.pong.input.Keyboard.Key;

public class TextBox {

	private static final int DEFAULT_BLINK_RATE = 30;
	private static final int DEFAULT_DELAY = 45;

	private int x, y, width, height;
	private Font font;
	private Color backgroundColor, fontColor, selectionColour;
	private boolean hasFocus = false;
	private String text = "";
	private String clipboard = "";

	private int delay = 0; //how many ticks to wait after a key has been held down before printing that key repeatedly
	private int blinkRate; //The number of ticks to wait before turning cursor on/off
	private boolean cursorOn = true; //Whether or not the cursor is being shown this frame (used for blinking effect)
	private int cursorStart = 0; //The number of chars to the left of the start of the selected text(EX: wor|ds, cursorPos=3)
	private int cursorEnd = 0; //The number of chars to the left of the end of the selected text
	private int ticks = 0;
	private boolean full = false; //is true once no more characters can fit in

	//TODO add insert mode?

	public TextBox(int x, int y, int width, int height, Font font, Color backgroundColor, Color fontColor,
			Color selectionColour, int blinkRate, int delay) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = font;
		this.backgroundColor = backgroundColor;
		this.fontColor = fontColor;
		this.blinkRate = blinkRate;
		this.delay = delay;
		this.selectionColour = selectionColour;
	}

	public TextBox(int x, int y, int width, int height) {
		this(x, y, width, height, Pong.font16, Color.WHITE, Color.BLACK, Colour.SELECTED_TEXT, DEFAULT_BLINK_RATE,
				DEFAULT_DELAY);
	}

	public void update() {
		if (Pong.mouse.isLeftClicked()) {
			if (Pong.mouse.getX() > x && Pong.mouse.getX() < x + width && Pong.mouse.getY() > y
					&& Pong.mouse.getY() < y + height) {
				hasFocus = true;
				//TODO set the cursor position to the mouse position
			} else {
				hasFocus = false;
			}
		}

		if (!hasFocus) return;

		ticks++;
		if (ticks > blinkRate) {
			ticks = 0;
			cursorOn = !cursorOn;
		}

		if (Key.BACKSPACE.down > delay) backspace();
		if (Key.DEL.down > delay) del();

		Key[] keys = Key.values();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].isFunctionKey) continue;
			if (Key.CONTROL.down != -1) continue; //Don't print out characters if the user if holding down control
			String s = Key.SHIFT.down != -1 ? keys[i].TEXT : keys[i].text;
			if (keys[i].down > delay) {
				addString(s);
			} else if (keys[i].clicked) {
				addString(s);
			}
		}

		if (Key.RIGHT.clicked || Key.RIGHT.down > delay) {
			if (Key.CONTROL.down > -1) {
				if (Key.SHIFT.down > -1) { //if shift is down, leave the cursorStart where it is
					cursorEnd = text.length();
				} else {
					cursorEnd = text.length();
					cursorStart = cursorEnd;
				}
			} else moveCursorLeft();
		} else if (Key.LEFT.clicked || Key.LEFT.down > delay) {
			if (Key.CONTROL.down > -1) {
				if (Key.SHIFT.down > -1) { //if shift is down, leave the cursorEnd where it is
					cursorStart = 0;
				} else {
					cursorStart = 0;
					cursorEnd = cursorStart;
				}
			} else moveCursorRight();
		}
		cursorClamp();

		if (Key.HOME.clicked) {
			if (Key.SHIFT.down > -1) {
				cursorStart = 0;
			} else {
				cursorStart = 0;
				cursorEnd = cursorStart;
			}
		} else if (Key.END.clicked) {
			if (Key.SHIFT.down > -1) {
				cursorEnd = text.length();
			} else {
				cursorStart = text.length();
				cursorEnd = cursorStart;
			}
		}

		if (Key.CONTROL.down != -1) {
			if (Key.A.clicked) {
				if (cursorStart == 0 && cursorEnd == text.length()) {
					cursorStart = text.length();
				} else {
					cursorStart = 0;
					cursorEnd = text.length();
				}
			}

			if (Key.C.clicked) {
				clipboard = text.substring(cursorStart, cursorEnd);
			}

			if (Key.X.clicked) {
				clipboard = text.substring(cursorStart, cursorEnd);
				backspace();
			}

			if (Key.V.clicked) {
				addString(clipboard);
			}
		}

		if (Key.BACKSPACE.clicked) backspace();
		if (Key.DEL.clicked) del();
	}

	private void moveCursorLeft() {
		if (Key.SHIFT.down > -1) {
			cursorEnd++; //expand current selection
		} else {
			cursorStart++; //move cursor to the right
			cursorEnd = cursorStart;
		}
	}

	private void moveCursorRight() {
		if (Key.SHIFT.down > -1) { //expand current selection
			cursorStart--;
		} else {
			cursorStart--; //move cursor to the left
			cursorEnd = cursorStart;
		}
	}

	public void render(Graphics g) {
		g.setFont(font);
		if (hasFocus) {
			g.setColor(Color.BLUE);
			g.fillRect(x - 2, y - 2, width + 4, height + 4);
		}

		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);

		if (g.getFontMetrics().stringWidth(text + g.getFontMetrics().getMaxAdvance() * 2) > width) {
			full = true;
		} else full = false;

		if (cursorStart == cursorEnd) {
			g.setColor(fontColor);
			if (cursorOn && hasFocus) {
				g.drawString(text.substring(0, cursorStart) + "|" + text.substring(cursorEnd), x + 2, y + 16);
			} else {
				g.drawString(text.substring(0, cursorStart) + " " + text.substring(cursorEnd), x + 2, y + 16);
			}
		} else { //something is selected
			String beginning = text.substring(0, cursorStart);
			String middle = text.substring(cursorStart, cursorEnd); //selected text
			String end = text.substring(cursorEnd);
			int xo = 0;

			if (beginning.length() > 0) {
				g.setColor(fontColor);
				g.drawString(beginning, x + 2, y + 16);
			}

			xo += g.getFontMetrics().stringWidth(beginning);

			//selected text
			if (cursorStart != cursorEnd) {
				g.setColor(selectionColour);
				g.fillRect(x + cursorStart, y + cursorEnd + 2, cursorEnd - cursorStart, height - 4);
			}

			g.setColor(Color.GREEN);
			g.drawString(middle, x + xo + 2, y + 16);

			xo += g.getFontMetrics().stringWidth(middle);

			g.setColor(fontColor);
			g.drawString(cursorOn ? "|" : " ", x + xo + 2, y + 16);

			xo += g.getFontMetrics().stringWidth(cursorOn ? "|" : " ");

			if (end.length() > 0) {
				g.setColor(fontColor);
				g.drawString(end, x + xo + 2, y + 16);
			}
		}

	}

	/** Deletes one character behind the cursor if nothing is selected.
	 *  If something is selected, it is deleted */
	public void backspace() {
		if (text.length() == 0) return; //nothing to delete
		if (cursorStart == 0 && cursorEnd == text.length()) { //everything is selected
			clear();
			return;
		}
		if (cursorStart == 0) {
			if (cursorEnd == cursorStart) return;
			text = text.substring(0, cursorEnd);
			cursorClamp();
			return;
		}
		if (cursorStart == cursorEnd) {
			text = text.substring(0, cursorStart - 1) + text.substring(cursorEnd);
			cursorStart--;
		} else {
			text = text.substring(0, cursorStart) + text.substring(cursorEnd);
		}
		cursorEnd = cursorStart;
		cursorClamp();
	}

	/** Deletes one character in front of the cursor if nothing is selected.
	 *  If something is selected, it is deleted */
	public void del() {
		if (text.length() == 0) return; //nothing to delete
		if (cursorStart == 0 && cursorEnd == text.length()) { //everything is selected
			clear();
			return;
		}
		if (cursorEnd == text.length()) {
			if (cursorStart == cursorEnd) return;
			text = text.substring(0, cursorStart);
			cursorClamp();
			return;

		}
		if (cursorStart != cursorEnd) {
			backspace();
			return;
		} else {
			text = text.substring(0, cursorStart) + text.substring(cursorStart + 1);
		}
	}

	public void addString(String s) {
		if (cursorStart != cursorEnd) { //some text is selected
			backspace(); //delete it
			cursorEnd = cursorStart; //reset cursor pos
		} else if (full) return;
		text = text.substring(0, cursorStart) + s + text.substring(cursorEnd);
		cursorStart++;
		cursorEnd = cursorStart;
		cursorClamp();
	}

	private void cursorClamp() {
		//TODO implement selection direction!!
		if (text.length() == 0) {
			cursorStart = 0;
			cursorEnd = 0;
			return;
		}

		if (cursorStart < 0) {
			cursorStart = 0;
		}

		if (cursorEnd < 0) {
			cursorStart = 0;
			cursorEnd = 0;
		}

		if (cursorStart > text.length()) {
			cursorStart = text.length();
			cursorEnd = text.length();
			return;
		}

		if (cursorEnd > text.length()) {
			cursorEnd = text.length();
		}

		if (cursorEnd < cursorStart) cursorStart = cursorEnd;
		if (cursorStart > cursorEnd) cursorEnd = cursorStart;
	}

	public void clear() {
		text = "";
		cursorClamp();
	}
}
