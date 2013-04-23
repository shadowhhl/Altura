package model;

import java.lang.Math;

public class NpvAnalyzer {
	/*Sophie's version*/
	private Double maintenanceCostPercentage = 0.0;
	private Double taxCostPercentage = 0.0;
	private Double transactionCostPercentage = 0.0;
	private Double mWaccPercentage = 0.0;
	private Double rentalGrowthPercentage = 0.0;
	private Double mRentalGrowthPercentage = 0.0;
	
	public Double getPerpetualNpv(Double rental) throws Exception{
		Double mWacc = mWaccPercentage / 100.0;
		Double mG = mRentalGrowthPercentage / 100.0;
		Double npv = 0.0;
			
		Double mCost = rental * maintenanceCostPercentage / 100.0;
		Double transCost = rental * 1.5;
		
		npv = rental/(mWacc - mG) - mCost/mWacc - transCost;
		
		return npv;
	}
	
	public Double getSellNpv(Double rental, int holdMonths, Double sellPrice) throws Exception{
		Double npv = 0.0;
		double mWacc = mWaccPercentage/100.0;
		double transC = transactionCostPercentage / 100.0;
		double tC = taxCostPercentage / 100.0;
		Double transCostForRent = rental * 1.5;
		Double transCostForSale = sellPrice * transC;
		Double taxCost = sellPrice * tC;
		
		npv = (rental)/mWacc*(1-1/(Math.pow(1+mWacc, holdMonths)))+(sellPrice - transCostForSale - taxCost)/Math.pow(1+mWacc, holdMonths) - transCostForRent;
		
		return npv;
	}

	public void setMaintenanceCostPercentage(Double maintenanceCostPercentage) {
		this.maintenanceCostPercentage = maintenanceCostPercentage;
	}

	public void setTaxCostPercentage(Double taxCostPercentage) {
		this.taxCostPercentage = taxCostPercentage;
	}

	public void setTransactionCostPercentage(Double transactionCostPercentage) {
		this.transactionCostPercentage = transactionCostPercentage;
	}

	public void setmWaccPercentage(Double mWaccPercentage) {
		this.mWaccPercentage = mWaccPercentage;
	}

	public void setRentalGrowthPercentage(Double rentalGrowthPercentage) {
		this.rentalGrowthPercentage = rentalGrowthPercentage;
		this.mRentalGrowthPercentage = rentalGrowthPercentage / 12.0;
	}
}
