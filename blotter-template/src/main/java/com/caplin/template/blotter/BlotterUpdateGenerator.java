package com.caplin.template.blotter;

import com.caplin.datasource.blotter.BlotterApplicationListener;
import com.caplin.datasource.blotter.BlotterChannel;
import com.caplin.datasource.blotter.BlotterItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BlotterUpdateGenerator implements BlotterApplicationListener, Runnable
{

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	private final String[] CURRENCY_PAIRS = { "EUR/USD", "USD/JPY", "GBP/USD", "USD/CHF", "EUR/GBP", "EUR/JPY", "EUR/CHF", "AUD/USD", "USD/CAD", "NZD/USD" };

	private final Logger logger;
	private final List<BlotterChannel> activeChannels;
	private final ScheduledExecutorService scheduledThreadPool;
	private final Random random;
	private final BlotterItem ocoParent;
	private final BlotterItem spotParent;

	private int currentItemIndex = 0;

	public BlotterUpdateGenerator(Logger logger)
	{
		this.logger = logger;
		this.activeChannels = new CopyOnWriteArrayList<>();
		this.scheduledThreadPool = Executors.newScheduledThreadPool(1);
		this.random = new Random();
		
		this.ocoParent = new BlotterItem("OCOParent");
		this.spotParent = new BlotterItem("SPOTParent");

		scheduledThreadPool.schedule(this, 0, TimeUnit.SECONDS);
	}
	
	@Override
	public void blotterChannelOpened(BlotterChannel channel)
	{
		logger.info("blotterChannelOpened: " + channel.getSubject());
		// adds a channel to the list so random items can be sent to it
		activeChannels.add(channel);

		// if sending BlotterItems with parents, the parents will be automatically sent
		channel.sendBlotterItems(createOCOChildren(channel));
		channel.sendBlotterItem(createSpotChild(channel));
	}

	private List<BlotterItem> createOCOChildren(BlotterChannel channel)
	{
		BlotterItem currentOcoOrder = new BlotterItem(Integer.toString(++currentItemIndex));
		
		currentOcoOrder.setField("tradeDate", DATE_FORMAT.format(new Date()));
		currentOcoOrder.setField("currencyPair", CURRENCY_PAIRS[random.nextInt(CURRENCY_PAIRS.length)]);
		currentOcoOrder.setField("submittedBy", channel.getUsername());
		
		//The same parent BlotterItem object can be used across multiple channels,
		//but only relevant children will be published to each user.
		currentOcoOrder.setParent(ocoParent);
		
		BlotterItem stopLoss = new BlotterItem(Integer.toString(++currentItemIndex));
		
		stopLoss.setField("orderType", "stop-loss");
		stopLoss.setField("rate", Integer.toString(random.nextInt(10) + 5));
		stopLoss.setParent(currentOcoOrder);
		
		
		BlotterItem limitOrder = new BlotterItem(Integer.toString(++currentItemIndex));
		
		limitOrder.setField("orderType", "limit-order");
		limitOrder.setField("rate", Integer.toString(random.nextInt(10) + 15));
		limitOrder.setParent(currentOcoOrder);
		
		//any added BlotterItem's parents will be added automatically   
		return asList(stopLoss, limitOrder);
	}
	
	private List<BlotterItem> asList(BlotterItem stopLoss, BlotterItem limitOrder)
	{
		List<BlotterItem> blotterItems = new ArrayList<>();
		blotterItems.add(stopLoss);
		blotterItems.add(limitOrder);
		return blotterItems;
	}

	private BlotterItem createSpotChild(BlotterChannel channel)
	{
		BlotterItem spot = new BlotterItem(Integer.toString(++currentItemIndex));
		spot.setField("tradeDate", DATE_FORMAT.format(new Date()));
		spot.setField("currencyPair", CURRENCY_PAIRS[random.nextInt(CURRENCY_PAIRS.length)]);
		spot.setField("submittedBy", channel.getUsername());
		spot.setField("orderType", "spot");
		spot.setField("rate", Integer.toString(random.nextInt(10) + 15));
		
		spot.setParent(spotParent);
		return spot;
	}

	@Override
	public void blotterChannelClosed(BlotterChannel channel)
	{
		logger.info("blotterChannelClosed: " + channel.getSubject());
		// removes a channel from the list so no more random items can be sent to it
		activeChannels.remove(channel);
	}
	
	@Override
	public void run()
	{
		for (BlotterChannel channel : activeChannels)
		{
			channel.sendBlotterItems(createOCOChildren(channel));		
			channel.sendBlotterItem(createSpotChild(channel));
		}

		// Schedules the next execution with random start delay
		scheduledThreadPool.schedule(this, random.nextInt(5) + 4, TimeUnit.SECONDS);
	}
}
