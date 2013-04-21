package model;

import java.lang.Math;

public class NpvAnalyzer {
	/*Sophie's version*/
	private Double maintenanceCostPercentage = 0.0;
	private Double taxCostPercentage = 0.0;
	private Double transactionCostPercentage = 0.0;
	private Double mIrrPercentage = 0.0;
	
	public Double getPerpetualNpv(Double rental) throws Exception{
		Double mIrr = mIrrPercentage / 100.0;
		Double npv = 0.0;
			
		Double mCost = rental * maintenanceCostPercentage / 100.0;
		Double transCost = rental * 1.5;
		
		npv = (rental - mCost)/mIrr - transCost;
		
		return npv;
	}
	
	public Double getSellNpv(Double rental, int holdMonths, Double sellPrice) throws Exception{
		Double npv = 0.0;
		double mIrr = mIrrPercentage/100.0;
		double transC = transactionCostPercentage / 100.0;
		double tC = taxCostPercentage / 100.0;
		Double transCostForRent = rental * 1.5;
		Double transCostForSale = sellPrice * transC;
		Double taxCost = sellPrice * tC;
		
		npv = (rental)/mIrr*(1-1/(Math.pow(1+mIrr, holdMonths)))+(sellPrice - transCostForSale - taxCost)/Math.pow(1+mIrr, holdMonths) - transCostForRent;
		
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

	public void setmIrrPercentage(Double mIrrPercentage) {
		this.mIrrPercentage = mIrrPercentage;
	}
}
