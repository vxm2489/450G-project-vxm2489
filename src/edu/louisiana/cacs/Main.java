package edu.louisiana.cacs;

import edu.louisiana.cacs.csce450GProject.Parser;

public class Main{
    public static void main(String[] args){
        Parser myParser = new Parser(args[0]);
        myParser.parse();
    }
}