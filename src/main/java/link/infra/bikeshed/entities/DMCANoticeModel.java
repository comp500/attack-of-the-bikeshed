package link.infra.bikeshed.entities;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class DMCANoticeModel extends EntityModel<DMCANotice> {
	private final ModelPart rot;
	private final ModelPart main;

	public DMCANoticeModel() {
		textureHeight = 16;
		textureWidth = 16;

		main = new ModelPart(this);
		main.setPivot(0.0F, 24.0F, 0.0F);
		main.addCuboid(-6.0F, -18.0F, 0.0F, 12.0F, 12.0F, 1.0F, 0.0F, false);

		rot = new ModelPart(this);
		rot.setPivot(0.0F, 24.0F, 2.25F);
		rot.yaw = 3.1416F;
		rot.addCuboid(-6.0F, -18.0F, 1.0F, 12.0F, 12.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setAngles(DMCANotice entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.scale(2F, 2F, 2F);
		matrices.translate(0F, -0.5F, 0F);
		main.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rot.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
