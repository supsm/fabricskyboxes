package io.github.amerebagatelle.fabricskyboxes.util;

import com.google.common.collect.Range;
import com.mojang.serialization.Codec;
import io.github.amerebagatelle.fabricskyboxes.util.object.MinMaxEntry;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Function;

public class Utils {
    /**
     * @return Whether the value is within any of the minMaxEntries.
     */
    public static boolean checkRanges(double value, List<MinMaxEntry> minMaxEntries) {
        return minMaxEntries.isEmpty() || minMaxEntries.stream()
                .anyMatch(minMaxEntry -> Range.closed(minMaxEntry.getMin(), minMaxEntry.getMax())
                        .contains((float) value));
    }

    /**
     * Gets the amount of ticks in between start and end, on a 24000 tick system.
     *
     * @param start The start of the time you wish to measure
     * @param end   The end of the time you wish to measure
     * @return The amount of ticks in between start and end
     */
    public static int getTicksBetween(int start, int end) {
        if (end < start) end += 24000;
        return end - start;
    }

    public static boolean isInTimeInterval(int currentTime, int startTime, int endTime) {
        if (currentTime >= 24000 && currentTime < 0) {
            throw new RuntimeException("Invalid current time, value must be between 0-23999: " + currentTime);
        }
        if (startTime <= endTime) {
            return currentTime >= startTime && currentTime <= endTime;
        } else {
            return currentTime >= startTime || currentTime <= endTime;
        }
    }

    public static float normalizeTime(float maxAlpha, int currentTime, int startTime, int endTime) {
        if (!isInTimeInterval(currentTime, startTime, endTime))
            return 0f;

        int range = (endTime - startTime + 24000) % 24000;
        if (range == 0) {
            return 0f;
        }

        float position = (float) ((currentTime - startTime + 24000) % 24000) / range;
        return position * maxAlpha;
    }

    public static Codec<Integer> getClampedInteger(int min, int max) {
        if (min > max) {
            throw new UnsupportedOperationException("Maximum value was lesser than than the minimum value");
        }
        return Codec.INT.xmap(f -> MathHelper.clamp(f, min, max), Function.identity());
    }

    public static Codec<Float> getClampedFloat(float min, float max) {
        if (min > max) {
            throw new UnsupportedOperationException("Maximum value was lesser than than the minimum value");
        }
        return Codec.FLOAT.xmap(f -> MathHelper.clamp(f, min, max), Function.identity());
    }

    public static Codec<Double> getClampedDouble(double min, double max) {
        if (min > max) {
            throw new UnsupportedOperationException("Maximum value was lesser than than the minimum value");
        }
        return Codec.DOUBLE.xmap(f -> MathHelper.clamp(f, min, max), Function.identity());
    }
}
