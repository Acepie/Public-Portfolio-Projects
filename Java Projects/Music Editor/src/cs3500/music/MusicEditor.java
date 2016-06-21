package cs3500.music;

import cs3500.music.controller.MusicController;
import cs3500.music.model.MusicScoreImpl;
import cs3500.music.view.GuiView;
import cs3500.music.view.MusicView;
import cs3500.music.view.ViewFactory;


public class MusicEditor {

  public static void main(String[] args) throws Exception {
    MusicView view;
    if (args.length == 1) {
      String viewType = args[0];
      view = ViewFactory.makeView(viewType, new MusicScoreImpl.Builder().build());
    } else if (args.length == 2) {
      String fileName = args[0];
      String viewType = args[1];
      view = ViewFactory.makeView(viewType, fileName, new MusicScoreImpl.Builder());
    } else {
      throw new IllegalArgumentException();
    }
    MusicController player = new MusicController((GuiView) view);
    while (true) {
      player.play();
    }
  }
}
