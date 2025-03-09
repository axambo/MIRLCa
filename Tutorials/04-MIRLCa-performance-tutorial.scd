// Latest update: 8.12.2022

// Author: Tutorial adapted from the MIRLCa Help file and live demoed by Anna Xambó during the 2021 MIRLCa workshops and updated by Anna Xambó for the ZKM Hacklab in 2022

// If you would like to see all the methods at once, click command+D next to the MIRLCa instruction

MIRLCa


// DIRECTORIES

// MIRLCa requires three directories, one for downloads, another for credits and a third folder for models.


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

// Where would you like to save/load the files of your model?

( // MOLDELS DIRECTORY
FileDialog({ |paths|
    postln("Selected paths:");
    paths.do(_.postln);
	~models = paths[0]++"/";
    }, fileMode: 0);
)

////////////////////////

// Alternatively to the above popups you can do it manually, for example:

(
~downloads = "/Users/anna/Desktop/MIRLCa-downloads/"; 	// Change accordingly
~credits ="/Users/anna/Desktop/MIRLCa-credits/";		// Change accordingly
~models ="/Users/anna/Desktop/MIRLCa-default-models/";			// Change accordingly
// and create the directories if they don't exist
File.mkdir(~downloads); File.mkdir(~credits); File.mkdir(~models);
)

////////////////////////

// MODELS

// Make sure to locate the following three files in the "models" folder:
// "model.JSON"
// "pca.JSON"
// "standardizer.JSON"
// If you don't have them, you can find them here inside the provided folder "models".
// Copy them and put them in your "models" folder


////////////////////////

// MIRLCa instantiation

a = MIRLCa.new(path: ~downloads, creditsPath: ~credits, modelsPath: ~models);

//  Let's retrieve one sound by tag

a.tag("london")

// Get a similar sound, it will pick the first "good" sound from a list of 10 candidates

a.similar // a.similar is the same than a.similar(0)

// As it currently stands, the similar function is constrained to have 10 candidates. You may find errors in the post window due to situations not matching the conditions... will fix them as soon as possible!


// Let's filter one random sound. Please note this can take some time to execute!

a.random

// You can request another sound using the same tag

a.tag("london")

// You can still get a sound by ID (target sound), this option skips the filter for now

a.id(3333)

// There might be target sounds with no similar_sounds information
// It will give an error because without similar_sounds information and other analysis information, MIRLCa cannot retrieve the sound

a.id(329706)
a.id(327984)


a.similar

a.mute(0) // Mutes sound on index 0

a.solo(1) // Solo of sound on index 1

a.play // Let's play all again

a.similar(1)

// You can build a step-by-step similarity group..

a.similar(2)

// Once you have a sound cloud, you can explore applying effects to the group...

a.lowpf

// Or bypassing the effect...

a.bypass

// You can try other effects...
a.delay
a.highpf
a.bandpf
a.vibrato
a.reverb
a.bitcrush
a.distort
a.rmod
a.compress

// Change the speed...

a.play(10)
a.play(0.5)
a.play(-0.5)

// Go back to normal...

a.play

// And fadeout the group...

a.fadeout(10)

// ....

// Also, you can instantiate more than one instance of MIRLCa pointing to different models. You should point to different folders and diferent variable names.


(
~downloads2 = "/Users/anna/Desktop/MIRLCa-downloads2/"; 	// Change accordingly
~credits2 ="/Users/anna/Desktop/MIRLCa-credits2/";		// Change accordingly
~models2 ="/Users/anna/Desktop/MIRLCa-models2/";			// Change accordingly
// and create the directories if they don't exist
File.mkdir(~downloads2); File.mkdir(~credits2); File.mkdir(~models2);
)


b = MIRLCa.new(path: ~downloads2, creditsPath: ~credits2, modelsPath: ~models2);

b.random

b.similar

b.fadeout(10)
