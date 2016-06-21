package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;
import cs3500.music.model.MusicScoreImpl;
import cs3500.music.util.MusicReader;
import cs3500.music.view.GuiView;
import cs3500.music.view.MusicView;
import cs3500.music.view.ViewFactory;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Music Views.
 */
public class MusicViewTest {

  MusicView midiView;
  MusicView mtMidi;
  MusicView consoleView;
  MusicView mtConsole;
  MusicView guiView;
  MusicView mtGui;
  GuiView testGuiView;
  MusicNote.NoteBuilder builder;
  MusicNote c411;
  MusicNote c412;

  @Before
  public void setUp() throws Exception {
    consoleView = ViewFactory.makeView("console-test", "mary-little-lamb.txt", new MusicScoreImpl.Builder());
    guiView = ViewFactory.makeView("visual", "mary-little-lamb.txt", new MusicScoreImpl.Builder());
    midiView = ViewFactory.makeView("midi-test", "mary-little-lamb.txt", new MusicScoreImpl.Builder());
    mtConsole = ViewFactory.makeView("console-test", new MusicScoreImpl.Builder().build());
    mtGui = ViewFactory.makeView("visual", new MusicScoreImpl.Builder().build());
    mtMidi = ViewFactory.makeView("midi-test", new MusicScoreImpl.Builder().build());
    testGuiView = (GuiView) ViewFactory.makeView("visual", new MusicScoreImpl.Builder().build());
    builder = new MusicNote.NoteBuilder();
    c411 = builder.build();
    c412 = builder.duration(4).build();
  }

