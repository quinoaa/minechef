package space.quinoaa.minechef.client.renderer;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import space.quinoaa.minechef.entity.BaseWorkerEntity;

public class WorkerEntityRenderer<T extends BaseWorkerEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {

    public WorkerEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PlayerModel<>(pContext.bakeLayer(ModelLayers.PLAYER), false), .5f);

        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(BaseWorkerEntity pEntity) {
        return ClientEntityRenderer.textures.get(pEntity.skinId % ClientEntityRenderer.textures.size());
    }
}
