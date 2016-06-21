package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Runnable that takes in a view and toggles whether it is playing or not.
 */
public class TogglePlaying implements Runnable {

  /**
   * View to run on.
   */
  private GuiView view;

  public TogglePlaying(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (view.playing()) {
      try {
        view.stopPlaying();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        view.startPlaying();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
