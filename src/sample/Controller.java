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

import java.sql.*;

import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;


public class Controller {

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
    public void createButton(ActionEvent actionEvent) throws SQLException {
        try{
            databaseCreate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void movie_Selected(ActionEvent event) throws IOException {
        clear();
        critic_Consensus_Label.setVisible(true);
        populateSelection();
    }

    HashMap<String, Movie> movieList;
    public void initialize() throws IOException {
        movieCreator();
        SortedSet<String> keys = new TreeSet<>(movieList.keySet());
        movie_Selector_Box.getItems().addAll(keys);
    }

    void populateSelection(){

        Movie selectedMovie = movieList.get(movie_Selector_Box.getSelectionModel().getSelectedItem());
        movie_Title.setText(selectedMovie.movieTitle + "\n" + "(" + selectedMovie.movieYear + ")");
        movie_Image.setImage(selectedMovie.movieImage);
        consensus_Text_Label.setText(selectedMovie.Consensus);
        critic_Percentage_Label.setText(selectedMovie.rottenCriticScore);
        critic_Score_Image.setImage(selectedMovie.rottenTomatoImage);
        audience_Percentage_Label.setText(selectedMovie.rottenAudienceScore);
        audience_Score_Image.setImage(selectedMovie.rottenAudienceImage);
        metascore_Label.setText(selectedMovie.metaScore);
        user_score_Label.setText(selectedMovie.metaUserScore);
        average_Percentage_Label.setText(selectedMovie.averageScore);
    }

    void movieCreator() throws IOException{
        String [] movies = new String [8];

        movies[0] = "https://www.rottentomatoes.com/m/avengers_endgame https://www.metacritic.com/movie/avengers-endgame";
        movies[1] = "https://www.rottentomatoes.com/m/blazing_saddles https://www.metacritic.com/movie/blazing-saddles";
        movies[2] = "https://www.rottentomatoes.com/m/captain_marvel https://www.metacritic.com/movie/captain-marvel";
        movies[3] = "https://www.rottentomatoes.com/m/the_curse_of_la_llorona_2019 " +
                "https://www.metacritic.com/movie/the-curse-of-la-llorona";
        movies[4] = "https://www.rottentomatoes.com/m/the_lord_of_the_rings_the_return_of_the_king" +
                " https://www.metacritic.com/movie/the-lord-of-the-rings-the-return-of-the-king";
        movies[5] = "https://www.rottentomatoes.com/m/miss_bala_2019 https://www.metacritic.com/movie/miss-bala-2019";
        movies[6] = "https://www.rottentomatoes.com/m/tombstone https://www.metacritic.com/movie/tombstone";
        movies[7] = "https://www.rottentomatoes.com/m/us_2019 https://www.metacritic.com/movie/us";

        final int MAX = movies.length;
        final JFrame frame = new JFrame();

        // creates progress bar
        final JProgressBar pb = new JProgressBar();
        pb.setMinimum(0);
        pb.setMaximum(MAX);
        pb.setStringPainted(true);
        pb.setSize(1000,900);

        JLabel label = new JLabel("Loading Data");
        frame.getContentPane().add(label);

        // add progress bar
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(pb);
        frame.setLocation(650,350);


        frame.setSize(200, 100);
        frame.setVisible(true);

        //Create a hashmap that holds movie objects
        movieList = new HashMap<>();

        //Counter for progress bar
        int counter = 0;

        for (String i : movies){

            String [] URLs = i.split(" ");
            Document rottenDoc = Jsoup.connect(URLs[0]).get();
            Document metaDoc = Jsoup.connect(URLs[1]).get();

            //Setting the Title and year of the movie
            String titleOfMovie = rottenDoc.selectFirst("h1.mop-ratings-wrap__title.mop-ratings-wrap__title--top").text();

            String yearOfMovie = metaDoc.selectFirst("span.release_year.lighter").text();

            //Setting image of movie
            String movieImageUrl = rottenDoc.selectFirst("img.posterImage.js-lazyLoad").attr("data-src");
            Image movieImg = new Image(movieImageUrl);

            //Setting Consensus text
            String consensus = rottenDoc.select("p.mop-ratings-wrap__text.mop-ratings-wrap__text--concensus").text();

            //Percentage scores
            String percentScores = rottenDoc.select("span.mop-ratings-wrap__percentage").text();
            String [] splitPercentages = percentScores.split(" ");
            String criticPercent = splitPercentages[0];
            String audiencePercent = splitPercentages[1];

            //Metactritic scores
            String metaPercents = metaDoc.select("span.metascore_w.larger.movie").text();
            String [] metaPercent = metaPercents.split(" ");
            int meta_metascore = Integer.parseInt(metaPercent[0]);
            int user_metascore = (int)(Double.parseDouble(metaPercent[1]) * 10);

            //Averaging Scores
            String rottenCriticPercent = criticPercent.replace("%" ,"");
            String rottenAudiencePercent = audiencePercent.replace("%","");
            String average = Integer.toString(((Integer.parseInt(rottenCriticPercent) + Integer.parseInt(rottenAudiencePercent) + meta_metascore +
                    user_metascore) / 4));

            //Setting URL's of Rotten Tomatoes Image
            String certFreshPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/cf-lg.3c29eff04f2.png";
            String freshPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-fresh-lg.12e316e31d2.png";
            String rottenPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-rotten-lg.ecdfcf9596f.png";

            String audienceUprightPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-upright.ac91cc241ac.png";
            String audienceSpilledPNG = "https://www.rottentomatoes.com/assets/pizza-pie/images/icons/global/new-spilled.844ba7ac3d0.png";

            //Selecting the specific <span> elements for .pngs
            Elements pngs = rottenDoc.select("span.mop-ratings-wrap__icon.meter-tomato.icon.big.medium-xs");

            String criticImage = pngs.first().toString();
            Image criticTomatoesPNG;
            if (criticImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs certified_fresh\"></span>")){
                criticTomatoesPNG = new Image(certFreshPNG);
            }else if (criticImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs fresh\"></span>")){
                criticTomatoesPNG = new Image(freshPNG);
            }else if(criticImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs rotten\"></span>")){
                criticTomatoesPNG = new Image(rottenPNG);
            }else {
                criticTomatoesPNG = null;
                JOptionPane.showMessageDialog(null, "Could not locate a URL for the Rotten Tomato .png");
            }

            String audienceImage = pngs.last().toString();
            Image audienceTomatoesPNG;
            if (audienceImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs upright\"></span>")){
                audienceTomatoesPNG = new Image(audienceUprightPNG);
            }else if (audienceImage.equals("<span class=\"mop-ratings-wrap__icon meter-tomato icon big medium-xs spilled\"></span>")) {
                audienceTomatoesPNG = new Image(audienceSpilledPNG);
            }else{
                audienceTomatoesPNG = null;
                JOptionPane.showMessageDialog(null, "Could not locate a URL for the Audience Popcorn .png");
            }

