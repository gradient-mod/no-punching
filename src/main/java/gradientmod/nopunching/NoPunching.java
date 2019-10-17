package gradientmod.nopunching;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(NoPunching.MOD_ID)
public class NoPunching {
  public static final String MOD_ID = "nopunching";

  public NoPunching() {
    Config.registerConfig(FMLPaths.CONFIGDIR.get());
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void disablePunching(final PlayerEvent.BreakSpeed event) {
    if(!Config.ENABLED.get()) {
      return;
    }

    final BlockState state = event.getState();
    final PlayerEntity player = event.getPlayer();

    if(state.getHarvestTool() == null || state.getBlockHardness(player.getEntityWorld(), event.getPos()) <= Config.MAX_HARDNESS.get()) {
      return;
    }

    final ItemStack held = player.getHeldItemMainhand();

    if(!held.isEmpty()) {
      if(held.canHarvestBlock(state)) {
        return;
      }

      for(final ToolType toolType : held.getToolTypes()) {
        if(state.isToolEffective(toolType)) {
          return;
        }
      }
    }

    if(Config.MODE.get() == Config.Mode.SLOW) {
      event.setNewSpeed(event.getOriginalSpeed() * Config.SPEED_MULTIPLIER.get());
    } else {
      event.setCanceled(true);
    }
  }
}
