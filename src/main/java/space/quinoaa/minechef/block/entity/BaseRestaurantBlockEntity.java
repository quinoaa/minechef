package space.quinoaa.minechef.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import space.quinoaa.minechef.init.MinechefBlockEntities;
import space.quinoaa.minechef.restaurant.Restaurant;

public class BaseRestaurantBlockEntity extends BlockEntity {
    private BlockPos restaurantPos = null;

    public BaseRestaurantBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        if(restaurantPos != null) pTag.put("restaurantpos", NbtUtils.writeBlockPos(restaurantPos));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if(pTag.contains("restaurantpos", CompoundTag.TAG_COMPOUND)){
            restaurantPos = NbtUtils.readBlockPos(pTag.getCompound("restaurantpos"));
        }

    }


    public void setRestaurantPos(BlockPos restaurantPos) {
        this.restaurantPos = restaurantPos;
    }

    public BlockPos getRestaurantPos(){
        return restaurantPos;
    }

    public Restaurant getRestaurant(){
        var pos = getRestaurantPos();
        if(pos == null) return null;

        if(!(level.getBlockEntity(pos) instanceof RestaurantBoardEntity entity)) return null;

        return entity.restaurant;
    }
}
