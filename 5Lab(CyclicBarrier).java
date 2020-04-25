import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
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

class EmptyFile extends Exception
{
    public EmptyFile(){};

    @Override
    public String toString()
    {
        return "File is empty! Fill the file";
    }
}

class lab1 extends Thread {
    private Thread t1;
    //CBR
    CyclicBarrier cbr;
    public lab1(CyclicBarrier cb_param)
    {
        cbr = cb_param;
    }

    public static void lb1() throws noStringExc {
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

    @Override
    public void start()
    {
        try {
            lb1();
        } catch (noStringExc e) {
            System.out.println(e.toString());
        }
        try {
            cbr.await();
        }catch (InterruptedException | BrokenBarrierException e)
        {}
    }
}


class lab2 extends Thread
{
    private Thread t1;

    //CBR
    CyclicBarrier cbr;
    public lab2(CyclicBarrier cb_param)
    {
        cbr = cb_param;
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
        try {
            cbr.await();
        }catch (InterruptedException | BrokenBarrierException e)
        {}
    }
    public static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
}

class lab3 extends Thread {
    //CBR
    static CyclicBarrier cbr;
    public lab3(CyclicBarrier cb_param)
    {
        cbr = cb_param;
    }

    private Thread t;
    public static void lb3() throws IOException, EmptyFile{
        EmptyFile empFile = new EmptyFile();
        Stack stk = new Stack();
        try {
            BufferedReader br = new BufferedReader(new FileReader("emails.txt"));


            String CurLine = new String();
            if((CurLine = br.readLine()) == null)
                throw empFile;

            System.out.println("Contents of the file: ");
            while ((CurLine = br.readLine()) != null) {
                System.out.println(CurLine);
                stk.push(CurLine);
            }

            PrintStream output = new PrintStream(new FileOutputStream("emails.txt"));
            PrintStream stdout = System.out;
            System.setOut(output);
            //----------------
            while (!stk.empty()) {
                System.out.println(stk.pop());
            }

            System.setOut(stdout);
            System.out.println("Reverse complete");
        } catch (FileNotFoundException e)
        {
            System.out.println("Ошибка! Файл не найден!");
        }
    }


    public  void start() {
        try {
            lb3();
        } catch (EmptyFile|IOException e) {
            e.printStackTrace(System.out);
        }
        try {
            cbr.await();
        }catch (InterruptedException | BrokenBarrierException e)
        {}
    }
}

class swiftframe extends JPanel implements Runnable
{
    private Thread t;

    public static String myText = "Sample";
    static CyclicBarrier cbr;

    public swiftframe(CyclicBarrier cb_param)
    {

        cbr = cb_param;
    }


    @Override
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("Arial", Font.BOLD, 30));

        try
        {
            cbr.await();
        } catch (InterruptedException | BrokenBarrierException e)
        {
            e.printStackTrace();
        }

        g2.drawString(myText,50,50);


    }

    @Override
    public void run()
    {
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new swiftframe(cbr));

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

public class Main {

    public static void main(String[] args) {
                    Runnable act = () -> System.out.println("CBR reached");
                    CyclicBarrier CBR = new CyclicBarrier(2, act);

                    lab2 threadL2CBR = new lab2(CBR);
                    swiftframe thread = new swiftframe(CBR);

                    thread.start();
                    try {
                        System.out.println("_____Lab2_____");
                        threadL2CBR.start();
                        threadL2CBR.join();
                        //System.out.println("_____Lab3_____");
                        //threadL3CBR.start();
                        //threadL3CBR.join();
                        //System.out.println("_____Lab1_____");
                        //threadL1CBR.start();
                        //threadL1CBR.join();
                    } catch (InterruptedException e){}
    }
}

