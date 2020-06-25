package link.infra.bikeshed.entities;

import link.infra.bikeshed.blocks.Bikerack;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collections;

@SuppressWarnings("EntityConstructor") // Stupid mcdev
public class Bike extends LivingEntity {
	public float wheelPosition = 0;
	public int lastUsedAge = 0;
	//private float leanBlockDistance = 0F;
	//private float lastLeanBlockDistance = 0F;
	//public static final Vec3d BACK_WHEEL_OFFSET = new Vec3d(0, 0.37, -0.75);

	public Bike(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		this.stepHeight = 1.0F;
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {}

	@Override
	public Arm getMainArm() {
		// Aww yeah left handed bike?
		return Arm.LEFT;
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		lastUsedAge = tag.getInt("LastUsedAge");
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		tag.putInt("LastUsedAge", lastUsedAge);
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			if (age - lastUsedAge > ((20 * (5 * 60)) - (20 * 30))) {
				setCustomName(new TranslatableText("entity.bikeshed.bike.crab"));
			}
			if (age - lastUsedAge > (20 * (5 * 60))) {
				kill();
			}
		}
	}

	// TODO: some logic to GC unused bikes?

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (isAlive()) {
			lastUsedAge = age;
			if (hasPassengers()) {
				return ActionResult.PASS;
			}

			ItemStack itemStack = player.getStackInHand(hand);
			if (!itemStack.isEmpty()) {
				ActionResult actionResult = itemStack.useOnEntity(player, this, hand);
				if (actionResult.isAccepted()) {
					return actionResult;
				}
			}
			if (!world.isClient()) {
				if (Registry.ITEM.getId(itemStack.getItem()).equals(new Identifier("minecraft", "iron_ingot"))) {
					// TODO: consume item?
					heal(2f);
				} else {
					player.yaw = yaw;
					player.pitch = pitch;
					player.startRiding(this);
				}
			}
			return ActionResult.success(world.isClient());
		}
		return ActionResult.PASS;
	}

	public static DefaultAttributeContainer.Builder createBikeAttributes() {
		return createLivingAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (isAlive()) {
			if (hasPassengers() && getPrimaryPassenger() instanceof LivingEntity) {
				lastUsedAge = age;
				// Update movement and rotation to that of the primary passenger
				LivingEntity ent = (LivingEntity) getPrimaryPassenger();
				yaw = ent.yaw;
				prevYaw = yaw;
				pitch = ent.pitch * 0.5F;
				setRotation(yaw, pitch);
				bodyYaw = yaw;
				headYaw = bodyYaw;
				float newSidewaysSpeed = ent.sidewaysSpeed * 0.5F;
				float newForwardSpeed = ent.forwardSpeed;
				if (newForwardSpeed <= 0.0F) {
					// If going backwards, move slower
					newForwardSpeed *= 0.25F;
				}

				this.flyingSpeed = this.getMovementSpeed() * 0.1F;
				if (this.isLogicalSideForUpdatingMovement()) {
					this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
					super.travel(new Vec3d(newSidewaysSpeed, movementInput.y, newForwardSpeed));
				} else if (ent instanceof PlayerEntity) {
					this.setVelocity(Vec3d.ZERO);
				}
			} else {
				super.travel(movementInput);
			}
		}
	}

	@Override
	public Entity getPrimaryPassenger() {
		return hasPassengers() ? getPassengerList().get(0) : null;
	}

	@Override
	public boolean isPushable() {
		// Don't allow pushing bikes with riders
		return !hasPassengers();
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		super.updatePassengerPosition(passenger);
		if (this.hasPassenger(passenger)) {
			lastUsedAge = age;
			// Translate 0.45 backwards, accounting for yaw
			double height = getY() + getMountedHeightOffset() + passenger.getHeightOffset();
			Vec3d backwardsOffset = new Vec3d(0, 0, -0.45).rotateY((float) Math.toRadians(-bodyYaw));
			passenger.updatePosition(getX() + backwardsOffset.getX(), height, getZ() + backwardsOffset.getZ());
		}
	}

	@Override
	public boolean isAffectedBySplashPotions() {
		return false;
	}

	@Override
	public boolean canHaveStatusEffect(StatusEffectInstance effect) {
		return false;
	}

	// TODO: readd leaning when going up blocks?
	//private static double blockCheckDistance = 2d;

	@Override
	public void tickMovement() {
		//this.lastLeanBlockDistance = this.leanBlockDistance;

		//Vec3d backWheelPos = getPos().add(BACK_WHEEL_OFFSET.rotateY((float) Math.toRadians(-bodyYaw)));
		// note: We can actually use world.getBlockCollisions(entity, targetBox).allMatch(VoxelShape::isEmpty);

//		Vec3d searchPos = null;
//		for (double i = 0.5d; i < blockCheckDistance + 0.5d; i++) {
//			searchPos = backWheelPos.add(new Vec3d(0, 0, i).rotateY((float) Math.toRadians(-bodyYaw)));
//			BlockPos blockInFront = new BlockPos(searchPos);
//			BlockPos blockInFrontAbove = new BlockPos(searchPos.add(0, 1, 0));
//			// If there is a collidable block above, stop searching
//			if (!world.getBlockState(blockInFrontAbove).getCollisionShape(world, blockInFrontAbove).isEmpty()) {
//				searchPos = null;
//				break;
//			} else if (!world.getBlockState(blockInFront).getCollisionShape(world, blockInFront).isEmpty()) {
//				// If there is a collidable block in front, finish searching
//				break;
//			}
//			searchPos = null;
//		}
//
//		if (searchPos == null) {
//			this.leanBlockDistance = 0.0F;
//		} else {
//			double searchDistance = backWheelPos.distanceTo(searchPos);
//			// TODO: make it work with the edge of the block?
//			this.leanBlockDistance = (float) searchDistance;
//		}
//		Vec3d end = backWheelPos.add(new Vec3d(0, 0, blockCheckDistance).rotateY((float) Math.toRadians(-bodyYaw)));
//		BlockHitResult res = world.rayTrace(new RayTraceContext(backWheelPos, end, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.NONE, this));
//		if (res.getType() == HitResult.Type.BLOCK) {
//			leanBlockDistance = (float) backWheelPos.distanceTo(res.getPos());
//		} else {
//			leanBlockDistance = 0.0F;
//		}

		super.tickMovement();
	}

