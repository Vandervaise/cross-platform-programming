import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static String readRegex()throws IOException, noStringExc {
        noStringExc noString = new noStringExc();
        System.out.print("Input a regex : ");
        String regex = null;

        regex = consoleReader.readLine();
        if (regex.equals(""))
            throw noString;
        //regex = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9\\+]+(\\.[A-Za-z0-9]+)*(\\.(com|ua|ru))$";
                else
            return regex;
        }

    public static BufferedReader readTypeOfReading() throws Exception {
        System.out.println("Введите метод считивания FILE or CONSOLE");
        BufferedReader reader = null;
        String type = consoleReader.readLine();
        switch (type) {
            case "FILE": {
                reader = new BufferedReader(new FileReader("emails.txt"));
                break;
            }
            case "CONSOLE": {
                reader = new BufferedReader(new InputStreamReader(System.in));
                break;
            }
            default:
                throw new Exception("You must choose FILE or CONSOLE!!");
        }
        return reader;
    }

    public static void buffer(String regex,BufferedReader br) throws Exception {
        System.out.println("Осуществляеться ввод emails : ");
        String line = null;
        Pattern email = Pattern.compile(regex);
        while ((line = br.readLine()) != null) {
            String[] values = line.split(" ");
            for (String str : values) {
                System.out.println(str);
                Matcher matcher = email.matcher(str);
                System.out.println(matcher.find());
            }
        }
        br.close();
    }
    public static void main(String[] args) throws Exception {

        try {
            buffer(readRegex(), readTypeOfReading());
        }
        catch (noStringExc e)
        {
            System.out.println(e.toString());
        }
    }
    public static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
}
