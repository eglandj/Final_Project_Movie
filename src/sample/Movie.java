package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Movie {
    public StringProperty movieTitle;
    public StringProperty movieYear;
    public Image movieImage;
    public StringProperty Consensus;
    public StringProperty rottenCriticScore;
    public Image rottenTomatoImage;
    public StringProperty rottenAudienceScore;
    public Image rottenAudienceImage;
    public StringProperty metaScore;
    public StringProperty metaUserScore;
    public StringProperty averageScore;

    public StringProperty getMovieTitle() {
        return movieTitle;
    }

    public StringProperty getMovieYear() {
        return movieYear;
    }
    public StringProperty getConsensus() {
        return Consensus;
    }
    public StringProperty getRottenCriticScore() {
        return rottenCriticScore;
    }
    public StringProperty getRottenAudienceScore() {
        return rottenAudienceScore;
    }

    public StringProperty getMetaScore() {
        return metaScore;
    }
    public StringProperty getMetaUserScore() {
        return metaUserScore;
    }
    public StringProperty getAverageScore() {
        return averageScore;
    }

    public Movie(String title, String year, Image imageOfMovie, String criticConsensus, String criticScoreRotten, Image criticImageTomato,
                 String audienceScoreRotten, Image audienceImagePopcorn, String metascore, String userscore, String average){
        movieTitle = new SimpleStringProperty(title);
        movieYear = new SimpleStringProperty(year);
        movieImage = imageOfMovie;
        Consensus = new SimpleStringProperty(criticConsensus);
        rottenCriticScore = new SimpleStringProperty(criticScoreRotten);
        rottenTomatoImage = criticImageTomato;
        rottenAudienceScore = new SimpleStringProperty(audienceScoreRotten);
        rottenAudienceImage = audienceImagePopcorn;
        metaScore = new SimpleStringProperty(metascore);
        metaUserScore = new SimpleStringProperty(userscore);
        averageScore = new SimpleStringProperty(average);
    }

    public Movie(String title, String year, String criticConsensus, String criticScoreRotten, String audienceScoreRotten, String metascore, String userscore, String average){
        movieTitle = new SimpleStringProperty(title);
        movieYear = new SimpleStringProperty(year);
        Consensus = new SimpleStringProperty(criticConsensus);
        rottenCriticScore = new SimpleStringProperty(criticScoreRotten);
        rottenAudienceScore = new SimpleStringProperty(audienceScoreRotten);
        metaScore = new SimpleStringProperty(metascore);
        metaUserScore = new SimpleStringProperty(userscore);
        averageScore = new SimpleStringProperty(average);
    }
}
