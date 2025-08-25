package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

import java.util.Map;

/**
 * Simple distortion (clipping) effect to emulate 'earrape'. Level controls amount of gain/clipping.
 */
public class DistortionEffect implements Effect {
    @Override public String getId() { return "distortion"; }
    @Override public String getName() { return "Earrape / Distortion"; }
    @Override public String getDescription() { return "Heavy clipping / distortion (use judiciously)."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        double level = params.getOrDefault("level", 0.5) instanceof Double ? (Double)params.get("level") : 0.5;
        final float gain = (float)(1.0f + level * 20f); // up to 21x amplification before clipping
        return new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] buffer = audioEvent.getFloatBuffer();
                for (int i = 0; i < buffer.length; i++) {
                    float v = buffer[i] * gain;
                    // hard clip
                    if (v > 1f) v = 1f;
                    if (v < -1f) v = -1f;
                    buffer[i] = v;
                }
                return true;
            }

            @Override
            public void processingFinished() { }
        };
    }
}