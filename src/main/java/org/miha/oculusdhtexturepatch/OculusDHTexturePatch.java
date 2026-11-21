package org.miha.oculusdhtexturepatch;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(OculusDHTexturePatch.MODID)
public class OculusDHTexturePatch {

    public static final String MODID = "oculusdhtexturepatch";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OculusDHTexturePatch() {
        LOGGER.info("Oculus Distant Horizons Texture Patch initialized!");
    }
}
