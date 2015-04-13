package org.itpa.tcvr;

import java.util.HashMap;

public class EntityFilter {

	private HashMap<String, String> entity_mapping;
	
	EntityFilter(HashMap<String, String> em) {
		entity_mapping = em;		
	}
	
	EntityFilter() {
		entity_mapping = new HashMap<String, String>();
		entity_mapping.put("m m c", "MMC");
		entity_mapping.put("m m campus", "MMC");
		entity_mapping.put("main campus", "MMC");
		entity_mapping.put("engineer center", "EC");
		entity_mapping.put(" e c ", " EC ");
		entity_mapping.put("biscayne bay campus", "BBC");
		entity_mapping.put("b b c", "BBC");
		entity_mapping.put(" g c ", " GC ");
		entity_mapping.put("graham center", "GC");
		entity_mapping.put("p g five east", "PG5 east");
		entity_mapping.put("one o nine tower", "109TOWER");
		entity_mapping.put("g p e", "GPE");
		entity_mapping.put("golden panther express", "GPE");
		entity_mapping.put("golden panther", "GPE");
		entity_mapping.put("panther express", "GPE");
		entity_mapping.put("e t a", "ETA");
		entity_mapping.put("p g one", "PG1");
		entity_mapping.put("p g two", "PG2");
		entity_mapping.put("p g three", "PG3");
		entity_mapping.put("p g four", "PG4");
		entity_mapping.put("p g five", "PG5");
		entity_mapping.put("p g six", "PG6");
		entity_mapping.put("f i u", "FIU");
		entity_mapping.put("lot three", "LOT3");
		entity_mapping.put("lot five", "LOT5");
		entity_mapping.put("biscayne boulevard", "BB");
		
	}
	
	public String Filter(String input_str) {
		
		String res = input_str;
		
		for (String k: entity_mapping.keySet()) {
			res = res.replace(k, entity_mapping.get(k));
		}
		return res;
	}
}
