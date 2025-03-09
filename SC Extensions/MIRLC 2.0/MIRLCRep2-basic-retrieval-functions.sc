+ MIRLCRep2 {

    //---------------------------------------------------//
    //QUERIES TO SEED A POOL OF SOUNDS (TEXT, CONTENT)
    //---------------------------------------------------//
    // FUNCTIONS: random, tag, content

    //------------------//
    // GET SOUND BY ID
    //------------------//
    // This public and private function can be used as a standalone public function to get [1..n] sounds by ID, and it is also used as a private function by random, tag, similar, filter, content to get sounds
    // params: id, size
    id { | id = 31362, size = 1 |

		// If (size > 1 && sound exists in the folder) // in the future store the metadata as well and so then if (size > 1)
		// just copy the info from previous
		// else:

        backendClass.getSound( id,
            { | f |
                //available metadata: "id","url","name","tags","description","geotag","created","license","type","channels","filesize""bitrate","bitdepth","duration","samplerate","username","Jovica","pack","pack_name","download","bookmark","previews","preview-lq-mp3","preview-hq-ogg","preview-hq-mp3","images","num_downloads","avg_rating","num_ratings","rate":,"comments","num_comments","comment","similar_sounds","analysis","analysis_frames","analysis_stats"
				// ways of printing info:
				// "name".postln; or snd["name"].postln;
				// this.printmetadata;

				// snd = f;
				var tmpSnd = f;

				if ( tmpSnd.isNil, {
					postln("ERROR: There was a problem downloading the sound with ID " ++ id ++ "\nINFO: Please try again.");},
				{

					if ( tmpSnd["detail"]=="Not found.", {
						postln("ERROR: Sound details not found.\nINFO: Please try another sound.");
					}, {

					if ( debugging == true,{"ERROR: Sound exists in the database.".postln;});

					index = metadata.size;
						// postln("**********************");
						// postln("SOUND ID IS: " + tmpSnd.id);
						// postln("**********************");
					metadata.add(index -> f);

					if ( size == 1, {
						this.loadmetadata(size);
					},{ // size > 1
						if ( (metadata.size - poolsizeold) == size, // need to wait until asynchronous call is ready! once all sounds are added in the dictionary, they can be retrieved
						{
                            this.loadmetadata(size);
                        }
                    );
					}); // end if

					try {
						file.open(creditsfilename,"a");
						file.write(tmpSnd["name"] ++ " by " ++ tmpSnd["username"] ++ " (" ++ tmpSnd["url"] ++") licensed under " ++ tmpSnd["license"] + "\n");
						file.close();
						} //end try
					{ |error| [\catchFileWrite, error].postln }; // end catch error

					}); // End IF (tmpSnd["detail"]=="Not found." vs sound found

				}); // End IF tmpSnd.isNil
		}); // End backendClass.getSound

    } //--//

    //------------------//
    // QUERY BY RANDOM
    //------------------//
    // This public function gets [1..n] sounds by random, and plays them
	// Previously was checking whether the sound existed with snd["detail"] != nil but it seems to work better snd["detail"]=="Not found."
    random { | size = 1 |

		var sndid;

        if ( debugging == true, {postln("INFO: Number of sounds selected by random: " ++ size);} );

        sndid = rrand ( 1, databaseSize );

        backendClass.getSound ( sndid,
            { | f |

				// snd = f;
				var tmpSnd = f;

				if ( tmpSnd.isNil, {
					postln("ERROR: There was a problem downloading the random sound with ID " ++ sndid ++ "\nINFO: Please try again.");},
				{

                if ( tmpSnd["detail"]=="Not found.",
                    {
						if ( debugging == true, {"ERROR: SoundID does not exist".postln;} );
						"WARNING: Sound not found in the database. Getting another sound.".postln;
                        this.random( size );
                    },
                    {
                        if ( debugging == true, {
                            postln("INFO: Potential sound candidate: ");
                            tmpSnd["name"].postln;
							postln("Counter value is: " + counter);
                        });

                        counter = counter + 1; // adding one sound to the counter in a recursive fashion
                        if (size == 1,
                            {
                                this.id ( sndid, size );
                            },
                            {//size > 1
								if ( debugging == true, {
									this.id(sndid, size);
									postln("INFO: Group size is greater than 1");
									postln("INFO: ( counter - size ): " ++ ( counter - size ));
									}); // end if debugging

                                if ( counter <= size ,
                                    {
                                        this.id(sndid, size);
                                        if ( counter < size, { this.random ( size ); } );
									}
                                );
                            }
                        );
					});
				});	// End IF tmpSnd.isNil
		}); // End Function backendClass.getSound
    } //--//

    //------------------//
    // QUERY BY TAG
    //------------------//
    // This public function gets [1..n] sounds by one defined tag, and plays them
    tag { |tag = "noise", size = 1|

        if ( debugging == true, {
            postln("Sounds selected by tag: " ++ size);
        });

        backendClass.textSearch(query: tag, params: ('page': 1),
            action: { |p|
				if ( p.isNil,
				{
					postln("ERROR: There was a problem downloading this sound using the tag method.\nINFO: Please try again.");
				}, {

						size.do { |index|
							snd = p[index];
							postln("INFO: Found sound by tag, id: " ++ snd["id"] ++ "name: " ++ snd["name"]);
							while (
								{this.sndidexist(snd.id) == 1},
								{
									postln ("INFO: Repeated sound, getting another sound...");
									index = index + 1 + size;
									snd = p[index];
							});
							this.id(snd.id, 1); // so that each sound is loaded & directly played
						}
				}); // End IF p.isNil
		    });	// End Function backendClass.textSearch

    } //--//


    //------------------//
    // QUERY BY CONTENT
    //------------------//
    // This public function gets [1..n] sounds by one defined feature and fx, and plays them
    content { | feature = 'dur', size = 1, fvalue = 1, fx = 'conf', fxvalue = 'bypass' |
        var fconcat, fxconcat;
        if (feature != 'id',
          { fconcat = this.gettranslation(feature.asSymbol)++fvalue; },
          { fconcat = fvalue });
        fxconcat = this.gettranslation(fx.asSymbol) ++ this.gettranslation(fxvalue);

        backendClass.contentSearch(
            target: fconcat,
            filter: fxconcat,
            params: ('page':1),
            action: {|p|
				if ( p.isNil,
				{
					postln("ERROR: There was a problem downloading this sound using the content method.\nINFO: Please try again.");
				}, {

					size.do { |index|
						snd = p[index];
						//check if snd.id already exists, if so, take next sound
						if (metadata.size > 0,
							{
								while ( {this.sndidexist(snd.id) == 1},
									{
										index = index + size;
										snd = p[index];
										postln ("repeated sound, getting another sound...");
								});
						});
						this.id(snd.id, 1);  // so that each sound is loaded directly played;
					} // End size.do
				}); // End of p.isNil
            }); // End Function backenClass.contentSearch
    } //--//

    //------------------//
    // QUERY BY PITCH
    //------------------//
    // This public function gets [1..n] sounds by pitch
	pitch { | fvalue = 440, size = 1, fx = 'conf', fxvalue = 'hi' |

		this.content('pitch', size, fvalue, fx, fxvalue);

	}//-//

    //------------------//
    // QUERY BY BPM
    //------------------//
    // This public function gets [1..n] sounds by BPM
	bpm { | fvalue = 60, size = 1, fx = 'conf', fxvalue = 'hi' |

		this.content('bpm', size, fvalue, fx, fxvalue);

	} //-//

    //------------------//
    // QUERY BY DURATION
    //------------------//
    // This public function gets [1..n] sounds by duration
	dur { | fvalue = 10, size = 1, fx = 'conf', fxvalue = 'hi' |

		this.content('dur', size, fvalue, fx, fxvalue);

	} //-//

    //------------------//
    // QUERY BY DISSONANCE
    //------------------//
    // This public function gets [1..n] sounds by dissonance
	diss { | fvalue = 1.0, size = 1, fx = 'conf', fxvalue = 'hi' |

		this.content ( 'dissonance', size, fvalue, fx, fxvalue );

	} //-//


    //---------------------------------------------------//
    // QUERIES TO CONTINUE ADDING SOUNDS (QUERY BY EXAMPLE)
    //---------------------------------------------------//
    // FUNCTIONS: similar, filter

    //------------------//
    // SIMILAR SOUNDS
    //------------------//
    // This public function gets [1..n] similar sounds from a target sound, usually the first sound from the dictionary
    similar { | targetnumsnd = 0, size = 1 |

        target = metadata[targetnumsnd];  // before: metadata[targetnumsnd - 1];

        target.getSimilar(
            action: { |p|

			if ( p.isNil,
			{
				postln("ERROR: There was a problem downloading this sound using the similar method.\nINFO: Please try again.");
			}, {
                size.do { |index|
                    snd = p[index+1]; // to avoid retrieving the same sound of the query
                    //check if snd.id already exists, if so, take next sound
                    if (metadata.size > 0,
                        {
                            while ( {this.sndidexist(snd.id) == 1},
                                {
                                    index = index + 1 + size;
                                    snd = p[index];
                                    postln ("INFO: repeated sound, getting another sound...");
                            });
                    });
                    this.id(snd.id, 1); // so that each sound is loaded directly played
                } // End Size do
			}); // End IF p.isNil
        }); // End Function target.getSimilar


    } //--//

    //------------------//
    // SIMILAR BY RANGE
    //------------------//
    // This public function gets [1..n] similar sounds from a target sound filtered by a fx
    filter { |targetnumsnd = 0, size = 1, fx = 'conf', fxvalue = 'bypass' |

        var  fxconcat;
        fxconcat = this.gettranslation(fx.asSymbol) ++ this.gettranslation(fxvalue);

        sndid = metadata[targetnumsnd].id; // before: metadata[targetnumsnd - 1].id

        backendClass.contentSearch(
            target: sndid,
            filter: fxconcat,
            params: ('page':1),
            action: { |p|

				if ( p.isNil,
				{
					postln("ERROR: There was a problem downloading this sound using the filter method.\nINFO: Please try again.");
				}, {

					size.do { |index|
						snd = p[index];
						//snd.name.postln;
						//check if snd.id already exists, if so, take next sound
						if (metadata.size > 0,
							{
								while ( {this.sndidexist(snd.id) == 1},
									{
										index = index + size;
										snd = p[index];
										postln ("repeated sound, getting another sound...");
								});
						});
						this.id(snd.id, 1); // so that each sound is loaded directly played
					} // End size.do

				});	// End IF p.isNil

            }); // End Function backendClass.contentSearch
    } //--//

}
