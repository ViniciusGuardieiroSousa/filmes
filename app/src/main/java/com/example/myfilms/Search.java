package com.example.myfilms;

public class Search {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
    private int imagem; //contem o id da imagem
    public Search(){}

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public int getImagem() {
        return imagem;
    }



    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }
    public String getPoster() {
        return Poster;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }
}
