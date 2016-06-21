package cs3500.music.controller.events;

import cs3500.music.view.GuiView;

/**
 * Change the instrument being displayed by a given view.
 */
public class ChangeInstrument implements Runnable {

  private GuiView view;

  public ChangeInstrument(GuiView view) {
    this.view = view;
  }

  @Override
  public void run() {
    view.changeInstrument();
  }
}
