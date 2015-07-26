import java.io.*;
import java.util.*;

public class LyricBindGenerator
{
    static char buttonBind;

    public static void main(String[] args) throws Exception
    {
        Scanner kb = new Scanner(System.in);
        System.out.println("What button do you want to bind it to?");
        buttonBind = kb.nextLine().charAt(0);
        
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        Scanner sc = new Scanner(new File("input.txt"));
        
        //MasterBind consists of name and long string of all lyrics
        Map<String,String> MasterBind = new TreeMap<String,String>();        
        
        //Reads the input and adds first line as name to masterbind and grabs lines as lyrics until = or end
        while(sc.hasNext())
        {
            String tempName = "";
            String tempLyric = "";
            tempName += sc.nextLine();
            while(sc.hasNext())
            {
                String temp = sc.nextLine();
                if(temp.equals("") && sc.hasNext())
                    temp = sc.nextLine();
                if(temp.equals("="))
                    break;
                tempLyric += temp;
                tempLyric += "\n";
            }
            MasterBind.put(tempName, tempLyric);
        }
        
        //Fancy crap
        writer.println("host_writeconfig");
        writer.println("echo \"===========================\""); 
        writer.println("echo \"KARAOKE BINDS INITIATED\"");
        writer.println("echo \"binds programmed by Emertxe\"");
        writer.println("echo \"==============================================================\""); 
        writer.println("echo \",.*'^'*.,.*'^'*.,.*'^'*.,.*'^'*.,.*'^'*.,,.*'^'*.,.*'^'*.,.*'^\"");
        writer.println("echo \"--------------------------------------------------------------\""); 
        writer.println("echo \"Type in the word 'lyric' to display list of lyrics available.\"");
        writer.println("echo \"--------------------------------------------------------------\""); 
        writer.println("echo \",.*'^'*.,.*'^'*.,.*'^'*.,.*'^'*.,.*'^'*.,,.*'^'*.,.*'^'*.,.*'^\"");
        writer.println("echo \"==============================================================\""); 
        
        //Makes lyric display
        String lyricDisplay = "";
        int numbering = 1;
        lyricDisplay += "alias lyric \"echo \"=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|\";echo \"Enter the number lyric you want plus L (e.g. 1L, 2L), add T for team (e.g. 1LT, 2LT)\";echo \"=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|=+%+=|\";lyric"+numbering+"\"\n\n";
                
        for(String o : MasterBind.keySet())
        {
            lyricDisplay += "alias lyric"+numbering + " \"echo \"" + numbering + "LT. " + o + "\";lyric"+ ++numbering + "\"\n";
        }
        String last = "lyric" + numbering;
        lyricDisplay = lyricDisplay.substring(0, lyricDisplay.length()-(last.length()+3));
        lyricDisplay += "\"\n";
        
        writer.println();
        writer.println(lyricDisplay);       
        
        //Replaces " with ''
        for(String s : MasterBind.keySet())
        {
            String temp = MasterBind.get(s);
            temp = temp.replace("\"","''");
            MasterBind.put(s, temp);
        }
        
        //Indexes and binds lyrics    
        numbering = 0;
        
        for(String s : MasterBind.keySet())
        {
            writer.println("alias " + ++numbering + "L \"bind " + buttonBind + " " + numbering + "L1;echo \"Loaded lyrics: "+s+"\"");
        }
        writer.println();
        
        numbering = 0;
        
        for(String s : MasterBind.keySet())
        {
            writer.println("alias " + ++numbering + "LT \"bind " + buttonBind + " " + numbering + "LT1;echo \"Loaded lyrics (team speak): "+s+"\"");
        }
        writer.println();
        
        numbering = 0;
        
        for(String s : MasterBind.keySet())
        {
            writer.println(bindCreator(++numbering + "L", MasterBind.get(s), buttonBind));
            writer.println();
        }
        
        numbering = 0;
        
        for(String s : MasterBind.keySet())
        {
            writer.println(teamBindCreator(++numbering + "LT", MasterBind.get(s), buttonBind));
            writer.println();
        }
        writer.close();
        
        //Final output test and completion notice
        for(String s : MasterBind.keySet())
        {
            System.out.println(s + "\n\n" + MasterBind.get(s));            
        }
        System.out.println("BIND COMPLETED, STORED IN OUTPUT.TXT");
    }
    
    public static String bindCreator(String name, String bind, char button)
    {
        Scanner in = new Scanner(bind);
        int count = 1;
        String finBind = "";
        
        String bindline = "alias " + name + count + " \"say " + in.nextLine() + ";unbind " + buttonBind + ";bind " + buttonBind + " " + name + ++count + "\"";
        String nl = "";
        while(in.hasNext())
        {
            nl = in.nextLine();
            if(nl.equals("") && in.hasNext())
                nl = in.nextLine();
            finBind += bindline;
            finBind += "\n";
            bindline = "alias " + name + count + " \"say " + nl + ";unbind " + buttonBind + ";bind " + buttonBind + " " + name + ++count + "\"";            
        }
        bindline = "alias " + name + --count + " \"say " + nl + ";unbind " + buttonBind + "\"";
        finBind += bindline;
        return finBind;
    }
    
    public static String teamBindCreator(String name, String bind, char button)
    {
        Scanner in = new Scanner(bind);
        int count = 1;
        String finBind = "";
        
        String bindline = "alias " + name + count + " \"say_team " + in.nextLine() + ";unbind " + buttonBind + ";bind " + buttonBind + " " + name + ++count + "\"";
        String nl = "";
        while(in.hasNext())
        {
            nl = in.nextLine();
            if(nl.equals("") && in.hasNext())
                nl = in.nextLine();
            finBind += bindline;
            finBind += "\n";
            bindline = "alias " + name + count + " \"say_team " + nl + ";unbind " + buttonBind + ";bind " + buttonBind + " " + name + ++count + "\"";            
        }
        bindline = "alias " + name + --count + " \"say_team " + nl + ";unbind " + buttonBind + "\"";
        finBind += bindline;
        return finBind;
    }
}