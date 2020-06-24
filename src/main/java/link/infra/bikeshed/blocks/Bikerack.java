package link.infra.bikeshed.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

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

	// TODO: Add right click action to spawn bike
}
