package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;
import java.util.Map;

/**
 * Handles Keyboard Events
 */
public final class KeyboardHandler implements KeyListener {

  private Map<Integer, Runnable> keyTypedEvents;

  private Map<Integer, Runnable> keyPressedEvents;

  private Map<Integer, Runnable> keyReleasedEvents;

  public KeyboardHandler() {
    this.keyTypedEvents = new Hashtable<>();
    this.keyPressedEvents = new Hashtable<>();
    this.keyReleasedEvents = new Hashtable<>();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    int code = e.getKeyCode();
    if (keyTypedEvents.containsKey(code)) {
      keyTypedEvents.get(code).run();
    }
  }


  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (keyPressedEvents.containsKey(code)) {
      keyPressedEvents.get(code).run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    if (keyReleasedEvents.containsKey(code)) {
      keyReleasedEvents.get(code).run();
    }
  }

  /**
   * Add a new callback to be run when a key is typed.
   *
   * @param key    Key that is typed.
   * @param runner Event to run.
   */
  public void addTypedRunnable(Integer key, Runnable runner) {
    if (!keyTypedEvents.containsKey(key)) {
      keyTypedEvents.put(key, runner);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Add a new callback to be run when a key is pressed.
   *
   * @param key    Key that is typed.
   * @param runner Event to run.
   */
  public void addPressedRunnable(Integer key, Runnable runner) {
    if (!keyPressedEvents.containsKey(key)) {
      keyPressedEvents.put(key, runner);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Add a new callback to be run when a key is released.
   *
   * @param key    Key that is typed.
   * @param runner Event to run.
   */
  public void addReleasedRunnable(Integer key, Runnable runner) {
    if (!keyReleasedEvents.containsKey(key)) {
      keyReleasedEvents.put(key, runner);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Typed events: \n");
    for (Integer key : keyTypedEvents.keySet()) {
      sb.append("key ");
      sb.append(key);
      sb.append(": ");
      sb.append(keyTypedEvents.get(key).toString());
      sb.append('\n');
    }
    sb.append("Pressed events: \n");
    for (Integer key : keyPressedEvents.keySet()) {
      sb.append("key ");
      sb.append(key);
      sb.append(": ");
      sb.append(keyPressedEvents.get(key).toString());
      sb.append('\n');
    }
    sb.append("Released events: \n");
    for (Integer key : keyReleasedEvents.keySet()) {
      sb.append("key ");
      sb.append(key);
      sb.append(": ");
      sb.append(keyReleasedEvents.get(key).toString());
      sb.append('\n');
    }
    return sb.toString();
  }
}
