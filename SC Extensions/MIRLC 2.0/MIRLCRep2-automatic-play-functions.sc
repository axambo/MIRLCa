+ MIRLCRep2 {

    //------------------//
    // PLAY AUTO
    //------------------//
    // This public function plays the sounds of the same group several times at different rates that are changed on every certain interval
	playauto { | times = 4, tempo = 30 |

		var counter = times;
		var bool = 1;
		var speed = 1;

		var t = TempoClock.new;

		t.sched(tempo, {
			postln("new playauto interval of " ++ tempo ++ " seconds");
			postln("rate " ++ speed);

			if (counter >= 0 && playing == true,
				{
					if ( bool == 1,
						{
						speed = 1.0.rand.postln*(-1);
						this.play(speed);
						"Play backwards <<".postln;
						bool = 0;
						},
						{
						speed = 1.0.rand.postln;
						this.play(speed);
						"Play forwards >>".postln;
						bool = 1;
						}
					);
					counter = counter - 1;
					tempo;
				},
				{
                    if ( playing == true, {
                        "Done playauto".postln;
                        nil;
                        this.play(1);
                    }, {
                        "Stopped playauto".postln;
                        nil;
                    });

				}
			);
		});

	}	//--//

    //------------------//
    // PLAY AUTODOWN
    //------------------//
    // This public function plays the sounds of the same group several times at different rates that increasingly slow down that are changed on every certain interval
	playautodown { | startspeed = 1, endspeed = 0, times = 5, tempo = 10 |

		var period = abs(endspeed - startspeed) / times;//0.2
		var counter = startspeed - period; //1
		var speed = startspeed - period;

		var t = TempoClock.new;

		t.sched(tempo, {
			postln("new playautodown interval of: " ++ tempo ++ " seconds");
			postln("rate: " ++ speed);
			if (counter >= 0.01 && playing == true,
				{
					this.play(speed);
					"playing at: "+speed.postln;
					counter = counter - period;
					speed = speed - period;
					tempo;
				},
				{
                    if ( playing == true, {
                        "Done playdown".postln;
                        nil;
                        this.stop;
                    },
                    {
                       "Done playdown".postln;
                       nil;
                    }
                    );
				}
			);
		});

	}	//--//

   //------------------//
    // AUTOCHOPPED
    //------------------//
    // This public function plays the sounds of the same group several times at randomly assigned speeds during a certain interval

	autochopped { | times = 4, tempo = 1 |

		var counter = times;
		var bool = 1;
		var speed = 1;
		var tmptempo = tempo;

		var t = TempoClock.new;
		// tempo = tempo*[1,2,3,4,5].choose;
		tempo = tempo*[0.1,0.5,1,2,3].choose;
		t.sched(tempo, {
			postln("new autochopped interval of:" ++ tempo ++ " seconds");
			postln("rate: " ++ speed);
			tempo = tmptempo*[1,2,3,4].choose;
			if (counter >= 0 && playing == true,
				{
					if ( bool == 1,
						{
						speed = 1.0.rand.postln*(-1);
						this.play(speed);
						"Play backwards <<".postln;
						bool = 0;
						},
						{
						speed = 1.0.rand.postln;
						this.play(speed);
						"Play forwards >>".postln;
						bool = 1;
						}
					);
					counter = counter - 1;
					tempo;
				},
				{
                    if (playing == true, {
                        "Done autochopped".postln;
                        nil;
                        this.play(1);
                    }, {
                        "Done autochopped".postln;
                        nil;
                    });



				}
			);
		});

	} //--//


}
