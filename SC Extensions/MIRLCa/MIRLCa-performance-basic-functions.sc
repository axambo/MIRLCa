+ MIRLCa {

    //------------------//
    // GET SOUND BY ID
    //------------------//
    // Performance mode: This public function is used by the live coder to get a sound by ID
    // params: id, size
	id { | id = 31362, size = 1, myFunc = nil |

		 backendClass.getSound ( id,
            { | f |

				var tmpSnd;
				tmpSnd = f;

			if ( tmpSnd.isNil, {
					postln("ERROR: There was a problem downloading the sound with ID " ++ id ++ "\nINFO: Please try again.");},
				{ //tmpSnd.notNil
					if ( tmpSnd["similar_sounds"].isNil , {
						postln("ERROR: This sound does not have similar_sounds information required by the MIRLCa agent.\nWARNING: Please try another sound.");});

					if ( tmpSnd["detail"] == "Not found.", {
						postln("ERROR: Sound details not found.\nWARNING: Please try another sound.");
					});

					if ( tmpSnd["detail"] != "Not found." && tmpSnd["similar_sounds"].notNil , { // Sound ID on Freesound exists
						this.storeplaysound(tmpSnd, size);
						this.printcredits ( tmpSnd );
						myFunc.value(tmpSnd); // MIRLCaproxy

					});
				}); // End If tmpSnd.isNil

			} );
    } //-//


    //------------------//
    // SIMILAR SOUNDS
    //------------------//
	// Performance mode: Public function that returns the best candidate of a list of similar sounds given a target sound. It can only return one sound for now.
	// Arguments:
	// targetnumsnd: Integer; the sound used as target. Range: [0, n-1]
	// size: Integer; how many sounds to be retrieved. For now only one sound is possible.
	// candidates: Integer; how many candidate sounds to be retrieved. For now only 15 sounds are possible. %TODO: load a second page asynchronously using FSPager as an asynchronous action using the method next
	similar { | targetnumsnd = 0, size = 1, candidates = 15, myFunc = nil |

		var index_offset = 1;
		// var candidates = cand; // 1 page has 15 sounds. %TODO: load a second page asynchronously using FSPager as an asynchronous action using the method next
		var query_params = Dictionary [
			"fields"->"id,name,analysis",
			"descriptors"->"lowlevel.mfcc"
		];

		target = metadata[targetnumsnd];  // before: metadata[targetnumsnd - 1];

		this.warnunhandledlimitstarget ( targetnumsnd );
		this.warnunhandledlimitssize ( size );
		this.warnunhandledlimitscandidates ( candidates );

		try {
		target.getSimilar ( params:query_params,
			action: { | p |

			/*// Getting info of the incoming sound
			p[i].id.postln;
			p[i].dict.keys.postln;
			p[i].dict.values.postln;*/
			if ( p.isNil,
			{
				postln("ERROR: There was a problem searching for a list of candidates of similar sounds from this target sound.\nINFO: Please try again.");
			}, {
				this.getbestfromtarget( p, candidates, index_offset, myFunc );
			});



		});
		}//end try
		{|error| [\catchSimilarMethod, error].postln; postln("ERROR: getSimilar() was not understood."); };

	} //--//

    //------------------//
    // QUERY BY TAG
    //------------------//
    // Performance mode: Public function that returns the best candidate of a sound by one defined tag. It can only return one sound for now.
	// Arguments:
	// tag: String
	// size: Integer
	// candidates: Integer; 1 page has 15 sounds. %TODO: load a second page asynchronously using FSPager as an asynchronous action using the method next

    tag { | tag = "noise", size = 1, candidates = 15, myFunc = nil |

		var index_offset = 0;
		// var candidates = cand; // 1 page has 15 sounds. %TODO: load a second page asynchronously using FSPager as an asynchronous action using the method next
		var query_params = Dictionary[
			"fields"->"id,name,analysis",
			"descriptors"->"lowlevel.mfcc"
		];

		this.warnunhandledlimitssize ( size );
		this.warnunhandledlimitscandidates ( candidates );

		/*if (size != 1, {size = 1}); // temp solution: limit retrieval to 1 sound
		if (candidates > 15, {candidates = 15}); // temp solution: limit to 14 sounds provided by FSPager*/

		// try { // seems to be giving an error
        backendClass.textSearch ( query: tag, params: query_params,

            action: { | p |
				if ( p.isNil,
				{
					postln("ERROR: There was a problem searching for a list of candidates from this tag.\nINFO: Please try again.");
				}, {
					this.getbestfromtarget( p, candidates, index_offset, myFunc );
				});

		    });
		// } //end try
		// { |error| [\catchTagMethod, error].postln };

    } //--//

    //------------------//
    // QUERY BY DISSONANCE
    //------------------//
    // This public function gets [1..n] sounds by dissonance
	diss { | fvalue = 1.0, size = 1, fx = 'conf', fxvalue = 'hi', cand = 15, myFunc = nil |

		this.content ( 'dissonance', size, fvalue, fx, fxvalue, cand, myFunc );

	} //-//


    //------------------//
    // QUERY BY PITCH
    //------------------//
    // This public function gets [1..n] sounds by pitch
	pitch { | fvalue = 440, size = 1, fx = 'conf', fxvalue = 'hi', cand = 15, myFunc = nil |

		this.content ( 'pitch', size, fvalue, fx, fxvalue, cand, myFunc );

	}//-//

    //------------------//
    // QUERY BY BPM
    //------------------//
    // This public function gets [1..n] sounds by BPM
	bpm { | fvalue = 60, size = 1, fx = 'conf', fxvalue = 'hi', cand = 15, myFunc = nil |

		this.content ( 'bpm', size, fvalue, fx, fxvalue, cand, myFunc );

	} //-//

    //------------------//
    // QUERY BY DURATION
    //------------------//
    // This public function gets [1..n] sounds by duration
	dur { | fvalue = 10, size = 1, fx = 'conf', fxvalue = 'hi', cand = 15, myFunc = nil |

		this.content ( 'dur', size, fvalue, fx, fxvalue, cand, myFunc );

	} //-//

    //------------------//
    // QUERY BY CONTENT
    //------------------//
    // Performance mode: Public function that returns the best candidate of a sound by one defined feature and fx. It can only return one sound for now.

    content { | feature = 'dur', size = 1, fvalue = 1, fx = 'conf', fxvalue = 'bypass', cand = 15, myFunc = nil |

		var index_offset = 0;
		var candidates = cand;
		var query_params = Dictionary [
			"fields"->"id,name,analysis",
			"descriptors"->"lowlevel.mfcc"
		];
        var fconcat, fxconcat;

		this.warnunhandledlimitssize ( size );
		this.warnunhandledlimitscandidates ( candidates );

        if (feature != 'id',
          { fconcat = this.gettranslation ( feature.asSymbol ) ++ fvalue },
          { fconcat = fvalue });
        fxconcat = this.gettranslation ( fx.asSymbol ) ++ this.gettranslation ( fxvalue );

        backendClass.contentSearch (
            target: fconcat,
            filter: fxconcat,
            params: query_params,
            action: { | p |
				if ( p.isNil,
				{
					this.warningnilcontent ( p[0].id );
				}, {

					this.getbestfromtarget ( p, candidates, index_offset, myFunc );

				}); // End of p.isNil
            }); // End Function backenClass.contentSearch
    } //--//

    //------------------//
    // QUERY BY RANDOM
    //------------------//
    // Performance mode: Public function that returns the best candidate of a list of random sounds. It can only return one sound for now.

    random { | size = 1, cand = 7, myFunc = nil |

		// var sndid;

		postln("INFO: Looking for a 'good' random sound...");
		postln("INFO: The number of candidates for the best random sound is: " ++ cand);
	    postln("INFO: The larger the number, the more time it will take. Be patient.");

		// sndid = this.getbest ( cand );

		this.getbest ( cand, myFunc );

    } //--//

}