package cs3500.music.view;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;

/**
 * The panel that displays the main contents of the score.
 */
final class ConcreteGuiViewPanel extends JPanel {

  /**
   * The lowest octave in the song.
   */
  private int lowestOctave;

  /**
   * The highest octave in the song
   */
  private int highestOctave;

  /**
   * The duration of the song.
   */
  private int duration;

  /**
   * The notes in the song.
   */
  private List<MusicNote> notes;

  /**
   * The beat this song is at.
   */
  private int beat;

  /**
   * The score this panel displays.
   */
  private MusicScore score;

  /**
   * List of all the spaces on this view that are currently selected
   */
  private List<Selection> selectSpaces;

  /**
   * List of instruments used in this song listed by midi number.
   */
  private List<Integer> instruments;

  /**
   * The index in the list of instruments of the current instrument.
   */
  private int instrument;

  public ConcreteGuiViewPanel(MusicScore score) {
    initialize(score);
    this.selectSpaces = new ArrayList<>();
  }

  /**
   * Initialize information given a new score.
   *
   * @param score Score to get data from.
   */
  void initialize(MusicScore score) {
    this.score = score;
    this.notes = score.notes();
    this.lowestOctave = score.lowestOctave();
    this.highestOctave = score.highestOctave();
    this.duration = score.duration();
    this.beat = 0;
    this.instruments = score.instruments();
    this.instrument = instruments.size();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    drawGrid(g);
    drawPitches(g);
    drawBeats(g);
    drawNotes(g);
    drawBeatLine(g);
    drawSelections(g);
  }

  @Override
  public String toString() {
    Graphics g = new MockGraphics();
    drawGrid(g);
    drawPitches(g);
    drawBeats(g);
    drawNotes(g);
    drawBeatLine(g);
    drawSelections(g);
    return g.toString();
  }

  /**
   * Move this view forward by increasing the beat by one.
   */
  void tick() {
    beat++;
  }

  int getBeat() {
    return this.beat;
  }

  /**
   * Get the score being used by this panel.
   *
   * @return This panel's score
   */
  MusicScore getScore() {
    return this.score;
  }

