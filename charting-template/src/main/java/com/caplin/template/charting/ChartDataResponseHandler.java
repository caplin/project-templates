package com.caplin.template.charting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import com.caplin.charting.ChartDataRequest;
import com.caplin.charting.OhlcvData;

/**
 * Does the work of generating random data and sending it.
 */
public class ChartDataResponseHandler implements Runnable
{
	private final ChartDataRequest request;

	public ChartDataResponseHandler( ChartDataRequest request )
	{
		this.request = request;
	}

	@Override
	public void run()
	{
		try
		{
			// Pause for 5 seconds as a way of simulating
			// the time it would take to retrieve the data.
			Thread.sleep(5);
			// Call the dataReceived call back with some
			// fake generated data.
			this.request.sendDataResponse("SeriesName", createOHLCVData());
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Generates some fake OHLCV data for the example datasource to respond with.
	 * 
	 * @return a list of randomly generated generated OhlcvData objects.
	 */
	private ArrayList<OhlcvData> createOHLCVData()
	{
		ArrayList<OhlcvData> histoData = new ArrayList<OhlcvData>();
		int numberOfMessages = 20;
		long startTime = 1306657800000l;
		double lastclose = 0.6754d;
		long timeIncrementInMillis = 60 * 1000;
		Calendar cal = Calendar.getInstance();
		Random rnd = new Random();
		for (int i = 0; i < numberOfMessages; i++)
		{
			long millis = startTime + i * timeIncrementInMillis;
			double open = lastclose + rnd.nextDouble() / 10000d;
			double close = open + rnd.nextDouble() / 10000d - 0.00005d;
			double high = Math.max(open, close) + rnd.nextDouble() / 10000d;
			double low = Math.min(open, close) - rnd.nextDouble() / 10000d;
			OhlcvData ohlcvData = new OhlcvData(new Date(millis), high, low, open, close, 8);
			histoData.add(ohlcvData);
		}
		return histoData;
	}
}