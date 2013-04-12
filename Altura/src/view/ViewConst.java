package view;

public class ViewConst {
	public static final int mainWindowWidth = 1000;
	public static final int mainWindowHeight = 700;
	public static final String[] tabFolderNames = {"User Guide", "Output(1)", "Output(2)", "Portfolio NPV", "Sensitivity", "Statistical Model"};
	
	public static final int comboWidthOffset = 22;
	
	public static final int updateButtonX = 700;
	public static final int updateButtonY = 10;
	public static final int updateButtonWidth = 100;
	public static final int updateButtonHeight = 25;
	public static final String updateString = "Update";
	
	//User Guide page
	public static final int userguideGroupControlWidth = 940;
	public static final int[] userguideGroupControlHeights = {120, 190, 130};
	public static final int userguideGroupControlX = 20;
	public static final int[] userguideGroupControlYs = {10, 150, 360};
	

	public static final String[] userguideGroupControlNames = {"Tabs", "Calculation of NPV", "Missing information"};
	public static final String[] userguideSessions = {
		//The first session
		"*Four tabs are following this one.\n" +
			"*The first one is the \"Output\" page where a summary is shown\n" +
			"*The second one is the \"Portfolio NPV\" where the NPV of all assets are calculated upon the assumptions that are described in Parameters.\n" +
			"*The third one is the \"Sensitivity\" where we show the impact on the on the NPV if we change: the IRR, the horizon period, the change in " +
			"value of the property \nover time or the rental increase in %.\n" +
			"*The fourth one is the \"Statistical Model\" where we show our estimates of properties fair market value by statistical analysis.",
		//The second session
			"*The formula is based upon: the sale price of the property, the rental income over the period, the maintenance costs and the transactions costs.\n" +
		"*The sale price of the property is calculated as follow: Appraiser FMV*(1+change in price over the horizon period)/((1+IRR)^(horizon period)).\n" +
		"*The rental income over the period formulat uses a rental base income calculated as a percentage of the Appraiser FMV (for example 10%) and an expected \nincrease in this rental income per year (for example 5%). \n" +
		"*Then I have implemented the formula we have seen in Advance Engineering to obtain the NPV of a geometric gradient serie.\n" +
		"*The transactions costs are calculated using a percentage of the sale price at the end of the horizon period (here I used 8%): \nAppraiser FMV*(1+change in price over the period)*8%/((1+IRR)^Horizon period)\n" +
		"*For the maintenance costs, I used the same formula as the rental income. The base maintenance costs are calculated as a % of the Appraiser FMV (I used 3%) \nand they increased each year by the % of inflation (4,5%).\n" +
		"*Then I have implemented the formula we have seen in Advance Engineering to obtain the NPV of a geometric gradient serie.",
		//The third session
		"*We don't have the Appraiser FMV for all the property\n" +
		"*Horizon period for all properties are missing\n" +
		"*Expected rental incomes per year or as a percentage of the value of the property are missing\n" +
		"*We don't know what is the projectec increase in these rental incomes\n" +
		"*Maintenance costs and transaction costs used as % here are indicative value but we should work on it\n" +
		"*Inflation rate: 4,5%, I am not sure if it is the \"right\" number to use"
		};

	//Output(1) & (2) pages
	public static final String[] outputGroupControlNames = {"Portfolio NPV Valuation", "Statistical Model", "Evolution of the Three Scenarios in the Future"};
	
	public static final int outputGroupControlWidth = 940;
	public static final int[] outputGroupControlHeights = {200, 190, 130};
	public static final int outputGroupControlX = 20;
	public static final int[] outputGroupControlYs = {10, 230, 440};
	
	public static final int outputNPVTableTitleRowNum = 3;
	public static final int outputNPVTableTitleColNum = 10;
	
	public static final int outputNPVTableXOffset = 10;
	public static final int[] outputNPVTableTitleHeights = {15, 15, 15};
	public static final int[] outputNPVTableTitleWidths_0 = {120, 690};
	public static final int[] outputNPVTableTitleWidths_1 = {120, 30, 200, 30, 200, 30, 200};
	public static final int[] outputNPVTableTitleWidths_2 = {120, 30, 100, 100, 30, 100, 100, 30, 100, 100};

