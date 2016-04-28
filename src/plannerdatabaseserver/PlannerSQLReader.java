/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plannerdatabaseserver;

/**
 *
 * @author Chris
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author Chris
 */
public class PlannerSQLReader {
    
    //ArrayList<Score> scores;
    private final String dataBaseURL;
    
    public PlannerSQLReader(String dataBaseURL){
        this.dataBaseURL = dataBaseURL;
    }
    
    public PlannerSQLReader(){
        dataBaseURL = "localhost:3306";
    }
    
    public static void main(String[] args) {
        PlannerSQLReader reader = new PlannerSQLReader();
        //reader.getEvents();
//        reader.fillWithRandomScores(60);
//        reader.getScores();
//        String name = "";
//        int score ++6= -1;
//        //reader.saveScoreByInput(name, score);
//        reader.saveScore("MRD", 50000);
//        reader.getScores();
//        System.out.println("top ten");
//        reader.getTopTenScores();
//        System.out.println("highest score");
//        reader.getHighScore();
        
        
    }
    
    private Connection connectToDataBase() throws SQLException{
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
            System.out.println("cant connect because of driver");
        }
        Connection conn = null;
        String databaseName = "planner_events";
        String url = "jdbc:mysql://"+ dataBaseURL +"/" + databaseName +"?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "hoffman";
          conn = DriverManager.getConnection(url, username, password);
        //System.out.println("Successful connection.");
        return conn;
    }
    
    public void createDatabase(String username){
        try {
            Connection conn = connectToDataBase();
            Statement m_Statement = conn.createStatement();
            String query = "CREATE table " + username + "_events( "+
                                "event_name varchar(300) NOT NULL, "+
                                "event_frequency varchar(60) NOT NULL,"+
                                "event_duration INT NOT NULL,"+
                                "initial_event_duration INT NOT NULL,"+
                                "event_finished varchar(30) NOT NULL,"+
                                "event_order INT);";
            m_Statement.execute(query);
            System.out.println("Table " + username + " created");
        } catch (SQLException ex) {
            Logger.getLogger(PlannerSQLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addToDatabase(String username, List<String> input){
        try {
            Connection conn = connectToDataBase();
            Statement m_Statement = conn.createStatement();
            String query = "INSERT INTO " + username + "_events (event_name, event_frequency, event_duration, initial_event_duration, event_finished, event_order) " +
                           "VALUES ('" + input.get(0) + "', '" + input.get(1) + "', " + input.get(2) + ", " + input.get(3) + ", '" + input.get(4) + "', " + input.get(5) + "); ";
            m_Statement.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(PlannerSQLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getEvents(String username){
        String result = "";
        try {
            // Do something with the Connection
            Connection conn = connectToDataBase();
            Statement m_Statement = conn.createStatement();
            String query = "SELECT * FROM "+ username + "_events";
            ResultSet m_ResultSet = m_Statement.executeQuery(query);
            while (m_ResultSet.next()) {
                result += "event ";
                 // scores.add(new Score(m_ResultSet.getString(1), Integer.parseInt(m_ResultSet.getString(2)), m_ResultSet.getString(3)));
                //System.out.println(m_ResultSet.getString(1) + " " + m_ResultSet.getString(2));
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            result = "error";
            ex.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
    
    public String doesUsernameExist(String username){
        
        try {
            Connection conn = connectToDataBase();
            Statement m_Statement = conn.createStatement();
            String query = "SELECT * FROM "+ username + "_events";
            m_Statement.executeQuery(query);
            return Boolean.toString(true);
        } catch (SQLException ex) {
            return Boolean.toString(false);
        }
        
    }
    
//    public void saveScoreByInput(String name, int score){
//        boolean tryAgain;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        
//        do{
//            String result = JOptionPane.showInputDialog("enter name to add"); 
//            if(result != null){
//                name = result.toUpperCase();
//            }
//        }while(name.length()!= 3 || name.equals(""));
//           
//        do{
//            try{
//                score = Integer.parseInt(JOptionPane.showInputDialog("enter score to add"));
//                tryAgain = false;
//            }catch(NumberFormatException e){
//                tryAgain = true;
//                JOptionPane.showMessageDialog(null, "Invalid input");
//            }
//        }while(tryAgain);
//        saveScore(name, score, dateFormat.format(date));
//        
//    }
    
//    public void saveScore(String name, int score, String date){
//        try {
//            // Do something with the Connection
//            Connection conn = connectToDataBase();
//            Statement m_Statement = conn.createStatement();
//            m_Statement.executeUpdate("INSERT INTO high_scores " + "VALUES ('"+ name + "', " + score + ", '"+ date +"')");
//            
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }
//    }
//    
//    public void saveScore(String name, int score){
//        try {
//            // Do something with the Connection
//            Connection conn = connectToDataBase();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date date = new Date();
//            Statement m_Statement = conn.createStatement();
//            m_Statement.executeUpdate("INSERT INTO high_scores " + "VALUES ('"+ name + "', " + score + ", '"+ dateFormat.format(date) +"')");
//            
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }
//    }
//    
//    public void fillWithRandomScores(int numberOfScores){
//        //int ij = (int)(Math.random() * (max - min) + min);
//        //90000
//        Random rand = new Random();
//        for (int i = 0; i < numberOfScores; i++) {
//            int score = (rand.nextInt((90/*000*/) + 1))*100;
//            char char1 = (char)(rand.nextInt((90 - 65) + 1) + 65);
//            char char2 = (char)(rand.nextInt((90 - 65) + 1) + 65);
//            char char3 = (char)(rand.nextInt((90 - 65) + 1) + 65);
//            String name = "" + char1 + char2 + char3;
//            saveScore(name,score);
//        }
//    }
}