  @Test
  public void testGuiViewModification() throws Exception {
    testGuiView.playSong();
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeAdd(c411);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeAdd(c412);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeRemove(1, 4, 1, 1);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeRemove(0, 4, 1, 0);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeRemove(0, 4, 1, 1);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeAdd(c412);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 40 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
    testGuiView.safeReplace(0, 4, 1, 1, c411);
    assertEquals(testGuiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 50, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
  }

  @Test
  public void testPlaySongConsole() throws Exception {
    consoleView.playSong();
    assertEquals(consoleView.displayLogData(),
        "   C2 C#2D2 D#2E2 F2 F#2G2 G#2A2 A#2B2 C3 C#3D3 D#3E3 F3 F#3G3 G#3A3 A#3B3 \n"
        + "00                      X                          X                       \n"
        + "01                      |                          |                       \n"
        + "02                      |                    X                             \n"
        + "03                      |                    |                             \n"
        + "04                      |              X                                   \n"
        + "05                      |              |                                   \n"
        + "06                      |                    X                             \n"
        + "07                                           |                             \n"
        + "08                      X                          X                       \n"
        + "09                      |                          |                       \n"
        + "10                      |                          X                       \n"
        + "11                      |                          |                       \n"
        + "12                      |                          X                       \n"
        + "13                      |                          |                       \n"
        + "14                      |                          |                       \n"
        + "15                                                                         \n"
        + "16                      X                    X                             \n"
        + "17                      |                    |                             \n"
        + "18                      |                    X                             \n"
        + "19                      |                    |                             \n"
        + "20                      |                    X                             \n"
        + "21                      |                    |                             \n"
        + "22                      |                    |                             \n"
        + "23                      |                    |                             \n"
        + "24                      X                          X                       \n"
        + "25                      |                          |                       \n"
        + "26                                                          X              \n"
        + "27                                                          |              \n"
        + "28                                                          X              \n"
        + "29                                                          |              \n"
        + "30                                                          |              \n"
        + "31                                                          |              \n"
        + "32                      X                          X                       \n"
        + "33                      |                          |                       \n"
        + "34                      |                    X                             \n"
        + "35                      |                    |                             \n"
        + "36                      |              X                                   \n"
        + "37                      |              |                                   \n"
        + "38                      |                    X                             \n"
        + "39                      |                    |                             \n"
        + "40                      X                          X                       \n"
        + "41                      |                          |                       \n"
        + "42                      |                          X                       \n"
        + "43                      |                          |                       \n"
        + "44                      |                          X                       \n"
        + "45                      |                          |                       \n"
        + "46                      |                          X                       \n"
        + "47                      |                          |                       \n"
        + "48                      X                    X                             \n"
        + "49                      |                    |                             \n"
        + "50                      |                    X                             \n"
        + "51                      |                    |                             \n"
        + "52                      |                          X                       \n"
        + "53                      |                          |                       \n"
        + "54                      |                    X                             \n"
        + "55                      |                    |                             \n"
        + "56             X                       X                                   \n"
        + "57             |                       |                                   \n"
        + "58             |                       |                                   \n"
        + "59             |                       |                                   \n"
        + "60             |                       |                                   \n"
        + "61             |                       |                                   \n"
        + "62             |                       |                                   \n"
        + "63             |                       |                                   \n");
  }

  @Test
  public void testPlaySongGui() throws Exception {
    assertEquals(guiView.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 2 at: 10, 280\n" +
        "Drawing string: C#2 at: 10, 270\n" +
        "Drawing string: D 2 at: 10, 260\n" +
        "Drawing string: D#2 at: 10, 250\n" +
        "Drawing string: E 2 at: 10, 240\n" +
        "Drawing string: F 2 at: 10, 230\n" +
        "Drawing string: F#2 at: 10, 220\n" +
        "Drawing string: G 2 at: 10, 210\n" +
        "Drawing string: G#2 at: 10, 200\n" +
        "Drawing string: A 2 at: 10, 190\n" +
        "Drawing string: A#2 at: 10, 180\n" +
        "Drawing string: B 2 at: 10, 170\n" +
        "Drawing string: C 3 at: 10, 160\n" +
        "Drawing string: C#3 at: 10, 150\n" +
        "Drawing string: D 3 at: 10, 140\n" +
        "Drawing string: D#3 at: 10, 130\n" +
        "Drawing string: E 3 at: 10, 120\n" +
        "Drawing string: F 3 at: 10, 110\n" +
        "Drawing string: F#3 at: 10, 100\n" +
        "Drawing string: G 3 at: 10, 90\n" +
        "Drawing string: G#3 at: 10, 80\n" +
        "Drawing string: A 3 at: 10, 70\n" +
        "Drawing string: A#3 at: 10, 60\n" +
        "Drawing string: B 3 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "Drawing string: 16 at: 200, 20\n" +
        "Drawing string: 32 at: 360, 20\n" +
        "Drawing string: 48 at: 520, 20\n" +
        "Drawing string: 64 at: 680, 20\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 40, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 40, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 40, 200 with dimensions: 80 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 40, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 60, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 60, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 80, 150 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 80, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 100, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 100, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 120, 200 with dimensions: 80 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 120, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 120, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 120, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 140, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 140, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 160, 110 with dimensions: 40 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 160, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 200, 200 with dimensions: 90 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 200, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 200, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 200, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 220, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 220, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 240, 130 with dimensions: 50 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 240, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 280, 200 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 280, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 280, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 280, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 300, 80 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 300, 80 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 320, 80 with dimensions: 50 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 320, 80 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 360, 200 with dimensions: 90 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 360, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 360, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 360, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 380, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 380, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 400, 150 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 400, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 420, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 420, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 440, 200 with dimensions: 90 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 440, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 440, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 440, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 460, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 460, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 480, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 480, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 500, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 500, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 520, 200 with dimensions: 90 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 520, 200 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 520, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 520, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 540, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 540, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 560, 110 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 560, 110 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 580, 130 with dimensions: 30 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 580, 130 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 600, 230 with dimensions: 90 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 600, 230 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=255,b=0]\n" +
        "Making rect at point: 600, 150 with dimensions: 90 by 10\n" +
        "setting color to: java.awt.Color[r=0,g=0,b=0]\n" +
        "Making rect at point: 600, 150 with dimensions: 10 by 10\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,280 to 40,40\n");
  }

  @Test
  public void testPlaySongMidi() throws Exception {
    assertEquals(midiView.displayLogData(),
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 1.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:0 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 1.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.8 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 1.8 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 1.0 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:7 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:7 and  octave:3 for 1.0 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 1.8 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:0 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 1.8 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:7 and  octave:2 for 1.8 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:2 and  octave:3 for 0.6 seconds \n" +
        "Playing note of pitch:4 and  octave:2 for 1.8 seconds \n" +
        "Playing note of pitch:0 and  octave:3 for 1.8 seconds \n");
  }

  @Test
  public void testPlaySongConsole2() throws Exception {
    mtConsole.playSong();
    assertEquals(mtConsole.displayLogData(),
        "");
  }

  @Test
  public void testPlaySongGui2() throws Exception {
    assertEquals(mtGui.displayLogData(),
        "Setting font to: java.awt.Font[family=Dialog,name=Helvetica,style=plain,size=10]\n" +
        "Drawing string: C 4 at: 10, 160\n" +
        "Drawing string: C#4 at: 10, 150\n" +
        "Drawing string: D 4 at: 10, 140\n" +
        "Drawing string: D#4 at: 10, 130\n" +
        "Drawing string: E 4 at: 10, 120\n" +
        "Drawing string: F 4 at: 10, 110\n" +
        "Drawing string: F#4 at: 10, 100\n" +
        "Drawing string: G 4 at: 10, 90\n" +
        "Drawing string: G#4 at: 10, 80\n" +
        "Drawing string: A 4 at: 10, 70\n" +
        "Drawing string: A#4 at: 10, 60\n" +
        "Drawing string: B 4 at: 10, 50\n" +
        "Drawing string: 0 at: 40, 20\n" +
        "setting color to: java.awt.Color[r=255,g=0,b=0]\n" +
        "Drawing line from: 40,160 to 40,40\n");
  }

  @Test
  public void testPlaySongMidi2() throws Exception {
    assertEquals(mtMidi.displayLogData(),
        "");
  }

  @Test
  public void testLoadScore() throws Exception {
    MusicScore newScore = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"),
        new MusicScoreImpl.Builder());
    mtConsole.loadScore(newScore);
    mtGui.loadScore(newScore);
    mtMidi.loadScore(newScore);
    mtConsole.playSong();
    consoleView.playSong();
    assertEquals(mtConsole.displayLogData(), consoleView.displayLogData());
    assertEquals(mtGui.displayLogData(), guiView.displayLogData());
    assertEquals(mtMidi.displayLogData(), midiView.displayLogData());
  }
}