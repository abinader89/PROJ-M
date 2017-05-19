# THE MUSIC EDITOR MODEL

---------------------------------
## Introduction: Key Points

* BEATS                 We elected to create our model without a Beat class. Instead, beats are
                        referenced within each Note, and Songs refer to their Notes' beat fields to
                        create the beats of the song. How this concept comes into play with specific
                        methods is explained in their respective class descriptions.

* OCTAVES               An intentional limitation of this design is that the only octaves that cannot
                        be less than zero. 

* INTRA-OCTAVE RANK     Each pitch within any one octave-rank has its own internal rank, which I
                        call an "intra-octave rank." 

* TOTAL RANK            For the purposes of measuring the full range of notes covered in a song,
                        we included a way to measure how the pitch of an individual note compares to
                        all other notes in the song, not just within one octave. This allows for
                        every note to have its own unique integer identifier and also is useful in
                        printing the header for the model, which ranges from the lowest note in the
                        song to the highest note. The "total rank," as we call this measurement, is
                        determined by the sum of the note's octave rank plus its intra-octave rank.
                        The "total rank" breakdown is as follows:

                                        C (lowest pitch)        B (highest pitch)
                          Octave 0              0                       11
                          Octave 1             12                       23
                          Octave 2             24                       35
                          Octave 3             36                       47
                          Octave 4             48                       59
                          Octave 5             60                       71
                          Octave 6             72                       83
                          Octave 7             84                       95
                          Octave 8             96                      107
                          Octave 9            108                      119

* MULTIPLE SONGS        For this particular "release," we do not support multiple song tabs open 
                        at once. We have kept our code flexible so that this functionality can be
                        added in the future, but for now, only one song can be viewed at a time.

* RUNNING WITH THE JAR  To run the program from the command-line, execute the following command
                        ```
                        java -jar MusicEditor.jar <music filename.txt> <Editor Mode>
                        ```
                        Where the music file name is the path of a music file and the Editor mode
                        is one of visual, midi, console, or combined.

---------------------------------
## UTILS PACKAGE

---------------------------------
### CompositionBuilder (from starter code)

* ABOUT: A builder of compositions.  Since we do not know in advance what the name of the main type 
         is for a model, we parameterize this builder interface by an unknown type.

* METHODS:
    - `build`, which constructs an actual composition, given the notes that have been added
    - `setTempo`, which sets the tempo of the piece in microseconds per beat
    - `newNote`, which adds a new note to this piece

---------------------------------
### MusicReader (from starter code)

* ABOUT: A helper to read music data and construct a music composition from it.

* METHODS:
    - `parseFile`, which is a factory for producing new music compositions. It is given a source 
      of music and a builder for constructing compositions. The input file format consists of two 
      types of lines (tempo and note).

---------------------------------
### Pair

* ABOUT: A class for a 2-tuple, utilized by the ISong interface and implementing classes.

* FIELDS:
    - `first`, representing the first item in the tuple
    - `second`, representing the second item in the tuple
    
* METHODS:
    - Getter methods for both fields
    - We also override the `equals` and `hashCode` methods in this class. Two Pairs are "equal" 
      when both of the fields are the same.

---------------------------------
### TrackBuilder

* ABOUT: A class to facilitate creating a MIDI track from our model's song representation.

* FIELDS:
    - Sequence `seq`
    - Track `track`
    - `millisecondsPerTick` as an integer
    - `channels` as a HashMap 
    - `nextChannel` as an integer

    
* METHODS:
    - `addNote`, which adds the given note to this track
    - Getter method for the sequence

---------------------------------
## MODEL PACKAGE

---------------------------------
### Pitch enum

* ABOUT: The Pitch class is an enum of each pitch occurring in a single octave.

* FIELDS:
    - A name in the form of a string (EX: "C#")
    - An intra-octave rank (defined above) in the form of an integer

