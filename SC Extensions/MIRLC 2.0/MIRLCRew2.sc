
MIRLCRew2 {

    classvar <server;
	classvar <>mode, <>soundfile;
	classvar numspeakers = 2;
	var maxvol = 1;
	var numchannels = 1;
	var bufferin, bufferbeattrack, bufferonsets, bufferkeytrack;
	var audiosrc, effectsig, overlaysig;
	var osctr;
	var miredsound;
	var overlayedsound;
	var instrsrc;
	var instrtype, previnstrtype;
	var beattracking, onsetstracking, ampstracking, pitchtracking, keytracking, freqtracking;
	var g0, g1, g2, g3, g4;
	var <b1, <b2, <b3;

	*new { |mod, filename, nchannels |
		^super.new.init(mod, filename, nchannels);
    }

    //------------------//
    // INIT
    //------------------//
    init { | mod, filename, nchannels |
        server = Server.local;
        server.boot;

		server.waitForBoot({

			// Groups
			g0 = Group.new(server); // audio source
			g1 = Group.after(g0); // tracking
			g2 = Group.after(g1); // instruments
			g3 = Group.after(g2); // effects
			g4 = Group.after(g3); // overlay

			// Buses
			b1 = Bus.audio(server, numspeakers); // Bus for original signal
			b2 = Bus.audio(server, numspeakers); // Bus for overlayed/mired signal
			b3 = Bus.audio(server, numspeakers); // Bus for effects

			//  Buffers
			bufferbeattrack = Buffer.alloc(server, 1024, numchannels);
			bufferonsets = Buffer.alloc(server, 512, numchannels);
			bufferkeytrack =  Buffer.alloc(server, 4096, numchannels);

			// Check & assign the mode
			if (mod.isNil,
				{
					"mod.isNil".postln;
					mod = 0;
				});
			mode = mod;
			postln("mode: "+mode);

			if ( mode == 0,
				{ "Listening to audio in...".postln },
				{ "Listening to an audio track...".postln }
			);

			// Load the sound
			if ( filename.isNil,
				{ soundfile = Platform.resourceDir +/+ "sounds/a11wlk01.wav" }, // mono file example
				{ soundfile = filename }
			);

			bufferin = Buffer.read(server, soundfile);

			// Check & assign number of channels
			if ( nchannels.isNil, { nchannels = 1 });
			numchannels = nchannels;

			// SynthDefs

           SynthDef (\sourcedef, { | amp = 1, ibs = 0, obs = 0, gate = 1, rate = 1, loop = 1, rel = 1.0 |
				var sig, env;
				var atk = 0.1;
				var sus = 1;
				if (mode == 1, {
					sig = PlayBuf.ar(numchannels, bufferin, BufRateScale.kr(bufferin) * rate, loop: loop);
				},
				{
					sig = SoundIn.ar(ibs, numchannels);
				});
				env = EnvGen.ar(Env.asr(atk,sus,rel), gate, doneAction:2);
				sig = sig * env * amp * maxvol;
				Out.ar(obs, sig);
			}).add;

			SynthDef (\overlaydef, { | amp1 = 0.5, amp2 = 0.5, ibs1=100, ibs2=110, obs = 0 |
				var sig, sig1, sig2;
				sig1 = In.ar(ibs1) * amp1;
				sig2 = In.ar(ibs2) * amp2;
				sig = sig1 + sig2;
				sig = sig * maxvol;
				Out.ar(obs, sig!numspeakers); // this assumes a mono signal which is expanded to multichannel
			}).add;

			// MIR / Machine listening / Analysis

			SynthDef (\onsetsdef, { | ibs = 0,  onsets = 1 |
				var sig, chain;
				sig = In.ar(ibs);
				chain = FFT(bufferonsets, sig);
				onsets = Onsets.kr(chain, threshold: 0.5, odftype: \rcomplex);
				SendReply.kr(onsets, '/tronsets', 1);
			}).add;

			SynthDef (\ampsdef, { | ibs = 0 |
				var sig, amps, trig;
				sig = In.ar(ibs);
				amps = Amplitude.kr(sig);
				trig = Dust.kr(0.5);
				SendReply.kr(trig, '/tramps', amps);
			}).add;

			SynthDef(\beattrkdef, { | ibs = 0, obs = 0, amp = 0.75 |
				var sig;
				var trackb, trackh, trackq, tempo;
				var beep;
				var bsound, hsound, qsound;
				var trig;
				sig = In.ar(ibs);
				#trackb, trackh, trackq, tempo = BeatTrack.kr(FFT(bufferbeattrack.bufnum, sig));
				SendReply.kr(trackb, '/trbeats', 1);
			}).add;

			SynthDef(\keydef, { | ibs = 0 |
				var sig, chain, key, trig;
				sig = In.ar(ibs);
				chain = FFT(bufferkeytrack, sig);
				// key = KeyTrack.kr(chain, 2.0, 0.5).poll(trig: 1, label: \trkey);
				key = KeyTrack.kr(chain, 2.0, 0.5);
				trig = Dust.kr(0.5);
				SendReply.kr(trig, '/trkey', key);
			}).add;

			SynthDef(\freqdef, { |ibs = 0 |
				var sig, chain, centroid, trig;
				sig = In.ar(ibs);
				chain = FFT(LocalBuf(2048), sig, wintype:1);
				centroid = SpecCentroid.kr(chain);
				// centroid.poll(trig: 1, label: \freq);
				trig = Dust.kr(0.5);
				SendReply.kr(trig, '/trfreq', centroid);
			}).add;

			SynthDef(\pitchdef, { |ibs = 0 |
				var sig, trig;
				var ampcurr, freq, hasFreq;
				var synth;
				sig = In.ar(ibs);
				#freq, hasFreq = Pitch.kr(sig, ampThreshold:0.02);
				trig = Dust.kr(0.5);
				SendReply.kr(trig, '/trpitch', freq);
			}).add;

			// Virtual Instruments

			SynthDef(\graindef, { | obs = 0, onset = 0, amp = 1, atk = 0.001, sus = 1, rel = 1 |
				var sig, env;
				sig = Impulse.ar(4, 0.0, 0.2);
				env = EnvGen.kr(Env.perc([atk, sus, rel, -4]), onset, doneAction: 2);
				sig = sig * env * amp;
				Out.ar(obs, sig);
			}).add;

			SynthDef(\sindef, { | obs = 0, freq = 90, onset = 0, amp = 1, rel = 1 |
				var sig;
				var env, envsh, envshape, envlist;
				sig = BPF.ar(SinOsc.ar(SinOsc.ar(freq)));
				envlist = [1, 2, 3, 4];
				envshape = envlist.choose;
				envsh = case
				{ envshape == 1} { Env.perc([0.001, rel, 1, -4]) } // \normal
				{ envshape == 2 } { Env.perc([0.001, rel, 1, -4]) } // \sharp
				{ envshape == 3 } { Env.perc([0.001, rel, 1, -8]) } // \short
				{ envshape == 4 } { Env.perc(1, 0.01, rel, 4) }; // \rev
				env = EnvGen.kr(envsh, onset, doneAction: 2);
				sig = sig * env * amp;
				Out.ar(obs, sig);
			}).send(server);

			SynthDef(\lfodef, { | obs = 0, onset = 0, amp = 1, rel = 1 |
				var sig;
				var env, envsh, envshape, envlist;
				sig = LFPulse.ar(10, 0, 0.2, 0.1)  * SinOsc.ar(440) * 0.1 ;
				envlist = [1, 2, 3, 4];
				envshape = envlist.choose;
				envsh = case
				{ envshape == 1} { Env.perc([0.001, rel, 1, -4]) } // \normal
				{ envshape == 2 } { Env.perc([0.001, rel, 1, -4]) } // \sharp
				{ envshape == 3 } { Env.perc([0.001, rel, 1, -8]) } // \short
				{ envshape == 4 } { Env.perc(1, 0.01, rel, 4) }; // \rev
				env = EnvGen.kr(envsh, onset, doneAction: 2);
				sig = sig * env * amp;
				Out.ar(obs, sig);
			}).send(server);

			SynthDef(\kickdrum, { | obs = 0, amp = 1, rel = 1 |
				var osc, env, sig;
				osc = {SinOsc.ar(60)};
				env = {Line.ar(1, 0, rel, doneAction: 2)};
				sig = osc * env * amp;
				Out.ar(obs, sig);

			}).add;

			SynthDef(\openhat, { | obs = 0, amp = 1, rel = 0.3 |
				var hatosc, hatenv, hatnoise, sig;
				hatnoise = { LPF.ar(WhiteNoise.ar(1),6000) };
				hatosc = { HPF.ar(hatnoise,2000) };
				hatenv = {Line.ar(1, 0, rel, doneAction:2 )};
				sig = hatosc * hatenv * amp;
				Out.ar(obs, sig);

			}).add;

			SynthDef(\closedhat, { | obs = 0, amp = 1, rel = 0.1 |
				var hatosc, hatenv, hatnoise, sig;
				hatnoise = {LPF.ar(WhiteNoise.ar(1),6000)};
				hatosc = {HPF.ar(hatnoise,2000)};
				hatenv = {Line.ar(1, 0, rel, doneAction:2 )};
				sig = hatosc * hatenv * amp;
				Out.ar(obs, sig);

			}).add;

			SynthDef(\resonators, { | freq, obs = 0, amp = 1, pos = 0.5, rel = 4, gate = 1 |
				var osc, sig, env;
				if (numspeakers > 2, {
					osc = PanX.ar(
						numspeakers,
						DynKlang.ar(`[ [freq, freq+(freq*2), freq-(freq/2)], [0.3, 0.3, 0.3], [freq, freq, freq] ]),
						pos) * amp;
				}, {
					osc = Pan2.ar(
						DynKlang.ar(`[ [freq, freq+(freq*2), freq-(freq/2)], [0.3, 0.3, 0.3], [freq, freq, freq] ]),
						pos) * amp;
				});

				env = EnvGen.kr(Env.sine(rel), gate, 0.02, doneAction:2 );
				sig = osc * env * amp;
				Out.ar(obs, sig);
			}).add;

			SynthDef(\resonators2, { | freq, obs = 0, amp = 1, pos = 0.5, rel = 4, gate = 1 |
				var osc, sig, env;
				if (numspeakers > 2, {
					osc = PanX.ar(
					numspeakers,
					DynKlang.ar(`[ [freq, freq+200, freq-200], nil, nil ], 1, 0) + DynKlang.ar(`[ Array.rand(12, freq, freq*2), nil, nil ], 1, 0),
						pos) * amp;
				}, {
					osc = Pan2.ar (
						DynKlang.ar(`[ [freq, freq+200, freq-200], nil, nil ], 1, 0) + DynKlang.ar(`[ Array.rand(12, freq, freq*2), nil, nil ], 1, 0),
						pos) * amp;
				});

				env = EnvGen.kr(Env.sine(rel), gate, 0.02, doneAction:2 );
				sig = osc * env * amp;
				Out.ar(obs, sig);
			}).add;

			SynthDef(\chords, { | keyin, obs = 0, amp = 1, rel = 0.1, gate = 1, option = 0 |
				var sig, env;
				var key, chord1, rootnote1, chord2, rootnote2, sig1, sig2;
				var atk = 0.1;
				var sus = 0.1;
				key = keyin;
				key = Median.kr(101, key);
				chord1 = #[0, 4, 7];
				rootnote1 = key + 60;
				chord2 = #[0, 3, 7];
				rootnote2 = (key-12) + 60;

				sig1 = SinOsc.ar((rootnote1 + chord1).midicps, 0, 0.4).mean;
				sig2 = SinOsc.ar((rootnote2 + chord2).midicps, 0, 0.4).mean;

				sig = Select.ar( option, [sig1, sig2] );

				env = EnvGen.ar (Env.asr( atk, sus, rel ), gate, doneAction: 2);
				sig = sig * env * amp;
				Out.ar(obs, sig);

			}).add;

			SynthDef(\binaryop, { | ibs = 0, obs = 0, amp = 1, bomode = 0, rel = 1, gate = 1 |
				var source1, source2, sig, sig1, sig2, env;
				var atk = 0.1;
				var sus = 1;

				source1 = In.ar(ibs);
				source2 = SinOsc.ar(1.2).round(0.1);

				sig1 = BinaryOpUGen('<=', source1, source2) * 0.1;
				sig2 = BinaryOpUGen('-', source1, source2) * 0.1;

				sig = Select.ar( bomode, [sig1, sig2] );

				env = EnvGen.ar ( Env.asr( atk, sus, rel ), gate: gate, doneAction:2 );
				sig = sig * env * amp;
				Out.ar(obs, sig);
			}).add;


			// Effects


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

			SynthDef(\bandpass_filter, { | ibs, obs, amp = 1, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, freq = 440, rq = 1.0 |
				var source, sig, env;
				env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
				source = In.ar([ibs, ibs]);
				sig = BPF.ar(source, freq, rq);
				sig = sig * env * amp;
				Out.ar(obs, sig);
			}).add;

			SynthDef(\rverb, { | ibs, obs, amp = 1, atk = 0.02, sus = 1, rel = 0.1, crv = 2, gate = 1, mix = 0.5, rm = 0.5, damp = 0 |
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

			SynthDef.new(\dlay, { | ibs = 0, obs = 0, amp = 1, maxdeltime = 0.5, mix = (-0.5), decay = 3, delHz = 0.25, delMin = 0.1, delMax = 0.4 |
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
			// sig = DelayC.ar(source, maxdelaytime, delaytime);
			sig = DelayC.ar(source, maxdelaytime, SinOsc.ar(Rand(5,10),0,0.0025,0.0075));
			sig = sig * env * amp;
			Out.ar(obs, sig);
		}).add;

		SynthDef(\bitcrush, { | ibs, obs, amp = 1, atk = 0.02, sus = 1, rel = 0.1, crv = 2, gate = 1, bit = 4 | // before amp = 0.01
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

		SynthDef(\rmod, { | ibs, obs, amp = 0.5, atk = 0.1, sus = 1, rel = 0.2, crv = 2, gate = 1, freq = 700 |

			var source, sig, env, rmod;
			env = EnvGen.ar(Env.asr(atk, sus, rel, crv), gate);
			source = In.ar([ibs, ibs]);
			rmod = source * SinOsc.ar(freq);
			sig = ( source + rmod ) * env * amp;
			Out.ar(obs, sig);
		}).add;

		}); // end of waitForBoot()
	} //--// end of init()

	// Code Editor GUI Functions here
}
