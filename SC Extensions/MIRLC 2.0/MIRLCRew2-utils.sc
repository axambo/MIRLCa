 + MIRLCRew2 {

	//------------------//
    // FREE
    //------------------//

	freeosc {
		if ( osctr.notNil,
			{ osctr.free; osctr = nil });
	}

	freeinstr {

		if ( previnstrtype.notNil, {
			if ( ( previnstrtype == \melodic && instrtype == \percussive ) || ( previnstrtype == \melodic && instrtype == \melodic ), {
			instrsrc.free;
			instrsrc = nil;
			});
		});
	}

	freetrackinginfo {

		if (beattracking.notNil, {beattracking.free; beattracking = nil;});
		if (onsetstracking.notNil, {onsetstracking.free; onsetstracking = nil;});
		if (ampstracking.notNil, {ampstracking.free; ampstracking = nil;});
		if (pitchtracking.notNil, {pitchtracking.free; pitchtracking = nil;});
		if (keytracking.notNil, {keytracking.free; keytracking = nil;});
		if (freqtracking.notNil, {freqtracking.free; freqtracking = nil;});
	}

	freefx {
		/*if( effectsig.notNil,
		{ effectsig.free; effectsig = nil });*/
	}

	freeaddons {
		this.freeosc;
		this.freeinstr;
		this.freetrackinginfo;
	}

	free {
		g0.free; g1.free; g2.free; g3.free; g4.free;
	} //--//

   //------------------//
    // SCOPE
    //------------------//
	plotserver {
		//server.scope;
		server.plotTree;
		server.queryAllNodes;
	} //--//

    //------------------//
    // CMD PERIOD
    //------------------//
    // This function is activated when stopping the code / recompiling / etc.
    cmdPeriod {
		this.free;
        currentEnvironment.clear;
    } //--//

}