package link.infra.bikeshed;

import io.netty.buffer.Unpooled;
import link.infra.bikeshed.blocks.Bikerack;
import link.infra.bikeshed.entities.Bike;
import link.infra.bikeshed.entities.DMCANotice;
import link.infra.bikeshed.items.DMCAWand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class BikeshedMain implements ModInitializer {

	public static final Identifier BIKE_ID = new Identifier("bikeshed", "bike");

	public static final EntityType<Bike> BIKE = Registry.register(Registries.ENTITY_TYPE,
		BIKE_ID,
		FabricEntityTypeBuilder.create(SpawnGroup.MISC, Bike::new)
			.dimensions(EntityDimensions.fixed(0.75f, 1.05f))
			// TODO: figure out velocity syncing issues
			.trackRangeBlocks(30)
			.forceTrackedVelocityUpdates(true)
			.build());

	public static final Identifier DMCA_NOTICE_ID = new Identifier("bikeshed", "dmca_notice");

	public static final EntityType<DMCANotice> DMCA_NOTICE = Registry.register(Registries.ENTITY_TYPE,
		DMCA_NOTICE_ID,
		FabricEntityTypeBuilder.create(SpawnGroup.MISC, DMCANotice::new)
			.dimensions(EntityDimensions.fixed(1.5f, 1.7f)).build());

	public static final Item DMCA_WAND = new DMCAWand(new Item.Settings());
	public static final Block BIKE_RACK = new Bikerack(FabricBlockSettings.of(Material.METAL).hardness(4.0f));

	public static final Identifier DMCA_ATTACK_PACKET_ID = new Identifier("bikeshed", "dmca_attack");
	public static final Identifier DMCA_ATTACK_BEAM_PACKET_ID = new Identifier("bikeshed", "dmca_attack_beam");

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(BIKE, Bike.createBikeAttributes());
		FabricDefaultAttributeRegistry.register(DMCA_NOTICE, DMCANotice.createLivingAttributes());

		Registry.register(Registries.ITEM, new Identifier("bikeshed", "dmca_wand"), DMCA_WAND);
		Registry.register(Registries.BLOCK, new Identifier("bikeshed", "bikerack"), BIKE_RACK);
		Registry.register(Registries.ITEM, new Identifier("bikeshed", "bikerack"), new BlockItem(BIKE_RACK, new Item.Settings()));

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.addAfter(Items.NAME_TAG, DMCA_WAND);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
			entries.addAfter(Items.BELL, BIKE_RACK);
		});

		ServerPlayNetworking.registerGlobalReceiver(DMCA_ATTACK_PACKET_ID, (server, player, handler, packetByteBuf, responseSender) -> {
			int entityId = packetByteBuf.readInt();
			Vec3d source = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			Vec3d target = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			// TODO: check the player has a DMCA wand?

			server.execute(() -> {
				World world = player.getEntityWorld();
				if (world.isChunkLoaded(BlockPos.ofFloored(source)) && world.isChunkLoaded(BlockPos.ofFloored(target))) {
					Entity hitEntity = world.getEntityById(entityId);
					if (hitEntity == null || hitEntity instanceof DMCANotice) {
						return;
					}

					PacketByteBuf beamPacket = new PacketByteBuf(Unpooled.buffer());
					beamPacket.writeDouble(source.getX());
					beamPacket.writeDouble(source.getY());
					beamPacket.writeDouble(source.getZ());
					beamPacket.writeDouble(target.getX());
					beamPacket.writeDouble(target.getY());
					beamPacket.writeDouble(target.getZ());

					PlayerLookup.tracking(hitEntity).forEach(playerx ->
						ServerPlayNetworking.send(playerx, DMCA_ATTACK_BEAM_PACKET_ID, beamPacket));

					NbtCompound nbt = new NbtCompound();
					hitEntity.writeNbt(nbt);
					Identifier type = Registries.ENTITY_TYPE.getId(hitEntity.getType());

					// Kill the hit entity
					hitEntity.kill();

					// Spawn the new entity
					DMCANotice notice = new DMCANotice(DMCA_NOTICE, world);
					notice.setExistingEntity(nbt, type);
					notice.updatePositionAndAngles(hitEntity.getX(), hitEntity.getY(), hitEntity.getZ(), hitEntity.getYaw(), hitEntity.getYaw());
					world.spawnEntity(notice);
				}
			});
		});
	}
}
