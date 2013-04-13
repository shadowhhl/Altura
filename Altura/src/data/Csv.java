package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Csv {
	
	public String[] readTitleFromCsv(String fileName) {
		String[] csvTitle = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			//read the first line, i.e. the title
			String line = reader.readLine();
			csvTitle = line.split(",");
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csvTitle;
	}
	
	public ArrayList< String[] > readValueFromCsv(String fileName) {
		ArrayList< String[] > csvValue = new ArrayList<String[]>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			//skip the first line, i.e. the title
			reader.readLine();
			while ((line = reader.readLine())!=null) {
				String[] lineSplit = line.split(",");
				if (lineSplit.length==0) {
					continue;
				}
				else {
					csvValue.add(lineSplit);
				}
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csvValue;
	}

}
