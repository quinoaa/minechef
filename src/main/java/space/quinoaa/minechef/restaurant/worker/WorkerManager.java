package space.quinoaa.minechef.restaurant.worker;

import net.minecraft.nbt.CompoundTag;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class WorkerManager {
    public final Restaurant restaurant;

    public final List<WorkerData> workers = new ArrayList<>();

    public WorkerManager(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addWorker(WorkerData.Type type){
        workers.add(new WorkerData(type));
        restaurant.queueTrackingUpdate(Restaurant.QueuedUpdate.WORKERS);
    }
    
    public boolean removeWorkerOf(WorkerData.Type type){
        for (int i = 0; i < workers.size(); i++) {
            var worker = workers.get(i);
            if(worker.type != type) continue;

            workers.remove(i).discard();
            restaurant.queueTrackingUpdate(Restaurant.QueuedUpdate.WORKERS);

            return true;
        }
        return false;
    }

    public int countType(WorkerData.Type type){
        return (int) workers.stream().filter(d->d.type==type).count();
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();

        int i = 0;
        for (WorkerData worker : workers) {
            tag.put(Integer.toString(i), worker.save());
            i++;
        }
        return tag;
    }

    public void load(CompoundTag tag){
        for (WorkerData worker : workers) {
            worker.discard();
        }
        workers.clear();
        for (String allKey : tag.getAllKeys()) {
            var data = new WorkerData(WorkerData.Type.CASHIER);
            data.load(tag.getCompound(allKey));
            workers.add(data);
        }
        restaurant.queueTrackingUpdate(Restaurant.QueuedUpdate.WORKERS);
    }


    public void tick() {
        for (WorkerData worker : workers) {
            worker.tick(restaurant);
        }
    }
}
