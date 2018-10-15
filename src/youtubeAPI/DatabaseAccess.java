package youtubeAPI;
import java.sql.*;
import java.util.Properties;

public class DatabaseAccess {
    private static Connection db;

    public static void DatabaseSetup() {
        try
        {
            Properties props = new Properties();
            props.setProperty("user","postgres");
            props.setProperty("password","appsandorgs");
            db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/forticlassifier", props);
            
            createTables();

       //     String video_name = "https://www.youtube.com/watch?v=vUtjt13k9m4";
       //     System.out.println(video_name.substring(video_name.length() - 11));
       //     this.putVideoEntry("youtube",video_name.substring(video_name.length() - 11));
   
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void createTables(){ // check if tables already exist
        // creates the tables in proper order to prevent PK, FK conflicts
        createVideosTable();
    }

    public static void dropTables(){
        try
        {
            String sql = "DROP TABLE IF EXISTS Videos CASCADE;";
            Statement st = db.createStatement();
            st.execute(sql);
            st.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void emptyTables(){
        try
        {
            String sql = "TRUNCATE Videos CASCADE;";
            Statement st = db.createStatement();
            st.execute(sql);
            st.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void createVideosTable(){
        try{
            String sqlS = "CREATE TABLE IF NOT EXISTS Videos\n"
                    + "(\n"
                    + "video_type character varying(20) NOT NULL,\n"
                    + "video_id character varying(11) NOT NULL UNIQUE,\n"
                    + "video_classification character varying(25),\n"
                    + "access_count int NOT NULL\n"
                    + ") WITH (OIDS = FALSE);"
                    + "ALTER TABLE Videos " +
                    "OWNER TO postgres;";
            Statement st = db.createStatement();
            st.execute(sqlS);
            st.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // add logic for videoType finding and videoId elsewhere
    public static void putVideoEntry(String videoType, String videoId){
        try{
            String sql = "INSERT INTO Videos\n" +
                    "(video_type, video_id, access_count, video_classification) VALUES \n" +
                    "(?,?,0,'NA');\n";
            PreparedStatement st = db.prepareStatement(sql);
            st.setString(1,videoType);
            st.setString(2,videoId);
            st.execute();
            st.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void incrementAccessCount(String videoType, String videoId){
        try{
            String sql = "UPDATE Videos\n" +
                    "SET access_count = access_count + 1 \n" +
                    "WHERE video_id = ? AND video_type = ?;\n";
            PreparedStatement st = db.prepareStatement(sql);
            st.setString(1,videoId);
            st.setString(2,videoType);
            st.execute();
            st.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateVideoEntry(String videoClassification, String videoType,
                                 String videoId){
        try
        {
            String sql = "UPDATE Videos\n" +
                    "SET video_classification = ? " +
                    "WHERE video_id = ? AND video_type = ?;\n";
            PreparedStatement st = db.prepareStatement(sql);
            st.setString(1, videoClassification);
            st.setString(2, videoId);
            st.setString(3, videoType);
            st.execute();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // if NA, then not existent
    public static String getVideoClassification(String videoType, String videoId){
        int i = 0;  String videoClassification = "NA";
        try
        {
            String sql1 = "SELECT *" +
                    " FROM Videos WHERE video_type = ? " +
                    "AND video_id = ?;\n";
            PreparedStatement st1 = db.prepareStatement(sql1);
            st1.setString(1, videoType);
            st1.setString(2, videoId);
            ResultSet rs1 = st1.executeQuery();
            incrementAccessCount(videoType,videoId);

            while (rs1.next()){
               videoClassification = rs1.getString("video_classification");
                i += 1;
                incrementAccessCount(videoType, videoId); // increments if found
            }
            if (i == 0)
            {
                //System.out.println("Video not found: " + videoType + " " + videoId);
            }

            rs1.close();
            st1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videoClassification;
    }

    public static int getVideoAccessCount(String videoType, String videoId){
        int i = 0;  int accessCount = 0;
        try
        {
            String sql1 = "SELECT *" +
                    " FROM Videos WHERE video_type = ? " +
                    "AND video_id = ?;\n";
            PreparedStatement st1 = db.prepareStatement(sql1);
            st1.setString(1, videoType);
            st1.setString(2, videoId);
            ResultSet rs1 = st1.executeQuery();

            while (rs1.next()){
                accessCount = rs1.getInt("access_count");
                incrementAccessCount(videoType,videoId);
                i += 1;
            }
            if (i == 0)
            {
                System.out.println("Video not found: " + videoType + " " + videoId);
            }

            rs1.close();
            st1.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return accessCount;
    }
}