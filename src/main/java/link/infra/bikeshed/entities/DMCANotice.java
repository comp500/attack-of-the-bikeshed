package link.infra.bikeshed.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collections;

@SuppressWarnings("EntityConstructor") // Stupid mcdev
public class DMCANotice extends LivingEntity {
	private NbtCompound existingEntityNbt = null;
	private Identifier existingEntityType = null;

	public DMCANotice(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	public void setExistingEntity(NbtCompound nbt, Identifier entityType) {
		existingEntityNbt = nbt;
		existingEntityType = entityType;
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
		return Arm.RIGHT;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (!player.getEntityWorld().isClient && existingEntityNbt != null) {
			// Kill DMCA notice
			kill();

			// Spawn existing entity
			EntityType<? extends Entity> type = Registry.ENTITY_TYPE.get(existingEntityType);
			Entity newEnt = type.create(world);
			if (newEnt == null) {
				return ActionResult.FAIL;
			}
			newEnt.readNbt(existingEntityNbt);
			newEnt.updatePositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());
			world.spawnEntity(newEnt);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		if (existingEntityNbt != null) {
			nbt.put("ExistingEntity", existingEntityNbt);
			// TODO: should this use raw IDs?
			nbt.putString("ExistingEntityType", existingEntityType.toString());
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		existingEntityNbt = nbt.getCompound("ExistingEntity");
		existingEntityType = new Identifier(nbt.getString("ExistingEntityType"));
	}
}
