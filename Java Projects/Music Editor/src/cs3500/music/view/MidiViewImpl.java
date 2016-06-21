package cs3500.music.view;

import java.util.List;
import java.util.Objects;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.VoiceStatus;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;

/**
 * Midi song playback class.
 */
public final class MidiViewImpl implements MusicView {

  /**
   * The synth to send data to.
   */
  private final Synthesizer synth;

  /**
   * The receiver to send data to.
   */
  private final Receiver receiver;

  /**
   * The score to be played back.
   */
  private MusicScore song;

  /**
   * The beat this song is currently at.
   */
  private int beat;

  /**
   * Is this view playing
   */
  private boolean playing;

  MidiViewImpl(MusicScore song, boolean testing) throws MidiUnavailableException {
    this.song = Objects.requireNonNull(song);
    this.beat = 0;
    this.playing = false;
    if (testing) {
      this.synth = new MockSynth();
    } else {
      this.synth = MidiSystem.getSynthesizer();
    }
    this.receiver = synth.getReceiver();
    synth.open();
  }

  /**
   * Play a single note by sending the proper messages to our receiver.
   *
   * @param note The note to be played.
   * @throws InvalidMidiDataException if something goes wrong with midi.
   */
  private void playNote(MusicNote note) throws InvalidMidiDataException {
    MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, note.instrument,
        note.getMidiPitch(), note.volume);
    MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, note.instrument,
        note.getMidiPitch(), note.volume);
    this.receiver.send(start, (this.synth.getMicrosecondPosition()));
    this.receiver.send(stop, (note.duration * song.getTempo() + this.synth.getMicrosecondPosition
        ()));
  }

  /**
   * Initializes notes to our song's notes.
   */
  private void initialize() {
    this.beat = 0;
  }

  @Override
  public void loadScore(MusicScore score) {
    this.song = Objects.requireNonNull(score);
    initialize();
  }

  @Override
  public void playSong() throws Exception {
    initialize();
    startPlaying();
    while (beat <= song.duration()) {
      tick();
      Thread.sleep(song.getTempo() / 1000);
    }
    this.receiver.close();
  }

  @Override
  public void tick() throws InvalidMidiDataException {
    if (playing) {
      sendMessageAtTime(beat);
      beat++;
    }
  }

  /**
   * Send the messages for all the notes starting at time.
   *
   * @param time The time that the notes start at.
   */
  private void sendMessageAtTime(int time) throws InvalidMidiDataException {
    List<MusicNote> notesPlayingNow = song.notesStartingAtTime(time);
    for (MusicNote note : notesPlayingNow) {
      playNote(note);
    }
  }

  @Override
  public String displayLogData() throws Exception {
    initialize();
    startPlaying();
    while (beat <= song.duration()) {
      tick();
    }
    return receiver.toString();
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
    if (beat > this.song.duration() + 1 || beat < 0) {
      throw new IllegalArgumentException();
    }
    this.beat = beat;
  }

  @Override
  public int beat() {
    return beat;
  }

  @Override
  public int duration() {
    return this.song.duration();
  }

  @Override
  public int tempo() {
    return this.song.getTempo();
  }

  /**
   * Mock synthesizer used for testing midi functions.
   */
  private class MockSynth implements Synthesizer {

    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
      return new MockReceiver();
    }

    @Override
    public Info getDeviceInfo() {
      return null;
    }

    @Override
    public void open() throws MidiUnavailableException {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
      return false;
    }

    @Override
    public long getMicrosecondPosition() {
      return 0;
    }

    @Override
    public int getMaxReceivers() {
      return 0;
    }

    @Override
    public int getMaxTransmitters() {
      return 0;
    }

    @Override
    public List<Receiver> getReceivers() {
      return null;
    }

    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
      return null;
    }

    @Override
    public List<Transmitter> getTransmitters() {
      return null;
    }

    @Override
    public int getMaxPolyphony() {
      return 0;
    }

    @Override
    public long getLatency() {
      return 0;
    }

    @Override
    public MidiChannel[] getChannels() {
      return new MidiChannel[0];
    }

    @Override
    public VoiceStatus[] getVoiceStatus() {
      return new VoiceStatus[0];
    }

    @Override
    public boolean isSoundbankSupported(Soundbank soundbank) {
      return false;
    }

    @Override
    public boolean loadInstrument(Instrument instrument) {
      return false;
    }

    @Override
    public void unloadInstrument(Instrument instrument) {

    }

    @Override
    public boolean remapInstrument(Instrument from, Instrument to) {
      return false;
    }

    @Override
    public Soundbank getDefaultSoundbank() {
      return null;
    }

    @Override
    public Instrument[] getAvailableInstruments() {
      return new Instrument[0];
    }

    @Override
    public Instrument[] getLoadedInstruments() {
      return new Instrument[0];
    }

    @Override
    public boolean loadAllInstruments(Soundbank soundbank) {
      return false;
    }

    @Override
    public void unloadAllInstruments(Soundbank soundbank) {

    }

    @Override
    public boolean loadInstruments(Soundbank soundbank, Patch[] patchList) {
      return false;
    }

    @Override
    public void unloadInstruments(Soundbank soundbank, Patch[] patchList) {

    }
  }

  /**
   * Mock receiver for testing midi functionality.
   */
  private class MockReceiver implements Receiver {

    StringBuilder output;

    MockReceiver() {
      this.output = new StringBuilder();
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
      ShortMessage shortMessage = (ShortMessage) message;

      if (shortMessage.getCommand() == 144) {
        int pitch = shortMessage.getData1();
        output.append("Playing note of pitch:");
        output.append(pitch % 12);
        output.append(" and  octave:");
        output.append(pitch / 12 - 2);
        output.append(" for ");
      } else {
        output.append(Float.toString(timeStamp / 1000000f));
        output.append(" seconds ");
        output.append('\n');
      }
    }

    @Override
    public void close() {

    }

    @Override
    public String toString() {
      return output.toString();
    }
  }
}
