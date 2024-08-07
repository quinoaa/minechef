package space.quinoaa.minechef.restaurant;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BlockManager {
    private final Restaurant restaurant;

    public Set<BlockPos> locations = new HashSet<>();

    public BlockManager(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public void addBlock(BlockPos pos){
        locations.add(pos);
    }

    public @Nullable BlockPos getFirstBlock(Block block, @Nullable BiPredicate<Level, BlockPos> predicate){
        var level = restaurant.level;
        if(level == null) return null;

        for (BlockPos location : locations) {
            if(level.getBlockState(location).is(block) && (predicate == null || predicate.test(level, location))) return location;
        }
        return null;
    }

    public int countBlocks(Block block) {
        var level = restaurant.level;
        if(level == null) return 0;

        int count = 0;
        for (BlockPos location : locations) {
            if(level.getBlockState(location).is(block)) count++;
        }
        return count;
    }





    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        int i = 0;
        for (BlockPos location : locations) {
            tag.put(Integer.toString(i), NbtUtils.writeBlockPos(location));
            i++;
        }

        return tag;
    }

    public void load(CompoundTag blocks) {
        locations.clear();

        for (String key : blocks.getAllKeys()) {
            locations.add(NbtUtils.readBlockPos(blocks.getCompound(key)));
        }
    }
}
