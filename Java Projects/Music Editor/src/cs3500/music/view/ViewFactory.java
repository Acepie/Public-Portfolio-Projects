package cs3500.music.view;

import java.io.FileReader;

import cs3500.music.model.MusicScore;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.MusicReader;

/**
 * Factory class for making views
 */
public final class ViewFactory {

  public static MusicView makeView(String viewType, String fileName, 
  	CompositionBuilder<MusicScore> builder) throws Exception {
    MusicScore score = MusicReader.parseFile(new FileReader(fileName), builder);
    return makeView(viewType, score);
  }

  public static MusicView makeView(String viewType, MusicScore score) throws Exception {
    switch (viewType.toLowerCase()) {
      case "console-test":
        return new ConsoleViewImpl(new StringBuilder(), score);
      case "console":
        return new ConsoleViewImpl(score);
      case "visual":
        return new GuiMusicView(score);
      case "midi":
        return new MidiViewImpl(score, false);
      case "midi-test":
        return new MidiViewImpl(score, true);
      case "composite":
        return new CompositeView(score);
      default:
        throw new IllegalArgumentException("not a valid view");
    }
  }

  public static GuiView makeGuiView(String viewType, MusicScore score) throws Exception {
    switch (viewType.toLowerCase()) {
      case "visual":
        return new GuiMusicView(score);
      case "composite":
        return new CompositeView(score);
      default:
        throw new IllegalArgumentException("not a valid view");
    }
  }
}
