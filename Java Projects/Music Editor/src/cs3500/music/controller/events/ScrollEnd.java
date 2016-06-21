package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Scroll a given view back to the start
 */
public class ScrollEnd implements Runnable {

  /**
   * View to run on.
   */
  private GuiView view;

  public ScrollEnd(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    this.view.setTime(view.duration() + 1);
  }
}