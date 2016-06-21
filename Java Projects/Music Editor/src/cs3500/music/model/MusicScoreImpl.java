package cs3500.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cs3500.music.util.CompositionBuilder;

/**
 * Implementation of Music Score.
 */
public final class MusicScoreImpl implements MusicScore {

  /**
   * Notes in the song. List is sorted by start time.
   */
  private List<MusicNote> notes;

  /**
   * Speed of the song in beats per minute.
   */
  private final int tempo;

  /**
   * Builder for MusicScoreImpl
   */
  public static final class Builder implements CompositionBuilder<MusicScore> {

    private int tempo;

    private List<MusicNote> notes;

    private MusicNote.NoteBuilder noteBuilder;

    public Builder() {
      this.tempo = 100000;
      this.notes = new ArrayList<MusicNote>();
      this.noteBuilder = new MusicNote.NoteBuilder();
    }

    @Override
    public MusicScore build() {
      return new MusicScoreImpl(this.notes, this.tempo);
    }

    @Override
    public CompositionBuilder<MusicScore> setTempo(int tempo) {
      this.tempo = tempo;
      return this;
    }

    @Override
    public CompositionBuilder<MusicScore> addNote(int start, int end, int instrument, int pitch,
                                                  int volume) {
      MusicNote note = noteBuilder.pitch(pitch % 12).octave(pitch / 12 - 2).startTime(start)
          .instrument(instrument - 1).duration(end - start + 1).volume(volume).build();
      this.notes.add(note);
      return this;
    }
  }

  /**
   * Construct this song with a set of notes and a speed.
   *
   * @param notes Notes to be used in score.
   * @param tempo The speed of the song in beats per minute.
   */
  private MusicScoreImpl(List<MusicNote> notes, int tempo) {
    Objects.requireNonNull(notes).sort(new TimeComparator());
    this.notes = notes;
    this.tempo = tempo;
  }

  @Override
  // should switch to binary insert later
  public void addNote(MusicNote... notes) {
    for (MusicNote note : notes) {
      for (int i = note.startTime; i <= note.endTime(); i++) {
        if (this.notePlayingAt(note.pitch, note.octave, i, note.instrument)) {
          throw new IllegalArgumentException("note already playing here");
        }
      }
      boolean added = false;
      for (int i = 0; i < this.notes.size() && !added; i++) {
        MusicNote n = this.notes.get(i);
        if (n.startTime > note.startTime) {
          this.notes.add(i, note);
          added = true;
        }
      }
      if (!added) {
        this.notes.add(note);
      }
    }
  }

  @Override
  public void removeNote(MusicNote note) {
    if (notes.contains(note)) {
      notes.remove(note);
    } else {
      throw new IllegalArgumentException("note not in this score");
    }
  }

  @Override
  public void replaceNote(MusicNote note, MusicNote newNote) {
    this.removeNote(note);
    this.addNote(newNote);
  }

  @Override
  public MusicScore composeScores(MusicScore score) {
    MusicScoreImpl toReturn = new MusicScoreImpl(this.notes(), tempo);
    List<MusicNote> scoreNotes = score.notes();
    for (MusicNote n : scoreNotes) {
      try {
        toReturn.addNote(n);
      } catch (IllegalArgumentException e) {
        for (int i = n.startTime; i <= n.endTime(); i++) {
          if (this.notePlayingAt(n.pitch, n.octave, i, n.instrument)) {
            MusicNote maybeReplace = getNotePlayingAt(n.pitch, n.octave, i, n.instrument);
            if (maybeReplace.startTime <= n.startTime && maybeReplace.endTime() >= n.endTime()) {
              break;
            } else if (maybeReplace.startTime > n.startTime && maybeReplace.endTime() < n.endTime
                ()) {
              toReturn.replaceNote(maybeReplace, n);
            } else if (maybeReplace.startTime <= n.startTime) {
              toReturn.replaceNote(maybeReplace,
                  new MusicNote.NoteBuilder().pitch(n.pitch).octave(n.octave).startTime
                      (maybeReplace.startTime).duration(n.endTime() - maybeReplace.startTime + 1)
                      .instrument(n.instrument).build());
            } else {
              toReturn.replaceNote(maybeReplace,
                  new MusicNote.NoteBuilder().pitch(n.pitch).octave(n.octave).startTime(n
                      .startTime).duration(maybeReplace.endTime() - n.startTime + 1).instrument(n
                      .instrument).build());
            }
          }
        }
      }
    }
    return toReturn;
  }

