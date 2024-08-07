package space.quinoaa.minechef.capability;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public interface IRestaurantHandler {

    @Nullable BlockPos getSelectedRestaurant();

    void setSelectedRestaurant(BlockPos pos);

    default void copyFrom(IRestaurantHandler originalCap) {
        setSelectedRestaurant(originalCap.getSelectedRestaurant());
    }


    class Impl implements IRestaurantHandler{
        private BlockPos pos;

        @Override
        public @Nullable BlockPos getSelectedRestaurant() {
            return pos;
        }

        @Override
        public void setSelectedRestaurant(BlockPos pos) {
            this.pos = pos;
        }
    }
}
