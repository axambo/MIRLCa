+ MIRLCa {

    //------------------//
    // START TRAINING
    //------------------//
	// Training mode: Public function used to start the training.

	starttraining { | mode = "random" |

		mode_training = mode;
		if ( is_training == false, {
			is_training = true;
			// "start training".postln;
		}, {
			"INFO: You are in training mode already".postln;
		});
		/*if (sndid_old_t.notNil && (sndid_t == sndid_old_t), {
				"Existing sound fading out...".postln;
				this.fadeout_t;
		});*/

		this.selectanswerbymode();

	} //--//

    //------------------//
    // TRAIN BY ID
    //------------------//
	// Training mode: Public function to request a sound by ID to be annotated.

	trainid { |idnumber = 3333 |

		if ( is_training == true, {
			this.givesoundbyid(idnumber);
			postln("********************************************");
			"Please wait until the sound has been downloaded before manually annotating it...".postln;
			postln("********************************************");
		}, {
			"INFO: You should call the method 'starttraining' or 'continuetraining' first".postln;
		});

	} //-//

    //------------------//
    // TRAIN BY RANDOM
    //------------------//
	// Training mode: Public function to request a random sound to be annotated.

	trainrand { //|idnumber = 3333 | // TODO: DELETE! & UPDATE HELPFILE

		if ( is_training == true, {
			this.giverandomsound();
			postln("********************************************");
			"Please wait until the sound has been downloaded before manually annotating it...".postln;
			postln("********************************************");
		}, {
			"WARNING: You should call the method 'starttraining' or 'continuetraining' first".postln;
		});

	} //-//

    //------------------//
    // USER MENU TO SELECT THE TRAINING MODE
    //------------------//
	// Training mode: Private function that shows the user the available options for training.

	selectanswerbymode {

		this.messagestraining;

		// DEACTIVATED:
		/*if ( mode_training == "random", {
			// Deactivated the automatic random mode until Freesound gets fixed
			/*this.giverandomsound();
			postln("********************************************");
			"Please wait until the sound has been downloaded before manually annotating it...".postln;
			postln("********************************************");*/
		}, {
			if ( mode_training == "id", {
			postln("********************************************");
			"For training by ID, please write: trainid(xxxx) where you need to replace xxxx with the id number".postln;
			postln("********************************************");
			});
		};
		);*/
	} //-//

    //------------------//
    // CONTINUE TRAINING
    //------------------//
	// Training mode: Public function to continue training in case it has been paused or stopped

	continuetraining { | mode |
		is_training==true;
		this.starttraining( mode_training );
	} //-//

    //------------------//
	// LABEL SOUND AS 'GOOD'
    //------------------//
	// Training mode: Public function to annotate the present sound as a 'good' sound

	ok {

		if (is_training==true, {
			temp_list_training[1] = "good";
			// "sound labeled as good".postln;
			temp_list_training.postln;
			// "save sound (temp array) in a dictionary (global variable) with label good".postln;
			manual_dataset_dict.add(sndid_t.asInteger -> temp_list_training); // dictionary key = ID; dictionary value = y array
			this.updatedict("'ok'");
			this.fadeout_t;
			this.selectanswerbymode();
		}, {
			"You need to start training first".postln;
		});


	} //--//

    //------------------//
	// LABEL SOUND AS 'BAD'
    //------------------//
	// Training mode: Public function to annotate the present sound as a 'bad' sound

	ko {

		if (is_training==true, {
			temp_list_training[1] = "bad";
			// "sound labeled as bad".postln;
			temp_list_training.postln;
			// "save sound (temp array) in a dictionary (global variable) with label good".postln;
			manual_dataset_dict.add(sndid_t.asInteger -> temp_list_training); // dictionary key = ID; dictionary value = y array
			this.updatedict("'bad'");
			this.fadeout_t;
			this.selectanswerbymode();
		}, {
			"You need to start training first".postln;
		});

	} //--//

	    //------------------//
	// SKIP TO LABEL THIS SOUND
    //------------------//
	// Training mode: Public function to skip adding the present sound in the training
	skip {

		if (is_training==true, {
			postln("INFO: Removing this sound from training.");
			this.messagestraining();
			this.fadeout_t;
		}, {
			postln("INFO: There's no sound to skip.\nYou need to start training first.");
		});


	} //--//

    //------------------//
	// SHOW INFO OF THE DATASET
    //------------------//
	// Training mode: Public function to show status information about the dataset in the training mode.

	showinfo {
		var goodsounds;
		var badsounds;
		var dataset = manual_dataset_dict;
		if ( is_training==true , {
			postln("INFO: You are in training mode.");
			postln("********************************************");
			postln("You have " ++ dataset.size ++ " sounds in your dataset");
			postln("The sound IDs are: "++ dataset.keys);
			postln("The values are: "++ dataset.values);
			dataset.values.select({ |x| x.postln; true});
			goodsounds = dataset.values.select({ |x| x[1]=="good"}).size;
			postln("Good sounds: " ++ goodsounds);
			badsounds = dataset.size - goodsounds;
			postln("Bad sounds: " ++ badsounds);
			// dataset.values.select({ |x| x[1]=='bad'}).postln;
			/*manual_dataset_dict.keysValuesDo{ |k,v|
				postln("key: "++k);
				postln("value: "++v);
				postln("value: "++v[1]);
				goodsounds = v[1].select({ |x| x[0]=='good'}).size;
				postln("Good sounds: " ++ goodsounds);
				badsounds = manual_dataset_dict.size - goodsounds;
				postln("Bad sounds: " ++ badsounds);
			};*/
			// goodsounds = manual_dataset_dict.select({ |x| x[0]=='good'}).size;
			// postln("Good sounds: " ++ goodsounds);
			// badsounds = manual_dataset_dict.size - goodsounds;
			// postln("Bad sounds: " ++ badsounds);
			postln("********************************************");
		}, {
			postln("INFO: You are in performance mode or not in training mode.");
		});
	} //-//

    //------------------//
	// PAUSE THE TRAINING
    //------------------//
	// Training mode: Public function to pause the training. This is a deprecated function since it is not needed anymore since the training by random is also manual (and not automatic as before).

	pause {
		if (is_training==true, {
			postln("INFO: Deprecated function.");
			postln("INFO: Process paused.");
			if ( sndid_old_t.notNil && (sndid_t == sndid_old_t ), {
				postln("INFO: Fading out the previous sound...");
				this.fadeout_t;
			});
		});
	} //-//

	//------------------//
    // STOP TRAINING
    //------------------//
	// Training mode: Public function to stop training the dataset.

	stoptraining { |perc = 0.7 |

		var result = Array.new;
		var training_candidates;
		var training_dataset_content = Dictionary.new;
		var test_dataset_content = Dictionary.new;
		var training_labels_content = Dictionary.new;
		var test_labels_content = Dictionary.new;
		var num_valid;

		if ( is_training == true, {
			is_training = false;
		});

		/*if (sndid_old_t.notNil && (sndid_t == sndid_old_t), {
			"Fading out the previous sound...".postln;
			this.fadeout_t;
		});*/

		training_candidates = (manual_dataset_dict.size*perc).round.asInteger;
		postln("********************************************");
		postln("There are: "++training_candidates++" training_candidates");
		postln("There are: "++(manual_dataset_dict.size - training_candidates)++" testing_candidates");
		postln("********************************************");

		manual_dataset_dict.keysDo { |key, count|
			if( count < training_candidates, {
				// add to train
				result = manual_dataset_dict.at(key);
				training_dict.add(key -> result[2..27].asFloat); // number of inputs
				training_label_dict.add(key -> result[1].asSymbol.asArray);
			},{
				// add to test
				result = manual_dataset_dict.at(key);
				test_dict.add(key -> result[2..27].asFloat); // number of inputs
				test_label_dict.add(key -> result[1].asSymbol.asArray);
			});

		};

		// prepare data

		training_dataset_content.add(\cols -> 26); // number of inputs
		training_dataset_content.add(\data -> training_dict);

		test_dataset_content.add(\cols -> 26); // number of inputs
		test_dataset_content.add(\data -> test_dict);


		//prepare labels

		training_labels_content.add(\cols -> 1);
		training_labels_content.add(\data -> training_label_dict);

		test_labels_content.add(\cols -> 1);
		test_labels_content.add(\data -> test_label_dict);

		//load

		fork {
			//for training
			training_dataset.load(training_dataset_content); server.sync;
			training_dataset.dump; server.sync;
			training_label_dataset.load(training_labels_content); server.sync;
			training_label_dataset.dump; server.sync;
			//same for test
			test_dataset_fixed.load(test_dataset_content); server.sync;
			test_dataset_fixed.dump; server.sync;
			test_label_dataset_fixed.load(test_labels_content); server.sync;
			test_label_dataset_fixed.dump; server.sync;
			// transform training
			standardizer.fitTransform(training_dataset, stand_dataset, {"Standardizer training Done".postln;});
			server.sync;
			// transform test
			standardizer.transform(test_dataset_fixed, stand_test_dataset_fixed, {"Standardizer training Done".postln;});
			server.sync;
			// transform training
			pca.fitTransform(stand_dataset, stand_dataset, {"PCA training Done".postln;});
			server.sync;
			//transfrom test
			pca.transform(stand_test_dataset_fixed, stand_test_dataset_fixed, {"PCA test Done".postln;});
			server.sync;
			// classifier fit
			classifier.fit(stand_dataset, training_label_dataset, action:{|loss| ("Trained"+loss).postln});
			server.sync;
			// classifier predict
			classifier.predict(stand_test_dataset_fixed, test_predicted_label_dataset, action:{"Test complete".postln});
			server.sync;
			// accuracy
			num_valid = 0;
			// "***ACCURACY****".postln;
			test_label_dict.size.postln;
			test_label_dict.keysValuesDo{ | k, v |
				// postln("key: "++k);
				// postln("value: "++v);
				test_predicted_label_dataset.getLabel( k, { | l |
					// postln("predicted: "++l);
					if(l.asString == v[0].asString){
						num_valid = num_valid+1};
				});
				server.sync;
			};
			postln("********************************************");
			postln("Accuracy (0%-100%): " ++ ( (num_valid/test_label_dict.size) * 100) ++ "%");
			postln("Continue training or Save to JSON files?");
			postln("To continue training: [nameVariable].continuetraining");
			postln("To save to JSON files: [nameVariable].save");
			postln("********************************************");
		};

	} //--//

    //------------------//
    // SAVE TRAINING DATA
    //------------------//
	// Training mode: Public function to save the machine learning model as JSON files

	save {

		try {
			try {
				classifier.write( modelfilepath ++ "model-new.JSON" );
			}
			{ |error| [\catchFileWriteModel, error].postln }; // end catch error

			try {
				standardizer.write( modelfilepath ++ "standardizer-new.JSON");
			}
			{ |error| [\catchFileWriteStandardizer, error].postln }; // end catch error

			try {
				pca.write( modelfilepath ++ "pca-new.JSON");
			}
			{ |error| [\catchFileWritePCA, error].postln }; // end catch error

			if ( is_training == true, {
				is_training = false;
			});

			postln("INFO: The JSON files of your machine learning model have been saved.");
		}


	} //--//

    //------------------//
    // ARCHIVE TRAINING DATA FOR LATER USE
    //------------------//
	// Training mode: Public function to save the machine learning model as JSON files for a later follow-up.

	archive {

		if (is_training==true, {
			archiving = (); // Event for the training dictionary information
			// var virtualfilename = "\myData";
			// For now it will overwrite the values every time
			archiving[\manual_dataset] = manual_dataset_dict;
			Archive.global.put(\todayTraining, archiving);
			postln("INFO: Dataset successfully save as: ");
			Archive.global.at(\todayTraining).postcs;
			postln("INFO: This function will temporarily archive the dictionary for later training.\nUse [namevariable].load to load it again.");
			// postln(manual_dataset_dict);
		});

	} //-//

	load {

		if (is_training==true, {

			archiving = Archive.global.at(\todayTraining); // Event for retrieving the dictionary information
			archiving.postcs;
			// archiving[\manual_dataset].value;
			// archiving[\manual_dataset].size;
			// postln("MANUAL DATASET BEFORE");
			// postln(manual_dataset_dict);
			manual_dataset_dict = archiving[\manual_dataset];
			// postln("MANUAL DATASET AFTER");
			postln("INFO: Dataset successfully loaded as: ");
			postln(manual_dataset_dict);
		});
	}



}	