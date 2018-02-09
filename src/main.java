import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by seong on 5/6/2017.
 */
public class main {
    private static String rotor1key = "EKMFLGDQVZNTOWYHXUSPAIBRCJ"; //Enigma I, rotor I
    private static String rotor2key = "AJDKSIRUXBLHWTMCQGZNPYFVOE"; //Enigma I, rotor II
    private static String rotor3key = "BDFHJLCPRTXVZNYEIWGAKMUSQO"; //Enigma I, rotor III
    private static String rotor4key = "ESOVPZJAYQUIRHXLNFTGKDCMWB"; //M3 Army, rotor IV
    private static String rotor5key = "VZBRGITYUPSDNHLXAWMJQOFECK"; //M3 Army, rotor V

    private static String beta = "LEYJVCNIXWPBQMDRTAKZGFUHOS"; //M4 R2, rotor Beta
    private static String gamma = "FSOKANUERHMBTIYCWLQPZXVGJD"; //M4 R2, rotor Gamma


    private static ArrayList<Sets> reflectorBeta = new ArrayList<>();
    private static ArrayList<Sets> reflectorGamma = new ArrayList<>();

    private static ArrayList<Sets> plugBoard = new ArrayList<>();

    private static ArrayList<Sets> leftRotor = new ArrayList<>();
    private static ArrayList<Sets> middleRotor = new ArrayList<>();
    private static ArrayList<Sets> rightRotor = new ArrayList<>();
    private static ArrayList<Sets> reflector = new ArrayList<>();

    private static int smallWheel = 0;
    private static int midWheel = 0;
    private static int largeWheel = 0;
    public static boolean usePlug = false;


    public static String encrypt(String in)
    {
        String encrypted = "";
        int test = 0;
        while(in.length()>0)
        {
            String temp = in.substring(0,1);
            String check = temp;
			if(usePlug){
			    temp = matchPlug(temp);
            }
            temp = findKey("right",temp,"encryK1");
            temp = findKey("middle",temp,"encryK2");
            temp = findKey("left",temp,"encryK3");
            temp = reflector(temp , "encryR");
            temp = findOG("left",temp,"encryO1");
            temp = findOG("middle",temp,"encryO2");
            temp = findOG("right",temp,"encryO3");
            if(usePlug){
                temp = matchPlugR(temp);
            }
            encrypted += temp;
            if(!temp.equals(check))
                rotate();
            in = in.substring(1);
        }
        return encrypted;
    }

    public static String decrypt(String in)
    {
        String decrypted = "";
        int test = 0;
        while(in.length()>0)
        {
            String temp = in.substring(0,1);
            String check = temp;
            if(usePlug){
                temp = matchPlug(temp);
            }
            temp = findKey("right",temp,"encryK1");
            temp = findKey("middle",temp,"encryK2");
            temp = findKey("left",temp,"encryK3");
            temp = reflectorR(temp , "encryR");
            temp = findOG("left",temp,"encryO1");
            temp = findOG("middle",temp,"encryO2");
            temp = findOG("right",temp,"encryO3");
            if(usePlug){
                temp = matchPlugR(temp);
            }
            decrypted += temp;
            if(!temp.equals(check))
                rotate();
            in = in.substring(1);
        }
        return decrypted;
    }

    //gets moving letter on moving part of the rotor
    public static String findKey(String rotor, String op, String loc)
    {
        if(rotor.equals("right"))
        {
            for (int i = 0; i<rightRotor.size(); i++)
            {
                if(rightRotor.get(i).og.equals(op))
                    return rightRotor.get(i).key;
            }
        }
        if(rotor.equals("middle"))
        {
            for (int i = 0; i<middleRotor.size(); i++)
            {
                if(middleRotor.get(i).og.equals(op))
                    return middleRotor.get(i).key;
            }
        }
        if(rotor.equals("left"))
        {
            for (int i = 0; i<leftRotor.size(); i++)
            {
                if(leftRotor.get(i).og.equals(op))
                    return leftRotor.get(i).key;
            }
        }
        //System.out.println("ERROR " + op + " : " + loc);
        return op;
    }

    //gets the letter on constant side of rotor
    public static String findOG(String rotor, String op, String loc)
    {
        if(rotor.equals("right"))
        {
            for (int i = 0; i<rightRotor.size(); i++)
            {
                if(rightRotor.get(i).key.equals(op))
                    return rightRotor.get(i).og;
            }
        }
        if(rotor.equals("middle"))
        {
            for (int i = 0; i<middleRotor.size(); i++)
            {
                if(middleRotor.get(i).key.equals(op))
                    return middleRotor.get(i).og;
            }
        }
        if(rotor.equals("left"))
        {
            for (int i = 0; i<leftRotor.size(); i++)
            {
                if(leftRotor.get(i).key.equals(op))
                    return leftRotor.get(i).og;
            }
        }
        //System.out.println("ERROR " + op + " : " + loc);
        return op;
    }

