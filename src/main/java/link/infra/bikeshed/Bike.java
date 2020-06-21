package link.infra.bikeshed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;

@SuppressWarnings("EntityConstructor") // Stupid mcdev
public class Bike extends LivingEntity {
	public float wheelPosition = 0;

	protected Bike(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
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
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (!world.isClient()) {
			player.yaw = yaw;
			player.pitch = pitch;
			player.startRiding(this);
			// TODO: some logic to GC unused bikes?
			// TODO: some logic to spawn bikes? or recipe?
			// TODO: check what interactions this blocks - like name tags!
		}
		return ActionResult.success(world.isClient());
	}

	public static DefaultAttributeContainer.Builder createBikeAttributes() {
		return createLivingAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (isAlive()) {
			if (hasPassengers() && getPrimaryPassenger() instanceof LivingEntity) {
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
			// Translate 0.45 backwards, accounting for yaw
			double height = getY() + getMountedHeightOffset() + passenger.getHeightOffset();
			Vec3d backwardsOffset = new Vec3d(0, 0, -0.45).rotateY((float) Math.toRadians(-bodyYaw));
			passenger.updatePosition(getX() + backwardsOffset.getX(), height, getZ() + backwardsOffset.getZ());
		}
	}
}
