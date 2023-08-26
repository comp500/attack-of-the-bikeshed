package link.infra.bikeshed.entities;

import link.infra.bikeshed.BikeshedMain;
import link.infra.bikeshed.entities.Bike;
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
import net.minecraft.util.math.Vec3d;

public class BikeModel extends EntityModel<Bike> {
	private static final String PEDALS = "pedals";
	private static final String FRAME = "frame";
	private static final String SEAT = "seat";
	private static final String SEAT_ATTACH = "seat_attach";
	private static final String BACK_ATTACH = "back_attach";
	private static final String FRONT_ATTACH = "front_attach";
	private static final String PEDALS_ATTACH_BACK = "pedals_attach_back";
	private static final String PEDALS_ATTACH_FRONT = "pedals_attach_front";
	private static final String HANDLES = "handles";
	private static final String RIGHT = "right";
	private static final String LEFT = "left";
	private static final String HANDLE_ATTACH = "handle_attach";
	private static final String WHEEL_F = "wheel_f";
	private static final String ROTATE = "rotate";
	private static final String ROTATE_2 = "rotate_2";
	private static final String ROTATE_3 = "rotate_3";
	private static final String SPOKE = "spoke";
	private static final String SPOKE_2 = "spoke_2";
	private static final String SPOKE_3 = "spoke_3";
	private static final String SPOKE_4 = "spoke_4";
	private static final String WHEEL_B = "wheel_b";
	private static final String ROTATE_4 = "rotate_4";
	private static final String ROTATE_5 = "rotate_5";
	private static final String ROTATE_6 = "rotate_6";
	private static final String SPOKE_5 = "spoke_5";
	private static final String SPOKE_6 = "spoke_6";
	private static final String SPOKE_7 = "spoke_7";
	private static final String SPOKE_8 = "spoke_8";

	public static final EntityModelLayer LAYER = new EntityModelLayer(BikeshedMain.BIKE_ID, PEDALS);

	private final ModelPart pedals;
	private final ModelPart frame;
	private final ModelPart handles;
	private final ModelPart wheelF;
	private final ModelPart wheelB;

