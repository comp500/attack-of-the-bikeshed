package link.infra.bikeshed.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.netty.buffer.Unpooled;
import link.infra.bikeshed.BikeshedMain;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
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
				Vec3d target;
				if (MinecraftClient.getInstance().crosshairTarget != null) {
					target = MinecraftClient.getInstance().crosshairTarget.getPos();
				} else {
					target = entity.getPos();
				}
				Vec3d source = user.getCameraPosVec(0);

				// TODO: wow this looks horribly insecure... I should probably validate this on the server somehow
				PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
				data.writeInt(entity.getEntityId());
				data.writeDouble(source.getX());
				data.writeDouble(source.getY());
				data.writeDouble(source.getZ());
				data.writeDouble(target.getX());
				data.writeDouble(target.getY());
				data.writeDouble(target.getZ());

				ClientSidePacketRegistry.INSTANCE.sendToServer(BikeshedMain.DMCA_ATTACK_PACKET_ID, data);
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
