package org.itpa.tcvr;

import java.io.IOException;

/**
 * huibo wang
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	SpeechRecognize sr = new SpeechRecognize();
    	DependenciesPaser dp = new DependenciesPaser();
    	
    	while(true){
	    	String speech_text = sr.getSpeechResult();
	       	System.out.println(speech_text);
	      	System.out.println(dp.getPaser(speech_text));
    	}


    }
}
