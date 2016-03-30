/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg2server;

/**
 *
 * @author achila
 */
import chilaadblib.championsleague;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChampionsLeagueRemoteObject extends UnicastRemoteObject implements IChampionsLeague {
    
    championsleague db;

    public ChampionsLeagueRemoteObject() throws RemoteException {
        super();
        db = new championsleague("ChampionsLeague", "ism6236", "ism6236bo");
    }
    
    public String AddPlayerStat(String mid, String pid, int goals)throws RemoteException{
      return db.AddPlayerStat(mid, pid, goals);
    }
     public List<String> ListPlayerStats(String pid) throws RemoteException {
       return db.ListPlayerStats(pid);
     }
     public List<String> ListTeamPoints(String tid) throws RemoteException {
        return db.ListTeamPoints(tid);
     }
    
}
