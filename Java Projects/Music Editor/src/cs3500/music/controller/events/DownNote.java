package cs3500.music.controller.events;

import cs3500.music.model.MusicNote;
import cs3500.music.view.GuiView;
import cs3500.music.view.Selection;

/**
 * Move selected notes up 1 pitch if possible
 */
public class DownNote implements Runnable {

  private GuiView view;

  public DownNote(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.instrumentViewing() != -1 && view.instrumentViewing() != -2) {
      for (Selection s : view.getSelections()) {
        int newPitch = (s.pitch + 11) % 12;
        int newOct = s.octave - (newPitch + 1) / 12;
        if (newOct >= 0) {
          MusicNote removed = view.safeRemove(s.pitch, s.octave, s.beat, view.instrumentViewing());
          if (removed != null) {
            MusicNote downned = new MusicNote.NoteBuilder().pitch(newPitch)
                .octave(newOct)
                .startTime(removed.startTime)
                .instrument(removed.instrument)
                .duration(removed.duration)
                .build();
            if (!view.safeAdd(downned)) {
              view.safeAdd(removed);
            }
          }
        }
      }
    }
  }
}
