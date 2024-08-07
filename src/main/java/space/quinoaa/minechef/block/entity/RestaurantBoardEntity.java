package space.quinoaa.minechef.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import space.quinoaa.minechef.init.MinechefBlockEntities;
import space.quinoaa.minechef.init.MinechefNetwork;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.network.BoardUpdateFundPacket;
import space.quinoaa.minechef.network.BoardUpdateMenuPacket;
import space.quinoaa.minechef.network.BoardUpdateWorkersPacket;
import space.quinoaa.minechef.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantBoardEntity extends BlockEntity {
    public final Restaurant restaurant = new Restaurant(null);


    public RestaurantBoardEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        restaurant.position = pPos;
    }

    public RestaurantBoardEntity(BlockPos pPos, BlockState pBlockState) {
        this(MinechefBlockEntities.RESTAURANT_BOARD.get(), pPos, pBlockState);
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        restaurant.level = pLevel;
    }

    public void tick() {
        restaurant.tick();
        setChanged();
        updateTrackedData();
    }

    private final List<ServerPlayer> menuTracking = new ArrayList<>();
    private void updateTrackedData() {
        if(level == null || level.isClientSide) return;
        List<Object> packets = new ArrayList<>();

        if(restaurant.consumeUpdateRequest(Restaurant.QueuedUpdate.FUND)){
            packets.add(new BoardUpdateFundPacket(restaurant.getFund()));
        }
        if(restaurant.consumeUpdateRequest(Restaurant.QueuedUpdate.MENU)){
            packets.add(new BoardUpdateMenuPacket(restaurant.menu));
        }
        if(restaurant.consumeUpdateRequest(Restaurant.QueuedUpdate.WORKERS)){
            packets.add(new BoardUpdateWorkersPacket(restaurant.workers));
        }

        for (int i = 0; i < menuTracking.size(); i++) {
            ServerPlayer plr = menuTracking.get(i);
            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) {
                menuTracking.remove(i);
                i--;
                continue;
            }

            for (Object packet : packets) {
                MinechefNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(()->plr), packet);
            }
        }
    }

    public void startTrackingMenu(ServerPlayer player){
        menuTracking.add(player);
    }


    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("restaurant", restaurant.save());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        restaurant.load(pTag.getCompound("restaurant"));
    }
}
