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
	private Color backgroundColor, fontColor;
	private boolean hasFocus = false;
	private String text = "";
	private String clipboard = "";

	private int delay = 0; //how many ticks to wait after a key has been held down before printing that key repeatedly
	private int blinkRate; //The number of updates to wait before turning cursor on/off
	private boolean cursorOn = true; //Whether or not the cursor is being shown this frame (used for blinking effect)
	private int cursorStart = 0; //The number of chars to the left of the cursor (EX: wor|ds, cursorPos=3)
	private int cursorEnd = 0; //The number of chars to the left of the current selected text
	private int ticks = 0;

	public TextBox(int x, int y, int width, int height, Font font, Color backgroundColor, Color fontColor,
			int blinkRate, int delay) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = font;
		this.backgroundColor = backgroundColor;
		this.fontColor = fontColor;
		this.blinkRate = blinkRate;
		this.delay = delay;
	}

	public TextBox(int x, int y, int width, int height) {
		this(x, y, width, height, Pong.font16, Color.WHITE, Color.BLACK, DEFAULT_BLINK_RATE, DEFAULT_DELAY);
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

		if (Key.RIGHT.clicked) {
			if (Key.SHIFT.down > -1) {
				cursorEnd++; //expand current selection
			} else {
				cursorStart++; //move cursor to the right
				cursorEnd = cursorStart;
			}
		} else if (Key.LEFT.clicked) {
			if (Key.SHIFT.down > -1) { //expand current selection
				cursorStart--;
			} else {
				cursorStart--; //move cursor to the left
				cursorEnd = cursorStart;
			}
		}
		cursorClamp();

		if (Key.CONTROL.down != -1) {
			if (Key.A.clicked) {
				cursorStart = 0;
				cursorEnd = text.length();
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

	public void render(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);

		while (g.getFontMetrics().stringWidth(text) > width) {
			text = text.substring(0, text.length());
		}
		cursorClamp();

		if (hasFocus) {
			g.setColor(Color.GRAY);
			g.drawRect(x + 1, y + 1, width - 3, height - 3);
		}

		g.setColor(fontColor);
		g.setFont(font);

		if (hasFocus) {
			if (cursorStart == cursorEnd) {
				if (cursorOn) {
					g.drawString(text.substring(0, cursorStart) + "|" + text.substring(cursorEnd), x + 2, y + 15);
				} else {
					g.drawString(text.substring(0, cursorStart) + " " + text.substring(cursorEnd), x + 2, y + 15);
				}
			} else {
				String beginning = text.substring(0, cursorStart);
				String middle = text.substring(cursorStart, cursorEnd);
				String end = text.substring(cursorEnd);
				if (beginning.length() > 0) {
					g.drawString(beginning, x + 2, y + 15);
				}

				g.setColor(Color.GREEN);
				g.drawString(middle, x + 2, y + 15);

				if (end.length() > 0) {
					g.setColor(fontColor);
					g.drawString(end, x + 2, y + 15);
				}
			}
		}
	}

	public void backspace() {
		if (text.length() == 0) return;
		if (cursorStart == 0 && cursorEnd == text.length()) {
			text = "";
			cursorEnd = 0;
			return;
		}
		//TODO check if cS == cE
		text = text.substring(0, cursorStart - 1) + text.substring(cursorEnd);
		cursorStart--;
		cursorEnd = cursorStart;
		cursorClamp();
	}

	public void del() {
		if (text.length() == 0) return;
		if (cursorStart == 0 && cursorEnd == text.length()) {
			text = "";
			cursorEnd = 0;
			return;
		}
		//TODO check if cS == cE
		if (cursorStart != cursorEnd) {
			backspace();
			return;
		} else {
			text = text.substring(0, cursorStart) + text.substring(cursorStart + 1);
		}
	}

	public void addString(String s) {
		if (cursorStart != cursorEnd) {
			backspace();
			cursorEnd = cursorStart;
		}
		int a = text.substring(0, cursorStart).length() + 1;
		text = text.substring(0, cursorStart) + s + text.substring(cursorEnd);
		cursorStart = a;
		cursorEnd = cursorStart;
		cursorClamp();
	}

	private void cursorClamp() {
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
