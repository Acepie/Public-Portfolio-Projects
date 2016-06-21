package cs3500.music.model;

import java.util.Comparator;

/**
 * Comparator for Music Notes that sorts by pitch.
 */
public final class PitchComparator implements Comparator<MusicNote> {

  @Override
  public int compare(MusicNote o1, MusicNote o2) {
    return o1.pitch - o2.pitch + (o1.octave - o2.octave) * 12;
  }
}
