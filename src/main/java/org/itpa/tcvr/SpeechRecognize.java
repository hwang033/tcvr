package org.itpa.tcvr;

import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

public class SpeechRecognize {
	private final static String AcousticModelPath = "resource:/edu/cmu/sphinx/models/en-us/en-us";
	private final static String DictionaryPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";

	private Configuration configuration = new Configuration();
	private LiveSpeechRecognizer recognizer;
	private SVMPredictCmd cmd_classifier;
	private DependenciesPaser dep_parser;
	private EntityFilter efilter;
	private UrlGenerator urlgen;

	public SpeechRecognize(SVMPredictCmd cmdClassifier, DependenciesPaser dp,
			EntityFilter ef, UrlGenerator ug) throws IOException {
		// Set path to acoustic model.
		configuration
				.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		// Set path to dictionary.
		configuration
				.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		// Set language model.
		// configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");
		// configuration.setLanguageModelPath("file:c:/itpa.gram");
		configuration.setGrammarPath("resource:/grammar");
		configuration.setUseGrammar(true);
		configuration.setGrammarName("itpa");
		recognizer = new LiveSpeechRecognizer(configuration);

		this.cmd_classifier = cmdClassifier;
		this.dep_parser = dp;
		this.efilter = ef;
		this.urlgen = ug;
	}

	public String getSpeechResult() {
		// Start recognition process pruning previously cached data.

		recognizer.startRecognition(true);

		String speech_text = "";
		SpeechResult result = null;

		System.out.println("start!");

		while ((result = recognizer.getResult()) != null) {
			speech_text = result.getHypothesis();
			speech_text = efilter.Filter(speech_text);
			System.out.println(speech_text);
			// System.out.println(cmd_classifier.PredictRawString(speech_text));
			
			System.out.println(dep_parser.getPaser(speech_text));
			System.out.println(urlgen.GetUrl(
					cmd_classifier.PredictRawString(speech_text),
					speech_text,
					dep_parser.getSemanticGraph(speech_text)));

			System.out.println("start!");
		}

		// Pause recognition process. It can be resumed then with
		// startRecognition(false).
		recognizer.stopRecognition();

		return speech_text;

	}

}
