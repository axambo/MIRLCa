+ MIRLCRep2 {

    //---------------------------------------------------//
    // ANALYZING SOUNDS / AIDING METHODS
    //---------------------------------------------------//
    // FUNCTIONS:

    //------------------//
    // ANALYZE
    //------------------//
    // This public function retrieves all content-based descriptors listed in the Analysis Descriptor Documentation from the FreeSound API: "https://www.freesound.org/docs/api/analysis_docs.html#analysis-docs"
    // The result can be filtered using the descriptors request parameter passing a list of comma separated descriptor names chosen from the available descriptors e.g. 'descriptors=lowlevel.mfcc,rhythm.bpm'
    analyze { | descriptors |
        metadata.size.do( { | index |
            metadata[index].getAnalysis( descriptors, { | val |
				var result=val.dict;
				descriptors.split($.).do{ | d |
					// result.postln;
					result = result[d];
				};
                result.postln;
            }, true)
        });

    }//--//

	 //------------------//
    // WHAT ID
    //------------------//
	// Public function
    whatid { |feature = "id" |
        metadata.size.do ({ |index|
            postln("[" ++ index ++ "]: " ++ "id: " ++ metadata[index].id);
        });
    }//--//

	 //------------------//
    // WHAT MASTER VOLUME
    //------------------//
	// Public function
    whatvol {
		postln("[" ++ maxvol ++ "]");
    }//--//


    //------------------//
    // WHAT PITCH
    //------------------//
	// Public function
    whatpitch { | feature = "lowlevel.pitch.mean" |
        this.analyze(feature);
    }//--//

    //------------------//
    // WHAT KEY
    //------------------//
	// Public function
    whatkey { | feature = "tonal.key_key" |
        this.analyze(feature);
    }//--//

    //------------------//
    // WHAT BPM
    //------------------//
	// Public function
    whatbpm { |feature = "rhythm.bpm" |
        this.analyze(feature);
    }//--//

    //------------------//
    // WHAT DURATION (sec)
    //------------------//
	// Public function
    whatdur { |feature = "sfx.duration" |
        this.analyze(feature);
    }//--//

    //------------------//
    // CMD PERIOD
    //------------------//
    // This public function is activated when stopping the code / recompiling / etc.
    cmdPeriod {
        currentEnvironment.clear;
    } //--//

    //---------------------------------------------------//
    // VISUALIZATION, PRINTING
    //---------------------------------------------------//
    // FUNCTIONS: scope, plotserver, printmedata, printsynths, printbuffers, printall

    //------------------//
    // SCOPE
    //------------------//
    // This public function plots a waveform scope

    scope {
        /*window = Window.new("MIRLC scope", Rect(200, 200, 1200, 500));
        window.view.decorator = FlowLayout(window.view.bounds);
        viewoscil = Stethoscope.new(server, view:window.view);
        window.onClose = { viewoscil.free }; // don't forget this
        window.front;*/
        Stethoscope.new(server);
    } //--//

    //------------------//
    // PLOT SERVER
    //------------------//
    // This public function plots the server
    plotserver {
		//server.scope;
		server.plotTree;
		server.queryAllNodes;
    } //--//

    //------------------//
    // PRINT METADATA
    //------------------//
    // This function prints the FS metadata information for all downloaded sounds
    printmetadata {
        metadata.size.do ({ |index|
            postln("[" ++ index ++ "]: " ++ "id: " ++ metadata[index].id ++ " name: " ++ metadata[index].name ++ " by: " ++ metadata[index].username ++ " dur: " ++ metadata[index].duration);
        });
    } //--//

	// DUPLICATED from "printmetadata". TODO: merge it
    //------------------//
    // PRINT METADATA
    //------------------//
    // This function prints the FS metadata information for all downloaded sounds
    info {
        metadata.size.do ({ |index|
            postln("[" ++ index ++ "]: " ++ "id: " ++ metadata[index].id ++ " name: " ++ metadata[index].name ++ " by: " ++ metadata[index].username ++ " dur: " ++ metadata[index].duration);
        });
    } //--//

    //------------------//
    // PRINT BUFFERS
    //------------------//
    // This public function prints the buffers information and associated FS metadata information for all downloaded sounds
    printbuffers {
        buffers.size.do ({ |index|
            postln("[" ++ index ++ "]: " ++ buffers[index] ++ "id: " ++ metadata[index].id ++ " name: " ++ metadata[index].name ++ " by: " ++ metadata[index].username);
        });
    } //--//

    //------------------//
    // PRINT SYNTHS
    //------------------//
    // This public function prints the synths information and associated FS metadata information for all the active sounds
    printsynths {
		//postln(synths.size);
		synths.size.do ( { | index |
            postln (
				"now playing..." ++
				"[" ++ index ++ "]: " ++
				"id: " ++ metadata[index].id ++
				" name: " ++ metadata[index].name ++
				" by: " ++ metadata[index].username ++
				" dur: " ++ metadata[index].duration ++
				$\n ++ "synth: " ++ synths[index] );
        });

    } //--//

    //------------------//
    // PRINT ALL (METADATA, BUFFERS, SYNTHS)
    //------------------//
    // This public function prints the 3 arrays stored during a session.
    printall {
        postln("FS metadata dictionary: ");
        this.printmetadata;
        postln("buffers dictionary: ");
        this.printbuffers;
        postln("synths dictionary: ");
        this.printsynths;
    } //--//

    //------------------//
    // PRINT DIRECTORIES
    //------------------//
    // This public function prints the relevant directories where files are loaded from / saved to.
	printdirectories {
		// this.class.name
		postln("[INFO MIRLC]: Sounds will be downloaded at: " ++ directoryPath);
		postln("[INFO MIRLC]: A sound credits list will be created at: " ++ creditsfilepath);
	} //-//

    //------------------//
    // CREDITS
    //------------------//
    // This public function is activated when stopping the code / recompiling / etc. It prints the list of sounds used in a session.
    credits {
		var listcredits;
		try {
			file.open(creditsfilename,"r");
			listcredits = file.readAllString;
			file.close();
			postln("********************************");
			listcredits.postln;
			postln("********************************");
		} //end try
		{|error| [\catchFileWrite, error].postln }; // end catch error
    } //--//

}