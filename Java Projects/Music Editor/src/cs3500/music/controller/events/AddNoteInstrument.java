package cs3500.music.controller.events;

import cs3500.music.model.MusicNote;
import cs3500.music.view.GuiView;
import cs3500.music.view.Selection;

/**
 * Add notes at selected spaces to given view if possible
 */
public class AddNoteInstrument implements Runnable {

  private GuiView view;

  private final int instrument;

  public AddNoteInstrument(GuiView view, int instrument) {
    this.view = view;
    this.instrument = instrument;
  }

  @Override
  public void run() {
    if (!view.playing()) {
      for (Selection s : view.getSelections()) {
        view.safeAdd(new MusicNote.NoteBuilder().pitch(s.pitch).octave(s.octave)
                .startTime(s.beat).instrument(instrument).build());
      }
    }
  }
}
