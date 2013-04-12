package chart;

import java.util.ArrayList;

import com.googlecode.charts4j.*;
import static com.googlecode.charts4j.Color.WHITE;

public class ScatterARC extends Chart {
	private ArrayList< Double > pointsX;
	private ArrayList< Double > pointsY;
	private ArrayList<Double> pointsSize;
	private String legend;
	private Double xAxisMax;
	private Double xAxisMin;
	private Double yAxisMax;
	private Double yAxisMin;
	
	public ScatterARC() {
	}
	
	public String getChartUrlString() {
		Data xData = Data.newData(pointsX);
		Data yData = Data.newData(pointsY);
		Data pointsSizeData = Data.newData(pointsSize);
		ScatterPlotData data = Plots.newScatterPlotData(xData, yData, pointsSizeData);
		data.setLegend(legend);
		Color dataColor = Color.newColor("FF471A");
		data.addShapeMarkers(Shape.DIAMOND, dataColor, 30);
		data.setColor(dataColor);
		ScatterPlot scatterPlotChart = GCharts.newScatterPlot(data);
		scatterPlotChart.setSize(chartWidth, chartWidth);
		
		scatterPlotChart.setTitle(chartTitle);
		
		//AxisLabels xAxisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(xAxisMin, xAxisMax);
		AxisLabels yAxisLabels = AxisLabelsFactory.newNumericRangeAxisLabels(yAxisMin, yAxisMax);
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("12 Months ago");
		labels.add("9 Months ago");
		labels.add("6 Months ago");
		labels.add("3 Months ago");
		labels.add("Now");
		AxisLabels xAxisLabels = AxisLabelsFactory.newAxisLabels(labels);
		xAxisLabels.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 13, AxisTextAlignment.CENTER));
		yAxisLabels.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 13, AxisTextAlignment.CENTER));

		scatterPlotChart.addXAxisLabels(xAxisLabels);
		scatterPlotChart.addYAxisLabels(yAxisLabels);

		scatterPlotChart.setBackgroundFill(Fills.newSolidFill(Color.newColor("2F3E3E")));
        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("3783DB"), 100);
        fill.addColorAndOffset(Color.newColor("9BD8F5"), 0);
        scatterPlotChart.setAreaFill(fill);
        String url = scatterPlotChart.toURLString();
        
        return url;
	}

	public ArrayList<Double> getPointsX() {
		return pointsX;
	}

	public void setPointsX(ArrayList<Double> pointsX) {
		this.pointsX = pointsX;
	}

	public ArrayList<Double> getPointsY() {
		return pointsY;
	}

	public void setPointsY(ArrayList<Double> pointsY) {
		this.pointsY = pointsY;
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

	public void setPointsSize(ArrayList<Double> pointsSize) {
		this.pointsSize = pointsSize;
	}

}
