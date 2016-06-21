package cs3500.music.controller.events;

/**
 * Runnable abstract interface for mouse events.
 */
public interface MouseRunnable {

  /**
   * Given the x and y coordinates of a mouse event do something.
   *
   * @param x The x coordinate of the mouse.
   * @param y The y coordinate of the mouse.
   */
  public abstract void run(int x, int y);
}
