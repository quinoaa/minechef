package space.quinoaa.minechef.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.client.screen.board.FoodEntry;
import space.quinoaa.minechef.client.screen.board.FoodEntryPanel;
import space.quinoaa.minechef.client.screen.board.JobEntryPanel;
import space.quinoaa.minechef.init.MinechefNetwork;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.network.BoardSelectRestaurantPacket;
import space.quinoaa.minechef.network.WithdrawPacket;
import space.quinoaa.minechef.restaurant.FundItem;

public class BoardScreen extends AbstractContainerScreen<RestaurantBoardMenu> {
    private static final ResourceLocation FUND_LOCATION = new ResourceLocation(Minechef.MODID, "textures/gui/container/board_fund.png");
    private static final ResourceLocation HIRE_LOCATION = new ResourceLocation(Minechef.MODID, "textures/gui/container/board_hire.png");
    private static final ResourceLocation MENU_LOCATION = new ResourceLocation(Minechef.MODID, "textures/gui/container/board_menu.png");

    private FoodEntryPanel foodPanel = new FoodEntryPanel(this);
    private JobEntryPanel jobPanel = new JobEntryPanel(this);

    private Tab tab;

    public BoardScreen(RestaurantBoardMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        imageHeight = 198;
        imageWidth = 176;

        setTab(Tab.OVERVIEW);
        menu.foodSlot.changeListener = ()->{
            if(tab == Tab.MENU) populateWidgets();
        };
    }

    @Override
    protected void init() {
        super.init();

        populateWidgets();
    }

    public void setTab(Tab tab){
        this.tab = tab;
        populateWidgets();
    }

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        return super.addRenderableWidget(pWidget);
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        switch (tab){
            case MENU -> foodPanel.tick();
            case WORKER -> jobPanel.tick();
        }
    }



    public void populateWidgets(){
        clearWidgets();
        int withdrawx = width / 2 - 3;
        int withdrawy = height / 2 - 12;

        int centerX = width / 2;
        int centerY = height / 2;
        int left = centerX - 176 / 2;


        int buttonX = centerX - 160;
        int btnY = centerY - 86;
        addRenderableWidget(new Button.Builder(Component.translatable("minechef.screen.board.tab.overview"), (p)->{
            setTab(Tab.OVERVIEW);
        }).size(60, 20).pos(buttonX, btnY).build());
        addRenderableWidget(new Button.Builder(Component.translatable("minechef.screen.board.tab.workers"), (p)->{
            setTab(Tab.WORKER);
        }).size(60, 20).pos(buttonX, btnY + 25).build());
        addRenderableWidget(new Button.Builder(Component.translatable("minechef.screen.board.tab.menu"), (p)->{
            setTab(Tab.MENU);
        }).size(60, 20).pos(buttonX, btnY + 50).build());

        menu.depositSlot.active = false;

        switch (tab){
            case OVERVIEW -> {
                addRenderableWidget(buildWithdrawButton(withdrawx, withdrawy, FundItem.IRON));
                addRenderableWidget(buildWithdrawButton(withdrawx + 22, withdrawy, FundItem.GOLD));
                addRenderableWidget(buildWithdrawButton(withdrawx + 44, withdrawy, FundItem.DIAMOND));

                addRenderableWidget(Button.builder(Component.translatable("minechef.screen.board.select"), (btn)->{
                    MinechefNetwork.CHANNEL.sendToServer(new BoardSelectRestaurantPacket());
                }).size(60, 16).pos(left + 7, centerY - 30).build());
                menu.depositSlot.active = true;
            }
            case WORKER -> {
                jobPanel.addWidgets();
            }
            case MENU -> {
                foodPanel.addWidgets();
            }
        }
    }





    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private ItemButton buildWithdrawButton(int x, int y, FundItem fund){
        var item = new ItemStack(fund.item);
        var btn = new ItemButton(x, y, item, ()->{giveStack(fund);});

        btn.setTooltip(Tooltip.create(Component.translatable("minechef.screen.board.fund.convert", fund.item.getName(item), fund.value)));
        return btn;
    }

    public void giveStack(FundItem itemType){
        var count = Math.min(menu.restaurant.getFund() / itemType.value, itemType.item.getMaxStackSize(new ItemStack(itemType.item)));
        if(count == 0) return;
        MinechefNetwork.CHANNEL.sendToServer(new WithdrawPacket(itemType, count));
    }

    @Override
    protected void renderBg(GuiGraphics g, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        renderBackground(g);
        g.blit(getCurrentMenu(), x, y, 0, 0, 176, 198);
    }

    private ResourceLocation getCurrentMenu(){
        return switch (tab){
            case OVERVIEW -> FUND_LOCATION;
            case MENU -> MENU_LOCATION;
            case WORKER -> HIRE_LOCATION;
        };
    }

    @Override
    public void render(GuiGraphics g, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(g, pMouseX, pMouseY, pPartialTick);

        renderTooltip(g, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int pMouseX, int pMouseY) {
        switch (tab){
            case OVERVIEW -> {
                g.drawString(font, "Fund", 86, 76, 4210752, false);
                g.drawString(font, "Overview", 6, 6, 4210752, false);
                g.drawString(font, "Fund: " + menu.restaurant.getFund() + "$", 10, 90, 4210752, false);
            }
            case WORKER -> {
                g.drawString(font, "Workers", 6, 6, 4210752, false);
                g.drawString(font, "Fund: " + menu.restaurant.getFund() + "$", 10, 103, 4210752, false);
            }
            case MENU -> {
                g.drawString(font, "Menu", 6, 6, 4210752, false);
                g.drawString(font, "Add dish", 6, 74, 4210752, false);
            }
        }
    }


    public enum Tab{
        OVERVIEW,
        WORKER,
        MENU
    }

}
