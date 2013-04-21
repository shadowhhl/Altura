package portfolio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.lang.Math;

import data.*;

public class Portfolio {
	private ArrayList< HashMap<String, String> > portfolio;
	private String portfolioFileName = "input_good.csv";
	//private String portfolioFileName = "input_all.csv";
	
	
	public void initialPortfolio() {
		Csv csvReader = new Csv();
		String[] title = csvReader.readTitleFromCsv(portfolioFileName);
		ArrayList<String[]> content = csvReader.readValueFromCsv(portfolioFileName);
		
		portfolio = new ArrayList< HashMap<String,String> >();
		
		for (int i=0;i<content.size();i++) {
			HashMap<String, String> entry = new HashMap<String, String>();
			String[] csvRow = content.get(i);
			for (int j=0;j<title.length;j++) {
				entry.put(title[j], csvRow[j]);
			}
			portfolio.add(entry);
		}
	}
	
	public void updatePortfolioRentalOverPeriod(HashMap<String, Double> rentalZEstimates,
												double monthIRR, 
												HashMap<String, Calendar> projectedTimelines, 
												Calendar todayDate) {
		for (int i=0;i<portfolio.size();i++) {
			HashMap<String, String> property = portfolio.get(i);
			String accountNum = property.get("Account");
			Double rentalEstimate = rentalZEstimates.get(accountNum);
			Calendar pTimeline = projectedTimelines.get(accountNum);
			Calendar now = Calendar.getInstance();
			long millisDiff = pTimeline.getTimeInMillis() - now.getTimeInMillis();
			double monthDiff = millisDiff/1000.0/3600.0/24.0/30.0;
			Double rentalOverPeriod; 
			if (millisDiff > 0)
				rentalOverPeriod = rentalEstimate/(monthIRR/100.0)*(1-1/(Math.pow((1+(monthIRR/100.0)),monthDiff)));
			else
				rentalOverPeriod = 0.0;
			
			portfolio.get(i).put("Rental Over Period", rentalOverPeriod.toString());
		}
	}
	
	public void updatePortfolioMaintenanceCostOverPeriod(HashMap<String, Double> values,
														 double maintenanceCost,
														 double monthIRR,
														 HashMap<String, Calendar> projectedTimelines,
														 Calendar todayDate) {
		for (int i=0;i<portfolio.size();i++) {
			HashMap<String, String> property = portfolio.get(i);
			String accountNum = property.get("Account");
			Double value = values.get(accountNum);
			Calendar pTimeline = projectedTimelines.get(accountNum);
			Calendar now = Calendar.getInstance();
			long millisDiff = pTimeline.getTimeInMillis() - now.getTimeInMillis();
			double monthDiff = millisDiff/1000.0/3600.0/24.0/30.0;
			Double maintenanceCostOverPeriod; 
			if (millisDiff > 0)
				maintenanceCostOverPeriod = maintenanceCost/100.0/12.0*value/(monthIRR/100.0)*(1-1/(Math.pow((1+(monthIRR/100.0)),monthDiff)));
			else
				maintenanceCostOverPeriod = 0.0;
			
			portfolio.get(i).put("Maintenance Over Period", maintenanceCostOverPeriod.toString());
		}
	}
	
	public void updatePortfolioRentAndSell(HashMap<String, Double> values,
										   double maintenanceCost,
										   double monthIRR,
										   double transactionCost,
										   HashMap<String, Calendar> projectedTimelines,
										   HashMap<String, Double> projectedPrices,
										   Calendar todayDate) {
		for (int i=0;i<portfolio.size();i++) {
			HashMap<String, String> property = portfolio.get(i);
			String accountNum = property.get("Account");
			Double value = values.get(accountNum);
			Double projectedPrice = projectedPrices.get(accountNum);
			Calendar pTimeline = projectedTimelines.get(accountNum);
			
			Double rentalOverPeriod = Double.valueOf(portfolio.get(i).get("Rental Over Period"));
			Double maintenanceOverPeriod = Double.valueOf(portfolio.get(i).get("Maintenance Over Period"));
			Double ARCPrice, AlturaPrice;
			ARCPrice = getProperyRentAndSell(value, monthIRR, transactionCost, pTimeline, rentalOverPeriod, maintenanceOverPeriod);
			AlturaPrice = getProperyRentAndSell(projectedPrice, monthIRR, transactionCost, pTimeline, rentalOverPeriod, maintenanceOverPeriod);
			
			portfolio.get(i).put("Rent and Sell later_ARC", ARCPrice.toString());
			portfolio.get(i).put("Rent and Sell later_Altura", AlturaPrice.toString());
		}
	}
	
