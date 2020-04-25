import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Exchanger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

class noStringExc extends Exception
{
    public noStringExc(){};

    @Override
    public String toString()
    {
        return "Empty string field caught. You must fill this field!";
    }
}
class lab2 extends Thread
{
    private Thread t1;
    static Exchanger<String> ex;

    public lab2(Exchanger<String> ex_param)
    {
        ex = ex_param;
    }
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
                try
                {
                    ex.exchange(str);
                }
                catch (InterruptedException e)
                {
                    System.out.println(e);
                }
            }

        }
        br.close();
    }
    public void start() {

        try {
            buffer(readRegex(), readTypeOfReading());
        }
        catch (noStringExc|IOException e)
        {
            System.out.println(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
}
//================Lab4================
class swiftframe extends JPanel implements Runnable
{
    private Thread t;

    public static String myText = "";
    static Exchanger<String> ex;

    public swiftframe(Exchanger<String> ex_param)
    {
        ex = ex_param;
    }


    @Override
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("Arial", Font.BOLD, 30));
            try
            {
                myText = ex.exchange(myText);
            }
            catch (InterruptedException e)
            {
                System.out.println(e);
            }
        g2.drawString(myText,50,50);
    }

    @Override
    public void run()
    {
        JFrame frame = new JFrame("lab4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new swiftframe(ex));

        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void start()
    {
        t = new Thread (this, "lab2");
        t.start();
    }
}

//--------------------------------------------------------------------------------------

public class Main
{
    public static void main(String[] args)
    {
        Exchanger<String> ex = new Exchanger();
        lab2 thread1 = new lab2(ex);
        swiftframe thread2 = new swiftframe(ex);

        thread2.start();
        try
        {
            thread1.start();
            thread1.join();
        }
        catch(InterruptedException e)
        {
            JOptionPane.showMessageDialog(null, "Interrupted Exception");
        }
    }
}
