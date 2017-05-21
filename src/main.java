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

    private static ArrayList<Sets> rotor1 = new ArrayList<Sets>();
    private static ArrayList<Sets> rotor2 = new ArrayList<Sets>();
    private static ArrayList<Sets> rotor3 = new ArrayList<Sets>();
    private static ArrayList<Sets> rotor4 = new ArrayList<Sets>();
    private static ArrayList<Sets> rotor5 = new ArrayList<Sets>();

    private static ArrayList<Sets> reflectorBeta = new ArrayList<Sets>();
    private static ArrayList<Sets> reflectorGamma = new ArrayList<Sets>();

    private static ArrayList<Sets> plugBoard = new ArrayList<Sets>();

    private static ArrayList<Sets> leftRotor = new ArrayList<Sets>();
    private static ArrayList<Sets> middleRotor = new ArrayList<Sets>();
    private static ArrayList<Sets> rightRotor = new ArrayList<Sets>();
    private static ArrayList<Sets> reflector = new ArrayList<Sets>();

    private static int smallWheel = 0;
    private static int midWheel = 0;
    private static int largeWheel = 0;
    private static boolean usePlug = false;


    public static String encrypt(String in)
    {
        String encrypted = "";
        int test = 0;
        while(in.length()>0)
        {
            String temp = in.substring(0,1);
            String check = temp;
			System.out.println ("run number : " + ++test);
			System.out.println (temp);
			if(usePlug){
			    System.out.println("using plugboard");
			    temp = matchPlug(temp);
            }
            temp = findKey("right",temp,"encryK1");
			System.out.println (temp);
            temp = findKey("middle",temp,"encryK2");
			System.out.println (temp);
            temp = findKey("left",temp,"encryK3");
			System.out.println (temp);
            temp = reflector(temp , "encryR");
			System.out.println (temp);
            temp = findOG("left",temp,"encryO1");
			System.out.println (temp);
            temp = findOG("middle",temp,"encryO2");
			System.out.println (temp);
            temp = findOG("right",temp,"encryO3");
			System.out.println (temp);
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
            System.out.println ("run number : " + ++test);
            System.out.println (temp);
            if(usePlug){
                temp = matchPlug(temp);
            }
            temp = findKey("right",temp,"encryK1");
            System.out.println (temp);
            temp = findKey("middle",temp,"encryK2");
            System.out.println (temp);
            temp = findKey("left",temp,"encryK3");
            System.out.println (temp);
            temp = reflectorR(temp , "encryR");
            System.out.println (temp);
            temp = findOG("left",temp,"encryO1");
            System.out.println (temp);
            temp = findOG("middle",temp,"encryO2");
            System.out.println (temp);
            temp = findOG("right",temp,"encryO3");
            System.out.println (temp);
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
        System.out.println("ERROR " + op + " : " + loc);
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
        System.out.println("ERROR " + op + " : " + loc);
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
        System.out.println("ERROR " + op + " : " + loc);
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
        System.out.println("ERROR " + op + " : " + loc);
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
        if(left==1)
            leftRotor = rotor1;
        if(left==2)
            leftRotor = rotor2;
        if(left==3)
            leftRotor = rotor3;
        if(left==4)
            leftRotor = rotor4;
        if(left==5)
            leftRotor = rotor5;
        if(middle==1)
            middleRotor = rotor1;
        if(middle==2)
            middleRotor = rotor2;
        if(middle==3)
            middleRotor = rotor3;
        if(middle==4)
            middleRotor = rotor4;
        if(middle==5)
            middleRotor = rotor5;
        if(right==1)
            rightRotor = rotor1;
        if(right==2)
            rightRotor = rotor2;
        if(right==3)
            rightRotor = rotor3;
        if(right==4)
            rightRotor = rotor4;
        if(right==5)
            rightRotor = rotor5;
    }

    //code for setting initial rotor location, not supported at the moment
    /*may not need...
    public static void setRotorLoc(int left, int middle, int right)
    {
        for (int i = 0; i<left; i++)
        {
            String tempKEY = leftRotor.get(0).key;
            for (int n = 0; n<25; n++) {
                leftRotor.get(i).key = leftRotor.get(i+1).key;
            }
            leftRotor.get(25).key = tempKEY;
            smallWheel++;
        }
        for (int i = 0; i<middle; i++)
        {
            String tempKEY = middleRotor.get(0).key;
            for (int n = 0; n<25; n++) {
                middleRotor.get(i).key = middleRotor.get(i+1).key;
            }
            middleRotor.get(25).key = tempKEY;
            midWheel++;
        }
        for (int i = 0; i<right; i++)
        {
            String tempKEY = rightRotor.get(0).key;
            for (int n = 0; n<25; n++) {
                rightRotor.get(i).key = rightRotor.get(i+1).key;
            }
            rightRotor.get(25).key = tempKEY;
            largeWheel++;
        }
    }
    */

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 	System.out.println ("Enigma(rip off): only works with letters, numbers and symbols will not be encrypted");
        int add = 0;
        //this will set up all rotors and and reflectors
        for (int i = 65; i<91; i++)
        {
            rotor1.add(new Sets((char)i+"" ,rotor1key.charAt(add)+""));
            rotor2.add(new Sets((char)i+"" ,rotor2key.charAt(add)+""));
            rotor3.add(new Sets((char)i+"" ,rotor3key.charAt(add)+""));
            rotor4.add(new Sets((char)i+"" ,rotor4key.charAt(add)+""));
            rotor5.add(new Sets((char)i+"" ,rotor5key.charAt(add)+""));
            reflectorBeta.add(new Sets((char)i+"" ,beta.charAt(add)+""));
            reflectorGamma.add(new Sets((char)i+"" ,gamma.charAt(add)+""));
            add++;
        }

        Scanner kb = new Scanner(System.in);

        //get message from user
        System.out.println ("what is your message?");
        String message = kb.nextLine();

        message = message.toUpperCase();

        //ask user if they want to use the plug board
        System.out.println("would you like to use the plugboard? \"y\" for yes and \"n\" for no");
        String plug;
        plug = kb.nextLine();
        if(plug.equals("y")){
            System.out.println("enter the connections matching \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"");
            plug = kb.nextLine();
            plug = plug.toUpperCase();
            add = 0;
            for (int i = 65; i<91; i++)
            {
                plugBoard.add(new Sets((char)i+"" ,plug.charAt(add)+""));
                add++;
            }
            usePlug = true;
        }


        //pick which rotors to use
        System.out.println ("select 3 rotors (1-5)\nexample input \"1 2 3\"");
        int left = kb.nextInt();
        int middle = kb.nextInt();
        int right = kb.nextInt();
        setRotors(left,middle,right);

        //SETTING ROTOR LOCATION
        System.out.println ("set rotor loc (0-25)\nexample input \"0 0 0\"");
        left = kb.nextInt();
        middle = kb.nextInt();
        right = kb.nextInt();
        int turns = left + middle*25 + right * 25 * 25;
        for (int i = 0; i < turns; i++) {
            rotate();
        }

        System.out.println ("pick reflector (beta,gamma)");
        if(kb.next().equals("beta"))
        {
            reflector=reflectorBeta;
        }else {
            reflector = reflectorGamma;
        }

        System.out.println("encrypt(e) or decrypt(d)");
        if(kb.next().equals("e"))
            System.out.println ("your encrypted message: \n" + encrypt(message));
        else
            System.out.println ("your message: \n" + decrypt(message));



    }
}