package space.quinoaa.minechef.client.screen.board;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import space.quinoaa.minechef.client.screen.BoardScreen;
import space.quinoaa.minechef.init.MinechefNetwork;
import space.quinoaa.minechef.network.BoardFirePacket;
import space.quinoaa.minechef.network.BoardHirePacket;
import space.quinoaa.minechef.restaurant.Restaurant;
import space.quinoaa.minechef.restaurant.worker.WorkerData;

import java.util.Arrays;

public class JobEntryPanel {
    private final BoardScreen screen;
    private final Restaurant restaurant;

    private JobEntry selected = null;

    public JobEntryPanel(BoardScreen screen) {
        this.screen = screen;
        restaurant = screen.getMenu().restaurant;
    }

    public void addWidgets() {
        selected = null;
        int i = 0;

        Button hire = Button.builder(Component.literal("Hire"), (btn)->{
            if(selected == null) return;
            MinechefNetwork.CHANNEL.sendToServer(new BoardHirePacket(selected.type));
        }).size(48, 12).pos(7 + screen.getGuiLeft(), 18 + screen.getGuiTop()).build();
        screen.addRenderableWidget(hire);
        Button fire = Button.builder(Component.literal("Fire"), (btn)->{
            if(selected == null) return;
            MinechefNetwork.CHANNEL.sendToServer(new BoardFirePacket(selected.type));
        }).size(48, 12).pos(7 + screen.getGuiLeft(), 36 + screen.getGuiTop()).build();
        screen.addRenderableWidget(fire);

        hire.active = false;
        fire.active = false;

        for (WorkerData.Type type : Arrays.asList(WorkerData.Type.CASHIER, WorkerData.Type.COOK)) {
            JobEntry entry = new JobEntry(62 + screen.getGuiLeft(), 8 + i * 18 + screen.getGuiTop(), restaurant, type);
            entry.clickListener = ()->{
                if(selected != null) selected.selected = false;
                selected = entry;
                selected.selected = true;

                hire.active = true;
                fire.active = true;
            };
            screen.addRenderableWidget(entry);
            i++;
        }
    }

    public void tick() {
        if(restaurant.consumeUpdateRequest(Restaurant.QueuedUpdate.WORKERS)){

        }
    }
}
