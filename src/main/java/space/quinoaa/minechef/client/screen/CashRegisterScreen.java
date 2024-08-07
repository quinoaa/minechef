package space.quinoaa.minechef.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.menu.CashRegisterMenu;
import space.quinoaa.minechef.restaurant.Menu;

public class CashRegisterScreen extends AbstractContainerScreen<CashRegisterMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Minechef.MODID, "textures/gui/container/cash_register.png");



    public CashRegisterScreen(CashRegisterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 159;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        setupWidgets();
    }



    public void setupWidgets(){
        clearWidgets();

        ItemButton btn = new ItemButton(133 + leftPos, 21 + topPos, menu.required != null ? menu.required : ItemStack.EMPTY, ()->{});
        if(menu.required != null){
            MutableComponent component = Component.empty();
            var list = menu.required.getTooltipLines(null, TooltipFlag.NORMAL);

            for (int i = 0; i < list.size(); i++) {
                component.append(list.get(i));
                if(i != list.size()-1) component.append("\n");
            }

            btn.setTooltip(Tooltip.create(component));
        }else{
            btn.setTooltip(Tooltip.create(Component.translatable("minechef.screen.cash-register.no-client")));
        }
        addRenderableWidget(btn);
    }

    @Override
    protected void containerTick() {
        if(menu.changed) {
            setupWidgets();
            menu.changed = false;
        }

    }

    @Override
    public void render(GuiGraphics g, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(g, pMouseX, pMouseY, pPartialTick);

        renderTooltip(g, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int pMouseX, int pMouseY) {
        g.drawString(this.font, Component.translatable("minechef.screen.cash-register.title"), this.titleLabelX, this.titleLabelY, 4210752, false);
        inventoryLabelY = imageHeight - 94;
        g.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

        g.drawString(font, Component.literal("Client requested item: "), 20, 26, 4210752, false);
        if(menu.required != null) g.drawString(font, Component.literal("Item value: " + Menu.calculatePrice(menu.required) + "$"), 20, 49, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(gui);
        gui.blit(new ResourceLocation(Minechef.MODID, "textures/gui/container/cash_register.png"), leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }


}
