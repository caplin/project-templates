package com.caplin.template.channel;


import com.caplin.datasource.DataSource;
import com.caplin.datasource.channel.JsonChannel;
import com.caplin.datasource.channel.JsonChannelListener;
import com.caplin.datasource.messaging.json.JsonChannelMessage;
import com.caplin.datasource.namespace.PrefixNamespace;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonChannelTemplate implements JsonChannelListener {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<String, JsonChannel> newChannels = new ConcurrentHashMap<>();
    private final Logger logger;
    private final DataSource dataSource;

    public JsonChannelTemplate(DataSource dataSource){
        this.dataSource = dataSource;
        logger = dataSource.getLogger();
    }

    public void initialise() {
        dataSource.addJsonChannelListener(new PrefixNamespace("/TEMPLATE/JSONCHANNEL"), this);
        executorService.scheduleAtFixedRate(() -> {
            newChannels.forEach((subject, channel) -> {
                channel.send(new JsonMessage("Hello", "Please send a jsonContrib with the name `operation` set to the value `Ping` to this channel"));
                newChannels.remove(subject);
            });
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public boolean onChannelOpen(JsonChannel jsonChannel) {
        logger.log(Level.INFO, "Opening jsonChannel " + jsonChannel);
        newChannels.put(jsonChannel.getSubject(), jsonChannel);
        return true;
    }

    @Override
    public void onChannelClose(JsonChannel jsonChannel) {
        logger.log(Level.INFO, "Closing jsonChannel " + jsonChannel);
    }

    @Override
    public void onMessageReceived(JsonChannel jsonChannel, JsonChannelMessage jsonChannelMessage) {
        logger.log(Level.INFO, "Received message " + jsonChannelMessage + " on jsonChannel " + jsonChannelMessage);
        newChannels.remove(jsonChannel.getSubject()); //Take channel out of newChannels map so that we don't send the Hello

        try {
            JsonMessage message = jsonChannelMessage.getJsonAsType(JsonMessage.class);
            if (message.getOperation().equals("Ping")) {
                jsonChannel.send(new JsonMessage("Pong", "Pong"));
            } else {
                jsonChannel.send(new JsonMessage("Error", "Please send a contrib with the field `operation` set to the value `Ping` to this channel"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class JsonMessage {
        String operation;
        String description;

        JsonMessage(String operation, String description){
            this.operation = operation;
            this.description = description;
        }

        JsonMessage(){
            this.operation = "";
            this.description = "";
        }
        public String getOperation() {
            return operation;
        }
    }
}
