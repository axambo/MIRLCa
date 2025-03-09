+ MIRLCa {

    //------------------//
    // PRINT DIRECTORIES
    //------------------//
    // This private function prints the relevant directories where files are loaded from / saved to.
	printdirectories {
		// this.class.name
		postln("[INFO MIRLCa]: Machine learning models are loaded from (in performance mode)" ++ "\nand will be saved at (in training mode): " ++ modelfilepath);
		postln("[INFO MIRLCa]: Sounds will be downloaded at: " ++ directoryPath);
		postln("[INFO MIRLCa]: A sound credits list will be created at: " ++ creditsfilepath);
	} //-//

    //------------------//
    // PRINT CREDITS
    //------------------//
    // This private function prints the sound credits list on a text file.
	printcredits { | snd |
		try {
			file.open(creditsfilename,"a");
			file.write(snd["name"] ++ " by " ++ snd["username"] ++ " (" ++ snd["url"] ++") licensed under " ++ snd["license"] + "\n");
			file.close();
		} //end try
		{ |error| [\catchFileWrite, error].postln }; // end catch error
	}

    //------------------//
    //
    //------------------//
	warnunhandledlimitstarget { | targetnumsnd |
		// temp solutions to avoid unhandled limits
		if (targetnumsnd >= metadata.size, { postln("INFO: This target sound does not exist.\nPlease choose another target sound."); }); // temp solution: limit retrieval to 1 sound
	}

    //------------------//
    //
    //------------------//
	// temp solution: limit retrieval to 1 sound
	warnunhandledlimitssize { | size |
		if ( size != 1, { size = 1 } );
	}

    //------------------//
    //
    //------------------//
	// temp solution: limit to 14 sounds limited by the FSPager
	warnunhandledlimitscandidates { | candidates |
		if ( candidates > 15, { candidates = 15 } );
	}

    //------------------//
    //
    //------------------//
	warncandidates {
		postln("********************************************");
		postln("ERROR: Something went wrong and there are no candidates.\nINFO: Please try again.");
		postln("********************************************");
	}

    //------------------//
    //
    //------------------//
	warningnilsound { | id |
		// postln("ERROR: There was a problem downloading this sound.\nThis sound will be skipped from the list of candidates.");
		postln("ERROR: There was a problem downloading the sound with ID " ++ id ++ ".\nWARNING: This sound will be skipped from the list of candidates.");
	}

    //------------------//
    //
    //------------------//
	warningnilanalysis { | id |
		postln("ERROR: Sound analysis of the sound with ID " ++ id ++ " does not exist.\nWARNING: This sound will be skipped from the list of candidates.");
	}

    //------------------//
    //
    //------------------//
	warningnildetail { | id |
		postln("ERROR: Sound details of the sound with ID " ++ id ++ " were not found.\nWARNING: This sound will be skipped from the list of candidates.");
	}

    //------------------//
    //
    //------------------//
	warningbadsounds { | size |
		postln("WARNING: Only bad sounds were found from "++size++" candidates.\nWARNING: Try another sound.");
	}

    //------------------//
    //
    //------------------//
	warningnilcontent { | id |
		postln("ERROR: There was a problem downloading the sound with ID " ++ id ++ " using the content method.\nINFO: Please try again.");
	}

    //------------------//
    //
    //------------------//
	infocandidates { | list |
		postln("INFO: The candidates for the best sound are: \n" ++ list);
	}

    //------------------//
    //
    //------------------//
	infocandidate { | id |
		postln("INFO: Evaluating candidate sound with ID:" + id);
	}


}	