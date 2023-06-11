package me13.me.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me13.core.configuration.Configuration;

public class MeConfiguration {
    public static Configuration config = new Configuration();
    public static Gson json = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {
        Channels.load();
    }
}
