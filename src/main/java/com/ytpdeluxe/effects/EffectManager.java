package com.ytpdeluxe.effects;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioPlayer;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Manages registered effects, their probabilities and levels, orchestrates applying them to a file and playing the result.
 * This is a simple manager: it will attempt to chain AudioProcessors when possible, and fall back to offline processing where necessary.
 */
public class EffectManager {
    private static class Registered {
        Effect effect;
        double probability; // 0..1
        double level; // 0..1
        Random rnd = new Random();
    }

    private final List<Registered> registered = new ArrayList<>();

    public void registerEffect(Effect effect, double probability, double level) {
        Registered r = new Registered();
        r.effect = effect;
        r.probability = probability;
        r.level = level;
        registered.add(r);
    }

    public void applyAndPlay(File input) throws IOException, UnsupportedAudioFileException {
        // For simplicity: create an AudioDispatcher and add processors that were chosen by probability.
        List<be.tarsos.dsp.AudioProcessor> processors = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        // Iterate registered effects and decide by probability
        for (Registered r : registered) {
            if (Math.random() <= r.probability) {
                params.put("level", r.level);
                be.tarsos.dsp.AudioProcessor p = r.effect.createProcessor(params);
                if (p != null) {
                    processors.add(p);
                } else {
                    // If offline processing is required, do a simple offline path:
                    // naive: convert input to temp WAV, run offline effect, write file, then continue chain.
                    // To keep example concise, offline processors are not fully implemented here.
                }
            }
        }

        // AudioDispatcherFactory: streaming play
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(input, 2048, 0);
        for (be.tarsos.dsp.AudioProcessor a : processors) {
            dispatcher.addAudioProcessor(a);
        }
        // final: audio player to speakers
        dispatcher.addAudioProcessor(new AudioPlayer(dispatcher.getFormat()));

        // Blocking run
        dispatcher.run();
    }
}