package cs3500.music.view;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.VoiceStatus;

import cs3500.music.model.ReadOnlyModel;
import cs3500.music.model.ITone;

/**
 * This view plays the tones in the model using MIDI devices and MidiMessages.
 */
public final class MidiViewImpl implements MidiView {
  private Synthesizer synthesizer;
  private Receiver receiver;
  private ReadOnlyModel model;
  private List<ITone> tones;
  private StringBuilder ap = null;
  private MidiChannel[] channels;
  private List<Integer> instruments;
  private int timerCurrentBeat = 0;
  
  
  /**
   * Constructor for MidiViewImpl.
   * @throws MidiUnavailableException this throws this exception.
   */
  public MidiViewImpl() throws MidiUnavailableException {
    this.synthesizer = MidiSystem.getSynthesizer();
    this.synthesizer.open();
    this.receiver = synthesizer.getReceiver();
    this.synthesizer.loadAllInstruments(this.synthesizer.getDefaultSoundbank());
    this.channels = this.synthesizer.getChannels();
  }
  
  /**
   * Second Testing Constructor for MidiViewImpl which tests the mock MIDI device.
   * @param ap Appendable
   * @throws MidiUnavailableException this throws this exception.
   */
  public MidiViewImpl(StringBuilder ap) throws
          MidiUnavailableException {
    this.synthesizer = new MockMidiDevice();
    this.receiver = synthesizer.getReceiver();
    this.ap = ap;
  }
  
  /**
   * This puts the information of which instruments are in the song into an List.
   * @param instList List[Integer]
   * @return List[Integer]
   */
  private List<Integer> getInstruments(List<Integer> instList) {
    for (ITone t : this.tones) {
      if (!instList.contains(t.getInstrument() - 1)) {
        instList.add(t.getInstrument() - 1);
      }
    }
    return instList;
  }
  
  /**
   * This prepares the proper channels with the proper instrument.
   */
  private void prepareChannels() {
    for (int i = 0; i < this.instruments.size(); i++) {
      this.channels[i].programChange(this.instruments.get(i));
    }
  }
  
  /**
   * This method sends all the required messages to the synthesizer via the receiver.
   * @param tones List[ITone]
   */
  private void setUpSequence(List<ITone> tones) {
    long tempo = this.model.viewTempo();
    long timeStampNoteOff;
    if (tones != null) {
      for (ITone t : tones) {
        // CALCULATED TIMESTAMP FOR CURRENT TONES NOTE OFF MESSAGE
        timeStampNoteOff = this.synthesizer.getMicrosecondPosition()
                + t.getDuration()
                * tempo;
        try {
          // CREATES THE MESSAGE TO TURN ON THE NOTE
          ShortMessage startMessage = new ShortMessage();
          startMessage.setMessage(ShortMessage.NOTE_ON, 0, t.getNote(),
                  t.getVolume());
          
          // CREATES THE MESSAGE TO TURN OFF THE NOTE
          ShortMessage endMessage = new ShortMessage();
          endMessage.setMessage(ShortMessage.NOTE_OFF, 0, t.getNote(),
                  t.getVolume());
          
          // SENDS MESSAGE TO TURN ON THE NOTE
          this.receiver.send(startMessage, 1000000);
          
          // SENDS MESSAGE TO TURN OFF THE NOTE
          this.receiver.send(endMessage, timeStampNoteOff
                  + synthesizer.getMicrosecondPosition()
                  / tempo);
          
        } catch (InvalidMidiDataException e) {
          // HANDLE
          e.printStackTrace();
        }
      }
    }
  }
  
  @Override
  public void initialize(ReadOnlyModel model) {
    this.model = model;
    this.tones = this.model.viewTones();
    if (this.ap == null) {
      this.instruments = this.getInstruments(new ArrayList<Integer>());
      if (this.instruments.size() >= 16) {
        throw new InvalidParameterException("the specified piece has more than 16 " +
                "different instruments.");
      }
      this.prepareChannels();
    }
  }
  
  @Override
  public void increaseCurrentBeat() {
    this.setUpSequence(this.model.viewTonesAtStartBeat(timerCurrentBeat));
    this.timerCurrentBeat++;
  }
  
  @Override
  public void resetBeat() {
    this.timerCurrentBeat = 0;
  }
  
  /**
   * Created by Abinader on 11/7/16.
   * MockMidiDevice is an Inner class that represents a synthesizer device for facilitating testing.
   */
  private final class MockMidiDevice implements MidiDevice, Synthesizer {
    
    @Override
    public Info getDeviceInfo() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public void open() throws MidiUnavailableException {
      // EMPTY BECAUSE THIS IS CALLED
    }
    
    @Override
    public void close() {
      // EMPTY BECAUSE THIS IS CALLED.
    }
    
    @Override
    public boolean isOpen() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public long getMicrosecondPosition() {
      return 0;
    }
    
    @Override
    public int getMaxReceivers() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public int getMaxTransmitters() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
      return new MockReceiver();
    }
    
    @Override
    public List<Receiver> getReceivers() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public List<Transmitter> getTransmitters() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public int getMaxPolyphony() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public long getLatency() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public MidiChannel[] getChannels() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public VoiceStatus[] getVoiceStatus() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isSoundbankSupported(Soundbank soundbank) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean loadInstrument(Instrument instrument) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public void unloadInstrument(Instrument instrument) {
      throw new UnsupportedOperationException();
      
    }
    
    @Override
    public boolean remapInstrument(Instrument from, Instrument to) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public Soundbank getDefaultSoundbank() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public Instrument[] getAvailableInstruments() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public Instrument[] getLoadedInstruments() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean loadAllInstruments(Soundbank soundbank) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public void unloadAllInstruments(Soundbank soundbank) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean loadInstruments(Soundbank soundbank, Patch[] patchList) {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public void unloadInstruments(Soundbank soundbank, Patch[] patchList) {
      throw new UnsupportedOperationException();
    }
  }
  
  /**
   * Created by Abinader on 11/7/16.
   * MockReceiver is an Inner Class that represents a receiver device which facilitates testing.
   */
  private final class MockReceiver implements Receiver {
    
    /**
     * Prints the message values and the timestamp values to a stringbuilder.
     * @param message MidiMessage
     * @param timeStamp long
     */
    @Override
    public void send(MidiMessage message, long timeStamp) {
      ap.append("status byte: ")
              .append(message.getStatus())
              .append(" timeStamp value: ").append(timeStamp + "\n");
    }
    
    @Override
    public void close() {
      throw new UnsupportedOperationException();
    }
  }
}
