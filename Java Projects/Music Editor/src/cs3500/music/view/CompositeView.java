package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;

/**
 * Combined visual and audible view.
 */
public class CompositeView implements GuiView {

  private GuiMusicView visualView;
  private MidiViewImpl midiView;
  private int beat;
  private boolean playing;

  CompositeView(GuiMusicView visualView, MidiViewImpl midiView) {
    this.visualView = visualView;
    this.midiView = midiView;
    this.beat = 0;
    this.playing = false;
  }

  CompositeView(MusicScore score) throws Exception {
    this.visualView = new GuiMusicView(score);
    this.midiView = new MidiViewImpl(score, false);
  }

  @Override
  public List<Selection> getSelections() {
    return visualView.getSelections();
  }

  @Override
  public void addSelection(int pitch, int beat) {
    visualView.addSelection(pitch, beat);
  }

  @Override
  public void clearSelections() {
    visualView.clearSelections();
  }

  @Override
  public void display() {
    visualView.display();
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    this.visualView.addKeyListener(listener);
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    this.visualView.addMouseListener(listener);
  }

  @Override
  public int instrumentViewing() {
    return visualView.instrumentViewing();
  }

  @Override
  public void changeInstrument() {
    this.visualView.changeInstrument();
  }

  @Override
  public boolean safeAdd(MusicNote... notes) {
    return this.visualView.safeAdd(notes);
  }

  @Override
  public MusicNote safeRemove(int pitch, int octave, int startBeat, int instrument) {
    return this.visualView.safeRemove(pitch, octave, startBeat, instrument);
  }

  @Override
  public void safeReplace(int pitch,
                          int octave,
                          int startBeat,
                          int instrument,
                          MusicNote newNote) {
    this.visualView.safeReplace(pitch, octave, startBeat, instrument, newNote);
  }

  @Override
  public void loadScoreInto(MusicView view) {
    this.visualView.loadScoreInto(view);
  }

  @Override
  public void startPlaying() throws Exception {
    this.visualView.loadScoreInto(midiView);
    if (beat >= visualView.duration() + 1) {
      setTime(0);
    }
    this.setTime(beat);
    this.playing = true;
    this.visualView.startPlaying();
    this.midiView.startPlaying();
  }

  @Override
  public void stopPlaying() throws Exception {
    this.visualView.stopPlaying();
    this.midiView.stopPlaying();
    this.playing = false;
  }

  @Override
  public boolean playing() {
    return playing;
  }

  @Override
  public void setTime(int beat) throws IllegalArgumentException {
    if (beat > this.visualView.duration() + 1 || beat < 0) {
      throw new IllegalArgumentException();
    }
    this.beat = beat;
    this.visualView.setTime(beat);
    this.midiView.setTime(beat);
  }

  @Override
  public int beat() {
    return beat;
  }

  @Override
  public int duration() {
    return this.visualView.duration();
  }

  @Override
  public int tempo() {
    return visualView.tempo();
  }

  @Override
  public void loadScore(MusicScore score) throws Exception {
    this.visualView.loadScore(score);
    this.midiView.loadScore(score);
  }

  @Override
  public void playSong() throws Exception {
    this.display();
    this.startPlaying();
    while (beat < visualView.duration()) {
      tick();
      Thread.sleep(visualView.tempo() / 1000);
    }
    this.stopPlaying();
  }

  @Override
  public void tick() throws Exception {
    if (playing) {
      if (beat == visualView.duration() + 1) {
        stopPlaying();
      } else {
        visualView.tick();
        midiView.tick();
        beat++;
      }
    }
  }

  @Override
  public String displayLogData() throws Exception {
    return this.visualView.displayLogData();
  }
}
