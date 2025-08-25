package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.writer.WriterProcessor;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Map;

/**
 * Reverse effect: offline reversal of samples (creates a temp WAV and plays it).
 */
public class ReverseEffect implements Effect {
    @Override
    public String getId() { return "reverse"; }
    @Override
    public String getName() { return "Reverse Clip"; }
    @Override
    public String getDescription() { return "Reverses the audio clip (offline)."; }

    @Override
    public AudioProcessor createProcessor(Map<String, Object> params) {
        // Streaming reverse is complex; return null to signal offline processing.
        return null;
    }

    @Override
    public short[] processOffline(short[] samples, int sampleRate, Map<String, Object> params) {
        int n = samples.length;
        short[] out = new short[n];
        for (int i = 0; i < n; i++) out[i] = samples[n - 1 - i];
        return out;
    }
}