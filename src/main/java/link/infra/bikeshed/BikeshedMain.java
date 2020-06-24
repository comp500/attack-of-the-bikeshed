package link.infra.bikeshed;

import link.infra.bikeshed.blocks.Bikerack;
import link.infra.bikeshed.items.DMCAWand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class BikeshedMain implements ModInitializer {

	public static final EntityType<Bike> BIKE = Registry.register(Registry.ENTITY_TYPE,
		new Identifier("bikeshed", "bike"),
		FabricEntityTypeBuilder.create(SpawnGroup.MISC, Bike::new)
			.dimensions(EntityDimensions.fixed(0.75f, 1.05f))
			// TODO: figure out velocity syncing issues
			.trackable(30, 3, true).build());

	public static final Item DMCA_WAND = new DMCAWand(new Item.Settings().group(ItemGroup.TRANSPORTATION));
	public static final Block BIKE_RACK = new Bikerack(FabricBlockSettings.of(Material.METAL).hardness(4.0f));

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(BIKE, Bike.createBikeAttributes());

		Registry.register(Registry.ITEM, new Identifier("bikeshed", "dmca_wand"), DMCA_WAND);
		Registry.register(Registry.BLOCK, new Identifier("bikeshed", "bikerack"), BIKE_RACK);
		Registry.register(Registry.ITEM, new Identifier("bikeshed", "bikerack"), new BlockItem(BIKE_RACK, new Item.Settings().group(ItemGroup.TRANSPORTATION)));
	}
}
