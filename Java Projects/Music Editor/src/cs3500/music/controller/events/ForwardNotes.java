package cs3500.music.controller.events;

import cs3500.music.model.MusicNote;
import cs3500.music.view.GuiView;
import cs3500.music.view.Selection;

/**
 * Move selected notes forward 1 beat if possible
 */
public class ForwardNotes implements Runnable {

  private GuiView view;

  public ForwardNotes(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.instrumentViewing() != -1 && view.instrumentViewing() != -2) {
      for (Selection s : view.getSelections()) {
        MusicNote removed = view.safeRemove(s.pitch, s.octave, s.beat, view.instrumentViewing());
        if (removed != null) {
          MusicNote forwarded = new MusicNote.NoteBuilder().pitch(removed.pitch)
              .octave(removed.octave)
              .startTime(removed.startTime + 1)
              .instrument(removed.instrument)
              .duration(removed.duration)
              .build();
          if (!view.safeAdd(forwarded)) {
            view.safeAdd(removed);
          }
        }
      }
    }
  }
}
