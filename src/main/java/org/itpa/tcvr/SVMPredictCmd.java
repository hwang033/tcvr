package org.itpa.tcvr;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import libsvm.*;

public class SVMPredictCmd {

	public enum COMMAND_TYPES {
		PARKINGOCCUPENCYINFO, 
		PARKINGOCCUPENCY, 
		RECOMMENDPARKING,
		SHOWPARKING,
		SHOWNEARESTPARKING,
		USERPOSITION,
		SHOWBUSROUTE,
		NEARESTBUSSTATION,
		BUSSTATION,
		BUSETA,
		BUSLOCATION,
		UNKNOWN
	}
	
	private static double atof(String s) {
		return Double.valueOf(s).doubleValue();
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}

	public COMMAND_TYPES Predict(String data_to_predict) {

		int pred = 11;
		StringTokenizer st = new StringTokenizer(data_to_predict, " \t\n\r\f:");

		int m = st.countTokens() / 2;
		svm_node[] x = new svm_node[m];
		for (int j = 0; j < m; j++) {
			x[j] = new svm_node();
			x[j].index = atoi(st.nextToken());
			x[j].value = atof(st.nextToken());
		}

		pred = (int)svm.svm_predict(smodel, x);
		
		return COMMAND_TYPES.values()[pred];
	}
	
	public COMMAND_TYPES PredictRawString(String sentence) {
		
		int pred = 11;
		sentence = sentence.replaceAll("[^a-zA-Z ]", "").toLowerCase();
		StringTokenizer st = new StringTokenizer(sentence, " \t\n\r\f:");
		
		int m = st.countTokens();
		svm_node[] x = new svm_node[m];
		for (int j = 0; j < m; j++) {
			x[j] = new svm_node();
			
			String word = st.nextToken();
			if (dicmap.containsKey(word)) {
				x[j].index = dicmap.get(word);
				x[j].value = 1;
			}
		}
		
		pred = (int)svm.svm_predict(smodel, x);
		
		return COMMAND_TYPES.values()[pred];
	}

	public SVMPredictCmd(String model_path, String[] volc) throws IOException {
		// load model in constructor
		volcabulary = volc;
		smodel = svm.svm_load_model(model_path);
		dicmap = new HashMap<String, Integer>();
		for (int i = 0; i < volcabulary.length; ++i) {
			dicmap.put(volcabulary[i], i+1);
		}
	}

	private svm_model smodel;
	private static HashMap<String, Integer> dicmap;
	private String[] volcabulary;
}
