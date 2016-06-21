package cs3500.music.model;

import java.util.Comparator;

/**
 * Comparator for Music Notes that sorts by start time.
 */
public final class TimeComparator implements Comparator<MusicNote> {

  @Override
  public int compare(MusicNote o1, MusicNote o2) {
    return o1.startTime - o2.startTime;
  }
}
