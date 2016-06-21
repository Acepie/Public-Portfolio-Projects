package cs3500.music.model;

import java.util.List;

/**
 * Represents a score of music (a song) scores start at beat 1.
 */
public interface MusicScore {

  /**
   * Add some notes into this score.
   *
   * @param notes Music notes to be added.
   * @throws IllegalArgumentException if a note with the same pitch, octave, and instrument is
   *                                  playing at the same time as a note being added.
   */
  void addNote(MusicNote... notes);

  /**
   * Remove a note from this score.
   *
   * @param note Note to be removed.
   * @throws IllegalArgumentException if note is not in score.
   */
  void removeNote(MusicNote note);

  /**
   * Replace a note from this score with a new one.
   *
   * @param note    Note to be replaced.
   * @param newNote New note to replace old note.
   * @throws IllegalArgumentException if note is not in score or if newNote can not be added
   *                                  properly.
   */
  void replaceNote(MusicNote note, MusicNote newNote);

  /**
   * Composes this score with another score so the 2 are simultaneous. If two notes of the same
   * pitch, octave, and starttime are played, the longer one overrides the shorter one.
   *
   * @param score The other score to be added.
   * @return New score that plays this score and the given score simultaneously.
   */
  MusicScore composeScores(MusicScore score);

  /**
   * Adds another score directly following this score.
   *
   * @param score Score to be played after this one.
   * @return New score that plays this score and the given score successively.
   */
  MusicScore consecutiveScore(MusicScore score);

  /**
   * Gives the length of the score in beats.
   *
   * @return The length of the score in beats.
   */
  int duration();

  /**
   * Get a copy of the notes in the song sorted by startTime.
   *
   * @return A list of all the notes in the song.
   */
  List<MusicNote> notes();

  /**
   * Get a copy of the notes in the song playing at a certain time.
   *
   * @param time Time that notes are playing at.
   * @return A list of all the notes in the song.
   */
  List<MusicNote> notesAtTime(int time);

  /**
   * Get a copy of the notes in the song starting at a certain time.
   *
   * @param time Time that notes are starting at.
   * @return A list of all the notes in the song.
   */
  List<MusicNote> notesStartingAtTime(int time);

  /**
   * Get the octave of the highest note in the song. If there are no notes output 4 (middle).
   *
   * @return The octave of the highest note.
   */
  int highestOctave();

  /**
   * Get the octave of the lowest note in the song. If there are no notes output 4 (middle).
   *
   * @return The octave of the lowest note.
   */
  int lowestOctave();

  /**
   * Checks if there is a note with the given pitch octave and start time.
   *
   * @param pitch      Pitch of the note.
   * @param octave     Octave of the note.
   * @param startTime  Starting beat of the note.
   * @param instrument Instrument of the note.
   * @return True if a valid note exists false otherwise.
   */
  boolean containsNote(int pitch, int octave, int startTime, int instrument);

  /**
   * Gets the note with the given pitch, octave, and startTime if it exists in the score.
   *
   * @param pitch      Pitch of the note.
   * @param octave     Octave of the note.
   * @param startTime  StartTime of the note.
   * @param instrument Instrument of the note.
   * @return Note with given properties.
   * @throws IllegalArgumentException if note is not in score.
   */
  MusicNote getNote(int pitch, int octave, int startTime, int instrument);

  /**
   * Gets the tempo of the song in beats per minute.
   *
   * @return The tempo of the song.
   */
  int getTempo();

  /**
   * Get an identical but different copy of this score.
   *
   * @return A score representing the same song as this score.
   */
  MusicScore copy();

  /**
   * Get a list of the midinumbers of all the instruments in the song.
   *
   * @return list of instruments in song.
   */
  List<Integer> instruments();
}
