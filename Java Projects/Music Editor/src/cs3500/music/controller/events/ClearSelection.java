package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Clear all the old selections.
 */
public class ClearSelection implements MouseRunnable {

  private GuiView view;

  public ClearSelection(GuiView view) {
    this.view = view;
  }


  @Override
  public void run(int x, int y) {
    view.clearSelections();
  }
}
