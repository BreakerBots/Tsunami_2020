package frc.team5104.util;

import java.util.ArrayList;
import java.util.List;

public class Plotter {

	private static List<PlotterPoint> buffer = new ArrayList<PlotterPoint>();
	
	public static void plotAll(PlotterPoint[] points) {
		for (PlotterPoint point : points)
			plot(point);
	}
	
	public static void reset() {
		buffer.add(new PlotterPoint(true));
	}
	
	public static void plot(double x, double y, PlotterPoint.Color color) {
		plot(new PlotterPoint(x, y, color));
	}
	
	public static void plot(PlotterPoint point) {
		buffer.add(point);
	}
	
	protected static String getBufferDataAsJSON() {
		String data = "[";
		
		for (PlotterPoint point : buffer) {
			data += point.toString() + ",";
		}
		
		if (data.charAt(data.length()-1) == ',')
			data = data.substring(0, data.length() - 1);
		
		return data + "]";
	}
	
	protected static void clearBuffer() {
		buffer.clear();
	}
	
	public static class PlotterPoint {
		public static enum Color { RED, BLUE, GREEN, PURPLE, ORANGE, BLACK };
		private double x, y;
		private Color color;
		private boolean isReset;
		
		PlotterPoint(boolean isReset) {
			this.isReset = isReset;
		}
		
		public PlotterPoint(double x, double y, Color color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
		
		public String toString() {
			if (isReset)
				return "{\"color\":\"RESET\"}";
			return "{\"x\":" + x + ",\"y\":" + y + ",\"color\":\"" + color.name() + "\"}";
		}
	}
}