package io.serateam.stewboo.core.utility;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration>
{
    @Override
    public JsonElement serialize(Duration duration, Type srcType, JsonSerializationContext context)
    {
        return new JsonPrimitive(duration.toString()); // ISO-8601 representation, e.g., "PT1H30M"
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        return Duration.parse(json.getAsString()); // Parse ISO-8601 string
    }
}