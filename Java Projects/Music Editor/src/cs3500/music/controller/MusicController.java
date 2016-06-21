package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import cs3500.music.controller.events.AddNote;
import cs3500.music.controller.events.BackNote;
import cs3500.music.controller.events.ChangeInstrument;
import cs3500.music.controller.events.Clear;
import cs3500.music.controller.events.ClearSelection;
import cs3500.music.controller.events.DecreaseDuration;
import cs3500.music.controller.events.DownNote;
import cs3500.music.controller.events.ForwardNotes;
import cs3500.music.controller.events.IncreaseDuration;
import cs3500.music.controller.events.MoveBackward;
import cs3500.music.controller.events.MoveForward;
import cs3500.music.controller.events.MultiSelectSpace;
import cs3500.music.controller.events.RemoveNote;
import cs3500.music.controller.events.ScrollBack;
import cs3500.music.controller.events.ScrollEnd;
import cs3500.music.controller.events.SelectSpace;
import cs3500.music.controller.events.TogglePlaying;
import cs3500.music.controller.events.UpNote;
import cs3500.music.model.MusicScore;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewFactory;

/**
 * Controller for Visual Music Views
 */
public class MusicController implements Controller {

  /**
   * Keyboard handler for this music controller.
   */
  private KeyboardHandler keyHandler;

  /**
   * Mouse handler for this music controller.
   */
  private MouseHandler mouseHandler;

  /**
   * The view this controller controls.
   */
  private GuiView view;

  /**
   * Timer that allows for ticking to occur
   */
  private Timer timer;

  public MusicController(GuiView view) {
    this.timer = new Timer(false);
    this.view = view;
    this.keyHandler = new KeyboardHandler();
    this.mouseHandler = new MouseHandler();
    this.addKeyEvents();
    this.addMouseEvents();
    this.view.addKeyListener(keyHandler);
    this.view.addMouseListener(mouseHandler);
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          view.tick();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, 0, view.tempo() / 1000);
    this.view.display();
  }

  public MusicController(MusicScore score) throws Exception {
    this((GuiView) ViewFactory.makeView("composite", score));
  }

  /**
   * Add all the key events for this controller
   */
  private void addKeyEvents() {
    //Toggles playing when space is pressed.
    keyHandler.addReleasedRunnable(KeyEvent.VK_SPACE, new TogglePlaying(view));
    //Set the time of this view to 0 and scroll to the beginning
    keyHandler.addReleasedRunnable(KeyEvent.VK_HOME, new ScrollBack(view));
    //Set the time of this view to the end and scroll to the end
    keyHandler.addReleasedRunnable(KeyEvent.VK_END, new ScrollEnd(view));
    //Add a note at selected spaces if possible
    keyHandler.addReleasedRunnable(KeyEvent.VK_I, new AddNote(view));
    //Remove notes at selected spaces if possible
    keyHandler.addReleasedRunnable(KeyEvent.VK_R, new RemoveNote(view));
    //Cycle through the instrument views
    keyHandler.addReleasedRunnable(KeyEvent.VK_C, new ChangeInstrument(view));
    //Increase the duration of selected notes by 1
    keyHandler.addReleasedRunnable(KeyEvent.VK_P, new IncreaseDuration(view));
    //Decrease the duration of selected notes by 1
    keyHandler.addReleasedRunnable(KeyEvent.VK_O, new DecreaseDuration(view));
    //Move the selected notes forward 1 beat if possible
    keyHandler.addReleasedRunnable(KeyEvent.VK_L, new ForwardNotes(view));
    //Move the selected notes backward 1 beat if possible
    keyHandler.addReleasedRunnable(KeyEvent.VK_K, new BackNote(view));
    //Move the selected notes up 1 pitch if possible
    keyHandler.addReleasedRunnable(KeyEvent.VK_M, new UpNote(view));
    //Move the selected notes down 1 pitch if possible
    keyHandler.addReleasedRunnable(KeyEvent.VK_N, new DownNote(view));
    //Clear the current score with escape
    keyHandler.addReleasedRunnable(KeyEvent.VK_ESCAPE, new Clear(view));
    //Move our view forward 1 beat
    keyHandler.addReleasedRunnable(KeyEvent.VK_RIGHT, new MoveForward(view));
    //Move our view backward 1 beat
    keyHandler.addReleasedRunnable(KeyEvent.VK_LEFT, new MoveBackward(view));
  }

  /**
   * Add all the mouse events for this controller
   */
  private void addMouseEvents() {
    //Clear the old selections and select a new space on this view with left click
    mouseHandler.addMousePressed(MouseEvent.BUTTON1, new SelectSpace(view));
    //Select a new space on this view with middle click
    mouseHandler.addMousePressed(MouseEvent.BUTTON2, new MultiSelectSpace(view));
    //Clear all the old selections on this view with right click
    mouseHandler.addMousePressed(MouseEvent.BUTTON3, new ClearSelection(view));
  }

  /**
   * Run this application.
   */
  public void play() throws Exception {
  }
}
