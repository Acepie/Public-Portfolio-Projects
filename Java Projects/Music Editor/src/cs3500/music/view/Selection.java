package cs3500.music.view;

/**
 * Represents a selected spot on the gui.
 */
public final class Selection {

  /**
   * The pitch of the selection.
   */
  public final int pitch;

  /**
   * The octave of the selection.
   */
  public final int octave;

  /**
   * The beat this selection is on.
   */
  public final int beat;

  public Selection(int pitch, int octave, int beat) {
    this.pitch = pitch;
    this.octave = octave;
    this.beat = beat;
  }
}
