import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import java.util.ArrayList;
public class SoundMatrix extends JFrame implements Runnable
{
	JPanel panel=new JPanel();
	AudioClip notes[]=new AudioClip[13];
	String noteNames[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C"};
	ArrayList<Beat> beats = new ArrayList<>();
	boolean notStopped=true;
	JFrame frame=new JFrame();
	Thread timing;

	public SoundMatrix(){
		try{	
			for(int i=0; i<notes.length; i++)
				notes[i] = JApplet.newAudioClip(new URL("file:not"+i+".wav"));
		}catch(MalformedURLException mue){
			System.out.println("File not found");
		}
		panel.setLayout(new GridLayout(13,9,10,10)); //the last two numbers "space out" the buttons
		for(int y=0;y<buttons.length;y++){
			panel.add(new JLabel(noteNames[12-y]));
			for(int x=0;x<buttons[y].length;x++){
				buttons[y][x]=new JToggleButton();
				panel.add(buttons[y][x]);
			}
		}
		this.add(panel);
		setSize(400,650);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timing = new Thread(this);
		timing.start();
	}

	public void run(){
		do{
			try{	
				for(int c=0; c<buttons[0].length; c++){
					for(int r=0; r<buttons.length; r++){
						if(buttons[r][c].isSelected())
						{ 
							notes[r].stop();
							notes[r].play();
						}
					}
					timing.sleep(100);
				}
			}
			catch(InterruptedException e){}
		}while(notStopped);

	}

	public static void main(String args[]){
		SoundMatrix app=new SoundMatrix();

	}
}