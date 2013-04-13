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
	
	public static final double sfrGapThreshold = 0.25;
	public static final double condoGapThreshold = 0.165;
	
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
	
	public ArrayList<String[]> getUpdateStatsNpvTableContent(Portfolio portfolio, int propertyIndex, ParamList paramList) {
		ArrayList<String[]> contents = new ArrayList<String[]>();
		SensitivityAnalyzer sa = new SensitivityAnalyzer();
		ArrayList<Calendar> futureDates = new ArrayList<Calendar>();
		for (int i=0;i<timelineLength;i++) {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.MONTH, i);
			futureDates.add(date);
		}
		sa.calculatePrices(portfolio, propertyIndex, paramList, futureDates);
		Double zEstimatePriceToday = Double.valueOf(portfolio.getValue(propertyIndex, "Zestimate Px"));
		Double inflationRate = Double.valueOf(paramList.getParam("Inflation Rate (%)"));
		for (int rowInd = 0;rowInd<timelineLength;rowInd++) {
			ArrayList<String> row = new ArrayList<String>();
			
			Double zEstimatePrice = zEstimatePriceToday * Math.pow((1+Formater.annualToMonth(inflationRate.doubleValue())/100.0), rowInd);
			Double ourProjPrice = sa.getPriceByAccountByDate(portfolio.getValue(propertyIndex, "Account"), futureDates.get(rowInd));
			Double bestEstimate;
			
			row.add(0, Formater.toDateString(futureDates.get(rowInd).get(Calendar.YEAR), futureDates.get(rowInd).get(Calendar.MONTH)+1));
			//row.add(1, String.valueOf(rowInd));
			
			if (ourProjPrice < zEstimatePrice*1.1 && ourProjPrice > zEstimatePrice *0.9) {
				bestEstimate = ourProjPrice;
			} else {
				bestEstimate = zEstimatePrice;
			}
			row.add(1, Formater.toCurrency(bestEstimate.doubleValue()));
			row.add(2, Formater.toCurrency(ourProjPrice.doubleValue()));
			row.add(3, Formater.toCurrency(zEstimatePrice.doubleValue()));
			String[] newRow = new String[row.size()];
			System.out.println(row);
			row.toArray(newRow);
			contents.add(newRow);
		}
		return contents;
	}
	
	public ArrayList<String[]> getTableContent(int[] options, Object addObj) {
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
				Regression regression = new Regression();
				regression.regression();
				boolean invalidRecord = false;
				for (int i=0;i<numRecords;i++) {
					invalidRecord = false;
					ArrayList<String> row = new ArrayList<String>();
					HashMap<String, String> portfolioRow = tempPortfolio.getEntry(i);

					Calendar now = Calendar.getInstance();
					int numBedrms = Integer.valueOf(portfolioRow.get("#Bedrooms"));
					double numBathrms = Double.valueOf(portfolioRow.get("#Bathrooms"));
					int lotSize = Integer.valueOf(portfolioRow.get("Size/SqFeet"));
					int age = now.get(Calendar.YEAR) - Integer.valueOf(portfolioRow.get("Year Built"));
					String zipCode = portfolioRow.get("Zip Code");
					String type = portfolioRow.get("Type");

					Index index = new Index();
					index.readIndex(Index.defaultCSIndexFileName);
					
					Double todayPrice=1.0, projPrice=1.0, zillowPrice=null;
					for (int j=0;j<ViewConst.npvCalTitle.length;j++) {
						switch (j) {
						case 0: {row.add(j, portfolioRow.get("Account"));break;}
						case 1: {row.add(j, portfolioRow.get("Zip Code"));break;}
						case 2: {row.add(j, portfolioRow.get("Street"));break;}
						case 3: {row.add(j, portfolioRow.get("Type"));break;}
						case 10: {
							String appraiserFMVStr = portfolioRow.get("Appraiser FMV");
							if (appraiserFMVStr==null) {
								row.add(j, "N/A");
							} else {
								row.add(j, Formater.toCurrency(Double.valueOf(appraiserFMVStr)));
							}
							break;
						}
						case 11: {
							String valueStr = portfolioRow.get("Value");
							if (valueStr==null) {
								row.add(j, "N/A");
							} else {
								row.add(j, Formater.toCurrency(Double.valueOf(valueStr)));
							}
							break;
						}
						case 12: {
							String projRecStr = portfolioRow.get("Projected Recovery");
							if (projRecStr==null) {
								row.add(j, "N/A");
							} else {
								row.add(j, Formater.toCurrency(Double.valueOf(projRecStr)));
							}
							break;
						}
						case 4: {
							String projTimeline = portfolioRow.get("Projected Timeline");
							if (projTimeline==null) {
								row.add(j,  "N/A");
							} else {
								row.add(j, projTimeline);
							}
							break;
						}
						case 5: {
							String sizeStr = portfolioRow.get("Size/SqFeet");
							if (sizeStr==null) {
								row.add(j, "N/A");
							} else {
								row.add(j, sizeStr);
							}
							break;
						}
						case 6: {
							String zEstimateStr = portfolioRow.get("Zestimate Px");
							if (zEstimateStr==null) {
								row.add(j, "N/A");
								zillowPrice = null;
							} else {
								zillowPrice = Double.valueOf(zEstimateStr);
								row.add(j, Formater.toCurrency(zillowPrice));
//								row.add(j, zEstimateStr);
							}
							break;
						}
						case 7: { //Today's Est. Price
							Double indexValue = index.getIndex(now);
							Double priceCS, priceA;
							if (type.equalsIgnoreCase("SFR")) {
								priceCS = regression.predict(Regression.SFR_CS_COEFF_INDEX, numBedrms, numBathrms, 
										age, lotSize, indexValue.doubleValue(), zipCode);
								priceA = regression.predict(Regression.SFR_A_COEFF_INDEX, numBedrms, numBathrms, 
										age, lotSize, indexValue.doubleValue(), zipCode);
							} else if (type.equalsIgnoreCase("Condo")) { //type==Code
								priceCS = regression.predict(Regression.CONDO_CS_COEFF_INDEX, numBedrms, numBathrms, 
										age, lotSize, indexValue.doubleValue(), zipCode);
								priceA = regression.predict(Regression.CONDO_A_COEFF_INDEX, numBedrms, numBathrms, 
										age, lotSize, indexValue.doubleValue(), zipCode);
							} else {
								priceCS = null;
								priceA = null;
							}
							if (priceA!=null && priceCS!=null) {
								double csDiff = Math.abs(priceCS/zillowPrice-1);
								double aDiff = Math.abs(priceA/zillowPrice-1);
								todayPrice = (csDiff<aDiff) ? priceCS : priceA;
							} else if (priceA==null && priceCS != null) {
								todayPrice = priceCS;
							} else if (priceA != null && priceCS == null) {
								todayPrice = priceA;
							} else { 
								todayPrice =zillowPrice;
							}
							
							if (type.equalsIgnoreCase("SFR") && Math.abs(todayPrice/zillowPrice-1)>sfrGapThreshold) {
								row.add(j, "N/A");
								invalidRecord = true;
							} else if (type.equalsIgnoreCase("Condo") && Math.abs(todayPrice/zillowPrice-1)>condoGapThreshold) {
								row.add(j, "N/A");
								invalidRecord = true;
							} else {
								row.add(j, Formater.toCurrency(todayPrice));
//								row.add(j, String.valueOf(todayPrice));
							}
							break;
						}
						case 8: { //projected price
							try {
								Calendar projTime = Calendar.getInstance();
								String projTimeStr = portfolioRow.get("Projected Timeline");
								projTime.setTime(new SimpleDateFormat("M-d-yyyy").parse(projTimeStr));
								Double indexValue = index.getIndex(projTime);
								
								if (type.equalsIgnoreCase("SFR")) {
									projPrice = regression.predict(Regression.SFR_CS_COEFF_INDEX, numBedrms, numBathrms, 
																	age, lotSize, indexValue.doubleValue(), zipCode);
								} else {
									projPrice = regression.predict(Regression.CONDO_CS_COEFF_INDEX, numBedrms, numBathrms, 
											age, lotSize, indexValue.doubleValue(), zipCode);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if (projPrice==null) {
								row.add(j, "N/A");
							} else {
								row.add(j, Formater.toCurrency(projPrice));
//								row.add(j, String.valueOf(projPrice));
							}
							break;
						}
						case 9: {
							if (todayPrice==null || projPrice==null) {
								row.add(j, "N/A");
							} else {
								row.add(j, Formater.toPercentage(projPrice/todayPrice));
							}
							break;
						}
						default: {row.add(j, null);break;}
						}
					}
					if (!invalidRecord) {
						String[] newRow = new String[row.size()];
						row.toArray(newRow);
						contents.add(newRow);
					} 
				}
				break;
				}
			default: {
				break;
			}
			}
			break;
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
					for (int j=0;j<ViewConst.npvCalTitle.length;j++) {
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
					for (int j=0;j<ViewConst.npvCalTitle.length;j++) {
						switch (j) {
						case 0: {row.add(j, portfolioRow.get("Account"));break;}
						case 1: {row.add(j, portfolioRow.get("Zip Code"));break;}
						case 2: {row.add(j, portfolioRow.get("Street"));break;}
						case 3: {row.add(j, portfolioRow.get("Type"));break;}
						case 4: {row.add(j, portfolioRow.get("Projected Timeline"));break;}
						case 6: {
							String zPriceStr = portfolioRow.get("Zestimate Px");
							if (zPriceStr!=null) {
								Double zPrice = Double.valueOf(zPriceStr);
								row.add(j, Formater.toCurrency(zPrice.doubleValue()));
							} else {
								row.add(j, "N/A");
							}
							break;
						}
						case 5: {row.add(j, portfolioRow.get("Size/SqFeet"));break;}
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
	
	public ArrayList<String> getParamContent(int[] options, Object addObject) {
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
				double mIRR = Formater.annualToMonth(10.0);
				contents.add(Formater.toShortDouble(mIRR,2));
				//Maintenance Cost annual
				contents.add("8");
				//Maintenance Costs
				double mMC = Formater.annualToMonth(8.0);
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
				contents.add(ViewConst.estimationMethods[0]);
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
		//System.out.println(stmt);
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
