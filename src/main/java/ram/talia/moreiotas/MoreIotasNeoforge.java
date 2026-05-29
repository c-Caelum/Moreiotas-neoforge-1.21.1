package ram.talia.moreiotas;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.common.lib.HexRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.ejml.MatrixFormattable;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.ops.SimpleOperations_DDRM;
import org.slf4j.Logger;
import ram.talia.moreiotas.common.lib.hex.MoreIotasActions;
import ram.talia.moreiotas.common.lib.hex.MoreIotasArithmetics;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;
import thedarkcolour.kotlinforforge.neoforge.KotlinModLoadingContext;
import org.ejml.dense.row.CommonOps_DDRM;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MoreIotasNeoforge.MODID)
public class MoreIotasNeoforge {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "moreiotasneoforge";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // public static final SimpleMatrix crasher = new SimpleMatrix(1,1, false);

    public static ResourceLocation id(String str) {
        return ResourceLocation.fromNamespaceAndPath(MODID, str);
    }
    public static boolean matrixIsEmpty(SimpleMatrix matrix) {
        return (matrix.getNumCols() * matrix.getNumRows()) < DoubleIota.TOLERANCE;
    }

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MoreIotasNeoforge(IEventBus bus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        bus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (MoreIotasNeoforge) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        bus.addListener(this::addCreative);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, MoreIotasConfig.SPEC);

        //bind(HexRegistries.ACTION, MoreIotasActions::register, bus);
        bus.addListener((RegisterEvent event) -> {
            event.register(HexRegistries.ACTION, actionRegistryEntryRegisterHelper -> {
                MoreIotasActions.register((t, rl) -> {
                    actionRegistryEntryRegisterHelper.register(rl, t);
                });
            });
        });
        //bind(HexRegistries.ARITHMETIC, MoreIotasArithmetics::register, bus);
        //bind(HexRegistries.IOTA_TYPE, MoreIotasIotaTypes::registerTypes, bus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    private <T> void bind(ResourceKey<? extends Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source, IEventBus bus) {
        bus.addListener((RegisterEvent event) -> {
            /*if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> {
                    event.register(registry, rl, () -> t);
                });
            }*/
        });
    }
}
