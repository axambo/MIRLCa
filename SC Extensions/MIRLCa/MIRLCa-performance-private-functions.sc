+ MIRLCa {

    //------------------//
    // STORE AND PLAY SOUND
    //------------------//
    // Performance & Training mode: Private function that manages to store and play a sound by ID.

	storeplaysound { | currentsnd, currentsize |

		index = metadata.size;

		try {

		metadata.add ( index -> currentsnd );

		if ( currentsize == 1,
		{ this.loadmetadata(currentsize); },
		{ // ELSE if currentsize > 1
			if ( ( metadata.size - poolsizeold ) == currentsize,
			{ // need to wait until asynchronous call is ready! once all sounds are added in the dictionary, they can be retrieved
			this.loadmetadata ( currentsize );
			});
		});
		} // end try
		{ |error| [\catchgetSound, error].postln }; // end catch error

	} //-//

    //------------------//
    // GET SOUND BY ID
    //------------------//
    // Performance & Training mode: Private function used by random, tag, and similar to get sounds
	// It has two modes: the performance mode and the training mode.
    // params: id, size
	// Overwriting this function to avoid sounds without analysis information
    prid { | id = 31362, size = 1, myFunc = nil |

		// try {
        backendClass.getSound ( id,
            { | f |
                //available metadata: "id","url","name","tags","description","geotag","created","license","type","channels","filesize""bitrate","bitdepth","duration","samplerate","username","Jovica","pack","pack_name","download","bookmark","previews","preview-lq-mp3","preview-hq-ogg","preview-hq-mp3","images","num_downloads","avg_rating","num_ratings","rate":,"comments","num_comments","comment","similar_sounds","analysis","analysis_frames","analysis_stats"

				var snd;
				snd = f;

				if ( is_training == false, // checks have been made of similarity data and file existence data (analysis data is not needed here)
					{
						this.storeplaysound ( snd, size );
						this.printcredits ( snd );
						myFunc.value(snd); // MIRLCaproxy
					},
					{ // IF Training is true, only 1 sound at a time
						if ( is_training == true, { // checks have been made of analysis data
							index = 0;
							metadata.add( index -> snd );
							this.loadmetadata_t ( 1 ); // size = 1, only 1 sound at a time
							this.printcredits ( snd );
							// TODO: Write the filenames during training distinguishing	between good and bad sounds
						});
					}); // End IF is_training == false

        } ); // end backendClass.getSound
    } //--//

    //------------------//
    // RETURNS BEST CANDIDATE
    //------------------//
	// Performance mode: Private function used to return the best candidate to the random() method.

	getbest { | candidates, myFunc = nil |

		var sndid;
		var temp_list_ids = List.new;
		var result;
		var temp_list = List.new;
		var temp_dict = Dictionary.new;
		var temp_counter = 0;
		var cond1 = Condition.new;
		var cond2 = Condition.new;
		var cond3 = Condition.new;

		// 1) generate a list of random numbers => sound IDs

		candidates.do( { | i |
			temp_list_ids.add(rrand ( 1, databaseSize ));
		});
		this.infocandidates ( temp_list_ids );

		// 2) get the analysis

		fork {
		candidates.do { | index |

			5.wait; // This is the amount of time in sec. For example, 10 sec means 6 sounds per minute.
			cond1.test = false;
			cond2.test = false;

			sndid = temp_list_ids[index];

			backendClass.getSound ( sndid,
			{ | f |

			var snd;
			temp_list = List.new;

			cond1.wait;
			snd = f;

			if ( snd.isNil, {
				this.warningnilsound ( sndid );
				}, {

				if ( snd["analysis"].isNil ) {
					this.warningnilanalysis ( sndid );
				};

				if ( snd["detail"]=="Not found.", {
					this.warningnildetail ( sndid );
				});

				if ( snd["detail"] != "Not found." && snd["analysis"].notNil, {

					snd.getAnalysis ( "lowlevel.mfcc", { | val |
						this.saveMFCCmetadata ( temp_list, val, val );
						temp_dict.add ( snd.id.asInteger -> temp_list.asFloat );
						cond2.wait;
					}, true); // end snd.getAnalysis

					}); // End IF get analysis
				}); // End IF-ELSE snd.isNil

			temp_counter = temp_counter + 1;

		}); // End of backendClass.getSound
			cond1.test = true;
			cond1.signal;
			cond2.test = true;
			cond2.signal;

		}; // End of size.do loop

		if ( temp_dict.size == 0, {
				this.warncandidates();
			}, {
				this.servebestcandidate ( temp_dict, myFunc );
			}); // End IF temp_dict.isNil

		}; // End of Fork

	} //--//

    //------------------//
    // RETURNS BEST CANDIDATE
    //------------------//
	// Performance mode: Private function used to return the best candidate to the tag(), similar() and diss() methods.
	// Arguments
	// p: FSPager; a page with results of candidate sounds. The first result is the same target sound.
	// candidates: Integer; number of sounds to be considered as candidates
	// index_offset: Integer; index to move along the sounds retrieved in FSPager. tag() will use value of 0 and similar() will use value of 1

	getbestfromtarget { | p, candidates, initial_offset = 0, myFunc = nil |

		var temp_dict = Dictionary.new;
		var offset = initial_offset;
		var sndid;

		// 1) find new sounds from the provided list and store their MFCCs info in a temp dictionary. The MFCCs are needed for finding best candidates
		while ( { ( offset < p.results.size ) && ( temp_dict.size < candidates ) },
		{
			snd = p[offset];
			sndid = snd.id;
			if ( this.sndidexist(sndid) == 0 , {
				this.infocandidate (sndid);
				this.savesoundMFCCdescriptors ( snd, temp_dict );
			} );
			offset = offset + 1;
		});

		// 2) Map the values of the temp dictionary to FluCoMa expectations
		this.servebestcandidate ( temp_dict, myFunc );

	} //--//

    //------------------//
    // SAVE SOUND MFCC DESCRIPTORS
    //------------------//
	// This function is called for each sound in the list of candidates. It checks that the required information exists and it saves the MFCCs metadata in a temporal list. The MFCCs information is necessary to then predict the best candidate. Private function used by the function getbestfromtarget().
	// Args
	// outdict: Dictionary; output dictionary

	savesoundMFCCdescriptors { | snd, outdict |

		var temp_list = List.new;

		if ( snd.isNil,
		{
			this.warningnilsound ( snd.id );
		},{
			if ( snd["analysis"].isNil ) {
				this.warningnilanalysis ( snd.id );
			};

			if ( snd["detail"]=="Not found." , {
				this.warningnildetail ( snd.id );
			});

			if ( snd["detail"] != "Not found." && snd["analysis"].notNil, {

				this.saveMFCCmetadata ( temp_list, snd.analysis, snd["analysis"] );
				outdict.add ( snd.id.asInteger -> temp_list.asFloat );

			}); // End IF sound EXISTS

		}); // End IF-ELSE snd.isNil
	}

    //------------------//
    // SAVE MFCC METADATA
    //------------------//
	// This function focuses on storing the MFCCs metadata in a temporal list. Private function used by the function getbestfromtarget() via savesoundMFCCdescriptors() for tag/similar/diss sounds and by the function getbest() for the random sounds.
	// Args
	// list: List; output list
	// path1: path1 to access to the MFCC mean.
	// path2: path2 to access to the MFCC variance.

	saveMFCCmetadata { | list, path1, path2 |
		list.add(path1.lowlevel.mfcc.mean[0]);
		list.add(path1.lowlevel.mfcc.mean[1]);
		list.add(path1.lowlevel.mfcc.mean[2]);
		list.add(path1.lowlevel.mfcc.mean[3]);
		list.add(path1.lowlevel.mfcc.mean[4]);
		list.add(path1.lowlevel.mfcc.mean[5]);
		list.add(path1.lowlevel.mfcc.mean[6]);
		list.add(path1.lowlevel.mfcc.mean[7]);
		list.add(path1.lowlevel.mfcc.mean[8]);
		list.add(path1.lowlevel.mfcc.mean[9]);
		list.add(path1.lowlevel.mfcc.mean[10]);
		list.add(path1.lowlevel.mfcc.mean[11]);
		list.add(path1.lowlevel.mfcc.mean[12]);
		list.add(path2["lowlevel"]["mfcc"]["var"][0]);
		list.add(path2["lowlevel"]["mfcc"]["var"][1]);
		list.add(path2["lowlevel"]["mfcc"]["var"][2]);
		list.add(path2["lowlevel"]["mfcc"]["var"][3]);
		list.add(path2["lowlevel"]["mfcc"]["var"][4]);
		list.add(path2["lowlevel"]["mfcc"]["var"][5]);
		list.add(path2["lowlevel"]["mfcc"]["var"][6]);
		list.add(path2["lowlevel"]["mfcc"]["var"][7]);
		list.add(path2["lowlevel"]["mfcc"]["var"][8]);
		list.add(path2["lowlevel"]["mfcc"]["var"][9]);
		list.add(path2["lowlevel"]["mfcc"]["var"][10]);
		list.add(path2["lowlevel"]["mfcc"]["var"][11]);
		list.add(path2["lowlevel"]["mfcc"]["var"][12]);
	}


    //------------------//
    // SERVE BEST CANDIDATE
    //------------------//
	// Once we obtained a dictionary of best candidates with their MFCCs descriptors, this function serves the first sound predicted as "good" from the list. Private function used by the function getbestfromtarget() and by the function getbest() for the random sounds.
	// Args
	// indict: Dictionary; input dictionary
	servebestcandidate { | indict, myFunc = nil |
		var test_dataset_content = Dictionary.new;
		// test_dataset_content = Dictionary.new;
		test_dataset_content.add(\cols -> 26); // number of inputs
		// test_dataset_content.add(\rows -> 1); // confirm it is needed
		test_dataset_content.add(\data -> indict);

		forkIfNeeded {
			test_dataset.load ( test_dataset_content );
			server.sync;
			standardizer.transform ( test_dataset, stand_test_dataset, {
				"Done Standardizer".postln;
			});
			server.sync;
			pca.transform ( stand_test_dataset, stand_test_dataset, {
				"Done PCA".postln;
			});
			server.sync;
			classifier.predict ( sourceDataSet: stand_test_dataset, targetLabelSet: test_predicted_label_dataset, action:{ "Done prediction".postln });
			server.sync;
			test_predicted_label_dataset.dump ( action: { | dict |
				var found = False;
				dict["data"].keysValuesDo { | k, v |
					// size.do { // TODO: implement the retrieval of +1 sound
					if ( v[0].asString =="good" && found == False, {
						this.prid (k, 1, myFunc); // each sound is loaded directly played
						"MIRLCa: Do you like this sound?".postln;
						found = True;
					});
					// }; // End of size.do loop
				}; // End keysValuesDo
				if ( found == False, {
					this.warningbadsounds ( indict.size );
				});

			});
			}; // End fork
	}

}