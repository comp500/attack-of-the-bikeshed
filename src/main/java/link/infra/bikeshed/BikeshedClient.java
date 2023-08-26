package link.infra.bikeshed;

import link.infra.bikeshed.entities.BikeEntityRenderer;
import link.infra.bikeshed.entities.BikeModel;
import link.infra.bikeshed.entities.DMCANoticeEntityRenderer;
import link.infra.bikeshed.entities.DMCANoticeModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import static link.infra.bikeshed.BikeshedMain.DMCA_ATTACK_BEAM_PACKET_ID;

public class BikeshedClient implements ClientModInitializer {
	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(BikeshedMain.BIKE, BikeEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(BikeModel.LAYER, BikeModel::getTexturedModelData);

		EntityRendererRegistry.register(BikeshedMain.DMCA_NOTICE, DMCANoticeEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(DMCANoticeModel.LAYER, DMCANoticeModel::getTexturedModelData);

		ClientPlayNetworking.registerGlobalReceiver(DMCA_ATTACK_BEAM_PACKET_ID, (client, handler, packetByteBuf, responseSender) -> {
			Vec3d source = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			Vec3d target = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			client.execute(() -> {
				Vec3d ray = target.subtract(source);
				double length = ray.length();
				Vec3d normal = ray.normalize();
				for (double i = 1.5; i < length; i += 1) {
					Vec3d currentPos = source.add(normal.multiply(i));
					handler.getWorld().addParticle(ParticleTypes.BARRIER, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 0, 0, 0);
				}
				handler.getWorld().addParticle(ParticleTypes.EXPLOSION, target.getX(), target.getY(), target.getZ(), 0, 0, 0);
			});
		});
	}
}
