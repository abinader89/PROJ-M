package cs3500.music.provider;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * Panel specifically for getting user input for Notes.
 */
public class NoteMakerPane extends JPanel {

  private final HashMap<String, Integer> instrumentMap;
  private final HashMap<Integer, String> instrumentReverseMap;
  private final JButton addButton;
  private final JSlider volumeSlider;
  private final JComboBox<String> instrumentSelect;
  private final JComboBox<Pitch> pitchSelect;
  private final JTextField octaveInput;
  private final JTextField startInput;
  private final JTextField durationInput;

  /**
   * Constructs a NoteMakerPane.
   */
  public NoteMakerPane() {
    this.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Create volume slider
    this.volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 127, 100);
    this.volumeSlider.setMajorTickSpacing(50);
    this.volumeSlider.setPaintTicks(true);
    this.volumeSlider.setPaintLabels(true);

    JLabel volumeLbl = new JLabel("Volume:");
    volumeLbl.setLabelFor(this.volumeSlider);

    // Create and configure add button.
    this.addButton = new JButton("Add Note");
    // create instrument input.
    this.instrumentMap = new HashMap<>();
    this.instrumentReverseMap = new HashMap<>();
    fillMaps();

    this.instrumentSelect = new JComboBox<String>(new Vector<>(instrumentMap.keySet()));

    // Create pitch input.
    JLabel pitchLbl = new JLabel("Pitch");
    this.pitchSelect = new JComboBox<Pitch>(Pitch.values());
    pitchLbl.setLabelFor(pitchSelect);

    // Create octave input.
    this.octaveInput = new JTextField(4);
    JLabel octaveLbl = new JLabel("Octave:");
    octaveLbl.setLabelFor(this.octaveInput);

    // Create start input
    this.startInput = new JTextField(10);
    JLabel startLbl = new JLabel("Start Beat:");
    startLbl.setLabelFor(this.startInput);

    // Create duration input
    this.durationInput = new JTextField(10);
    JLabel durationLbl = new JLabel("Duration in Beats:");
    durationLbl.setLabelFor(this.durationInput);

    // Add all of the components.
    this.add(this.addButton);
    this.add(pitchLbl);
    this.add(this.pitchSelect);
    this.add(octaveLbl);
    this.add(this.octaveInput);
    this.add(startLbl);
    this.add(this.startInput);
    this.add(durationLbl);
    this.add(this.durationInput);
    this.add(this.instrumentSelect);
    this.add(volumeLbl);
    this.add(this.volumeSlider);

    this.setFocusable(true);
  }

  /**
   * Fill in all of the instrument map information.
   */
  private void fillMaps() {
    // End putting stuff in.
    this.instrumentMap.put("Acoustic Grand Piano", 1);
    this.instrumentMap.put("Xylophone", 14);
    this.instrumentMap.put("Church Organ", 20);
    this.instrumentMap.put("Accordion", 22);
    this.instrumentMap.put("Harmonica", 23);
    this.instrumentMap.put("Acoustic Guitar", 25);
    this.instrumentMap.put("Electric Guitar", 28);
    this.instrumentMap.put("Acoustic Bass", 33);
    this.instrumentMap.put("Electric Bass", 34);
    this.instrumentMap.put("Violin", 41);
    this.instrumentMap.put("Viola", 42);
    this.instrumentMap.put("Cello", 43);
    this.instrumentMap.put("Trumpet", 57);
    this.instrumentMap.put("Trombone", 58);
    this.instrumentMap.put("Tuba", 59);
    this.instrumentMap.put("Alto Sax", 66);
    this.instrumentMap.put("Clarinet", 72);
    this.instrumentMap.put("Flute", 74);
    this.instrumentMap.put("Tinkle Bell", 113);
    this.instrumentMap.put("Steel Drums", 115);

    for (String key : this.instrumentMap.keySet()) {
      this.instrumentReverseMap.put(this.instrumentMap.get(key), key);
    }

  }

  /**
   * Set the action listener on the add button.
   *
   * @param onAdd the action when the add button is clicked.
   */
  public void setActionListener(ActionListener onAdd) {
    this.addButton.addActionListener(onAdd);
  }

  /**
   * Set the fields to the values in the note.
   *
   * @param note the note to use for the fields.
   */
  public void setNoteValues(INote note) {
    this.instrumentSelect.setSelectedItem(this.instrumentReverseMap.get(note.getInstrument()));
    this.pitchSelect.setSelectedItem(note.pitchPlease());
    this.octaveInput.setText("" + note.getOctave());
    this.startInput.setText("" + note.getStart());
    this.durationInput.setText("" + note.getDuration());
    this.volumeSlider.setValue(note.getVolume());
  }

  /**
   * Gets the note from this NoteMakerPane.
   *
   * @return INote from the NoteMakerPane.
   */
  public INote getNote() {
    Pitch p = (Pitch) this.pitchSelect.getSelectedItem();
    int octave = Integer.parseInt(this.octaveInput.getText());
    int start = Integer.parseInt(this.startInput.getText());
    int duration = Integer.parseInt(this.durationInput.getText());
    int instrument = this.instrumentMap.get((String) this.instrumentSelect.getSelectedItem());
    int volume = this.volumeSlider.getValue();
    return new Note(p, octave, start, duration, instrument, volume);
  }
}
