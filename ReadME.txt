README:

This MusicModel implements the IMusicModel<K> interface, the K is a ITone interface which supports
values for a MIDI style note, a start beat, a duration, a volume, and instruments. The Model
can read in a composition in the format of a text file with the tempo at the top of the file, and
every line thereafter, populated with the note string and 5 integer values separated by
spaces. IMusicModel interface supports the methods to add, remove, combining simultaneously and
consecutively, displaying the tones in a textual representation, getting a list of
all the tones in a song, getting all the tones that start at a given beat and tones that
sustain at a given beat, a tempo setter and getter, and a method to create a list of the lowest
tone played up to the highest with all the tones in between.
Changelog:

Updated Tones so that a constructing a tone with a duration of 0 no longer throws exceptions in
order to play one of the composition txt files with 0 duration notes.

Added instruments and volume support for tones so that these values are not just thrown away by
the builder.

Moved a few informational methods that certain views needed to the interface level.

Added a tempo field to the model and an interface level getter, as this is a value that the
text files provide and is used by the MIDI.

Modified the Tone class in the model to throw exceptions if invalid values are given for volume &
instruments.

Added a new interface (ReadOnlyModel), and had IMusicModel<K> extend this interface, the reason
is to hide the MusicModel behind this new interface and declare only informational methods in it
so that the views that get a model declared as ReadOnlyModel cannot access any of the
mutation methods from IMusicModel. This was to inhibit the mutation of the model  within the view
as this is the job of the controller.

11/10
Switched to a timer to represent a create notion (MIDI no longer sends all messages at initialize
but sends them as needed), this was in order to pause and play the playback as well as sync the
GUI to the current beat being played.

11/11
Updated tests to reflect the timer.

11/13
Made an abstract controller class extended by different controllers that have different
functionality depending on the viewtype.

11/14
Added the edit method to the IMusicModel interface, implemented it in the ConcreteMusicModel and
wrote a test in the TestModel class.

11/16
Threw out the controller factory and instead, added a getController method to the view interface
which instantiates a controller based on the view invoking it.

11/18
Modified the GuiViewFrame, refactored the DisplayPanel to be an inner class of it and made the
rendering more efficient.

11/29
Modified durations to be more accurate by changing the derived calculation of duration from
(start - end) to ((start - end) + 1). Also had to compensate for this in the GuiViewFrame by
subtracting an additional 1 from the current tone's duration value in the drawSustainNote method
located in the ConcreteGuiViewPanel. Additionally, I modified the constructor in the tone to
throw an exception if a tone was created with a duration < 1 instead of < 0.

11/30
Updated the GuiViewFrame's auto scrolling function.

12/1
Modified the controller factory

12/8
Added a new method to the IMusicModel & ReadOnlyModel interfaces, getTone(K tone), viewTone(K
tone), respectively. The purpose of this was to be able to implement the new functionality of
viewing information on a tone that is selected in the GUI.

TESTING:
the View Factory has a testing method and a regular method. The testing method when invoked with
a StringBuilder in addition to the params that the initialize view takes, will append
note or MidiMessage data to the StringBuilder instead of its regular behavior.

USER MANUAL:
This music editor supports playing a composition, adding notes, editing notes, removing notes,
getting information on notes, pausing, playing, skipping to the end, skipping to the start,
scrolling with the keys, and changing the tempo of the piece.

PLAYING/PAUSING:
To pause, press the "p" key and to play, press the "o" key.

ADD TONE:
To add a Tone, you must be paused, then press the "a" key to switch the state into add mode, then
click and drag on the panel, you will then be prompted for the instrument and the volume
respectively. If you enter valid values for these, the note will be created and the panel will
reflect this change to the model.

REMOVE TONE:
To remove a Tone, you must be paused, then press the "delete" key to toggle the state into delete
mode, then click on the start beat of any note to delete it. Press the "delete" key again to
toggle off the delete mode once you are done. The panel will reflect any changes to the model.

EDIT TONE:
To edit a Tone, you must be paused, then press the "e" key to enter edit mode, then click on the
start beat of any note to highlight it. The editor will then switch over to add mode,
successfully add a tone and the highlighted tone will be deleted, if unsuccessful, the
highlighted note remains in place.

CHANGE TEMPO:
To change the tempo, you must first be paused, then press the "t" key and input a new tempo in
the window that comes up.

TONE INFORMATION:
To get information on a tone, you must be paused, then press the "i" key to enter information
mode, then click on the stat beat of any note to view its sound, starting beat, duration,
instrument, and volume values.

JUMP TO:
While paused, press the comma key to jump to the start, or the period key to jump to the end.

KEY SCROLL:
UP, DOWN, LEFT, RIGHT increments the scroll in each direction respectively.