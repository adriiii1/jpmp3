package com.company;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        clearScreen();
        File folder=new File("");
        Scanner sc=new Scanner(System.in);
	    if(args.length==0){
            System.out.println("Please specify a path");
            try{
                folder = new File(sc.nextLine());
            }catch(NullPointerException e){
                System.out.println("The path specified couldn't be found");
            }
	    }else{
	        try{
	            folder=new File(args[0]);
	        }catch(NullPointerException e){
                System.out.println("The path specified couldn't be found");
            }
        }
        clearScreen();
        File[] listOfFiles=folder.listFiles();
	    File[] mp3files=new File[listOfFiles.length];
	    int count=0;
        for(int i=0;i<listOfFiles.length;i++){
            if((listOfFiles[i].isFile())&&(getExt(listOfFiles[i]).equals(".mp3"))){
                mp3files[count]=listOfFiles[i];
                count++;
            }
        }
        System.out.println("Found "+mp3files.length+" songs");
        System.out.println("Num  | Song");
        for(int i=0;i<mp3files.length;i++){
            String spacing="";
            int numSpaces=4;
            for(int j=String.valueOf(i).length();j<=numSpaces;j++){
                spacing+=" ";
            }
            System.out.println(String.valueOf(i)+spacing+"- "+mp3files[i].getName());
        }
        int sel=sc.nextInt();
        boolean isNum=String.valueOf(sel).chars().allMatch(Character::isDigit);
        while(!isNum){
            System.out.println("Please, specify the song's number");
            sel=sc.nextInt();
            isNum=String.valueOf(sel).chars().allMatch(Character::isDigit);
        }
        playerThread playerThread=new playerThread(mp3files,sel,0);
        playerThread.start();
        boolean accpInput=false;
        String off="";
        while(!accpInput){
            System.out.println("s to stop, p to pause, q to quit");
            off=sc.next();
            if((!off.equals("s"))||(!off.equals("p"))||(!off.equals("q")));{
                accpInput=true;
            }
        }
        if(off.equals("s")){
            playerThread.stopPlay();
            clearScreen();
            printScreen(mp3files,sc);
        }else if(off.equals("p")){
            playerThread.pausePlay();
            System.out.println("r to resume");
            String newCommand=sc.next();
            if(newCommand.equals("r")){
                int pos=playerThread.getPausedMoment();
                playerThread newPlayer=new playerThread(mp3files,sel,pos);
                newPlayer.start();
            }
        }else if(off.equals("q")){
            playerThread.stopPlay();
            clearScreen();
            System.exit(1);
        }
    }

    public static void printScreen(File[] mp3files, Scanner sc){
        clearScreen();
        System.out.println("Found "+mp3files.length+" songs");
        System.out.println("Num  | Song");
        for(int i=0;i<mp3files.length;i++){
            String spacing="";
            int numSpaces=4;
            for(int j=String.valueOf(i).length();j<=numSpaces;j++){
                spacing+=" ";
            }
            System.out.println(String.valueOf(i)+spacing+"- "+mp3files[i].getName());
        }
        int sel=sc.nextInt();
        boolean isNum=String.valueOf(sel).chars().allMatch(Character::isDigit);
        while(!isNum){
            System.out.println("Please, specify the song's number");
            sel=sc.nextInt();
            isNum=String.valueOf(sel).chars().allMatch(Character::isDigit);
        }
        playerThread playerThread=new playerThread(mp3files,sel,0);
        playerThread.start();
        boolean accpInput=false;
        String off="";
        while(!accpInput){
            System.out.println("s to stop, p to pause, q to quit");
            off=sc.next();
            if((!off.equals("s"))||(!off.equals("p"))||(!off.equals("q")));{
                accpInput=true;
            }
        }
        if(off.equals("s")){
            playerThread.stopPlay();
            clearScreen();
            printScreen(mp3files,sc);
        }else if(off.equals("p")){
            playerThread.pausePlay();
            System.out.println("r to resume");
            String newCommand=sc.next();
            if(newCommand.equals("r")){
                int pos=playerThread.getPausedMoment();
                playerThread newPlayer=new playerThread(mp3files,sel,pos);
                playerThread.start();
                clearScreen();
                printScreen(mp3files,sc);
            }
        }else if(off.equals("q")){
            playerThread.stopPlay();
            clearScreen();
            System.exit(1);
        }
    }

    public static String getExt(File file){
        String arch=file.getName();
        int lastIndexOf=arch.lastIndexOf(".");
        if (lastIndexOf==-1){
            return "";
        }
        return arch.substring(lastIndexOf);
    }

    public static void clearScreen(){
        try{
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }else{
                System.out.print("\033[H\033[2J");
                //Runtime.getRuntime().exec("clear");
            }
        }catch (IOException | InterruptedException e){}
    }
}
