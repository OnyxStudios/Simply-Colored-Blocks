package dev.onyxstudios.simplycoloredblocks.utils;

import dev.onyxstudios.simplycoloredblocks.items.ColoredBlock;
import dev.onyxstudios.simplycoloredblocks.registry.ModItems;
import net.minecraft.util.math.vector.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static int createHex(int r, int g, int b) {
        return (0xff << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    public static Vector3i createRGB(int color) {
        int r = ((color & 0xff000000) >>> 24);
        int g = ((color & 0x00ff0000) >>> 16);
        int b = ((color & 0x0000ff00) >>> 8);

        return new Vector3i(r, g, b);
    }

    public static int findDistance(int color1, int color2) {
        int r1 = ((color1 & 0xff000000) >>> 24);
        int g1 = ((color1 & 0x00ff0000) >>> 16);
        int b1 = ((color1 & 0x0000ff00) >>> 8);

        int r2 = ((color2 & 0xff000000) >>> 24);
        int g2 = ((color2 & 0x00ff0000) >>> 16);
        int b2 = ((color2 & 0x0000ff00) >>> 8);

        int deltaR = r1 - r2;
        int deltaG = g1 - g2;
        int deltaB = b1 - b2;

        return (deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB);
    }

    public static List<ColoredBlock> findClosestColors(int color, int maxDistance) {
        List<ColoredBlock> blocks = new ArrayList<>();
        for (ColoredBlock block : ModItems.colors.values()) {
            int distance = findDistance(color, block.getColor());

            if(distance <= maxDistance) {
                blocks.add(block);
            }
        }

        blocks.sort((block1, block2) -> block2.getColor() - block1.getColor());
        return blocks;
    }
}
