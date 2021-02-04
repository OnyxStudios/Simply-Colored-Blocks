package dev.onyxstudios.simplycoloredblocks.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.onyxstudios.simplycoloredblocks.SimplyColoredBlocks;
import dev.onyxstudios.simplycoloredblocks.client.container.ColorWheelContainer;
import dev.onyxstudios.simplycoloredblocks.items.ColoredBlock;
import dev.onyxstudios.simplycoloredblocks.network.ColorBlockMessage;
import dev.onyxstudios.simplycoloredblocks.network.ModPackets;
import dev.onyxstudios.simplycoloredblocks.utils.ColorUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ColorWheelScreen extends ContainerScreen<ColorWheelContainer> {

    public static ResourceLocation GUI = new ResourceLocation(SimplyColoredBlocks.MODID, "textures/gui/color_wheel_gui.png");
    private List<ColoredBlock> validBlocks = new ArrayList<>();
    private Scrollbar scrollbar;
    private boolean isMouseDown = false;

    public ColorWheelScreen(ColorWheelContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
        this.scrollbar = new Scrollbar(guiLeft + 156, guiTop + 8, 12, 70);
        updateScrollbar();
        updateColor();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mx, int my) {
        this.font.func_243248_b(matrixStack, this.title, this.titleX, -this.titleY * 2, 0xFFFFFF);

        int x = 44;
        int y = 8;
        int slot = scrollbar != null ? (scrollbar.getOffset() * 6) : 0;

        for (int i = 0; i < 6 * 4; i++) {
            if(slot < validBlocks.size())
                minecraft.getItemRenderer().renderItemIntoGUI(validBlocks.get(slot).asItem().getDefaultInstance(), x, y);

            if(isPointInRegion(x, y, 16, 16, mx, my)) {
                fill(matrixStack, x, y, x + 16, y + 16, 0x80FFFFFF);
            }

            slot++;
            x += 18;

            if((i + 1) % 6 == 0) {
                x = 44;
                y += 18;
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mx, int my) {
        getMinecraft().getTextureManager().bindTexture(GUI);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        if(scrollbar != null) {
            scrollbar.update(isMouseDown, mx, my);
            scrollbar.draw(matrixStack, this);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if(button == 0) {
            isMouseDown = true;
        }

        int x = 44;
        int y = 8;
        int slot = scrollbar != null ? (scrollbar.getOffset() * 6) : 0;

        for (int i = 0; i < 6 * 4; i++) {
            if(slot < validBlocks.size() && isPointInRegion(x, y, 16, 16, mouseX, mouseY)) {
                int toCraft = (int) Math.floor(container.inventory.getStackInSlot(0).getCount() / 8) * 8;
                int dyeToTake = toCraft / 8;

                if(container.inventory.getStackInSlot(1).getCount() < dyeToTake) {
                    dyeToTake = container.inventory.getStackInSlot(1).getCount();
                    toCraft = dyeToTake * 8;
                }

                ItemStack block = new ItemStack(validBlocks.get(slot).asItem(), toCraft);
                container.inventory.extractItem(0, toCraft, false);
                container.inventory.extractItem(1, dyeToTake, false);
                minecraft.player.inventory.setItemStack(block);
                ModPackets.INSTANCE.sendToServer(new ColorBlockMessage(block, toCraft, dyeToTake));
            }

            slot++;
            x += 18;

            if((i + 1) % 6 == 0) {
                x = 44;
                y += 18;
            }
        }

        return clicked;
    }

    public int getSlotHovered() {
        int slot = scrollbar != null ? (scrollbar.getOffset() * 6) : 0;

        for (int i = 0; i < 6 * 4; i++) {
            if(slot < validBlocks.size())
                return slot;

            slot++;
        }

        return -1;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean released = super.mouseReleased(mouseX, mouseY, button);
        if(button == 0) {
            isMouseDown = false;
        }

        return released;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        boolean scrolled = super.mouseScrolled(mouseX, mouseY, delta);
        updateScrollbar();
        if(scrollbar != null && delta != 0) {
            scrollbar.wheel((int) delta);
            return true;
        }

        return scrolled;
    }

    public void updateColor() {
        if(container.inventory.getStackInSlot(0).isEmpty() || container.inventory.getStackInSlot(1).isEmpty()) {
            validBlocks.clear();
            scrollbar.setOffset(0);
            updateScrollbar();
            return;
        }

        DyeItem dye = (DyeItem) container.inventory.getStackInSlot(1).getItem();
        validBlocks.clear();
        List<ColoredBlock> colors = ColorUtils.findClosestColors(dye.getDyeColor().getColorValue(), 68000);
        validBlocks.addAll(colors);

        scrollbar.setOffset(0);
        updateScrollbar();
    }

    public int getRows() {
        return Math.max(0, (int) Math.ceil((float) validBlocks.size() / 6f));
    }

    @Override
    public boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
        return super.isPointInRegion(x, y, width, height, mouseX, mouseY);
    }

    public void updateScrollbar() {
        if (scrollbar != null) {
            scrollbar.setEnabled(getRows() > 4);
            scrollbar.setMaxOffset(getRows() - 4);
        }
    }
}
