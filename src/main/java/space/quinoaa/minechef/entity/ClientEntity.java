package space.quinoaa.minechef.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.block.entity.CashRegisterEntity;
import space.quinoaa.minechef.block.entity.RestaurantBoardEntity;
import space.quinoaa.minechef.init.MinechefBlocks;
import space.quinoaa.minechef.init.MinechefEntity;
import space.quinoaa.minechef.restaurant.Restaurant;

public class ClientEntity extends Mob {
    public static final float SPEED = 0.5f;
    private final BlockPos restaurantLocation;

    public State state = State.WALK_TO_REGISTER;
    private BlockPos registerLoc;

    public ClientEntity(EntityType<? extends ClientEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        restaurantLocation = null;
    }

    public ClientEntity(Level pLevel, BlockPos restaurantLocation) {
        super(MinechefEntity.CLIENT.get(), pLevel);
        this.restaurantLocation = restaurantLocation;
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        return super.mobInteract(pPlayer, pHand);
    }

    public @Nullable Restaurant getRestaurant(){
        if(restaurantLocation == null || !(level().getBlockEntity(restaurantLocation) instanceof RestaurantBoardEntity rest)) return null;

        return rest.restaurant;
    }

    public @Nullable CashRegisterEntity getRegister(){
        if(registerLoc == null) return null;
        if(!(level().getBlockEntity(registerLoc) instanceof CashRegisterEntity entity)) return null;
        return entity;
    }

    @Override
    public void tick() {
        if(!level().isClientSide){
            serverTick();
        }

        super.tick();
    }



    int cooldown = 20;
    private void serverTick() {
        cooldown--;
        if(cooldown > 0) return;
        cooldown = 10;

        var restaurant = getRestaurant();
        if(restaurant == null || restaurant.level == null){
            discard();
            return;
        }

        switch (state){
            case WALK_TO_REGISTER -> {
                if(registerLoc == null) {
                    registerLoc = restaurant.blocks.getFirstBlock(MinechefBlocks.CASH_REGISTER.get(), this::isFree);
                    if(registerLoc == null) return;
                }
                if(!(restaurant.level.getBlockEntity(registerLoc) instanceof CashRegisterEntity entity)) {
                    registerLoc = null;
                    return;
                }
                entity.reserve(this);
                navigation.moveTo(navigation.createPath(registerLoc, 2), SPEED);
                if(distanceToSqr(registerLoc.getCenter()) < 3 * 3){
                    navigation.stop();
                    state = State.WAIT;
                }
            }
            case WAIT -> {
                var register = getRegister();
                if(register == null) {
                    state = State.WALK_TO_REGISTER;
                    return;
                }
                if(register.currentRequest == null) register.setCurrentRequest(restaurant.menu.getRandomMenuItem());
            }
            case EXIT -> {
                var teleporter = restaurant.blocks.getFirstBlock(MinechefBlocks.RESTAURANT_PORTAL.get(), null);
                if(teleporter == null) {
                    discard();
                    return;
                }
                navigation.moveTo(navigation.createPath(teleporter, 1), SPEED);
                if(distanceToSqr(teleporter.getCenter()) < 2 * 2){
                    discard();
                }
            }
        }
    }

    private boolean isFree(Level level, BlockPos blockPos) {
        if(!(level.getBlockEntity(blockPos) instanceof CashRegisterEntity entity)) return false;

        return !entity.isReservedByClient();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes();
    }

    public enum State{
        WALK_TO_REGISTER,
        WAIT,
        EXIT
    }
}
