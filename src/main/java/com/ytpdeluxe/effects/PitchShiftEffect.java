package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.effects.PitchShifter;

import java.util.Map;

/**
 * Pitch shift effect using TarsosDSP PitchShifter.
 * Level 0..1 maps to -6 .. +6 semitones.
 */
public class PitchShiftEffect implements Effect {
    @Override public String getId() { return "pitchshift"; }
    @Override public String getName() { return "Pitch Shift / Vibrato"; }
    @Override public String getDescription() { return "Lift or lower pitch (vibrato/pitch bend can be composed)."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        double level = params.getOrDefault("level", 0.5) instanceof Double ? (Double)params.get("level") : 0.5;
        double semitones = -6 + level * 12; // -6..+6
        float pitchShiftFactor = (float)Math.pow(2.0, semitones / 12.0);
        // Buffer size and overlap typical values:
        return new PitchShifter(pitchShiftFactor, 2048, 512, 44100f);
    }
}