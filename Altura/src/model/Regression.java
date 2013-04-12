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
	public static final String sfrPropertyListFileName = "sfr_property_list.csv";
	public static final String sfrZipcode = "sfr_zipcode.txt";
	public static final String rOutput = "r_output.txt";
	public static final String rSummary = "r_summary.txt";
	public static final String dirName = "~/Documents/Altura/";
	public static final String absoluteDirName = "/Users/shadowhhl/Documents/Altura/";
	
	private double a;
	private double b;
	private double R2;
	private double x0;
	private double ci; //confidence interval
	public int count;
	private double SSX=0;
	private double x_avg=0;
	private double SYX = 0;
	
	public boolean reg(ArrayList<Double> x_data,
			ArrayList<Double> y_data) {
		if (x_data.size() != y_data.size()) {
			return false;
		} 
		else {	
			int n = x_data.size();
			count = n;
			System.out.println(count);
			double xy=0, x=0, y=0, xx=0;
			for (int i=0;i<n;i++) {
				xy+=x_data.get(i)*y_data.get(i);
				x+=x_data.get(i);
				y+=y_data.get(i);
				xx+=Math.pow(x_data.get(i), 2);
			}
			
			b = (xy-x*y/n)/(xx-x*x/n);
			a = (y-b*x)/n;
			
			double SStot = 0, SSerr = 0;
			double y_avg = y/n;
			x_avg = x/n;
			
			for (int i=0;i<n;i++) {
				SSX+=Math.pow((x_data.get(i)-x_avg), 2);
				SStot+=Math.pow((y_data.get(i)-y_avg), 2);
				SSerr+=Math.pow((y_data.get(i)-a-b*x_data.get(i)), 2);
			}
			R2 = 1-SSerr/SStot;

			{
				double temp1=0;
				double temp2=0;
				double temp3=0;
				for (int i=0;i<n;i++) {
					temp1+=(x_data.get(i)-x_avg)*(y_data.get(i)-y_avg);
					temp2+=Math.pow((x_data.get(i)-x_avg), 2);
					temp3+=Math.pow((y_data.get(i)-y_avg), 2);
				}
				temp1=Math.pow(temp1, 2);
				SYX=temp3-temp1/temp2;
			}
			
			SYX=Math.sqrt(SYX/(n-2));
			return true;
		}
	}
	
	public double getCi(double x) {
		int df = count-2;
		double p = 0.05;
		TDistribution k = new TDistribution(df);
		double t = k.inverse(1-p/2);
		System.out.println("df=" + df + "t=: " + t);
		ci = t*SYX*Math.sqrt(1/count+(x-x_avg)*(x-x_avg)/SSX);
		
		return ci;
	}
	
	public double getA() {
		return a;
	}
	public double getB() {
		return b;
	}
	public double getR2() {
		return R2;
	}

	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}
	
	public double getY0() {
		return a+b*x0;
	}

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
