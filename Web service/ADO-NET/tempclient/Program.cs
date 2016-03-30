using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using chilaaadodblib;

namespace chilaaasg5client
{
    class Program
    {
        static void Main(string[] args)
        {
            championsleague db = new championsleague("ChampionsLeague", "ism6236", "ism6236bo");

            String str = db.AddPlayerStat("3", "P01", 7);
            List<String> l = db.ListPlayerStats("P01");
            
            for (int i = 0; i < l.Count; i++)
                Console.WriteLine(l[i]);

            List<String> m = db.ListTeamPoints("T01");
            for (int i = 0; i < m.Count; i++)
                Console.WriteLine(m[i]);
            Console.ReadLine();  

            
        }
    }
}
