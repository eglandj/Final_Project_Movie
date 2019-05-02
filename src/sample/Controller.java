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
        movieTitleURL.put("Avengers Endgame", "https://www.rottentomatoes.com/m/avengers_endgame" );
        movieTitleURL.put("Blazing Saddles", "https://www.rottentomatoes.com/m/blazing_saddles");
        movieTitleURL.put("Captain Marvel", "https://www.rottentomatoes.com/m/captain_marvel");
        movieTitleURL.put("The Curse of La Llorona", "https://www.rottentomatoes.com/m/the_curse_of_la_llorona_2019");
        movieTitleURL.put("Lord of the Rings: Return of the King", "https://www.rottentomatoes.com/m/the_lord_of_the_rings_the_return_of_the_king");
        movieTitleURL.put("Miss Bala", "https://www.rottentomatoes.com/m/miss_bala_2019");
        movieTitleURL.put("Tombstone", "https://www.rottentomatoes.com/m/tombstone");
        movieTitleURL.put("Us", "https://www.rottentomatoes.com/m/us_2019");


        movie_Selector_Box.getItems().addAll(movieTitleURL.keySet());
    }

    @FXML
    private Label movie_Title;

    @FXML
    private ImageView movie_Image;

    @FXML
    private ImageView critic_Score_Image;

    @FXML
    private ImageView audience_Score_Image;

    @FXML
    private ImageView user_Score_DB_Image;

    @FXML
    private Label rotten_Tomatoes_Label;

    @FXML
    private Label movie_DB_Label;

    @FXML
    private ComboBox<String> movie_Selector_Box;

    @FXML
    private Label critic_Score_RT_label;

    @FXML
    private Label audience_Score_RT_label;

    @FXML
    private Label user_score_DB_label;

    @FXML
    private Label critic_Percentage_Label;

    @FXML
    private Label audience_Percentage_Label;

    @FXML
    private Label user_Percentage_Label;

    @FXML
    private Label consensus_Text_Label;

    @FXML
    private Label critic_Consensus_Label;

    @FXML
    private Label average_Rating;

    @FXML
    private Label average_Percentage_Label;

    @FXML
    void movie_Selected(ActionEvent event) throws IOException {
        clear();

        critic_Consensus_Label.setVisible(true);
        //Connecting to URL's
        String selectedMovieURL = movieTitleURL.get(movie_Selector_Box.getSelectionModel().getSelectedItem());
        Document doc = Jsoup.connect(selectedMovieURL).get();

        //Setting the Title of the movie
        Element titleOfMovie = doc.selectFirst("h1.mop-ratings-wrap__title.mop-ratings-wrap__title--top");
        movie_Title.setText(titleOfMovie.text());

        //Setting image of movie
        String movieImageUrl = doc.selectFirst("img.posterImage.js-lazyLoad").attr("data-src");
        Image movieImg = new Image(movieImageUrl);
        movie_Image.setImage(movieImg);

        //Setting Consensus text
        String consensus = doc.select("p.mop-ratings-wrap__text.mop-ratings-wrap__text--concensus").text();
        //System.out.println(consensus);
        consensus_Text_Label.setText(consensus);

        String percentScores = doc.select("span.mop-ratings-wrap__percentage").text();
        //System.out.println(percentScores);
        String [] splitPercentages = percentScores.split(" ");
        String criticPercent = splitPercentages[0];
        String audiencePercent = splitPercentages[1];
        critic_Percentage_Label.setText(criticPercent);
        audience_Percentage_Label.setText(audiencePercent);

        //Averaging Scores
        String newC = criticPercent.replace("%" ,"");
        String newA = audiencePercent.replace("%","");
        String average = Integer.toString(((Integer.parseInt(newC) + Integer.parseInt(newA)) / 2));
        average_Percentage_Label.setText(average + "%");


        //Setting URL's of Rotten Tomatoes Image
        String certFreshPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/cf-lg.3c29eff04f2.png";
        String freshPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-fresh-lg.12e316e31d2.png";
        String rottenPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-rotten-lg.ecdfcf9596f.png";

        String audienceUprightPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-upright.ac91cc241ac.png";
        String audienceSpilledPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-spilled.844ba7ac3d0.png";

        //Selecting the specific <span> elements
        Elements pngs = doc.select("span.mop-ratings-wrap__icon.meter-tomato.icon.big.medium-xs");

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
        user_Score_DB_Image.setImage(null);
        critic_Percentage_Label.setText(null);
        audience_Percentage_Label.setText(null);
        user_Percentage_Label.setText(null);
    }
}