* METHODS:
    - Getter-methods for each of these fields, both of which are ultimately used in the Song 
      class (described below).

---------------------------------
### INote class

* ABOUT: The INote interface defines the smallest full unit of the model -- like atoms are to matter.

* METHODS:
    - `getTotalRank`, which calculates the total rank of a note (defined in Key Points)
    - `compare`, which returns an integer comparison of this note to the given note
        - If this note is a(n) ____ rank than the given note, the output is ___:
        - lower; < 0
        - equal; 0
        - higher; >0
    - `getEnd`, which is calculated by adding the beat duration to the start beat
    - `moveStart`, which is necessary to combine songs so they play consecutively
    - `resizeDuration`, which changes the length of time for which this note plays
    - Also included in this interface are getter-methods for each of the fields

---------------------------------
### Note class

* ABOUT: The Note class is the concrete implementation of the INote interface.

* FIELDS:
    - Pitch (as explained above)
    - Octave (defined in Key Points)
    - A start beat in the form of an integer, which determines when in a Song this Note will begin to play
    - An integer duration, representing the length of this note in number of beats
    - An integer instrument corresponding to the General MIDI instrument list (1-128)
    - An integer volume

* METHODS:
    - Implementations of the following public methods from the interface:
        - `getTotalRank`
        - `compare`
        - `getEnd`
        - `moveStart`
        - `resizeDuration`
        - All getter methods
    - We also override the `equals` and `hashCode` methods in this class. Two notes are "equal"
      when ALL of the fields are the same.

---------------------------------
### ISong interface

* ABOUT: The ISong interface defines the product of combining notes to make a piece of music.

* METHODS:
    - `setTempo`, which sets the current tempo of this song to the given tempo
    - `addNote`, which adds the given note to this song
    - `addAllNotes`, which adds all notes in the given Collection of notes to this song
    - `removeNote`, which removes the given note from this song (if the given note does exist in this song)
    - `getLength`, which calculates the total number of beats in this song
    - `getLowHigh`, which returns a Pair of notes representing the lowest and highest notes in this song
    - `getFirstLast`, which returns a Pair of notes representing the first and last occurring notes in this song
    - Also included in this interface are getter-methods for each of the fields in ASong

---------------------------------
### ASong abstract class

* ABOUT: The ASong class Represents the abstract Song class. Because we utilized a Collection, 
         we abstracted the Song class in such a case that we wanted to change the container used. 
         Most of the functionality can still be defined within this abstract class.

* FIELDS:
    - A name in the form of a String
    - A Collection of notes
    - An integer beats per measure (bpm), which we assumed to be 4 unless otherwise specified
    - An integer tempo (microseconds per beat), which we assumed to be 100, unless otherwise specified

* METHODS:
    - Implementations of the following public methods from the interface:
        - `setTempo`
        - `addNote`
        - `addAllNotes`
        - `removeNote`
        - `getLength`
        - `getLowHigh`
        - `getFirstLast`
        - All getter methods except `getNotes`
    - We also override the `equals` and `hashCode` methods in this class. Two songs are "equal"
      when ALL of the fields are the same.
      
---------------------------------
### Song class

* ABOUT: The Song class is the concrete extension of ASong, which implements the storage of Notes 
         as an ArrayList (instead of a generalized Collection).

* CLASSES:
    - `SongBuilder` implementation of the `CompositionBuilder` interface using a song as the type for the model
    
* FIELDS:
    - A name in the form of a String, which we included at this model stage in anticipation that it
      may be useful in the later controller/view stages
    - A collection of notes as an array list
    - An integer beats per measure (bpm), which we included at this model stage in anticipation that
      it may be useful in the later controller/view stages

* METHODS:
    - Getter method for the `SongBuilder`
    - Getter method `getNotes` to retrieve the song's initial notes as an ArrayList

---------------------------------
### IMusicModel interface

