package cs3500.music.controller.events;

import cs3500.music.model.MusicNote;
import cs3500.music.view.GuiView;
import cs3500.music.view.Selection;

/**
 * Increase the duration of selected notes if possible.
 */
public class IncreaseDuration implements Runnable {

  private GuiView view;

  public IncreaseDuration(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.instrumentViewing() != -1 && view.instrumentViewing() != -2) {
      for (Selection s : view.getSelections()) {
        MusicNote removed = view.safeRemove(s.pitch, s.octave, s.beat, view.instrumentViewing());
        if (removed != null) {
          MusicNote backed = new MusicNote.NoteBuilder().pitch(removed.pitch)
              .octave(removed.octave)
              .startTime(removed.startTime)
              .instrument(removed.instrument)
              .duration(removed.duration + 1)
              .build();
          if (!view.safeAdd(backed)) {
            view.safeAdd(removed);
          }
        }
      }
    }
  }
}