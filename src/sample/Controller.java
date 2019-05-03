package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Controller {
    HashMap<String, String> movieTitleURL;
    public void initialize() throws IOException {

        //Create a hashmap that holds the institution name and corresponding url
        movieTitleURL = new HashMap<String, String>();
        movieTitleURL.put("Avengers Endgame","https://www.rottentomatoes.com/m/avengers_endgame https://www.metacritic.com/movie/avengers-endgame" );
        movieTitleURL.put("Blazing Saddles","https://www.rottentomatoes.com/m/blazing_saddles https://www.metacritic.com/movie/blazing-saddles");
        movieTitleURL.put("Captain Marvel","https://www.rottentomatoes.com/m/captain_marvel https://www.metacritic.com/movie/captain-marvel");
        movieTitleURL.put("The Curse of La Llorona","https://www.rottentomatoes.com/m/the_curse_of_la_llorona_2019" +
                " https://www.metacritic.com/movie/the-curse-of-la-llorona");
        movieTitleURL.put("Lord of the Rings: Return of the King","https://www.rottentomatoes.com/m/the_lord_of_the_rings_the_return_of_the_king" +
                " https://www.metacritic.com/movie/the-lord-of-the-rings-the-return-of-the-king");
        movieTitleURL.put("Miss Bala","https://www.rottentomatoes.com/m/miss_bala_2019 https://www.metacritic.com/movie/miss-bala-2019");
        movieTitleURL.put("Tombstone","https://www.rottentomatoes.com/m/tombstone https://www.metacritic.com/movie/tombstone");
        movieTitleURL.put("Us","https://www.rottentomatoes.com/m/us_2019 https://www.metacritic.com/movie/us");

        movie_Selector_Box.getItems().addAll(movieTitleURL.keySet());
    }

    @FXML
    private Label movie_Title;

    @FXML
    private ImageView movie_Image;

    @FXML
    private Label rotten_Tomatoes_Label;

    @FXML
    private Label metacritic_Text_Label;

    @FXML
    private Label critic_Score_RT_label;

    @FXML
    private Label audience_Score_RT_label;

    @FXML
    private ImageView critic_Score_Image;

    @FXML
    private ImageView audience_Score_Image;

    @FXML
    private Label metascore_Label;

    @FXML
    private ComboBox<String> movie_Selector_Box;

    @FXML
    private Label critic_Percentage_Label;

    @FXML
    private Label audience_Percentage_Label;

    @FXML
    private Label consensus_Text_Label;

    @FXML
    private Label critic_Consensus_Label;

    @FXML
    private Label average_Rating;

    @FXML
    private Label average_Percentage_Label;

    @FXML
    private Label user_score_Label;

    @FXML
    private Label user_score_text_Label;

    @FXML
    void movie_Selected(ActionEvent event) throws IOException {
        clear();

        critic_Consensus_Label.setVisible(true);
        //Connecting to URL's
        String selectedMovieURL = movieTitleURL.get(movie_Selector_Box.getSelectionModel().getSelectedItem());
        String [] selectionURLs = selectedMovieURL.split(" ");
        //System.out.println(selectedMovieURL);
        Document rottenTomatoes = Jsoup.connect(selectionURLs[0]).get();
        Document metaCritic = Jsoup.connect(selectionURLs[1]).get();

        //Setting the Title and year of the movie
        Element titleOfMovie = rottenTomatoes.selectFirst("h1.mop-ratings-wrap__title.mop-ratings-wrap__title--top");

        Element yearOfMovie = metaCritic.selectFirst("span.release_year.lighter");

        movie_Title.setText(titleOfMovie.text() + "\n" + "(" + yearOfMovie.text() + ")");

        //Setting image of movie
        String movieImageUrl = rottenTomatoes.selectFirst("img.posterImage.js-lazyLoad").attr("data-src");
        Image movieImg = new Image(movieImageUrl);
        movie_Image.setImage(movieImg);

        //Setting Consensus text
        String consensus = rottenTomatoes.select("p.mop-ratings-wrap__text.mop-ratings-wrap__text--concensus").text();
        //System.out.println(consensus);
        consensus_Text_Label.setText(consensus);

        //Percentage scores
        String percentScores = rottenTomatoes.select("span.mop-ratings-wrap__percentage").text();
        //System.out.println(percentScores);
        String [] splitPercentages = percentScores.split(" ");
        String criticPercent = splitPercentages[0];
        String audiencePercent = splitPercentages[1];
        critic_Percentage_Label.setText(criticPercent);
        audience_Percentage_Label.setText(audiencePercent);

        //Metactritic scores
        String metaPercents = metaCritic.select("span.metascore_w.larger.movie").text();
        String [] metaPercent = metaPercents.split(" ");
        int meta_metascore = Integer.parseInt(metaPercent[0]);
        int user_metascore = (int)(Double.parseDouble(metaPercent[1]) * 10);
        metascore_Label.setText(meta_metascore + "%");
        user_score_Label.setText(user_metascore + "%");

        //Averaging Scores
        String rottenCriticPercent = criticPercent.replace("%" ,"");
        String rottenAudiencePercent = audiencePercent.replace("%","");
        String average = Integer.toString(((Integer.parseInt(rottenCriticPercent) + Integer.parseInt(rottenAudiencePercent) + meta_metascore +
                user_metascore) / 4));
        average_Percentage_Label.setText(average + "%");



        //Setting URL's of Rotten Tomatoes Image
        String certFreshPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/cf-lg.3c29eff04f2.png";
        String freshPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-fresh-lg.12e316e31d2.png";
        String rottenPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-rotten-lg.ecdfcf9596f.png";

        String audienceUprightPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-upright.ac91cc241ac.png";
        String audienceSpilledPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-spilled.844ba7ac3d0.png";

        //Selecting the specific <span> elements
        Elements pngs = rottenTomatoes.select("span.mop-ratings-wrap__icon.meter-tomato.icon.big.medium-xs");

        String criticImage = pngs.first().toString();
        if (criticImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs certified_fresh\"></span>")){
            Image criticTomatoesPNG = new Image(certFreshPNG);
            critic_Score_Image.setImage(criticTomatoesPNG);
        }else if (criticImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs fresh\"></span>")){
            Image criticTomatoesPNG = new Image(freshPNG);
            critic_Score_Image.setImage(criticTomatoesPNG);
        }else if(criticImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs rotten\"></span>")){
            Image criticTomatoesPNG = new Image(rottenPNG);
            critic_Score_Image.setImage(criticTomatoesPNG);
        }else{

        }

        String audienceImage = pngs.last().toString();
        if (audienceImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs upright\"></span>")){
            Image audienceTomatoesPNG = new Image(audienceUprightPNG);
            audience_Score_Image.setImage(audienceTomatoesPNG);
        }else if (audienceImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs spilled\"></span>")) {
            Image audienceTomatoesPNG = new Image(audienceSpilledPNG);
            audience_Score_Image.setImage(audienceTomatoesPNG);
        }
    }
    void clear(){
        movie_Title.setText(null);
        movie_Image.setImage(null);
        critic_Score_Image.setImage(null);
        audience_Score_Image.setImage(null);
        metascore_Label.setText(null);
        critic_Percentage_Label.setText(null);
        audience_Percentage_Label.setText(null);
    }
}
