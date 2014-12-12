package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class AnaMain {
	public static final int ANA_BEGIN = 601314;
	public static final int ANA_END = 602000;
	public static final int DATE_INDEX = 0;
	public static final int END_PRICE_INDEX = 4;
	
	public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = ANA_BEGIN; i < ANA_END; i++) {
			try {
				List<Double> prices = new ArrayList<Double>();
				List<Date> dates = new ArrayList<Date>();
				CSVReader csvReader = new CSVReader(new FileReader(new File("d:\\stock\\" + i + ".csv")), ',');
				try {
					String[] line = csvReader.readNext();
					while ((line = csvReader.readNext()) != null) {
						dates.add(df.parse(line[DATE_INDEX]));
						prices.add(Double.valueOf(line[END_PRICE_INDEX]));
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println("No file for " + i);
			}
		}
	}
}
