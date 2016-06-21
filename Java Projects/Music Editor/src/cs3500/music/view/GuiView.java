package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;

import cs3500.music.model.MusicNote;

/**
 * Sub-interface of Music View specifically for visual views. Can cycle through displaying
 * instruments one at a time until the end where it displays all of them
 */
public interface GuiView extends MusicView {

  /**
   * Get a list of all the places currently selected on the gui.
   *
   * @return List of selections.
   */
  List<Selection> getSelections();

  /**
   * Select a new place on the gui.
   *
   * @param x x coordinate of view.
   * @param y y coordinate of view.
   */
  void addSelection(int x, int y);

  /**
   * Unselect all selections.
   */
  void clearSelections();

  /**
   * Display visual view.
   */
  void display();

  /**
   * Add a new key listener to this view.
   *
   * @param listener listener to be added.
   */
  void addKeyListener(KeyListener listener);

  /**
   * Add a new mouse listener to this view.
   *
   * @param listener listener to be added.
   */
  void addMouseListener(MouseListener listener);

  /**
   * Get the instrument currently being viewed. If there are no instruments in the score return -1
   * If all the instruments are being viewed then return -2
   *
   * @return the midi number of the instrument.
   */
  int instrumentViewing();

  /**
   * Change the instrument currently being viewed. Scrolls through instruments in the song. If the
   * last instrument is the current instrument then display all the instruments
   */
  void changeInstrument();

  /**
   * Adds all the notes that don't cause collisions to the score drawn by this view.
   *
   * @param notes notes to add.
   * @return if a note was added.
   */
  boolean safeAdd(MusicNote... notes);

  /**
   * Removes the note present with the given properties if it exists.
   *
   * @param pitch      Pitch of the note.
   * @param octave     Octave of the note.
   * @param startBeat  Starting beat of the note.
   * @param instrument Instrument of the note.'
   * @return The note removed or null if nothing was removed.
   */
  MusicNote safeRemove(int pitch, int octave, int startBeat, int instrument);

  /**
   * Replace the note present with the given properties with a new note if possible.
   *
   * @param pitch      Pitch of the note.
   * @param octave     Octave of the note.
   * @param startBeat  Starting beat of the note.
   * @param instrument Instrument of the note.
   * @param newNote    The new note to add instead.
   */
  void safeReplace(int pitch, int octave, int startBeat, int instrument, MusicNote newNote);

  /**
   * Load the score represented by this view into another view
   *
   * @param view The view to load this view's score into.
   */
  void loadScoreInto(MusicView view);
}
