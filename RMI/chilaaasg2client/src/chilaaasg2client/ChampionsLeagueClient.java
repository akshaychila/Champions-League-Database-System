/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg2client;

/**
 *
 * @author achila
 */
import java.rmi.RemoteException;
import java.rmi.Naming;
import chilaaasg2server.*; //this is where the server stub and interface are


//import java.util.Random; //Random number generation
import javax.swing.*; //GUI
import java.util.*; //for lists
import java.io.*;


public class ChampionsLeagueClient {
    
    private String servname;
    private IChampionsLeague icl;
    
    public ChampionsLeagueClient(String hst) {
        servname = "rmi://" + hst + "/ChampionsLeague";;
        try {
      //Get a reference to the interface using the naming service
      icl = (IChampionsLeague) Naming.lookup(servname);

    }

    catch (java.rmi.ConnectException ce) {
      ce.printStackTrace();
    }
    catch (RemoteException re) {
      re.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PrintStream cout = System.out;
        Scanner cin = new Scanner(System.in);
         ChampionsLeagueClient clc = new ChampionsLeagueClient("localhost");
         
         cout.println();
        cout.println("Enter A to Add Player Stat, P to list Player Stats, T to list Team Stats, Q to quit");
        cout.flush();
        String input = cin.nextLine();
        boolean quit = false;

        while (!quit) {
           
            try{
                   int c = input.charAt(0);
            
         
            switch (c) {
                case 'a':
                case 'A':
                    cout.println("Enter Match ID: ");
                    cout.flush();
                    String mid = cin.nextLine();
                    cout.println("Enter Player ID: ");
                    cout.flush();
                    String pid = cin.nextLine();
                    cout.println("Enter Goals: ");
                    cout.flush();
                    int goals = cin.nextInt();
                    try{
                    String name = clc.icl.AddPlayerStat(mid, pid, goals);
                    System.out.println(name);
                    }
                    catch(RemoteException ex){
                        ex.printStackTrace();
                    }
                                   
                    break;
                    
                case 'P':
                case 'p':
                    cout.println("Enter Player ID: ");
                    cout.flush();
                    pid = cin.nextLine();
                    try{
                    List<String> list = (List<String>)clc.icl.ListPlayerStats(pid);
                    
                    
                    for(String s:list){
                    System.out.println(s);
                    }
                    }
                    catch(RemoteException ex){
                        ex.printStackTrace();
                    }
                    break;
                case 'T':
                case 't':
                    cout.println("Enter Team ID: ");
                    cout.flush();
                    String tid = cin.nextLine();
                    try{
                    List<String> l = clc.icl.ListTeamPoints(tid);
                    for (String s1:l) {
                        System.out.println(s1);
                    }
                    }
                    catch(RemoteException ex){
                        ex.printStackTrace();
                    }

                    break;
                default:
                    quit = true;

            }
            
            
            
            if (!quit) {
                cout.println("Enter A to Add Player Stat, P to list Player Stats, T to list Team Stats, Q to quit");
                cout.flush();
                
                input = cin.nextLine();
            }
            }
            catch(IndexOutOfBoundsException e){
                //input = null;
                input = cin.nextLine();
            
            }

        }
        
    }
}

    

