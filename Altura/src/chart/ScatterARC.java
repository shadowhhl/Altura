package chart;

import java.util.ArrayList;

import model.Formater;

import com.googlecode.charts4j.*;
import static com.googlecode.charts4j.Color.WHITE;

public class ScatterARC extends Chart {
	private ArrayList< Double > closedPointsX;
	private ArrayList< Double > closedPointsY;
	private ArrayList< Double > activePointsX;
	private ArrayList< Double > activePointsY;
	private ArrayList<Double> closedPointsSize;
	private ArrayList<Double> activePointsSize;
	private String legend;
	private Double xAxisMax;
	private Double xAxisMin;
	private Double yAxisMax;
	private Double yAxisMin;
	
	public ScatterARC() {
	}
	
	public String getChartUrlString() {
		int closedCount = closedPointsX.size();
		int activeCount = activePointsX.size();
		ArrayList<Double> allPointsX = new ArrayList<Double>();
		ArrayList<Double> allPointsY = new ArrayList<Double>();
		ArrayList<Double> allPointsSize = new ArrayList<Double>();
		for (int i=0;i<closedCount+activeCount;i++) {
			if (i<closedCount) {
				allPointsX.add(closedPointsX.get(i));
				allPointsY.add(closedPointsY.get(i));
				allPointsSize.add(closedPointsSize.get(i));
			} else {
				allPointsX.add(activePointsX.get(i-closedCount));
				allPointsY.add(activePointsY.get(i-closedCount));
				allPointsSize.add(activePointsSize.get(i-closedCount));
			}
		}
		Data xData = Data.newData(allPointsX);
		Data yData = Data.newData(allPointsY);
		Data pointsSizeData = Data.newData(allPointsSize);
		
		ScatterPlotData data = Plots.newScatterPlotData(xData, yData, pointsSizeData);
		data.setLegend(legend);
		Color closedDataColor = Color.newColor("000080");
		Color activeDataColor = Color.newColor("FF471A");
		
		
		for (int i=0;i<closedCount+activeCount;i++) {
			if (i<closedCount) {
				data.addShapeMarker(Shape.SQUARE, closedDataColor, allPointsSize.get(i).intValue(), i);
				
			} else {
				data.addShapeMarker(Shape.SQUARE, activeDataColor, allPointsSize.get(i).intValue(), i);
			}
		}
		data.setColor(closedDataColor);
		ScatterPlot scatterPlotChart = GCharts.newScatterPlot(data);
		scatterPlotChart.setDataEncoding(DataEncoding.SIMPLE);
		//System.out.println(chartWidth +" "+chartHeight);
		scatterPlotChart.setSize(chartWidth, chartWidth);
		
		scatterPlotChart.setTitle(chartTitle, WHITE, 13);
		
		//AxisLabels xAxisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(xAxisMin, xAxisMax);
		//AxisLabels yAxisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(yAxisMin, yAxisMax);
		Double yAxisMinRounded = roundTo1K(yAxisMin);
		Double yAxisMaxRounded = roundTo1K(yAxisMax);
		ArrayList<String> yLabels = new ArrayList<String>();
		Double cur = yAxisMinRounded;
		Double step = (yAxisMaxRounded - yAxisMinRounded)/10;
		if (step!=0) {
			while (true) {
				yLabels.add(Formater.toCurrency(cur));
				cur=roundTo1K(cur+step);
				if (cur>yAxisMaxRounded) {
					break;
				}
			}
		} else {
			yLabels.add("");
		}
		AxisLabels yAxisLabels = AxisLabelsFactory.newAxisLabels(yLabels);
		
		ArrayList<String> xLabels = new ArrayList<String>();
		ArrayList<Double> xPositions = new ArrayList<Double>(); 
		xLabels.add("12 M ago");xPositions.add(10.0);
		xLabels.add("9 M ago");xPositions.add(35.0);
		xLabels.add("6 M ago");xPositions.add(60.0);
		xLabels.add("3 M ago");xPositions.add(85.0);
		xLabels.add("Now");xPositions.add(100.0);
		AxisLabels xAxisLabels = AxisLabelsFactory.newAxisLabels(xLabels, xPositions);
		
		xAxisLabels.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 13, AxisTextAlignment.LEFT));
		yAxisLabels.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 13, AxisTextAlignment.CENTER));

		scatterPlotChart.addXAxisLabels(xAxisLabels);
		scatterPlotChart.addYAxisLabels(yAxisLabels);

		scatterPlotChart.setBackgroundFill(Fills.newSolidFill(Color.newColor("2F3E3E")));
		Fill fill = Fills.newSolidFill(Color.newColor("9F9F9F"));
        scatterPlotChart.setAreaFill(fill);
        String url = scatterPlotChart.toURLString();
        
        return url;
	}

	private Double roundTo1K(Double x) {
		Double xInK = x/1000;
		double xInK2 = xInK.doubleValue();
		int newX = (int)Math.round(xInK2);
		return Double.valueOf(newX*1000);
	}
	
	public void setClosedPointsX(ArrayList<Double> closedPointsX) {
		this.closedPointsX = closedPointsX;
	}

	public void setClosedPointsY(ArrayList<Double> closedPointsY) {
		this.closedPointsY = closedPointsY;
	}

	public void setActivePointsX(ArrayList<Double> activePointsX) {
		this.activePointsX = activePointsX;
	}

	public void setActivePointsY(ArrayList<Double> activePointsY) {
		this.activePointsY = activePointsY;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public Double getxAxisMax() {
		return xAxisMax;
	}

	public void setxAxisMax(Double xAxisMax) {
		this.xAxisMax = xAxisMax;
	}

	public Double getxAxisMin() {
		return xAxisMin;
	}

	public void setxAxisMin(Double xAxisMin) {
		this.xAxisMin = xAxisMin;
	}

	public Double getyAxisMax() {
		return yAxisMax;
	}

	public void setyAxisMax(Double yAxisMax) {
		this.yAxisMax = yAxisMax;
	}

	public Double getyAxisMin() {
		return yAxisMin;
	}

	public void setyAxisMin(Double yAxisMin) {
		this.yAxisMin = yAxisMin;
	}

	public void setClosedPointsSize(ArrayList<Double> closedPointsSize) {
		this.closedPointsSize = closedPointsSize;
	}

	public void setActivePointsSize(ArrayList<Double> activePointsSize) {
		this.activePointsSize = activePointsSize;
	}
}
