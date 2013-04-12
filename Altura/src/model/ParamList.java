package model;

import java.util.HashMap;

public class ParamList {
	private HashMap<String, String> paramList;
	
	public ParamList() {
		paramList = new HashMap<String, String>();
	}
	
	public String getParam(String key) {
		return paramList.get(key);
	}
	
	public void setParamValue(String key, String value) {
		paramList.put(key, value);
	}
	
	public String toString() {
		return paramList.toString();
	}
}
