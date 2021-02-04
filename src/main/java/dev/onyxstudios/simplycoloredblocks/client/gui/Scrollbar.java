package dev.onyxstudios.simplycoloredblocks.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Scrollbar {

    public ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final int SCROLLER_HEIGHT = 15;

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean enabled = false;

    private int offset;
    private int maxOffset;

    private boolean wasClicking = false;
    private boolean isScrolling = false;

    public Scrollbar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void draw(MatrixStack stack, ColorWheelScreen gui) {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        gui.blit(stack, x, y + (int) Math.min(height - SCROLLER_HEIGHT, (float) offset / (float) maxOffset * (float) (height - SCROLLER_HEIGHT)), isEnabled() ? 232 : 244, 0, 12, 15);
    }

    public void update(boolean down, int mouseX, int mouseY) {
        if (!isEnabled()) {
            isScrolling = false;
            wasClicking = false;
        } else {
            if (!wasClicking && down && inBounds(x, y, width, height, mouseX, mouseY)) {
                isScrolling = true;
            }

            if (!down) {
                isScrolling = false;
            }

            wasClicking = down;
            if (isScrolling) {
                setOffset((int) Math.floor((float) (mouseY - y) / (float) (height - SCROLLER_HEIGHT) * (float) maxOffset));
            }
        }
    }

    public boolean inBounds(int x, int y, int w, int h, int ox, int oy) {
        return ox >= x && ox <= x + w && oy >= y && oy <= y + h;
    }

    public void wheel(int delta) {
        if (isEnabled()) {
            setOffset(offset + Math.max(Math.min(-delta, 1), -1));
        }
    }

    public void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;

        if (offset > maxOffset) {
            this.offset = Math.max(0, maxOffset);
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        if (offset >= 0 && offset <= maxOffset) {
            this.offset = offset;
        }
    }
}
