package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Controller {

    private static final String link = "eglandj";
    private static final String linked = "{f4YbHR6";

    private static final String myDriver = "com.mysql.jdbc.Driver";
    private static final String myUrl = "jdbc:mysql://db4free.net:3306/final_project_4?verifyServerCertificate=false&useSSL=false";

    HashMap<String, Movie> movieList;
    List<String> movieTitleList;
    String optionMovie;

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
    private Button create_Button;

    @FXML
    private Button update_Button;

    @FXML
    private Button delete_button;

    @FXML
    private TableView<Movie> movie_Table;
        @FXML
        private TableColumn<Movie, String> movie_title_col;

        @FXML
        private TableColumn<Movie, String> movie_year_col;

        @FXML
        private TableColumn<Movie, String> critic_col;

        @FXML
        private TableColumn<Movie, String> audience_col;

        @FXML
        private TableColumn<Movie, String> metascore_col;

        @FXML
        private TableColumn<Movie, String> user_col;

        @FXML
        private TableColumn<Movie, String> average_col;

    @FXML
    public void createButton(ActionEvent actionEvent) {
        databaseCreate();
        showButtons(optionMovie);
        movie_Selector_Box.setValue(null);
    }

    @FXML
    void updateButton(ActionEvent event) {
        databaseUpdate();
        showButtons(optionMovie);
        movie_Selector_Box.setValue(null);
    }

    @FXML
    public void deleteButton(ActionEvent actionEvent) {
        databaseDelete();
        showButtons(optionMovie);
        movie_Selector_Box.setValue(null);
    }

    @FXML
    void movie_Selected(ActionEvent event) {
        if(movie_Selector_Box.getValue() != null){
            clear();
            optionMovie = populateSelection();
            showButtons(optionMovie);
        }else{
            clear();
        }
    }

    public void initialize() throws IOException {
        fillTable(null);
        movieCreator();
        SortedSet<String> keys = new TreeSet<>(movieList.keySet());
        movie_Selector_Box.getItems().addAll(keys);
        clear();
    }

    String populateSelection(){

        Movie selectedMovie = movieList.get(movie_Selector_Box.getSelectionModel().getSelectedItem());
        movie_Title.setText(selectedMovie.movieTitle.getValue() + "\n" + "(" + selectedMovie.movieYear.getValue() + ")");
        movie_Image.setImage(selectedMovie.movieImage);
        consensus_Text_Label.setText(selectedMovie.getConsensus().getValue());
        critic_Percentage_Label.setText(selectedMovie.getRottenCriticScore().getValue());
        critic_Score_Image.setImage(selectedMovie.rottenTomatoImage);
        audience_Percentage_Label.setText(selectedMovie.getRottenAudienceScore().getValue());
        audience_Score_Image.setImage(selectedMovie.rottenAudienceImage);
        metascore_Label.setText(selectedMovie.getMetaScore().getValue());
        user_score_Label.setText(selectedMovie.getMetaUserScore().getValue());
        average_Percentage_Label.setText(selectedMovie.getAverageScore().getValue());

        return selectedMovie.getMovieTitle().getValue();
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

            movieList.put(movie.getMovieTitle().getValue(), movie);

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

    void databaseCreate(){

        Movie selectedMovie = movieList.get(movie_Selector_Box.getSelectionModel().getSelectedItem());

        try {
            Connection conn = DriverManager.getConnection(myUrl, link, linked);
            System.out.println("Connection made");

            // the mysql insert statement
            String query = "INSERT INTO movies (movieTitle, movieYear, Consensus, rottenCriticScore, rottenAudienceScore, " +
                    "metaScore, metaUserScore, averageScore)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, selectedMovie.getMovieTitle().getValue());
            preparedStmt.setString(2, selectedMovie.getMovieYear().getValue());
            preparedStmt.setString(3, selectedMovie.getConsensus().getValue());
            preparedStmt.setString(4, selectedMovie.getRottenCriticScore().getValue());
            preparedStmt.setString(5, selectedMovie.getRottenAudienceScore().getValue());
            preparedStmt.setString(6, selectedMovie.getMetaScore().getValue());
            preparedStmt.setString(7, selectedMovie.getMetaUserScore().getValue());
            preparedStmt.setString(8, selectedMovie.getAverageScore().getValue());

            // execute the preparedstatement
            preparedStmt.execute();
            System.out.println("Record Created");

            fillTable(conn);

            conn.close();
            System.out.println("Connection closed.");
        }catch (Exception e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }
    void databaseUpdate(){
        Movie selectedMovie = movieList.get(movie_Selector_Box.getSelectionModel().getSelectedItem());

        try {
            Connection conn = DriverManager.getConnection(myUrl, link, linked);
            System.out.println("Connection made");

            // the mysql insert statement
            /*UPDATE `movies` SET `movieTitle`=[value-1],`movieYear`=[value-2],`Consensus`=[value-3],
            `rottenCriticScore`=[value-4],`rottenAudienceScore`=[value-5],`metaScore`=[value-6],
            `metaUserScore`=[value-7],`averageScore`=[value-8] WHERE 1*/
            String query = "UPDATE movies SET movieTitle=?, movieYear=?,Consensus=?,rottenCriticScore=?,rottenAudienceScore=?," +
                    "metaScore=?,metaUserScore=?,averageScore=? WHERE movieTitle=?";

            // create the mysql insert prepared statement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, selectedMovie.getMovieTitle().getValue());
            preparedStmt.setString(2, selectedMovie.getMovieYear().getValue());
            preparedStmt.setString(3, selectedMovie.getConsensus().getValue());
            preparedStmt.setString(4, selectedMovie.getRottenCriticScore().getValue());
            preparedStmt.setString(5, selectedMovie.getRottenAudienceScore().getValue());
            preparedStmt.setString(6, selectedMovie.getMetaScore().getValue());
            preparedStmt.setString(7, selectedMovie.getMetaUserScore().getValue());
            preparedStmt.setString(8, selectedMovie.getAverageScore().getValue());
            preparedStmt.setString(9, selectedMovie.getMovieTitle().getValue());

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            System.out.println("Record Updated");

            fillTable(conn);

            conn.close();
            System.out.println("Connection closed.");
        }catch (Exception e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }

    void databaseDelete(){

        Movie selectedMovie = movieList.get(movie_Selector_Box.getSelectionModel().getSelectedItem());
        try {
            Connection conn = DriverManager.getConnection(myUrl, link, linked);
            System.out.println("Connection made");

            String sql = "DELETE FROM movies WHERE movieTitle = " + "'" + selectedMovie.getMovieTitle().getValue() + "'";

            PreparedStatement preparedStmt = conn.prepareStatement(sql);

            // execute the preparedstatement
            preparedStmt.executeUpdate();

            System.out.println("Record Deleted");

            fillTable(conn);

            conn.close();
            System.out.println("Connection closed.");

        }catch (Exception e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    void fillTable(Connection conn){

        ObservableList<Movie> databaseMovieList = FXCollections.observableArrayList();
        String title ="", year="", consensus="", criticScore="", audienceScore="", metascore="", userScore="", average="";

        movie_title_col.setCellValueFactory(cellData -> cellData.getValue().getMovieTitle());
        movie_year_col.setCellValueFactory(cellData -> cellData.getValue().getMovieYear());
        critic_col.setCellValueFactory(cellData -> cellData.getValue().getRottenCriticScore());
        audience_col.setCellValueFactory(cellData -> cellData.getValue().getRottenAudienceScore());
        metascore_col.setCellValueFactory(cellData -> cellData.getValue().getMetaScore());
        user_col.setCellValueFactory(cellData -> cellData.getValue().getMetaUserScore());
        average_col.setCellValueFactory(cellData -> cellData.getValue().getAverageScore());

        Statement sql_statement = null; ResultSet rs1 = null;

        if(conn == null){
            try {
                conn = DriverManager.getConnection(myUrl, link, linked);
                System.out.println("Connection made");

                String getMoviesQuery = "SELECT movieTitle, movieYear, Consensus, rottenCriticScore, rottenAudienceScore, metaScore, " +
                        "metaUserScore, averageScore FROM movies";

                // create the java statement
                sql_statement = conn.createStatement();
                rs1 = sql_statement.executeQuery(getMoviesQuery);

                //Iterate through database and grab all records
                while(rs1.next()) {

                    title = rs1.getString(1);
                    year = rs1.getString(2);
                    consensus = rs1.getString(3);
                    criticScore = rs1.getString(4);
                    audienceScore = rs1.getString(5);
                    metascore = rs1.getString(6);
                    userScore = rs1.getString(7);
                    average = rs1.getString(8);

                    databaseMovieList.add(new Movie(title, year, consensus, criticScore, audienceScore, metascore, userScore, average));

                }//End of while
                movie_Table.setItems(databaseMovieList);
                movie_Table.getSortOrder().add(movie_title_col);
                System.out.println("Table Created");

                conn.close();
                System.out.println("Connection closed.");

            }catch (Exception e){
                System.err.println("Connection was null & Got an exception!");
                System.err.println(e.getMessage());
            }
        }else{
            try{
                String getMoviesQuery = "SELECT movieTitle, movieYear, Consensus, rottenCriticScore, rottenAudienceScore, metaScore, " +
                        "metaUserScore, averageScore FROM movies";

                // create the java statement
                sql_statement = conn.createStatement();
                rs1 = sql_statement.executeQuery(getMoviesQuery);

                //Iterate through database and grab all records
                while(rs1.next()) {

                    title = rs1.getString(1);
                    year = rs1.getString(2);
                    consensus = rs1.getString(3);
                    criticScore = rs1.getString(4);
                    audienceScore = rs1.getString(5);
                    metascore = rs1.getString(6);
                    userScore = rs1.getString(7);
                    average = rs1.getString(8);

                    databaseMovieList.add(new Movie(title, year, consensus, criticScore, audienceScore, metascore, userScore, average));

                }//End of while
                movie_Table.setItems(databaseMovieList);
                movie_Table.getSortOrder().add(movie_title_col);
                System.out.println("Table Created");

            }catch (Exception e){
                System.err.println("Connection was not null & Got an exception!");
                System.err.println(e.getMessage());
            }
        }


    }

    void showButtons(String option){
        for(Movie title : movie_Table.getItems()){
            String output = title.getMovieTitle().getValue();
            if(option.equals(output)){
                System.out.println("Exists");
                create_Button.setVisible(false);
                update_Button.setVisible(true);
                delete_button.setVisible(true);
                break;
            }else{
                System.out.println("Does not exist");
                create_Button.setVisible(true);
                update_Button.setVisible(false);
                delete_button.setVisible(false);
            }
        }
        critic_Consensus_Label.setVisible(true);
    }
    void clear(){
        create_Button.setVisible(false);
        update_Button.setVisible(false);
        delete_button.setVisible(false);
        movie_Title.setText(null);
        movie_Image.setImage(null);
        consensus_Text_Label.setText(null);
        critic_Percentage_Label.setText(null);
        critic_Score_Image.setImage(null);
        audience_Percentage_Label.setText(null);
        audience_Score_Image.setImage(null);
        metascore_Label.setText(null);
        user_score_Label.setText(null);
        average_Percentage_Label.setText(null);
        critic_Consensus_Label.setVisible(false);
    }
}
