package link.infra.bikeshed.entities;

import link.infra.bikeshed.BikeshedMain;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class DMCANoticeModel extends EntityModel<DMCANotice> {
	private static final String ROT = "rot";
	private static final String MAIN = "main";

	public static final EntityModelLayer LAYER = new EntityModelLayer(BikeshedMain.DMCA_NOTICE_ID, MAIN);

	private final ModelPart rot;
	private final ModelPart main;

	public DMCANoticeModel(ModelPart root) {
		this.main = root.getChild(MAIN);
		this.rot = root.getChild(ROT);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData model = new ModelData();
		ModelPartData root = model.getRoot();

		root.addChild(
			MAIN,
			ModelPartBuilder.create().cuboid(-6.0F, -18.0F, 0.0F, 12.0F, 12.0F, 1.0F, false),
			ModelTransform.pivot(0.0F, 24.0F, 0.0F)
		);

		root.addChild(
			ROT,
			ModelPartBuilder.create().cuboid(-6.0F, -18.0F, 1.0F, 12.0F, 12.0F, 1.0F, false),
			ModelTransform.of(0.0F, 24.0F, 2.25F, 0.0F, 3.1416F, 0.0F)
		);

		return TexturedModelData.of(model, 16, 16);
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
