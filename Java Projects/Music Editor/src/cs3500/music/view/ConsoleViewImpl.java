package cs3500.music.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;

/**
 * Outputs music in simple text form into the console.
 */
public final class ConsoleViewImpl implements MusicView {

  /**
   * Where the text should be output to.
   */
  private Appendable output;

  /**
   * Length of the song.
   */
  private int duration;

  /**
   * The beat the song is currently on. Starts at 0.
   */
  private int beat;

  /**
   * The lowest octave in the song.
   */
  private int lowOctave;

  /**
   * The highest octave in the song.
   */
  private int highOctave;

  /**
   * The notes that are playing on this beat.
   */
  private List<MusicNote> notesPlaying;

  /**
   * Notes that have not been played yet. Sorted by start Time.
   */
  private List<MusicNote> notesLeft;

  /**
   * The score to be played.
   */
  private MusicScore score;

  /**
   * Is this view playing
   */
  private boolean playing;

  ConsoleViewImpl(Appendable output, MusicScore score) {
    this.score = Objects.requireNonNull(score);
    this.output = Objects.requireNonNull(output);
    initialize();
  }

  ConsoleViewImpl(MusicScore score) {
    this(System.out, score);
  }

  /**
   * Reset this view to its initial state.
   */
  public void initialize() {
    this.duration = score.duration();
    this.beat = 0;
    this.notesPlaying = new ArrayList<MusicNote>();
    this.notesLeft = score.notes();
    this.lowOctave = score.lowestOctave();
    this.highOctave = score.highestOctave();
  }

  /**
   * Display all the lines in the song. Starts song from beginning. Displays everything between the
   * lowest and highest octave.
   *
   * @throws IOException if appendable can not be appended to.
   */
  @Override
  public void playSong() throws Exception {
    initialize();
    this.startPlaying();
    if (duration != 0) {
      displayHeader();
    }
    for (int i = 0; i < duration; i++) {
      tick();
    }
    this.stopPlaying();
  }

  @Override
  public String displayLogData() {
    return output.toString();
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
    if (beat > this.duration || beat < 0) {
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
    return duration;
  }

  @Override
  public int tempo() {
    return this.score.getTempo();
  }

  /**
   * Display the next line of this score.
   *
   * @throws IOException if the output can't be appended to.
   */
  public void tick() throws IOException {
    if (playing) {
      if (beat <= duration) {
        int numSpaces = Integer.toString(duration).length();
        output.append(String.format("%0" + Integer.toString(numSpaces) + "d ", beat));
        notesPlaying.removeIf(n -> !n.playingAt(beat));

        int pitchSeperation = 3;
        int octaveLength = 36;
        int numOctaves = highOctave - lowOctave + 1;
        char[] asChars = new char[octaveLength * numOctaves];
        for (int i = 0; i < asChars.length; i++) {
          asChars[i] = ' ';
        }
        for (MusicNote note : notesPlaying) {
          int indexToChange = note.pitch * pitchSeperation
                              + note.octave * octaveLength
                              - lowOctave * octaveLength;
          asChars[indexToChange] = "|".charAt(0);
        }

        while (!notesLeft.isEmpty() && notesLeft.get(0).startTime == beat) {
          MusicNote note = notesLeft.get(0);
          int indexToChange = note.pitch * pitchSeperation
                              + note.octave * octaveLength
                              - lowOctave * octaveLength;
          asChars[indexToChange] = "X".charAt(0);
          notesPlaying.add(notesLeft.remove(0));
        }
        output.append(new String(asChars));
        output.append("\n");
        beat++;
      }
    }
  }

  /**
   * Display the octaves in this song.
   */
  private void displayHeader() throws IOException {
    int numSpaces = Integer.toString(duration).length();
    String header = " ";
    for (int i = 0; i < numSpaces; i++) {
      header += " ";
    }
    for (int i = lowOctave; i <= highOctave; i++) {
      header += createOctaveString(i);
    }
    output.append(header);
    output.append("\n");
  }

  /**
   * Create the string representing the notes at a given octave. i.e "CnC#n DnD#n En FnF#n GnG#n
   * AnA#n Bn ".
   *
   * @param octave The octave which this string represents.
   * @return String like above with ns replaced with octave.
   */
  private String createOctaveString(int octave) {
    return "Cn C#nDn D#nEn Fn F#nGn G#nAn A#nBn ".replace("n", Integer.toString(octave));
  }

  @Override
  public void loadScore(MusicScore score) {
    this.score = score;
    initialize();
  }
}
