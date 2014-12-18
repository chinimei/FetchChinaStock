package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class SmoothMain {
	public static final int ANA_BEGIN = 600000;
	public static final int ANA_END = 602001;
	public static final int DATE_INDEX = 0;
	public static final int END_PRICE_INDEX = 4;

	private void smooth1(List<Double> data) {
		for (int i = 1; i < data.size() - 2; i++) {
			double value1 = data.get(i - 1);
			double value2 = data.get(i);
			double value3 = data.get(i + 1);
			double smoothValue = (value1 + value2 + value3) / 3d;
			data.set(i, smoothValue);
		}
	}

	private void smooth2(List<Double> data) {
		for (int i = 2; i < data.size() - 3; i++) {
			int g = i - 2, h = i - 1, j = i + 1, k = i + 2;
			double value1 = g >= 0 ? data.get(g) : 0d;
			double value2 = h >= 0 ? data.get(h) : 0d;
			double value3 = data.get(i);
			double value4 = j < data.size() ? data.get(j) : 0d;
			double value5 = k < data.size() ? data.get(k) : 0d;
			double smoothValue = (value1 + value2 * 2d + value3 * 3d + value4 * 2d + value5) / 9d;
			data.set(i, smoothValue);
		}
	}

	public static void main(String[] args) {
		SmoothMain sm = new SmoothMain();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = ANA_BEGIN; i < ANA_END; i++) {
			try {
				List<Double> prices = new ArrayList<Double>();
				List<Date> dates = new ArrayList<Date>();
				CSVReader csvReader = new CSVReader(new FileReader(new File("d:\\stock\\origin\\" + i + ".csv")), ',');
				CSVWriter writer = null;
				try {
					String[] line = csvReader.readNext();
					while ((line = csvReader.readNext()) != null) {
						dates.add(df.parse(line[DATE_INDEX]));
						prices.add(Double.valueOf(line[END_PRICE_INDEX]));
					}
					if (prices.size() > 0) {
//						sm.smooth1(prices);
//						writer = new CSVWriter(new FileWriter(new File("d:\\stock\\smooth1\\" + i + ".csv")), ',');
//						for (int j = 0; j < dates.size(); j++) {
//							writer.writeNext(new String[] { df.format(dates.get(j)), prices.get(j).toString() });
//						}
						sm.smooth2(prices);
						writer = new CSVWriter(new FileWriter(new File("d:\\stock\\smooth2\\" + i + ".csv")), ',');
						for (int j = 0; j < dates.size(); j++) {
							writer.writeNext(new String[] { df.format(dates.get(j)), prices.get(j).toString() });
						}
//						sm.smooth2(prices);
//						sm.smooth2(prices);
//						writer = new CSVWriter(new FileWriter(new File("d:\\stock\\smooth3\\" + i + ".csv")), ',');
//						for (int j = 0; j < dates.size(); j++) {
//							writer.writeNext(new String[] { df.format(dates.get(j)), prices.get(j).toString() });
//						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} finally {
					if (csvReader != null) {
						try {
							csvReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("No file for " + i);
			}
		}
	}

}
