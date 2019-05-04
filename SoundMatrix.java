import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import java.util.ArrayList;
public class SoundMatrix extends JFrame implements Runnable, ActionListener
{
	JPanel panel=new JPanel();
	JMenuBar menuBar=new JMenuBar();
	JMenu addCol, removeCol;
	AudioClip notes[]=new AudioClip[13];
	String noteNames[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C"};
	ArrayList<Beat> beats = new ArrayList<>();
	boolean notStopped=true;
	JFrame frame=new JFrame("SoundMATRIX");
	Thread timing;

	public SoundMatrix(){
		frame.setSize(400,650);
		try{	
			for(int i=0; i<notes.length; i++)
				notes[i] = JApplet.newAudioClip(new URL("file:not"+i+".wav"));
		}catch(MalformedURLException mue){
			System.out.println("File not found");
		}
		panel.setLayout(new GridLayout(13,9,10,10)); //the last two numbers "space out" the buttons

		for(int i=0; i<8; i++)
			beats.add(new Beat());

		for(int r = 0; r<13; r++){
			panel.add(new JLabel(noteNames[12-r]));
			for(int c=0; c<beats.size(); c++){
				panel.add(beats.get(c).getButton(r));
			}
		}

		/*for(int y=0;y<buttons.length;y++){
			panel.add(new JLabel(noteNames[12-y]));
			for(int x=0;x<buttons[y].length;x++){
				buttons[y][x]=new JToggleButton();
				panel.add(buttons[y][x]);
			}
		}Original Method using 2D Button Array*/

		addCol = new JMenu("Add Column");
		addCol.addActionListener(this);
		removeCol = new JMenu("Remove Column");
		removeCol.addActionListener(this);

		menuBar.add(addCol);

		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timing = new Thread(this);
		timing.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	public void run(){
		do{
			try{	
				for(int b = 0; b<beats.size(); b++){
					JToggleButton[] buttons = beats.get(b).getButtons();
					for(int t=0; t<buttons.length; t++){
						if(buttons[t].isSelected())
						{ 
							notes[t].stop();
							notes[t].play();
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