package view;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import model.Formater;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.googlecode.charts4j.Color;

import chart.ScatterARC;
import data.Index;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class PricePlotWindow {

	private ArrayList<HashMap<String, String>> closedPropertyList;
	private ArrayList<HashMap<String, String>> activePropertyList;
	protected Shell plotShell;

	private static int closedPriceOptionAdjusted = 1;
	private static int closedPriceOptionUnadjusted = 3;
	private static int activePriceOption = 2;
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		plotShell.open();
		plotShell.layout();
		while (!plotShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void createContents() {
		int graphWidth=400;
		int graphHeight=300;
		int statsHeight=200;
		int statsWidth=graphWidth*2;
		
		plotShell = new Shell(SWT.CLOSE);
		plotShell.setSize(graphWidth+statsWidth, 2*graphHeight);
		plotShell.setText("Graph");

		Composite compositePrice = new Composite(plotShell, SWT.NONE);
		ScatterARC scatterChart = new ScatterARC();
		
		ArrayList<Double> closedPointsX = getPriceChartX(closedPriceOptionAdjusted);
		ArrayList<Double> closedPointsY = getPriceChartY(closedPriceOptionAdjusted);
		ArrayList<Double> closedPointsSize = getPriceChartSize(closedPriceOptionAdjusted);
		
		ArrayList<Double> activePointsX = new ArrayList<Double>();
		ArrayList<Double> activePointsY = getPriceChartY(activePriceOption);
		ArrayList<Double> activePointsSize = getPriceChartSize(activePriceOption);
		
		//Do scaling
		Double xMax = Double.MIN_VALUE;
		Double xMin = Double.MAX_VALUE;
		Double yMax = Double.MIN_VALUE;
		Double yMin = Double.MAX_VALUE;
		Double sizeMax = Double.MIN_VALUE;
		Double sizeMin = Double.MAX_VALUE;
		for (int i=0;i<closedPointsX.size();i++) {
			//System.out.println("X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
			if (closedPointsX.get(i)>xMax) xMax = closedPointsX.get(i);
			if (closedPointsX.get(i)<xMin) xMin = closedPointsX.get(i);
			if (closedPointsY.get(i)>yMax) yMax = closedPointsY.get(i);
			if (closedPointsY.get(i)<yMin) yMin = closedPointsY.get(i);
			if (closedPointsSize.get(i)>sizeMax) sizeMax = closedPointsSize.get(i);
			if (closedPointsSize.get(i)<sizeMin) sizeMin = closedPointsSize.get(i);
		}
		
		for (int i=0;i<activePointsY.size();i++) {
			//System.out.println("X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
			if (activePointsY.get(i)>yMax) yMax = activePointsY.get(i);
			if (activePointsY.get(i)<yMin) yMin = activePointsY.get(i);
			if (activePointsSize.get(i)>sizeMax) sizeMax = activePointsSize.get(i);
			if (activePointsSize.get(i)<sizeMin) sizeMin = activePointsSize.get(i);
		}
		
		if (yMax>yMin) {
			if (yMin-(yMax-yMin)/3.0>0) {
				//scale to 20-80
				for (int i=0;i<closedPointsY.size();i++) {
					closedPointsY.set(i, (closedPointsY.get(i)-yMin)/(yMax-yMin)*60+20);
				}
				for (int i=0;i<activePointsY.size();i++) {
					activePointsY.set(i, (activePointsY.get(i)-yMin)/(yMax-yMin)*60+20);
				}
				scatterChart.setyAxisMax(yMax+(yMax-yMin)/3.0);
				scatterChart.setyAxisMin(yMin-(yMax-yMin)/3.0);
			} else {
				for (int i=0;i<closedPointsY.size();i++) {
					closedPointsY.set(i, (closedPointsY.get(i)-0)/(yMax-0)*80);
				}
				for (int i=0;i<activePointsY.size();i++) {
					activePointsY.set(i, (activePointsY.get(i)-0)/(yMax-0)*80);
				}
				scatterChart.setyAxisMax(yMax + yMax/4.0);
				scatterChart.setyAxisMin(0.0);
			}
		} else {
			for (int i=0;i<closedPointsY.size();i++) {
				closedPointsY.set(i, 50.0);
			}
			for (int i=0;i<activePointsY.size();i++) {
				activePointsY.set(i, 50.0);
			}
			scatterChart.setyAxisMin(0.8*yMin);
			scatterChart.setyAxisMax(1.2*yMin);
		}
		
		
		for (int i=0;i<closedPointsX.size();i++) {
			closedPointsX.set(i, closedPointsX.get(i)/365*100.0+0);
			closedPointsSize.set(i, closedPointsSize.get(i)/sizeMax*2+5);
		}
		
		for (int i=0;i<activePointsY.size();i++) {
			activePointsX.add(99.0);
			activePointsSize.set(i, activePointsSize.get(i)/sizeMax*2+5);
		}
		
		scatterChart.setxAxisMin(-365.0);
		scatterChart.setxAxisMax(0.0);
		
		scatterChart.setClosedPointsX(closedPointsX);
		scatterChart.setClosedPointsY(closedPointsY);
		scatterChart.setClosedPointsSize(closedPointsSize);
		scatterChart.setActivePointsX(activePointsX);
		scatterChart.setActivePointsY(activePointsY);
		scatterChart.setActivePointsSize(activePointsSize);
		
		scatterChart.setLegend("Close Price");
		scatterChart.setChartWidth(graphWidth);
		scatterChart.setChartHeight(graphHeight);
		scatterChart.setChartTitle("Historical Transactions");
		
		String imageClosedURL = scatterChart.getChartUrlString();
		
		//set Image
		URL closedUrl;
		try {
			closedUrl = new URL(imageClosedURL);
			HttpURLConnection huc = (HttpURLConnection) closedUrl.openConnection();
			huc.setRequestMethod("GET");
			huc.connect();
			huc.setConnectTimeout(5*1000);
			int code = huc.getResponseCode();
			if (code == 200) {
				Display imgDisplay = Display.getDefault();
				Image closedImage = new Image(imgDisplay, closedUrl.openStream());
				compositePrice.setBounds(0, 0, closedImage.getImageData().width, closedImage.getImageData().height);
				compositePrice.setBackgroundImage(closedImage);
				//Manually draw the legends
				Label coverLabel = new Label(plotShell, SWT.NONE | SWT.ON_TOP);
				coverLabel.setBounds(315, 0, closedImage.getImageData().width-315, closedImage.getImageData().height);
				coverLabel.setBackground(SWTResourceManager.getColor(47,62,62));
				coverLabel.moveAbove(compositePrice);
				
				Label legendLabel1 = new Label(plotShell, SWT.NONE|SWT.ON_TOP);
				legendLabel1.setBounds(320, 150, 10, 10);
				legendLabel1.setBackground(SWTResourceManager.getColor(0, 0, 128));
				legendLabel1.moveAbove(coverLabel);
				
				Label legendTextLabel1 = new Label(plotShell, SWT.NONE);
				legendTextLabel1.setBounds(335, 147, 65, 38);
				legendTextLabel1.setText("Closed\nSales");
				legendTextLabel1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				legendTextLabel1.moveAbove(coverLabel);
				
				Label legendLabel2 = new Label(plotShell, SWT.NONE|SWT.ON_TOP);
				legendLabel2.setBounds(320, 195, 10, 10);
				legendLabel2.setBackground(SWTResourceManager.getColor(255, 71, 26));
				legendLabel2.moveAbove(coverLabel);
				
				Label legendTextLabel2 = new Label(plotShell, SWT.NONE);
				legendTextLabel2.setBounds(335, 192, 65, 38);
				legendTextLabel2.setText("Active\nSales");
				legendTextLabel2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				legendTextLabel2.moveAbove(coverLabel);
				
				//Manually draw axis name
				Label xLabel = new Label(plotShell, SWT.NONE);
				xLabel.setBounds(315, closedImage.getImageData().height-20, 60, 20);
				xLabel.setText("Timeline");
				xLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				xLabel.moveAbove(coverLabel);
				
				Label yLabel = new Label(plotShell, SWT.NONE);
				yLabel.setBounds(0, 6, 80, 20);
				yLabel.setText("Total Price");
				yLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				yLabel.moveAbove(coverLabel);
				
				int xOffset = graphWidth, yOffset = 30;
				createStats(xOffset, yOffset, statsWidth, statsHeight);
			} else  if (code==414) {
				Label errorLabel = new Label(plotShell, SWT.NONE);
				errorLabel.setBounds(0, 0, 500, 100);
				errorLabel.setText("Too many data points to show. Please set a smaller constraint.");
				
				int xOffset = graphWidth, yOffset = 30;
				createStats(xOffset, yOffset, statsWidth, statsHeight);
			} else if (code == 408) {
				Label errorLabel = new Label(plotShell, SWT.NONE);
				errorLabel.setBounds(0, 0, 500, 100);
				errorLabel.setText("Request Timeout. Please try again.");
			}
			plotShell.pack();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createStats(int xOffset, int yOffset, int statsWidth, int statsHeight) {
		int labelWidth=110;
		int labelHeight=20;
		
		String[] tableFirstRowNames = {"Now", "3 Months", "6 Months", "9 Months", "12 Months"};
		String[] tableFirstColumnNames = {"Max", "Min", "Median", "Average", "Std", "# of Transactions"};
		
		Label title = new Label(plotShell, SWT.NONE);
		title.setBounds(xOffset, 0, labelWidth*2, labelHeight);
		title.setText("Case Shiller Index Adjusted:");
		title = new Label(plotShell, SWT.NONE);
		title.setBounds(xOffset, 2*yOffset+6*labelHeight, labelWidth, labelHeight);
		title.setText("Unadjusted:");
		for (int i=0;i<tableFirstRowNames.length;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+(i+1)*labelWidth, yOffset, labelWidth, labelHeight);
			label.setText(tableFirstRowNames[i]);
			label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+(i+1)*labelWidth, 2*yOffset+7*labelHeight, labelWidth, labelHeight);
			label.setText(tableFirstRowNames[i]);
		}
		for (int i=0;i<tableFirstColumnNames.length;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset, yOffset+(i+1)*labelHeight, labelWidth, labelHeight);
			label.setText(tableFirstColumnNames[i]);
			label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset, 2*yOffset+(i+8)*labelHeight, labelWidth, labelHeight);
			label.setText(tableFirstColumnNames[i]);
		}
		
		ArrayList<Double> activePrices = getPriceChartY(activePriceOption);
		ArrayList<Double> closedPricesAdjusted = getPriceChartY(closedPriceOptionAdjusted);
		ArrayList<Double> closedPricesUnadjusted = getPriceChartY(closedPriceOptionUnadjusted);
		ArrayList<Double> closedDates = getPriceChartX(closedPriceOptionAdjusted);
	
		int rowNum = tableFirstColumnNames.length;
		Double max=0.0, min=0.0, median=0.0, mean=0.0, std=0.0;
		//compute statistics of active properties
		max = computeMax(activePrices);
		min = computeMin(activePrices);
		median = computeMedian(activePrices);
		mean = computeMean(activePrices);
		std = computeStd(activePrices);
		for (int i=0;i<rowNum;i++) {
			Label label1 = new Label(plotShell, SWT.NONE);
			Label label2 = new Label(plotShell, SWT.NONE);
			label1.setBounds(xOffset+labelWidth, yOffset+(i+1)*labelHeight, labelWidth, labelHeight);
			label2.setBounds(xOffset+labelWidth, 2*yOffset+(i+8)*labelHeight, labelWidth, labelHeight);
			if (i==0) {label1.setText(Formater.toCurrency(max));label2.setText(Formater.toCurrency(max));}
			else if (i==1) {label1.setText(Formater.toCurrency(min));label2.setText(Formater.toCurrency(min));}
			else if (i==2) {label1.setText(Formater.toCurrency(median));label2.setText(Formater.toCurrency(median));}
			else if (i==3) {label1.setText(Formater.toCurrency(mean));label2.setText(Formater.toCurrency(mean));}
			else if (i==4) {label1.setText(Formater.toCurrency(std));label2.setText(Formater.toCurrency(std));}
			else if (i==5) {label1.setText(String.valueOf(activePrices.size()));label2.setText(String.valueOf(activePrices.size()));}
		}
		
		ArrayList<Double> closedPrices3MonthsAdjusted = new ArrayList<Double>();
		ArrayList<Double> closedPrices6MonthsAdjusted = new ArrayList<Double>();
		ArrayList<Double> closedPrices9MonthsAdjusted = new ArrayList<Double>();
		ArrayList<Double> closedPrices3MonthsUnadjusted = new ArrayList<Double>();
		ArrayList<Double> closedPrices6MonthsUnadjusted = new ArrayList<Double>();
		ArrayList<Double> closedPrices9MonthsUnadjusted = new ArrayList<Double>();
		for (int i=0;i<closedDates.size();i++) {
			double daysFromNow = 365.0 - closedDates.get(i);
			if (daysFromNow < 365.0/4.0) {
				closedPrices3MonthsAdjusted.add(closedPricesAdjusted.get(i));
				closedPrices3MonthsUnadjusted.add(closedPricesUnadjusted.get(i));
			}
			if (daysFromNow < 365.0/2.0) {
				closedPrices6MonthsAdjusted.add(closedPricesAdjusted.get(i));
				closedPrices6MonthsUnadjusted.add(closedPricesUnadjusted.get(i));
			}
			if (daysFromNow < 3.0*365.0/4.0) {
				closedPrices9MonthsAdjusted.add(closedPricesAdjusted.get(i));
				closedPrices9MonthsUnadjusted.add(closedPricesUnadjusted.get(i));
			}
		}
		//compute statistics of properties closed within 3 months
		max = computeMax(closedPrices3MonthsAdjusted);
		min = computeMin(closedPrices3MonthsAdjusted);
		median = computeMedian(closedPrices3MonthsAdjusted);
		mean = computeMean(closedPrices3MonthsAdjusted);
		std = computeStd(closedPrices3MonthsAdjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+2*labelWidth, yOffset+(i+1)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPrices3MonthsAdjusted.size()));
		}
		//compute statistics of properties closed within 6 months
		max = computeMax(closedPrices6MonthsAdjusted);
		min = computeMin(closedPrices6MonthsAdjusted);
		median = computeMedian(closedPrices6MonthsAdjusted);
		mean = computeMean(closedPrices6MonthsAdjusted);
		std = computeStd(closedPrices6MonthsAdjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+3*labelWidth, yOffset+(i+1)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPrices6MonthsAdjusted.size()));
		}
		//compute statistics of properties closed within 9 months
		max = computeMax(closedPrices9MonthsAdjusted);
		min = computeMin(closedPrices9MonthsAdjusted);
		median = computeMedian(closedPrices9MonthsAdjusted);
		mean = computeMean(closedPrices9MonthsAdjusted);
		std = computeStd(closedPrices9MonthsAdjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+4*labelWidth, yOffset+(i+1)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPrices9MonthsAdjusted.size()));
		}
		//compute statistics of properties closed within 12 months
		max = computeMax(closedPricesAdjusted);
		min = computeMin(closedPricesAdjusted);
		median = computeMedian(closedPricesAdjusted);
		mean = computeMean(closedPricesAdjusted);
		std = computeStd(closedPricesAdjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+5*labelWidth, yOffset+(i+1)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPricesAdjusted.size()));			
		}
		
		//***********************Unadjusted************************************
		//compute statistics of properties closed within 3 months
		max = computeMax(closedPrices3MonthsUnadjusted);
		min = computeMin(closedPrices3MonthsUnadjusted);
		median = computeMedian(closedPrices3MonthsUnadjusted);
		mean = computeMean(closedPrices3MonthsUnadjusted);
		std = computeStd(closedPrices3MonthsUnadjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+2*labelWidth, 2*yOffset+(i+8)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPrices3MonthsUnadjusted.size()));
		}
		//compute statistics of properties closed within 6 months
		max = computeMax(closedPrices6MonthsUnadjusted);
		min = computeMin(closedPrices6MonthsUnadjusted);
		median = computeMedian(closedPrices6MonthsUnadjusted);
		mean = computeMean(closedPrices6MonthsUnadjusted);
		std = computeStd(closedPrices6MonthsUnadjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+3*labelWidth, 2*yOffset+(i+8)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPrices6MonthsUnadjusted.size()));
		}
		//compute statistics of properties closed within 9 months
		max = computeMax(closedPrices9MonthsUnadjusted);
		min = computeMin(closedPrices9MonthsUnadjusted);
		median = computeMedian(closedPrices9MonthsUnadjusted);
		mean = computeMean(closedPrices9MonthsUnadjusted);
		std = computeStd(closedPrices9MonthsUnadjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+4*labelWidth, 2*yOffset+(i+8)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPrices9MonthsUnadjusted.size()));
		}
		//compute statistics of properties closed within 12 months
		max = computeMax(closedPricesUnadjusted);
		min = computeMin(closedPricesUnadjusted);
		median = computeMedian(closedPricesUnadjusted);
		mean = computeMean(closedPricesUnadjusted);
		std = computeStd(closedPricesUnadjusted);
		for (int i=0;i<rowNum;i++) {
			Label label = new Label(plotShell, SWT.NONE);
			label.setBounds(xOffset+5*labelWidth, 2*yOffset+(i+8)*labelHeight, labelWidth, labelHeight);
			if (i==0) label.setText(Formater.toCurrency(max));
			else if (i==1) label.setText(Formater.toCurrency(min));
			else if (i==2) label.setText(Formater.toCurrency(median));
			else if (i==3) label.setText(Formater.toCurrency(mean));
			else if (i==4) label.setText(Formater.toCurrency(std));
			else if (i==5) label.setText(String.valueOf(closedPricesUnadjusted.size()));			
		}
	}
	
	private ArrayList<Double> getPriceChartSize(int option) {
		ArrayList<Double> size = new ArrayList<Double>();
		if (option == closedPriceOptionAdjusted) {
			for (int i=0;i<this.closedPropertyList.size();i++) {
				Double s = Double.valueOf(this.closedPropertyList.get(i).get("mlx_LA"));
				size.add(s);
			}
		} else if (option == activePriceOption) {		
			for (int i=0;i<this.activePropertyList.size();i++) {
				Double s = Double.valueOf(this.activePropertyList.get(i).get("mlx_LA"));
				size.add(s);
			}
		}
		return size;
	}
	
	private Double computeMax(ArrayList<Double> x) {
		int n=x.size();
		Double max = 0.0;
		if (n==0) return Double.NaN;
		else {
			for (int i=0;i<n;i++) {
				if (x.get(i)>max)
					max = x.get(i);
			}
			return max;
		}
	}
	
	private Double computeMin(ArrayList<Double> x) {
		int n=x.size();
		Double min = Double.MAX_VALUE;
		if (n==0) return Double.NaN;
		else {
			for (int i=0;i<n;i++) {
				if (x.get(i)<min) 
					min = x.get(i);
			}
			return min;
		}
	}
	
	private Double computeMedian(ArrayList<Double> x) {
		int n=x.size();
		Double median = 0.0;
		if (n==0) return Double.NaN;
		else {
			Collections.sort(x);
			median = x.get((int)Math.round((n-0.1)/2.0)); //deal with size==1
			return median;
		}
	}
	
	
	private Double computeStd(ArrayList<Double> x) {
		int n = x.size();
		Double std = 0.0;
		if (n==1 || n==0) {
			return Double.NaN;
		} else {
			Double mean = computeMean(x);
			double total = 0;
			for (int i=0;i<n;i++) {
				total = total + (x.get(i)-mean) * (x.get(i)-mean);
				
			}
			std = Math.sqrt(total/(n-1));
			return std;
		}
	}
	
	private Double computeMean(ArrayList<Double> x) {
		int n=x.size();
		Double mean = 0.0;
		if (n==0) return Double.NaN;
		else {
			for (int i=0;i<n;i++) {
				mean = mean+x.get(i);
			}
			mean = mean/n;
			return mean;
		}
	}
	
	
	private ArrayList<Double> getPriceChartX(int option) {
		ArrayList<Double> x = new ArrayList<Double>();
		Calendar now = Calendar.getInstance();
		
		if (option == closedPriceOptionAdjusted) {
			for (int i=0;i<this.closedPropertyList.size();i++) {
				try {
					Calendar closedDate = Calendar.getInstance();
					closedDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(this.closedPropertyList.get(i).get("mlx_CD")));
					long millisDiff = now.getTimeInMillis() - closedDate.getTimeInMillis();
					Double dayDiff = millisDiff/1000.0/3600.0/24.0;
					x.add(365.0-dayDiff);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (option == activePriceOption) {
			for (int i=0;i<this.activePropertyList.size();i++) {
				try {
					Calendar listDate = Calendar.getInstance();
					listDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(this.activePropertyList.get(i).get("mlx_LD")));
					long millisDiff = now.getTimeInMillis() - listDate.getTimeInMillis();
					Double dayDiff = millisDiff/1000.0/3600.0/24.0;
					x.add(365.0-dayDiff);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return x;
	}
	
	private ArrayList<Double> getPriceChartY(int option) {
		ArrayList<Double> y = new ArrayList<Double>();
		Index indexReader = new Index();
		indexReader.readIndex(Index.defaultCSIndexFileName);
		Calendar now = Calendar.getInstance();
		Double todayIndex = indexReader.getIndex(now);
		
		if (option==closedPriceOptionAdjusted) {
			for (int i=0;i<this.closedPropertyList.size();i++) {
				try {
					Double price = Double.valueOf(this.closedPropertyList.get(i).get("mlx_SPdollar"));
					//System.out.print("Closed Price: " +price+"---");
					Calendar closedDate = Calendar.getInstance();
					closedDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(this.closedPropertyList.get(i).get("mlx_CD")));
					
					Double closedIndex = indexReader.getIndex(closedDate);
					//System.out.print("Closed Month: " + (closedDate.get(Calendar.MONTH)+1) +"---"+"Closed Index: "+closedIndex);
					price = price*(todayIndex/closedIndex);
					//System.out.println("Adjusted Price: " + price);
					
					y.add(price);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (option==closedPriceOptionUnadjusted) {
			for (int i=0;i<this.closedPropertyList.size();i++) {
					Double price = Double.valueOf(this.closedPropertyList.get(i).get("mlx_SPdollar"));
					y.add(price);
			}
		}
		else if (option==activePriceOption) {
			for (int i=0;i<this.activePropertyList.size();i++) {
				Double price = Double.valueOf(this.activePropertyList.get(i).get("mlx_LPdollar"));
				y.add(price);
			}
		}
		return y;
	}
	
	public void setClosedPriceData(ArrayList<HashMap<String, String> > propertyList) throws Exception {
		this.closedPropertyList = new ArrayList<HashMap<String,String>>();
		for (int i=0;i<propertyList.size();i++) {
			HashMap<String, String> newProperty = new HashMap<String, String>();
			HashMap<String, String> property = propertyList.get(i);
			String spDollar = property.get("mlx_SPdollar");
			spDollar = spDollar.replace(",", "");
			
			String size = property.get("mlx_LA");
			size = size.replace(",", "");
					
			newProperty.put("mlx_SPdollar", spDollar);
			newProperty.put("mlx_CD", property.get("mlx_CD"));
			newProperty.put("mlx_LA", size);
			
			this.closedPropertyList.add(newProperty);
		}
	}
	
	public void setActivePriceData(ArrayList<HashMap<String, String> > propertyList) throws Exception {
		this.activePropertyList = new ArrayList<HashMap<String,String>>();
		for (int i=0;i<propertyList.size();i++) {
			HashMap<String, String> newProperty = new HashMap<String, String>();
			HashMap<String, String> property = propertyList.get(i);
			String lpDollar = property.get("mlx_LPdollar");
			lpDollar = lpDollar.replace(",", "");
			
			String size = property.get("mlx_LA");
			size = size.replace(",", "");
					
			newProperty.put("mlx_LPdollar", lpDollar);
			newProperty.put("mlx_LA", size);
			
			this.activePropertyList.add(newProperty);
		}
	}
	
	
}
