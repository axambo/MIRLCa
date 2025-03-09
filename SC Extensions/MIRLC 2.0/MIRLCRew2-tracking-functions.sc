+ MIRLCRew2 {

    //------------------//
    // ONSETS
    //------------------//
	onsets { | instr = \grain, amp = 1 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },
			{
				var onsetin;
				instrtype = \percussive;
				miredsound = 1;

				"INFORM: You selected onsets".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						this.overlay(\only, amp1: 0, amp2: amp);
					},
					{
						this.overlay(\on, amp1: 0.5, amp2: 0.5);
				});

				onsetstracking = Synth.new(\onsetsdef, [\ibs, b1], g1);

				osctr = OSCFunc({ | msg, time |

					onsetin = msg[3];
					("onset:"+onsetin).postln;

					case
					{instr == \sin}
					{
						instrsrc =  Synth.new(\sindef, [\amp, amp, \obs, b2, \onset, 1, \freq, rrand(90, 400)], g2);

					}
					{instr == \lfo}
					{
						instrsrc =  Synth.new(\lfodef, [\amp, amp, \obs, b2, \onset, 1], g2);

					}
					{instr == \grain}
					{
						instrsrc =  Synth.new(\graindef, [\amp, amp, \obs, b2, \onset, 1], g2);
					};

				},'/tronsets', server.addr);

				previnstrtype = instrtype;

		});

	} //--//



    //------------------//
    // AMPS
    //------------------//
	amps { | instr = \grain, amp = 1 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },
			{
				var ampin, normampin;
				instrtype = \percussive;
				miredsound = 1;

				"INFORM: You selected amps".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						overlaysig.set(\obs, 0, \amp1, 0, \amp2, amp);
					},{
						overlaysig.set(\obs, 0, \amp1, 0.5, \amp2, 0.5);
				});

				ampstracking = Synth.new(\ampsdef, [\ibs, b1], g1);

				osctr = OSCFunc({ | msg, time |

					ampin = msg[3];

					("amps:"+ampin).postln;

					normampin = ControlSpec(0.0, ampin, \lin).unmap(ampin);

					case
					{instr == \sin}
					{
						instrsrc =  Synth.new(\sindef, [\amp, normampin, \obs, b2, \onset, 1, \freq, rrand(90, 400)], g2);
					}
					{instr == \lfo}
					{
						instrsrc =  Synth.new(\lfodef, [\amp, normampin, \obs, b2, \onset, 1], g2);
					}
					{instr == \grain}
					{
						instrsrc =  Synth.new(\graindef, [\amp, normampin, \obs, b2, \onset, 1], g2);
					};


				},'/tramps', server.addr);

				previnstrtype = instrtype;
		});

	} //--//

    //------------------//
    // BEATS
    //------------------//
	beats { | amp = 0.1, w1 = 0.6, w2 = 0.25, w3 = 0.15, rate = 8 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },
			{
				var beatin, synthlist, instr;
				instrtype = \percussive;
				miredsound = 1;

				"INFORM: You selected beats".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						this.overlay(\only, amp1: 0, amp2: amp);
					},
					{
						this.overlay(\on, amp1: 0.5, amp2: 0.5);
				});

				beattracking =  Synth.new(\beattrkdef, [\ibs, b1], g1);

				osctr = OSCFunc({ | msg, time |

					beatin = msg[3];

					("beats: " + beatin).postln;

					// (time%numspeakers).asInteger.postln;

					synthlist = [\kickdrum, \openhat, \closedhat];

					instr = synthlist.wchoose([w1, w2, w3]);

					case
					{instr == \kickdrum}
					{
						instrsrc = Synth(\kickdrum, [\obs, (time%rate).asInteger, \amp, amp], g2);

					}
					{instr == \openhat}
					{
						instrsrc = Synth(\openhat, [\obs, (time%rate).asInteger, \amp, amp], g2);
					}
					{instr == \closedhat}
					{
						instrsrc = Synth(\closedhat, [\obs, (time%rate).asInteger, \amp, amp], g2);
					}

				},'/trbeats', server.addr);

				previnstrtype = instrtype;
		});

	} //--//


    //------------------//
    // KEY
    //------------------//
	key { | instr = \chords, amp = 1 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },
			{
				var keyin;
				var initkey = 1;
				instrtype = \melodic;
				miredsound = 1;

				"INFORM: You selected key".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						this.overlay(\only, amp1: 0, amp2: amp);
					},
					{
						this.overlay(\on, amp1: 0.5, amp2: 0.5);
				});


				keytracking = Synth.new(\keydef, [\ibs, b1], g1);

				osctr = OSCFunc({ | msg, time |

					keyin = msg[3];

					("key: " + keyin).postln;

					if ( initkey == 1,
						{
							instrsrc = Synth(\chords, [\obs, b2, \key, keyin, \option, 0 ], g2);
							initkey = 0;
						}, {
							if ( keyin < 12, {
								instrsrc.set(\option, 0);
							},
							{
								instrsrc.set(\option, 1);
							});
					});

					},'/trkey', server.addr);

				previnstrtype = instrtype;

		});

	} //--//


    //------------------//
    // PITCH
    //------------------//
	pitch { | instr = \resonators, amp = 1 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },
			{

				var pitchin;
				instrtype = \melodic;
				miredsound = 1;

				"INFORM: You selected pitch".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						this.overlay(\only, amp1: 0, amp2: amp);
					},
					{
						this.overlay(\on, amp1: 0.5, amp2: 0.5);
				});

				pitchtracking =  Synth.new(\pitchdef, [\ibs, b1], g1);

				osctr = OSCFunc({ | msg, time |

				pitchin = msg[3];

				("pitch: " + pitchin).postln;

				case
				{ instr == \resonators } {
						instrsrc = Synth(\resonators, [\obs, b2, \freq, pitchin, \pos, { 1.0.rand } ], g2);
					}
				{ instr == \resonators2 } {
						instrsrc = Synth(\resonators2, [\obs, b2, \freq, pitchin, \pos, { 1.0.rand } ], g2);
				}

				},'/trpitch', server.addr );

				previnstrtype = instrtype;

		});

	} //--//


    //------------------//
    // FREQ
    //------------------//
	freq { | instr = \resonators, amp = 1 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },{

				var freqin;
				instrtype = \melodic;
				miredsound = 1;

				"INFORM: You selected freq".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						this.overlay(\only, amp1: 0, amp2: amp);
					},
					{
						this.overlay(\on, amp1: 0.5, amp2: 0.5);
				});

				freqtracking =  Synth.new(\freqdef, [\ibs, b1], g1);

				osctr = OSCFunc({ | msg, time |

				freqin = msg[3];

				("freq: " + freqin).postln;

				case
				{ instr == \resonators } {
						instrsrc = Synth(\resonators, [\obs, b2, \freq, freqin, \pos, { 1.0.rand } ], g2);
					}
				{ instr == \resonators2 } {
						instrsrc = Synth(\resonators2, [\obs, b2, \freq, freqin, \pos, { 1.0.rand } ], g2);
				}

				},'/trfreq', server.addr );

 				previnstrtype = instrtype;

		});

	} //--//

	//-------------------//
	// BINARY OPERATIONS
	//-------------------//

	biop { | bomode = 0, amp = 1 |

		if ( audiosrc.isNil,
			{ "ERROR: You must start with x.source method (assuming MIRLCRew2 has been instantiated with MIRLCRew2.new method)".postln },{

				instrtype = \melodic;
				miredsound = 1;

				"INFORM: You selected binary operator".postln;

				this.freeaddons;

				if ( overlayedsound == 0,
					{
						this.overlay(\only, amp1: 0, amp2: amp);
					},
					{
						this.overlay(\on, amp1: 0.5, amp2: 0.5);
				});

				instrsrc = Synth.new(\binaryop, [\obs, b2, \bomode, bomode]);

				previnstrtype = instrtype;
		}
		);
	}

}	