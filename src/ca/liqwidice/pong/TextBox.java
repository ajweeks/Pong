package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ca.liqwidice.pong.input.Keyboard.Key;

public class TextBox {

	private static final int DEFAULT_BLINK_RATE = 30;

	private int x, y, width, height;
	private Font font;
	private Color backgroundColor, fontColor;
	private String text = "";
	private boolean hasFocus = false;

	private int blinkRate; //The number of updates to wait before turning cursor on/off
	private boolean cursorOn = true; //Whether or not the cursor is being shown this frame (used for blinking effect)
	private int cursorStart = 0; //The number of chars to the left of the cursor (EX: wor|ds, cursorPos=3)
	private int cursorEnd = 0;

	private int ticks = 0;

	public TextBox(int x, int y, int width, int height, Font font, Color backgroundColor, Color fontColor, int blinkRate) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = font;
		this.backgroundColor = backgroundColor;
		this.fontColor = fontColor;
		this.blinkRate = blinkRate;
	}

	public TextBox(int x, int y, int width, int height) {
		this(x, y, width, height, Pong.font16, Color.WHITE, Color.BLACK, DEFAULT_BLINK_RATE);
	}

	public void update() {
		if (Pong.mouse.isLeftDown()) {
			if (Pong.mouse.getX() > x && Pong.mouse.getX() < x + width && Pong.mouse.getY() > y
					&& Pong.mouse.getY() < y + height) {
				hasFocus = true;
			} else {
				hasFocus = false;
			}
		}

		if (!hasFocus) return;

		if (++ticks > blinkRate) {
			ticks = 0;
			cursorOn = !cursorOn;
		}

		Key[] keys = Key.values();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].isFunctionKey) continue;

			if (keys[i].clicked) {
				if (Key.SHIFT.down) {
					addString(keys[i].TEXT);
				} else {
					addString(keys[i].text);
				}
			}
		}

		if (Key.BACKSPACE.clicked) delete();
		if (Key.DEL.clicked) deleteForwards();
	}

	public void render(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);

		if (hasFocus) {
			g.setColor(Color.GRAY);
			g.drawRect(x + 1, y + 1, width - 3, height - 3);
		}

		g.setColor(fontColor);
		g.setFont(font);

		if (cursorOn && hasFocus) {
			g.drawString(text + "|", x + 2, this.y + 15);
		} else {
			g.drawString(text, x + 2, this.y + 15);
		}
	}

	public void delete() {
		if (text.length() == 0) return;
		if (cursorStart == cursorEnd) { //no text is selected
			text = text.substring(0, text.length() - 1);
		} else {

		}
	}

	public void deleteForwards() {
		if (text.length() == 0) return;
		else text = text.substring(0, text.length() - 1);
	}

	public void addString(String s) {
		text += s;
		cursorStart = text.length();
		cursorEnd = cursorStart;
	}

	public void clear() {
		text = "";
		cursorStart = 0;
		cursorEnd = 0;
	}

}
