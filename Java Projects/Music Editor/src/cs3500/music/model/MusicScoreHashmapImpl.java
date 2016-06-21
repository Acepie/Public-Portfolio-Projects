package cs3500.music.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cs3500.music.util.CompositionBuilder;

/**
 * Implementation of Music Score using a hashmap
 */
public final class MusicScoreHashmapImpl implements MusicScore {

  /**
   * A map of the notes in the song. The key is the beat of the song and the value is a list of all
   * notes playing at that time
   */
  private final Map<Integer, List<MusicNote>> noteMap;

  /**
   * Tempo of the song.
   */
  private final int tempo;

  /**
   * Duration of the song.
   */
  private int duration;

  /**
   * Lowest octave in the song.
   */
  private int lowestOctave;

  /**
   * Highest octave in the song.
   */
  private int highestOctave;

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
      return new MusicScoreHashmapImpl(this.notes, this.tempo);
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

  private MusicScoreHashmapImpl(List<MusicNote> notes, int tempo) {
    noteMap = new Hashtable<Integer, List<MusicNote>>();
    this.tempo = tempo;
    this.duration = 0;
    this.lowestOctave = 10;
    this.highestOctave = 0;
    for (MusicNote note : notes) {
      this.addNote(note);
    }
  }

  @Override
  public void addNote(MusicNote... notes) {
    for (MusicNote n : notes) {
      for (int i = n.startTime; i <= n.endTime(); i++) {
        if (n.endTime() > this.duration) {
          this.duration = n.endTime();
        }
        if (n.octave > this.highestOctave) {
          this.highestOctave = n.octave;
        }
        if (n.octave < this.lowestOctave) {
          this.lowestOctave = n.octave;
        }
        if (noteMap.containsKey(i)) {
          List<MusicNote> notesPlayingAtTime = noteMap.get(i);
          for (MusicNote n2 : notesPlayingAtTime) {
            if (n.pitch == n2.pitch && n.octave == n2.octave && n.instrument == n2.instrument) {
              throw new IllegalArgumentException("note already playing here");
            }
          }
          notesPlayingAtTime.add(n);
        } else {
          List<MusicNote> noteInList = new ArrayList<MusicNote>();
          noteInList.add(n);
          noteMap.put(i, noteInList);
        }
      }
    }
  }

  @Override
  public void removeNote(MusicNote note) {
    for (int i = note.startTime; i <= note.endTime(); i++) {
      List<MusicNote> notesAtTime = noteMap.get(note.startTime);
      if (notesAtTime.contains(note)) {
        notesAtTime.remove(note);
      } else {
        throw new IllegalArgumentException("note not in score");
      }
    }
  }

  @Override
  public void replaceNote(MusicNote note, MusicNote newNote) {
    this.removeNote(note);
    this.addNote(newNote);
  }

  @Override
  public MusicScore composeScores(MusicScore score) {
    MusicScore toReturn = new MusicScoreHashmapImpl(this.notes(), this.tempo);
    for (MusicNote n : score.notes()) {
      try {
        toReturn.addNote(n);
      } catch (IllegalArgumentException e) {
        for (int i = n.startTime; i <= n.endTime(); i++) {
          if (!toReturn.notesAtTime(i).isEmpty()) {
            List<MusicNote> notesPlayingAtTime = toReturn.notesAtTime(i);
            for (MusicNote n2 : notesPlayingAtTime) {
              if (n.pitch == n2.pitch && n.octave == n2.octave && n.instrument == n2.instrument) {
                if (n2.startTime <= n.startTime && n2.endTime() >= n.endTime()) {
                  break;
                } else if (n2.startTime > n.startTime && n2.endTime() < n.endTime
                    ()) {
                  toReturn.replaceNote(n2, n);
                } else if (n2.startTime <= n.startTime) {
                  toReturn.replaceNote(n2,
                      new MusicNote.NoteBuilder().pitch(n.pitch).octave(n.octave).startTime
                          (n2.startTime).duration(n.endTime() - n2.startTime + 1)
                          .instrument(n.instrument).build());
                } else {
                  toReturn.replaceNote(n2,
                      new MusicNote.NoteBuilder().pitch(n.pitch).octave(n.octave).startTime(n
                          .startTime).duration(n2.endTime() - n.startTime + 1).instrument(n
                          .instrument).build());
                }
              }
            }
          }
        }
      }
    }
    return toReturn;
  }

  @Override
  public MusicScore consecutiveScore(MusicScore score) {
    MusicScore scoreToReturn = new MusicScoreHashmapImpl(this.notes(), this.tempo);
    for (MusicNote note : score.notes()) {
      MusicNote noteToAdd = new MusicNote.NoteBuilder().pitch(note.pitch).octave(note.octave)
          .duration(note.duration).instrument(note.instrument).startTime(note.startTime + this
              .duration).volume(note.volume).build();
      scoreToReturn.addNote(noteToAdd);
    }
    return scoreToReturn;
  }

  @Override
  public int duration() {
    return this.duration;
  }

  @Override
  public List<MusicNote> notes() {
    List notesToReturn = new ArrayList<MusicNote>();
    for (int i = 0; i <= this.duration; i++) {
      if (noteMap.containsKey(i)) {
        for (MusicNote note : noteMap.get(i)) {
          if (!notesToReturn.contains(note)) {
            notesToReturn.add(note);
          }
        }
      }
    }
    notesToReturn.sort(new TimeComparator());
    return notesToReturn;
  }

  @Override
  public List<MusicNote> notesAtTime(int time) {
    if (noteMap.containsKey(time)) {
      return noteMap.get(time);
    } else {
      return new ArrayList<MusicNote>();
    }
  }

  @Override
  public List<MusicNote> notesStartingAtTime(int time) {
    if (noteMap.containsKey(time)) {
      List<MusicNote> notesToReturn = noteMap.get(time);
      notesToReturn.removeIf((MusicNote m) -> (m.startTime != time));
      return notesToReturn;
    } else {
      return new ArrayList<MusicNote>();
    }
  }

  @Override
  public int highestOctave() {
    if (notes().isEmpty()) {
      return 4;
    }
    return this.highestOctave;
  }

  @Override
  public int lowestOctave() {
    if (notes().isEmpty()) {
      return 4;
    }
    return this.lowestOctave;
  }

  @Override
  public boolean containsNote(int pitch, int octave, int startTime, int instrument) {
    List<MusicNote> notesPlayingAtTime = noteMap.get(startTime);
    for (MusicNote note : notesPlayingAtTime) {
      if (note.pitch == pitch
          && note.octave == octave
          && note.startTime == startTime
          && note.instrument == instrument) {
        return true;
      }
    }
    return false;
  }

  @Override
  public MusicNote getNote(int pitch, int octave, int startTime, int instrument) {
    List<MusicNote> notesPlayingAtTime = noteMap.get(startTime);
    for (MusicNote note : notesPlayingAtTime) {
      if (note.pitch == pitch
          && note.octave == octave
          && note.startTime == startTime
          && note.instrument == instrument) {
        return note;
      }
    }
    throw new IllegalArgumentException();
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public MusicScore copy() {
    return new MusicScoreHashmapImpl(this.notes(), this.tempo);
  }

  @Override
  public List<Integer> instruments() {
    List<Integer> instruments = new ArrayList<>();
    for (MusicNote n : this.notes()) {
      if (!instruments.contains(n.instrument)) {
        instruments.add(n.instrument);
      }
    }
    return instruments;
  }
}
