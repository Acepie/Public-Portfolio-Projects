package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Map;

import cs3500.music.controller.events.MouseRunnable;
import cs3500.music.controller.events.NoRunMouseRunnable;

/**
 * Handles Mouse Events
 */
public final class MouseHandler implements MouseListener {

  private Map<Integer, MouseRunnable> clicked;
  private Map<Integer, MouseRunnable> pressed;
  private Map<Integer, MouseRunnable> released;
  private MouseRunnable entered;
  private MouseRunnable exited;

  public MouseHandler() {
    this.clicked = new Hashtable<>();
    this.pressed = new Hashtable<>();
    this.released = new Hashtable<>();
    this.entered = new NoRunMouseRunnable();
    this.exited = new NoRunMouseRunnable();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int button = e.getButton();
    if (clicked.containsKey(button)) {
      clicked.get(button).run(e.getX(), e.getY());
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    int button = e.getButton();
    if (pressed.containsKey(button)) {
      pressed.get(button).run(e.getX(), e.getY());
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int button = e.getButton();
    if (released.containsKey(button)) {
      released.get(button).run(e.getX(), e.getY());
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    entered.run(e.getX(), e.getY());
  }

  @Override
  public void mouseExited(MouseEvent e) {
    exited.run(e.getX(), e.getY());
  }

  /**
   * Add a mouse event for when a certain button is clicked.
   *
   * @param button The button activated.
   * @param event  The event to be run.
   */
  public void addMouseClicked(int button, MouseRunnable event) {
    if (!clicked.containsKey(button)) {
      clicked.put(button, event);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Add a mouse event for when a certain button is pressed.
   *
   * @param button The button activated.
   * @param event  The event to be run.
   */
  public void addMousePressed(int button, MouseRunnable event) {
    if (!pressed.containsKey(button)) {
      pressed.put(button, event);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Add a mouse event for when a certain button is released.
   *
   * @param button The button activated.
   * @param event  The event to be run.
   */
  public void addMouseReleased(int button, MouseRunnable event) {
    if (!released.containsKey(button)) {
      released.put(button, event);
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Add a mouse event for when a certain button enters the screen.
   *
   * @param event The event to be run.
   */
  public void addMouseEntered(MouseRunnable event) {
    this.entered = event;
  }

  /**
   * Add a mouse event for when a certain button exits the screen
   *
   * @param event The event to be run.
   */
  public void addMouseExited(MouseRunnable event) {
    this.exited = event;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Clicked events: \n");
    for (Integer key : pressed.keySet()) {
      sb.append("key ");
      sb.append(key);
      sb.append(": ");
      sb.append(pressed.get(key).toString());
      sb.append('\n');
    }
    for (Integer key : released.keySet()) {
      sb.append("key ");
      sb.append(key);
      sb.append(": ");
      sb.append(released.get(key).toString());
      sb.append('\n');
    }
    for (Integer key : clicked.keySet()) {
      sb.append("key ");
      sb.append(key);
      sb.append(": ");
      sb.append(clicked.get(key).toString());
      sb.append('\n');
    }
    return sb.toString();
  }
}
