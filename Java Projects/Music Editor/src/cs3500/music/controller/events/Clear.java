package cs3500.music.controller.events;

import cs3500.music.model.MusicScoreImpl;
import cs3500.music.view.GuiView;

/**
 * Clear the given view.
 */
public class Clear implements Runnable {

  private GuiView view;

  public Clear(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    try {
      view.loadScore(new MusicScoreImpl.Builder().build());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
