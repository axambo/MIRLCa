Latest update: 8.3.2025


MIRLCa
===
A SuperCollider extension that inherits from MIRLCRep2 class and expands its capabilities by proposing a virtual agent that embodies machine learning techniques.

(c) 2020-2025 by Anna Xambó Sedó (<a.xambosedo@qmul.ac.uk>).


Introduction
----

The extension MIRLCa.sc is a follow-up work-in-progress of `MIRLCRep.sc` and `MIRLCRep2.sc`. It includes a virtual agent that suggests similar sounds from Freesound.org based on your musical taste. This extension is still under development.

Application Start
----

Drag the `MIRLCa` folder to the Extensions folder of SuperCollider (suggested to create a subfolder with the same name): `/Users/{username}/Library/Application Support/SuperCollider/Extensions` (in Mac). If you are unsure where is this folder, open SuperCollider and type: `Platform.userExtensionDir`

You will also need 3 JSON files generated from the FluCoMa library `model.JSON`, `standardizer.JSON`, and `pca.JSON`), which need to be saved in your MIRLC folder (if you are unsure just use the default temp directory `path = (Platform.defaultTempDir)`). A default collection of 3 files has been generated so that you can try the extension.

Remember to either recompile the class library (`Language>Recompile Class Library`) or restart SuperCollider.


Funding
----

This work was partially developed within the project *MIRLCAuto: A Virtual Agent for Music Information Retrieval in Live Coding* funded by the EPSRC HDI Network Plus Grant (EP/R045178/1).


License
----

BSD 3-Clause License.
