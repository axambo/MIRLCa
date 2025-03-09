MIRLCa : MIRLCRep2 {

	var test_dataset;
	var stand_test_dataset;
	var test_predicted_label_dataset;
	var training_dataset;
	var training_label_dataset;
	var test_dataset_fixed;
	var test_label_dataset_fixed;
	var stand_dataset;
	var stand_test_dataset_fixed;
	var standardizer;
	var pca;
	var classifier;
	var is_training;
	var mode_training;
	var temp_list_training;
	var sndid_t;
	var sndid_old_t;
	var manual_dataset_dict;
	var training_dict;
	var training_label_dict;
	var test_dict;
	var test_label_dict;
	var modelfilepath;
	var archiving;

	*new { | backend = 0, dbSize = 571613, path = (Platform.defaultTempDir), creditsPath = (Platform.defaultTempDir), modelsPath = (Platform.defaultTempDir) |
		^super.new(backend, dbSize, path, creditsPath).initagent(modelsPath);
    }

	initagent { | modelsPath |
		var randomnum = 100000.rand;
		var test_dataset_name = \mlpclassify_testdata++randomnum.asSymbol;
		var stand_test_dataset_name = \mlpclassify_stand_test_data++randomnum.asSymbol;
		var test_predicted_label_dataset_name = \mlpclassify_predictlabels++randomnum.asSymbol;
		var mlpclassify_trainingdata = \mlpclassify_trainingdata++randomnum.asSymbol;
		var mlpclassify_labels = \mlpclassify_labels++randomnum.asSymbol;
		var mlpclassify_testdata = \mlpclassify_testdata++randomnum.asSymbol;
		var mlpclassify_testlabels = \mlpclassify_testlabels++randomnum.asSymbol;
		var mlpclassify_standdata = \mlpclassify_standdata++randomnum.asSymbol;
		var mlpclassify_stand_test_data = \mlpclassify_stand_test_data++randomnum.asSymbol;


		modelfilepath = modelsPath;
		is_training = false;
		manual_dataset_dict = Dictionary.new;
		training_dict = Dictionary();
		training_label_dict = Dictionary();
		test_dict = Dictionary();
		test_label_dict = Dictionary();


		server.waitForBoot {

		"I'm an agent: 🤖".postln;

		fork {
		training_dataset.free;
		training_dataset = FluidDataSet(server, mlpclassify_trainingdata); server.sync;
		training_label_dataset.free;
		training_label_dataset = FluidLabelSet(server, mlpclassify_labels); server.sync;
		test_dataset_fixed.free;
		test_dataset_fixed = FluidDataSet(server, mlpclassify_testdata); server.sync;
		test_label_dataset_fixed.free;
		test_label_dataset_fixed = FluidLabelSet(server, mlpclassify_testlabels); server.sync;
		test_dataset.free;
		test_dataset = FluidDataSet(server, test_dataset_name); server.sync;
		stand_dataset.free;
		stand_dataset = FluidDataSet(server, mlpclassify_standdata); server.sync;
		stand_test_dataset_fixed.free;
		stand_test_dataset_fixed = FluidDataSet(server, mlpclassify_stand_test_data);
		stand_test_dataset.free;
		stand_test_dataset = FluidDataSet(server, stand_test_dataset_name); server.sync;
		test_predicted_label_dataset.free;
		test_predicted_label_dataset = FluidLabelSet(server, test_predicted_label_dataset_name); server.sync;
		standardizer.free;
		standardizer = FluidStandardize(server); server.sync;
		pca.free;
		pca = FluidPCA(server,20); server.sync;
		classifier.free;
		classifier = FluidMLPClassifier(server, [14], FluidMLPClassifier.relu, 10000, 0.01, 0.9, 10, 0.2);
		server.sync;

		//Checking if the model files exist: standardizer.JSON, pca.JSON and model.JSON
		if ( File.exists( modelfilepath ++ "standardizer.JSON" ),
			{ standardizer.read(modelfilepath ++ "standardizer.JSON"); },
			{ postln("ERROR: standardizer.JSON file was not found in the directory "++ modelfilepath) }
		);

		if ( File.exists(modelfilepath ++ "pca.JSON" ),
			{ pca.read(modelfilepath ++ "pca.JSON"); },
			{ postln("ERROR: pca.JSON file was not found in the directory "++ modelfilepath) }
		);

		if ( File.exists(modelfilepath ++ "model.JSON" ),
			{ classifier.read(modelfilepath ++ "model.JSON"; ); },
			{ postln("ERROR: model.JSON file was not found in the directory "++ modelfilepath) }
		);

		}; // End fork

		}; // End server.waitForBoot

	}

}

