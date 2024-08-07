package space.quinoaa.minechef.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.init.MinechefCapabilities;

@AutoRegisterCapability
public class RestaurantCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(Minechef.MODID, "restaurant");

    private final IRestaurantHandler.Impl impl = new IRestaurantHandler.Impl();
    private final LazyOptional<IRestaurantHandler> optional = LazyOptional.of(()->impl);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == MinechefCapabilities.RESTAURANT){
            return this.optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        var pos = impl.getSelectedRestaurant();
        if(pos != null) {
            tag.put("selected", NbtUtils.writeBlockPos(pos));
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.contains("selected", CompoundTag.TAG_COMPOUND)) {
            impl.setSelectedRestaurant(NbtUtils.readBlockPos(nbt.getCompound("selected")));
        }
    }
}
