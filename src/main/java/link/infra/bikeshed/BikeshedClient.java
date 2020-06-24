package link.infra.bikeshed;

import link.infra.bikeshed.BikeshedMain;
import link.infra.bikeshed.entities.BikeEntityRenderer;
import link.infra.bikeshed.entities.DMCANoticeEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;

public class BikeshedClient implements ClientModInitializer {
	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(BikeshedMain.BIKE,
			(dispatcher, context) -> new BikeEntityRenderer(dispatcher));

		EntityRendererRegistry.INSTANCE.register(BikeshedMain.DMCA_NOTICE,
			(dispatcher, context) -> new DMCANoticeEntityRenderer(dispatcher));
	}
}
