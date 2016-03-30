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
import java.rmi.*;
import java.util.*;

public interface IChampionsLeague extends Remote {
    
     public String AddPlayerStat(String mid, String pid, int goals)throws RemoteException;
     public List<String> ListPlayerStats(String pid) throws RemoteException;
     public List<String> ListTeamPoints(String tid) throws RemoteException;
    
}
