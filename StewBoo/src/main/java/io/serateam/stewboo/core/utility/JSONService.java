package io.serateam.stewboo.core.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class JSONService
{
    private static final GsonBuilder builder = new GsonBuilder()
                                            .setPrettyPrinting()
                                            .registerTypeAdapter(LocalDateTime.class,
                                                    new LocalDateTimeTypeAdapter())
                                            .registerTypeAdapter(Duration.class,
                                                    new DurationTypeAdapter());
    private static final Gson gson = builder.create();

    // NOTE: We use generics here to limit what goes into this function,
    // so only classes (T) that implements ISerializable can only go in here.

    /**
     * @param data class instance with data
     * @param <T> Class that implements ISerializable
     * @return JSON String
     */
    public static <T extends ISerializable> String serialize(T data)
    {
        return gson.toJson(data);
    }

    /**
     * @param pathToJSonFile use the String from SharedVariables.java class
     * @param data class instance with data
     * @param <T> Class that implements ISerializable
     */
    public static <T extends ISerializable> void serializeAndWriteToFile(String pathToJSonFile, T data)
    {
        try (FileWriter writer = new FileWriter(pathToJSonFile))
        {
            gson.toJson(data, writer);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        System.out.println("JSONService: serialized to file " + pathToJSonFile);
    }

    /**
     * @param pathToJsonFile use the String from SharedVariables.java class
     * @param classType e.g. MyClass.class
     * @param <T> Class that implements ISerializable
     * @return class instance with data; null if file not found
     */
    public static <T extends ISerializable> T deserialize(String pathToJsonFile, Class<T> classType)
    {
        try (FileReader reader = new FileReader(pathToJsonFile))
        {
            return gson.fromJson(reader, classType);
        }
        catch (IOException e)
        {
            System.err.printf("JSONService: File %s is unavailable for deserializing due to [%s]%n", pathToJsonFile, e.getMessage());
            return null;
        }
    }
}
