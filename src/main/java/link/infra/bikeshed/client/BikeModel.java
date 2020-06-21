package link.infra.bikeshed.client;

import link.infra.bikeshed.Bike;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BikeModel extends EntityModel<Bike> {
	private final ModelPart pedals;
	private final ModelPart frame;
	private final ModelPart seat;
	private final ModelPart seatAttach;
	private final ModelPart backAttach;
	private final ModelPart frontAttach;
	private final ModelPart pedalsAttachBack;
	private final ModelPart pedalsAttachFront;
	private final ModelPart handles;
	private final ModelPart right;
	private final ModelPart left;
	private final ModelPart handleAttach;
	private final ModelPart wheelF;
	private final ModelPart rotate;
	private final ModelPart rotate2;
	private final ModelPart rotate3;
	private final ModelPart spoke;
	private final ModelPart spoke2;
	private final ModelPart spoke3;
	private final ModelPart spoke4;
	private final ModelPart wheelB;
	private final ModelPart rotate4;
	private final ModelPart rotate5;
	private final ModelPart rotate6;
	private final ModelPart spoke5;
	private final ModelPart spoke6;
	private final ModelPart spoke7;
	private final ModelPart spoke8;

	public BikeModel() {
		textureWidth = 32;
		textureHeight = 32;
		
		pedals = new ModelPart(this);
		pedals.setPivot(0.0F, 19.5F, 2.0F);
		pedals.setTextureOffset(12, 27).addCuboid(0.5F, -0.5F, 0.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
		pedals.setTextureOffset(12, 27).addCuboid(-2.5F, -0.5F, -2.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
		// Tweaked width to fix Z-fighting
		pedals.setTextureOffset(12, 23).addCuboid(-0.505F, -1.0F, -1.0F, 1.01F, 2.0F, 2.0F, 0.0F, false);

		frame = new ModelPart(this);
		frame.setPivot(0.5F, 19.0F, 0.0F);
		setRotationAngle(frame, -0.0873F, 0.0F, 0.0F);
		frame.setTextureOffset(0, 12).addCuboid(-1.0F, -5.981F, -5.4358F, 1.0F, 1.0F, 10.0F, 0.0F, false);

		seat = new ModelPart(this);
		seat.setPivot(-0.625F, 2.0757F, 2.4924F);
		frame.addChild(seat);
		setRotationAngle(seat, -0.0873F, 0.0F, 0.0F);
		seat.setTextureOffset(16, 27).addCuboid(-1.125F, -11.0863F, -0.2637F, 2.0F, 1.0F, 4.0F, 0.0F, false);

		seatAttach = new ModelPart(this);
		seatAttach.setPivot(0.125F, -3.25F, 2.25F);
		seat.addChild(seatAttach);
		setRotationAngle(seatAttach, -0.0873F, 0.0F, 0.0F);
		seatAttach.setTextureOffset(0, 17).addCuboid(-0.5F, -7.322F, -1.8809F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		backAttach = new ModelPart(this);
		backAttach.setPivot(-1.5F, -0.1139F, 3.5605F);
		frame.addChild(backAttach);
		setRotationAngle(backAttach, 0.8727F, 0.0F, 0.0F);
		backAttach.setTextureOffset(8, 23).addCuboid(-0.5F, -3.1464F, 3.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		frontAttach = new ModelPart(this);
		frontAttach.setPivot(0.0F, 1.7433F, 0.1525F);
		frame.addChild(frontAttach);
		setRotationAngle(frontAttach, -0.3491F, 0.0F, 0.0F);
		frontAttach.setTextureOffset(0, 23).addCuboid(0.0F, -5.2156F, -8.9658F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		frontAttach.setTextureOffset(0, 23).addCuboid(-2.0F, -5.2156F, -8.9658F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		frontAttach.setTextureOffset(4, 23).addCuboid(-1.0F, 1.7763F, -8.9116F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		pedalsAttachBack = new ModelPart(this);
		pedalsAttachBack.setPivot(-0.5F, -2.7359F, 3.1524F);
		frame.addChild(pedalsAttachBack);
		setRotationAngle(pedalsAttachBack, -0.2618F, 0.0F, 0.0F);
		pedalsAttachBack.setTextureOffset(4, 26).addCuboid(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		pedalsAttachFront = new ModelPart(this);
		pedalsAttachFront.setPivot(-0.5F, -1.8731F, -0.9071F);
		frame.addChild(pedalsAttachFront);
		setRotationAngle(pedalsAttachFront, 0.9599F, 0.0F, 0.0F);
		pedalsAttachFront.setTextureOffset(28, 22).addCuboid(-0.5F, -6.0F, -0.5F, 1.0F, 9.0F, 1.0F, 0.0F, false);

		handles = new ModelPart(this);
		handles.setPivot(0.0F, 13.5F, -6.5F);


		right = new ModelPart(this);
		right.setPivot(0.0F, 0.0F, 0.0F);
		handles.addChild(right);
		setRotationAngle(right, 0.0F, 0.2618F, 0.0F);
		right.setTextureOffset(12, 15).addCuboid(-4.0126F, -5.0F, 1.0093F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		left = new ModelPart(this);
		left.setPivot(0.0F, 0.0F, 0.0F);
		handles.addChild(left);
		setRotationAngle(left, 0.0F, -0.2618F, 0.0F);
		left.setTextureOffset(12, 13).addCuboid(1.005F, -5.0F, 1.0093F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		handleAttach = new ModelPart(this);
		handleAttach.setPivot(0.0F, 1.0F, 1.0F);
		handles.addChild(handleAttach);
		setRotationAngle(handleAttach, -0.1745F, 0.0F, 0.0F);
		handleAttach.setTextureOffset(12, 17).addCuboid(-0.5F, -5.7352F, -0.7902F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		wheelF = new ModelPart(this);
		wheelF.setPivot(0.0F, 19.5F, -8.5F);
		wheelF.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		wheelF.setTextureOffset(1, 13).addCuboid(-0.5F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		wheelF.setTextureOffset(0, 12).addCuboid(-0.5F, -1.25F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		wheelF.setTextureOffset(0, 12).addCuboid(-0.5F, -1.0F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		rotate = new ModelPart(this);
		rotate.setPivot(0.0F, -0.0625F, 0.0F);
		wheelF.addChild(rotate);
		setRotationAngle(rotate, 0.3927F, 0.0F, 0.0F);
		rotate.setTextureOffset(0, 12).addCuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		rotate.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate.setTextureOffset(1, 13).addCuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate.setTextureOffset(0, 12).addCuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		rotate2 = new ModelPart(this);
		rotate2.setPivot(0.0F, -0.0625F, 0.0F);
		wheelF.addChild(rotate2);
		setRotationAngle(rotate2, 0.7854F, 0.0F, 0.0F);
		rotate2.setTextureOffset(0, 12).addCuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		rotate2.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate2.setTextureOffset(1, 13).addCuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate2.setTextureOffset(0, 12).addCuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		rotate3 = new ModelPart(this);
		rotate3.setPivot(0.0F, -0.0625F, 0.0F);
		wheelF.addChild(rotate3);
		setRotationAngle(rotate3, 1.1781F, 0.0F, 0.0F);
		rotate3.setTextureOffset(0, 12).addCuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		rotate3.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate3.setTextureOffset(1, 13).addCuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate3.setTextureOffset(0, 12).addCuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		spoke = new ModelPart(this);
		spoke.setPivot(0.0F, 0.0F, 0.0F);
		wheelF.addChild(spoke);
		setRotationAngle(spoke, -0.7854F, 0.0F, 0.0F);
		spoke.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		spoke2 = new ModelPart(this);
		spoke2.setPivot(0.0F, 0.0F, 0.0F);
		wheelF.addChild(spoke2);
		setRotationAngle(spoke2, 0.7854F, 0.0F, 0.0F);
		spoke2.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		spoke3 = new ModelPart(this);
		spoke3.setPivot(0.0F, 0.0F, 0.0F);
		wheelF.addChild(spoke3);
		setRotationAngle(spoke3, 1.5708F, 0.0F, 0.0F);
		spoke3.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		spoke4 = new ModelPart(this);
		spoke4.setPivot(0.0F, 0.0F, 0.0F);
		wheelF.addChild(spoke4);
		setRotationAngle(spoke4, 3.1416F, 0.0F, 0.0F);
		spoke4.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		wheelB = new ModelPart(this);
		wheelB.setPivot(0.0F, 19.5F, 9.5F);
		wheelB.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		wheelB.setTextureOffset(1, 13).addCuboid(-0.5F, -4.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		wheelB.setTextureOffset(0, 12).addCuboid(-0.5F, -1.25F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		wheelB.setTextureOffset(0, 12).addCuboid(-0.5F, -1.0F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		rotate4 = new ModelPart(this);
		rotate4.setPivot(0.0F, -0.0625F, 0.0F);
		wheelB.addChild(rotate4);
		setRotationAngle(rotate4, 0.3927F, 0.0F, 0.0F);
		rotate4.setTextureOffset(0, 12).addCuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		rotate4.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate4.setTextureOffset(1, 13).addCuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate4.setTextureOffset(0, 12).addCuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		rotate5 = new ModelPart(this);
		rotate5.setPivot(0.0F, -0.0625F, 0.0F);
		wheelB.addChild(rotate5);
		setRotationAngle(rotate5, 0.7854F, 0.0F, 0.0F);
		rotate5.setTextureOffset(0, 12).addCuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		rotate5.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate5.setTextureOffset(1, 13).addCuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate5.setTextureOffset(0, 12).addCuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		rotate6 = new ModelPart(this);
		rotate6.setPivot(0.0F, -0.0625F, 0.0F);
		wheelB.addChild(rotate6);
		setRotationAngle(rotate6, 1.1781F, 0.0F, 0.0F);
		rotate6.setTextureOffset(0, 12).addCuboid(-0.5F, -0.9375F, 3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		rotate6.setTextureOffset(2, 19).addCuboid(-0.5F, 3.5625F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate6.setTextureOffset(1, 13).addCuboid(-0.5F, -4.4375F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		rotate6.setTextureOffset(0, 12).addCuboid(-0.5F, -1.1875F, -4.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		spoke5 = new ModelPart(this);
		spoke5.setPivot(0.0F, 0.0F, 0.0F);
		wheelB.addChild(spoke5);
		setRotationAngle(spoke5, -0.7854F, 0.0F, 0.0F);
		spoke5.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		spoke6 = new ModelPart(this);
		spoke6.setPivot(0.0F, 0.0F, 0.0F);
		wheelB.addChild(spoke6);
		setRotationAngle(spoke6, 0.7854F, 0.0F, 0.0F);
		spoke6.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		spoke7 = new ModelPart(this);
		spoke7.setPivot(0.0F, 0.0F, 0.0F);
		wheelB.addChild(spoke7);
		setRotationAngle(spoke7, 1.5708F, 0.0F, 0.0F);
		spoke7.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);

		spoke8 = new ModelPart(this);
		spoke8.setPivot(0.0F, 0.0F, 0.0F);
		wheelB.addChild(spoke8);
		setRotationAngle(spoke8, 3.1416F, 0.0F, 0.0F);
		spoke8.setTextureOffset(23, 14).addCuboid(0.0F, -3.5F, -0.25F, 0.0F, 7.0F, 0.5F, 0.0F, false);
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

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
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

