package com.example.myfilms.feature.movies.domain.entity;

public class Movie {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Movie(){}

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

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Movie)){
            return false;
        }
        final Movie other = (Movie) obj;
        return this.getTitle().equals(other.getTitle());
    }
}
