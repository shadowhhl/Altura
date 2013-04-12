package model;

import portfolio.*;
import view.ViewConst;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import data.*;

public class ContentController {
	private int timelineLength = 24;
	
	public static final int DISPLAY_DEFAULT		 		= 0;
	public static final int DISPLAY_NPV_PARAMS	 		= 1;
	public static final int DISPLAY_NPV_STATS	 		= 2;
	public static final int DISPLAY_NPV_TABLE	 		= 3;
	public static final int DISPLAY_SEN_PARAMS1  		= 4;
	public static final int DISPLAY_SEN_PARAMS2  		= 5;
	public static final int DISPLAY_STATS_PARAMS 		= 6;
	public static final int DISPLAY_ALL_ACOUNT_NUM 		= 7;
	public static final int DISPLAY_OUTPUT_NPV_TABLE 	= 8;
	public static final int DISPLAY_OUTPUT_STATS_TABLE 	= 9;
	public static final int DISPLAY_SEN_NPV_TABLE   	= 10;
	public static final int DISPLAY_STATS_NPV_TABLE 	= 11;
	public static final int DISPLAY_UPDATE				= 12;
	
	public static final int SQL_COMPARABLE_LIST_CSONLY				= 0;
	
	public static final int PROPERTY_TYPE_SFR           = 0;
	public static final int PROPERTY_TYPE_CONDO         = 1;
	
