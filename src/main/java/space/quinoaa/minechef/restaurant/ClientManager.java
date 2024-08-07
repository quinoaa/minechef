package space.quinoaa.minechef.restaurant;

import space.quinoaa.minechef.entity.ClientEntity;
import space.quinoaa.minechef.init.MinechefBlocks;

import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    private static final int MAX_CLIENT_COUNT = 10;
    private final Restaurant restaurant;

    private List<ClientEntity> clients = new ArrayList<>();
    private int cooldown = 20;

    public ClientManager(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    private void cleanupClientList(){
        clients.removeIf(ClientEntity::isRemoved);
    }

    public boolean shouldAcceptClient(){
        cleanupClientList();
        return clients.size() < MAX_CLIENT_COUNT
                && clients.size() < restaurant.blocks.countBlocks(MinechefBlocks.CASH_REGISTER.get())
                && !restaurant.menu.items.isEmpty();
    }

    public void tick() {
        cooldown--;
        if(cooldown > 0) return;
        cooldown = (int) (10 + Math.random() * 20 * 2) / Math.max(restaurant.blocks.countBlocks(MinechefBlocks.CASH_REGISTER.get()), 1);

        if(!shouldAcceptClient()) return;
        if(restaurant.position == null) return;

        var portal = restaurant.blocks.getFirstBlock(MinechefBlocks.RESTAURANT_PORTAL.get(), null);
        if(portal == null) return;

        ClientEntity client = new ClientEntity(restaurant.level, restaurant.position);
        client.setPos(portal.above().getCenter());
        restaurant.level.addFreshEntity(client);
        clients.add(client);
    }
}