    //gets letter to reflector
    public static String reflector(String op,String loc)
    {
        for (int i = 0; i<reflector.size(); i++)
        {
            if(reflector.get(i).og.equals(op))
                return reflector.get(i).key;
        }
        //System.out.println("ERROR " + op + " : " + loc);
        return op;
    }

    //gets the reverse reflector
    public static String reflectorR(String op,String loc)
    {
        for (int i = 0; i<reflector.size(); i++)
        {
            if(reflector.get(i).key.equals(op))
                return reflector.get(i).og;
        }
        //System.out.println("ERROR " + op + " : " + loc);
        return op;
    }

    public static String matchPlugR(String op)
    {
        for (int i = 0; i<plugBoard.size(); i++)
        {
            if(plugBoard.get(i).og.equals(op))
                return plugBoard.get(i).key;
        }
        System.out.println("ERROR " + op);
        return op;
    }

    public static String matchPlug(String op)
    {
        for (int i = 0; i<plugBoard.size(); i++)
        {
            if(plugBoard.get(i).key.equals(op))
                return plugBoard.get(i).og;
        }
        System.out.println("ERROR " + op);
        return op;
    }

    //rotates the rotors after every character
    public static void rotate()
    {
        String tempKEY = rightRotor.get(0).key;
        for (int i = 0; i<25; i++) {
            rightRotor.get(i).key = rightRotor.get(i+1).key;
        }
        rightRotor.get(25).key = tempKEY;
        smallWheel++;
        if(smallWheel>26)
        {
            tempKEY = middleRotor.get(0).key;
            for (int i = 0; i<25; i++) {
                middleRotor.get(i).key = middleRotor.get(i+1).key;
            }
            middleRotor.get(25).key = tempKEY;
            smallWheel %= 26;
            midWheel++;
        }
        if(midWheel>26)
        {
            tempKEY = leftRotor.get(0).key;
            for (int i = 0; i<25; i++) {
                leftRotor.get(i).key = leftRotor.get(i+1).key;
            }
            leftRotor.get(25).key = tempKEY;
            midWheel %= 26;
            largeWheel++;
        }
        if(largeWheel>26)
        {
            largeWheel %=26;
        }
    }

