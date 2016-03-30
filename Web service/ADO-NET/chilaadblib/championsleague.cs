
#region includes
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;
using System.Data;
using System.Data.SqlClient;
#endregion

namespace chilaaadodblib
{
    public class championsleague
    {
         #region Data

        public SqlConnection cnn;

        #endregion

        #region Constructor-Destructor
        public championsleague(String dbname, String uid, String pass)
        {
    
            cnn = new SqlConnection();
            cnn.ConnectionString = String.Format("Data Source=(local);Initial Catalog=ChampionsLeague;Integrated Security=True;User ID={1};Password={2};", dbname, uid, pass);
            cnn.Open();

        }

     ~championsleague()
        {
            try
            {
                if (cnn.State == ConnectionState.Open)
                    cnn.Close();

            }
            catch (Exception e) { Console.WriteLine(e.Message); }
        }
 #endregion

        #region GeneralPurposeDBMethods


        public DataSet RunSelect(SqlConnection cnn, String query)

        {
            DataSet ds = new DataSet();
            try
            {

                SqlDataAdapter da = new SqlDataAdapter();
                SqlCommand cmd = new SqlCommand(query, cnn);

                // Set the command to run
                da.SelectCommand = cmd;
                //fill the data set
                da.Fill(ds);
              
                //return the dataset
            }
            catch (Exception e) { Console.WriteLine(e.Message); }
            return ds;
        }

           public void TransactSQL(String[] sql, SqlConnection cnn)


        {

            SqlCommand cmd = new SqlCommand();
            SqlTransaction trans = cnn.BeginTransaction();

            cmd.Connection = cnn;
            try
            {
                // Assign transaction object for a pending local transaction
                cmd.Transaction = trans;

                for (int i = 0; i < sql.Length; i++)
                {
                    cmd.CommandText = sql[i];
                    cmd.ExecuteNonQuery();
                }
                //Commit the transaction
                trans.Commit();
            }
            catch (InvalidOperationException ex)
            {
                //MessageBox.Show(ex.Message); 
                trans.Rollback();
                throw ex;
            }
            catch (Exception ex)
            {
                //MessageBox.Show(ex.Message);
                trans.Rollback();
                throw ex;
            }

        }
        #endregion
        

        #region DBSpecificMethods

         public String AddPlayerStat(String mid, String pid, int goals){
                String pname = null;
             try{
                 String[] sql;
                 String s = String.Format("Select Team1, Team2 as TeamID from MatchSchedule WHERE MID = '{0}'",mid);
                 DataSet ds = RunSelect(cnn, s);
                 DataTable dt = ds.Tables[0];

                 String tid1 = dt.Rows[0][0].ToString();
                 String tid2 = dt.Rows[0][1].ToString();

                String q = String.Format("SELECT CASE WHEN EXISTS(SELECT * FROM TeamRoster where (TID = '{0}' OR TID = '{1}') AND PID = '{2}') THEN 'true' ELSE 'false' END AS Verify FROM TeamRoster", tid1,tid2,pid);
                ds = RunSelect(cnn, q);

                dt = ds.Tables[0];
                Boolean verify = Convert.ToBoolean(dt.Rows[0][0].ToString());
 
                String r = String.Format("Select TID from TeamRoster where PID = '{0}'",pid);
                ds = RunSelect(cnn, r);

                dt = ds.Tables[0];

                String tid = dt.Rows[0][0].ToString();
                
                 if(verify == true){
                Console.WriteLine("Verified"+verify);
                 sql = new String[2];
                
                     
                         sql[0]=String.Format("Insert Into MatchRoster(MID,PID,Goals) Values ('{0}','{1}',{2})", mid, pid, goals);
                         sql[1]=String.Format("Update TeamRoster Set Goals = (Select sum(Goals) from MatchRoster where PID = '{0}') WHERE PID = '{1}' AND TID = '{2}'", pid, pid, tid);
                      //   sql[2]=String.format("Select Pname from Player where PID = '{0}'",pid);

                     try{
                         TransactSQL(sql,cnn);
                         String p = String.Format("Select Pname from Player where PID = '{0}'",pid);
                         ds = RunSelect(cnn, p);

                         dt = ds.Tables[0];

                         pname = "The stats for" + dt.Rows[0][0].ToString() + "is updated";
                         }
                     catch (Exception ex) { Console.WriteLine(ex.Message); }
                  }
                else{
                pname = "Player did not play the match";

                }
                  
             }
              catch (SqlException ex) {
                    Console.WriteLine(ex.Message);
                } catch (Exception e) {
                    Console.WriteLine(e.Message); 
        
               }
              return pname;
                       
             }
 
