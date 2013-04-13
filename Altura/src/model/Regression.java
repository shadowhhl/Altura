package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import data.R;

import JSci.maths.statistics.TDistribution;

public class Regression{
	public static final String absoluteDirName = "/Users/shadowhhl/Documents/Altura/";
	public static final String dirName = "~/Documents/Altura/";
	
	public static final String sfrPropertyListFileName = "sfr_property_list.csv";
	public static final String sfrZipcode = "sfr_zipcode.txt";
	public static final String rOutput = "r_output.txt";
	public static final String rSummary = "r_summary.txt";
	
	public static final String condo_cs_CoeffFileName = "r_condo_cs.txt";
	public static final String condo_a_CoeffFileName = "r_condo_a.txt";
	public static final String sfr_cs_CoeffFileName = "r_sfr_cs.txt";
	public static final String sfr_a_CoeffFileName = "r_sfr_a.txt";
	
	public double R2;
	private HashMap<String, Double> condo_cs_Coefficients = new HashMap<String, Double>();
	private HashMap<String, Double> condo_a_Coefficients = new HashMap<String, Double>();
	private HashMap<String, Double> sfr_cs_Coefficients = new HashMap<String, Double>();
	private HashMap<String, Double> sfr_a_Coefficients = new HashMap<String, Double>();
	
	public static final int CONDO_CS_COEFF_INDEX=0;
	public static final int CONDO_A_COEFF_INDEX=1;
	public static final int SFR_CS_COEFF_INDEX=2;
	public static final int SFR_A_COEFF_INDEX=3;
	
	private void parseR2() {
		try {
			BufferedReader resultReader = new BufferedReader(new InputStreamReader(new FileInputStream(absoluteDirName+rSummary)));
			
			String line = new String();
			while ((line = resultReader.readLine())!=null) {
				if (line.contains("Multiple R-squared")) {
					String tag = "Adjusted R-squared: ";
					int ind = line.indexOf(tag);
					this.R2 = Double.valueOf(line.substring(ind+tag.length()));
				}
			}
			resultReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public HashMap<Calendar, Double> getPredictPricesOnDate(ArrayList<Calendar> dates) {
		return null;
	}

	public void regression() {
		ArrayList<String> rCommands = new ArrayList<String>();
		R r = new R();
		Thread t1 = new Thread(r);
		t1.run();
		r.executeR();
		
		for (int i=0;i<4;i++) {
			parseCoefficients(i);
		}
	}
	
	private void parseCoefficients(int index) {
		try {
			String fileName = "";
			switch (index) {
			case CONDO_CS_COEFF_INDEX:
				fileName = absoluteDirName + condo_cs_CoeffFileName;
				break;
			case CONDO_A_COEFF_INDEX:
				fileName = absoluteDirName + condo_a_CoeffFileName;
				break;
			case SFR_CS_COEFF_INDEX:
				fileName = absoluteDirName + sfr_cs_CoeffFileName;
				break;
			case SFR_A_COEFF_INDEX:
				fileName = absoluteDirName + sfr_a_CoeffFileName;
				break;
			}
			
			//skip the first line
			BufferedReader resultReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			
			//skip the first line
			String line = new String();
			line = resultReader.readLine();
			HashMap<String, Double> coeff = new HashMap<String, Double>();
			while ((line = resultReader.readLine())!=null) {
				String[] lineSplit = line.split(" ");
				String key = lineSplit[0];
				String value = lineSplit[1];
				key = key.replace("\"", "");
				if (value.equals("NA")) {
					coeff.put(key, null);
				} else {
					coeff.put(key, Double.valueOf(value));
				}
			}
			resultReader.close();
			
			switch (index) {
			case CONDO_CS_COEFF_INDEX:
				condo_cs_Coefficients = coeff;
				break;
			case CONDO_A_COEFF_INDEX:
				condo_a_Coefficients = coeff;
				break;
			case SFR_CS_COEFF_INDEX:
				sfr_cs_Coefficients = coeff;
				break;
			case SFR_A_COEFF_INDEX:
				sfr_a_Coefficients = coeff;
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void predict(int numBedrms, double numBathrms, int age, double lotSize, double currentIndex, String zipCode) {
		//when doing prediction, leave out zipcode is not in the hashmap, while zipcodes with NA values return null
		
		
	}
	
	public ArrayList<Double> predict(int numBedrms, double numBathrms, int age, double lotSize, ArrayList<Double> currentIndex, String zipCode) {
		//get build model
		//Set R commands
		ArrayList<String> rCommands = new ArrayList<String>();
		rCommands.add("sink(\"" + dirName + rOutput + "\")");
		rCommands.add("x<-read.csv(\"" + dirName + sfrPropertyListFileName + "\", header=TRUE)");
		rCommands.add("llm<-lm(x$TotalPrice~., data=x)");
		for (int i=0;i<currentIndex.size();i++) {
			String t = "ans<-" + //String.valueOf(lotSize) + "*" +
					"exp(llm$coefficients[\"(Intercept)\"]+" +
					"llm$coefficients[\"X.BD\"]*log(" + String.valueOf(numBedrms) + ")" +
							"+llm$coefficients[\"X.Bth\"]*log(" + String.valueOf(numBathrms) + ") " +
									"+ llm$coefficients[\"AGE\"]*log(" + String.valueOf(age) + ") + " +
											"llm$coefficients[\"Size\"]*log(" + String.valueOf(lotSize) + ")" +
													"+llm$coefficients[\"X" +zipCode +"\"]*1 +" +
															"llm$coefficients[\"schiller.value\"]*" + String.valueOf(currentIndex.get(i)) + ")";
			
			rCommands.add(t);
			rCommands.add("ans<-as.numeric(ans)");
			rCommands.add("ans");
		}
		rCommands.add("sink()");
		rCommands.add("sink(\"~/Documents/Altura/" + rSummary + "\")");
		rCommands.add("summary(llm)");
		rCommands.add("sink()");
		
		R r = new R();
		Thread t1 = new Thread(r);
		t1.run();
		ArrayList<Double> p = (ArrayList<Double>)r.executeR(rCommands);
		parseR2();
		return p;
		
	}
}
