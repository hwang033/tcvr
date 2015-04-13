package org.itpa.tcvr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.*;

import org.itpa.tcvr.SVMPredictCmd.COMMAND_TYPES;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.TypedDependency;

public class UrlGenerator {
	
	private String[] route_name = {"cats", "GPE"};
	
	private String[] platform_name = { "MMC", "EC", "BBC", "GC", "PG5 EAST",
			"PG5", "109TOWER", "LOT5", "LOT3", "BB" };
	
	private String[] reg_from_platform_pattern = {"prep_from\\(.*?, (.+?)-\\d+\\)"};
	
	private String[] reg_to_platform_pattern = {
			"aux\\((.+?)-\\d+, to-\\d+\\)", "prep_to\\(.*?, (.+?)-\\d+\\)",
			""};
	
	private String[] reg_at_platform_pattern = {
			"prep_at\\(.+?, (.*?)-\\d+\\)",
			"amod\\(.*?, (.*?)-\\d+\\)",
			"prep_of\\(.*?, (.*?)-\\d+\\)",
			"nn\\((.*?)-\\d+, .*?\\)",
			"dobj\\(.*?, (.*?)-\\d+\\)",
			"xcomp\\(.*?, (.*?)-\\d+\\)"
			};
	
	private String[] reg_garage_pattern = {"nn\\((.*?)-\\d+, garage-\\d+\\)"};
	
	private List<Pattern> compiled_from_platform;
	private List<Pattern> compiled_to_platform;
	private List<Pattern> compiled_at_platform;
	private List<Pattern> compiled_garage;
	
	private HashSet<String> platform_set;
	private HashSet<String> route_set;
	
	public UrlGenerator() {
		compiled_from_platform = new ArrayList<Pattern>();
		compiled_to_platform   = new ArrayList<Pattern>();
		compiled_at_platform   = new ArrayList<Pattern>();
		compiled_garage        = new ArrayList<Pattern>();
		
		for(String i: reg_from_platform_pattern) {
			compiled_from_platform.add(Pattern.compile(i));
		}
		
		for(String j: reg_to_platform_pattern) {
			compiled_to_platform.add(Pattern.compile(j));
		}
		
		for(String k: reg_at_platform_pattern) {
			compiled_at_platform.add(Pattern.compile(k));
		}
		
		for(String l: reg_garage_pattern) {
			compiled_garage.add(Pattern.compile(l));
		}
		
		platform_set = new HashSet<String>(Arrays.asList(platform_name));
		route_set = new HashSet<String>(Arrays.asList(route_name));
	}
	
	public String[] ExtractGarageInfo(String text, SemanticGraph sg) {
		
		/* res should contain
		 * 1. garage name
		 * 2. lot name
		 * */
		
		String[] res = new String[]{null, null};
		String dep = sg.toList();
		
		Matcher m;
		for(Pattern p: compiled_garage) {
			m = p.matcher(dep);
			if (m.find()) {
				res[0] = m.group(1);
				break;
			}
		}
		
		return res;
	}
	
	public String[] ExtractRoutePlatformInfo(String text, SemanticGraph sg) {
		
		/* res should contain
		 * 1. route name
		 * 2. from_platform
		 * 3. to_platform
		 * 4. at_platform / through_platform / by_platform
		 * */
		
		String[] res = new String[]{"null", "null", "null", "null"};
		
		// firstly find route name
		for(String rn : route_name) {
			if (text.contains(rn)) {
				res[0] = rn;
				break;
			}
		}
		
		String dep = sg.toList();
		
		Matcher m;
		for(Pattern p: compiled_from_platform) {
			 m = p.matcher(dep);
			 if (m.find() && platform_set.contains(m.group(1))) {
				 res[1] = m.group(1);
				 break;
			 }
		}
		
		for(Pattern q: compiled_to_platform) {
			m = q.matcher(dep);
			if (m.find() && platform_set.contains(m.group(1))) {
				res[2] = m.group(1);
				break;
			}
		}
		
		for(Pattern r: compiled_at_platform) {
			m = r.matcher(dep);
			if (m.find() && platform_set.contains(m.group(1))) {
				res[3] = m.group(1);
				break;
			}
		}
		
		return res;
	}

