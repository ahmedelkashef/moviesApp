package com.example.ahmedelkashef.MoviesApp;

public  class MovieReview {
    private String Id;
    private String Author;
    private String Content;
    private String URL;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getId() {

        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}