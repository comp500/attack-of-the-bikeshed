package link.infra.bikeshed;

import link.infra.bikeshed.BikeshedMain;
import link.infra.bikeshed.entities.BikeEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class BikeshedClient implements ClientModInitializer {
	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(BikeshedMain.BIKE,
			(dispatcher, context) -> new BikeEntityRenderer(dispatcher));
	}
}
