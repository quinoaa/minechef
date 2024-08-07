package space.quinoaa.minechef.restaurant;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.restaurant.worker.WorkerManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Restaurant {
    private int fund;
    public final Menu menu = new Menu(this);
    public final BlockManager blocks = new BlockManager(this);
    public final ClientManager clients = new ClientManager(this);
    public final WorkerManager workers = new WorkerManager(this);
    @Nullable
    public Level level;
    @Nullable
    public BlockPos position = null;
    public Set<QueuedUpdate> queuedTrackingUpdate = new HashSet<>();

    public List<FoodRequest> foodQueue = new ArrayList<>();

    public Restaurant(Level level) {
        this.level = level;
    }

    public boolean consumeUpdateRequest(QueuedUpdate update){
        if(queuedTrackingUpdate.contains(update)){
            queuedTrackingUpdate.remove(update);
            return true;
        }
        return false;
    }

    public int getFund(){
        return fund;
    }

    public boolean hasFund(int amount){
        return fund >= amount;
    }

    public void setFund(int amount){
        fund = amount;
        queueTrackingUpdate(QueuedUpdate.FUND);
    }

    public int addFund(int amount){
        fund += amount;
        queueTrackingUpdate(QueuedUpdate.FUND);

        return fund;
    }

    public void queueTrackingUpdate(QueuedUpdate update){
        queuedTrackingUpdate.add(update);
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("fund", fund);
        tag.put("menu", menu.save());
        tag.put("blocks", blocks.save());
        tag.put("workers", workers.save());

        return tag;
    }

    public Restaurant load(CompoundTag tag){
        if(tag.contains("fund", CompoundTag.TAG_INT)) fund = tag.getInt("fund");
        if(tag.contains("menu", CompoundTag.TAG_COMPOUND)) menu.load(tag.getCompound("menu"));
        if(tag.contains("blocks", CompoundTag.TAG_COMPOUND)) blocks.load(tag.getCompound("blocks"));
        if(tag.contains("workers", Tag.TAG_COMPOUND)) workers.load(tag.getCompound("workers"));

        return this;
    }

    public void tick() {
        clients.tick();
        workers.tick();
    }

    public enum QueuedUpdate{
        FUND,
        MENU,
        WORKERS
    }
}
