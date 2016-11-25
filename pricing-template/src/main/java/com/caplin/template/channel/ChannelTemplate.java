package com.caplin.template.channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.channel.Channel;
import com.caplin.datasource.channel.ChannelListener;
import com.caplin.datasource.messaging.record.RecordMessage;
import com.caplin.datasource.namespace.PrefixNamespace;

public class ChannelTemplate implements ChannelListener {
    private static final String OPERATION_FIELD = "operation";
    private static final String DESCRIPTION_FIELD = "description";
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<String, Channel> newChannels = new ConcurrentHashMap<>();
    private final Logger logger;
    private final DataSource dataSource;

    public ChannelTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        logger = dataSource.getLogger();
    }

    public void initialise() {
        dataSource.addChannelListener(new PrefixNamespace("/TEMPLATE/CHANNEL"), this);

        executorService.scheduleAtFixedRate(() -> {
            newChannels.forEach((subject, channel) -> {

                RecordMessage message = channel.createRecordMessage(channel.getSubject());
                message.setField(OPERATION_FIELD, "Hello");
                message.setField(DESCRIPTION_FIELD, "Please send a contrib with the field `operation` set to the value `Ping` to this channel");
                message.setImage(true);
                channel.sendMessage(message);

                newChannels.remove(subject);
            });
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public boolean onChannelOpen(Channel channel) {
        logger.log(Level.INFO, "Opening channel " + channel);
        newChannels.put(channel.getSubject(), channel);
        return true;
    }

    @Override
    public void onChannelClose(Channel channel) {
        logger.log(Level.INFO, "Closing channel " + channel);
    }

    @Override
    public void onMessageReceived(Channel channel, RecordMessage recordMessage) {
        logger.log(Level.INFO, "Received message " + recordMessage + " on channel " + recordMessage);

        newChannels.remove(channel.getSubject()); //Take channel out of newChannels map so that we don't send the Hello
        RecordMessage message = channel.createRecordMessage(channel.getSubject());

        if (recordMessage.getField(OPERATION_FIELD).equals("Ping")) {
            message.setField(OPERATION_FIELD, "Pong");
            message.setField(DESCRIPTION_FIELD, "Pong");
        } else {
            message.setField(OPERATION_FIELD, "Error");
            message.setField(DESCRIPTION_FIELD, "Please send a contrib with the field `operation` set to the value `Ping` to this channel");
        }

        channel.sendMessage(message);
    }
}