            /*Movie(String title, String year, Image imageOfMovie, String criticConsensus, String criticScoreRotten, Image criticImageTomato,
                    String audienceScoreRotten, Image audienceImagePopcorn, String metascore, String userscore, String average)*/
            Movie movie = new Movie(titleOfMovie, yearOfMovie, movieImg, consensus, criticPercent, criticTomatoesPNG, audiencePercent, audienceTomatoesPNG,
                    meta_metascore + "%", user_metascore + "%", average + "%");

            movieList.put(movie.movieTitle, movie);

            counter++;
            // update progressbar
            final int currentValue = counter;
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        pb.setValue(currentValue);
                    }
                });
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage());
            }
        }
        frame.setVisible(false);
    }

    void databaseCreate() throws SQLException, ClassNotFoundException {

        Movie selectedMovie = movieList.get(movie_Selector_Box.getSelectionModel().getSelectedItem());

        String link = "eglandj";
        String linked = "{f4YbHR6";
        try {
            // create a mysql database connection
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://db4free.net:3306/final_project_4?verifyServerCertificate=false&useSSL=false";
            Class.forName(myDriver);
            System.out.println("Driver Created");

            Connection conn = DriverManager.getConnection(myUrl, link, linked);
            System.out.println("Connection made");

            /*// the mysql insert statement
            String query = " insert into movies (movieTitle, movieYear, movieImage, Consensus, rottenCriticScore, rottenTomatoImage, rottenAudienceScore, " +
                    "rottenAudienceImage, metaScore, metaUserScore, averageScore)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, selectedMovie.movieTitle);
            preparedStmt.setString(2, selectedMovie.movieYear);
            preparedStmt.setString(3, selectedMovie.movieImage.toString());
            preparedStmt.setString(4, selectedMovie.Consensus);
            preparedStmt.setString(5, selectedMovie.rottenCriticScore);
            preparedStmt.setString(6, selectedMovie.rottenTomatoImage.toString());
            preparedStmt.setString(7, selectedMovie.rottenAudienceScore);
            preparedStmt.setString(8, selectedMovie.rottenAudienceImage.toString());
            preparedStmt.setString(9, selectedMovie.metaScore);
            preparedStmt.setString(10, selectedMovie.metaUserScore);
            preparedStmt.setString(10, selectedMovie.averageScore);

            // execute the preparedstatement
            preparedStmt.execute();*/

            conn.close();
            System.out.println("Connection closed.");
        }catch (Exception e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
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
