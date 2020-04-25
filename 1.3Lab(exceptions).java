import java.io.*;
import java.util.*;

class EmptyFile extends Exception
{
    public EmptyFile(){};

    @Override
    public String toString()
    {
        return "File is empty! Fill the file";
    }
}

public class Main {

    public static void app() throws IOException, EmptyFile{
            EmptyFile empFile = new EmptyFile();
            Stack stk = new Stack();
            try {
            BufferedReader br = new BufferedReader(new FileReader("source.txt"));


        String CurLine = new String();
        if((CurLine = br.readLine()) == null)
           throw empFile;

        System.out.println("Contents of the file: ");
        while ((CurLine = br.readLine()) != null) {
            System.out.println(CurLine);
            stk.push(CurLine);
        }

        PrintStream output = new PrintStream(new FileOutputStream("source.txt"));
        PrintStream stdout = System.out;
        System.setOut(output);
        //----------------
        while (!stk.empty()) {
            System.out.println(stk.pop());
        }

        System.setOut(stdout);
        System.out.println("Order of lines has been reversed");
            } catch (FileNotFoundException e)
            {
                System.out.println("Ошибка! Файл не найден!");
            }
    }


    public static void main(String[] args) {
        try {
            app();
        } catch (EmptyFile|IOException e) {
            e.printStackTrace(System.out);
        }

    }
}
