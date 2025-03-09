+ MIRLCRep2 {

    //---------------------------------------------------//
    // FUNCTIONS FOR LIVE CODING WITH THE SOUNDS
    //---------------------------------------------------//
    // FUNCTIONS: play, sequence, parallel, stop, solo, solo all, mute, mute all, free, free all

    //------------------//
    // PLAY
    //------------------//
    // This public function plays the sounds of the same group at the same rate
    play { | rate = 1 |
		currentrate = rate;
        size = synths.size;
        size.do ( { | index |
			if( effectson == 0,
				{
					synths[index].set(\amp, maxvol, \rate, currentrate, \out, 0);
				},
				{
					synths[index].set(\amp, maxvol, \rate, currentrate, \out, b0);
				}
			);

            this.printsynth(index, index);
        });
    } //--//

	// Need to deactivate it calling at playauto(0)

    //------------------//
    // SEQUENCE
    //------------------//
    // This public function plays sounds sequentially, one after the other

   sequence { | rate = 1 |

		sequential = true;

		if (rate.isNil == false, { currentrate = rate });

		if ( prevplaymode == "parallel" || prevplaymode.isNil, {

			postln("INFO: Changed to sequence mode.");

			// delete all sounds except the first (synth and dict item)
			// left sound should play loop = 0
			synths.size.do { | index |
				if( index > 0,
					{ // remove other sounds
						this.free(index);
						synths.put(index, nil);
					},
					{ // when b=0, start play seq with first sound (loop = 0)
						synths[index].set(\loop, 0, \da, 2, \rate, currentrate);
						this.printsynth(counter, (synths.size-1));
						this.playnext(index, counter);
						postln("RATE INSIDE SEQUENCE: " + currentrate);

					}
				);

			};
		}, {
			postln("INFO: Remain in sequence mode.");
			currentrate = rate;
		});

		prevplaymode = "sequential";

    } //--//

    //------------------//
    // PARALLEL
    //------------------//
    // This public function plays sounds in parallel, all of them looping at the same time. If it comes from sequential, it will start the sounds from the beginning right right at the moment of triggering the function.

	parallel { | rate = 1 |

        sequential = false;

		if (rate.isNil == false, { currentrate = rate });

		if ( prevplaymode == "sequential", {

			oscreceiver.free;
			postln("INFO: Changed to parallel mode.");

			// delete all sounds (synth and dict item)
			synths.size.do { | index |
				this.free(index);
				synths.put(index, nil);
			};

			// bring them back (synth and dict item)
			// play them in loop
			buffers.size.do { | index |
				var buf = buffers[index];
				if ( effectson == 0,
					{
						synths.add ( index -> Synth.new(\synth_mono_fs, [\buf, buf, \bufnum, buf.bufnum, \rate, currentrate, \loop, 1, \amp, maxvol], g0) );
					},
					{
						synths.add ( index -> Synth.new(\synth_mono_fs, [\buf, buf, \bufnum, buf.bufnum, \rate, currentrate, \loop, 1, \amp, maxvol, \out, b0]) );
				});
				this.printsynths;
			};
		}, {
			postln("INFO: Remain in parallel mode.");
			this.play(currentrate);
		});

		prevplaymode = "parallel";

	} //--//


    //------------------//
    // VOLUME
    //------------------//
    // This public function sets the volume of the group of sounds 0..1
    volume { | vol = 0.2 |
        size = synths.size;
        size.do( { | index |
            synths[index].set(\amp, vol);
        });
    } //--//

    //------------------//
    // STOP
    //------------------//
	// This public function stops the sound of the group of sounds (sets the amplitude to zero)
    stop {
        size = synths.size;
        size.do( { |index|
            synths[index].set(\amp, 0);
        });
    } //--//

    //------------------//
    // FADE OUT
    //------------------//
	// This public function fades out all synths of a group of sounds with a smooth fade out

    fadeout { | release = 1.0 |
        playing = false;
		sequential = false; // to avoid inconsistencies

		if ( debugging == true,{
			postln("Number of sounds fading out: " ++ synths.size);
		});


		synths.size.do( { | index |
			try {
				synths[index].set(\gate, 0, \rel, release, \da, 2);
			} // try
			{|error| [\catchFadeout, error].postln };
		});


    } //--//


    //------------------//
    // SOLO
    //------------------//
    // This public function mutes all the sounds except for the selected sound from a given group
	  solo { | targetnumsnd = 0 |
        synths.size.do( { | index |
            if ( index == (targetnumsnd) , // before: (index == (targetnumsnd-1)
                {synths[index].set(\amp, maxvol)},
                {synths[index].set(\amp, 0)}
            );
        });
    } //--//

    //------------------//
    // MUTE
    //------------------//
    // This public function mutes a selected sound from a given group
    mute { |targetnumsnd = 0|
        synths[targetnumsnd].set(\amp, 0); // before: synths[targetnumsnd-1].set(\amp, 0);
    } //--//

    //------------------//
    // MUTE ALL
    //------------------//
    // This public function mutes all the sounds from a given group
    muteall {
        synths.size.do( { |index|
            synths[index].set(\amp, 0);
        });
    } //--//

	//------------------//
    // BYPASS
    //------------------//
    // This public function bypasses the effects
	bypass {
		effectson = 0;
		this.play;
	}//--//


}
