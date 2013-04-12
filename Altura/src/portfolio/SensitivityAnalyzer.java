package portfolio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import data.Index;

import model.Formater;
import model.NpvAnalyzer;
import model.ParamList;
import model.Regression;

public class SensitivityAnalyzer{
	HashMap<String, HashMap<Calendar, Double> > projectedPrices; //<AccountNum, Prices at different date>
	HashMap<String, HashMap<Calendar, Double> > rentAndSellPrices; 
	HashMap<String, HashMap<Calendar, Double> > sellNowPrices;
	HashMap<String, HashMap<Calendar, Double> > holdAndSellPrices;
	
    private NpvAnalyzer npvAnalyzer;
    
	public NpvAnalyzer getNpvAnalyzer() {
		return npvAnalyzer;
	}

	public void setNpvAnalyzer(NpvAnalyzer npvAnalyzer) {
		this.npvAnalyzer = npvAnalyzer;
	}
	
	public void calculatePrices(Portfolio portfolio, int index, ParamList paramList, ArrayList<Calendar> futureDates) {
		String account = portfolio.getValue(index, "Account");
		//calculate projected prices
		projectedPrices = new HashMap<String, HashMap<Calendar,Double>>();
		Calendar now = Calendar.getInstance();
		
		
		int numBedrms = Integer.valueOf(portfolio.getValue(index, "#Bedrooms"));
		double numBathrms = Double.valueOf(portfolio.getValue(index, "#Bathrooms"));
		int lotSize = Integer.valueOf(portfolio.getValue(index, "Size/SqFeet"));
		int age = now.get(Calendar.YEAR) - Integer.valueOf(portfolio.getValue(index, "Year Built"));
		String zipCode = portfolio.getValue(index, "Zip Code");
		
		Regression reg = new Regression();
		Index propertyIndex = new Index();
		propertyIndex.readIndex(Index.defaultCSIndexFileName);
		ArrayList<Double> indexValues = new ArrayList<Double>();
		for (int dateIndex=0;dateIndex<futureDates.size();dateIndex++) {
			Calendar futureDate = futureDates.get(dateIndex);	
			Double indexValue = propertyIndex.getIndex(futureDate);
			
			indexValues.add(indexValue);
		}
		ArrayList<Double> predictedPrices = reg.predict(numBedrms, numBathrms, age, lotSize, indexValues, zipCode);
		HashMap<Calendar, Double> tProjPrice = new HashMap<Calendar, Double>();;
		for (int dateIndex=0;dateIndex<futureDates.size();dateIndex++) {
			
			tProjPrice.put(futureDates.get(dateIndex), predictedPrices.get(dateIndex));
		}
		projectedPrices.put(account, tProjPrice);
		
		//calculate rent and sell prices
		
		//calculate sell now prices
		
		//calculate hold and sell prices
	}
	
	public HashMap<Calendar, Double> getPricesByAccount(String account) {
		return projectedPrices.get(account);
	}
	
	public double getPriceByAccountByDate(String account, Calendar date) {
		return projectedPrices.get(account).get(date).doubleValue();
	}
}