	public String GetUrl(COMMAND_TYPES ctype, String text, SemanticGraph sgraph) {
		
		StringBuilder url = new StringBuilder("");
		
		switch (ctype) {
		case PARKINGOCCUPENCYINFO:
			url.append("/parking_info");
			break;
		case PARKINGOCCUPENCY:
			String[] garages = ExtractGarageInfo(text, sgraph);
			url.append("/park_occupency/");
			if (!garages[0].equals("null")) {
				url.append(garages[0]);
			} else if (!garages[1].equals("null")) {
				url.append(garages[1]);
			}
			break;
		case RECOMMENDPARKING:
			url.append("/recommend_parking/{lon}/{lat}");
			break;
		case SHOWPARKING:
			String[] garages2 = ExtractGarageInfo(text, sgraph);
			url.append("/show_parking/");
			if (!garages2[0].equals("null")) {
				url.append(garages2[0]);
			} else if (!garages2[1].equals("null")) {
				url.append(garages2[1]);
			}
			break;
		case SHOWNEARESTPARKING:
			url.append("/show_nearest_parking/{lon}/{lat}");
			break;
		case USERPOSITION:
			url.append("/user_position");
			break;
		case SHOWBUSROUTE:
			String[] sbr_info = ExtractRoutePlatformInfo(text, sgraph);
			if (!sbr_info[0].equals("null")) {
				url.append("/show_bus_route/routenm/");
				url.append(sbr_info[0]);
			} else if (!sbr_info[1].equals("null") || !sbr_info[2].equals("null")) {
				url.append("/show_bus_route/platform/");
				url.append(sbr_info[1].toString());
				url.append("/");
				url.append(sbr_info[2].toString());
			}
			break;
		case NEARESTBUSSTATION:
			String[] nb_info = ExtractRoutePlatformInfo(text, sgraph);
			url.append("/nearest_bus_station/");
			if (!nb_info[0].equals("null")) {
				url.append("routenm/").append(nb_info[0]).append("/");
			} else if (!nb_info[1].equals("null") || !nb_info[2].equals("null")) {
				url.append("platform/")
				   .append(nb_info[1].toString()).append("/")
				   .append(nb_info[2].toString()).append("/");
			} else if (!nb_info[3].equals("null")) {
				url.append("platform/")
				   .append(nb_info[3]).append("/null/");
			}
			url.append("{lon}/{lat}");
			break;
		case BUSSTATION:
			String[] bs_info = ExtractRoutePlatformInfo(text, sgraph);
			
			if (!bs_info[0].equals("null")) {
				url.append("/bus_station/");
				url.append("routenm/").append(bs_info[0]);
			} else if (!bs_info[1].equals("null")) {
				url.append("/bus_station/");
				url.append("platform/").append(bs_info[1]);
			} else if (!bs_info[2].equals("null")) {
				url.append("/bus_station/");
				url.append("platform/").append(bs_info[2]);
			} else if (!bs_info[3].equals("null")) {
				url.append("/bus_station/");
				url.append("platform/").append(bs_info[3]);
			}
			break;
		case BUSETA:
			String[] beta_info = ExtractRoutePlatformInfo(text, sgraph);
			
			if (!beta_info[0].equals("null")) {
				if (!beta_info[1].equals("null") || !beta_info[2].equals("null")) {
					url.append("/bus_eta/").append(beta_info[0]).append("/")
					   .append(beta_info[1].toString()).append("/")
					   .append(beta_info[2].toString()).append("/");
				} else {
					url.append("/bus_eta/routenm/").append(beta_info[0]);
				}
			} else if (!beta_info[1].equals("null") || !beta_info[2].equals("null")) {
				
				url.append("/bus_eta/platform/")
				   .append(beta_info[1].toString()).append("/")
				   .append(beta_info[2].toString()).append("/");
				
			} else if( !beta_info[3].equals("null")) {
				url.append("/bus_eta/platform/null/")
				   .append(beta_info[3].toString()).append("/");
			}
			break;
		case BUSLOCATION:
			String[] bl_info = ExtractRoutePlatformInfo(text, sgraph);
			
			if (!bl_info[0].equals("null")) {
				if (!bl_info[1].equals("null") || !bl_info[2].equals("null")) {
					url.append("/bus_location/").append(bl_info[0]).append("/")
					   .append(bl_info[1].toString()).append("/")
					   .append(bl_info[2].toString()).append("/");
				} else {
					url.append("/bus_location/routenm/").append(bl_info[0]);
				}
			} else if (!bl_info[1].equals("null") || !bl_info[2].equals("null")) {
				
				url.append("/bus_location/platform/")
				   .append(bl_info[1].toString()).append("/")
				   .append(bl_info[2].toString()).append("/");
				
			} else if(!bl_info[3].equals("null")) {
				url.append("/bus_location/platform/null/")
				   .append(bl_info[3].toString()).append("/");
			}
			break;
		case UNKNOWN:
			break;
		}
		
		return url.toString();
	}
}
