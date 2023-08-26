package link.infra.bikeshed.blocks;

import link.infra.bikeshed.BikeshedMain;
import link.infra.bikeshed.entities.Bike;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class Bikerack extends Block {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

	public Bikerack(Settings settings) {
		super(settings);
		setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction dir = state.get(FACING);
		switch(dir) {
			case NORTH:
			case SOUTH:
				return VoxelShapes.cuboid(0f, 0f, 3f/16f, 1f, 1f/16f, 13f/16f);
			case EAST:
			case WEST:
				return VoxelShapes.cuboid(3f/16f, 0f, 0f, 13f/16f, 1f/16f, 1f);
			default:
				return VoxelShapes.fullCube();
		}
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	public Vec3d getBikeOffset(BlockState state) {
		Direction dir = state.get(FACING);
		return new Vec3d(-dir.getOffsetX(), -dir.getOffsetY(), -dir.getOffsetZ());
	}

	public float getBikeYaw(BlockState state) {
		Direction dir = state.get(FACING);
		return dir.asRotation();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.hasVehicle()) {
			if (player.getEntityWorld().isClient()) {
				return ActionResult.SUCCESS;
			}
			// TODO: survival cost/recipe, and configuration
			// If there are nearby bikes with no passengers, put the player on one
			List<Bike> nearbyBikes = world.getEntitiesByClass(Bike.class, new Box(pos).expand(1), e -> !e.hasPassengers());
			if (nearbyBikes.size() > 0) {
				Bike bike = nearbyBikes.get(0);
				player.setYaw(bike.getYaw());
				player.setPitch(bike.getPitch());
				player.startRiding(bike);
				return ActionResult.SUCCESS;
			}

			Bike bike = new Bike(BikeshedMain.BIKE, world);
			Vec3d vec = Vec3d.of(pos).add(0.5, 0, 0.5).add(getBikeOffset(state));
			float yaw = getBikeYaw(state);
			bike.updatePositionAndAngles(vec.getX(), vec.getY(), vec.getZ(), yaw, 0);
			bike.setHeadYaw(yaw);
			bike.setBodyYaw(yaw);
			world.spawnEntity(bike);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