	public ArrayList<String[]> getUpdateSenNpvTableContent(Portfolio portfolio, int propertyIndex, ParamList paramList) {
		ArrayList<String[]> contents = new ArrayList<String[]>();
		SensitivityAnalyzer sa = new SensitivityAnalyzer();
		ArrayList<Calendar> futureDates = new ArrayList<Calendar>();
		for (int i=0;i<timelineLength;i++) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.MONTH, i);
			futureDates.add(date);
		}
		sa.calculatePrices(portfolio, propertyIndex, paramList, futureDates);
		for (int rowInd = 0;rowInd<timelineLength;rowInd++) {
			ArrayList<String> row = new ArrayList<String>();
			
			row.add(0, Formater.toDateString(futureDates.get(rowInd).get(Calendar.YEAR), futureDates.get(rowInd).get(Calendar.MONTH)+1));
			row.add(1, String.valueOf(rowInd));
			row.add(2, Formater.toCurrency(sa.getPriceByAccountByDate(portfolio.getValue(propertyIndex, "Account"), 
																						 futureDates.get(rowInd))));
			
			String[] newRow = new String[row.size()];
			System.out.println(row);
			row.toArray(newRow);
			contents.add(newRow);
		}
		return contents;
	}
	
	public ArrayList<String[]> getDefaultTableContent(int[] options, Object addObj) {
		ArrayList<String[]> contents = new ArrayList<String[]>();
		switch(options[0]) {
		case DISPLAY_UPDATE: {
			switch (options[1]) {
			case DISPLAY_SEN_NPV_TABLE: {
				
				break;
			}
			case DISPLAY_NPV_TABLE: {
				Portfolio tempPortfolio = (Portfolio)addObj;
				int numRecords = tempPortfolio.getPortfolioSize();
				for (int i=0;i<numRecords;i++) {
					ArrayList<String> row = new ArrayList<String>();
					HashMap<String, String> portfolioRow = tempPortfolio.getEntry(i);
					double todayPrice=1, projPrice=1;
					for (int j=0;j<ViewConst.npvCalTitle2.length;j++) {
						switch (j) {
						case 0: {row.add(j, portfolioRow.get("Account"));break;}
						case 1: {row.add(j, portfolioRow.get("Zip Code"));break;}
						case 2: {row.add(j, portfolioRow.get("Street"));break;}
						case 3: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Appraiser FMV"))));break;}
						case 4: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Value"))));break;}
						case 5: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Projected Recovery"))));break;}
						case 6: {row.add(j, portfolioRow.get("Projected Timeline"));break;}
						case 7: {row.add(j, portfolioRow.get("Size/SqFeet"));break;}
						case 8: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Zestimate Rental"))));break;}
						case 9: { //Today's Est. Price
							Calendar now = Calendar.getInstance();
							
							int numBedrms = Integer.valueOf(portfolioRow.get("#Bedrooms"));
							double numBathrms = Double.valueOf(portfolioRow.get("#Bathrooms"));
							int lotSize = Integer.valueOf(portfolioRow.get("Size/SqFeet"));
							int age = now.get(Calendar.YEAR) - Integer.valueOf(portfolioRow.get("Year Built"));
							String zipCode = portfolioRow.get("Zip Code");
							Regression reg = new Regression();
							Index index = new Index();
							index.readIndex(Index.defaultCSIndexFileName);
							Double indexValue = index.getIndex(now);
							ArrayList<Double> indexValues = new ArrayList<Double>();
							indexValues.add(indexValue);
							ArrayList<Double> prices = reg.predict(numBedrms, numBathrms, age, lotSize, indexValues, zipCode); 
							todayPrice = prices.get(0);
							row.add(j, Formater.toCurrency(todayPrice));
							break;
							}
						case 10: { //projected price
							try {
								Calendar now = Calendar.getInstance();
								Calendar projTime = Calendar.getInstance();
								String projTimeStr = portfolioRow.get("Projected Timeline");
								projTime.setTime(new SimpleDateFormat("M-d-yyyy").parse(projTimeStr));
								
								int numBedrms = Integer.valueOf(portfolioRow.get("#Bedrooms"));
								double numBathrms = Double.valueOf(portfolioRow.get("#Bathrooms"));
								int lotSize = Integer.valueOf(portfolioRow.get("Size/SqFeet"));
								int age = now.get(Calendar.YEAR) - Integer.valueOf(portfolioRow.get("Year Built"));
								String zipCode = portfolioRow.get("Zip Code");
								Regression reg = new Regression();
								Index index = new Index();
								index.readIndex(Index.defaultCSIndexFileName);
								Double indexValue = index.getIndex(projTime);
								ArrayList<Double> indexValues = new ArrayList<Double>();
								indexValues.add(indexValue);
								
								ArrayList<Double> predictedPrices = reg.predict(numBedrms, numBathrms, age, lotSize, indexValues, zipCode); 
								projPrice =  predictedPrices.get(0);
								row.add(j, Formater.toCurrency(projPrice));
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
							}
						case 11: {row.add(j, Formater.toPercentage(projPrice/todayPrice));break;}
						case 12: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Rental Over Period"))));break;}
						case 13: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Maintenance Over Period"))));break;}
						case 14: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Rent and Sell later_ARC"))));break;}
						case 15: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Rent and Sell later_Altura"))));break;}
						case 16: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Sell now_ARC"))));break;}
						case 17: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Sell now_Altura"))));break;}
						case 18: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Hold and Sell later_ARC"))));break;}
						case 19: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Hold and Sell later_Altura"))));break;}
						default: {row.add(j, null);break;}
						}
					}
					String[] newRow = new String[row.size()];
					row.toArray(newRow);
					contents.add(newRow);
				}
				break;
				}
			default: {
				break;
			}
			}
		}
		case DISPLAY_DEFAULT: {
			switch (options[1]) {
			case DISPLAY_SEN_NPV_TABLE: {
				
				for (int rowInd = 0;rowInd<timelineLength;rowInd++) {
					//get current date
					Calendar now = Calendar.getInstance();
					now.add(Calendar.MONTH, rowInd);
					String timelineStr = Formater.toDateString(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1);
					ArrayList<String> row = new ArrayList<String>();
					row.add(0, timelineStr);
					row.add(1, String.valueOf(rowInd));
					String[] newRow = new String[row.size()];
					row.toArray(newRow);
					contents.add(newRow);
				}
				break;
			}
			case DISPLAY_STATS_NPV_TABLE: {
				for (int rowInd = 0;rowInd<timelineLength;rowInd++) {
					//get current date
					Calendar now = Calendar.getInstance();
					now.add(Calendar.MONTH, rowInd);
					String timelineStr = Formater.toDateString(now.get(Calendar.YEAR), now.get(Calendar.MONTH)+1);
					ArrayList<String> row = new ArrayList<String>();
					row.add(0, timelineStr);
					row.add(1, String.valueOf(rowInd));
					
					String[] newRow = new String[row.size()];
					row.toArray(newRow);
					contents.add(newRow);
				}
				break;
			}
			case DISPLAY_OUTPUT_STATS_TABLE: {
				for (int i=0;i<ViewConst.estimationMethods.length;i++) {
					ArrayList<String> row = new ArrayList<String>();
					row.add(0, ViewConst.estimationMethods[i]);
					
					String[] newRow = new String[row.size()];
					row.toArray(newRow);
					contents.add(newRow);
				}
				break;
			}
			case DISPLAY_OUTPUT_NPV_TABLE: {
				Portfolio tempPortfolio = (Portfolio)addObj;
				int numRecords = tempPortfolio.getPortfolioSize();
				for (int i=0;i<numRecords;i++) {
					ArrayList<String> row = new ArrayList<String>();
					HashMap<String, String> portfolioRow = tempPortfolio.getEntry(i);
					for (int j=0;j<ViewConst.npvCalTitle2.length;j++) {
						switch (j) {
						case 0: {row.add(j, portfolioRow.get("Account"));break;}
						
						}
					}
					String[] newRow = new String[row.size()];
					row.toArray(newRow);
					contents.add(newRow);
				}
				break;
			}
			case DISPLAY_NPV_TABLE: {				
				Portfolio tempPortfolio = (Portfolio)addObj;
				int numRecords = tempPortfolio.getPortfolioSize();
				for (int i=0;i<numRecords;i++) {
					ArrayList<String> row = new ArrayList<String>();
					HashMap<String, String> portfolioRow = tempPortfolio.getEntry(i);
					for (int j=0;j<ViewConst.npvCalTitle2.length;j++) {
						switch (j) {
						case 0: {row.add(j, portfolioRow.get("Account"));break;}
						case 1: {row.add(j, portfolioRow.get("Zip Code"));break;}
						case 2: {row.add(j, portfolioRow.get("Street"));break;}
						case 3: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Appraiser FMV"))));break;}
						case 4: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Value"))));break;}
						case 5: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Projected Recovery"))));break;}
						case 6: {row.add(j, portfolioRow.get("Projected Timeline"));break;}
						case 7: {row.add(j, portfolioRow.get("Size/SqFeet"));break;}
						case 8: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Zestimate Rental"))));break;}
						//case 9: {row.add(j, Formater.toCurrency(Double.valueOf(portfolioRow.get("Today's Est. Price"))));break;}
						default: {row.add(j, null);break;}
						}
					}
					String[] newRow = new String[row.size()];
					row.toArray(newRow);
					contents.add(newRow);
				}
				break;
			}
			}
			break;
		}
		default: {
			break;
		}
		}
		return contents;
	}
	
	public ArrayList<String> getDefaultContent(int[] options, Object addObject) {
		ArrayList<String> contents = new ArrayList<String>();
		switch(options[0]) {
		case DISPLAY_DEFAULT: {//default
			switch (options[1]) {
			case DISPLAY_ALL_ACOUNT_NUM: {
				Portfolio portfolio = (Portfolio)addObject;
				contents = portfolio.getAccountNum();
				break;
			}
			case DISPLAY_NPV_PARAMS: {
				//Today's date
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				contents.add(sdf.format(date));
				//IRR
				contents.add("10");
				//Monthly IRR
				double mIRR = 100.0*(Math.pow(1+10.0/100.0, 1.0/12.0)-1.0);
				contents.add(Formater.toShortDouble(mIRR,2));
				//Inflation Rate
				contents.add("2.5");
				//Maintenance Costs
				contents.add("8");
				//Transaction Costs
				contents.add("7");
				break;
			}
			case DISPLAY_NPV_STATS: {
				//Data Timeframe
				contents.add("12");
				//Square Footage Constraints
				contents.add("50");
				break;
			}
			case DISPLAY_NPV_TABLE: {
				System.out.println("Called wrong method");
				break;
			}
			case DISPLAY_SEN_PARAMS1: {
				contents.add(null);
				//IRR
				contents.add("10");
				//Monthly IRR
				double mIRR = 10.0/12.0;
				contents.add(Formater.toShortDouble(mIRR,2));
				//Maintenance Cost annual
				contents.add("8");
				//Maintenance Costs
				double mMC = 8.0/12.0;
				contents.add(Formater.toShortDouble(mMC,2));
				//Transaction Costs
				contents.add("7");
				//Inflation Rate
				contents.add("2.5");
				//Rental Income
				contents.add("N/A");
				//Projected Timeline
				contents.add("N/A");
				break;
			}
			case DISPLAY_SEN_PARAMS2: {
				break;
			}
			case DISPLAY_STATS_PARAMS: {
				contents.add(null);
				//Estimation Method
				contents.add("N/A");
			}
			}
			break;
		}
		default: {
			System.out.println("Invalid display option");
			break;
		}
		}
		return contents;
	}

	public ArrayList< HashMap<String, String> > getSqlResult(String stmt) {
		System.out.println(stmt);
		Db db = new Db();
		db.connect();
		db.query(stmt);
		
		//clean data
		//only keep data within 12 months
		ArrayList< HashMap<String, String> > sqlResult = db.parseResult();
		ArrayList< HashMap<String, String> > cleanResult = new ArrayList<HashMap<String,String>>();
		try {
			Calendar now = Calendar.getInstance();
			for (int i=0;i<sqlResult.size();i++) {
				Calendar listDate = Calendar.getInstance();
				listDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(sqlResult.get(i).get("mlx_LD")));
				long millisDiff = now.getTimeInMillis() - listDate.getTimeInMillis();
				Double dayDiff = millisDiff/1000.0/3600.0/24.0;
				if (dayDiff<=365) {
					cleanResult.add(sqlResult.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cleanResult;
	}
	
	public String sqlGenerator(int option, Integer numBedrooms, Integer numBathrooms, Double size, String zipCode, int propertyType) {
		String sqlStmt = ""; 
		
		switch (option) {
		case 0: { //
			Double sizeHigh = size*1.5;
			Double sizeLow = size*0.5;
			if (propertyType== PROPERTY_TYPE_SFR)
				sqlStmt = "select mlx_LPdollar, mlx_LD, mlx_LA from MLX_SFR_data where mlx_BEDSsharp=" + numBedrooms.toString() +
						" and mlx_FBsharp= " + numBathrooms.toString() + 
						" and mlx_LA > " + sizeLow.toString() +
						" and mlx_LA < " + sizeHigh.toString() + 
						" and mlx_ZIP= " + zipCode + 
						" and mlx_STATUS='CS'" +
						" and mlx_LPdollar <> ''";
			else if (propertyType == PROPERTY_TYPE_CONDO)
				sqlStmt = "select mlx_LPdollar, mlx_LD, mlx_LA from MLX_CONDO_data where mlx_BEDSsharp=" + numBedrooms.toString() +
				" and mlx_FBsharp= " + numBathrooms.toString() + 
				" and mlx_LA > " + sizeLow.toString() +
				" and mlx_LA < " + sizeHigh.toString() + 
				" and mlx_ZIP= " + zipCode +
				" and mlx_STATUS='CS'" +
				" and mlx_LPdollar <> ''";
			break;
		}
		default: {
			break;
		}
		}
		return sqlStmt;
	}
}
