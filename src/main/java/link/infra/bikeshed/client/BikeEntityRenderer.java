package link.infra.bikeshed.client;

import link.infra.bikeshed.Bike;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class BikeEntityRenderer extends LivingEntityRenderer<Bike, BikeModel> {
	public BikeEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new BikeModel(), 0.0f);
	}

	@Override
	public Identifier getTexture(Bike entity) {
		return new Identifier("bikeshed", "textures/entity/bike/bike.png");
	}

	@Override
	protected boolean hasLabel(Bike ent) {
		// For some reason, this logic isn't overridden in LivingEntityRenderer, only in MobEntityRenderer
		return super.hasLabel(ent) && (ent.shouldRenderName() || ent.hasCustomName() && ent == dispatcher.targetedEntity);
	}
}
