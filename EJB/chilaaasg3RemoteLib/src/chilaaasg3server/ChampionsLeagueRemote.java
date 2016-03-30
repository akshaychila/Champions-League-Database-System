/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg3server;

import javax.ejb.Remote;
import java.util.List;

/**
 *
 * @author achila
 */
@Remote
public interface ChampionsLeagueRemote {
    
    public String AddPlayerStat(String mid, String pid, int goals);
     public List<String> ListPlayerStats(String pid);
     public List<String> ListTeamPoints(String tid);
    
}
