package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Select a new space on the given view.
 */
public class MultiSelectSpace implements MouseRunnable {

  private GuiView view;

  public MultiSelectSpace(GuiView view) {
    this.view = view;
  }


  @Override
  public void run(int x, int y) {
    view.addSelection(x, y);
  }
}
