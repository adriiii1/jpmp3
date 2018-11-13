package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class playerThread extends Thread{
    private File[] mp3files;
    private int sel;
    protected AdvancedPlayer player;
    private int pausedMoment=0;
    private volatile boolean running=true;
    private int pos=0;

    public playerThread(File[] mp3files, int sel,int pos){
        this.mp3files=mp3files;
        this.sel=sel;
        this.pos=pos;
    }

    @Override
    public void run(){
        while(running){
            try {
                FileInputStream ar = new FileInputStream(mp3files[sel].getAbsoluteFile().getAbsolutePath());
                this.player=new AdvancedPlayer(ar);
                System.out.println("Now playing: "+mp3files[sel].getName());
                player.play(pos,Integer.MAX_VALUE);
            }catch (JavaLayerException e) {
                e.printStackTrace();
            }catch (FileNotFoundException ex){
                ex.printStackTrace();
            }
        }
    }

    public void pausePlay(){
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackFinished(PlaybackEvent playbackEvent) {
                pausedMoment=playbackEvent.getFrame();
            }
        });
        player.stop();
        running=false;
        System.out.println("Paused playing of "+mp3files[sel].getName());
    }

    public void stopPlay(){
        running=false;
        player.close();
        System.out.println("Stopped playing of "+mp3files[sel].getName());
    }

    public int getPausedMoment(){
        return pausedMoment;
    }
}
