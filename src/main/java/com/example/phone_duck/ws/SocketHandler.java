package com.example.phone_duck.ws;

import com.example.phone_duck.model.Channel;
import com.example.phone_duck.model.TextModel;
import com.example.phone_duck.service.ChannelService;
import com.example.phone_duck.service.TextModelService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private TextModelService textModelService;

    private List<WebSocketSession> sessions = new ArrayList<>();
    private List<TextModel> textModels = new ArrayList<>();

    public void broadcastNewChannel(Channel channel){
        Gson gson = new Gson();
        try
        {
            for (WebSocketSession session: sessions) {
                if (session.getHandshakeHeaders().getFirst("type").equals("mainChannel"))
                {
                    String textMessage = gson.toJson(channel);
                    session.sendMessage(new TextMessage(textMessage));
                }


            }
        }
        catch (Exception e)
        {

        }
    }

    public void broadcastHistory(WebSocketSession session, List<Channel> channels)
    {
        Gson gson = new Gson();
        try
        {
            for (Channel channel: channels) {
                String textMessage = gson.toJson(channel);
                session.sendMessage(new TextMessage(textMessage));


            }
        }
        catch (Exception e)
        {

        }
    }

    public void broadcastToRightChannel(List<String> message){
        Gson gson = new Gson();
        List<TextModel> textersToBroadcast = textModelService.getAllByChannelId(textModels, message.get(0));
        try
        {
            for (TextModel textModel : textersToBroadcast) {
                String textMessage = gson.toJson(message);
                textModel.getSession().sendMessage(new TextMessage(textMessage));

            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String messagePayload = message.getPayload();
        List<String> messageDivided = Arrays.asList(messagePayload.split(">"));
        String messageWithOwner = session.getHandshakeHeaders().getFirst("name") + ": " + messageDivided.get(1);
        messageDivided.set(1, messageWithOwner);

        broadcastToRightChannel(messageDivided);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        if (session.getHandshakeHeaders().getFirst("type").equals("mainChannel"))
        {
            broadcastHistory(session, channelService.getAllChannels());
        }
        else if(session.getHandshakeHeaders().getFirst("type").equals("chat"))
        {
            TextModel newTextModel = new TextModel();
            newTextModel.setName(session.getHandshakeHeaders().getFirst("name"));
            newTextModel.setChannelIds(session.getHandshakeHeaders().getFirst("ChannelIds"));
            newTextModel.setSession(session);
            textModels.add(newTextModel);
        }
        System.out.println("Socket created");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session.getHandshakeHeaders().getFirst("type").equals("mainChannel"))
        {
            sessions.remove(session);
        }
        else if(session.getHandshakeHeaders().getFirst("type").equals("chat"))
        {
            textModels = textModelService.delete(session, textModels);
        }
    }
}