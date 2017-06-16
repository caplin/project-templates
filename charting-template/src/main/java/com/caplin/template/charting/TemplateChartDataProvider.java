package com.caplin.template.charting;

import com.caplin.charting.ChartDataProvider;
import com.caplin.charting.ChartDataRequest;

/**
 * Deals with requests for historic chart data.
 */
public class TemplateChartDataProvider implements ChartDataProvider
{
	/**
	 * The TemplateChartDataProvider responds to requests on a separate thread as a way of
	 * demonstrating that it works asynchronously.
	 */
	@Override
	public void requestChartData( final ChartDataRequest request )
	{
		ChartDataResponseHandler historicResponseThread = new ChartDataResponseHandler(request);
		historicResponseThread.run();
	}
}
