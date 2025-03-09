+ MIRLCRep2 {

    //------------------//
    // SIMILAR AUTO
    //------------------//
    // This public function gets [1..n] similar sounds from a target sound, scheduled to be downloaded on every certain interval.
	similarauto { |targetnumsnd = 0, size = 3, tempo = 30 |

		var counter = size;
		var offset = targetnumsnd;
		var t = TempoClock.new;

		t.sched(tempo, {
			"INFO: getting a similar sound (auto mode)...".postln;
			this.similar(offset);
			counter = counter - 1;
			offset = offset + 1;
			if (counter <= 0,
				{nil},
				{tempo}
			);
		});

	}	//--//

    //------------------//
    // SAME ID AUTO
    //------------------//
    // This public function downloads and plays the same sound a number of times on every certain interval.

	sameidauto { |id = 0, size = 3, tempo = 30 |

		var counter = size;
		var t = TempoClock.new;

		t.sched(tempo, {
			"INFO: getting same sound (auto mode)...".postln;
			this.id(id);
			counter = counter - 1;
			if (counter <= 0,
				{nil},
				{tempo}
			);
		});

	}	//--//




}
