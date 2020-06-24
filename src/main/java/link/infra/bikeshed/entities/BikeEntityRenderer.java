package link.infra.bikeshed.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
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