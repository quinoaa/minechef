package space.quinoaa.minechef.client.screen.board;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.restaurant.Menu;

public class FoodEntry extends AbstractWidget {
    private static final ResourceLocation MENU_LOCATION = new ResourceLocation(Minechef.MODID, "textures/gui/container/board_menu.png");

    public boolean selected = false;

    public ItemStack item = null;
    public Runnable clickListener = null;

    public FoodEntry(int x, int y) {
        super(x, y, 106, 18, Component.empty());
    }


    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if(clickListener != null) clickListener.run();
    }

    public void setItem(ItemStack item){
        this.item = item;

        if(item == null){
            setTooltip(null);
            return;
        }

        MutableComponent tooltip = Component.empty();
        var lines = item.getTooltipLines(null, TooltipFlag.NORMAL);
        for (int i = 0; i < lines.size(); i++) {
            tooltip = tooltip.append(lines.get(i));
            if(i != lines.size() - 1) tooltip.append("\n");
        }

        setTooltip(Tooltip.create(tooltip));
    }


    @Override
    protected void renderWidget(GuiGraphics g, int pMouseX, int pMouseY, float pPartialTick) {
        if(item == null) return;

        int offset = 0;
        if(isMouseOver(pMouseX, pMouseY)) offset = 1;
        if(selected) offset = 2;
        g.blit(MENU_LOCATION, getX(), getY(), 0, 198 + offset * 18, width, height);

        g.renderItem(item, getX() + 1, getY() + 1);

        Minecraft mc = Minecraft.getInstance();
        g.drawString(mc.font, item.getHoverName(), getX() + 20, getY() + 5, 0xFFFFFF);

        String price = Menu.calculatePrice(item) + "$";
        g.drawString(mc.font, price, width - mc.font.width(price) - 2 + getX(), getY() + 5, 0xffffff);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

}
