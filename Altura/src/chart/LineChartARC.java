package chart;

import java.util.ArrayList;

import com.googlecode.charts4j.GCharts;

import static com.googlecode.charts4j.Color.*;
import com.googlecode.charts4j.*;

public class LineChartARC extends Chart {
	private ArrayList< ArrayList<Double> > lineData;
	private ArrayList<String> lineTitles;
	
	public String getChartUrlString() {
		
		int numLines = lineData.size();
		
		ArrayList<Line> lines = new ArrayList<Line>();
		Double minNum = Double.MAX_VALUE;
		Double maxNum = Double.MIN_VALUE;
		for (int i=0;i<numLines;i++) {
			ArrayList<Double> data = lineData.get(i);
			for (int j=0;j<data.size();j++) {
				if (data.get(j) > maxNum) maxNum = data.get(j);
				if (data.get(j) < minNum) minNum = data.get(j);
			}
		}
		
		for (int i=0;i<numLines;i++) {
			Line line = Plots.newLine(DataUtil.scaleWithinRange(minNum,maxNum,lineData.get(i)), YELLOW, lineTitles.get(i));
			line.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
	        line.addShapeMarkers(Shape.CIRCLE, YELLOW, 10);
	        line.addShapeMarkers(Shape.CIRCLE, BLACK, 7);
	        line.setFillAreaColor(LIGHTYELLOW);
			lines.add(line);
		}
        // Defining chart.
        LineChart chart = GCharts.newLineChart(lines);
        chart.setSize(chartWidth, chartHeight);
        chart.setTitle(chartTitle, WHITE, 14);

        // Defining axis info and styles
        

        // Adding axis info to chart.

        chart.setGrid(100, 6.78, 5, 0);

        // Defining background and chart fills.
        chart.setBackgroundFill(Fills.newSolidFill(BLACK));
        chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
        String url = chart.toURLString();
        
        return url;
	}

	public void setLineTitles(ArrayList<String> lineTitles) {
		this.lineTitles = lineTitles;
	}
	
	public ArrayList<ArrayList<Double>> getLineData() {
		return lineData;
	}
	public void setLineData(ArrayList< ArrayList<Double> > lineData) {
		this.lineData = lineData;
	}

}
