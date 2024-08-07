package space.quinoaa.minechef.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import space.quinoaa.minechef.entity.ClientEntity;

import java.util.ArrayList;
import java.util.List;

public class ClientEntityRenderer extends LivingEntityRenderer<ClientEntity, PlayerModel<ClientEntity>> {
    public static final List<ResourceLocation> textures = new ArrayList<>();

    public ClientEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PlayerModel<>(pContext.bakeLayer(ModelLayers.PLAYER), false), .5f);

        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
    }

    @Override
    protected void renderNameTag(ClientEntity pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        //super.renderNameTag(pEntity, pDisplayName, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ClientEntity pEntity) {
        return textures.get(pEntity.getId() % textures.size());
    }

    static {
        String[] names = new String[]{"alex", "ari", "efe", "kai", "makena", "noor", "steve", "sunny", "zuri"};

        for (String name : names) {
            textures.add(new ResourceLocation("textures/entity/player/wide/" + name + ".png"));
        }
    }
}
