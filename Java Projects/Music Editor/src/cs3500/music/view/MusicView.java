package cs3500.music.view;

import cs3500.music.model.MusicScore;

/**
 * Interface for views for music viewer.
 */
public interface MusicView {

  /**
   * Load in a new music score.
   *
   * @param score Score to be loaded in.
   */
  void loadScore(MusicScore score) throws Exception;

  /**
   * Plays the song loaded into this view.
   */
  void playSong() throws Exception;

  /**
   * Advance the view 1 tick
   */
  void tick() throws Exception;

  /**
   * Displays log data for testing purposes.
   *
   * @return Log information based on view.
   */
  String displayLogData() throws Exception;

  /**
   * Start playing the song.
   */
  void startPlaying() throws Exception;

  /**
   * Stop playing the song.
   */
  void stopPlaying() throws Exception;

  /**
   * Is this view playing currently?
   *
   * @return whether this view is playing.
   */
  boolean playing();

  /**
   * The set start time of this view.
   *
   * @param beat The beat to be set.
   * @throws IllegalArgumentException if beat is after the duration of the song or less than 0
   */
  void setTime(int beat) throws IllegalArgumentException;

  /**
   * Get the current beat this view is at.
   *
   * @return the beat number.
   */
  int beat();

  /**
   * Get the length of the song being viewed.
   *
   * @return The beat length of the song.
   */
  int duration();

  /**
   * Get the tempo of the song being viewed.
   *
   * @return The amount of microseconds between beats.
   */
  int tempo();

}
