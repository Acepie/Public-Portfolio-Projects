package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Moves song backward 1 beat
 */
public class MoveBackward implements Runnable {

  private GuiView view;

  public MoveBackward(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.beat() != 0) {
      view.setTime(view.beat() - 1);
    }
  }
}