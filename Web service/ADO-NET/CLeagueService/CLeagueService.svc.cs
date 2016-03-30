using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using chilaaadodblib;

namespace CLeagueService
{
    public class CLService : ICLeagueService
    {
        championsleague db;

        public CLService(){
          db = new championsleague("ChampionsLeague","ism6236","ism6236bo");
        }

        public String AddPlayerStat(String mid, String pid, int goals)
        {
            return db.AddPlayerStat(mid,pid,goals);
        }


        
        public List<String> ListPlayerStats(String pid)
        {
            return db.ListPlayerStats(pid);
        }

       
        public List<String> ListTeamPoints(String tid)
        {
            return db.ListTeamPoints(tid);
        }

       
    }
}
