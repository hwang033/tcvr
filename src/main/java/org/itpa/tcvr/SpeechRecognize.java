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
	
	
	
	public SpeechRecognize() throws IOException {
	   	// Set path to acoustic model.
    	configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
    	// Set path to dictionary.
    	configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
    	// Set language model.
    	//configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp");
    	//configuration.setLanguageModelPath("file:c:/itpa.gram");
    	configuration.setGrammarPath("file:c:/Springwp/tcvr/grammar");
        configuration.setUseGrammar(true);
        configuration.setGrammarName("itpa");
        recognizer = new LiveSpeechRecognizer(configuration);
     
	}



	public String getSpeechResult(){
       	// Start recognition process pruning previously cached data.
		
		recognizer.startRecognition(true);
    	System.out.println("start!");
    	SpeechResult result = recognizer.getResult();
    	String speech_text = "";
    	    
	   	for (WordResult r : result.getWords()) {
	   		//r.toString();
	   		int comma_idx =  r.toString().indexOf(",");
	   		if(comma_idx != -1){
	   			speech_text += r.toString().substring(1, comma_idx);
	   			speech_text += " ";
	   		}
	   	}
    
    	// Pause recognition process. It can be resumed then with startRecognition(false).
    	recognizer.stopRecognition();
    	
		return speech_text;
    	

	}

}
