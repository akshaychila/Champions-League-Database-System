using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using chilaaasg5client.CLServiceReference;

namespace chilaaasg5client
{
    class Program
    {
        static void Main(string[] args)
        {
           CLeagueServiceClient clsc = new CLeagueServiceClient();

                
            
           Console.WriteLine();
        Console.WriteLine("Enter A to Add Player Stat, P to list Player Stats, T to list Team Stats, Q to quit");
       
        String input = Console.ReadLine();
        Boolean quit = false;

        while (!quit) {
           
            try{
                   Char c = input[0];
            
         
            switch (c) {
                case 'a':
                case 'A':
                    Console.WriteLine("Enter Match ID: ");
                   
                    String mid = Console.ReadLine();
                    Console.WriteLine("Enter Player ID: ");
                  
                    String pid = Console.ReadLine();
                    Console.WriteLine("Enter Goals: ");
                  
                    int goals = Convert.ToInt32(Console.ReadLine());
                    
                    String name = clsc.AddPlayerStat(mid, pid, goals);
                    Console.WriteLine(name);
                                                      
                    break;
                    
                case 'P':
                case 'p':
                    Console.WriteLine("Enter Player ID: ");
                  
                    pid = Console.ReadLine();
                    
                    String [] list = clsc.ListPlayerStats(pid);
                    
                    
                    for (int i = 0; i < list.Length; i++){
                            Console.WriteLine(list[i]);
                    }
                    
                    break;
                case 'T':
                case 't':
                    Console.WriteLine("Enter Team ID: ");
                  
                    String tid = Console.ReadLine();
                   
                    String [] l = clsc.ListTeamPoints(tid);
                    for (int i = 0; i < l.Length; i++){
                            Console.WriteLine(l[i]);
                      }             
                    break;

               default:
                    quit = true;
                    break;

            }
            
            
            
            if (!quit) {
                Console.WriteLine("Enter A to Add Player Stat, P to list Player Stats, T to list Team Stats, Q to quit");
                
                
                input = Console.ReadLine();
            }
            }
            catch(Exception e){
                
                input = Console.ReadLine();
            
            }

        }
        

           
        }
    }
}