	public BikeModel(ModelPart root) {
		pedals = root.getChild(PEDALS);
		frame = root.getChild(FRAME);
		handles = root.getChild(HANDLES);
		wheelF = root.getChild(WHEEL_F);
		wheelB = root.getChild(WHEEL_B);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData model = new ModelData();
		ModelPartData root = model.getRoot();

		root.addChild(
			PEDALS,
			ModelPartBuilder.create()
				.uv(12, 27)
				.cuboid(0.5F, -0.5F, 0.5F, 2.0F, 1.0F, 2.0F, false)
				.cuboid(-2.5F, -0.5F, -2.5F, 2.0F, 1.0F, 2.0F, false)
				// Tweaked width to fix Z-fighting
				.uv(12, 23)
				.cuboid(-0.505F, -1.0F, -1.0F, 1.01F, 2.0F, 2.0F, false),
			ModelTransform.pivot(0.0F, 19.5F, 2.0F)
		);

		ModelPartData frame = root.addChild(
			FRAME,
			ModelPartBuilder.create()
				.uv(0, 12)
				.cuboid(-1.0F, -5.981F, -5.4358F, 1.0F, 1.0F, 10.0F, false),
			ModelTransform.of(0.5F, 19.0F, 0.0F, -0.0873F, 0.0F, 0.0F)
		);

		ModelPartData seat = frame.addChild(
			SEAT,
			ModelPartBuilder.create()
				.uv(16, 27)
				.cuboid(-1.125F, -11.0863F, -0.2637F, 2.0F, 1.0F, 4.0F, false),
			ModelTransform.of(-0.625F, 2.0757F, 2.4924F, -0.0873F, 0.0F, 0.0F)
		);

		seat.addChild(
			SEAT_ATTACH,
			ModelPartBuilder.create()
				.uv(0, 17)
				.cuboid(-0.5F, -7.322F, -1.8809F, 1.0F, 3.0F, 1.0F, false),
			ModelTransform.of(0.125F, -3.25F, 2.25F, -0.0873F, 0.0F, 0.0F)
		);

		frame.addChild(
			BACK_ATTACH,
			ModelPartBuilder.create()
				.uv(8, 23)
				.cuboid(-0.5F, -3.1464F, 3.5F, 1.0F, 8.0F, 1.0F, false),
			ModelTransform.of(-1.5F, -0.1139F, 3.5605F, 0.8727F, 0.0F, 0.0F)
		);

		frame.addChild(
			FRONT_ATTACH,
			ModelPartBuilder.create()
				.uv(0, 23)
				.cuboid(0.0F, -5.2156F, -8.9658F, 1.0F, 8.0F, 1.0F, false)
				.cuboid(-2.0F, -5.2156F, -8.9658F, 1.0F, 8.0F, 1.0F, false)
				.cuboid(-1.0F, 1.7763F, -8.9116F, 1.0F, 1.0F, 1.0F, false)
				.uv(4, 23),
			ModelTransform.of(0.0F, 1.7433F, 0.1525F, -0.3491F, 0.0F, 0.0F)
		);

		frame.addChild(
			PEDALS_ATTACH_BACK,
			ModelPartBuilder.create()
				.uv(4, 26)
				.cuboid(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, false),
			ModelTransform.of(-0.5F, -2.7359F, 3.1524F, -0.2618F, 0.0F, 0.0F)
		);

		frame.addChild(
			PEDALS_ATTACH_FRONT,
			ModelPartBuilder.create()
				.uv(28, 22)
				.cuboid(-0.5F, -6.0F, -0.5F, 1.0F, 9.0F, 1.0F, false),
			ModelTransform.of(-0.5F, -1.8731F, -0.9071F, 0.9599F, 0.0F, 0.0F)
		);

		ModelPartData handles = root.addChild(
			HANDLES,
			ModelPartBuilder.create(),
			ModelTransform.pivot(0.0F, 13.5F, -6.5F)
		);

		handles.addChild(
			RIGHT,
			ModelPartBuilder.create()
				.uv(12, 15)
				.cuboid(-4.0126F, -5.0F, 1.0093F, 3.0F, 1.0F, 1.0F, false),
			ModelTransform.rotation(0.0F, 0.2618F, 0.0F)
		);

		handles.addChild(
			LEFT,
			ModelPartBuilder.create()
				.uv(12, 13)
				.cuboid(1.005F, -5.0F, 1.0093F, 3.0F, 1.0F, 1.0F, false),
			ModelTransform.rotation(0.0F, -0.2618F, 0.0F)
		);

		handles.addChild(
			HANDLE_ATTACH,
			ModelPartBuilder.create()
				.uv(12, 17)
				.cuboid(-0.5F, -5.7352F, -0.7902F, 1.0F, 4.0F, 1.0F, false),
			ModelTransform.of(0.0F, 1.0F, 1.0F, -0.1745F, 0.0F, 0.0F)
		);

		ModelPartData wheelF = root.addChild(
			WHEEL_F,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -1.25F, -4.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.0F, 3.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.pivot(0.0F, 19.5F, -8.5F)
		);

		wheelF.addChild(
			ROTATE,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.of(0.0F, -0.0625F, 0.0F, 0.3927F, 0.0F, 0.0F)
		);

		wheelF.addChild(
			ROTATE_2,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.of(0.0F, -0.0625F, 0.0F, 0.7854F, 0.0F, 0.0F)
		);

		wheelF.addChild(
			ROTATE_3,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.of(0.0F, -0.0625F, 0.0F, 1.1781F, 0.0F, 0.0F)
		);

		wheelF.addChild(
			SPOKE,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(-0.7854F, 0.0F, 0.0F)
		);

		wheelF.addChild(
			SPOKE_2,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(0.7854F, 0.0F, 0.0F)
		);

		wheelF.addChild(
			SPOKE_3,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(1.5708F, 0.0F, 0.0F)
		);

		wheelF.addChild(
			SPOKE_4,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(3.1416F, 0.0F, 0.0F)
		);

		ModelPartData wheelB = root.addChild(
			WHEEL_B,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -1.25F, -4.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.0F, 3.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.pivot(0.0F, 19.5F, 9.5F)
		);

		wheelB.addChild(
			ROTATE_4,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.of(0.0F, -0.0625F, 0.0F, 0.3927F, 0.0F, 0.0F)
		);

		wheelB.addChild(
			ROTATE_5,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.of(0.0F, -0.0625F, 0.0F, 0.7854F, 0.0F, 0.0F)
		);

		wheelB.addChild(
			ROTATE_6,
			ModelPartBuilder.create()
				.uv(2, 19)
				.cuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(1, 13)
				.cuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, false)
				.uv(0, 12)
				.cuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, false)
				.cuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, false),
			ModelTransform.of(0.0F, -0.0625F, 0.0F, 1.1781F, 0.0F, 0.0F)
		);

		wheelB.addChild(
			SPOKE_5,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(-0.7854F, 0.0F, 0.0F)
		);

		wheelB.addChild(
			SPOKE_6,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(0.7854F, 0.0F, 0.0F)
		);

		wheelB.addChild(
			SPOKE_7,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(1.5708F, 0.0F, 0.0F)
		);

		wheelB.addChild(
			SPOKE_8,
			ModelPartBuilder.create()
				.uv(23, 14)
				.cuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, false),
			ModelTransform.rotation(3.1416F, 0.0F, 0.0F)
		);

		return TexturedModelData.of(model, 32, 32);
	}

	@Override
	public void setAngles(Bike entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		double speed = entity.getVelocity().dotProduct(new Vec3d(0, 0, 1).rotateY((float) Math.toRadians(-entity.bodyYaw)));
		double radius = 0.25;
		double circumference = 2 * Math.PI * radius;
		entity.wheelPosition += speed / circumference;
		entity.wheelPosition = entity.wheelPosition % 360f;
		wheelB.pitch = entity.wheelPosition;
		wheelF.pitch = entity.wheelPosition;
		pedals.pitch = (entity.wheelPosition + 180f) % 360f;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();
		matrices.translate(0f, -0.45f, 0f);
		matrices.scale(1.3f, 1.3f, 1.3f);
		wheelB.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		wheelF.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		frame.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		handles.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		pedals.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		matrices.pop();
	}
}

