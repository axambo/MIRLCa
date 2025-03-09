
+ MIRLCRew2 {

	//------------------//
    // SOURCE
    //------------------//
	source { | amp = 1 |

		overlayedsound = 1;
		miredsound = 0;
		"Listening to the original sound source...".postln;

		this.freeaddons;
		this.freefx;

		if ( audiosrc.notNil && overlaysig.notNil,
			{
				audiosrc.set(\amp, amp, \obs, b1);
				overlaysig.set(\obs, 0, \amp1, amp, \amp2, 0, \ibs1, b1, \ibs2, b2);

		},
		{
			audiosrc = Synth.new(\sourcedef, [\amp, amp, \obs, b1], g0);
			overlaysig = Synth.new(\overlaydef, [\obs, 0, \ibs1, b1, \ibs2, b2, \amp1, amp, \amp2, 0], g3);
		}
		);

	} //--//



	 //------------------//
	// OVERLAY (of the original source)
    //------------------//
	overlay { | mode = \on, amp1, amp2 |
		var ampsource, ampmired;

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },
			{

				if ( amp1.isNil, {
					case
					{ mode == \off } { amp1 = 1 }
					{ mode == \on } { amp1 = 1 }
					{ mode ==\only } { amp1 = 0 }

				});
				if ( amp2.isNil, {
					case
					{ mode == \off } { amp2 = 0 }
					{ mode == \on } { amp2 = 1 }
					{ mode ==\only } { amp2 = 1 }
				});

				ampsource = amp1;
				ampmired = amp2;

				case
				{ mode == \off } {
					"Now playing: Only original source...".postln;
					if ( overlaysig.notNil,
						{
							overlayedsound = 0;
							overlaysig.set(\obs, 0, \amp1, ampsource, \amp2, ampmired, \ibs1, b1, \ibs2, b2);
					} );
				} // end mode 'off'

				{ mode == \on } {
					if ( miredsound == 0,
						{ "ERROR: You must call a function using MIR first (e.g., onsets amps, beats)".postln },
						{
							"Now playing: Both original source + MIR...".postln;
						}
					);

					if ( overlaysig.notNil,
						{
							overlayedsound = 1;
							overlaysig.set(\obs, 0, \amp1, ampsource, \amp2, ampmired, \ibs1, b1, \ibs2, b2);
					});
				} // end mode 'on'

				{ mode == \only } {
					if ( miredsound == 0,
						{ "ERROR: You must call a function using MIR first (e.g., onsets amps, beats)".postln },
						{
							"Now playing: Only MIR...".postln;
						}
					);

					if ( overlaysig.notNil,
						{
							overlayedsound = 0;
							overlaysig.set(\obs, 0, \amp1, ampsource, \amp2, ampmired, \ibs1, b1, \ibs2, b2);
					});
				} // end mode 'only'


			}

		);

	} //--//

}
