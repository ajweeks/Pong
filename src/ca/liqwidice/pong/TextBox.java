package ca.liqwidice.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ca.liqwidice.pong.input.Keyboard;
import ca.liqwidice.pong.input.Keyboard.Key;

public class TextBox {

	private static final int DEFAULT_BLINK_RATE = 25;
	private static final int DEFAULT_DELAY = 30;

	private int x, y, width, height;
	private Font font;
	private Color backgroundColour, selectedBackgroundColour, textColour, selectedTextColour, promptTextColour;
	private boolean hasFocus = false;
	private String text = "";
	private String promptText = "";
	private String clipboard = "";

	private int delay = 0; //how many ticks to wait after a key has been held down before printing that key repeatedly
	private int blinkRate; //The number of ticks to wait before turning cursor on/off
	private String cursor = ""; //The current cursor (can be "|", "_", or " ")
	private int cursorStart = 0; //The number of chars to the left of the start of the current selection
	private int cursorEnd = 0; //The number of chars to the left of the end of the current selection
	private int ticks = 0;
	private boolean full = false; //is true once no more characters can fit in
	private boolean acceptsNumbers = true;
	private boolean acceptsLetters = true;
	private boolean acceptsSpecialCharacters = true;

	public TextBox(int x, int y, int width, int height, Font font, Color backgroundColor,
			Color selectionBackgroundColour, Color fontColor, Color selectionColour, Color promptTextColour,
			int blinkRate, int delay) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.font = font;
		this.backgroundColour = backgroundColor;
		this.selectedBackgroundColour = selectionBackgroundColour;
		this.textColour = fontColor;
		this.selectedTextColour = selectionColour;
		this.promptTextColour = promptTextColour;
		this.blinkRate = blinkRate;
		this.delay = delay;
	}

	public TextBox(int x, int y, int width, int height) {
		this(x, y, width, height, Pong.font16, Color.WHITE, Colour.SELECTED_TEXT_BG, Color.BLACK, Colour.SELECTED_TEXT,
				Colour.PROMPT_TEXT, DEFAULT_BLINK_RATE, DEFAULT_DELAY);
	}

	private void turnCursorOn() {
		ticks = 0;
		if (Keyboard.insert) cursor = "_";
		else cursor = "|";
	}

	public void update() {
		if (Pong.mouse.isLeftClicked()) {
			if (Pong.mouse.getX() > x && Pong.mouse.getX() < x + width && Pong.mouse.getY() > y
					&& Pong.mouse.getY() < y + height) {
				hasFocus = true;
			} else {
				hasFocus = false;
			}
		}

		if (!hasFocus) {
			cursor = "";
			return;
		}

		if (ticks++ > blinkRate) {
			ticks = 0;
			if (!hasFocus) cursor = "";
			else if (Keyboard.insert) {
				if (cursor.equals("")) cursor = "_";
				else cursor = "";
			} else {
				if (cursor.equals("")) cursor = "|";
				else cursor = "";
			}
			//turnCursorOn();
		}

		if (Key.BACKSPACE.down > delay) backspace();
		if (Key.DEL.down > delay) del();

		Key[] keys = Key.values();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].isFunctionKey) continue; //Don't print out function keys!
			if (Key.CONTROL.down != -1) continue; //Don't print out characters if the user if holding down control
			if (keys[i].down > delay) {
				addCharacter(keys[i]);
			} else if (keys[i].clicked) {
				addCharacter(keys[i]);
			}
		}

		if (Key.RIGHT.clicked || Key.RIGHT.down > delay) {
			turnCursorOn();
			if (Key.CONTROL.down > -1) {
				if (Key.SHIFT.down > -1) {
					cursorEnd = nextSpaceToRight(cursorEnd);
				} else { //control is down, but shift isn't
					cursorEnd = nextSpaceToRight(cursorEnd);
					cursorStart = cursorEnd;
				}
			} else moveCursorRight();
		} else if (Key.LEFT.clicked || Key.LEFT.down > delay) {
			turnCursorOn();
			if (Key.CONTROL.down > -1) {
				if (Key.SHIFT.down > -1) {
					cursorEnd = nextSpaceToLeft(cursorEnd);
				} else { //control is down, but shift isn't
					cursorEnd = nextSpaceToLeft(cursorEnd);
					cursorStart = cursorEnd;
				}
			} else moveCursorLeft();
		}
		cursorClamp();

		if (Key.HOME.clicked) {
			turnCursorOn();
			if (Key.SHIFT.down > -1) {
				cursorStart = 0;
			} else {
				cursorStart = 0;
				cursorEnd = cursorStart;
			}
		} else if (Key.END.clicked) {
			turnCursorOn();
			if (Key.SHIFT.down > -1) {
				cursorEnd = text.length();
			} else {
				cursorStart = text.length();
				cursorEnd = cursorStart;
			}
		}

		if (Key.CONTROL.down != -1) {
			if (Key.A.clicked) {
				selectAll();
			}

			if (Key.C.clicked) {
				if (cursorStart > cursorEnd) {
					clipboard = text.substring(cursorEnd, cursorStart);
				} else if (cursorEnd > cursorStart) {
					clipboard = text.substring(cursorStart, cursorEnd);
				}
			}

			if (Key.X.clicked) {
				if (cursorStart > cursorEnd) {
					clipboard = text.substring(cursorEnd, cursorStart);
					backspace();
				} else if (cursorEnd > cursorStart) {
					clipboard = text.substring(cursorStart, cursorEnd);
					backspace();
				}
			}

			if (Key.V.clicked) {
				addString(clipboard);
			}
		}

		if (Key.BACKSPACE.clicked) backspace();
		if (Key.DEL.clicked) del();
	}

	public void render(Graphics g) {
		g.setFont(font);
		if (hasFocus) {
			g.setColor(Color.BLUE);
			g.fillRect(x - 2, y - 2, width + 4, height + 4);
		}

		g.setColor(backgroundColour);
		g.fillRect(x, y, width, height);

		full = false;
		while (g.getFontMetrics().stringWidth(text + g.getFontMetrics().getMaxAdvance()) > width) { //make sure the text isn't bigger than the text box
			full = true;
			text = text.substring(0, text.length() - 1);
		}

		while (g.getFontMetrics().stringWidth(promptText + g.getFontMetrics().getMaxAdvance()) > width) { //make sure the prompt text isn't bigger than the text box
			promptText = promptText.substring(0, promptText.length() - 1);
		}

		//LATER add mouse cursor selection

		if (text.length() > 0) drawText(g, text, textColour);
		else drawText(g, promptText, promptTextColour);
	}

	private void drawText(Graphics g, String text, Color color) {
		int xo = 0;

		for (int i = 0; i < text.length(); i++) {
			if (cursorEnd == cursorStart && cursorEnd == i && hasFocus) { //nothing is selected
				g.setColor(color);
				g.drawString(cursor, x + xo - 4, y + 16);
			}
			xo += drawChar(i, x + xo, y + 16, g, text, color);
		}
		if (cursorEnd == text.length() && cursorStart == cursorEnd && hasFocus) { //if the cursor is at the end of the string, it won't get drawn above
			g.setColor(color);
			g.drawString(cursor, x + xo - 4, y + 16);
		}
	}

	/** Draws the character at index in this.text at specified pos
	 *  Returns the width of this character */
	private int drawChar(int index, int x, int y, Graphics g, String text, Color color) {
		String s = String.valueOf(text.charAt(index));
		int w = g.getFontMetrics().stringWidth(s);
		if (hasFocus
				&& ((cursorStart < cursorEnd && index >= cursorStart && index < cursorEnd) || (cursorStart > cursorEnd
						&& index < cursorStart && index >= cursorEnd))) { //this character is selected
			g.setColor(selectedBackgroundColour);
			g.fillRect(x, y - 16, w, height); //highlight it

			g.setColor(selectedTextColour);
		} else g.setColor(color);
		g.drawString(s, x, y);
		return w;
	}

	/** @return the next space character to the right of the current index position in the text OR the length of the text if there are no spaces to the right */
	private int nextSpaceToRight(int index) {
		for (int i = index + 1; i < text.length(); i++) {
			if (text.charAt(i) == ' ') return i + 1;
		}
		return text.length();
	}

	/** @return the next space character to the right of the current index position in the text OR 0 if there are no spaces to the left */
	private int nextSpaceToLeft(int index) {
		for (int i = index - 1; i > 0; i--) {
			if (text.charAt(i) == ' ') return i;
		}
		return 0;
	}

	/** also selects none if all is already selected */
	private void selectAll() {
		if (cursorStart == 0 && cursorEnd == text.length()) {
			cursorStart = cursorEnd;
		} else if (cursorEnd == 0 && cursorStart == text.length()) {
			cursorEnd = cursorStart;
		} else {
			cursorStart = 0;
			cursorEnd = text.length();
		}
	}

	private void moveCursorLeft() {
		if (Key.SHIFT.down == -1) {
			if (cursorStart > cursorEnd) {
				cursorStart = cursorEnd;
			} else if (cursorEnd > cursorStart) {
				cursorEnd = cursorStart;
			} else {
				cursorEnd--;
				cursorStart = cursorEnd;
			}
		} else {
			cursorEnd--;
		}
	}

	private void moveCursorRight() {
		if (Key.SHIFT.down == -1) {
			if (cursorStart > cursorEnd) {
				cursorEnd = cursorStart;
			} else if (cursorEnd > cursorStart) {
				cursorStart = cursorEnd;
			} else {
				cursorEnd++;
				cursorStart = cursorEnd;
			}
		} else {
			cursorEnd++;
		}
	}

	private void cursorClamp() {
		if (text.length() == 0) {
			cursorStart = 0;
			cursorEnd = 0;
			return;
		}

		if (cursorStart < 0) cursorStart = 0;
		if (cursorEnd < 0) cursorEnd = 0;

		if (cursorStart > text.length()) cursorStart = text.length();
		if (cursorEnd > text.length()) cursorEnd = text.length();
	}

	public void clear() {
		text = "";
		cursorClamp();
	}

	/** Deletes one character behind the cursor if nothing is selected.
	 *  If something is selected, it is deleted */
	public void backspace() {
		if (text.length() == 0) return; //nothing to delete
		if ((cursorStart == 0 && cursorEnd == text.length()) || (cursorStart == text.length() && cursorEnd == 0)) { //everything is selected
			clear();
			return;
		}
		if (cursorStart > cursorEnd) {
			text = text.substring(0, cursorEnd) + text.substring(cursorStart);
			cursorStart = cursorEnd;
		} else if (cursorEnd > cursorStart) {
			text = text.substring(0, cursorStart) + text.substring(cursorEnd);
			cursorEnd = cursorStart;
		} else if (cursorStart == cursorEnd) {
			text = text.substring(0, cursorStart - 1) + text.substring(cursorEnd);
			cursorStart--;
		}
		cursorEnd = cursorStart;
		cursorClamp();

		turnCursorOn();
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

		turnCursorOn();
	}

	public void addString(String s) {
		if (cursorStart != cursorEnd) { //some text is selected
			backspace(); //delete it
			cursorEnd = cursorStart; //reset cursor pos
		} else if (full) return;

		text = text.substring(0, cursorStart) + s + text.substring(cursorEnd);
		cursorStart += s.length();
		cursorEnd = cursorStart;
		cursorClamp();

		turnCursorOn();
	}

	public void addCharacter(Key k) {
		if (k.isNumber) {
			if (!acceptsNumbers) return;
			if (Key.SHIFT.down > -1) {
				if (!acceptsSpecialCharacters) return;
			}
		}
		if (k.isLetter && !acceptsLetters) return;
		if (cursorStart != cursorEnd) { //some text is selected
			backspace(); //delete it
			cursorEnd = cursorStart; //reset cursor pos
		} else if (full) return;

		String s;
		if (k.isNumber) {
			s = Key.SHIFT.down > -1 ? k.TEXT : k.text;
		} else if (Key.SHIFT.down > -1) {
			if (Keyboard.capsLock) {
				s = k.text;
			} else {
				s = k.TEXT;
			}
		} else {
			if (Keyboard.capsLock) {
				s = k.TEXT;
			} else {
				s = k.text;
			}
		}
		if (Keyboard.insert) {
			if (cursorEnd + 1 <= text.length()) {
				text = text.substring(0, cursorStart) + s + text.substring(cursorEnd + 1);
			}
		} else {
			text = text.substring(0, cursorStart) + s + text.substring(cursorEnd);
		}
		cursorStart++;
		cursorEnd = cursorStart;
		cursorClamp();

		turnCursorOn();
	}

	public String getText() {
		return text;
	}

	public void setPromptText(String text) {
		this.promptText = text;
	}

	public void setAcceptsLetters(boolean acceptsLetters) {
		this.acceptsLetters = acceptsLetters;
	}

	public void setAcceptsNumbers(boolean acceptsNumbers) {
		this.acceptsNumbers = acceptsNumbers;
	}

	public void setAcceptsSpecialCharacters(boolean acceptsSpecialCharacters) {
		this.acceptsSpecialCharacters = acceptsSpecialCharacters;
	}
}
