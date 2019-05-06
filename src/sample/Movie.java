package sample;

import javafx.scene.image.Image;

public class Movie {
    public String movieTitle;
    public String movieYear;
    public Image movieImage;
    public String Consensus;
    public String rottenCriticScore;
    public Image rottenTomatoImage;
    public String rottenAudienceScore;
    public Image rottenAudienceImage;
    public String metaScore;
    public String metaUserScore;
    public String averageScore;

    public Movie(String title, String year, Image imageOfMovie, String criticConsensus, String criticScoreRotten, Image criticImageTomato,
                 String audienceScoreRotten, Image audienceImagePopcorn, String metascore, String userscore, String average){
        movieTitle = title;
        movieYear = year;
        movieImage = imageOfMovie;
        Consensus = criticConsensus;
        rottenCriticScore = criticScoreRotten;
        rottenTomatoImage = criticImageTomato;
        rottenAudienceScore = audienceScoreRotten;
        rottenAudienceImage = audienceImagePopcorn;
        metaScore = metascore;
        metaUserScore = userscore;
        averageScore = average;
    }
}
