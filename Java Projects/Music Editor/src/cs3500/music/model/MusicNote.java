package cs3500.music.model;

/**
 * A Music Note represents a single note of music.
 */
public final class MusicNote {

  /**
   * Pitch is a numerical representation of the pitch. Pitch must be between 0 and 11 inclusive. 0
   * represents C, 1 represents C#, ... 10 represents A#, 11 represents B.
   */
  public final int pitch;

  /**
   * Octave represents the octave of the note. Octave must be above 0. Humans can not hear above
   * about 9 or below about 0 so octave must be between 0 and 9.
   */
  public final int octave;

  /**
   * Duration represents how long a note lasts for. Duration is measured in single beats. Must be
   * positive.
   */
  public final int duration;

  /**
   * Beat on which a note starts.
   */
  public final int startTime;

  /**
   * The instrument this note is played on.
   */
  public final int instrument;

  /**
   * The volume of this note.
   */
  public final int volume;

  /**
   * Constructs a music note
   *
   * @param pitch     The pitch of the note.
   * @param octave    The octave of the note.
   * @param duration  The duration of the note.
   * @param startTime The beat at which the note starts.
   * @throws IllegalArgumentException if any of the inputs are negative or if pitch or octave are
   *                                  out of range.
   */
  private MusicNote(int pitch, int octave, int duration,
                    int startTime, int instrument, int volume) {
    if (pitch < 0 || pitch > 11 || octave < 0 || octave > 9 || duration <= 0 || startTime < 0 ||
        instrument < 0 || volume < 0 || volume > 128) {
      throw new IllegalArgumentException();
    }
    this.pitch = pitch;
    this.octave = octave;
    this.duration = duration;
    this.startTime = startTime;
    this.instrument = instrument;
    this.volume = volume;
  }

  /**
   * Builder for note class.
   */
  public static class NoteBuilder {

    /**
     * Pitch is a numerical representation of the pitch. Pitch must be between 0 and 11 inclusive.
     * 0 represents C, 1 represents C#, ... 10 represents A#, 11 represents B.
     */
    private int pitch;

    /**
     * Octave represents the octave of the note. Octave must be above 0. Humans can not hear above
     * about 9 or below about 0 so octave must be between 0 and 9.
     */
    private int octave;

    /**
     * Duration represents how long a note lasts for. Duration is measured in single beats. Must be
     * positive.
     */
    private int duration;

    /**
     * Beat on which a note starts.
     */
    private int startTime;

    /**
     * The instrument this note is played on
     */
    private int instrument;

    /**
     * The volume of the note.
     */
    private int volume;

    /**
     * Load this builder with defaults.
     */
    public NoteBuilder() {
      this.pitch = 0;
      this.octave = 4;
      this.duration = 1;
      this.startTime = 1;
      this.instrument = 1;
      this.volume = 64;
    }

    /**
     * Build a new note and reset the defaults.
     *
     * @return New MusicNote with properties defined in this builder.
     */
    public MusicNote build() {
      MusicNote n = new MusicNote(pitch, octave, duration, startTime, instrument, volume);
      this.pitch = 0;
      this.octave = 4;
      this.duration = 1;
      this.startTime = 1;
      this.instrument = 1;
      this.volume = 93;
      return n;
    }

    /**
     * Set the pitch of this builder.
     *
     * @param pitch The new pitch.
     * @return This builder.
     */
    public NoteBuilder pitch(int pitch) {
      this.pitch = pitch;
      return this;
    }

    /**
     * Set the octave of this builder.
     *
     * @param octave The new octave.
     * @return This builder.
     */
    public NoteBuilder octave(int octave) {
      this.octave = octave;
      return this;
    }

    /**
     * Set the duration of this builder.
     *
     * @param duration The new duration.
     * @return This builder.
     */
    public NoteBuilder duration(int duration) {
      this.duration = duration;
      return this;
    }

    /**
     * Set the start time of this builder.
     *
     * @param startTime The new start time.
     * @return This builder.
     */
    public NoteBuilder startTime(int startTime) {
      this.startTime = startTime;
      return this;
    }

    /**
     * Set the instrument of this builder.
     *
     * @param instrument The new instrument.
     * @return This builder.
     */
    public NoteBuilder instrument(int instrument) {
      this.instrument = instrument;
      return this;
    }

    /**
     * Set the volume of this builder.
     *
     * @param volume The new volume.
     * @return This builder.
     */
    public NoteBuilder volume(int volume) {
      this.volume = volume;
      return this;
    }
  }

  /**
   * Return the endtime of this note.
   *
   * @return The last beat this note is playing on.
   */
  public int endTime() {
    return startTime + duration - 1;
  }

  @Override
  public String toString() {
    return "(" + Integer.toString(pitch) + " " + Integer.toString(octave) + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MusicNote) {
      MusicNote n = (MusicNote) obj;
      return this.pitch == n.pitch &&
             this.octave == n.octave &&
             this.duration == n.duration &&
             this.startTime == n.startTime &&
             this.instrument == n.instrument;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return pitch + octave * 12 + duration * 1000 + startTime * 10000 + instrument * 1000000;
  }

  /**
   * Is this note playing at a given time?
   *
   * @param time Time to be checked.
   * @return Whether this note is playing or not.
   */
  public boolean playingAt(int time) {
    return startTime <= time && endTime() > time;
  }

  public int getMidiPitch() {
    return this.pitch + (this.octave + 2) * 12;
  }
}
