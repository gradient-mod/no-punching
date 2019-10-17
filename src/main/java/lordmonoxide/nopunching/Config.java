package lordmonoxide.nopunching;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {
  private static final String GENERAL_CATEGORY = "general";
  private static final ForgeConfigSpec SERVER_CONFIG;

  public static final ForgeConfigSpec.BooleanValue ENABLED;
  public static final ForgeConfigSpec.ConfigValue<Float> MAX_HARDNESS;
  public static final ForgeConfigSpec.EnumValue<Mode> MODE;
  public static final ForgeConfigSpec.ConfigValue<Float> SPEED_MULTIPLIER;

  static {
    final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    builder.comment("General configuration").push(GENERAL_CATEGORY);

    ENABLED = builder
      .comment("Enables or disables the entire mod")
      .define("enabled", true);

    MAX_HARDNESS = builder
      .comment("The maximum hardness a player can break without the correct tool")
      .define("max_hardness", 1.0f);

    MODE = builder
      .comment("Choose whether to slow or outright prevent block-breaking")
      .defineEnum("mode", Mode.PREVENT);

    SPEED_MULTIPLIER = builder
      .comment("If set to \"slow\" mode, how slow should it be?")
      .define("speed_multiplier", 0.1f);

    builder.pop();

    SERVER_CONFIG = builder.build();
  }

  public static void registerConfig(final Path configDir) {
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);

    final CommentedFileConfig configData = CommentedFileConfig
      .builder(configDir.resolve(NoPunching.MOD_ID + "-server.toml"))
      .sync()
      .autosave()
      .autoreload()
      .writingMode(WritingMode.REPLACE)
      .build();

    configData.load();
    SERVER_CONFIG.setConfig(configData);
  }

  public enum Mode {
    SLOW, PREVENT
  }
}
