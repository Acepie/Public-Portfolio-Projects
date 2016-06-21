package cs3500.music.tests;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.music.model.MusicNote;
import cs3500.music.model.MusicScore;
import cs3500.music.model.MusicScoreHashmapImpl;
import cs3500.music.model.MusicScoreImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MusicScoreTest {

  MusicNote.NoteBuilder builder;
  MusicNote c411;
  MusicNote c412;
  MusicNote c421;
  MusicNote c415;
  MusicNote c445;
  MusicNote c31;
  MusicNote b41;
  MusicNote e41;
  MusicNote g41;
  MusicNote c417;
  MusicNote c427;
  MusicNote c546;
  MusicScore score1;
  MusicScore score2;
  MusicScore score3;
  MusicScore score4;
  MusicScore score5;

  @Before
  public void setUp() {
    builder = new MusicNote.NoteBuilder();
    c411 = builder.build();
    c412 = builder.startTime(2).build();
    c421 = builder.duration(2).build();
    c415 = builder.startTime(5).build();
    c445 = builder.startTime(5).duration(4).build();
    c31 = builder.octave(3).build();
    b41 = builder.pitch(11).build();
    e41 = builder.pitch(4).startTime(3).build();
    g41 = builder.pitch(7).startTime(5).build();
    c417 = builder.startTime(7).build();
    c427 = builder.startTime(7).duration(2).build();
    c546 = builder.octave(5).startTime(6).duration(4).build();
    score1 = new MusicScoreHashmapImpl.Builder().build();
    score2 = new MusicScoreHashmapImpl.Builder().build();
    score3 = new MusicScoreHashmapImpl.Builder().build();
    score4 = new MusicScoreHashmapImpl.Builder().build();
    score5 = new MusicScoreHashmapImpl.Builder().build();
    score1.addNote(e41, c411, g41, c415);
    score2.addNote(c421, e41);
    score3.addNote(g41, c417);
    score5.addNote(c546, c31, b41);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoteConstructor1() {
    MusicNote bad = builder.pitch(-1).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoteConstructor2() {
    MusicNote bad = builder.pitch(12).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoteConstructor3() {
    MusicNote bad = builder.octave(-1).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoteConstructor4() {
    MusicNote bad = builder.octave(10).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoteConstructor5() {
    MusicNote bad = builder.startTime(-1).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoteConstructor6() {
    MusicNote bad = builder.duration(0).build();
  }

  @Test
  public void testNoteEndTime() {
    assertEquals(1, c411.endTime());
    assertEquals(2, c421.endTime());
    assertEquals(5, c415.endTime());
    assertEquals(8, c445.endTime());
  }

  @Test
  public void testConstruction() {
    MusicScore emptyScore = new MusicScoreImpl.Builder().build();
    assertEquals(emptyScore.notes().size(), 0);
  }

  @Test
  public void testAddNote1() {
    assertEquals(score1.notes().size(), 4); //assert right number of notes are in
    assertEquals(score1.notes().get(0), c411); //assert sort is accurate
    assertEquals(score1.notes().get(1), e41);
    assertTrue(score1.containsNote(4, 4, 3, 1));
    assertTrue(score1.containsNote(7, 4, 5, 1));
    assertTrue(score1.containsNote(0, 4, 5, 1));
    score1.addNote(c546);
    assertEquals(score1.notes().size(), 5);
    assertEquals(score1.notes().get(4), c546); //adds into correct spot
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNote2() {
    MusicScore badscore = new MusicScoreImpl.Builder().build();
    badscore.addNote(c411, c421);
  }

  @Test
  public void testRemoveNote1() {
    score1.removeNote(c411);
    assertEquals(score1.notes().size(), 3);
    assertFalse(score1.containsNote(0, 4, 1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNote2() {
    score1.removeNote(c31);
  }

  @Test
  public void testReplaceNote1() {
    score1.replaceNote(c411, b41);
    assertFalse(score1.containsNote(0, 4, 1, 1));
    assertTrue(score1.containsNote(11, 4, 1, 1));
    score1.replaceNote(b41, c417);
    assertFalse(score1.containsNote(11, 4, 1, 1));
    assertTrue(score1.containsNote(0, 4, 7, 1));
    assertEquals(score1.notes().get(3), c417); //ensure replacement does not destroy sorting
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReplaceNote2() {
    score1.replaceNote(c31, b41);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReplaceNote3() {
    score1.replaceNote(c411, e41);
  }

  @Test
  public void testComposeScores() {
    MusicScore score23 = score2.composeScores(score3);
    assertEquals(score23.notes().size(), 4);
    assertTrue(score23.containsNote(0, 4, 1, 1));
    assertTrue(score23.containsNote(4, 4, 3, 1));
    assertTrue(score23.containsNote(7, 4, 5, 1));
    assertTrue(score23.containsNote(0, 4, 7, 1));
    MusicScore score13 = score1.composeScores(score3);
    assertEquals(score13.notes().size(), 5);
    assertTrue(score13.containsNote(0, 4, 1, 1));
    assertTrue(score13.containsNote(4, 4, 3, 1));
    assertTrue(score13.containsNote(7, 4, 5, 1));
    assertTrue(score13.containsNote(0, 4, 5, 1));
    assertTrue(score13.containsNote(0, 4, 7, 1));
    MusicScore score12 = score1.composeScores(score2);

    assertEquals(score12.notes().size(), 4);
    assertTrue(score12.containsNote(0, 4, 1, 1));
    assertTrue(score12.containsNote(4, 4, 3, 1));
    assertTrue(score12.containsNote(7, 4, 5, 1));
    assertTrue(score12.containsNote(0, 4, 5, 1));
    assertEquals(score12.notes().get(0).duration, 2);
  }

  @Test
  public void testConsecutiveScore() {
    MusicScore score12 = score1.consecutiveScore(score2);
    assertEquals(score12.notes().size(), 6);
    assertEquals(score12.duration(), score1.duration() + score2.duration());
    assertEquals(score12.notes().get(0), c411);
    assertEquals(score12.notes().get(1), e41);
    assertEquals(score12.notes().get(2), g41);
    assertEquals(score12.notes().get(3), c415);
    assertEquals(score12.notes().get(4).pitch, c421.pitch);
    assertEquals(score12.notes().get(4).octave, c421.octave);
    assertEquals(score12.notes().get(4).duration, c421.duration);
    assertEquals(score12.notes().get(4).startTime, c421.startTime + score1.duration());
  }

  @Test
  public void testDuration() {
    MusicScore score4 = new MusicScoreImpl.Builder().build();
    score4.addNote(c427, c411);
    assertEquals(score1.duration(), 5);
    assertEquals(score2.duration(), 3);
    assertEquals(score4.duration(), 8);
    score4.addNote(c546);
    assertEquals(score4.duration(), 9);
  }

  @Test
  public void testNotes() {
    assertEquals(score1.notes().size(), 4);
    assertEquals(score1.notes().get(0), c411); //assert sort is accurate
    assertEquals(score1.notes().get(1), e41);
    assertTrue(score1.containsNote(4, 4, 3, 1));
    assertTrue(score1.containsNote(7, 4, 5, 1));
    assertTrue(score1.containsNote(0, 4, 5, 1));
  }

  @Test
  public void testHighestOctave() {
    assertEquals(score4.highestOctave(), 4);
    assertEquals(score5.highestOctave(), 5);
    assertEquals(score1.highestOctave(), 4);
  }

  @Test
  public void testLowestOctave() {
    assertEquals(score4.lowestOctave(), 4);
    assertEquals(score5.lowestOctave(), 3);
    assertEquals(score1.lowestOctave(), 4);
  }

  @Test
  public void testContainsNote() {
    assertTrue(score1.containsNote(0, 4, 1, 1));
    assertFalse(score1.containsNote(11, 4, 1, 1));
  }

  @Test
  public void testGetNote1() {
    assertEquals(score1.getNote(0, 4, 1, 1), c411);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNote2() {
    assertEquals(score1.getNote(11, 4, 1, 1), c411);
  }

  @Test
  public void testPlayingAt() {
    List<MusicNote> notes1 = score1.notesAtTime(5);
    assertFalse(notes1.contains(c411));
    assertFalse(notes1.contains(e41));
    assertTrue(notes1.contains(g41));
    assertTrue(notes1.contains(c415));
  }

  @Test
  public void testStartingAt() {
    MusicNote playingNotStarting = builder.pitch(11).startTime(4).duration(2).build();
    score1.addNote(playingNotStarting);
    List<MusicNote> notes1 = score1.notesStartingAtTime(5);
    assertFalse(notes1.contains(c411));
    assertFalse(notes1.contains(e41));
    assertTrue(notes1.contains(g41));
    assertTrue(notes1.contains(c415));
    assertFalse(notes1.contains(playingNotStarting));
  }
}