  @Override
  public MusicScore consecutiveScore(MusicScore score) {
    List<MusicNote> scoreNotes = score.notes();
    for (int i = 0; i < scoreNotes.size(); i++) {
      MusicNote n = scoreNotes.get(i);
      scoreNotes.set(i, new MusicNote.NoteBuilder()
          .pitch(n.pitch)
          .octave(n.octave)
          .startTime(n.startTime + this.duration())
          .duration(n.duration)
          .instrument(n.instrument)
          .build());
    }
    scoreNotes.addAll(this.notes());
    return new MusicScoreImpl(scoreNotes, tempo);
  }

  @Override
  public int duration() {
    int lastEnd = 0;
    for (MusicNote n : notes) {
      if (n.endTime() > lastEnd) {
        lastEnd = n.endTime();
      }
    }
    return lastEnd;
  }

  @Override
  public List<MusicNote> notes() {
    return notes.stream().collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public List<MusicNote> notesAtTime(int time) {
    List<MusicNote> notesToReturn = notes();
    notesToReturn.removeIf((MusicNote m) -> !m.playingAt(time));
    return notesToReturn;
  }

  @Override
  public List<MusicNote> notesStartingAtTime(int time) {
    List<MusicNote> notesToReturn = notes();
    notesToReturn.removeIf((MusicNote m) -> (m.startTime != time));
    return notesToReturn;
  }

  @Override
  public int highestOctave() {
    if (notes.isEmpty()) {
      return 4;
    } else {
      return Collections.max(notes, new PitchComparator()).octave;
    }
  }

  @Override
  public int lowestOctave() {
    if (notes.isEmpty()) {
      return 4;
    } else {
      return Collections.min(notes, new PitchComparator()).octave;
    }
  }

  @Override
  public boolean containsNote(int pitch, int octave, int startTime, int instrument) {
    boolean found = false;
    for (int i = 0; i < notes.size() && !found; i++) {
      MusicNote note = notes.get(i);
      if (note.startTime > startTime) {
        break;
      } else if (note.startTime == startTime && note.pitch == pitch && note.octave == octave
                 && note.instrument == instrument) {
        found = true;
      }
    }
    return found;
  }

  /**
   * Check if a note of a given pitch, octave, and instrument is playing at a given time.
   *
   * @param pitch      The pitch of the note.
   * @param octave     The octave of the note.
   * @param time       The time to be checked.
   * @param instrument The instrument to be checked.
   * @return True if a note of the given pitch and octave is playing at the given time.
   */
  private boolean notePlayingAt(int pitch, int octave, int time, int instrument) {
    boolean found = false;
    for (int i = 0; i < notes.size() && !found; i++) {
      MusicNote note = notes.get(i);
      if (note.startTime > time) {
        break;
      } else if (note.endTime() >= time && note.pitch == pitch && note.octave == octave
                 && note.instrument == instrument) {
        found = true;
      }
    }
    return found;
  }

  /**
   * Gets a note of a given pitch and octave is playing at a given time.
   *
   * @param pitch      The pitch of the note.
   * @param octave     The octave of the note.
   * @param time       The time to be checked.
   * @param instrument The instrument to be checked.
   * @return The note playing at time with pitch and octave.
   * @throws IllegalArgumentException if there is no valid note.
   */
  private MusicNote getNotePlayingAt(int pitch, int octave, int time, int instrument) {
    for (MusicNote note : notes) {
      if (note.startTime > time) {
        break;
      } else if (note.endTime() >= time && note.pitch == pitch && note.octave == octave
                 && note.instrument == instrument) {
        return note;
      }
    }
    throw new IllegalArgumentException();
  }

  @Override
  public MusicNote getNote(int pitch, int octave, int startTime, int instrument) {
    for (MusicNote note : notes) {
      if (note.startTime == startTime && note.pitch == pitch && note.octave == octave
          && note.instrument == instrument) {
        return note;
      }
      if (note.startTime > startTime) {
        break;
      }
    }
    throw new IllegalArgumentException("note not in score");
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public MusicScore copy() {
    return new MusicScoreImpl(this.notes(), this.tempo);
  }

  @Override
  public List<Integer> instruments() {
    List<Integer> instruments = new ArrayList<>();
    for (MusicNote n : notes) {
      if (!instruments.contains(n.instrument)) {
        instruments.add(n.instrument);
      }
    }
    return instruments;
  }
}
