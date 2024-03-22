package com.example.demo;

import com.contentstack.sdk.*;
import com.contentstack.sdk.Error;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class ContentFetcher {
    public void fetchContent() {
        Stack stack = null;
        try {
            stack = Contentstack.stack("blte0c652cd065dcec5", "cs505de4550c9fd7a9729e7b28", "development");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ContentType contentType = stack.contentType("blog_post");
        Entry entry = contentType.entry("blt57efde66b730c0e2");

        System.out.println("Fetching a single entry:");
        entry.fetch(new EntryResultCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, Error error) {
                if (error == null) {
                    System.out.println("Content fetched successfully: " + entry.toJSON());
                } else {
                    System.err.println("Error fetching content: " + error.getErrorMessage());
                }
            }
        });
    }

public void fetchAllContent(Consumer<List<String>> consumer) {
    Stack stack = null;
    try {
        stack = Contentstack.stack("blte0c652cd065dcec5", "cs505de4550c9fd7a9729e7b28", "development");
    } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
    }
    stack.getContentTypes(new JSONObject(), new ContentTypesCallback() {
        @Override
        public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
            if (error == null) {
                List<String> contentTypes = new ArrayList<>();
                contentTypesModel.getResultArray().forEach(contentTypeJson -> {
                    JSONObject contentTypeJsonObject = (JSONObject) contentTypeJson;
                    String contentTypeTitle = contentTypeJsonObject.optString("title");
                    contentTypes.add(contentTypeTitle);
                });
                consumer.accept(contentTypes);
            } else {
                System.err.println("Error fetching content types: " + error.getErrorMessage());
            }
        }
    });
}


    public void fetchAll(String contentTypeName,String key,Consumer<List<String>> consumer) {
        Stack stack = null;
        try {
            stack = Contentstack.stack("blte0c652cd065dcec5", "cs505de4550c9fd7a9729e7b28", "development");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ContentType contentType = stack.contentType(contentTypeName);
        Query csquery = contentType.query();
        csquery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<String> titles = new ArrayList<>();
                    queryresult.getResultObjects().forEach(entry -> {
                        String authorTitle = entry.getString(key);
                        titles.add(authorTitle);
                    });
                    consumer.accept(titles);

                } else {
                    System.err.println("Error fetching content types: " + error.getErrorMessage());
                }
            }
        });
    }
    public void fetchAllImg(String contentTypeName, String key,Consumer<List<String>> consumer){
        Stack stack = null;
        try {
            stack = Contentstack.stack("blte0c652cd065dcec5", "cs505de4550c9fd7a9729e7b28", "development");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ContentType contentType = stack.contentType(contentTypeName);
        Query csquery = contentType.query();
        csquery.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                if (error == null) {
                    List<String> authorImg = new ArrayList<>();
                    queryresult.getResultObjects().forEach(entry -> {
//                        JSONObject authorPicture = entry.getJSONObject("picture");
//                        String ImgUrl = authorPicture.getString("url");
                        Asset authorPicture = entry.getAsset(key);
                        String ImgUrl = authorPicture.getUrl();
                        authorImg.add(ImgUrl);
                    });
                    consumer.accept(authorImg);
                } else {
                    System.err.println("Error fetching images: " + error.getErrorMessage());
                }
            }
        });
    }
}