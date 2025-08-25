package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

import java.util.Map;
import java.util.Random;

/**
 * Stutter loop: repeats short slices randomly.
 */
public class StutterEffect implements Effect {
    @Override public String getId() { return "stutter"; }
    @Override public String getName() { return "Stutter Loop"; }
    @Override public String getDescription() { return "Short choppy repetition (stutter)."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        double level = params.getOrDefault("level", 0.5) instanceof Double ? (Double)params.get("level") : 0.5;
        final Random rnd = new Random();
        final int maxRepeat = Math.max(1, (int)(1 + level * 6));
        final int window = (int)(256 + level * 2048);

        return new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] buf = audioEvent.getFloatBuffer();
                // naive: for some frames randomly copy earlier frame to create stutter
                if (rnd.nextDouble() < 0.02 + level * 0.3) {
                    int rep = rnd.nextInt(maxRepeat) + 1;
                    float[] copy = new float[Math.min(window, buf.length)];
                    System.arraycopy(buf, 0, copy, 0, copy.length);
                    for (int r = 0; r < rep; r++) {
                        for (int i = 0; i < copy.length && i < buf.length; i++) {
                            buf[i] = (buf[i] + copy[i]) / 2f;
                        }
                    }
                }
                return true;
            }

            @Override
            public void processingFinished() { }
        };
    }
}