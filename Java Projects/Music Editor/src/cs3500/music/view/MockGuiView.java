package cs3500.music.view;


import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;

/**
 * Mock Gui View used for testing controllers
 */
public class MockGuiView extends Component implements GuiView {

  StringBuilder stringbuilder;

  public MockGuiView() {
    stringbuilder = new StringBuilder();
  }

  @Override
  public java.util.List<Selection> getSelections() {
    return null;
  }

  @Override
  public void addSelection(int x, int y) {
    stringbuilder.append("Added selection ");
    stringbuilder.append(x);
    stringbuilder.append(" and ");
    stringbuilder.append(y);
    stringbuilder.append("\n");
  }

  @Override
  public void clearSelections() {
    stringbuilder.append("cleared selections\n");
  }

  @Override
  public void display() {
    stringbuilder.append("displayed\n");
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    super.addKeyListener(listener);
    stringbuilder.append("added keylistener\n");
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    super.addMouseListener(listener);
    stringbuilder.append("added mouselistener\n");
  }

  @Override
  public int instrumentViewing() {
    return 0;
  }

  @Override
  public void changeInstrument() {
    stringbuilder.append("Changed instruments\n");
  }

  @Override
  public boolean safeAdd(MusicNote... notes) {
    return false;
  }

  @Override
  public MusicNote safeRemove(int pitch, int octave, int startBeat, int instrument) {
    return null;
  }

  @Override
  public void safeReplace(int pitch,
                          int octave,
                          int startBeat,
                          int instrument,
                          MusicNote newNote) {
    stringbuilder.append("replaced note at pitch ");
    stringbuilder.append(pitch);
    stringbuilder.append(" and at octave ");
    stringbuilder.append(octave);
    stringbuilder.append(" that starts at ");
    stringbuilder.append(startBeat);
    stringbuilder.append(" with instrument ");
    stringbuilder.append(instrument);
    stringbuilder.append(" with a new note\n");
  }

  @Override
  public void loadScoreInto(MusicView view) {
    stringbuilder.append("Loaded new view\n");
  }

  @Override
  public void loadScore(MusicScore score) throws Exception {
    stringbuilder.append("Loaded new Score\n");
  }

  @Override
  public void playSong() throws Exception {
    stringbuilder.append("Song playing\n");
  }

  @Override
  public void tick() throws Exception {
    stringbuilder.append("Tick\n");
  }

  @Override
  public String displayLogData() throws Exception {
    return stringbuilder.toString();
  }

  @Override
  public void startPlaying() throws Exception {
    stringbuilder.append("Started Playing\n");
  }

  @Override
  public void stopPlaying() throws Exception {
    stringbuilder.append("Stopped Playing\n");
  }

  @Override
  public boolean playing() {
    return false;
  }

  @Override
  public void setTime(int beat) throws IllegalArgumentException {
    stringbuilder.append("Time set to ");
    stringbuilder.append(beat);
    stringbuilder.append("\n");
  }

  @Override
  public int beat() {
    return 0;
  }

  @Override
  public int duration() {
    return 0;
  }

  @Override
  public int tempo() {
    return 2000;
  }
}
