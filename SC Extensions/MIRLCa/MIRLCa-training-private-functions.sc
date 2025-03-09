+ MIRLCa {

    //------------------//
	// SHOW TO THE USER INFO ABOUT THE LATEST ANNOTATION
    //------------------//
	// Training mode: Private function to show info of the latest information and the dataset

	updatedict { |value|
			postln("********************************************");
		    postln("Sound annotated as " ++ value);
			postln("You have " ++ manual_dataset_dict.size ++ " sounds in your dataset");
			postln("The sound IDs are: "++manual_dataset_dict.keys);
			postln("********************************************");
	} //-//

	//------------------//
    // RETURN A RANDOM SOUND
    //------------------//
	// Training mode: Private function that returns a random sound for annotation.

    giverandomsound {

		/*if (sndid_old_t.notNil && (sndid_t == sndid_old_t), {
			"Fading out the previous sound...".postln;
			this.fadeout_t;
		});*/

		// "then give a random sound".postln;
		// "its descriptors are stored in a temp array".postln;

		sndid_t = rrand (1, databaseSize);
		// sndid_t = 329706; // This SoundID is for testing "analysis":null

		this.getsoundfromfreesound (sndid_t);


    } //--//

	//------------------//
    // RETURN A SOUND BY ID
    //------------------//
	// Training mode: Private function that returns a sound by ID for annotation.

	givesoundbyid { | id = 3333 |


		/*if (sndid_old_t.notNil && (sndid_t == sndid_old_t), {
			"Fading out the previous sound...".postln;
			this.fadeout_t;
		});*/

		// "then give a sound by ID".postln;
		// "its descriptors are stored in a temp array".postln;

		sndid_t = id;
		// sndid_t = 329706; // This SoundID is for testing "analysis":null

		this.getsoundfromfreesound (sndid_t);

    } //--//

    //------------------//
    // ANALYSES A SOUND DOWNLOADED FROM FREESOUND
    //------------------//
	// Training mode: Private function used to store MFCCs values of a downloaded sound required for the training of the machine learning model.

	getsoundfromfreesound { |sndid_t = 3333 |

		backendClass.getSound ( sndid_t,
            { | f |


				var snd_t;

                snd_t = f;

				if ( snd_t.isNil,
					{
						postln("ERROR: There was a problem downloading this sound.\nWARNING: Try another sound.");
						this.messagestraining;
					},
					{
					// "INFO: The sound has been downloaded".postln;

					/*if ( snd_t.dict != nil,
						{ postln("INFO: The dict exists in Freesound" )},
						{ postln("INFO: The dict DOES NOT EXIST in Freesound") }
					);*/

					// if "analysis":null
					if (snd_t["analysis"].isNil) {
						postln("ERROR: Sound analysis does not exist.\nTry another sound.");
						this.messagestraining;
					};

					if (snd_t["detail"]=="Not found.", {
						postln("ERROR: Sound not found in the database.\nTry another sound.");
						this.messagestraining;
					});

					if ( snd_t["detail"] != "Not found." && snd_t["analysis"].notNil,
                    {
						this.prid(sndid_t, 1);

						temp_list_training = List.fill(28,0);
						temp_list_training[0] = sndid_t;

						snd_t.getAnalysis("lowlevel.mfcc", { | val |

							if ( (val.lowlevel.mfcc.mean[0] == 0) || (val.lowlevel.mfcc.mean[1] == 0) || (val.lowlevel.mfcc.mean[2] == 0) || (val.lowlevel.mfcc.mean[3] == 0) ,
								{ postln("WARNING: At least two MFCCs retrieved zero, this sound should be skipped. Please annotate it as [namevariable].skip");
							});

							temp_list_training[2] = val.lowlevel.mfcc.mean[0];
							temp_list_training[3] = val.lowlevel.mfcc.mean[1];
							temp_list_training[4] = val.lowlevel.mfcc.mean[2];
							temp_list_training[5] = val.lowlevel.mfcc.mean[3];
							temp_list_training[6] = val.lowlevel.mfcc.mean[4];
							temp_list_training[7] = val.lowlevel.mfcc.mean[5];
							temp_list_training[8] = val.lowlevel.mfcc.mean[6];
							temp_list_training[9] = val.lowlevel.mfcc.mean[7];
							temp_list_training[10] = val.lowlevel.mfcc.mean[8];
							temp_list_training[11] = val.lowlevel.mfcc.mean[9];
							temp_list_training[12] = val.lowlevel.mfcc.mean[10];
							temp_list_training[13] = val.lowlevel.mfcc.mean[11];
							temp_list_training[14] = val.lowlevel.mfcc.mean[12];
							temp_list_training[15] = val["lowlevel"]["mfcc"]["var"][0];
							temp_list_training[16] = val["lowlevel"]["mfcc"]["var"][1];
							temp_list_training[17] = val["lowlevel"]["mfcc"]["var"][2];
							temp_list_training[18] = val["lowlevel"]["mfcc"]["var"][3];
							temp_list_training[19] = val["lowlevel"]["mfcc"]["var"][4];
							temp_list_training[20] = val["lowlevel"]["mfcc"]["var"][5];
							temp_list_training[21] = val["lowlevel"]["mfcc"]["var"][6];
							temp_list_training[22] = val["lowlevel"]["mfcc"]["var"][7];
							temp_list_training[23] = val["lowlevel"]["mfcc"]["var"][8];
							temp_list_training[24] = val["lowlevel"]["mfcc"]["var"][9];
							temp_list_training[25] = val["lowlevel"]["mfcc"]["var"][10];
							temp_list_training[26] = val["lowlevel"]["mfcc"]["var"][11];
							temp_list_training[27] = val["lowlevel"]["mfcc"]["var"][12];

							// temp_list_training.postln;

						}, true);

						// "INFO: sound descriptors are downloaded".postln;
						sndid_old_t = sndid_t;

                    },
                    {
						postln("ERROR: Either SoundID or sound analysis does not exist.\nWARNING: Try another sound.");
						this.messagestraining;
					}); // End IF getanalysis
				}); // End of IF snd_t.isNil
        }); // End of backendClass.getSound
	} //-//

	//------------------//
    // PRINT MESSAGES TO USER
    //------------------//
	// Training mode: Private function that reminds the user about available methods for training

	messagestraining {

		if (is_training==true, {

			postln("********************************************");
			"You are in training mode.".postln;
			"You need to request for a new sound and then tell if you like it or not.".postln;
			postln("(1)");
			"To get a new random sound...".postln;
			"Please write: [namevariable].trainrand".postln;
			postln("(2)");
			"To get a new sound by ID...".postln;
			"Please write: [namevariable].trainid(xxxx)".postln;
			"where you need to replace xxxx with the Freesound ID number".postln;
			"Please wait until the sound has been downloaded before manually annotating it...".postln;
			"If you like the sound, please write: [namevariable].ok".postln;
			"If you don't like the sound, please write: [namevariable].ko".postln;
			"If you want to skip the sound, please write: [namevariable].skip".postln;
			postln("********************************************");
		});

	}

    //------------------//
    //
    //------------------//

	fadeout_t { |release = 1.0|

		this.fadeout;
	} //-//

    //------------------//
    // RETRIEVE SOUNDS
    //------------------//
    // Training mode: Private function that has been modified to only deal with one sound at a time for the training.
	// It manages the dictionary metadata (sounds with Freesound sound info) and loads the new sound.
    // It stores a group of one sound that is stored in index 1.

    loadmetadata_t { |totalsnds = 1|
        totalsnds.do ({ |index|
            this.loadsounds(metadata, index);
        });
		this.printmetadata;
    } //-//

}
