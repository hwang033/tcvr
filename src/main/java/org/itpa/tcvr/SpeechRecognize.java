package org.itpa.tcvr;

import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

public class SpeechRecognize {
	private final static String  AcousticModelPath = "resource:/edu/cmu/sphinx/models/en-us/en-us";
	private final static String  DictionaryPath = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
	
	private Configuration configuration = new Configuration();
	private LiveSpeechRecognizer recognizer;
	private SVMPredictCmd cmd_classifier;
	private DependenciesPaser dep_parser;
	
	public SpeechRecognize(SVMPredictCmd cmdClassifier, DependenciesPaser dp) throws IOException {
	   	// Set path to acoustic model.
    	configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
    	// Set path to dictionary.
    	configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
    	// Set language model.
    	//configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");
    	//configuration.setLanguageModelPath("file:c:/itpa.gram");
    	configuration.setGrammarPath("resource:/grammar");
        configuration.setUseGrammar(true);
        configuration.setGrammarName("itpa");
        recognizer = new LiveSpeechRecognizer(configuration);
        cmd_classifier = cmdClassifier;
        this.dep_parser = dp;
	}

	public String getSpeechResult(){
       	// Start recognition process pruning previously cached data.
		
		recognizer.startRecognition(true);
    	
    	String speech_text = "";
    	SpeechResult result = null;
    	
    	System.out.println("start!");
    	
    	while ((result = recognizer.getResult()) != null) {
    		speech_text = result.getHypothesis();
    		System.out.println(speech_text);
    		System.out.println(cmd_classifier.PredictRawString(speech_text));
    		System.out.println(dep_parser.getPaser(speech_text));
//    		System.out.println(result.getLattice());
//    		speech_text = "";
//		   	for (WordResult r : result.getWords()) {
//		   	
//		   		System.out.println(r.toString());
//		   		//r.toString();
//		   		int comma_idx =  r.toString().indexOf(",");
//		   		if(comma_idx != -1){
//		   			speech_text += r.toString().substring(1, comma_idx);
//		   			speech_text += " ";
//		   		}
//		   	}
//		   	System.out.println(speech_text);
		   	System.out.println("start!");
    	}
    
    	// Pause recognition process. It can be resumed then with startRecognition(false).
    	recognizer.stopRecognition();
    	
		return speech_text;
    	

	}

}
