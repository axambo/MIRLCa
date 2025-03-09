// Latest update: 8.12.2022

// Author: Tutorial adapted from the MIRLCRep2 help and live demoed by Sam Roig during the 2021 MIRLCa workshops and updated by Anna Xambó for the ZKM Hacklab in 2022

// MIRLCRep2
//
// This class is an incremental improvement over MIRLC.
// It provides improved querying and some automatic methods.
// And it can also apply effects to the downloaded sounds.
//
// It is is still somehow buggy, especially the sequence method.
//
// Besides this document, or looking at the source, you can also refer to
// the earlier MIRLC tutorial.
//
// This recording of a performance by Anna Xambó can also serve you as a guide.
// https://vimeo.com/439543308

// If you would like to see all the methods at once, click command+D next to the MIRLCRep2 instruction

MIRLCRep2

// MIRLC requires two directories, one for downloads and the other for credits.

// Where would you like to download the sounds from Freesound?

( // DOWNLOADS DIRECTORY
FileDialog({ |paths|
    postln("Selected paths:");
    paths.do(_.postln);
	~downloads = paths[0]++"/";
    }, fileMode: 0);
)
x

// Where would you like to store the credits file of the sounds downloaded?

( // CREDITS DIRECTORY
FileDialog({ |paths|
    postln("Selected paths:");
    paths.do(_.postln);
	~credits = paths[0]++"/";
    }, fileMode: 0);
)

////////////////////////

// Alternatively to the above popups you can do it manually, for example:

(
~downloads = "/Users/anna/Desktop/MIRLC-downloads/"; 	// Change accordingly
~credits ="/Users/anna/Desktop/MIRLC-credits/";		// Change accordingly
// and create the directories if they don't exist
File.mkdir(~downloads); File.mkdir(~credits);
)

////////////////////////

// Let's create a instance of MIRLCRep2 and assignt it to variable
a = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);

// Now you can start querying sounds from Freesound!

// Search by tag (just like with MIRLCRep)
a.tag("prayer");

a.stop
a.play	// args: |rate=1| (can be negative)
a.play(-1)	// args: |rate=1| (can be negative)
a.play(1)	// args: |rate=1| (can be negative)
a.play(-0.5)	// args: |rate=1| (can be negative)

// You can add a second sound in the group
a.tag("drone")

// You have now two sounds in the group, positioned at indexes 0 and 1
// You can control indvidual sounds using their index

a.mute(0)	// this mutes the sound at index 0; args: |targetnumsnd=0|
a.play		// this plays all the sounds back; args: |targetnumsnd=0|
a.solo(0)	// solo sound index 0; args: |targetnumsnd=0|
a.solo(1)	// solo sound index 1; args: |targetnumsnd=0|
a.play		// play them all

// Some useful functions to know what we're dealing with considering that we are retrieving sounds from Freesound and can get some characteristics:
a.whatid;  // ID
a.whatkey; // Key
a.pitch;   // Mean Pitch (Hz)
a.whatbpm; // BPM (beats per minute)
a.whatvol; // Master Volume
a.whatdur; // Duration (in secs)

// The following function retrieves all content-based descriptors listed in the  Analysis Descriptor
// Documentation from the FreeSound API:
// "https://www.freesound.org/docs/api/analysis_docs.html#analysis-docs"
// The result can be filtered using the descriptors request parameter passing a list of comma separated descriptor names chosen from the available descriptors e.g. 'descriptors=lowlevel.mfcc,rhythm.bpm'
// args: |descriptors, action|
a.analyze;
a.analyze('descriptors=lowlevel.mfcc,rhythm.bpm')

a.fadeout(10) // you can fade out this group (the parameter defines release time in seconds)

// Create another instance of MIRLCRep2 to put other sounds in it
b = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);
b.id(279086); 		// Get a sound by id
b.whatid;     		// Use this to take note of sounds you like.
b.similar(0, 2); 	// You can retrieve 2 similar sounds to the sound position at index 0

// Playback controls
b.sequence // sequential playback, one sample in the bucket after another (Warning: this is very buggy, so please avoid to use for now!)
b.parallel // parallel playback, all at the same time
// workaround: Always go back to parallel to avoid an existing bug with sequence and trying certain operations or downloading new soudns

b.solo(1)
b.muteall

// QUERY BY CONTENT
// This function gets [1..n] sounds by one defined feature and fx, and plays them
b.content(1, 'dur', 0.5) // args: { |size = 1, feature = 'dur', fvalue = 1, fx = 'conf', fxvalue = 'bypass' |
b.content(1, 'pitch', 110)

b.play	// let's play them all


// FUNCTIONS TO KEEP ADDING SOUNDS
// This function gets [1..n] similar sounds from a target sound filtered by a fx

