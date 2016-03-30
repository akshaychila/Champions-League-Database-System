/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaadblib;

import java.sql.*; //DB
import java.util.*; //for lists
import java.time.LocalDate;
/**
 *
 * @author achila
 */
public class championsleague {
    
    Connection mcn;
    
    public championsleague(String dbname, String uid, String pass) {

        try {

            setConnection(dbname, uid, pass);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    


public void setConnection(String db, String uid, String pass) {
        try {
            //Per microsoft documentation no need to load the driver explicitly. Get connection does that on our behalf.
            // See http://msdn.microsoft.com/en-us/library/ms378526.aspx

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;"
           + "databaseName=" + db + ";user=" + uid + ";password="+ pass + ";";

            mcn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error code:" + ex.getErrorCode());//ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public boolean IsConnected() {
        return (mcn != null ? true : false);
    }

    private String fixStringFromJDBC(String s) {
        if (s == null) {
            s = "";
        }
        if (s.equals(" ")) {
            s = "";
        }
        s = s.replace('`', '\'');
        return s;
    }

   /* private void TransactSQL(String[] sql) {
        
        int n = 0;
        try {
            
            //System.out.println(mcn.getAutoCommit());
            mcn.setAutoCommit(false);
            PreparedStatement pst = null;

            for (int i = 0; i < sql.length; i++) {
                 pst = mcn.prepareStatement(sql[i]);
                 pst.executeQuery();
            }
            //mcn.rollback();
            mcn.commit();
        } catch (SQLException ex) {
            try {
                //rollback if there is an error
                mcn.rollback();
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
    }*/
    
    public String AddPlayerStat(String mid, String pid, int goals){
         String pname = null;
        try{
          String sql[] = null; 
          
        PreparedStatement pst = mcn.prepareStatement("Select Team1, Team2 as TeamID from MatchSchedule WHERE MID = ?");
            pst.setString(1, mid);
            
            ResultSet rs = pst.executeQuery();
            rs.next();
            String tid1 = rs.getString(1);
            String tid2 = rs.getString(2);
          
        PreparedStatement pst1 = mcn.prepareStatement(
                "SELECT CASE WHEN EXISTS(SELECT * FROM TeamRoster where (TID = ? OR TID = ?) AND PID = ?) THEN 'true' ELSE 'false' END AS Verify FROM TeamRoster ");
                    
 
        
        pst1.setString(1,tid1);
        pst1.setString(2,tid2);
        pst1.setString(3,pid);
        ResultSet r = pst1.executeQuery();
        r.next();
        Boolean verify = r.getBoolean(1);
        //System.out.println(verify);
        PreparedStatement ps = mcn.prepareStatement("Select TID from TeamRoster where PID = ?");
        ps.setString(1, pid);
        ResultSet r1 = ps.executeQuery();
        r1.next();
        String tid = r1.getString(1);
        if(verify = true){
              System.out.println("Verified"+verify);
        //  sql = new String[3];
              try{
              mcn.setAutoCommit(false);
            PreparedStatement s1 = mcn.prepareStatement("Insert Into MatchRoster(MID,PID,Goals) Values (?,?,?)");
            s1.setString(1, mid);
            s1.setString(2, pid);
            s1.setInt(3, goals);
            PreparedStatement s2 = mcn.prepareStatement("Update TeamRoster Set Goals = (Select sum(Goals) from MatchRoster where PID = ?) WHERE PID = ? AND TID = ?");
            s2.setString(1, pid);
          //  s2.setInt(1, goals);
            s2.setString(2, pid);
            s2.setString(3, tid);
            PreparedStatement s3 = mcn.prepareStatement("Select Pname from Player where PID = ?");
            s3.setString(1, pid);
            s1.execute();
            s2.executeUpdate();
           ResultSet r3 = s3.executeQuery();
           r3.next();
          pname = "The stats for "+r3.getString(1)+" is updated";
          
               mcn.commit();
        } catch (SQLException ex) {
            try {
                //rollback if there is an error
                mcn.rollback();
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
           
         //   sql[0]=String.format("Insert Into MatchRoster(MID,PID,Goals) Values ('%s','%s',%d);", mid, pid, goals);
          //  sql[1]=String.format("Update TeamRoster Set Goals = %d WHERE PID = '%s' AND TID = '%s';", goals, pid, tid);
           // sql[2]=String.format("Select Pname from Player where PID = '%s';",pid);
         //  TransactSQL(sql);
        }
        else{
            pname = "Player did not play the match";
        }
        
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        
        } catch (Exception e) {
            e.printStackTrace();
            
       
        }
       return pname;
    }
    
    public List<String> ListPlayerStats(String pid) {
        List<String> rv = new ArrayList<String>();
        try {

            //CallableStatement st = mcn.prepareCall("{call GetPurchaseRequests(?) }");
/*            
             Statement s = mcn.createStatement();
             String sql = 
             String.format("SELECT Amount, Appcode, CodeType FROM PurchaseRequests WHERE AccountNo='%s'", accno)
             */
            PreparedStatement st = mcn.prepareStatement(
                    "SELECT Pname, Goals FROM Player,TeamRoster WHERE Player.PID = ? AND TeamRoster.PID = ?;");
            st.setString(1, pid);
            st.setString(2, pid);

            ResultSet rs = st.executeQuery();
            String line;
            while (rs.next()) {

                String player = rs.getString(1);
                int goals = rs.getInt(2);
          
                
                line = String.format("%-15s\t%-1s\t%-1d\t%-10s", player,"-", goals,"goals");
                rv.add(line);
            }
                line = String.format("%-15s\t%-20s\t%-5s", "Opponent","Date", "Goals");
                rv.add(line);
                line = String.format("%-15s\t%-20s\t%-5s", "---------------","--------------------", "----");
                rv.add(line);
            
            PreparedStatement s = mcn.prepareStatement("Select TID from TeamRoster where PID = ?");
            // Execute the query and store the results in a resultset object
            s.setString(1,pid);
            ResultSet r = s.executeQuery();
            r.next();
            String tid = r.getString(1);
            
            PreparedStatement s1 = mcn.prepareStatement("Select MID, MDate from MatchSchedule where Team1=? OR Team2=?") ;
            s1.setString(1, tid);
            s1.setString(2, tid);
            ResultSet r1 = s1.executeQuery();
            while(r1.next()){
                String mid = r1.getString(1);
                String mdate= r1.getString(2);
                
             PreparedStatement s2 = mcn.prepareStatement("Select Goals from MatchRoster where MID = ? AND PID =? ");
             s2.setString(1, mid);
             s2.setString(2, pid);
             ResultSet r2 = s2.executeQuery();
             
             int goals=0;
             while(r2.next()){
             goals = r2.getInt(1);
             }
             PreparedStatement s3 = mcn.prepareStatement("(Select Team1 as TeamID from MatchSchedule where Team2=? AND MID = ?)Union All (Select Team2 as TeamID from MatchSchedule where Team1=? AND MID = ?)");
             s3.setString(1, tid);
             s3.setString(2, mid);
             s3.setString(3, tid);
             s3.setString(4, mid);
             ResultSet r3 = s3.executeQuery();
             r3.next();
             String teamid = r3.getString(1);
             
             PreparedStatement s4 = mcn.prepareStatement("Select TName from Team where TID = ?");
             s4.setString(1, teamid);
             ResultSet r4 = s4.executeQuery();
             r4.next();
             String tname= r4.getString(1);
             
             line = String.format("%-15s\t%-10s\t%-5d", tname, mdate,goals);
                rv.add(line);
            }
            
         /*   PreparedStatement s1 = mcn.prepareStatement("Select Team1, Team2 From MatchSchedule where Team1 = ? OR Team2 = ?");
            s1.setString(1, tid);
            s1.setString(2, tid);
            ResultSet r1 = s1.executeQuery();
           while(r1.next()){
            String tid1= r1.getString(1);
            String tid2= r1.getString(2);
      //      System.out.println(tid1);
       //     System.out.println(tid2);
       //     System.out.println("tid -----" +tid);
            
            String t = null;
            if(tid.equals(tid1)){
                t= tid2;
            }
            else if (tid.equals(tid2)){
                t = tid1;
            }
       //     System.out.println("t ===" +t);
            PreparedStatement s2 = mcn.prepareStatement("Select TName,TID From Team where TID = ? ");
            s2.setString(1, t);
            ResultSet r2 = s2.executeQuery();
       //     System.out.println("before while r2");
            while(r2.next()){
       //         System.out.println("in while r2");
            String tname = r2.getString(1);
            String ti = r2.getString(2);
            
            PreparedStatement s3 = mcn.prepareStatement("Select MID, MDate from MatchSchedule where (Team1=? AND Team2=?) OR (Team1=? AND Team2 = ?)");
            s3.setString(1, tid);
            s3.setString(2, ti);
            s3.setString(3, ti);
            s3.setString(4, tid);
            ResultSet r3 = s3.executeQuery();
            while(r3.next()){
         //       System.out.println("before while r3");
            String mid  = r3.getString(1);
            String mdate = r3.getString(2);
            
            PreparedStatement s4 = mcn.prepareStatement("Select Goals from MatchRoster where MID=?");
            s4.setString(1, mid);
            //s4.setString(2, tid);
            //s4.setString(3, tid);
            ResultSet r4 = s4.executeQuery();
            while(r4.next()){
         //       System.out.println("before while r4");
                int goals = r4.getInt(1);
                line = String.format("%-15s\t%-10s\t%-5d", tname, mdate,goals);
                rv.add(line);
                //System.out.println(line);
           
            }
            }
            }
            }*/
           
            
            

            // now display the output
           /* ResultSetMetaData rsmd = rs.getMetaData();

            int ncols = rsmd.getColumnCount();
            String header = "";
            for (int i = 1; i <= ncols; i++) {
                header += rsmd.getColumnName(i) + "   ";
            }
            rv.add(header);*/
            

            

            // close everything
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
        }
    
    public List<String> ListTeamPoints(String tid) {
        List<String> retval = new ArrayList<String>();
        try {
            
            PreparedStatement pst = mcn.prepareStatement("Select Tname from Team where TID = ?");
            pst.setString(1, tid);
            ResultSet res = pst.executeQuery();
            String line = String.format("%-12s\t%-3s\t%-3s\t%-3s\t%-3s\t%-3s\t%-3s\t","Team","W","T","L","G","A","P");
            retval.add(line);
            line = String.format("%-12s\t%-3s\t%-3s\t%-3s\t%-3s\t%-3s\t%-3s\t","------------","---","---","---","---","---","---");
            retval.add(line);
            
            while(res.next()){
                String name = res.getString(1);
                
                PreparedStatement pst1 =mcn.prepareStatement("Select Count(*) as Won from Match M1,Match M2 where M1.TID = ? AND M1.MID = M2.MID AND M1.Goals>M2.Goals ");
                pst1.setString(1, tid);
                ResultSet res1 = pst1.executeQuery();
                res1.next();
                    String won = res1.getString(1);
            //        System.out.print(won);
                
                PreparedStatement pst2 =mcn.prepareStatement("Select Count(*) as Ties from Match M1,Match M2 where M1.TID = ? AND M1.MID = M2.MID AND M1.Goals = M2.Goals AND M1.TID<>M2.TID ");
                pst2.setString(1, tid);
                ResultSet res2 = pst2.executeQuery();
                res2.next();
                    String ties = res2.getString(1);
              //      System.out.print(ties); 
                    
                PreparedStatement pst3 =mcn.prepareStatement("Select Count(*) as Lost from Match M1,Match M2 where M1.TID = ? AND M1.MID = M2.MID AND M1.Goals<M2.Goals ");
                pst3.setString(1, tid);
                ResultSet res3 = pst3.executeQuery();
                res3.next();
                    String lost = res3.getString(1);
                //    System.out.print(lost);
                
                PreparedStatement pst4 =mcn.prepareStatement("Select Sum(Goals) as Goals from TeamRoster where TID = ?; ");
                pst4.setString(1, tid);
                ResultSet res4 = pst4.executeQuery();
                res4.next();
                    String goals = res4.getString(1);
             //       System.out.print(goals); 
                    
                PreparedStatement pst5 =mcn.prepareStatement("Select sum(Goals) from MatchRoster where MID IN (Select MID from MatchSchedule where Team1 = ? OR Team2 = ?) AND\n" +
"PID IN (Select PID from TeamRoster where TID IN ((Select Team1 from MatchSchedule WHERE Team2 = ?)Union (Select Team2 from MatchSchedule where Team1 = ?)))");
                pst5.setString(1, tid);
                pst5.setString(2, tid);
                pst5.setString(3, tid);
                 pst5.setString(4, tid);
                
                ResultSet res5 = pst5.executeQuery();
                res5.next();
                    String goalsall = res5.getString(1);
            //        System.out.print(goalsall); 
                    
               
                    int points = (Integer.parseInt(won)*3) + (Integer.parseInt(ties)*1) ;
            //        System.out.print(points); 
                    
                    line = String.format("%-12s\t%-3s\t%-3s\t%-3s\t%-3s\t%-3s\t%-3s\t", name,won, ties,lost,goals,goalsall,points);
                    retval.add(line);
                    
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
        }
        
    
    
    
}