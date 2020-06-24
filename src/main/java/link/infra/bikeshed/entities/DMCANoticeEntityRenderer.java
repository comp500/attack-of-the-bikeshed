package link.infra.bikeshed.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DMCANoticeEntityRenderer extends LivingEntityRenderer<DMCANotice, DMCANoticeModel> {
	public DMCANoticeEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new DMCANoticeModel(), 0.0f);
	}

	@Override
	protected boolean hasLabel(DMCANotice ent) {
		// For some reason, this logic isn't overridden in LivingEntityRenderer, only in MobEntityRenderer
		return super.hasLabel(ent) && (ent.shouldRenderName() || ent.hasCustomName() && ent == dispatcher.targetedEntity);
	}

	@Override
	public Identifier getTexture(DMCANotice entity) {
		return new Identifier("bikeshed", "textures/entity/dmcanotice.png");
	}
}
