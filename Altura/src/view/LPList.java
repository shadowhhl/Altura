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

public class LPList {

	private ArrayList<HashMap<String, String>> propertyList;
	protected Shell shell;

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
		shell = new Shell(SWT.CLOSE);
		shell.setSize(450, 300);
		shell.setText("Graph");

		Composite composite = new Composite(shell, SWT.NONE);
		ScatterARC scatterChart = new ScatterARC();
		ArrayList<Double> pointsX = getListPriceChartX();
		ArrayList<Double> pointsY = getListPriceChartY();
		ArrayList<Double> pointsSize = getListPriceChartSize();
		
		//Do scaling
		Double xMax = Double.MIN_VALUE;
		Double xMin = Double.MAX_VALUE;
		Double yMax = Double.MIN_VALUE;
		Double yMin = Double.MAX_VALUE;
		Double sizeMax = Double.MIN_VALUE;
		Double sizeMin = Double.MAX_VALUE;
		for (int i=0;i<pointsX.size();i++) {
			//System.out.println("X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
			if (pointsX.get(i)>xMax) xMax = pointsX.get(i);
			if (pointsX.get(i)<xMin) xMin = pointsX.get(i);
			if (pointsY.get(i)>yMax) yMax = pointsY.get(i);
			if (pointsY.get(i)<yMin) yMin = pointsY.get(i);
			if (pointsSize.get(i)>sizeMax) sizeMax = pointsSize.get(i);
			if (pointsSize.get(i)<sizeMin) sizeMin = pointsSize.get(i);
		}
		for (int i=0;i<pointsX.size();i++) {
			//System.out.println("old: " + "X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
			pointsX.set(i, pointsX.get(i)/365*100.0+0);
			pointsY.set(i, pointsY.get(i)/yMax*60+20);
			pointsSize.set(i, pointsSize.get(i)/sizeMax*30+10);
			//System.out.println("new: " + "X: "+pointsX.get(i)+" Y: "+pointsY.get(i));
		}
		scatterChart.setxAxisMin(-365.0);
		scatterChart.setxAxisMax(0.0);
		if (yMax > yMin) {
			scatterChart.setyAxisMin(yMin-(yMax-yMin)/3.0);
			scatterChart.setyAxisMax(yMax+(yMax-yMin)/3.0);
		}
		else {
			scatterChart.setyAxisMin(0.8*yMin);
			scatterChart.setyAxisMax(1.2*yMax);
		}
		scatterChart.setPointsX(pointsX);
		scatterChart.setPointsY(pointsY);
		scatterChart.setPointsSize(pointsSize);
		scatterChart.setLegend("List Price");
		scatterChart.setChartWidth(500);
		scatterChart.setChartHeight(300);
		scatterChart.setChartTitle("List Price");
		String imageURL = scatterChart.getChartUrlString();
		//set Image
		URL url;
		try {
			url = new URL(imageURL);
			Display imgDisplay = Display.getDefault();
			Image image = new Image(imgDisplay, url.openStream());
			System.out.println(url);
			composite.setBounds(0, 0, image.getImageData().width, image.getImageData().height);
			composite.setBackgroundImage(image);
			shell.pack();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<Double> getListPriceChartSize() {
		ArrayList<Double> size = new ArrayList<Double>();
		for (int i=0;i<this.propertyList.size();i++) {
			Double s = Double.valueOf(this.propertyList.get(i).get("mlx_LA"));
			size.add(s);
		}
		return size;
	}
	
	private ArrayList<Double> getListPriceChartX() {
		ArrayList<Double> x = new ArrayList<Double>();
		Calendar now = Calendar.getInstance();
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
		
		return x;
	}
	
	private ArrayList<Double> getListPriceChartY() {
		ArrayList<Double> y = new ArrayList<Double>();
		for (int i=0;i<this.propertyList.size();i++) {
			Double price = Double.valueOf(this.propertyList.get(i).get("mlx_LPdollar"));
			y.add(price);
		}
		return y;
	}
	
	public void setListPriceData(ArrayList<HashMap<String, String> > propertyList) throws Exception {
		this.propertyList = new ArrayList<HashMap<String,String>>();
		for (int i=0;i<propertyList.size();i++) {
			HashMap<String, String> newProperty = new HashMap<String, String>();
			HashMap<String, String> property = propertyList.get(i);
			String lpDollar = property.get("mlx_LPdollar");
			lpDollar = lpDollar.replace(",", "");
			
			String size = property.get("mlx_LA");
			size = size.replace(",", "");
					
//			Calendar listDate = Calendar.getInstance();
//			String ld = property.get("mlx_LD");
//			listDate.setTime(new SimpleDateFormat("M/d/yyyy").parse(ld));
//			
			newProperty.put("mlx_LPdollar", lpDollar);
			newProperty.put("mlx_LD", property.get("mlx_LD"));
			newProperty.put("mlx_LA", size);
			
			this.propertyList.add(newProperty);
		}
		System.out.println(this.propertyList);
	}
}