    //allows user to choose the rotor used to encrypt message, NOT SUPPORTED IN THIS VERSION
    public static void setRotors(int left, int middle, int right)
    {
        int add = 0;

        if(left==1)
            for (int i = 65; i<91; i++) {
                leftRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor1key.charAt(add)) ) );
                add++;
            }
        if(left==2)
            for (int i = 65; i<91; i++) {
                leftRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor2key.charAt(add)) ) );
                add++;
            }
        if(left==3)
            for (int i = 65; i<91; i++) {
                leftRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor3key.charAt(add)) ) );
                add++;
            }
        if(left==4)
            for (int i = 65; i<91; i++) {
                leftRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor4key.charAt(add)) ) );
                add++;
            }
        if(left==5)
            for (int i = 65; i<91; i++) {
                leftRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor5key.charAt(add)) ) );
                add++;
            }

        add = 0;

        if(middle==1)
            for (int i = 65; i<91; i++) {
                middleRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor1key.charAt(add)) ) );
                add++;
            }
        if(middle==2)
            for (int i = 65; i<91; i++) {
                middleRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor2key.charAt(add)) ) );
                add++;
            }
        if(middle==3)
            for (int i = 65; i<91; i++) {
                middleRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor3key.charAt(add)) ) );
                add++;
            }
        if(middle==4)
            for (int i = 65; i<91; i++) {
                middleRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor4key.charAt(add)) ) );
                add++;
            }
        if(middle==5)
            for (int i = 65; i<91; i++) {
                middleRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor5key.charAt(add)) ) );
                add++;
            }

        add = 0;

        if(right==1)
            for (int i = 65; i<91; i++) {
                rightRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor1key.charAt(add)) ) );
                add++;
            }
        if(right==2)
            for (int i = 65; i<91; i++) {
                rightRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor2key.charAt(add)) ) );
                add++;
            }
        if(right==3)
            for (int i = 65; i<91; i++) {
                rightRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor3key.charAt(add)) ) );
                add++;
            }
        if(right==4)
            for (int i = 65; i<91; i++) {
                rightRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor4key.charAt(add)) ) );
                add++;
            }
        if(right==5)
            for (int i = 65; i<91; i++) {
                rightRotor.add( new Sets( String.valueOf((char)i), String.valueOf(rotor5key.charAt(add)) ) );
                add++;
            }
    }

    public static void setReflectorBeta(){
        int add = 0;
        for (int i = 65; i<91; i++) {
            reflector.add( new Sets( String.valueOf((char)i), String.valueOf(beta.charAt(add)) ) );
            add++;
        }
    }

    public static void setReflectorGamma(){
        int add = 0;
        for (int i = 65; i<91; i++) {
            reflector.add( new Sets( String.valueOf((char)i), String.valueOf(gamma.charAt(add)) ) );
            add++;
        }
    }
    /**
     * @param args the command line arguments, args = "inputfile" "outputfile"
     */
    public static void main(String[] args) {
        if(args.length == 0){
            runComandline();
        } else {
            runFiles(args);
        }
    }

    private static void setUp(){
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("This will only encrypt letters, all letters converted to uppercase");

        //Set plug board
        System.out.println("would you like to use the plugboard? \"y\" for yes and \"n\" for no");
        try{
            if(input.readLine().equals("y")){
                System.out.println("enter the connections matching \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\" \n");
                String plug = input.readLine();
                plug = plug.toUpperCase();
                int add = 0;
                for (int i = 65; i<91; i++)
                {
                    plugBoard.add( new Sets( String.valueOf((char)i) ,String.valueOf(plug.charAt(add)) ) );
                    add++;
                }
                usePlug = true;
                System.out.println("Plug board connected");
            }
        } catch (IOException e){
            System.out.println("ERROR: Input error");
        }

        //Set rotors
        System.out.println ("select 3 rotors (1-5) (example input: \"1 2 3\")");
        try {
            String temp = input.readLine();
            String rotors[] = temp.split("\\s");
            setRotors(Integer.parseInt(rotors[0]),Integer.parseInt(rotors[1]),Integer.parseInt(rotors[2]));
            System.out.println("Rotors set");
        } catch (IOException e){
            System.out.println("ERROR: Input error");
        }

        //set reflectors
        System.out.println ("pick reflector (\"beta\" or \"gamma\")");
        try{
            if(input.readLine().equals("beta")) {
                setReflectorBeta();
            }else {
                setReflectorGamma();
            }
            System.out.println("Reflectors set");
        } catch (IOException e){
            System.out.println("ERROR: Input error");
        }

        //Set Rotor location
        System.out.println ("set rotor loc (0-25)\nexample input \"0 0 0\"");
        try {
            String temp = input.readLine();
            String location[] = temp.split("\\s");
            int turns = Integer.parseInt(location[0]) + Integer.parseInt(location[1])*26 + Integer.parseInt(location[2])*26*26;
            for (int i = 0; i < turns; i++) {
                rotate();
            }
            System.out.println("Rotors set");
        } catch (IOException e){
            System.out.println("ERROR: Input error");
        }
    }

    public static void runComandline(){
        //configure settings
        setUp();

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        //Encrypt or Decrypt
        System.out.println("encrypt(e) or decrypt(d)");
        try {
            if (input.readLine().equals("e")){
                System.out.println("Enter message:");
            System.out.println("your encrypted message: \n" + encrypt(input.readLine().toUpperCase()));
            } else {
                System.out.println("Enter message:");
                System.out.println("your message: \n" + decrypt(input.readLine().toUpperCase()));
            }
        } catch (IOException e){
            System.out.println("ERROR: Input error");
        }
    }

    public static void runFiles(String args[]){
        System.out.println("args = [" + args[0] + " " +args[1] + "]");
        System.out.println(new File(args[0]).getAbsolutePath());
        System.out.println(new File(args[1]).getAbsolutePath());
        if(args.length > 2 ){
            BufferedReader reader = null;
            BufferedWriter writer = null;

            try{
                reader = new BufferedReader(new FileReader(args[0]));
                writer = new BufferedWriter(new FileWriter(args[1]));
                String line;
                while ((line = reader.readLine()) != null){
                    writer.write(encrypt(line.toUpperCase()) + '\n');
                }
                System.out.println("DONE");
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null)
                        reader.close();

                    if (writer != null)
                        writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            setUp();
            BufferedReader reader = null;
            BufferedWriter writer = null;
            try{
                reader = new BufferedReader(new FileReader(args[0]));
                writer = new BufferedWriter(new FileWriter(args[1]));
                String line;
                while ((line = reader.readLine()) != null){
                    writer.write(encrypt(line.toUpperCase()) + '\n');
                }
                System.out.println("DONE");
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null)
                        reader.close();

                    if (writer != null)
                        writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}