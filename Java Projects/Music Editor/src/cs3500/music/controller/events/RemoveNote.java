package cs3500.music.controller.events;

import cs3500.music.view.GuiView;
import cs3500.music.view.Selection;

/**
 * Remove notes at selected spaces to given view if possible
 */
public class RemoveNote implements Runnable {

  private GuiView view;

  public RemoveNote(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.instrumentViewing() != -1 && view.instrumentViewing() != -2) {
      for (Selection s : view.getSelections()) {
        view.safeRemove(s.pitch, s.octave, s.beat, view.instrumentViewing());
      }
    } else if (!view.playing() && view.instrumentViewing() == -1) {
      for (Selection s : view.getSelections()) {
        view.safeRemove(s.pitch, s.octave, s.beat, 0);
      }
    }
  }
}