       public List<String> ListPlayerStats(String pid) {
              List<String> rv = new List<String>();
             try{
              String a = String.Format("SELECT Pname, Goals FROM Player,TeamRoster WHERE Player.PID = '{0}' AND TeamRoster.PID = '{1}'",pid,pid);
                 
              DataSet ds = RunSelect(cnn, a);
              DataTable dt = ds.Tables[0];

              String line;
              for (int i = 0; i < dt.Rows.Count; i++)
              {

                  String player = dt.Rows[0][0].ToString();
                  int goals = Convert.ToInt32(dt.Rows[0][1].ToString());
                  
                  line = String.Format("{0,-15}\t{1,-1}\t{2,-1}\t{3,-10}", player, "-", goals, "goals");
                  rv.Add(line);
              }
                line = String.Format("{0,-15}\t{1,-20}\t{2,-5}", "Opponent","Date", "Goals");
                rv.Add(line);
                line = String.Format("{0,-15}\t{1,-20}\t{2,-5}", "---------------", "--------------------", "----");
                rv.Add(line);

                String c = String.Format("Select TID from TeamRoster where PID = '{0}'",pid);
                 ds = RunSelect(cnn,c);
                 dt = ds.Tables[0];

                String tid = dt.Rows[0][0].ToString();

               String d = String.Format("Select MID, MDate from MatchSchedule where Team1= '{0}' OR Team2= '{1}'",tid,tid);
                 ds = RunSelect(cnn,d);
                 dt = ds.Tables[0];


                 for (int i = 0; i < dt.Rows.Count; i++)
                 {
                     String mid = dt.Rows[i][0].ToString();
                     String mdate = dt.Rows[i][1].ToString();

                     String e = String.Format("Select Goals from MatchRoster where MID = '{0}' AND PID = '{1}'", mid, pid);
                     ds = RunSelect(cnn, e);
                     DataTable dt1 = ds.Tables[0];

                     int goal = 0;
                     for (int j = 0; j < dt1.Rows.Count; j++)
                     {
                         goal = Convert.ToInt32(dt1.Rows[j][0].ToString());
                     }
                     
                     String f = String.Format("(Select Team1 as TeamID from MatchSchedule where Team2='{0}' AND MID = '{1}')Union All (Select Team2 as TeamID from MatchSchedule where Team1='{2}' AND MID = '{3}')", tid, mid, tid, mid);
                     ds = RunSelect(cnn, f);
                     DataTable dt2 = ds.Tables[0];
                     String teamid = dt2.Rows[0][0].ToString();

                     String g = String.Format("Select TName from Team where TID = '{0}'", teamid);
                     ds = RunSelect(cnn, g);
                     DataTable dt3 = ds.Tables[0];
                     String tname = dt3.Rows[0][0].ToString();

                     line = String.Format("{0,-15}\t{1,-20}\t{2,-5}", tname, mdate, goal);
                     rv.Add(line);

                 }
               } catch (SqlException ex) {
                    Console.WriteLine(ex.Message);
                } catch (Exception e) {
                    Console.WriteLine(e.Message); 
                }
               
               
               return rv;
            }

         public List<String> ListTeamPoints(String tid) {
                List<String> retval = new List<String>();
                try{
                String l = String.Format("Select Tname from Team where TID = '{0}'",tid);
                 
                DataSet ds = RunSelect(cnn, l);
                DataTable dt = ds.Tables[0];
   
                String line = String.Format("{0,-12}\t{1,-3}\t{2,-3}\t{3,-3}\t{4,-3}\t{5,-3}\t{6,-3}\t","Team","W","T","L","G","A","P");
                retval.Add(line);
                line = String.Format("{0,-12}\t{1,-3}\t{2,-3}\t{3,-3}\t{4,-3}\t{5,-3}\t{6,-3}\t", "------------", "---", "---", "---", "---", "---", "---");
                retval.Add(line);

                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    String name = dt.Rows[0][0].ToString();

                    String win = String.Format("Select Count(*) as Won from Match M1,Match M2 where M1.TID = '{0}' AND M1.MID = M2.MID AND M1.Goals>M2.Goals ", tid);

                    ds = RunSelect(cnn, win);
                    dt = ds.Tables[0];
                    String won = dt.Rows[0][0].ToString();

                    String tie = String.Format("Select Count(*) as Ties from Match M1,Match M2 where M1.TID = '{0}' AND M1.MID = M2.MID AND M1.Goals = M2.Goals AND M1.TID<>M2.TID ", tid);

                    ds = RunSelect(cnn, tie);
                    dt = ds.Tables[0];
                    String ties = dt.Rows[0][0].ToString();

                    String loss = String.Format("Select Count(*) as Lost from Match M1,Match M2 where M1.TID = '{0}' AND M1.MID = M2.MID AND M1.Goals<M2.Goals", tid);

                    ds = RunSelect(cnn, loss);
                    dt = ds.Tables[0];
                    String lost = dt.Rows[0][0].ToString();


                    String go = String.Format("Select Sum(Goals) as Goals from TeamRoster where TID = '{0}' ", tid);

                    ds = RunSelect(cnn, go);
                    dt = ds.Tables[0];
                    String goals = dt.Rows[0][0].ToString();

                    String all = String.Format("Select sum(Goals) from MatchRoster where MID IN (Select MID from MatchSchedule where Team1 = '{0}' OR Team2 = '{1}') AND\n" +
   "PID IN (Select PID from TeamRoster where TID IN ((Select Team1 from MatchSchedule WHERE Team2 = '{2}')Union (Select Team2 from MatchSchedule where Team1 = '{3}')))", tid, tid, tid, tid);

                    ds = RunSelect(cnn, all);
                    dt = ds.Tables[0];
                    String goalsall = dt.Rows[0][0].ToString();

                    int points = (Convert.ToInt32(won) * 3) + (Convert.ToInt32(ties) * 1);

                    line = String.Format("{0,-12}\t{1,-3}\t{2,-3}\t{3,-3}\t{4,-3}\t{5,-3}\t{6,-3}\t", name, won, ties, lost, goals, goalsall, points);
                    retval.Add(line);
                }
             } catch (SqlException ex) {
                    Console.WriteLine(ex.Message);
                } catch (Exception e) {
                    Console.WriteLine(e.Message); 
                }
             
             return retval;

         }
        #endregion

    }


        
    }



