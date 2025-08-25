package com.ytpdeluxe.gui;

import com.ytpdeluxe.effects.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainWindow extends JFrame {
    private final DefaultListModel<Effect> effectsListModel = new DefaultListModel<>();
    private final JList<Effect> effectsJList = new JList<>(effectsListModel);
    private final JCheckBox randomizeCheckbox = new JCheckBox("Randomize effects");
    private final JSlider probabilitySlider = new JSlider(0, 100, 50);
    private final JSlider levelSlider = new JSlider(0, 100, 50);
    private File loadedFile;

    public MainWindow() {
        setTitle("YTP+ Deluxe Edition");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        populateEffects();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        setContentPane(main);

        // Left: effect list
        JPanel left = new JPanel(new BorderLayout(4, 4));
        left.setBorder(BorderFactory.createTitledBorder("Effects"));
        effectsJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        effectsJList.setCellRenderer(new EffectCellRenderer());
        left.add(new JScrollPane(effectsJList), BorderLayout.CENTER);

        // Center: controls
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        JButton loadButton = new JButton("Load audio...");
        loadButton.addActionListener(this::onLoad);
        JButton applyButton = new JButton("Apply / Play");
        applyButton.addActionListener(this::onApply);
        center.add(loadButton);
        center.add(Box.createRigidArea(new Dimension(0, 6)));
        center.add(randomizeCheckbox);
        center.add(new JLabel("Effect probability (%)"));
        center.add(probabilitySlider);
        center.add(new JLabel("Global effect level (0-100)"));
        center.add(levelSlider);
        center.add(Box.createRigidArea(new Dimension(0, 6)));
        center.add(applyButton);

        // Right: effect details/help
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Selected effect description"));
        JTextArea desc = new JTextArea();
        desc.setEditable(false);
        right.add(new JScrollPane(desc), BorderLayout.CENTER);

        effectsJList.addListSelectionListener(e -> {
            Effect selected = effectsJList.getSelectedValue();
            desc.setText(selected == null ? "" : selected.getDescription());
        });

        main.add(left, BorderLayout.WEST);
        main.add(center, BorderLayout.CENTER);
        main.add(right, BorderLayout.EAST);
    }

    private void populateEffects() {
        // Add built-in effects
        List<Effect> effects = List.of(
                new ReverseEffect(),
                new SpeedEffect(),
                new PitchShiftEffect(),
                new ChorusEffect(),
                new DistortionEffect(),
                new StutterEffect(),
                new RandomSoundInjection()
                // Many more effect classes can be added here (placeholders or real)
        );
        effects.forEach(effectsListModel::addElement);
    }

    private void onLoad(ActionEvent ev) {
        JFileChooser chooser = new JFileChooser();
        int r = chooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            loadedFile = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Loaded: " + loadedFile.getName());
        }
    }

    private void onApply(ActionEvent ev) {
        if (loadedFile == null) {
            JOptionPane.showMessageDialog(this, "Load an audio file first (WAV recommended).");
            return;
        }
        List<Effect> selected = effectsJList.getSelectedValuesList();
        boolean randomize = randomizeCheckbox.isSelected();
        int probability = probabilitySlider.getValue();
        int level = levelSlider.getValue();

        EffectManager manager = new EffectManager();
        // register selected effects with chosen level/probabilities
        for (Effect e : selected) {
            manager.registerEffect(e, probability / 100.0, level / 100.0);
        }

        if (randomize) {
            // If randomize is on, fill manager with all effects and random selection
            for (int i = 0; i < effectsListModel.size(); i++) {
                Effect e = effectsListModel.get(i);
                manager.registerEffect(e, probability / 100.0, level / 100.0);
            }
        }

        new Thread(() -> {
            try {
                manager.applyAndPlay(loadedFile);
            } catch (IOException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Error processing audio: " + ex.getMessage()));
            }
        }).start();
    }

    private static class EffectCellRenderer extends JLabel implements ListCellRenderer<Effect> {
        public Component getListCellRendererComponent(JList<? extends Effect> list, Effect value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getName());
            setOpaque(true);
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            return this;
        }
    }
}