package com.example.guardianfeeds;

public class Feed {
    private String title;
    private String category;
    private String date;
    private String url;
    private String author;

    public Feed(String author, String title, String category, String date, String url) {
        this.author = author;
        this.title = title;
        this.category = category;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }


    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }


    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

}
