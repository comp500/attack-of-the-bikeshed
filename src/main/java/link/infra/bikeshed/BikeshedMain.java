package link.infra.bikeshed;

import io.netty.buffer.Unpooled;
import link.infra.bikeshed.blocks.Bikerack;
import link.infra.bikeshed.entities.Bike;
import link.infra.bikeshed.entities.DMCANotice;
import link.infra.bikeshed.items.DMCAWand;
import net.fabricmc.api.ModInitializer;
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
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;


public class BikeshedMain implements ModInitializer {

	public static final Identifier BIKE_ID = new Identifier("bikeshed", "bike");

	public static final EntityType<Bike> BIKE = Registry.register(Registry.ENTITY_TYPE,
		BIKE_ID,
		FabricEntityTypeBuilder.create(SpawnGroup.MISC, Bike::new)
			.dimensions(EntityDimensions.fixed(0.75f, 1.05f))
			// TODO: figure out velocity syncing issues
			.trackRangeBlocks(30)
			.forceTrackedVelocityUpdates(true)
			.build());

	public static final Identifier DMCA_NOTICE_ID = new Identifier("bikeshed", "dmca_notice");

	public static final EntityType<DMCANotice> DMCA_NOTICE = Registry.register(Registry.ENTITY_TYPE,
		DMCA_NOTICE_ID,
		FabricEntityTypeBuilder.create(SpawnGroup.MISC, DMCANotice::new)
			.dimensions(EntityDimensions.fixed(1.5f, 1.7f)).build());

	public static final Item DMCA_WAND = new DMCAWand(new Item.Settings().group(ItemGroup.TRANSPORTATION));
	public static final Block BIKE_RACK = new Bikerack(FabricBlockSettings.of(Material.METAL).hardness(4.0f));

	public static final Identifier DMCA_ATTACK_PACKET_ID = new Identifier("bikeshed", "dmca_attack");
	public static final Identifier DMCA_ATTACK_BEAM_PACKET_ID = new Identifier("bikeshed", "dmca_attack_beam");

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(BIKE, Bike.createBikeAttributes());
		FabricDefaultAttributeRegistry.register(DMCA_NOTICE, DMCANotice.createLivingAttributes());

		Registry.register(Registry.ITEM, new Identifier("bikeshed", "dmca_wand"), DMCA_WAND);
		Registry.register(Registry.BLOCK, new Identifier("bikeshed", "bikerack"), BIKE_RACK);
		Registry.register(Registry.ITEM, new Identifier("bikeshed", "bikerack"), new BlockItem(BIKE_RACK, new Item.Settings().group(ItemGroup.TRANSPORTATION)));

		ServerPlayNetworking.registerGlobalReceiver(DMCA_ATTACK_PACKET_ID, (server, player, handler, packetByteBuf, responseSender) -> {
			int entityId = packetByteBuf.readInt();
			Vec3d source = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			Vec3d target = new Vec3d(packetByteBuf.readDouble(), packetByteBuf.readDouble(), packetByteBuf.readDouble());
			// TODO: check the player has a DMCA wand?

			server.execute(() -> {
				World world = player.getEntityWorld();
				if (world.isChunkLoaded(new BlockPos(source)) && world.isChunkLoaded(new BlockPos(target))) {
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
					Identifier type = Registry.ENTITY_TYPE.getId(hitEntity.getType());

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