	public static final int outputNPVTableHeight = 130;
	public static int getNPVTableColNum(int row) {
		if (row == 0) return 2;
		else if (row == 1) return 7;
		else if (row == 2) return 10;
		else return 0;
	}
	
	public static int getNPVTableTitleY(int row) {
		int sum = 0;
		for (int i=0;i<row;i++) {
			sum+=outputNPVTableTitleHeights[i];
		}
		return sum;
	}
	
	public static int getNPVTableTitleX(int row, int col) {
		int sum = 0;
		if (row == 0) {
			for (int i=0;i<col;i++) {
				sum+=outputNPVTableTitleWidths_0[i];
			}
		}
		else if (row == 1) {
			for (int i=0;i<col;i++) {
				sum+=outputNPVTableTitleWidths_1[i];
			}
		}
		else if (row == 2) {
			for (int i=0;i<col;i++) {
				sum+=outputNPVTableTitleWidths_2[i];
			}
		}
		return sum;
	}
	
	public static int getNPVTableTitleWidth(int row, int col) {
		if (row == 0) return outputNPVTableTitleWidths_0[col];
		else if (row == 1) return outputNPVTableTitleWidths_1[col];
		else if (row == 2) return outputNPVTableTitleWidths_2[col];
		else return 0;
	}
	public static String getNPVTableTitle(int row, int col) {
		if (row == 0 && col == 1) return "NPV";
		else if (row == 1 && col == 0) return "ARC Portfolios";
		else if (row == 1 && col == 2) return "Rent it and sell it later";
		else if (row == 1 && col == 4) return "Sell it later";
		else if (row == 1 && col == 6) return "Hold it and sell later";
		else if (row == 2 && col == 0) return "Loan account";
		else if (row == 2 && (col == 2 || col == 5 || col == 8)) return "ARC";
		else if (row == 2 && (col == 3 || col == 6 || col == 9)) return "Altura";
		else return "";
	}
	
	public static final int outputStatsTableXOffset = outputNPVTableXOffset;
	public static final int[] outputStatsTableWidths = {200, 100, 100, 100, 100};
	public static int getStatsTableTitleX(int col) {
		int sum = 0;
		for (int i=0;i<col;i++) {
			sum+=outputStatsTableWidths[i];
		}
		return sum;
	}
	public static final int outputStatsTableHeight = 110;
	public static final int outputStatsTableTitleHeight = 15;
	public static final int outputStatsTableColNum = 5;
	
	public static final String[] outputStatsTableTitles = {"Loan Selection", "# of Deals", 
		"Method", "Estimate", "Upper Bound", "Lower Bound", "Std/R2"};
//	public static final String[] estimationMethods = {"Average", "Case-Shiller Regression", 
//		"Zillow Regression", "Case-Shiller Extrapolation", "Zillow Extrapolation"};
//	
	public static final String[] estimationMethods = {"Regression"};
	
	//Portfolio NPV page
	public static final String[] npvGroupControlNames = {"Parameters", "Statistical Market Analysis", "NPV Calculation"};
	public static final int[] npvGroupControlHeights = {180,90,200};
	public static final int npvGroupControlWidth = 940;
	public static final int npvGroupControlX = 20;
	public static final int[] npvGroupControlYs = {10, 210, 320};
	
	public static final String[] npvParamsNames = {"Today's date", "IRR (%)", "Monthly IRR (%)", "Inflation Rate (%)", "Maintenance Costs (%)", "Transaction Costs (%)"};
	public static final boolean[] npvParamsEditable = {false, true, false, true, true, true};
	public static final int npvParamLabelWidth = 200;
	public static final int npvParamTextWidth = 100;
	public static final int npvParamHeight = 20;
	
