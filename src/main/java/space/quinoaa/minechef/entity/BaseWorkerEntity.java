package space.quinoaa.minechef.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.block.entity.RestaurantBoardEntity;
import space.quinoaa.minechef.restaurant.Restaurant;
import space.quinoaa.minechef.restaurant.worker.WorkerData;

public abstract class BaseWorkerEntity extends Mob {
    public static final float SPEED = .5f;

    @Nullable
    public final BlockPos restaurantPos;

    @Nullable
    public final WorkerData data;
    public int skinId = (int) (Math.random() * Integer.MAX_VALUE);


    public BaseWorkerEntity(EntityType<? extends BaseWorkerEntity> pEntityType, Level pLevel, @Nullable BlockPos restaurantPos, @Nullable WorkerData data) {
        super(pEntityType, pLevel);
        this.restaurantPos = restaurantPos;
        this.data = data;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes();
    }

    private int updateCooldown = 0;
    @Override
    public void tick() {
        super.tick();

        if(level().isClientSide) return;
        updateCooldown--;
        if(updateCooldown >0) return;
        updateCooldown = 10;

        var restaurant = getRestaurant();
        if(restaurant == null) {
            discard();
            return;
        }

        serverTick(restaurant);
    }

    public @Nullable Restaurant getRestaurant(){
        if(restaurantPos == null) return null;
        if(level().getBlockEntity(restaurantPos) instanceof RestaurantBoardEntity menu) return menu.restaurant;
        return null;
    }

    public abstract void serverTick(Restaurant restaurant);

    public boolean handleMove(BlockPos pos){
        if(distanceToSqr(pos.getCenter()) < 3 * 3){
            navigation.stop();
            return true;
        }

        navigation.moveTo(navigation.createPath(pos, 2), SPEED);
        return false;
    }
}
