package warrook.lunamancy.utils.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;
import warrook.lunamancy.Lunamancy;
import warrook.lunamancy.blocks.entities.LightTransmitterImpl;
import warrook.lunamancy.utils.LocalizationKeys;

import java.util.Optional;

import static warrook.lunamancy.utils.LocalizationKeys.*;

public class LightUtils {
    public static boolean isValidConnection(RegistryKey<World> fromWorld, RegistryKey<World> toWorld, BlockPos fromPos, BlockPos toPos) {
        //TODO: Message to player
        if (fromWorld != toWorld) {
            Lunamancy.log(Level.INFO, GUI_NET_CONNECTING.translate("diff_dimensions"));
            return false;
        }
        if (!toPos.isWithinDistance(fromPos, 10d)) {
            Lunamancy.log(Level.INFO, GUI_NET_CONNECTING.translate("too_far"));
            return false;
        }
        return true;
    }

    public static boolean connect(World world, BlockPos from, BlockPos to) {
        //Could ask the objects instead of the manager, but this seems more generic
        //(but less performant since it runs through all nets...)
        LightNetManager manager = LightNetManager.of((ServerWorld) world);
        Optional<LightNet> netFrom = Optional.ofNullable(manager.getNetWithNodePos(from));
        Optional<LightNet> netTo = Optional.ofNullable(manager.getNetWithNodePos(to));
        BlockEntity entityFrom = world.getBlockEntity(from);
        BlockEntity entityTo = world.getBlockEntity(to);

        if (netFrom.isPresent() && netTo.isPresent()) {
            if (netFrom.get().equals(netTo.get())) {
                Lunamancy.log(Level.INFO, GUI_NET_CONNECTING.translate("same_net"));
                return false;
            }
            //TODO: Merge confirmation
            manager.merge(netTo.get(), netFrom.get());
            Lunamancy.log(Level.INFO, GUI_NET_CONNECTING.translate("success"));
            return true;
        }

        LightNet net;
        if (netFrom.isPresent() ^ netTo.isPresent()) {
            net = netTo.orElseGet(netFrom::get);
        } else {
            net = manager.makeAndRegisterNewNet();
        }

        LightNodeInfo infoFrom = getOrCreateInfo(entityFrom, from, net);
        LightNodeInfo infoTo = getOrCreateInfo(entityTo, to, net);
        infoFrom.addConnection(infoTo);
        infoTo.addConnection(infoFrom);

        //I don't think this is needed due to getOrCreateInfo()
        trySetInfo(entityFrom, infoFrom);
        trySetInfo(entityTo, infoTo);
        net.addNodes(infoFrom, infoTo);

        Lunamancy.log(Level.INFO, GUI_NET_CONNECTING.translate("success"));

        manager.markDirty();
        return true;
    }

    private static LightNodeInfo getOrCreateInfo(@Nullable BlockEntity entity, BlockPos pos, LightNet net) {
        return entity instanceof LightTransmitterImpl && ((LightTransmitterImpl) entity).getNodeInfo() != null
                ? ((LightTransmitterImpl) entity).getNodeInfo()
                : new LightNodeInfo(pos, net);
    }

    private static boolean trySetInfo(@Nullable BlockEntity entity, LightNodeInfo info) {
        return entity instanceof LightTransmitterImpl && ((LightTransmitterImpl) entity).setNodeInfo(info);
    }
}
