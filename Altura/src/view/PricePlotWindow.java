package view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import chart.ScatterARC;

public class PricePlotWindow {

	private ArrayList<HashMap<String, String>> propertyList;
	protected Shell shell;

	private static int closedPriceOption = 1;
	private static int activePriceOption = 2;
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void createContents() {
		int graphWidth=400;
		int graphHeight=500;
		int statsHeight=200;
		int statsWidth=graphWidth*2;
		
		shell = new Shell(SWT.CLOSE);
		shell.setSize(graphWidth*2, graphHeight+statsHeight);
		shell.setText("Graph");

		Composite compositeClosedPrice = new Composite(shell, SWT.NONE);
		Composite compositeActivePrice = new Composite(shell, SWT.NONE);
		ScatterARC scatterClosedChart = new ScatterARC();
		ScatterARC scatterActiveChart = new ScatterARC(); 
		
		ArrayList<Double> closedPointsX = getPriceChartX(closedPriceOption);
		ArrayList<Double> closedPointsY = getPriceChartY(closedPriceOption);
		ArrayList<Double> closedPointsSize = getPriceChartSize();
		
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
		for (int i=0;i<closedPointsX.size();i++) {
			//System.out.println("old: " + "X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
			closedPointsX.set(i, closedPointsX.get(i)/365*100.0+0);
			closedPointsY.set(i, closedPointsY.get(i)/yMax*60+20);
			closedPointsSize.set(i, closedPointsSize.get(i)/sizeMax*30+10);
			//System.out.println("new: " + "X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
		}
		scatterClosedChart.setxAxisMin(-365.0);
		scatterClosedChart.setxAxisMax(0.0);
		if (yMax > yMin) {
			scatterClosedChart.setyAxisMin(yMin-(yMax-yMin)/3.0);
			scatterClosedChart.setyAxisMax(yMax+(yMax-yMin)/3.0);
		}
		else {
			scatterClosedChart.setyAxisMin(0.8*yMin);
			scatterClosedChart.setyAxisMax(1.2*yMax);
		}
		scatterClosedChart.setPointsX(closedPointsX);
		scatterClosedChart.setPointsY(closedPointsY);
		scatterClosedChart.setPointsSize(closedPointsSize);
		scatterClosedChart.setLegend("Close Price");
		scatterClosedChart.setChartWidth(graphWidth);
		scatterClosedChart.setChartHeight(graphHeight);
		scatterClosedChart.setChartTitle("Close Price");
		String imageURL = scatterClosedChart.getChartUrlString();
		//set Image
		URL url;
		try {
			url = new URL(imageURL);
			Display imgDisplay = Display.getDefault();
			Image image = new Image(imgDisplay, url.openStream());
//			System.out.println(url);
			compositeClosedPrice.setBounds(0, 0, image.getImageData().width, image.getImageData().height);
			compositeClosedPrice.setBackgroundImage(image);
			compositeActivePrice.setBounds(0, 0, image.getImageData().width, image.getImageData().height);
			compositeActivePrice.setBackgroundImage(image);
			
			//shell.pack();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<Double> getPriceChartSize() {
		ArrayList<Double> size = new ArrayList<Double>();
		for (int i=0;i<this.propertyList.size();i++) {
			Double s = Double.valueOf(this.propertyList.get(i).get("mlx_LA"));
			size.add(s);
		}
		return size;
	}
	
	private ArrayList<Double> getPriceChartX(int option) {
		ArrayList<Double> x = new ArrayList<Double>();
		Calendar now = Calendar.getInstance();
		
		if (option == closedPriceOption) {
			for (int i=0;i<this.propertyList.size();i++) {
				try {
					Calendar closedDate = Calendar.getInstance();
					closedDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(this.propertyList.get(i).get("mlx_CD")));
					long millisDiff = now.getTimeInMillis() - closedDate.getTimeInMillis();
					Double dayDiff = millisDiff/1000.0/3600.0/24.0;
					x.add(365.0-dayDiff);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (option == activePriceOption) {
			for (int i=0;i<this.propertyList.size();i++) {
				try {
					Calendar listDate = Calendar.getInstance();
					listDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(this.propertyList.get(i).get("mlx_LD")));
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
		if (option==closedPriceOption) {
			for (int i=0;i<this.propertyList.size();i++) {
				Double price = Double.valueOf(this.propertyList.get(i).get("mlx_SPdollar"));
				y.add(price);
			}
		}
		else if (option==activePriceOption) {
			for (int i=0;i<this.propertyList.size();i++) {
				Double price = Double.valueOf(this.propertyList.get(i).get("mlx_LPdollar"));
				y.add(price);
			}
		}
		return y;
	}
	
	public void setPriceData(ArrayList<HashMap<String, String> > propertyList) throws Exception {
		this.propertyList = new ArrayList<HashMap<String,String>>();
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
			
			this.propertyList.add(newProperty);
		}
	}
}
