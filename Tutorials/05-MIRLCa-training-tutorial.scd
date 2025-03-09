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
// In this "models" folder is where you will save your machine learning model in the form of three files when training
FileDialog({ |paths|
    postln("Selected paths:");
    paths.do(_.postln);
	~models = paths[0]++"/";
    }, fileMode: 0);
)

////////////////////////

// Alternatively to the above popups you can do it manually, for example:

(
~downloads = "/Users/anna/Desktop/MIRLCa-training-downloads/"; 	// Change accordingly
~credits ="/Users/anna/Desktop/MIRLCa-training-credits/";		// Change accordingly
~models ="/Users/anna/Desktop/MIRLCa-default-models/";					// Change accordingly
// and create the directories if they don't exist
File.mkdir(~downloads); File.mkdir(~credits); File.mkdir(~models);
)

////////////////////////

// MODELS

// Make sure to locate the following three files in the "models" folder.
// If you don't have them, you can find them here inside the folder "models": https://tiny.cc/hacklab
// Copy them and put them in your "models" folder

// "model.JSON"
// "pca.JSON"
// "standardizer.JSON"

////////////////////////

// MIRLCa instantiation

a = MIRLCa.new(path: ~downloads, creditsPath: ~credits, modelsPath: ~models);

// To start training, you need to tell your intention

a. starttraining

// This tells MIRLCa that you are in training mode.

// You need to request for a new sound and then tell if you like it or not.
// There are two ways of requesting sounds:

// Option 1: To get a new random sound... Please write: [namevariable].trainrand
// Option 2: To get a new sound by ID... Please write: [namevariable].trainid(xxxx)
// where you need to replace xxxx with the Freesound ID number
// Please wait until the sound has been downloaded before manually annotating it...

// You can either train by ID or by random or by a combined approach of random and by ID

// To request a random sound for training purposes...

a.trainrand

// Please wait, the sound might be very long!
// When you query for a sound, you need to wait until the sound is dowloaded, and then you need to execute the methods 'ok' or 'ko' depending on whether you like the sound or not.

// If you like the sound, execute this command

a.ok // ok or ko?

// If you don't like the sound, execute this command

a.ko

// If you want to skip that sound from the classification, just skip it

a.skip

// Once you annotate the sound, you will need to query for another sound. You can request another random sound by executing `x.trainrand` or you can try requesting a sound by ID

a.trainid(3333)

// You can keep going until you get at least 20-30 sounds.
// Either use a.trainrand or a.trainid(xxxx)
// For example you could have a list of sounds by ID

a.trainid(3334); // ok or ko?
a.trainid(3335); // ok or ko?
a.trainid(3336); // ok or ko?
a.trainid(3337); // ok or ko?
a.trainid(3338); // ok or ko?
a.trainid(3332); // ok or ko?
a.trainid(3339); // ok or ko?
a.trainid(4334); // ok or ko?
a.trainid(4335); // ok or ko?
a.trainid(4336); // ok or ko?
a.trainid(4337); // ok or ko?
a.trainid(4338); // ok or ko?
a.trainid(4339); // ok or ko?

// Make sure there's a good representation of good and bad sounds!

// To check how are you doing, you can see what's the accuracy of your model

// You will need to stop training. The post window will tell you how are you doing.

a.stoptraining

// If you want to continue training, just execute the following instruction

a.continuetraining

// Else, if you want to stop training and save your files, execute the following instruction

a.save

// Notice that three files have been generated in your "models" folder:

// model-new.JSON
// pca-new.JSON
// standardizer-new.JSON

// To use them, you will need to remove the suffix "-new"

// Once you train your machine learning model, you will change the three files in the "models" folder. Make sure to rename the JSON files to the expected names:

// "model-new.JSON" => "model.JSON"
// "pca-new.JSON" => "pca.JSON"
// "standardizer-new.JSON" => "standardizer.JSON"
