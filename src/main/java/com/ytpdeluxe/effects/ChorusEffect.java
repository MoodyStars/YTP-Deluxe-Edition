package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.effects.DelayEffect;

import java.util.Map;

/**
 * Chorus/Echo-like effect using multiple delays. This is a simple implementation mapping level to delay depth.
 */
public class ChorusEffect implements Effect {
    @Override public String getId() { return "chorus"; }
    @Override public String getName() { return "Chorus Effect"; }
    @Override public String getDescription() { return "Adds chorus / echo to the audio."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        double level = params.getOrDefault("level", 0.5) instanceof Double ? (Double)params.get("level") : 0.5;
        float delaySeconds = 0.02f + (float)(level * 0.1f); // 20ms..120ms
        float gain = (float)(0.3 + level * 0.7);
        return new DelayEffect(delaySeconds, gain, 44100f);
    }
}