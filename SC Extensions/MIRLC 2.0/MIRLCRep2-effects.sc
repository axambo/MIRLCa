+ MIRLCRep2 {

	//------------------//
    // LPF
    //------------------//
    // This public function applies a low pass filter.
    lowpf { | freq = 440 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

		effects.add (0 -> Synth.new(\lowpass_filter, [\obs, 0, \ibs, b0, \freq, freq], g1));


    } //--//

	//------------------//
    // HPF
    //------------------//
    // This public function applies a high pass filter.
    highpf { | freq = 1200 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

        effects.add (0 -> Synth.new(\highpass_filter, [\obs, 0, \ibs, b0, \freq, freq], g1));

    } //--//

	//------------------//
    // BPF
    //------------------//
    // This public function applies a band pass filter.
   bandpf { | freq = 440 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

        effects.add (0 -> Synth.new(\bandpass_filter, [\freq, freq, \obs, 0, \ibs, b0], g1));

    } //--//


	//------------------//
    // REVERB
    //------------------//
    // This public function applies a reverb.
    reverb { | mix = 0.5 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);
		effects.add (0 -> Synth.new(\rverb, [\obs, 0, \ibs, b0, \mix, mix], g1));


    } //--//

	//------------------//
    // DELAY
    //------------------//
    // This public function applies a delay.
    delay { | decay = 3 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

		effects.add (0 -> Synth.new(\dlay, [\obs, 0, \ibs, b0, \decay, decay], g1));
    } //--//

	//------------------//
    // VIBRATO
    //------------------//
    // This public function applies a phaser.
    vibrato { | maxdelaytime = 0.01 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

		effects.add (0 -> Synth.new(\vibrato, [\obs, 0, \ibs, b0, \maxdelaytime, maxdelaytime], g1));
    } //--//

	//------------------//
    // BITCRUSH
    //------------------//
    // This public function applies a bitcrush filter.
    bitcrush { | bit = 4 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

		effects.add (0 -> Synth.new(\bitcrush, [\obs, 0, \ibs, b0, \bit, 4], g1));

    } //--//

	//------------------//
    // DISTORTION
    //------------------//
    // This public function applies a distortion
    distort { | distortlevel = 10 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

		effects.add (0 -> Synth.new(\distort, [\obs, 0, \ibs, b0, \distortlevel, distortlevel], g1));
    } //--//

	//------------------//
    // RING MODULATOR
    //------------------//
    // This public function applies a ring modulator.
    rmod { | freq = 700 |

		effectson = 1;
        size = synths.size;
        size.do( { |index|
            synths[index].set(\out, b0);
            this.printsynth(index);
        });

		if ( effects.size>0,
			{
				effects[0].free;
			}
		);

		effects.add (0 -> Synth.new(\rmod, [\obs, 0, \ibs, b0, \freq, freq], g1));
    } //--//

}