* ABOUT: IMusicModel is the interface representation of a the music editor model. 
         A music editor can contain multiple songs at once. This interface allows the client
         to access the intentionally public methods in MusicModel so they can utilize the different
         functionalities. It is parameterized over a general `Key`.

* METHODS:
    - `addSong`, which adds a song to this model
    - `removeSong`, which removes the given song from this model (provided that the given song 
      already exists there)
    - `combineSimul`, which allows the client to specify a sub-list of songs *already contained
      within the model* that they would like to combine into one single song, played at the same
      time.
    - `combineConsec`, which allows the client to specify a sub-list of songs *already contained
      within the model* that they would like to combine into one single song, played one after
      another.
    - Also included in this interface are getter-methods for each of the fields in AMusicModel.

---------------------------------
### AMusicModel abstract class

* ABOUT: AMusicModel is the abstract class representation of the music editor itself. 
         This abstract class allows the client to use any kind of Map and storage method.

* FIELDS:
    - A list of the initial songs in this model as a Map<Key, ISong>

* METHODS:
    - Implementations of the following public methods from the interface:
        - `addSong`
        - `removeSong`
    - Getter method `getSongs`

---------------------------------
### MusicModel class

* ABOUT: MusicModel is the concrete class representation of the music editor with songs stored in a
         String-keyed HashMap. This implementation also relies on the default song type 
         for creating and combining songs.

* FIELDS:
    - A list of songs stored as a String-keyed HashMap (the String representing the song name)

* METHODS:
    - Implementations of the following public methods from the interface:
        - `combineSimul`
        - `combineConsec`

---------------------------------
## VIEW PACKAGE

---------------------------------
### IMusicView interface

* ABOUT: The interface representation of a music editor's view. 

* METHODS:
    - `render`, which takes a list of songs as input. Render may be called many times, 
      and it is up to the implementers of IMusicView to decide how to handle new data that 
      is passed to render. For now, the view is only responsible for showing the Song data 
      to a user.
    - `play`, which plays the song from its current position.
    - `pause`, which pauses the song and holds its current position. 
    - `stop`, which stops the song and send the current position back to the beginning.
    - `cleanUp`, which tells the view to clean up its resources before dying.
    - `setSongAdder` and `setSongRemover`, function objects which set the song adder/remover 
      for the relevant views
    - `showMessage`, which is used to convey a message to the client in some form.

---------------------------------
### IGUIMusicView interface

* ABOUT: Extension of IMusicView interface for GUI-specific view functionality.

* METHODS:
    - `setOnPlayAction`, `setOnPauseAction`, `setOnStopAction`, which each take an ActionListener 
      and apply them to the respective existing buttons. These each only allow one ActionListener,
      so if a second is called, the first is removed and replaced with the newly called ActionListener
    - `setTimeGetter`, which takes a Supplier `howToGetTime` and gives the current time of the MIDI timer
    - Getter method `getSelectedSong`


---------------------------------
### GUIMusicView class

* ABOUT: The GUIMusicView is a JFrame that relies heavily on the SongRenderPane to display 
         the Song in a user friendly way. The GUIMusicView itself is the top-most parent 
         Component in the design. 

* FIELDS:
    - A JTabbedPane field `songTabs` at the center of a BorderLayout
    - A constant `SLEEP_TIME`, which we have set to 100 milliseconds
    - A NoteMakerPane `noteMaker` (see NoteMakerPane class description)
    - Consumers `addSong` and `removeSong`
    - Supplier `getTime`
    - JButtons `playButton`, `pauseButton`, and `stopButton`
    - Boolean `isPlaying` utilized in our `renderTracker` Thread
    - A KeyboardHandler `keyHandler`
    - Integers representing the `selectedInstrument` and `selectedVolume`

