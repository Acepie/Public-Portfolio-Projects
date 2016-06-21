package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Moves song forward 1 beat
 */
public class MoveForward implements Runnable {

  private GuiView view;

  public MoveForward(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    if (!view.playing() && view.beat() != view.duration()) {
      view.setTime(view.beat() + 1);
    }
  }
}
