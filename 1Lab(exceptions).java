
import java.io.*;
import java.util.*;

//----------------------------------
class splitter

{
    public static void app() throws noStringExc
    {
    noStringExc noString = new noStringExc();
        System.out.println("Hello");
    //String str1 = new String("Что такое Java? Java - это язык программирования");
    String str1 = new String("");


    int count = 0;

            if (str1.equals(""))
                    throw noString;

            for (String item : str1.split(" ")) {
    for (String retval : str1.split(" ")) {
        if (retval.regionMatches(true, 0, item, 0, item.length())) {
            count++;
        }
        }
        System.out.println(item + " : " + count);
        count = 0;
        }
    }
}

class noStringExc extends Exception
{
    public noStringExc(){};

    @Override
    public String toString()
    {
        return "Empty string field caught. You must fill this field!";
    }
}

public class Main {

    public static void main(String[] args) throws noStringExc {


        try
        {
            splitter.app();
        }
        catch (noStringExc e)
        {
            System.out.println(e.toString());
        }
    }
    }

