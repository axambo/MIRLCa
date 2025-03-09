+ MIRLCRew2 {

	//------------------//
    // LPF
    //------------------//
    // This public function applies a low pass filter.
	lowpf { | freq = 440 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \lowpass_filter, [\obs, 0, \ibs, b3, \freq, freq], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \lowpass_filter, [\obs, 0, \ibs, b3, \freq, freq], g4);
		});
	}

	//--//

	//------------------//
    // HPF
    //------------------//
    // This public function applies a high pass filter.
	highpf { | freq = 1200 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \highpass_filter, [\obs, 0, \ibs, b3, \freq, freq], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \highpass_filter, [\obs, 0, \ibs, b3, \freq, freq], g4);
		});
	}

	//--//

	//------------------//
    // BPF
    //------------------//
    // This public function applies a band pass filter.
	bandpf { | freq = 440 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \bandpass_filter, [\freq, freq, \obs, 0, \ibs, b3], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \bandpass_filter, [\freq, freq, \obs, 0, \ibs, b3], g4);
		});
	}

	//--//

	//------------------//
    // REVERB
    //------------------//
    // This public function applies a reverb.
	reverb { | mix = 0.5 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \rverb, [\obs, 0, \ibs, b3, \mix, mix], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \rverb, [\obs, 0, \ibs, b3, \mix, mix], g4);
		});

	}

	//--//

	//------------------//
    // DELAY
    //------------------//

	delay { | decay = 3 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \dlay, [\obs, 0, \ibs, b3, \decay, decay], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \dlay, [\obs, 0, \ibs, b3, \decay, decay], g4);
		});
	}

	//--//

	//------------------//
    // VIBRATO
    //------------------//
    // This public function applies a vibrato.
	vibrato { | maxdelaytime = 0.01 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \vibrato, [\obs, 0, \ibs, b3, \maxdelaytime, maxdelaytime], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \vibrato, [\obs, 0, \ibs, b3, \maxdelaytime, maxdelaytime], g4);
		});
	}

	//--//


	//------------------//
    // BITCRUSH
    //------------------//
    // This public function applies a bitcrush filter.
	bitcrush { | bit = 4 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \bitcrush, [\obs, 0, \ibs, b3, \bit, 4], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \bitcrush, [\obs, 0, \ibs, b3, \bit, 4], g4);
		});
	}

	//--//

	//------------------//
    // DISTORTION
    //------------------//
    // This public function applies a distortion
    distort { | distortlevel = 10 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \distort, [\obs, 0, \ibs, b3, \distortlevel, distortlevel], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \distort, [\obs, 0, \ibs, b3, \distortlevel, distortlevel], g4);
		});

    } //--//


	//------------------//
    // RING MODULATOR
    //------------------//
    // This public function applies a ring modulation effect
	rmod { | freq = 700 |

		if (effectsig.notNil, {
			effectsig.free;
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \rmod, [\obs, 0, \ibs, b3, \freq, freq], g4);
		}, {
			overlaysig.set(\obs, b3);
			effectsig =  Synth.new( \rmod, [\obs, 0, \ibs, b3, \freq, freq], g4);
		});
	}



}	