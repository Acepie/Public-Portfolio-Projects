package cs3500.music.controller.events;

import cs3500.music.model.MusicNote;
import cs3500.music.view.GuiView;
import cs3500.music.view.Selection;

/**
 * Add notes at selected spaces to given view if possible
 */
public class AddNote implements Runnable {

  private GuiView view;

  public AddNote(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.instrumentViewing() != -2 && view.instrumentViewing() != -1) {
      for (Selection s : view.getSelections()) {
        view.safeAdd(new MusicNote.NoteBuilder().pitch(s.pitch).octave(s.octave)
            .startTime(s.beat).instrument(view.instrumentViewing()).build());
      }
    } else if (!view.playing() && view.instrumentViewing() == -1) {
      for (Selection s : view.getSelections()) {
        view.safeAdd(new MusicNote.NoteBuilder().pitch(s.pitch).octave(s.octave)
            .startTime(s.beat).instrument(0).build());
      }
    }
  }
}