  /**
   * Draw the line representing the beat that is currently playing.
   *
   * @param g The swing graphics object.
   */
  private void drawBeatLine(Graphics g) {
    int xCoord = (beat + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE;
    int yCoord1 = (((highestOctave - lowestOctave + 1) * 12) + GuiMusicView.PADDING / 2)
                  * GuiMusicView.PITCHSIZE;
    int yCoord2 = (GuiMusicView.PADDING / 2)
                  * GuiMusicView.PITCHSIZE;
    g.setColor(Color.RED);
    g.drawLine(xCoord, yCoord1, xCoord, yCoord2);
  }

  /**
   * Draw all the notes onto the screen.
   *
   * @param g The swing graphics object.
   */
  private void drawNotes(Graphics g) {
    Color startNoteColor = Color.BLACK;
    Color sustainNoteColor = Color.GREEN;
    for (MusicNote n : notes) {
      if (instrument == instruments.size() || n.instrument == instruments.get(instrument)) {
        g.setColor(sustainNoteColor);
        int xCoord = (n.startTime + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE;
        int yCoord = (((11 - n.pitch) + (highestOctave - n.octave) * 12)
                      + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE;
        g.fillRect(xCoord, yCoord,
            GuiMusicView.PITCHSIZE * n.duration, GuiMusicView.PITCHSIZE);
        g.setColor(startNoteColor);
        g.fillRect(xCoord, yCoord,
            GuiMusicView.PITCHSIZE, GuiMusicView.PITCHSIZE);
      }
    }
  }

  /**
   * Draw all the selected spaces onto the screen.
   *
   * @param g The swing graphics object.
   */
  private void drawSelections(Graphics g) {
    for (Selection s : selectSpaces) {
      g.setColor(Color.BLUE);
      int xCoord = (s.beat + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE;
      int yCoord = (((11 - s.pitch) + (highestOctave - s.octave) * 12)
                    + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE;
      g.fillRect(xCoord, yCoord,
          GuiMusicView.PITCHSIZE, GuiMusicView.PITCHSIZE);
    }
  }

  /**
   * Draws the grid that the notes are placed on.
   *
   * @param g Swing graphics object.
   */
  private void drawGrid(Graphics g) {
    for (int i = 0; i < (highestOctave - lowestOctave + 1) * 12; i++) {
      for (int j = 0; j < (duration / 4) + 1; j++) {
        g.drawRect((j * 4 + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE,
            (i + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE,
            4 * GuiMusicView.PITCHSIZE, GuiMusicView.PITCHSIZE);
      }
    }
  }

  /**
   * Draws a red line to denote the current beat.
   *
   * @param g Swing graphics object.
   */
  private void drawBeats(Graphics g) {
    for (int i = 0; i < (duration / 16 + 1); i++) {
      g.drawString(Integer.toString(i * 16),
          (i * 16 + GuiMusicView.PADDING / 2) * GuiMusicView.PITCHSIZE,
          GuiMusicView.PADDING / 4 * GuiMusicView.PITCHSIZE);
    }
  }

  /**
   * Draws the pitches at the side of the screen.
   *
   * @param g Swing graphics object.
   */
  private void drawPitches(Graphics g) {
    g.setFont(new Font("Helvetica", Font.PLAIN, GuiMusicView.PITCHSIZE));
    int index = lowestOctave;
    while (index <= highestOctave) {
      int y = ((highestOctave - index) * 12 + GuiMusicView.PADDING / 2)
              * GuiMusicView.PITCHSIZE;
      int xCoord = GuiMusicView.PADDING / 8 * GuiMusicView.PITCHSIZE;
      g.drawString("C " + Integer.toString(index),
          xCoord,
          y + 12 * GuiMusicView.PITCHSIZE);
      g.drawString("C#" + Integer.toString(index),
          xCoord,
          y + 11 * GuiMusicView.PITCHSIZE);
      g.drawString("D " + Integer.toString(index),
          xCoord,
          y + 10 * GuiMusicView.PITCHSIZE);
      g.drawString("D#" + Integer.toString(index),
          xCoord,
          y + 9 * GuiMusicView.PITCHSIZE);
      g.drawString("E " + Integer.toString(index),
          xCoord,
          y + 8 * GuiMusicView.PITCHSIZE);
      g.drawString("F " + Integer.toString(index),
          xCoord,
          y + 7 * GuiMusicView.PITCHSIZE);
      g.drawString("F#" + Integer.toString(index),
          xCoord,
          y + 6 * GuiMusicView.PITCHSIZE);
      g.drawString("G " + Integer.toString(index),
          xCoord,
          y + 5 * GuiMusicView.PITCHSIZE);
      g.drawString("G#" + Integer.toString(index),
          xCoord,
          y + 4 * GuiMusicView.PITCHSIZE);
      g.drawString("A " + Integer.toString(index),
          xCoord,
          y + 3 * GuiMusicView.PITCHSIZE);
      g.drawString("A#" + Integer.toString(index),
          xCoord,
          y + 2 * GuiMusicView.PITCHSIZE);
      g.drawString("B " + Integer.toString(index),
          xCoord,
          y + GuiMusicView.PITCHSIZE);
      index++;
    }
  }

  /**
   * Mock Graphics class for testing
   */
  private class MockGraphics extends Graphics {

    private StringBuilder output;

    public MockGraphics() {
      this.output = new StringBuilder();
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {

    }

    @Override
    public String toString() {
      return output.toString();
    }

    @Override
    public Graphics create() {
      return null;
    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public Color getColor() {
      return null;
    }

    @Override
    public void setColor(Color c) {
      output.append("setting color to: ");
      output.append(c.toString());
      output.append('\n');
    }

    @Override
    public void setPaintMode() {

    }

    @Override
    public void setXORMode(Color c1) {

    }

    @Override
    public Font getFont() {
      return null;
    }

    @Override
    public void setFont(Font font) {
      output.append("Setting font to: ");
      output.append(font.toString());
      output.append('\n');
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
      return null;
    }

    @Override
    public Rectangle getClipBounds() {
      return null;
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {

    }

    @Override
    public void setClip(int x, int y, int width, int height) {

    }

    @Override
    public Shape getClip() {
      return null;
    }

    @Override
    public void setClip(Shape clip) {

    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
      output.append("Drawing line from: ");
      output.append(x1);
      output.append(",");
      output.append(y1);
      output.append(" to ");
      output.append(x2);
      output.append(",");
      output.append(y2);
      output.append('\n');
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
      output.append("Making rect at point: ");
      output.append(x);
      output.append(", ");
      output.append(y);
      output.append(" with dimensions: ");
      output.append(width);
      output.append(" by ");
      output.append(height);
      output.append('\n');
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {

    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void drawOval(int x, int y, int width, int height) {

    }

    @Override
    public void fillOval(int x, int y, int width, int height) {

    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void drawString(String str, int x, int y) {
      output.append("Drawing string: ");
      output.append(str);
      output.append(" at: ");
      output.append(x);
      output.append(", ");
      output.append(y);
      output.append('\n');
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {

    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
      return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver
        observer) {
      return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
      return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor,
                             ImageObserver observer) {
      return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int
        sx2, int sy2, ImageObserver observer) {
      return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int
        sx2, int sy2, Color bgcolor, ImageObserver observer) {
      return false;
    }

    @Override
    public void dispose() {

    }
  }

  @Override
  public Dimension getPreferredSize() {
    int numOctave = highestOctave - lowestOctave + 1;
    int height = numOctave * 12 * GuiMusicView.PITCHSIZE + GuiMusicView.PADDING * GuiMusicView
        .PITCHSIZE;
    int width = (duration + GuiMusicView.PADDING) * GuiMusicView.PITCHSIZE;
    return new Dimension(width, height);
  }

  /**
   * Get the selections of this panel.
   *
   * @return list of selections.
   */
  List<Selection> getSelections() {
    return this.selectSpaces;
  }

  /**
   * Add a selection to this panel based on the clicked screen position.
   *
   * @param x x coordinate of mouse.
   * @param y y coordinate of mouse.
   */
  void addSelection(int x, int y) {
    int beat = x / GuiMusicView.PITCHSIZE - GuiMusicView.PADDING / 2;
    int octave = -1 * (((y / GuiMusicView.PITCHSIZE - GuiMusicView.PADDING / 2) / 12) -
                       highestOctave);
    int pitch = -1 * (((y / GuiMusicView.PITCHSIZE - GuiMusicView.PADDING / 2)
                       - (highestOctave - octave) * 12) - 11);
    while (pitch >= 12) {
      pitch -= 12;
      octave += 1;
    }
    boolean repeated = false;
    for (Selection s : selectSpaces) {
      if (s.pitch == pitch && s.octave == octave && s.beat == beat) {
        repeated = true;
      }
    }
    if (!repeated && beat >= 0 && octave >= 0 && octave <= 9) {
      selectSpaces.add(new Selection(pitch, octave, beat));
    }
  }

  /**
   * Clear the selections of this panel.
   */
  void clearSelections() {
    this.selectSpaces.clear();
  }

  /**
   * Set the beat number of this panel.
   *
   * @param beat the new beat number.
   */
  void setTime(int beat) {
    if (beat > this.score.duration() + 1 || beat < 0) {
      throw new IllegalArgumentException();
    }
    this.beat = beat;
  }

  /**
   * Get the current instrument being displayed. If all the instruments are being displayed then
   * return the first instrument.
   *
   * @return the midi number of the current instrument.
   */
  int getInstrument() {
    if (instruments.size() == 0) {
      return -1;
    } else if (instrument == instruments.size()) {
      return -2;
    }
    return instruments.get(this.instrument);
  }

  /**
   * Cycle through the current list of instruments. Once at the end of the list display all the
   * instruments.
   */
  void changeInstrument() {
    this.instrument = (1 + this.instrument) % (instruments.size() + 1);
  }
}
