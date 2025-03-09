// MIRLCaProxy is a class used to build the dataset of Task 2:
// a collection of entries that show call-response actions with values
// This will be used to train a model that will predict, from a query, what to respond
// Once a model is trained, it will need to be integrated with the MIRLCa code. Perhaps using an additional "layer"/class is a suitable approach.
//
// My pseudocode comments
// Process:
	//
	// For each of the functions e.g. pitch, similar, etc
	// call the dictionary
	// store the values
	// increment counter +1
	// add data in entry (call)
	// add data in entry (retrieve)
	// add entry in collection
    //
	// how can you "overwrite" / "proxy" the sound downloaded?
    // Assumption: all functions retrieve one sound
// TODO:
    // Implement effects
    // Update code for mode load (right now only mode new works with the text file, the load mode still has the code of loading and ARCHIVE file.

MIRLCaProxy : MIRLCa {

	classvar sessioncounter;
	classvar itemcounter;
	classvar <collection;
	classvar actiontyprev;
	classvar actionvalueprev;
	classvar soundprev;
	classvar analysisprev;

	// When instatiating tell whether 1) creation of a new dataset (mod = \new) or retrieval of an existing dataset (with the name (String) as an argument (mod = \load))

	// create a MIRLCa instance in the background

	// TODO: Fix the parameters so they are not hardcoded

	*new { | backend = 0, dbSize = 571613, path = "/Users/anna/Desktop/MIRLCa-downloads/", creditsPath = "/Users/anna/Desktop/MIRLCa-credits/", modelsPath = "/Users/anna/Desktop/MIRLCa-default-models/",  mode = \new |
		^super.new(backend, dbSize, path, creditsPath, modelsPath).initproxy(mode);
    }

	initproxy { | mode |

		var loaded;
		var event = ();

		if ( collection == nil,
			{ collection = Dictionary() }
		);


		if ( sessioncounter == nil,
			{ sessioncounter = 0  },
			{ sessioncounter = sessioncounter + 1 }
		);

		if ( mode == \load, { // TODO: is this working?
			postln("MODE LOAD");
			loaded = 1;
			event = Archive.global.at(\task2_database_rain);
			event.postcs;
			collection = event[\session1];
			itemcounter = collection.size;
		}, { // elseif mod = \new
			loaded = 0;
			// itemcounter = 0;
			if ( itemcounter == nil, { itemcounter = 0 } );
		} );

	}

	// id = code 001
	id { | id = 31362, size = 1 |
		var code, fvalue, myFunc;
		code = 1;
		fvalue = id;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.id (id, size, myFunc);
	}

	// tag = code 002
	tag { | tag = "noise", size = 1, candidates = 15 |
		var code, fvalue, myFunc;
		code = 2;
		fvalue = tag;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.tag (tag, size, candidates, myFunc);
	}

	// similar = code 003
	similar { | targetnumsnd = 0, size = 1, candidates = 15 |
		var code, fvalue, myFunc;
		code = 3;
		fvalue = targetnumsnd;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.similar (targetnumsnd, size, candidates, myFunc);
	}

	// random = code 004
	random { | size = 1, cand = 7 |
		var code, fvalue, myFunc;
		code = 4;
		fvalue = nil;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.random (size, cand, myFunc);
	}

	// pitch = code 005
	// changed name of parameter from fvalue to featvalue
	pitch { | featvalue = 440, size = 1, featfx = 'conf', featfxvalue = 'hi', cand = 15 |
		var code, fvalue, myFunc;
		code = 5;
		fvalue = featvalue;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.pitch (featvalue, size, featfx, featfxvalue, cand,  myFunc);

	}

	// bpm = code = 006
	bpm { | featvalue = 60, size = 1, featfx = 'conf', featfxvalue = 'hi', cand = 15 |
		var code, fvalue, myFunc;
		code = 6;
		fvalue = featvalue;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.bpm (featvalue, size, featfx, featfxvalue, cand,  myFunc);

	}


	// dur = code 007
	dur { | featvalue = 10, size = 1, featfx = 'conf', featfxvalue = 'hi', cand = 15 |
		var code, fvalue, myFunc;
		code = 7;
		fvalue = featvalue;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.dur (featvalue, size, featfx, featfxvalue, cand,  myFunc);
	}


	// diss = code 008
	diss { | featvalue = 1.0, size = 1, featfx = 'conf', featfxvalue = 'hi', cand = 15 |
		var code, fvalue, myFunc;
		code = 8;
		fvalue = featvalue;
		myFunc = { | fsnd | // when sound is downloaded...
			fsnd.getAnalysis( action: { | analysis |
				analysis.dict.postln;
				analysis.dict.keys().postln;
				this.addaction (code, fvalue, fsnd, analysis);
			});
		}
		^super.diss (featvalue, size, featfx, featfxvalue, cand,  myFunc);
	}

	// Effects To Be Added
	// 101 effect1
	// 102 effect2
	// 103 effect3
	// 104
	// 105
	// 106
	// 107

	addaction { | actiontype, actionvalue, sound, analysis |

		var entry;
		entry = Dictionary();

		postln("!START: ADD ACTION FUNCTION");
		if (sessioncounter == 0, {
			actiontyprev = actiontype;
			actionvalueprev = actionvalue;
			soundprev = sound;
			analysisprev = analysis;
			sessioncounter = sessioncounter + 1;
			postln("actiontyprev: " ++ actiontyprev);
			postln("actionvalueprev: " ++ actionvalueprev);
			postln("sessionocounter: " ++ sessioncounter);
			postln("soundprev: " ++ soundprev);
			postln("analysisprev: " ++ analysisprev);

			postln("!SESSION COUNTER: " ++ sessioncounter);
			postln("!ITEM COUNTER: " ++ itemcounter);
		}, {
			//
			itemcounter = itemcounter + 1;
			//
			entry.put(\action_type_call, actiontyprev);
			entry.put(\action_value_call, actionvalueprev);
			entry.put(\sound_info, soundprev);
			entry.put(\sound_analysis, analysisprev);
			entry.put(\action_type_response, actiontype);
			entry.put(\action_value_response, actionvalue);
			//
			postln("!VALUES TO BE STORED:");
			postln("\action_type_call: " ++ actiontyprev);
			postln("\action_value_call: " ++ actionvalueprev);
			postln("\sound_info: " ++ soundprev);
			postln("\sound_analysis: " ++ analysisprev);
			postln("\action_type_response: " ++ actiontype);
			postln("\action_value_response: " ++ actionvalue);
			//
			this.addentry (itemcounter, entry);
			//
			postln("!SESSION COUNTER: " ++ sessioncounter);
			postln("!ITEM COUNTER: " ++ itemcounter);
			//
			actiontyprev = actiontype;
			actionvalueprev = actionvalue;
			soundprev = sound;
			analysisprev = analysis;
			//
			entry = Dictionary();
			//
			postln("!AFTER ENTRY HAS BEEN STORED, UPDATE VALUES:");
			postln("actiontyprev: " ++ actiontyprev);
			postln("actionvalueprev: " ++ actionvalueprev);
			postln("soundprev: " ++ soundprev);
			postln("analysisprev: " ++ analysisprev);
			//
		} );

		postln("!END: ADD ACTION FUNCTION");
	}

	addentry { | index, data |
		postln("!START: ADD ENTRY FUNCTION");
		collection.put(index, data);
		collection.keysDo{ | k |
			postln("!CONTENT STORED IN DICTIONARY");
			postln("Item["++ k ++"]:");
			collection[k].postln;
		};
		postln("!END: ADD ENTRY FUNCTION");
	}

	savedictionary {
		var event = ();
		// change to the session number.
		event[\collection] = collection;
		event[\collection].value.writeArchive("/Users/anna/Desktop/session_" ++ date.stamp ++ ".txt");
		// TODO: check if there's data and confirm to overwrite it
		// Archive.global.put(\task2_database_rain_session5, event);
		// Archive.global.at(\task2_database_rain_session5).postcs;
	}

	plotdictionary {
		collection.keysDo{ | k |
			postln("!CONTENT STORED IN DICTIONARY SO FAR");
			postln("Item["++ k ++"]:");
			collection[k].postln;
		};
	}

	sizedictionary {
		collection.size.postln;
	}

}
