package space.quinoaa.minechef.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemButton extends AbstractWidget {
    private final ItemStack item;
    private final Runnable onClick;

    public ItemButton(int pX, int pY, ItemStack item, Runnable onClick) {
        super(pX, pY, 18, 18, Component.empty());

        this.item = item.copy();
        this.onClick = onClick;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        onClick.run();
    }

    @Override
    protected void renderWidget(GuiGraphics g, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        g.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());

        g.renderItem(item, getX() + 1, getY() + 1);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput el) {

    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }
}
