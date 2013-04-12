package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Index {
	private HashMap<Calendar, Double> indices;
	public static final String defaultCSIndexFileName = "/Users/shadowhhl/Documents/Altura/csindex.csv";
	public static final String defaultZillowIndexFileName = "/Users/shadowhhl/Documents/Altura/zillowindex.txt";
	
	public Index() {
		indices = new HashMap<Calendar, Double>();
	}
	public HashMap<Calendar, Double> readIndex(String fileName) {
		BufferedReader reader;
		if (indices == null) {
			indices = new HashMap<Calendar, Double>();
		}
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String[] lineSplit;
			String line;
			while ((line=reader.readLine())!=null) {
				lineSplit = line.split(",");
				
				String indexDateStr = lineSplit[0];
				String indexStr = lineSplit[1];
				Double index = Double.valueOf(indexStr);
				
				Calendar indexDate = Calendar.getInstance();
				indexDate.setTime(new SimpleDateFormat("yyyy-M").parse(indexDateStr));
				
				indices.put(indexDate, index);
			}
			
			
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indices;
	}
	
	public Double getIndex(Calendar calendar) {
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH)+1;
		
		
		return getIndex(year.toString() + "-" + month.toString());
	}
	
	public Double getIndex(String calendarString) {
		Double index = null;
		try {
			Calendar indexDate = Calendar.getInstance();
			indexDate.setTime(new SimpleDateFormat("yyyy-M").parse(calendarString));
			index = indices.get(indexDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return index;
	}
	
}
