package text;

import java.util.Scanner;

public class Text {

    public Scanner sc;
	
    public Text(){
    	sc = new Scanner(System.in);
    }

    /*
    Used in pretty much all non error related text printing.
     */
    public void print(String s){
        System.out.println("[P] " + s);
    }

    /*
    Used when printing error messages.
     */
    public void error(String s)
    {
        System.out.println("[E] " + s);
    }

    public void seperator(){
        System.out.println("============================");
    }

    public void blank(){
        System.out.println();
    }

    /*
    Inserts text between two seperators.
     */
    public void seperateText(String s){
        this.blank();
        this.seperator();
        this.print(s);
        this.seperator();
        this.blank();
    }

    public String getStringInput(String s){
        this.print(s);
        String n = sc.nextLine();
        return n;
    }

}
