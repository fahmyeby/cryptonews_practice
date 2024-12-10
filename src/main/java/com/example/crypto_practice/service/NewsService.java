package com.example.crypto_practice.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.crypto_practice.model.News;
import com.example.crypto_practice.repo.MapRepo;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@Service
public class NewsService {
    @Autowired
    MapRepo repo;
    RestTemplate rt = new RestTemplate();
    private static final String apiURL = "https://data-api.cryptocompare.com/news/v1/article/list?lang=EN&limit=10";

    public List<News> getArticlesFromAPI() {
        String data = rt.getForObject(apiURL, String.class);
        JsonReader jsonReader = Json.createReader(new StringReader(data));
        JsonObject jsonObject = jsonReader.readObject();
        JsonArray jsonArray = jsonObject.getJsonArray("Data");
        List<News> newsData = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject record = jsonArray.getJsonObject(i);
            News n = new News();

            n.setID(record.getInt("ID"));
            n.setGUID(record.getString("GUID"));

            // date conversion
            if (record.containsKey("PUBLISHED_ON")) {
                Long epochTime = record.getJsonNumber("PUBLISHED_ON").longValue();
                Date publishedDate = new Date(epochTime * 1000);
                n.setPUBLISHED_ON(publishedDate);
            }

            n.setIMAGE_URL(record.getString("IMAGE_URL"));
            n.setTITLE(record.getString("TITLE"));
            n.setURL(record.getString("URL"));
            n.setBODY(record.getString("BODY"));

            JsonArray categoryArray = record.getJsonArray("CATEGORY_DATA");
            List<String> categories = new ArrayList<>();
            for (int j = 0; j < categoryArray.size(); j++) {
                JsonObject catObject = categoryArray.getJsonObject(j);
                String catValue = catObject.getString("CATEGORY");
                if (!categories.contains(catValue)) {
                    categories.add(catValue);
                }
            }
            n.setCATEGORY(categories);
            newsData.add(n);
        }
        return newsData;
    }

    public void saveArticles(List<String> articleIds) {
        List<News> allNews = getArticlesFromAPI();

        Map<Integer, News> newsMap = allNews.stream()
                .collect(Collectors.toMap(News::getID, newsData -> newsData));

        for (String idString : articleIds) {
            Integer id = Integer.parseInt(idString);
            News news = newsMap.get(id);

            if (news != null) {
                JsonObjectBuilder builder = Json.createObjectBuilder()
                        .add("ID", news.getID())
                        .add("GUID", news.getGUID())
                        .add("IMAGE_URL", news.getIMAGE_URL())
                        .add("TITLE", news.getTITLE())
                        .add("URL", news.getURL())
                        .add("BODY", news.getBODY());

                Date publishedDate = news.getPUBLISHED_ON();
                Long publishedEpoch = publishedDate.getTime() / 1000;
                builder.add("PUBLISHED_ON", publishedEpoch);

                JsonArrayBuilder catArrayBuilder = Json.createArrayBuilder();
                for (String category : news.getCATEGORY()) {
                    catArrayBuilder.add(category);
                }
                builder.add("CATEGORY", catArrayBuilder);

                String newsJson = builder.build().toString();

                repo.create("News", idString, newsJson);
            }
        }
    }

    public News getById(String id) {
        if (!repo.keyExists("News", id)) {
            return null;
        }

        // get from redis
        String newsJson = repo.get("News", id);

        // json to object
        JsonReader jsonReader = Json.createReader(new StringReader(newsJson));
        JsonObject record = jsonReader.readObject();

        News n = new News();

        n.setID(record.getInt("ID"));
        n.setGUID(record.getString("GUID"));

        // date conversion
        if (record.containsKey("PUBLISHED_ON")) {
            Long epochTime = record.getJsonNumber("PUBLISHED_ON").longValue();
            Date publishedDate = new Date(epochTime * 1000);
            n.setPUBLISHED_ON(publishedDate);
        }

        n.setIMAGE_URL(record.getString("IMAGE_URL"));
        n.setTITLE(record.getString("TITLE"));
        n.setURL(record.getString("URL"));
        n.setBODY(record.getString("BODY"));

        JsonArray categoryArray = record.getJsonArray("CATEGORY");
        List<String> categories = new ArrayList<>();
        for (int j = 0; j < categoryArray.size(); j++) {
            JsonObject catObject = categoryArray.getJsonObject(j);
            String catValue = catObject.getString("CATEGORY");
            if (!categories.contains(catValue)) {
                categories.add(catValue);
            }
        }
        n.setCATEGORY(categories);

        return n;

    }

    public List<News> getNewsFromRedis() {
        List<News> retrievedNewsList = new ArrayList<>();
        Map<Object, Object> retrievedData = repo.getEntries("News");

        for (Entry<Object, Object> mapEntry : retrievedData.entrySet()) {
            JsonReader jsonReader = Json.createReader(new StringReader(mapEntry.getValue().toString()));
            JsonObject record = jsonReader.readObject();

            News n = new News();

            n.setID(record.getInt("ID"));
            n.setGUID(record.getString("GUID"));
    
            // date conversion
            if (record.containsKey("PUBLISHED_ON")) {
                Long epochTime = record.getJsonNumber("PUBLISHED_ON").longValue();
                Date publishedDate = new Date(epochTime * 1000);
                n.setPUBLISHED_ON(publishedDate);
            }
    
            n.setIMAGE_URL(record.getString("IMAGE_URL"));
            n.setTITLE(record.getString("TITLE"));
            n.setURL(record.getString("URL"));
            n.setBODY(record.getString("BODY"));
    
            JsonArray categoryArray = record.getJsonArray("CATEGORY");
            List<String> categories = new ArrayList<>();
            for (int j = 0; j < categoryArray.size(); j++) {
                String catValue = categoryArray.getString(j);
                if (!categories.contains(catValue)) {
                    categories.add(catValue);
                }
            }
            n.setCATEGORY(categories);
            retrievedNewsList.add(n);
        }
        return retrievedNewsList;
    }

    public Boolean delete(Integer id){
        Long deletedCount = repo.delete("News", id);
        return deletedCount > 0;
    }

}
