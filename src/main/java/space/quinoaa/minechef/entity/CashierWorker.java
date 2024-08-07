package space.quinoaa.minechef.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.block.entity.CashRegisterEntity;
import space.quinoaa.minechef.block.entity.PlateBlockEntity;
import space.quinoaa.minechef.init.MinechefBlocks;
import space.quinoaa.minechef.init.MinechefEntity;
import space.quinoaa.minechef.restaurant.FoodRequest;
import space.quinoaa.minechef.restaurant.Restaurant;
import space.quinoaa.minechef.restaurant.worker.WorkerData;


public class CashierWorker extends BaseWorkerEntity{
    public CashierWorker(EntityType<? extends CashierWorker> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, null, null);
    }

    public CashierWorker(Level pLevel, @Nullable BlockPos restaurantPos, @Nullable WorkerData data) {
        super(MinechefEntity.CASHIER.get(), pLevel, restaurantPos, data);
    }

    public BlockPos registerPos;
    private FoodRequest request;

    @Override
    public void serverTick(Restaurant restaurant) {
        if(registerPos == null){
            registerPos = restaurant.blocks.getFirstBlock(MinechefBlocks.CASH_REGISTER.get(), this::isFree);
            if(registerPos != null && level().getBlockEntity(registerPos) instanceof CashRegisterEntity entity){
                entity.reserve(this);
            }
        }
        if(registerPos == null) return;

        var register = getCashRegister();
        if(register == null) return;

        if(register.currentRequest == null){
            handleMove(registerPos);
            return;
        }else{
            if(!getItemInHand(InteractionHand.MAIN_HAND).isEmpty()){
                if(handleMove(registerPos)){
                    if(register.fillRequest()) setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }else{
                var pos = restaurant.blocks.getFirstBlock(MinechefBlocks.PLATE.get(), ((level, blockPos) -> {
                    if(level.getBlockEntity(blockPos) instanceof Container entity){
                        return containerHasRequired(register.currentRequest, entity);
                    }
                    return false;
                }));
                if(pos == null){
                    pos = restaurant.blocks.getFirstBlock(MinechefBlocks.FRIDGE.get(), ((level, blockPos) -> {
                        if(level.getBlockEntity(blockPos) instanceof Container entity){
                            return containerHasRequired(register.currentRequest, entity);
                        }
                        return false;
                    }));
                }

                if(pos == null) {
                    if(request == null || !request.isStillValid()){
                        request = new FoodRequest(register.currentRequest.copyWithCount(1));
                        restaurant.foodQueue.add(request);
                    }
                    return;
                }
                if(handleMove(pos)){
                    if(level().getBlockEntity(pos) instanceof Container entity){
                        for (int i = 0; i < entity.getContainerSize(); i++) {
                            var item = entity.getItem(i);
                            if(ItemStack.isSameItem(item, register.currentRequest)){
                                item.shrink(1);
                                setItemInHand(InteractionHand.MAIN_HAND, register.currentRequest.copyWithCount(1));
                                request = null;
                            }
                        }
                    }
                }
            }
        }
    }



    private boolean containerHasRequired(ItemStack req, Container container){
        for (int i = 0; i < container.getContainerSize(); i++) {
            var is = container.getItem(i);

            if(ItemStack.isSameItem(is, req)) return true;
        }
        return false;
    }

    private @Nullable CashRegisterEntity getCashRegister(){
        if(registerPos != null && level().getBlockEntity(registerPos) instanceof CashRegisterEntity entity){
            return entity;
        }
        return null;
    }

    private boolean isFree(Level level, BlockPos blockPos) {
        if(!(level.getBlockEntity(blockPos) instanceof CashRegisterEntity entity)) return false;

        return !entity.isReservedByCashier();
    }
}