* METHODS:
    - Implementation of the `render` method from the IGUIMusicView interface, which checks to see 
      if each song is part of the current tabbed pane. If it is, the SongRenderPane is updated to 
      display the newest version of the song. Otherwise, a new SongRenderPane is created using the 
      new Song.
    - Implementation of the `play`, `pause`, `stop`, and `cleanUp` methods
        - When a song is played, the `play` method is called, creating a new Thread `renderTracker` 
          and utilizing the `isPlaying` boolean. 
        - `renderTracker` updates where the tracker line is and shifts the frame when the tracker
          line moves outside the frame
    - Getter methods `getSelectedSongPane` and `getSelectedSong` (the latter is an implementation 
      from the IGUIMusicView interface)
    - Implementation of `setSongAdder`, `setSongRemover` from the IMusicView interface
    - Implementation of `showMessage` method from the interface, which in this implementation 
      displays the desired message as a JOptionDialogue

---------------------------------
### IMidiMusicView interface

* ABOUT: IMidiMusicView is an interface extension of the IMusicView interface, adding the 
         MIDI-specific getter method for the current time in milliseconds. 
         
* METHODS:
    - `getTimeMilli`, which gets the current time from the underlying sequencer in the MIDI view. 
      We wanted to keep our time synchronization in one place, and because the MidiMusicView 
      class owns the sequencer, we chose to make this extension within this area of the view. 
      **NOTE**: We realize that this approach may be very different than that of most of our peers, 
            so it is important for clients to note that in order to make the tracker line function, 
            this is the method they will need to utilize.

---------------------------------
### MidiMusicView class

* ABOUT: MidiMusicView is the class used to make a Song audible using Java's MIDI framework. 
         Although the IMusicView interface passes a list of ISongs, only the first song is rendered.
         In the future, it is possible that we would play multiple songs back to back.

* METHODS:
    - Implementation of the `render` method from the interface, which in this class generates 
      a new Track using the notes in the song and the TrackBuilder class. Each of the songs is 
      converted into two MidiEvents which hold the Messages for starting and stopping the note 
      at the calculated offset times. These notes are part of a sequence which is fed into a 
      Sequencer object that is then used to play the song. The Sequencer class was chosen in 
      this implementation to avoid keeping track of scheduling each of the notes.
    - `ensureSequencerState`, which ensures that the sequencer is open before playing the 
      MIDI track.
    - Implementation of the `play`, `pause`, `stop`, `cleanUp`, and `getTimeMilli` methods 
      from the interface

---------------------------------
### MidiAndGuiView class

* ABOUT: The MidiAndGuiView class is an adapter that binds the MidiMusicView and GUIMusicView 
         classes together. 

* FIELDS:
    - IGUIMusicView `guiView`
    - IMidiMusicView `midiView`

* CONSTRUCTOR:
    - Sets ActionListeners `onPlay`, `onPause`, and `onStop` for both the GUI view and 
      the MIDI view.
    - Sets the default functions using `setOnPlayAction`, `setOnPauseAction`, 
      `setOnStopAction`, and `setTimeGetter` (the latter using `getTimeMilli` from the 
      aforementioned IMidiMusicView interface)

* METHODS:
    - Implements methods from the IMusicView interface and delegates them to the GUI view 
      and MIDI view

---------------------------------
### NoteRectangle class

* ABOUT: The NoteRectangle class is for pairing a Rectangle object with a Color. 
         This class is used for drawing a single note. It helps decouple the painting 
         of the notes from the time of their creation.

* FIELDS:
    - `color`, the color of this rectangle
    - Inherits the following fields from the Rectangle2D class:
        - `x`, the top left corner X coordinate of the note rectangle
        - `y`, the top left corner Y coordinate of the note rectangle
        - `h`, the height of the rectangle in pixels
        - `w`, the width of the rectangle in pixels

* METHOD:
    - `getColor`, the getter-method for the only field not inherited by the
      Rectangle2D class

---------------------------------
### SongRenderPane class

