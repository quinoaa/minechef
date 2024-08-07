package space.quinoaa.minechef.client.screen.board;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.client.screen.BoardScreen;
import space.quinoaa.minechef.init.MinechefNetwork;
import space.quinoaa.minechef.network.BoardRemoveMenuPacket;
import space.quinoaa.minechef.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FoodEntryPanel {
    private static final int ITEM_PER_PAGE = 5;
    private final BoardScreen screen;
    private final Restaurant restaurant;
    private List<FoodEntry> entries = new ArrayList<>();
    private int page = 0;

    private Button next, previous, delete;

    public FoodEntryPanel(BoardScreen screen) {
        this.screen = screen;
        restaurant = screen.getMenu().restaurant;
    }

    public void addWidgets(){
        int centerX = screen.width / 2;
        int centerY = screen.height / 2 - 16;
        int left = centerX - 176 / 2;
        int top = centerY - 198 / 2 + 16;

        previous = Button.builder(Component.literal("<"), (btn)->{
            switchPage(page-1);
        }).pos(61 + left, 101 + top).size(12, 12).build();
        next = Button.builder(Component.literal(">"), (btn)->{
            switchPage(page+1);
        }).pos(157 + left, 101 + top).size(12, 12).build();
        delete = Button.builder(Component.literal("Delete"), (btn)->{
            if(selected != -1){
                MinechefNetwork.CHANNEL.sendToServer(new BoardRemoveMenuPacket(selected + page * ITEM_PER_PAGE));
            }
        }).pos(9 + left, 17 + top).size(46, 12).build();

        screen.addRenderableWidget(previous);
        screen.addRenderableWidget(next);
        screen.addRenderableWidget(delete);

        entries.clear();
        for (int i = 0; i < 5; i++) {
            int index = i;
            var entry = new FoodEntry(centerX - 26, centerY + i * 18 - 75);
            entries.add(entry);
            entry.clickListener = ()->setSelected(index);

            screen.addRenderableWidget(entry);
        }

        updateEntries();
    }

    public void tick(){
        if(restaurant.consumeUpdateRequest(Restaurant.QueuedUpdate.MENU)){
            updateEntries();
        }
    }

    private void switchPage(int i) {
        page = i;
        updateEntries();
    }

    private void updateEntries() {
        var list = restaurant.menu.items;
        int maxPage = Math.max((list.size() - 1) / ITEM_PER_PAGE, 0);
        if(page < 0) page = 0;
        if(page > maxPage) page = maxPage;

        if(previous != null) previous.active = page > 0;
        if(next != null) next.active = page < maxPage;

        for (int i = 0; i < 5; i++) {
            int index = i + page * 5;
            if(index < list.size()){
                entries.get(i).setItem(list.get(index).copyWithCount(1));
            }else{
                entries.get(i).setItem(null);
            }
        }
        setSelected(-1);
    }

    private int selected = -1;
    private void setSelected(int index){
        selected = index;
        if(selected < 0 || selected > entries.size()) selected = -1;

        for (FoodEntry entry : entries) {
            entry.selected = false;
        }
        if(selected != -1) entries.get(selected).selected = true;
        delete.active = selected != -1;
    }



}
