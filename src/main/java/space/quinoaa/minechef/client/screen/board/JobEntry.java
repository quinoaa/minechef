package space.quinoaa.minechef.client.screen.board;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.restaurant.Restaurant;
import space.quinoaa.minechef.restaurant.worker.WorkerData;

public class JobEntry extends AbstractWidget {
    private static final ResourceLocation MENU_LOCATION = new ResourceLocation(Minechef.MODID, "textures/gui/container/board_hire.png");

    private final Restaurant restaurant;
    public final WorkerData.Type type;

    public boolean selected = false;
    public Runnable clickListener = null;


    public JobEntry(int pX, int pY, Restaurant restaurant, WorkerData.Type type) {
        super(pX, pY, 106, 18, Component.empty());

        this.restaurant = restaurant;
        this.type = type;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if(clickListener != null) clickListener.run();
    }

    @Override
    protected void renderWidget(GuiGraphics g, int pMouseX, int pMouseY, float pPartialTick) {
        int offset = 0;
        if(isMouseOver(pMouseX, pMouseY)) offset = 1;
        if(selected) offset = 2;
        var mc = Minecraft.getInstance();
        g.blit(MENU_LOCATION, getX(), getY(), 0, 198 + offset * 18, width, height);

        g.drawString(mc.font, type.name, getX() + 4, getY() + 1, 0xffffff);
        g.drawString(mc.font, type.price + "$", getX() + 4, getY() + 1 + mc.font.lineHeight, 0xffffff);

        String count = "x" + restaurant.workers.countType(type);
        g.drawString(mc.font, count, getX() + width - 4 - mc.font.width(count), getY() + 5, 0xffffff);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
