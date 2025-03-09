// Latest update: 8.12.2022
// Author: Anna Xambó

// instantiation

a = MIRLCRew2.new // audio in mode, mod: 0 by default (make sure to wear headphones and to connect a microphone)
// or
a = MIRLCRew2.new(mod:1) // audio sample by default: "sounds/a11wlk01.wav"
// or
a = MIRLCRew2.new(mod: 1, filename: "Users/anna/Documents/Work/Detuning/AudioFiles/7/tuning-a4-a0_mono.wav", nchannels: 1) // path to a sound sample


// sound source

a.source // pure audio source, you always need to start with this function


// sound from audio features

a.onsets
a.onsets(instr: \sin, amp: 4);
a.overlay(\only);
a.onsets(instr: \sin, amp: 4);
a.onsets(instr: \lfo, amp: 2);
a.onsets(instr: \grain, amp: 1);
a.overlay(mode: \on, amp1: 0.1, amp2: 0.9)

a.amps;
a.overlay(\only);
a.amps(instr: \sin, amp: 10);
a.amps(instr: \lfo, amp: 8);
a.amps(instr: \grain, amp: 10);
a.overlay(mode: \on, amp1: 0.1, amp2: 0.9)

a.beats
a.beats(amp:0.2)
a.beats(amp:0.2, w1:1, w2:0, w3:0)
a.beats(amp:0.2, w1:0.6, w2:0.25, w3:0.25)
a.beats(amp:0.6, w1:1, w2:0, w3:0, rate: 8)
a.overlay(\only)
a.overlay(mode: \on, amp1: 0.2, amp2: 0.8)

a.key
a.overlay(\only);
a.overlay(\on, amp1: 0.2, amp2: 2);

a.pitch
a.overlay(\only);
a.overlay(\on, amp1: 0.1, amp2: 0.9);
a.pitch(instr: \resonators2); a.overlay(\on, amp1: 0.1, amp2: 0.9);
a.pitch(instr: \resonators); a.overlay(\on, amp1: 0.1, amp2: 0.9);

a.freq
a.freq(instr: \resonators2)
a.freq(instr: \resonators)
a.overlay(\only);
a.overlay(\on, amp1: 0.2, amp2: 0.8);

a.biop(bomode: 0);
a.biop(bomode: 1);
a.overlay(\only);
a.overlay(\on, amp1: 0.1, amp2: 0.9);


a.volume(0.2);
a.fadeout(10);
a.free;

