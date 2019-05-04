package com.ucsdextandroid1.snapmap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActiveUserLocationResponseDeserializer implements JsonDeserializer<ActiveUserLocationResponse> {
    @Override
    public ActiveUserLocationResponse deserialize(JsonElement json
                                                 , Type typeOfT
                                                 , JsonDeserializationContext context
    ) throws JsonParseException {

        ActiveUserLocationResponse response = new ActiveUserLocationResponse();

        List<UserLocationData> locations = new ArrayList<>();  //take data from the data source response and return to ActiveUserLocationResponse and loop thru the list in array
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {

           UserLocationData locationData = context.deserialize(entry.getValue(),UserLocationData.class);

           //because we have the class alrady defined for the object name, we can use the method above instead of the below method
//            UserLocationData locationData = new UserLocationData(
//               entry.getValue().getAsJsonObject().get("Color").getAsString();
//            )

            locationData.setUserId(entry.getKey());

            locations.add(locationData);

        }

        response.setUserLocations(locations);

        return response;
    }
}