// This function gets [1..n] similar sounds from a target sound, usually the first sound from the dictionary
b.similar(0, 2)  // args: | targetnumsnd = 0, size = 1 |
b.filter(0, 4, "key", 'A'); // args: targetnumsnd = 0, size = 1, fx = 'conf', fxvalue = 'bypass'
b.filter(0, 4, "bpm", 70)
b.sequence
b.parallel

b.fadeout(4)


// Create another instance of MIRLCRep2 to put other sounds in it
c = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);
c.tag("karlsruhe")

// AUTOMATIC FUNCTIONS
// Use the following functions carefully: don't execute them simultaneously (let the process end).
c.similarauto(0, 4, 10)  // args: |targetnumsnd = 0, size = 3, tempo = 10|

// playauto changes speed and reverses the samples randomly
c.playauto     		// args: |times = 4, tempo = 30|
c.playautodown; 	// Varispeed-like playback. args: |start = 1, end = 0, times = 5, tempo = 10|
c.autochopped(32,1);  // Chop the audio and play some fragments backwards. args: |times = 4, tempo = 1|

c.sequence;
c.parallel;

c.fadeout(10);

// A new instance for random sounds
z = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);
z.random(4); // Get 4 random sounds
// wait... :)

z.sequence;  // Listen to them in sequence to lessen the (highly likely) chaos
z.parallel;

// More often than not, random search is probably not the best strategy...
z.stop;      // Get some rest.



// Let's create a instance of MIRLCRep2 and assignt it to variable
a = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);

// Now you can start querying sounds from Freesound!

// Search by tag (just like with MIRLCRep)
a.tag("prayer")

// Let's see what's the mean pitch of the 'prayer' file is.
a.whatpitch; // Take a look at the post window: 229.90976804768104 Hz

// Let's search for another sound with the same pitch and store it in b
// Ideally we'd query by key but this functionality is not implemented yet.
a.pitch(1,229,'conf','hi');

// Not quite what I was looking for...
a.mute(1);

// Possibly the call for prayer is in sung in A
// The offset in 9Hz is possibly an artifact of averaging the pitch of the melody.
// Let's try with 220 Hz...
a.pitch(1,220,'conf','hi');

// Better!

a.pitch(1, 880 * 3/2, 'conf', 'hi'); // a fith, a couple of octaves above
a.stop

// Let's add a bit of rhythm:
a.whatbpm // Check the bpm of the original file: 139.6686541180513

// create a third instance of MIRLCRep2 for the rhytmic part
d = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);
d.tag("beat", 4); // Let's search for a beat. We'll audition 4.
d.muteall; // Let's mute them all, then audition one by one.
d.solo(2);

// Dissonance
d.diss(1,fxvalue:'hi') // args: |size = 1, fvalue = 1.0, fx = 'conf', fxvalue = 'lo'|

// Let's search a similar sound to the call for prayer
d.similar(1, 2)

d.whatkey
d.key

// We can narrow our search with additional tags like so:
d.tag("water" + "drop");

d.whatpitch
d.whatbpm

d.similar(0, 2, 108)
d.sequence
d.parallel

// new instance
e = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);
// Search by tag (just like with MIRLCRep)
e.tag("prayer");
e.filter(0, 4, "key", 'A');
e.filter(0, 4, 'bpm', 220); // args: targetnumsnd = 0, size = 1, fx = 'conf', fxvalue = 'bypass'

// We can also narrow our search with additional tags.
// Let's mute a for now and create a new instance.
e.muteall;
e.fadeout;

f = MIRLCRep2.new(path: ~downloads, creditsPath: ~credits);

(
var r;
r = Routine {
    10.do({
        f.random;
        // Often you might see Wait being used to pause a routine
        // This waits for one second between each number
        1.wait;
    });
    // Wait half second before saying we're done
    0.5.wait;
    "done".postln;
}.play;
)
f.fadeout(4);


// You can also apply FX to any of the sounds
a.play 				// back to the first sound
a.bypass 			// bypass the FX
					// This function changes the volume of the whole group from 0 to 1.
a.volume(0.5); 		// args: |vol = 0.2|
					// This function fades out all synths with a smooth fade out
a.fadeout;    		// args: |release = 1.0|
					// MIRLC2 provides some simple built-in FX (consider adding yours!)
a.delay
a.lowpf
a.highpf
a.bandpf
a.vibrato
a.reverb
a.bitcrush
a.distort
a.rmod
a.compress


// VISUALIZATION, PRINTING
// FUNCTIONS: scope, plotserver, printmedata, printsynths, printbuffers, printall
a.credits // prints the sound credits on screen
a.scope
a.plotserver
a.info;       // This function prints the FS metadata information for all downloaded sounds
a.printmedata // This function prints the FS metadata information for all downloaded sounds
a.printsynth // This function prints the synth information and associated FS metadata information of the current active sound
a.printsynths // This function prints the synths information and associated FS metadata information for all the active sounds
a.printbuffers // This function prints the buffers information and associated FS metadata information for all downloaded sounds
a.printall // Print everything (metadata, synths, buffers)


