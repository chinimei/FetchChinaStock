package data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

public class FetchMain {

	public static final int STOCK_COUNT_BEGIN = 600318;
	public static final int STOCK_COUNT_TOTAL = 602000;
	public static final int BUFFER_SIZE = 8192;

	public static void main(String[] args) throws IOException, InterruptedException {
		for (int i = STOCK_COUNT_BEGIN; i < STOCK_COUNT_TOTAL; i++) {
			URL url = new URL("http://table.finance.yahoo.com/table.csv?s=" + i + ".ss");
			URLConnection con = url.openConnection();
			con.setConnectTimeout(1000000);
			con.setReadTimeout(2000000);
			BufferedInputStream is = null;
			BufferedOutputStream os = null;
			try {
				is = new BufferedInputStream(con.getInputStream());
				os = new BufferedOutputStream(new FileOutputStream("d:\\stock\\" + i + ".csv"), BUFFER_SIZE);
				System.out.println("begin download " + i);
				final byte data[] = new byte[BUFFER_SIZE];
				int count;
				while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
					os.write(data, 0, count);
				}
				System.out.println(i + " download complete!");
			} catch (FileNotFoundException fnf) {
				System.out.println(i + " is not found!");
				continue;
			} catch (ConnectException cone) {
				System.out.println("Connection timeout! Wait 1 min and start again.");
				i--; 
				Thread.sleep(60000);
				continue;
			} finally {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				Thread.sleep(30000);
			}
		}
	}
}