	public static final String[] npvStatsNames = {"Data Timeframe (Months)", "Square Footage Constraint (%)"};
	public static final int npvStatsLabelWidth = 200;
	public static final int npvStatsTextWidth = 100;
	public static final int npvStatsHeight = 20;
	
//	public static final String[] npvCalTitle1 = {"ARC Portfolio", "Address", "Values of the property given by ARC", 
//												"Characteristics of the Property", "Our estimated prices",
//												"Management of the Property", "Rent it and sell it later", 
//												"Sell it now", "Hold and sell later"};
	public static final String[] npvCalTitle2 = {"Loan Account", "Zip Code", 
												"Street", "Appraiser FMV", "Value", "Projected Recovery",
												"Projected Timeline", "Lot Size", "Rental (Zestimate)",
												"Today's Est. Price", "Proj. Timeline Est. Price",
												"Change in Value", "Rental over the Period",
												"Maintenance Costs over the period", 
												"Rent and sell later_ARC", "Rent and sell later_Altura", 
												"Sell now_ARC", "Sell now_Altura", 
												"Hold and sell later_ARC", "Hold and sell later_Altura"};
	public static final int npvCalWidth = 900;
	public static final int npvCalHeight = 180;
	public static final int npvCalColumnWidth = 100;
	
	//Sensitivity page
	public static final String[] senGroupControlNames = {"Parameters", "NPV Estimates"};
	public static final int[] senGroupControlHeights = {240,350};
	public static final int senGroupControlWidth = 940;
	public static final int senGroupControlX = 20;
	public static final int[] senGroupControlYs = {10, 270};
	
	public static final String[] senParamsNames0 = {"Loan Selection", "IRR (%)", "Monthly IRR (%)", "Maintenance costs (%)", 
												  "Monthly Maintenance costs (%)", "Transaction costs (%)", "Inflation rate (%)", "Rental Income",
												  "Projected timeline"};
	public static final boolean[] senParamsEditable0 = {false, true, false, true, false, true, true, false, false};
 	public static final String[] senParamsNames1 = {"Lot size", "Appraiser FMV", "ARC Price", "Altura Today's Price",
												   "Altura Projected Price"};
 	public static final boolean[] senParamsEditable1 = {false, false, false, false, false};
	public static final int senParamLabelWidth = 200;
	public static final int senParamTextWidth = 100;
	public static final int senParamHeight = 20;
	public static final int[] senParamXs = {10, 350}; 
	
	public static final int[] senNpvGroupControlXs = {10, 460};
	public static final int[] senNpvGroupControlWidths = {430, 430};
	public static final int senNpvGroupControlHeight = 340;
	
	public static final int senNpvTableWidth = 420;
	public static final int senNpvTableHeight = 320;
	public static final String[] senNpvTableNames = {"Timeline", "Rental length (months)", "Our projected price", 
													 "Rent and sell later", "Sell now", "Hold and sell later"};
	public static final int[] senNpvTableColumnWidth = {60, 100, 100, 100, 80, 120};
	
	//Statistical Model page
	public static final String[] statsGroupControlNames = {"Parameters", "NPV Estimates"};
	public static final int[] statsGroupControlHeights = {70,540};
	public static final int statsGroupControlWidth = 940;
	public static final int statsGroupControlX = 20;
	public static final int[] statsGroupControlYs = {10, 90};
	
	public static final String[] statsParamsNames = {"Loan Selection", "Estimation Method"};
	public static final boolean[] statsParamsEditable = {false, false};
	public static final int statsParamLabelWidth = 200;
	public static final int statsParamTextWidth = 100;
	public static final int statsParamHeight = 20; 
	
	public static final int[] statsNpvGroupControlXs = {10, 460, 460};
	public static final int[] statsNpvGroupControlYs = {10, 10,330};
	public static final int[] statsNpvGroupControlWidths = {430, 430, 430};
	public static final int[] statsNpvGroupControlHeights = {500, 240, 240};
	
	public static final int statsNpvTableWidth = 420;
	public static final int statsNpvTableHeight = 490;
	public static final String[] statsNpvTableNames = {"Timeline", "Best Estimate", "Our projected price", 
													 "Zillow price" }; //, "Upper Bound of Estimate", "Lower Bound of Estimate"};
	public static final int[] statsNpvTableColumnWidth = {60, 100, 100, 100}; //, 80, 120};
	
	
}
