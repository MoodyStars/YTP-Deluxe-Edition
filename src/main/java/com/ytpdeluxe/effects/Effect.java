package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioProcessor;

import java.util.Map;

/**
 * Audio effect abstraction. Implementations either produce an AudioProcessor (for streaming effects),
 * or will process a buffer offline (methods can return null if they process offline).
 *
 * Also provides a human-readable description for the GUI.
 */
public interface Effect {
    String getId();
    String getName();
    String getDescription();

    /**
     * Return an AudioProcessor that implements the effect, or null if this effect performs offline processing.
     * params contains keys:
     *  - "level": Double 0.0-1.0 (effect intensity)
     *  - "seed": Long (optional random seed)
     */
    AudioProcessor createProcessor(Map<String, Object> params);

    /**
     * Some effects may prefer to do offline processing on an array; the manager will handle both patterns.
     * Implementations can leave this empty (return null) if not using offline.
     */
    default short[] processOffline(short[] samples, int sampleRate, Map<String, Object> params) {
        return null;
    }
}