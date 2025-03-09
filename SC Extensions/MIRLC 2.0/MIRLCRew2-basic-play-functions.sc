 + MIRLCRew2 {

    //------------------//
    // VOLUME
    //------------------//
	// This function controls the general volume from 0 to 1.

	volume { | amp |
		audiosrc.set(\amp, amp);
		overlaysig.set(\amp1, amp, \amp2, amp);

    } //--//

    //------------------//
    // FADE OUT
    //------------------//
	// This function fades out all synths with a smooth fade out. To start again, you need to instantiate again.

    fadeout { | release = 4.0 |
		audiosrc.set(\gate, 0, \rel, release);
		instrsrc.set(\gate, 0, \rel, release);
/*		TempoClock.sched(release, {
			this.freeosc;
			this.freetrackinginfo;
			nil;
		});*/
		this.freeosc;
		this.freetrackinginfo;

    } //--//

    //------------------//
    // PLAY
    //------------------//
    // This function plays the sounds of the same group at the same rate.
    play { | rate = 1 |

        audiosrc.set(\rate, rate);

    } //--//

}
