package view;

public class ViewConst {
	public static final int mainWindowWidth = 1000;
	public static final int mainWindowHeight = 700;
	public static final String[] tabFolderNames = {"User Guide", "Output(1)", "Output(2)", "Portfolio NPV", "Sensitivity", "Statistical Model", "Dynamic Pricing"};
	
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
	

	public static final String[] userguideGroupControlNames = {"General Information", "Methodology", "Input and Output"};
	public static final String[] userguideSessions = {
		//The first session
		"*This tool enables users to estimate the market value of perperties in the ARC's portfolio via a regression model.\n",
			
		//The second session
			"*Historical transaction records are downloaded from MLS and cleaned, then imported to a MySQL database.\n" +
		"*Records of ARC's portfolio are cleaned and kept in a CSV file.\n" +
		"*The \"Portfolio value\" tab shows the estimates of properties in ARC's portfolio. Only properties with completed information are included.\n" +
		"*For each property, the estimate from Zillow, i.e. the ZEstimate, the estimate from our model, and the difference between these two estimates are shown.\n",
		//The third session
		"*A log-linear regression model is applied to estimate the market value of each property.\n" +
		"*In the regression model, the dependent variables is the \"total price\", and independent variables include \"number of bedrooms\",\n\"number of bathrooms\", \"size of the property\", \"zip code\" (as dummy variables), and \"Case Shiller Index\"\n"
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
	public static final String[] npvGroupControlNames = {"Parameters", "Statistical Market Analysis", "Market Value Estimates"};
	public static final int[] npvGroupControlHeights = {180,90,250};
	public static final int npvGroupControlWidth = 940;
	public static final int npvGroupControlX = 20;
	public static final int[] npvGroupControlYs = {10, 210, 325};
	
	public static final String[] npvParamsNames = {"Today's date", "IRR (%)", "Monthly IRR (%)", "Maintenance Costs (%)", "Transaction Costs (%)", "Tax (%)"};
	//public static final String[] npvParamsNames = {"Today's date"};
	public static final boolean[] npvParamsEditable = {false, true, false, true, true, true};
	public static final int npvParamLabelWidth = 200;
	public static final int npvParamTextWidth = 100;
	public static final int npvParamHeight = 20;
	
	public static final String[] npvStatsNames = {"Data Timeframe (Months)", "Square Footage Constraint (%)"};
	public static final int npvStatsLabelWidth = 200;
	public static final int npvStatsTextWidth = 100;
	public static final int npvStatsHeight = 20;
	
	public static final String[] npvCalTitle = {"Loan Account", "Zip Code", 
												"Street", "Type", "Square Footage", "# of Bedrooms", "# of Bathrooms",
												"Zillow Estimate", "Today's Est. Price", "Difference in Value", 
												"NPV Value", "Projected Timeline", "Absorption Rate"};
	public static final int npvCalWidth = 910;
	public static final int npvCalHeight = 210;
	public static final int npvCalColumnWidth = 80;
	
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
