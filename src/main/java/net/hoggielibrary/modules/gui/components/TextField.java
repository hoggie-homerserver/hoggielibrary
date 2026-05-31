package net.hoggielibrary.modules.gui.components;

import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class TextField extends HoggieWidget {

    protected String text = "";
    protected int cursorPos;
    protected int textColor = 0xFFFFFFFF;
    protected int cursorColor = 0xFFFFFFFF;
    protected int focusedBgColor = 0x80303030;
    protected int unfocusedBgColor = 0x80181818;
    protected String placeholder = "";
    protected int placeholderColor = 0x80808080;
    protected Consumer<String> onChange;
    protected int maxLength = 256;
    protected int cursorTimer;

    public TextField(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public TextField(int x, int y, int width) {
        this(x, y, width, 20);
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (!visible) return;
        int bg = focused ? focusedBgColor : unfocusedBgColor;
        ctx.fill(x, y, x + width, y + height, bg);
        renderBorder(ctx);

        String display = text;
        if (display.isEmpty() && !focused && !placeholder.isEmpty()) {
            ctx.drawText(getTextRenderer(), placeholder, x + 4, y + (height - 9) / 2, placeholderColor, false);
        } else {
            String visible = display;
            int textW = getTextRenderer().getWidth(visible);
            int offset = 0;
            if (textW > width - 8) {
                int excess = textW - (width - 8);
                offset = Math.min(excess, textW);
            }
            ctx.drawText(getTextRenderer(), visible, x + 4 - offset, y + (height - 9) / 2, textColor, false);

            if (focused) {
                cursorTimer++;
                if ((cursorTimer / 10) % 2 == 0) {
                    int cx = x + 4 + getTextRenderer().getWidth(display.substring(0, cursorPos)) - offset;
                    ctx.fill(cx, y + 3, cx + 1, y + height - 3, cursorColor);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isInside(mx, my)) return false;
        focused = true;
        double relX = mx - (x + 4);
        int newPos = 0;
        for (int i = 0; i <= text.length(); i++) {
            int w = getTextRenderer().getWidth(text.substring(0, i));
            if (relX >= w) newPos = i;
        }
        cursorPos = newPos;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        switch (keyCode) {
            case 259 -> { // Backspace
                if (cursorPos > 0) {
                    text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                    cursorPos--;
                    fireChange();
                }
                return true;
            }
            case 261 -> { // Delete
                if (cursorPos < text.length()) {
                    text = text.substring(0, cursorPos) + text.substring(cursorPos + 1);
                    fireChange();
                }
                return true;
            }
            case 263 -> { // Left
                if (cursorPos > 0) cursorPos--;
                return true;
            }
            case 262 -> { // Right
                if (cursorPos < text.length()) cursorPos++;
                return true;
            }
            case 268 -> { // Home
                cursorPos = 0;
                return true;
            }
            case 269 -> { // End
                cursorPos = text.length();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!focused) return false;
        if (chr >= ' ' && chr < 127 && text.length() < maxLength) {
            text = text.substring(0, cursorPos) + chr + text.substring(cursorPos);
            cursorPos++;
            fireChange();
            return true;
        }
        return false;
    }

    private void fireChange() {
        if (onChange != null) onChange.accept(text);
    }

    public String getText() { return text; }
    public void setText(String text) {
        this.text = text;
        cursorPos = text.length();
    }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
    public void setTextColor(int color) { this.textColor = color; }
    public void setOnChange(Consumer<String> onChange) { this.onChange = onChange; }
}
