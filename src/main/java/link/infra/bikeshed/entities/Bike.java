package link.infra.bikeshed.entities;

import link.infra.bikeshed.blocks.Bikerack;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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
import java.util.Random;

@SuppressWarnings("EntityConstructor") // Stupid mcdev
public class Bike extends LivingEntity {
	public float wheelPosition = 0;
	public int lastUsedAge = 0;
	public ItemStack heldItem = ItemStack.EMPTY;
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
		if (slot == EquipmentSlot.MAINHAND) {
			return heldItem;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
			heldItem = stack;
		}
	}

	@Override
	public Iterable<ItemStack> getItemsHand() {
		return heldItem.isEmpty() ? Collections.emptyList() : Collections.singletonList(heldItem);
	}

	@Override
	public Arm getMainArm() {
		// Aww yeah left handed bike?
		return Arm.LEFT;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		lastUsedAge = nbt.getInt("LastUsedAge");
		heldItem = ItemStack.fromNbt(nbt.getCompound("HeldItem"));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putInt("LastUsedAge", lastUsedAge);
		if (!heldItem.isEmpty()) {
			NbtCompound heldItemNbt = new NbtCompound();
			heldItem.writeNbt(heldItemNbt);
			nbt.put("HeldItem", heldItemNbt);
		}
	}

	private Text prevCustomName = null;
	private boolean prevCustomNameVisible = false;
	private boolean customNameDirty = false;

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			if (age - lastUsedAge > ((20 * (5 * 60)) - (20 * 30))) {
				if (!customNameDirty) {
					prevCustomName = getCustomName();
					prevCustomNameVisible = isCustomNameVisible();
					setCustomName(new TranslatableText("entity.bikeshed.bike.crab"));
					setCustomNameVisible(true);
					customNameDirty = true;
				}
			}
			if (age - lastUsedAge > (20 * (5 * 60))) {
				kill();
			}
		}
	}

	private void resetCustomName() {
		if (customNameDirty) {
			setCustomName(prevCustomName);
			setCustomNameVisible(prevCustomNameVisible);
			customNameDirty = false;
		}
	}

	private boolean canBeHeld(Item item) {
		String ident = Registry.ITEM.getId(item).toString();
		return ident.contains("bonecheese") || ident.contains("bone_cheese") || ident.contains("bucket") || ident.contains("tater");
	}

	private String selectRandomOf(String... choices) {
		Random rand = new Random();
		return choices[rand.nextInt(choices.length)];
	}

	private Text getNameFromItem() {
		String ident = Registry.ITEM.getId(heldItem.getItem()).toString();

		if (ident.contains("bonecheese") || ident.contains("bone_cheese")) {
			return new LiteralText(selectRandomOf("b o n e", "bone cheese, with ease"));
		} else if (ident.contains("water_bucket")) {
			return new LiteralText(selectRandomOf("1000mb", "9223372036854775807/9223372036854775807", "1.0F", "3/3 bottles"));
		} else if (ident.contains("lava_bucket")) {
			return new LiteralText("Heat: 1000 degrees Kelvinheit");
		} else if (ident.contains("milk_bucket")) {
			return new LiteralText("milk blocks when??");
		} else if (ident.contains("pufferfish_bucket")) {
			return new LiteralText("helo i am puf fish");
		} else if (ident.contains("salmon_bucket")) {
			return new LiteralText("NBT fluids haha yes");
		} else if (ident.contains("bucket")) {
			return new LiteralText("fluid api go brrr");
		}
		return null;
	}

	private void setNameFromItem() {
		setCustomName(getNameFromItem());
		setCustomNameVisible(true);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (isAlive()) {
			lastUsedAge = age;
			resetCustomName();
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
				if (!itemStack.isEmpty()) {
					if (Registry.ITEM.getId(itemStack.getItem()).equals(new Identifier("minecraft", "iron_ingot"))) {
						// TODO: consume item?
						heal(2f);
						((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getY(), getZ(), 3, 0.5, 0.25, 0.5, 0);
						return ActionResult.success(false);
					} else if (canBeHeld(itemStack.getItem()) && heldItem.isEmpty()) {
						// TODO: consume item?
						heldItem = itemStack.copy();
						heldItem.setCount(1);
						if (!customNameDirty) {
							setNameFromItem();
						}
						return ActionResult.success(false);
					}
				}
				// If sneaking, give item to player
				if (player.isSneaking()) {
					if (!heldItem.isEmpty()) {
						ItemEntity itemEntity = new ItemEntity(world, getX(), getY(), getZ(), heldItem);
						itemEntity.setPickupDelay(0);
						world.spawnEntity(itemEntity);
						heldItem = ItemStack.EMPTY;
						if (!customNameDirty) {
							setCustomName(null);
							setCustomNameVisible(false);
						}
						return ActionResult.success(false);
					}
					return ActionResult.PASS;
				}
				player.setYaw(getYaw());
				player.setPitch(getPitch());
				player.startRiding(this);
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
				resetCustomName();
				// Update movement and rotation to that of the primary passenger
				LivingEntity ent = (LivingEntity) getPrimaryPassenger();
				setYaw(ent.getYaw());
				prevYaw = getYaw();
				setPitch(ent.getPitch() * 0.5F);
				setRotation(getYaw(), getPitch());
				bodyYaw = getYaw();
				headYaw = bodyYaw;
				float newSidewaysSpeed = ent.sidewaysSpeed * 0.5F;
				float newForwardSpeed = ent.forwardSpeed;
				if (newForwardSpeed <= 0.0F) {
					// If going backwards, move slower
					newForwardSpeed *= 0.25F;
				}

				this.airStrafingSpeed = this.getMovementSpeed() * 0.1F;
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
			resetCustomName();
			// Translate 0.45 backwards, accounting for yaw
			double height = getY() + getMountedHeightOffset() + passenger.getHeightOffset();
			Vec3d backwardsOffset = new Vec3d(0, 0, -0.45).rotateY((float) Math.toRadians(-bodyYaw));
			passenger.setPosition(getX() + backwardsOffset.getX(), height, getZ() + backwardsOffset.getZ());
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
		resetCustomName();
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
					double height = world.getDismountHeight(targetBlockPos);
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
		BlockState state = getBlockStateAtPos();
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
			setBodyYaw(yaw);
		}
	}
}
