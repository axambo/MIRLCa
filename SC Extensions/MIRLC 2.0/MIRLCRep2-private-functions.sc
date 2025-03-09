+ MIRLCRep2 {

    //---------------------------------------------------//
    //SOUND GROUP MANAGEMENT (PRIVATE FUNCTIONS)
    //---------------------------------------------------//
    // FUNCTIONS: loadsoands, loadmetadata, playnext, freeall, free

    //------------------//
    // RETRIEVE SOUNDS
    //------------------//
    // This private function manages the dictionary metadata (sounds with fs info) and loads the new sounds
    // This function is in charge of the storage of a new group of sounds by managing the right index number when calling the function load() for each new sound from a dictionary of json info and resetting the counter.
    loadmetadata { | totalsnds = 1 |
        totalsnds.do ( { | index |
            this.loadsounds( metadata, (index + poolsizeold) );
        });
        poolsizeold = metadata.size;
        counter = 0; // used in random
        this.printmetadata;
    }

    //------------------//
    // LOAD SOUNDS
    //------------------//
    // This private function parses the Freesound information of each sound and converts it to the SuperCollider language, storing all the info in two dictionaries (Buffers and Synths). The result is a sound that plays once is correctly stored in the synths dictionary. The sound is stored in mono and converted in stereo when played.
    loadsounds { | dict, index |
        dict[index].retrievePreview(directoryPath, {
			/*("previewFilename").postln;
			dict[index].previewFilename.postln;*/
            buf = Buffer.readChannel(server, directoryPath ++ dict[index].previewFilename,
                channels: [0],
                action: { | buf |
                    if ( sequential == false ,
                        { // in parallel mode, play all sounds as they get downloaded
							if ( effectson == 0,
								{
									synths.add (index -> Synth.new(\synth_mono_fs, [\buf, buf, \bufnum, buf.bufnum, \loop, 1, \amp, maxvol], g0) );

								},
								{
									synths.add (index -> Synth.new(\synth_mono_fs, [\buf, buf, \bufnum, buf.bufnum, \loop, 1, \amp, maxvol, \out, b0]) );
								});

                        },
                        { // in sequential mode, store the sound in buffers only to be loaded and played in synths when triggered
                          // No action required here
                    });
                    buffers.add(index -> buf);
            });
        });
    } //--//


    //------------------//
	// PLAY NEXT (SEQUENCE MODE) (PRIVATE)
    //------------------//
    // This function is private and makes sure to play sounds sequentially via OSC messages

	playnext { | index, counter | // index = 0, 1st position
		var currentbuf;
		// Testing incoming traffic
		// OSCFunc.trace(true); // Turn posting on
		oscreceiver = OSCFunc ( { | msg |
			// "message arrived".postln;
			if (currentrate < 0, {
				currentrate = currentrate * (-1);
				postln("INFO: Negative values can't be handled.");
			});
			if ( synths.size == 1, {

				synths.put( index, nil );

				if ( counter < (buffers.size - 1), {
				counter = counter + 1;
			}, {
				counter = 0;
			});

			currentbuf = buffers[counter];

			if ( effectson == 0,
				{
						synths.add ( index -> Synth.new(\synth_mono_fs, [\buf, currentbuf, \bufnum, currentbuf.bufnum, \rate, currentrate, \loop, 0, \da, 2, \amp, maxvol], g0 ) );
						// this.printsynth(0);
						// this.printsynths;
				},
				{
					synths.add ( index -> Synth.new(\synth_mono_fs, [\buf, currentbuf, \bufnum, currentbuf.bufnum, \rate, currentrate, \loop, 0, \da, 2, \amp, maxvol, \out, b0] ) );
						// this.printsynth(0);
						// this.printsynths;
				});
				// this.printsynths;
				this.printsynth(counter, (synths.size-1));
			});


		}, '/bufDone');

	} //--//

	//------------------//
	// FREE ALL
	//------------------//
	// This is a private function that frees all the synths from a given group
	freeall {
		synths.size.do ( { |index|
			synths[index].free;
		});
	} //--//

	//------------------//
	// FREE
	//------------------//
	// This is a private function that frees a given synth from a given group
    free { | index |
        synths[index].free;
    } //--//

    //---------------------------------------------------//
    // UTILS
    //---------------------------------------------------//
    // FUNCTIONS: sndexist, argstranslate, gettranslation

    //------------------//
    // DOES A SOUND EXIST
    //------------------//
    // This private function returns whether the sound is already in the metadata dictionary or not
    sndidexist { | id |
        metadata.size.do ( { | index |
			if ( metadata[index].id == id,
				{ ^1 }
            );
        });
       ^0;

    } //--//

    //------------------//
    // TRANSLATE TO FS ARGS
    //------------------//
    // This private function maps from shorter arguments to the ones expected by the FreeSound quark
    argstranslate {
        //Features
        translation.add(\pitch -> ".lowlevel.pitch.mean:");
        translation.add(\dur -> ".sfx.duration:");
        translation.add(\dissonance -> ".lowlevel.dissonance.mean:");
        translation.add(\bpm -> ".rhythm.bpm:");
        //Filters
        translation.add(\key -> "tonal.key_key:");
        translation.add(\scale -> "tonal.key_scale:");
        translation.add(\conf -> ".lowlevel.pitch_instantaneous_confidence.mean:");
        translation.add(\mfcc0 -> "lowlevel.mfcc.mean[0]:");
        translation.add(\mfcc1 -> "lowlevel.mfcc.mean[1]:");
        translation.add(\mfcc4 -> "lowlevel.mfcc.mean[4]:");
        //Filter values
        translation.add(\Asharp-> "\"ASharp\"");
        translation.add(\A-> "\"A\"");
        translation.add(\B-> "\"B\"");
        translation.add(\C-> "\"C\"");
        translation.add(\D-> "\"D\"");
        translation.add(\E-> "\"E\"");
        translation.add(\F-> "\"F\"");
        translation.add(\G-> "\"G\"");
        translation.add(\major -> "\"major\"".asString);
        translation.add(\minor -> "\"minor\"".asString);
        translation.add(\hi -> "[0.8 TO 1]");
        translation.add(\lo -> "[0 TO 0.2]");
        translation.add(\bypass -> "[0 TO 1]");
        translation.add(\1720 -> "[17 TO 20]");
        translation.add(\2040 -> "[20 TO 40]");
        translation.add(\neg -> "[-1124 TO -1121]");
        //translation.add(\diss -> "lowlevel.dissonance.mean:"); // 0 -> consonant, 1-> dissonant [0.3-0.5]
        //translation.add(\voiced -> "lowlevel.spectral_entropy.mean:"); // [2-10]

    } //--//


    //------------------//
    // GET TRANSLATION
    //------------------//
    // This private function translates a parameter only if it exists in the dictionary translation

    gettranslation { | key |
    		if (translation.includesKey(key)){
    			^translation[key];
    		}{
    			^key;
    		}
    	}

    //------------------//
    // PRINT SYNTH
    //------------------//
    // This public function prints the synth information and associated FS metadata information of the current active sound
    printsynth { | indexmetadata, indexsynth |

        postln (
			"now playing..." ++
			"[" ++ indexsynth ++ "]: " ++
			"id: " ++ metadata[indexmetadata].id ++
			" name: " ++ metadata[indexmetadata].name ++
			" by: " ++ metadata[indexmetadata].username ++
			" dur: " ++ metadata[indexmetadata].duration ++
			$\n ++ "synth: " ++ synths[indexsynth] );

    } //--//

}