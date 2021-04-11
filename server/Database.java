package server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Database {
    private String filePath = "src/server/data/db.json";
    private Path path;
    private Gson gson;

    private JsonObject data;
    private ReadWriteLock lock;

    public Database() {
        lock = new ReentrantReadWriteLock();
        path = Paths.get(filePath);
        gson = new Gson();
        refreshDatabase();
    }

    public int getSize() {
        return data.size();
    }

    public void setData(JsonElement key, JsonElement value) {
//        data.put(key, value);

        JsonObject current = data;
        String lastKey = "";

        try {
            if (key.isJsonArray()) {
                JsonArray array = (JsonArray) key;
                lastKey = array.get(array.size() - 1).getAsString();
                for (int i = 0; i < array.size() - 1; i++) {
                    current = (JsonObject) current.get(array.get(i).getAsString());
                }
            } else {
                lastKey = key.getAsString();
            }

            if (value.isJsonObject()) {
                current.add(lastKey, value);
            } else {
                current.addProperty(lastKey, value.getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        save();
        refreshDatabase();
    }

    public JsonElement get(JsonElement key) {
        
        JsonObject targetedObject = getTragetedObject(key);
        String finalKey = getFinalKey(key);
        JsonElement retrievedElement = targetedObject.get(finalKey);

        return retrievedElement;
    }

    public void delete(JsonElement key) {

        JsonObject targetedObject = getTragetedObject(key);
        String finalKey = getFinalKey(key);
        targetedObject.remove(finalKey);

        save();
        refreshDatabase();
    }

    private String getFinalKey(JsonElement key) {
        String lastKey = "";
        if (key.isJsonArray()) {
            JsonArray array = (JsonArray) key;
            lastKey = array.get(array.size() - 1).getAsString();
        } else {
            lastKey = key.getAsString();
        }
        return lastKey;
    }

    private JsonObject getTragetedObject(JsonElement key) {
        JsonObject current = data;
        String lastKey = "";

        if (key.isJsonArray()) {
            JsonArray array = (JsonArray) key;
            lastKey = array.get(array.size() - 1).getAsString();
            for (int i = 0; i < array.size() - 1; i++) {
                String currentKey = array.get(i).getAsString();
                if (!current.has(currentKey)) {
                    throw new RuntimeException("No such key");
                }
                current = (JsonObject) current.get(currentKey);
            }
        } else {
            lastKey = key.getAsString();
        }
        if (!current.has(lastKey)) {
            throw new RuntimeException("No such key");
        }
        return current;
    }

//    private void checkKey(JsonElement key) {
//
//    }

    private void refreshDatabase() {

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            Lock readLock = lock.readLock();

            try {
                readLock.lock();
                data = gson.fromJson(reader, JsonElement.class).getAsJsonObject();
//                System.out.println("::Loaded:: " + data);
            } finally {
                readLock.unlock();
            }

        } catch (IOException e) {
            System.out.println("database error");
        }
    }

    public void save() {

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

            Lock writeLock = lock.writeLock();

            try {
                writeLock.lock();
                gson.toJson(data, writer);
            } finally {
                writeLock.unlock();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