* ABOUT: The SongRenderPane class is used to render a single ISong Graphically.
         It can be used as is, or as part of a larger project. The purpose of 
         this class is to pull the actual drawing of the songs into a single location 
         so that the rest of the GUIMusicView can be focused on managing the Swing 
         components for user interaction and ergonomics.

* FIELDS:
    - The following constants:
        - `NOTE_SIZE`, the size of the box for one note, which we have set to 20 pixels
        - `HIT_COLOR`, the color for a newly pressed note, which we have set 
           to an RGB value of 42/74/159
        - `HOLD_COLOR`, the color for a held note, which we have set to an RGB value 
           of 0/167/157
        - `LINE_COLOR`, the color of the lines separating each note row and beat column,
           which we have set to black
    - An ISong `song`      
    - An integer `borderXOffset`, making space in the view for the vertical column with
      the note names
    - An integer `borderYOffset`, making space in the view for the horizontal header with
      the beat number

* METHODS:
    - Implementation of `paintComponent` method from JComponent interface
    - `setSong`, which sets the selected song to the given song
    - `calculateSongRenderSize`, which calculates the size of the Panel that would be 
      needed to display the entire song. While this dimension will often be too large 
      reasonably display in a single window, it is useful for setting the preferred size 
      of the window. In general, the SongRenderPane is well suited to live inside of a 
      JScrollPane.
    - `drawLines`, which renders the lines to the given graphic until either X or Y 
      exceeds the given stopValue.
    - `renderSong`, which renders the song to the pane. This is the primary render point of 
      entry for the Graphical View. There are 4 steps to rendering a song which have been 
      broken out into subsequent helper methods:
        1. `renderBorder`, which renders the border of this song. This consists of the labels 
           for each of the pitches in the song, as well as the beat count at the beginning of 
           each measure. The border contains the names of each pitch, as well as indicators
           for the number of beats at the start of each measure.
        2. `renderNotes`, which renders all of the note rectangles. Each of the Notes is given 
           a Hit box that is the size of one beat. All subsequent beats in the notes duration 
           are drawn as a separate rectangle which is distinguished by its color.Next, the notes are rendered. 
           The notes are basically just rectangles. For specifics about how the notes are rendered, 
           see renderNotes, and the NoteRectangle class. 
        3. `renderMeasureLines`, which draws the measure lines in the view ever BPM number of beats, and
           `renderNoteLines`, which renders the lines to distinguish between each pitch. These lines 
           give the model a definite shape. The choice to render them last was made to avoid the note 
           rectangles overwriting the lines. 

---------------------------------
### NoteMakerPane class

* ABOUT: The NoteMakerPane class is the user-input interface for adding notes. 

* FIELDS:
    - HashMaps `instrumentMap` and `instrumentReverseMap`, which map a select number of 
      basic instrument names to their MIDI integer value (and vice versa); utilized in 
      `fillMaps` method
    - JSlider `volumeSlider`, which allows the user to change the volume property of a note
    - JComboBoxes `instrumentSelect` and `pitchSelect`
    - JTextFields `octaveInput`, `startInput` (as an integer start beat), and `durationInput` 
      (in integer beats)
    - JButton `addButton`, which adds the note with the above user-chosen properties to 
      the current song
    - JLabels `volumeLbl`, `octaveLbl`, `pitchLbl`, `startLbl`, and `durationLbl`, which provide 
      the visual labels for the respective input fields 

* METHODS:
    - `fillMaps`, which assigns a select number of basic instrument names to their 
      MIDI instrument value (and vice versa), utilizing the `instrumentMap` and 
      `instrumentReverseMap` fields
    - `setActionListener`, which takes an ActionListener and sets it on the `addButton`
    - `setNoteValues`, which sets the given note to the properties defined in the note 
      property fields (defined above)
    - Getter method `getNote`

---------------------------------
### KeyboardHandler class

* ABOUT: Takes the KeyListener and maps that key binding to the relevant functions.

* FIELDS:
    - HashMaps `typedDispatcher`, `pressedDispatcher`, and `releasedDispatcher`

