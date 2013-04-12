package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class R implements Runnable{
	private static final String rscriptFileName = "/Users/shadowhhl/Documents/Altura/rscript.R";
	private static final String outputFileName = "/Users/shadowhhl/Documents/Altura/r_output.txt";
	
	public Object executeR(ArrayList<String> commands) {
		
		try {
			ArrayList<Double> prices = new ArrayList<Double>();
			PrintWriter csvOut = new PrintWriter(new File(rscriptFileName).getAbsoluteFile());
			csvOut.println("#!/usr/bin/env Rscript");
			for (int i=0;i<commands.size();i++) {
				csvOut.println(commands.get(i));
			}
			csvOut.close();
			Runtime r = Runtime.getRuntime();
			
			Process p = r.exec("Rscript /Users/shadowhhl/Documents/Altura/rscript.R");
			
			int flag = p.waitFor();
			BufferedReader resultReader;
			if (flag==0) {
				resultReader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFileName)));
				
				String line = new String();
				while ((line = resultReader.readLine())!=null) {
					if (line.length()==0) {
						continue;
					} else {
						String[] lineSplit = line.split(" ");
						prices.add(Double.valueOf(lineSplit[1]));
//						resultReader.close();
//						return Double.valueOf(lineSplit[1]);
					}
				}
				System.out.println(prices);
				resultReader.close();
				return prices;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

