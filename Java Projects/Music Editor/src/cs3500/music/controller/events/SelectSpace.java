package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Select a new space on the given view and clear all the old ones.
 */
public class SelectSpace implements MouseRunnable {

  private GuiView view;

  public SelectSpace(GuiView view) {
    this.view = view;
  }


  @Override
  public void run(int x, int y) {
    view.clearSelections();
    view.addSelection(x, y);
  }
}
