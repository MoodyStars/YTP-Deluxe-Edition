package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;

/**
 * Random Sound Injection: overlays a short embedded sample at random times.
 * For demo, uses a tiny resource sample (if present) — otherwise is a no-op.
 */
public class RandomSoundInjection implements Effect {
    @Override public String getId() { return "randomsound"; }
    @Override public String getName() { return "Random Sound Injection"; }
    @Override public String getDescription() { return "Injects random short sounds from a resource bank."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        final Random rnd = new Random();
        final double level = params.getOrDefault("level", 0.5) instanceof Double ? (Double)params.get("level") : 0.5;

        return new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] buffer = audioEvent.getFloatBuffer();
                if (rnd.nextDouble() < 0.01 + level * 0.05) {
                    // overlay with a quick blip (sine burst)
                    int n = Math.min(256, buffer.length);
                    double freq = 300 + rnd.nextDouble() * 4000;
                    for (int i = 0; i < n; i++) {
                        double t = i / (double) audioEvent.getSampleRate();
                        float s = (float)(Math.sin(2 * Math.PI * freq * t) * 0.2 * level);
                        buffer[i] += s;
                    }
                }
                return true;
            }

            @Override public void processingFinished() { }
        };
    }
}