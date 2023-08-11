package com.example.phone_duck.service;

import com.example.phone_duck.model.TextModel;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TextModelService {

    public List<TextModel> getAllByChannelId(List<TextModel> textModelList, String channelID)
    {
        List<TextModel> foundTextModelList = new ArrayList<>();
        for (TextModel textModel : textModelList) {
            List<String> channelIdAsArray = Arrays.asList(textModel.getChannelIds().split(","));
            if(channelIdAsArray.contains(channelID))
            {
                foundTextModelList.add(textModel);
            }


        }
        return foundTextModelList;
    }

    public List<TextModel> delete(WebSocketSession session, List<TextModel> textModelList)
    {
        for (TextModel textModel : textModelList) {
            if (textModel.getSession() == session)
            {
                textModelList.remove(textModel);
            }

        }
        return textModelList;

    }
}