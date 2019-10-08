import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * This program asks the user for an input to know how many terms of the
 * Fibonacci sequence it is going to run. The program runs two Fibonacci
 * function methods, one iterative and the other recursive. The program will the
 * print out the Fibonacci sequence and then its term number and the speed in
 * nanoseconds in which it was assembled. After this the program displays a line
 * chart that shows time (ns) vs term number of both methods. Then the program
 * saves the line chart as PNG file.
 *
 */

@SuppressWarnings("serial")
public class FibonacciRuntimeTest extends JFrame {

	/**
	 * This is a recursive Fibonacci method which takes in a term number and returns
	 * the Fibonacci sequence of that term number.
	 * 
	 * @param number of the Fibonacci sequence term
	 * @return sequence of Fibonacci numbers
	 */

	public int recursiveFibonacci(int number) {
		int sequence = 0;
		if (number <= 1) {
			sequence = number;
			return sequence;
		}
		return recursiveFibonacci(number - 1) + recursiveFibonacci(number - 2);
	}

	/**
	 * This is the iterative Fibonacci method which takes in a term number and
	 * returns the Fibonacci sequence of that term number.
	 * 
	 * @param number of the Fibonacci sequence term
	 * @return sequence of Fibonacci numbers
	 */

	public int iterativeFibonacci(int number) {
		int sequence = 0;
		if (number <= 1) {
			sequence = number;
			return sequence;
		}

		int prevPrevNumber = 0;
		int prevNumber = 1;

		for (int i = 2; i <= number; i++) {
			sequence = prevNumber + prevPrevNumber;
			prevPrevNumber = prevNumber;
			prevNumber = sequence;
		}
		return sequence;
	}

	/**
	 * This is the constructor of the class and it used to call the createChartPanel
	 * method to begin creating the line chart. Here we set the window name for the
	 * JPanel and the size of it.
	 */
	public FibonacciRuntimeTest() {
		super("Fibonacci Runtime Test");

		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);

		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/**
	 * This method is used to input the characteristics of the line chart. Here we
	 * set backgrounds, colors and the legends. Apart from this, we have the call to
	 * the method createDataset to create the charts data sets. A second thing this
	 * method does is that it runs the code to create and export a PNG file of the
	 * line chart and it is surrounded by a try and catch block.
	 * 
	 * @return ChartPanel with all the characteristics set by the user.
	 */
	public JPanel createChartPanel() {
		String chartTitle = "Iterative vs Recursive Fibonacci Runtime";
		String xAxisLabel = "Input (n)";
		String yAxisLabel = "Time (ns)";

		XYDataset dataset = createDataset();

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// sets paint color for each series
		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);

		// sets thickness for series (using strokes)
		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
		plot.setRenderer(renderer);

		plot.setOutlinePaint(Color.BLUE);
		plot.setOutlineStroke(new BasicStroke(2.0f));
		plot.setBackgroundPaint(Color.DARK_GRAY);

		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);

		// Creates a PNG photo file of the chart and exports it
		File imageFile = new File("Fibonacci_runtime_efficiency_chart.png");
		int width = 640;
		int height = 480;
		try {
			ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
		} catch (IOException ex) {
			System.err.println(ex);
		}

		return new ChartPanel(chart);
	}

	/**
	 * This method creates the datasets for the line chart. Here we call the user to
	 * input the Nth term number for which the Fibonacci methods will run for. This
	 * method calls both Fibonacci methods, records the time (in nanoseconds) it
	 * takes for the method to run and records into a data set to be inputted into
	 * the line chart.
	 * 
	 * @return dataset of points which is going to be inputted into the line chart
	 */

	public XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Iterative Function");
		XYSeries series2 = new XYSeries("Recursive Function");

		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the number of terms of the Fibonacci sequence: ");
		int i = input.nextInt();
		int number = 0;
		long startTime = 0;
		long endTime = 0;

		System.out.println("\nIterative version:\n");
		while (i >= number) {
			startTime = System.nanoTime();
			System.out.println("Fibonacci Sequence: " + iterativeFibonacci(number));
			endTime = System.nanoTime();
			series1.add(number, endTime - startTime);
			System.out.println("(Term Number (n): " + number + ", Speed: " + (endTime - startTime) + " ns)");
			number = number + 1;
		}

		number = 0;
		System.out.println("\nRecursive version:\n");
		while (i >= number) {
			startTime = System.nanoTime();
			System.out.println("Fibonacci Sequence: " + recursiveFibonacci(number));
			endTime = System.nanoTime();
			series2.add(number, endTime - startTime);
			System.out.println("(Term Number (n): " + number + ", Speed: " + (endTime - startTime) + " ns)");
			number = number + 1;
		}

		dataset.addSeries(series1);
		dataset.addSeries(series2);

		return dataset;
	}

	/**
	 * Main method in which we call the constructor method to start the process for
	 * creating the line chart.
	 * 
	 * @param args an array of command-line arguments for the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FibonacciRuntimeTest().setVisible(true);
			}
		});
	}

}
