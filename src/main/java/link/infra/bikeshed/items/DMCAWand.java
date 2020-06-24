package link.infra.bikeshed.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class DMCAWand extends Item {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public DMCAWand(Settings settings) {
		super(settings);
		attributeModifiers = ImmutableMultimap.<EntityAttribute, EntityAttributeModifier>builder()
			.put(ReachEntityAttributes.REACH, new EntityAttributeModifier("Reach", 30, EntityAttributeModifier.Operation.ADDITION))
			.build();
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("item.bikeshed.dmca_wand.tooltip").formatted(Formatting.ITALIC, Formatting.GRAY));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (!(entity instanceof PlayerEntity)) {
			// TODO: sync?
			if (user.getEntityWorld().isClient()) {
				// Spawn particles
				Vec3d entityPos;
				if (MinecraftClient.getInstance().crosshairTarget != null) {
					entityPos = MinecraftClient.getInstance().crosshairTarget.getPos();
				} else {
					entityPos = entity.getPos();
				}
				Vec3d rayVector = entityPos.subtract(user.getCameraPosVec(0));
				double length = rayVector.length();
				Vec3d normal = rayVector.normalize();
				for (double i = 1.5; i < length; i += 1) {
					Vec3d currentPos = user.getCameraPosVec(0).add(normal.multiply(i));
					user.getEntityWorld().addParticle(ParticleTypes.BARRIER, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 0, 0, 0);
				}

				// Replace entity
				// TODO: replace entity
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return ActionResult.FAIL;
	}
}
