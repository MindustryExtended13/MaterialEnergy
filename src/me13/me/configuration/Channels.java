package me13.me.configuration;

import arc.struct.ObjectMap;

import static me13.me.configuration.MeConfiguration.*;

public class Channels {
    public static ObjectMap<String, Integer> additionDefault;
    public static Channels conf;

    public static void load() {
        config.setDefKey("me-channels-config.json", json.toJson(getDefault()));
        conf = json.fromJson(config.getKey("me-channels-config.json"), Channels.class);
    }

    public static Channels getDefault() {
        return new Channels(additionDefault, 8);
    }

    static {
        additionDefault = new ObjectMap<>();
        additionDefault.put("me-controller", 5);
        additionDefault.put("me-erekir-controller", 5);
    }

    private ObjectMap<String, Integer> addition;
    private int defaultChannels;

    @SuppressWarnings("unused")
    public Channels() {
    }

    public Channels(ObjectMap<String, Integer> addition, int defaultChannels) {
        this.addition = addition;
        this.defaultChannels = defaultChannels;
    }

    public int getDefaultChannels() {
        return defaultChannels;
    }

    public int getChannelsAddition(String block) {
        return addition.containsKey(block) ? addition.get(block) : defaultChannels;
    }
}