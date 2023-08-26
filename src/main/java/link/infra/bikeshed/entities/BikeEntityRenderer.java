package link.infra.bikeshed.entities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BikeEntityRenderer extends LivingEntityRenderer<Bike, BikeModel> {
	public BikeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BikeModel(context.getPart(BikeModel.LAYER)), 0.0f);
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

	@Override
	public void render(Bike bike, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		super.render(bike, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
		if (!bike.heldItem.isEmpty()) {
			matrixStack.push();
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MathHelper.lerp(tickDelta, bike.prevBodyYaw, bike.bodyYaw)));
			matrixStack.scale(0.5f, 0.5f, 0.5f);
			matrixStack.translate(0.4, 2.65, 0.9);
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-70));
			// TODO: check transforms
			MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer().renderItem(bike, bike.heldItem, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, true, matrixStack, vertexConsumerProvider, light);
			matrixStack.pop();
		}
	}

	@Override
	protected void setupTransforms(Bike entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
		//float distance = entity.getLeanBlockDistance(tickDelta);
		//if (distance > 0.0F) {
//			// Rotate around the back wheel
//			Vec3d rotationPointOffset = Bike.BACK_WHEEL_OFFSET.rotateY((float) Math.toRadians(-entity.bodyYaw));
//			matrices.translate(rotationPointOffset.getX(), rotationPointOffset.getY(), rotationPointOffset.getZ());
//			// TODO: clamp?
//			//matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(animationProgress % 360f));
//			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(entity.bodyYaw));
//			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(animationProgress % 360f));
//			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-entity.bodyYaw));
//			//matrices.multiply(Vector3f.POSITIVE_X.getRadialQuaternion((float) Math.atan(1.0F / distance)));
//			// TODO: disable to test rot point?
//			matrices.translate(-rotationPointOffset.getX(), -rotationPointOffset.getY(), -rotationPointOffset.getZ());

//			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevBodyYaw, entity.bodyYaw)));
//			// Rotate around the back wheel
//			matrices.translate(Bike.BACK_WHEEL_OFFSET.getX(), Bike.BACK_WHEEL_OFFSET.getY(), Bike.BACK_WHEEL_OFFSET.getZ());
//			// TODO: clamp?
//			//matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(animationProgress % 360f));
//
//			matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(animationProgress % 360f));
//
//			//matrices.multiply(Vector3f.POSITIVE_X.getRadialQuaternion((float) Math.atan(1.0F / distance)));
//
//			matrices.translate(-Bike.BACK_WHEEL_OFFSET.getX(), -Bike.BACK_WHEEL_OFFSET.getY(), -Bike.BACK_WHEEL_OFFSET.getZ());
//			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-MathHelper.lerp(tickDelta, entity.prevBodyYaw, entity.bodyYaw)));
		//}
	}
}
