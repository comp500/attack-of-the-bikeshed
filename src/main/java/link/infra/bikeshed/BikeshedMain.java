package link.infra.bikeshed;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class BikeshedMain implements ModInitializer {

	public static final EntityType<Bike> BIKE = Registry.register(Registry.ENTITY_TYPE,
		new Identifier("bikeshed", "bike"),
		FabricEntityTypeBuilder.create(SpawnGroup.MISC, Bike::new)
			.dimensions(EntityDimensions.fixed(0.75f, 1.05f))
			// TODO: figure out velocity syncing issues
			.trackable(30, 3, true).build());

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(BIKE, Bike.createBikeAttributes());
	}
}