* METHODS:
    - `registerKeyTypedHandler`, `registerKeyPressedHandler`, and `registerKeyReleasedHandler`, 
      which each take an integer keyCode and a Runnable and register them to their respective 
      dispatcher field handlers
    - Implementation of KeyListener's `keyTyped`, `keyPressed`, and `keyReleased` methods

---------------------------------
### TextView class

* ABOUT: Renders the text-based view of the model. TextView only supports rendering 
         the Song at index O of the given songList.

* FIELDS:
    - An appendable `ap`, allowing the TextView to be rendered in the console

* METHODS:
    - `notesAtBeat`, which creates an ArrayList of Notes that are playing at the given beat
    - `atOctave`, which uses the total rank of a note to return its integer octave
    - `pitchAt`, which uses the intra-octave rank to return the Pitch of a note
    - `noteStringAt`, a helper method that prints the string name of an individual note 
       using its total rank
    - `noteStringWithSpaces`, a helper method that "centers" the note name string in a string 
      of 5 characters
    - `beatLeadingSpaces` and `headerLeadingSpaces`, helper methods for the eventual printing 
      of a song
    - `renderHeader`, a helper method that prints a header for the console view of a song in the form
       of a string, ranging from the lowest to highest note occurring in the song. Below is an 
       example of its product:
               
                  C3  C#3   D3  D#3   E3   F3  F#3   G3  G#3   A3  A#3   B3

    - `renderMinusHeader`, a helper method that creates a console view of a song as a string, intentionally
       excluding the header (rendered by a separate helper method). Below is an example of its product:

               1  X                                  X                   X
               2                 X                             X         |
               3                      X         X         X
               4                                          |
               5                                          |
    - `render`, which combines the header and the headerless song strings to create a final console view of
      the given song. Below is an example of its product:
      
                      C3  C#3   D3  D#3   E3   F3  F#3   G3  G#3   A3  A#3   B3
                   1  X                                  X                   X
                   2                 X                             X         |
                   3                      X         X         X
                   4                                          |
                   5                                          |
                   6  X                                  X                   X
                   7                 X                             X         |
                   8                      X         X         X
                   9                                          |
                  10                                          |

---------------------------------
### ViewFactory class

* ABOUT: The ViewFactory class is a factory class for constructing a view object.

* METHODS:
    - `makeView`, a switch statement which takes a String describing either 
      a `console`, `visual`, or `midi` type of view, and uses helper methods 
      (`makeConsoleView`, `makeGuiView`, `makeMidiView`) to return the requested 
      view type

---------------------------------
## CONTROLLER PACKAGE

---------------------------------
### IMusicEditorController interface

* ABOUT: The IMusicEditorController is the controller for the MusicEditor, which negotiates 
         between the Model and the View. It is parameterized over a general type, because at 
         this level we may not know what kind of storage method is used in the model.

* METHODS:
    - `runEditor`, which run the editor with the given underlying music model and interacts with 
      the given view.

---------------------------------
### MusicEditorController class

* ABOUT: The MusicEditorController is the concrete controller class. This class takes a string as
         its parameter, as is done in the concrete model class.

* METHODS:
    - Implementation of `runEditor` from the interface
    
---------------------------------
## OTHER

---------------------------------
### MidiLoggingTest

* ABOUT: These tests are done to test that messages are being sent as expected to Midi. To show this,
         the receiver is mock to print the values instead of playing them.

* CLASSES:
    - Static `LoggingSequencer` class, which pretends to be a Sequencer and logs all of the MIDI messages
        - The majority of the methods in this class are not overridden in our Sequencer implementation, so
          if they are called, something is broken. These unsupported methods throw an IllegalArgumentException
          if called.

* METHODS:
    - The `main` method that executes these tests.

---------------------------------
### MusicEditor

* ABOUT: The MusicEditor class is home to the main method that runs our program.
