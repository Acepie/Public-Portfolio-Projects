package cs3500.music.view;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;

/**
 * The main frame for displaying our music view visually
 */
public final class GuiMusicView extends javax.swing.JFrame implements GuiView {

  /**
   * The scroll pane containing our main pane.
   */
  private JScrollPane scrollPane;

  /**
   * The panel containing the main content of our song.
   */
  private ConcreteGuiViewPanel displayPanel;

  /**
   * The lowest octave of our song.
   */
  private int lowestOctave;

  /**
   * The highest octave of our song.
   */
  private int highestOctave;

  /**
   * The duration of our song.
   */
  private int duration;

  /**
   * Constant representing the size in pixels of each note.
   */
  public final static int PITCHSIZE = 10;

  /**
   * Constant representing amount to pad the sides of the screen.
   */
  public final static int PADDING = 8;

  /**
   * Is this view currently advancing.
   */
  private boolean playing;

  /**
   * Creates new View
   */
  GuiMusicView(MusicScore score) {
    super("Visual Music Viewer");
    this.displayPanel = new ConcreteGuiViewPanel(score);
    this.scrollPane = new JScrollPane(displayPanel);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().add(scrollPane);
    initialize(score);
  }

  /**
   * Uses a given score to initialize data and underlying panels.
   *
   * @param score The score to initialize data with.
   */
  public void initialize(MusicScore score) {
    this.playing = false;
    this.lowestOctave = score.lowestOctave();
    this.highestOctave = score.highestOctave();
    this.duration = score.duration();
    this.displayPanel.initialize(score);
  }

  @Override
  public Dimension getPreferredSize() {
    int numOctave = highestOctave - lowestOctave + 1;
    int height = numOctave * 12 * GuiMusicView.PITCHSIZE
                 + GuiMusicView.PADDING * GuiMusicView.PITCHSIZE
                 + getInsets().top + getInsets().bottom;
    int width = (duration + GuiMusicView.PADDING) * GuiMusicView.PITCHSIZE
                + getInsets().left + getInsets().right;
    return new Dimension(width, height);
  }

  @Override
  public void loadScore(MusicScore score) {
    initialize(score);
    repaint();
  }

  @Override
  public void playSong() throws Exception {
    display();
    this.startPlaying();
    for (int i = 0; i < duration - 1; i++) {
      tick();
    }
    this.stopPlaying();
  }

  @Override
  public void tick() throws Exception {
    if (playing) {
      this.displayPanel.tick();
      int xCoord = (this.displayPanel.getBeat() + GuiMusicView.PADDING / 2)
                   * GuiMusicView.PITCHSIZE;
      Rectangle r = new Rectangle(xCoord + this.getSize().width - this.getInsets().right - this
          .getInsets().left - 5 * GuiMusicView.PITCHSIZE, 1, 1, 1);
      this.displayPanel.scrollRectToVisible(r);
      this.getContentPane().repaint();
    }
  }

  @Override
  public String displayLogData() {
    return this.displayPanel.toString();
  }

  @Override
  public List<Selection> getSelections() {
    return this.displayPanel.getSelections();
  }

  @Override
  public void addSelection(int x, int y) {
    this.displayPanel.addSelection(x, y);
    repaint();
  }

  @Override
  public void clearSelections() {
    this.displayPanel.clearSelections();
    repaint();
  }

  @Override
  public void display() {
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void startPlaying() throws Exception {
    this.playing = true;
  }

  @Override
  public void stopPlaying() throws Exception {
    this.playing = false;
  }

  @Override
  public boolean playing() {
    return playing;
  }

  @Override
  public void setTime(int beat) throws IllegalArgumentException {
    this.displayPanel.setTime(beat);
    int xCoord = (this.displayPanel.getBeat()) * GuiMusicView.PITCHSIZE;
    Rectangle r = new Rectangle(xCoord, 1, this.getWidth(), this.getHeight());
    this.displayPanel.scrollRectToVisible(r);
    this.getContentPane().repaint();
  }

  @Override
  public int beat() {
    return this.displayPanel.getBeat();
  }

  @Override
  public int duration() {
    return this.displayPanel.getScore().duration();
  }

  @Override
  public int tempo() {
    return this.displayPanel.getScore().getTempo();
  }

  @Override
  public synchronized void addMouseListener(MouseListener l) {
    super.addMouseListener(l);
    displayPanel.addMouseListener(l);
  }

  @Override
  public int instrumentViewing() {
    return displayPanel.getInstrument();
  }

  @Override
  public void changeInstrument() {
    this.displayPanel.changeInstrument();
    this.repaint();
  }

  @Override
  public boolean safeAdd(MusicNote... notes) {
    boolean added = false;
    for (MusicNote note : notes) {
      try {
        this.displayPanel.getScore().addNote(note);
        added = true;
      } catch (IllegalArgumentException e) {

      }
    }
    int instrument = notes[0].instrument;
    loadScore(this.displayPanel.getScore());
    while (displayPanel.getInstrument() != instrument) {
      displayPanel.changeInstrument();
    }
    return added;
  }

  @Override
  public MusicNote safeRemove(int pitch, int octave, int startBeat, int instrument) {
    if (displayPanel.getScore().containsNote(pitch, octave, startBeat, instrument)) {
      MusicNote note = displayPanel.getScore().getNote(pitch, octave,
          startBeat, instrument);
      displayPanel.getScore().removeNote(note);
      loadScore(this.displayPanel.getScore());
      return note;
    }
    return null;
  }

  @Override
  public void safeReplace(int pitch,
                          int octave,
                          int startBeat,
                          int instrument,
                          MusicNote newNote) {
    if (displayPanel.getScore().containsNote(pitch, octave, startBeat, instrument)) {
      MusicNote noteToMaybeRemove = displayPanel.getScore().getNote(pitch, octave,
          startBeat, instrument);
      safeRemove(pitch, octave, startBeat, instrument);
      try {
        this.displayPanel.getScore().addNote(newNote);
        System.out.print("added new note");
      } catch (IllegalArgumentException e) {
        this.displayPanel.getScore().addNote(noteToMaybeRemove);
      }
    } else {
      safeAdd(newNote);
    }
    loadScore(this.displayPanel.getScore());
  }

  @Override
  public void loadScoreInto(MusicView view) {
    try {
      view.loadScore(displayPanel.getScore());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
