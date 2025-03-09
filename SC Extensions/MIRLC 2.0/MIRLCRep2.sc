MIRLCRep2 {

    classvar <>server;
    classvar <>file;
    classvar <>date;
    classvar <>debugging;
    var metadata, <buffers, synths, effects, <translation;
    var snd, preview, buf, target, sequential, prevplaymode, granular;
    var poolsizeold, index, keys, counter, sndid, numsnds, size, rndnums;
	var oscreceiver;
    var window, viewoscil;
    var backendClass;
    var databaseSize;
    var directoryPath;
	var g0, g1;
	var b0, b1, b2;
	var fxlpf;
	var effectson;
    var playing;
	var creditsfilename;
	var creditsfilepath;
	var <>currentrate;

	var maxvol =0.5;

	*new {|backend = 0, dbSize = 571613, path = (Platform.defaultTempDir), creditsPath = (Platform.defaultTempDir) |
        ^super.new.init(backend, dbSize, path, creditsPath)
    }

    init {|backend, dbSize, path, creditsPath|
        server = Server.local;
        server.boot;
        metadata = Dictionary.new;
        buffers = Dictionary.new;
        synths = Dictionary.new;
		effects = Dictionary.new;
        translation = Dictionary.new;
        debugging = false; // true
        poolsizeold = 0;
        counter = 0;
        sequential = false;
		prevplaymode = nil;
		granular = false;
        databaseSize = dbSize;
        directoryPath = path;
		effectson = 0;
        playing = true;
		creditsfilepath = creditsPath;
		date = Date.getDate;
		currentrate = 1;

        if(backend == 0){
			try {
				backendClass = FSSound;
			} // end try
			{|error| [\catchFreesoundClass, error].postln };
			try {
            Freesound.authType = "token"; // default, only needed if you changed it
            Freesound.token="<your_api_key>"; // change it to own API key token. To request a Freesound API key token go here: https://freesound.org/apiv2/apply
			} // end try
			{|error| [\catchFreesoundToken, error].postln };
        }{
			try {
				backendClass = FLSound;
			} //end try
			{|error| [\catchFlucomaSound, error].postln };
        };

		// Management of credits file
		try {
			if (creditsfilepath != nil, {
				creditsfilename = creditsfilepath.standardizePath ++ date.stamp ++ "_credits" ++ ".txt";
			}, {

			});
		} // end try
		{|error| [\catchFileName, error].postln };
		try {
			file = File(creditsfilename,"a");
			file.write("Sound samples used from Freesound.org:\n");
			file.close;
		} // end try
		{|error| [\catchFileWrite, error].postln };
		// end of management of credits file

        this.argstranslate;

		server.waitForBoot {

			g0 = Group.new(server);
			g1 = Group.after(g0);
			b0 =  Bus.audio(server);

		SynthDef(\synth_mono_fs, {
            | bufnum, buf, amp = 1, out = 0, rate = 1, da = 0, loop = 1, trig = 0, gate = 1, atk = 0.1, sus = 1, rel = 1.0 |
            var sig, env, done;
            sig = PlayBuf.ar(1, bufnum, BufRateScale.kr(buf) * rate,  doneAction: da, loop: loop, trigger: trig);
			done = Done.kr(sig);
			env = EnvGen.ar(Env.asr(atk,sus,rel), gate, doneAction:2);
			sig = sig * amp * env;
			// effective only in sequential mode
			SendReply.kr(done, '/bufDone', [bufnum]);
			//
			Out.ar(out, sig!2);
        }).add;


		// Filters

		SynthDef(\lowpass_filter, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, freq = 440 |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = LPF.ar(source, freq);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef(\highpass_filter, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, freq = 1200 |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = HPF.ar(source, freq);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef(\bandpass_filter, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, freq |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = BPF.ar(source, freq);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;


		SynthDef(\rverb, { | ibs, obs, amp = 1, atk = 0.02, sus = 1, rel = 0.1, crv = 2, gate = 1, mix = 0.5, rm = 0.5,	damp = 0 |
			// mix: dry/wet balance 0..1
			// rm: room size 0..1
			// damp: reverb HF damp 0..1
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = FreeVerb.ar(source, mix, rm, damp);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef.new(\dlay, { | ibs = 0, obs = 0, amp = 0.8, maxdeltime = 0.5, mix = (-0.5), decay = 3, delHz = 0.25, delMin = 0.1, delMax = 0.4 |
			// No envelope needed here
			var sig, delaytime, delay;
			sig = In.ar([ibs, ibs]);
			delaytime = SinOsc.kr([delHz, delHz*0.9]).exprange(delMin, delMax);
			delay = CombL.ar(sig, maxdeltime, delaytime, decay);
			sig = XFade2.ar(sig, delay, mix) * amp;
			Out.ar(obs, sig);
		}).add;


		SynthDef(\vibrato, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, maxdelaytime = 0.01 |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = DelayC.ar(source, maxdelaytime, SinOsc.ar(Rand(5,10),0,0.0025,0.0075));
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef(\bitcrush, { | ibs, obs, amp = 0.4, atk = 0.02, sus = 1, rel = 0.1, crv = 2, gate = 1, bit = 4 |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = (source*(2.pow(bit))).round/2.pow(bit);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

			SynthDef(\distort, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, distortlevel = 10 |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = 0.1*((distortlevel*source).clip2);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef(\rmod, { | ibs, obs, amp = 0.5, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, maxdelaytime = 0.01, freq = 700 |
			var source, sig, env, rmod;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			rmod = source * SinOsc.ar(freq);
			sig = ( source + rmod );
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef(\compress, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, gain = 1.5, threshold = 0.5, slopebelow = 1, slopeabove = 0.5 |
			var source, sig, env;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			sig = CompanderD.ar(source, threshold, slopebelow, slopeabove);
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		this.printdirectories;

		};


    } //--//

}
