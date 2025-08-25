```markdown
YTP+ Deluxe Edition — Java (Old Windows) — Source / Release
================================================================

Overview
--------
This project is a Java Swing application (YTP+ Deluxe Edition — feature-rich), designed for "old Windows" packaging via Launch4j. It focuses on audio "YouTube Poop"-style effects (and includes placeholders for visual effects). The project is intended as a starting point — many effects are implemented and many more are scaffolded for you to extend.

Key features implemented
- Swing GUI with effect toggles, probability, and level sliders
- Effects framework for audio effects
- Implemented audio effects: Reverse, Speed change, Pitch shift, Chorus/Echo, Distortion (Earrape), Stutter, Random sound injection
- Batch / random application via EffectManager
- Launch4j configuration and Windows build script

Requirements
------------
- Java 11+
- Maven 3.6+
- Optional: Launch4j (to create an EXE)
- Optional for advanced video/visual effects: FFmpeg (recommended)

How to build
------------
1. Build JAR:
   mvn clean package

2. Create EXE (Windows):
   - Install Launch4j (https://launch4j.sourceforge.net/) and ensure launch4j command-line is available, or edit build-windows.bat with the proper path.
   - Run:
     build-windows.bat

Files of interest
-----------------
- src/main/java/com/ytpdeluxe/gui/MainWindow.java — main Swing UI
- src/main/java/com/ytpdeluxe/effects/* — effect implementations and framework
- launch4j/ytp_plus_launch4j.xml — Launch4j config
- build-windows.bat — example script to build EXE (edit paths)

Extending
---------
- Audio: Add more AudioProcessor-based effects using TarsosDSP (dependencies are in pom.xml).
- Video: Use FFmpeg to apply overlays, invert colors, mirror frames, rainbow, green-screen portals, etc. I provide VisualEffect placeholder classes where you can call FFmpeg commands.

Notes
-----
- Some effects (e.g., Reverse) operate offline by generating a temporary WAV and playing it; streaming reversals are more complex.
- Randomness parameters (probability and level) are wired in — many effects include level sliders that scale their intensity.

License
-------
MIT — adapt as you need for your project.

```