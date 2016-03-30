/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg3server;

import java.util.List;
import javax.ejb.Stateless;
import chilaadblib.championsleague;

/**
 *
 * @author achila
 */
@Stateless
public class ChampionsLeague implements ChampionsLeagueRemote {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    championsleague db;

    public ChampionsLeague() {
        super();
        db = new championsleague("ChampionsLeague", "ism6236", "ism6236bo");
    }
    
    public String AddPlayerStat(String mid, String pid, int goals){
      return db.AddPlayerStat(mid, pid, goals);
    }
     public List<String> ListPlayerStats(String pid) {
       return db.ListPlayerStats(pid);
     }
     public List<String> ListTeamPoints(String tid){
        return db.ListTeamPoints(tid);
     }
    
    
}
