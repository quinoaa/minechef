package space.quinoaa.minechef.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.entity.CashierWorker;
import space.quinoaa.minechef.entity.ClientEntity;
import space.quinoaa.minechef.init.MinechefBlockEntities;
import space.quinoaa.minechef.init.MinechefNetwork;
import space.quinoaa.minechef.menu.CashRegisterMenu;
import space.quinoaa.minechef.network.CashRegisterSetRequestPacket;
import space.quinoaa.minechef.restaurant.Menu;

import java.util.HashSet;
import java.util.Set;

public class CashRegisterEntity extends BaseRestaurantBlockEntity {
    public ItemStack currentRequest;
    private ClientEntity clientUser;


    public CashRegisterEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinechefBlockEntities.CASH_REGISTER.get(), pPos, pBlockState);
    }

    public boolean isReservedByClient(){
        if(clientUser == null) return false;
        if(clientUser.isRemoved()) return false;
        if(clientUser.state == ClientEntity.State.EXIT) return false;

        return true;
    }

    public void reserve(ClientEntity entity){
        clientUser = entity;
    }



    private Set<ServerPlayer> tracking = new HashSet<>();
    public void setCurrentRequest(@Nullable ItemStack item){
        currentRequest = item;
        if(getRestaurant().foodQueue.size() < 1000 && item != null) getRestaurant().foodQueue.add(item.copy());

        tracking.removeIf(plr->!(plr.containerMenu instanceof CashRegisterMenu));

        var packet = new CashRegisterSetRequestPacket(item != null ? item.copy() : null);
        for (ServerPlayer player : tracking) {
            MinechefNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->player), packet);
        }
    }


    public ClientEntity getReservingEntity(){
        return isReservedByClient() ? clientUser : null;
    }

    public void startTrackingMenu(ServerPlayer player) {
        tracking.add(player);
    }

    public boolean fillRequest() {
        if(currentRequest == null) return false;

        var restaurant = getRestaurant();
        if(restaurant == null) return false;

        restaurant.addFund(Menu.calculatePrice(currentRequest));
        if(clientUser != null) {
            clientUser.setItemInHand(InteractionHand.MAIN_HAND, currentRequest.copy());
            clientUser.state = ClientEntity.State.EXIT;
        }
        setCurrentRequest(null);
        clientUser = null;

        return true;
    }

    public CashierWorker cashier;

    public boolean isReservedByCashier() {
        return cashier != null
                && !cashier.isRemoved()
                && cashier.registerPos.equals(getBlockPos());
    }

    public void reserve(CashierWorker cashier){
        this.cashier = cashier;
    }
}
