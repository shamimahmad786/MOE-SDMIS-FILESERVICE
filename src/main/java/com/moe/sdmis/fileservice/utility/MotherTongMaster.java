package com.moe.sdmis.fileservice.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MotherTongMaster {
	public static Map<Integer, Boolean> motherTongMap_116=new HashMap<Integer,Boolean>();
	 static {
	        HashMap<Integer,Boolean> aMap = new HashMap<Integer,Boolean>();
	        aMap.put(1, true);
	        aMap.put(2, true);
	        motherTongMap_116 =  Collections.unmodifiableMap(aMap);
	    }
	 
	 public static Map<Integer, Boolean> motherTongMap_112=new HashMap<Integer,Boolean>();
	 static {
	        HashMap<Integer,Boolean> aMap = new HashMap<Integer,Boolean>();
	        aMap.put(1, true);
	        aMap.put(2, true);
	        motherTongMap_112 =  Collections.unmodifiableMap(aMap);
	    }
}