//	public float getLeanBlockDistance(float tickDelta) {
//		return MathHelper.lerp(tickDelta, lastLeanBlockDistance, leanBlockDistance);
//	}


	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		lastUsedAge = age;
		Direction dir = getMovementDirection();
		if (dir.getAxis() == Direction.Axis.Y) {
			// If moving vertically, delegate to super
			Vec3d pos = super.updatePassengerForDismount(passenger);
			snapToBikeRack();
			return pos;
		} else {
			int[][] offsets = Dismounting.getDismountOffsets(dir);
			BlockPos currBlockPos = getBlockPos();
			BlockPos.Mutable targetBlockPos = new BlockPos.Mutable();

			for (EntityPose pose : passenger.getPoses()) {
				Box passengerBounds = passenger.getBoundingBox(pose);
				for (int[] offset : offsets) {
					targetBlockPos.set(currBlockPos.getX() + offset[0], currBlockPos.getY(), currBlockPos.getZ() + offset[1]);
					double height = world.getCollisionHeightAt(targetBlockPos);
					if (Dismounting.canDismountInBlock(height)) {
						Vec3d newPos = Vec3d.ofCenter(targetBlockPos, height);
						if (Dismounting.canPlaceEntityAt(world, passenger, passengerBounds.offset(newPos))) {
							passenger.setPose(pose);
							snapToBikeRack();
							return newPos;
						}
					}
				}
			}
		}
		Vec3d pos = super.updatePassengerForDismount(passenger);
		snapToBikeRack();
		return pos;
	}

	private void snapToBikeRack() {
		// Check current block pos
		BlockState state = getBlockState();
		BlockPos pos = getBlockPos();
		if (!(state.getBlock() instanceof Bikerack)) {
			// Check block pos in front
			pos = getBlockPos().offset(getMovementDirection());
			state = world.getBlockState(pos);
		}

		if (state.getBlock() instanceof Bikerack) {
			Bikerack bikerack = (Bikerack) state.getBlock();
			Vec3d vec = Vec3d.of(pos).add(0.5, 0, 0.5).add(bikerack.getBikeOffset(state));
			float yaw = bikerack.getBikeYaw(state);
			refreshPositionAndAngles(vec.getX(), vec.getY(), vec.getZ(), yaw, 0);
			setHeadYaw(yaw);
			setYaw(yaw);
		}
	}
}
