package org.itpa.tcvr;

import java.io.IOException;

import org.itpa.tcvr.SVMPredictCmd.COMMAND_TYPES;

/**
 * huibo wang
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	String[] volcabulary = { "where", "is", "the", "least",
				"occupied", "garage", "which", "has", "most", "spots", "available",
				"garages", "are", "full", "parking", "at", "f", "i", "u", "empty",
				"space", "will", "be", "able", "to", "find", "show", "me", "top",
				"three", "with", "occupancy", "availability", "of", "<garage>",
				"can", "get", "park", "in", "does", "have", "left", "out", "rest",
				"slot", "how", "many", "there", "any", "free", "position", "room",
				"for", "what", "should", "may", "tell", "spot", "best", "place",
				"recommend", "a", "suggest", "recommendation", "go", "do",
				"location", "all", "near", "around", "here", "nearest", "closest",
				"having", "my", "area", "am", "current", "<routenm>", "bus",
				"shuttle", "buses", "shuttles", "route", "path", "from",
				"<platform>", "passing", "through", "give", "highlight", "on",
				"map", "line", "pass", "by", "take", "routes", "station", "gets",
				"way", "dose", "run", "stop", "stations", "stops", "want", "know",
				"nearby", "that", "now", "going", "belongs", "located", "navigate",
				"good", "estimate", "arrival", "time", "e", "t", "arrive", "you",
				"long", "before", "next", "wait", "it", "until", "comes", "till",
				"soon", "come", "when", "coming", "arriving", "due", "reach",
				"leave", "late", "far", "away", "waiting", "dynamic", "catch",
				"currently" };
		
		SVMPredictCmd classifier = new SVMPredictCmd("c:\\svm_train2.model", volcabulary);
		DependenciesPaser dp = new DependenciesPaser();
		EntityFilter efilter = new EntityFilter();
		UrlGenerator ug = new UrlGenerator();
		
//		String teststr = "i want to know the position of closest shuttle stop to MMC";
//		//String[] res = ug.ExtractRoutePlatformInfo(teststr, dp.getSemanticGraph(teststr));
//		System.out.println(ug.GetUrl(COMMAND_TYPES.NEARESTBUSSTATION, teststr, dp.getSemanticGraph(teststr)));
//		
//		System.out.println("done");
//		
		SpeechRecognize sr = new SpeechRecognize(classifier, dp, efilter, ug);
    	
    	while(true){
	    	String speech_text = sr.getSpeechResult();
	       	System.out.println(speech_text);
	       	//System.out.println(dsb.PredictRawString(speech_text));
	      	System.out.println(dp.getPaser(speech_text));
    	}


    }
}
