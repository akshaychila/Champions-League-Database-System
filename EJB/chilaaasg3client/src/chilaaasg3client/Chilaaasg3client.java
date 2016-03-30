/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilaaasg3client;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NameClassPair;
import chilaaasg3server.ChampionsLeagueRemote;

/**
 *
 * @author achila
 */
public class Chilaaasg3client {

    /**
     * @param args the command line arguments
     */
    public static ChampionsLeagueRemote clr;
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            InitialContext ic = new InitialContext();
/*            NamingEnumeration<NameClassPair> ne = ic.list("");
            while(ne.hasMore())
            {
                NameClassPair ncp = ne.next();
                System.out.println(ncp.toString());
            }
*/
            //ccr = (CreditCardRemote)ic.lookup("ccejb.CreditCardRemote");
            clr = (ChampionsLeagueRemote)ic.lookup("java:global/chilaaasg3server/ChampionsLeague!chilaaasg3server.ChampionsLeagueRemote");
            //System.out.println(ccr.Update("1111","13000"));
            PrintStream cout = System.out;
        Scanner cin = new Scanner(System.in);
            
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
                    
                    String name = clr.AddPlayerStat(mid, pid, goals);
                    System.out.println(name);
                                                      
                    break;
                    
                case 'P':
                case 'p':
                    cout.println("Enter Player ID: ");
                    cout.flush();
                    pid = cin.nextLine();
                    
                    List<String> list = (List<String>)clr.ListPlayerStats(pid);
                    
                    
                    for(String s:list){
                    System.out.println(s);
                    }
                    
                    break;
                case 'T':
                case 't':
                    cout.println("Enter Team ID: ");
                    cout.flush();
                    String tid = cin.nextLine();
                   
                    List<String> l = clr.ListTeamPoints(tid);
                    for (String s1:l) {
                        System.out.println(s1);
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
        } catch(Exception ex){ex.printStackTrace();}


        
    }
    
}
