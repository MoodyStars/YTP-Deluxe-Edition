package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.effects.RateTransposer;

import java.util.Map;

/**
 * Speed up / Slow down using RateTransposer from TarsosDSP.
 * Level (0..1): 0.5 => slow, 1.0 => normal, >1 => fast. We'll map level to a rate between 0.5 and 2.0.
 */
public class SpeedEffect implements Effect {
    @Override public String getId() { return "speed"; }
    @Override public String getName() { return "Speed Up / Slow Down"; }
    @Override public String getDescription() { return "Change playback speed (resamples)."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        double level = params.getOrDefault("level", 0.5) instanceof Double ? (Double)params.get("level") : 0.5;
        // map level 0..1 to rate 0.5..2.0 (1.0 at 0.5)
        double rate = 0.5 + level * 1.5;
        return new RateTransposer(rate);
    }
}