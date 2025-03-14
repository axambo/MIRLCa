// Latest update: 8.3.2025

MIRLC 2.0
===
SuperCollider extensions for using MIR techniques in live coding.

(c) 2019-2025 by Anna Xambó Sedó (<a.xambosedo@qmul.ac.uk>).


Introduction
----

The extension `MIRLC2.sc` is a follow-up work-in-progress of `MIRLC.sc`. It includes effects and more automatic behaviours. It is still under development.


Application Start
----

Drag the `MIRLC2` folder to the Extensions folder of SuperCollider (suggested to create a subfolder with the same name): `/Users/{username}/Library/Application Support/SuperCollider/Extensions` (in Mac). If you are unsure where is this folder, open SuperCollider and type: `Platform.userExtensionDir`

Either recompile the class library (`Language>Recompile Class Library`) or restart SuperCollider.

MIRLCRep2 Module
----

The `MIRLCRep2` class is designed for repurposing audio samples from Freesound.org using and expanding the Freesound quark for SuperCollider.

### Requirements

* Make sure you have an Internet connection.
* Make sure you have a [Freesound.org](http://freesound.org) account.
* Make sure to obtain an [API key](https://freesound.org/help/developers/).
* Install [Freesound quark](https://github.com/g-roma/Freesound.sc), which is a SuperCollider client for accessing the Freesound API and operate with sounds from Freesound.org.
* In order to connect with Freesound.org, the type of authentication used in MIRLC is Token. Make sure to introduce your API key in the class `MIRLC2.sc` and recompile the class library.

### Additions to the Freesound quark

* Asynchronous management of multiple sounds by a single query.
* User-friendlier queries by content, similarity, tag, filter, and sound id.
* A new user-friendly query by random.
* A new architecture of groups of sounds with user-friendly functions for playing them in sequence and in parallel, which are managed asynchronously.
* A new set of functions to control both individual sounds and group of sounds (e.g., play, stop, mute single sounds, solo single sounds).
* Retrieval of sounds avoiding repetition in queries by content and similarity.
* Retrieval of sounds avoiding inexistent results in random queries.
* A customisable text file that prints the sounds used by title and username.

### Additions to MIRLCRep.sc

* Audio effects.
* Automatic functions.
* Solved some bugs.
* Reorganisation of the code in files.

MIRLCRew2 Module
----

The `MIRLCRew2` class is designed for rewiring an audio in signal as both a control signal or audio signal using MIR techniques in SuperCollider.

### Requirements

* Make sure you have either a microphone connected or an audio file.
* Input audio files work in mono.

### Additions to MIRLCRew.sc

* Audio effects.
* Solved some bugs.
* Reorganisation of the code in files.


License
----

BSD 3-Clause License.
