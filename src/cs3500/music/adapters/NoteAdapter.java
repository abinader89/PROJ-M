package cs3500.music.adapters;

import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.ITone;
import cs3500.music.provider.INote;
import cs3500.music.provider.ISong;
import cs3500.music.provider.Note;
import cs3500.music.provider.Pitch;
import cs3500.music.provider.Song;

/**
 * Created by Abinader on 11/27/16.
 * This is an adapter for tones.
 */
public class NoteAdapter implements IViewAdapter {
  
  /**
   * This method does the converting from tone to note.
   * @param toneList List[ITone]
   * @return List[ISong]
   */
  public static List<ISong> toneConverter(List<ITone> toneList, int tempo) {
    List<ISong> returnList = new ArrayList<ISong>();
    ArrayList<INote> noteList = new ArrayList<INote>();
    ISong song = new Song("STRING", new ArrayList<>());
    for (ITone t: toneList) {
      // CONVERT THESE TONES INTO PROVIDER NOTES
      noteList.add(new Note(Pitch.values()[(t.getNote() % 12)], (t.getNote() / 12),
              t.getStartBeat(), t.getDuration(), t.getInstrument(), t.getVolume()));
    }
    song.setTempo(tempo);
    song.addAllNotes(noteList);
    returnList.add(song);
    return returnList;
  }
}