	public void updatePortfolioSellNow(HashMap<String, Double> values,
									   HashMap<String, Double> projectedPrices,
									   double transactionCost
									   ) {
		for (int i=0;i<portfolio.size();i++) {
			HashMap<String, String> property = portfolio.get(i);
			String accountNum = property.get("Account");
			Double value = values.get(accountNum);
			Double projectedPrice = projectedPrices.get(accountNum);
			Double ARCPrice, AlturaPrice;
			ARCPrice = value*(1-transactionCost/100.0);
			AlturaPrice = projectedPrice*(1-transactionCost/100.0);
			
			portfolio.get(i).put("Sell now_ARC", ARCPrice.toString());
			portfolio.get(i).put("Sell now_Altura", AlturaPrice.toString());
		}
	}

	public void updatePortfolioHoldAndSell(HashMap<String, Double> values,
			   							   HashMap<String, Double> projectedPrices,
			   							   double monthIRR,
			   							   HashMap<String, Calendar> projectedTimelines,
										   double transactionCost,
										   Calendar todayDate
										   ) {
		for (int i=0;i<portfolio.size();i++) {
			HashMap<String, String> property = portfolio.get(i);
			String accountNum = property.get("Account");
			Double value = values.get(accountNum);
			Double projectedPrice = projectedPrices.get(accountNum);
			Calendar pTimeline = projectedTimelines.get(accountNum);
			Calendar now = Calendar.getInstance();
			long millisDiff = pTimeline.getTimeInMillis() - now.getTimeInMillis();
			double monthDiff = millisDiff/1000.0/3600.0/24.0/30.0;
			
			Double maintenanceOverPeriod = Double.valueOf(portfolio.get(i).get("Maintenance Over Period"));
			Double ARCPrice, AlturaPrice;
			if (millisDiff > 0) {
				ARCPrice = (value*(1-transactionCost/100.0))/(Math.pow(1+monthIRR/100, monthDiff))-maintenanceOverPeriod;
				AlturaPrice = (projectedPrice*(1-transactionCost/100.0))/(Math.pow(1+monthIRR/100, monthDiff))-maintenanceOverPeriod;
			}
			else {
				ARCPrice = 0.0;
				AlturaPrice = 0.0;
			}
			
			portfolio.get(i).put("Hold and Sell later_ARC", ARCPrice.toString());
			portfolio.get(i).put("Hold and Sell later_Altura", AlturaPrice.toString());
		}
	}

	public Double getProperyRentAndSell(double value,
										double monthIRR,
										double transactionCost,
										Calendar projectedTimeline,
										double rentalOverPeriod,
										double maintenanceOverPeriod) {
		Double rentAndSellValues = 0.0;
		
		Calendar now = Calendar.getInstance();
		long millisDiff = projectedTimeline.getTimeInMillis() - now.getTimeInMillis();
		double monthDiff = millisDiff/1000.0/3600.0/24.0/30.0;
		if (millisDiff > 0) {
			rentAndSellValues = rentalOverPeriod-maintenanceOverPeriod+(value*(1-transactionCost/100.0))/(Math.pow(1+monthIRR/100, monthDiff));
		}
		else {
			rentAndSellValues = 0.0;
		}
		
		return rentAndSellValues;
		
	}
	
	public int getPortfolioSize() {
		return portfolio.size();
	}
	
	public ArrayList<String> getAccountNum() {
		ArrayList<String> accountNum = new ArrayList<String>();
		for (int i=0;i<portfolio.size();i++) {
			accountNum.add(portfolio.get(i).get("Account"));
		}
		return accountNum;
	}

	public HashMap<String, String> getEntry(int entryIndex) {
		if (entryIndex>portfolio.size()) {
			return null;
		}
		else {
			return portfolio.get(entryIndex);
		}
	}
	
	public String getValue(int entryIndex, String fieldName) {
		if (entryIndex>portfolio.size()) {
			return null;
		}
		else {
			HashMap<String, String> entry = portfolio.get(entryIndex);
			return entry.get(fieldName);
		}
	}
	
	@Override
	public String toString() {
		return "Portfolio [portfolio=" + portfolio + "]";
	}

	public HashMap<String, String> getEntry(String accountNum) {
		for (int i=0;i<portfolio.size();i++) {
			HashMap<String, String> row = portfolio.get(i);
			if (row.get("Account").equalsIgnoreCase(accountNum)) {
				return row;
			}
		}
		return null;
	}
}
