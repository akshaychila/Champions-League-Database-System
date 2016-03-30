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
import java.rmi.registry.*;

public class ChampionsLeagueServer {

    public ChampionsLeagueServer(){
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
         LocateRegistry.createRegistry(1099);
    IChampionsLeague icl = new ChampionsLeagueRemoteObject();

    // Specify the name of the object
    //rmi://host:port/name
    String servname = "rmi://localhost/ChampionsLeague";// default port is 1099

    //rebind the object. Binding does not alter the registry even if this is a new object so
    // rebind is used to guarantee the latest version
    Naming.rebind(servname, icl);
    System.out.println("C/L Service Started");

    }
    
}
