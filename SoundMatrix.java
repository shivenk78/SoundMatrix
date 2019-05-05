import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import java.net.*;
import java.util.ArrayList;

public class SoundMatrix extends JFrame implements Runnable, ActionListener, ChangeListener {
	// ADDONS LIST: 1) ScrollPane 2)Cursor moves along notes 3)Slider to adjust tempo 4)Play/Pause Button

	private JPanel panel, topPanel, buttonPanel;
	private JScrollPane scrollPane;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file, cols, songs;
	private JMenuItem loadFile, saveFile, addCol, removeCol, odeItem, maryItem, lightItem;
	private JButton clearButton, randButton, playPauseButton;
	private JSlider tempo;
	private AudioClip notes[] = new AudioClip[13];
	private String noteNames[] = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C" };
	private ArrayList<Beat> beats = new ArrayList<>();
	private JFrame frame = new JFrame("SoundMATRIX");
	private Thread timing;

	private int bpm = 120;
	private boolean notStopped = true;
	private boolean suspended = false;

	public SoundMatrix() {
		ImageIcon img = new ImageIcon("dot_matrix.png");
		frame.setIconImage(img.getImage());

		frame.setSize(570, 660);
		panel = new JPanel();
		scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		topPanel = new JPanel();
		buttonPanel = new JPanel();

		try {
			for (int i = 0; i < notes.length; i++)
				notes[i] = JApplet.newAudioClip(new URL("file:not" + i + ".wav"));
		} catch (MalformedURLException mue) {
			System.out.println("File not found");
		}
		panel.setLayout(new GridLayout(13, 14, 10, 10));

		for (int i = 0; i < 13; i++)
			beats.add(new Beat());

		for (int r = 0; r < 13; r++) {
			panel.add(new JLabel(noteNames[12 - r]));
			for (int c = 0; c < beats.size(); c++) {
				panel.add(beats.get(c).getButton(r));
			}
		}

		file = new JMenu("File");
		file.setToolTipText("Save or Load Songs as Text Files");
		loadFile = new JMenuItem("Load Song File");
		loadFile.addActionListener(this);
		saveFile = new JMenuItem("Save Song File");
		saveFile.addActionListener(this);

		cols = new JMenu("Columns");
		cols.setToolTipText("Add or Remove Beat Columns");
		addCol = new JMenuItem("Add Column");
		addCol.addActionListener(this);
		removeCol = new JMenuItem("Remove Column");
		removeCol.addActionListener(this);

		songs = new JMenu("Songs");
		songs.setToolTipText("Choose From Three Preset Songs");
		odeItem = new JMenuItem("Ode to Joy");
		odeItem.addActionListener(this);
		maryItem = new JMenuItem("Mary Had a Little Lamb");
		maryItem.addActionListener(this);
		lightItem = new JMenuItem("Light of the Seven (GoT)");
		lightItem.addActionListener(this);

		file.add(loadFile);
		file.add(saveFile);
		menuBar.add(file);
		cols.add(addCol);
		cols.add(removeCol);
		menuBar.add(cols);
		songs.add(odeItem);
		songs.add(maryItem);
		songs.add(lightItem);
		menuBar.add(songs);

		tempo = new JSlider(JSlider.HORIZONTAL, 10, 310, 120);
		tempo.setPreferredSize(new Dimension(150, 50));
		tempo.setMajorTickSpacing(100);
		tempo.setMinorTickSpacing(10);
		tempo.setPaintTicks(true);
		tempo.setPaintLabels(true);
		tempo.addChangeListener(this);

		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		randButton = new JButton("Random");
		randButton.addActionListener(this);
		playPauseButton = new JButton("Play/Pause");
		playPauseButton.addActionListener(this);

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(clearButton);
		buttonPanel.add(randButton);
		buttonPanel.add(new JLabel("Tempo (BPM):"));
		buttonPanel.add(tempo);
		buttonPanel.add(playPauseButton);
		buttonPanel.setLayout(new FlowLayout());

		topPanel.setLayout(new BorderLayout());
		topPanel.add(menuBar, BorderLayout.NORTH);
		topPanel.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timing = new Thread(this);
		timing.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addCol) {
			beats.add(new Beat());
			drawBeats();
		}
		if (e.getSource() == removeCol) {
			beats.remove(beats.size() - 1);
			drawBeats();
		}
		if (e.getSource() == clearButton) {
			for (int r = 0; r < 13; r++) {
				for (int c = 0; c < beats.size(); c++) {
					beats.get(c).getButton(r).setSelected(false);
				}
			}
		}
		if (e.getSource() == randButton) {
			for (int r = 0; r < 13; r++) {
				for (int c = 0; c < beats.size(); c++) {
					if (Math.random() <= 0.5)
						beats.get(c).getButton(r).setSelected(true);
					else
						beats.get(c).getButton(r).setSelected(false);
				}
			}
		}
		if(e.getSource() == playPauseButton){
			if(suspended){
				timing.resume();
				suspended = false;
			}else{
				timing.suspend();
				suspended = true;
			}
		}
		if (e.getSource() == loadFile) {
			beats = Beat.beatsFromArray(getIntsFromFile(getFileName(false)));
			drawBeats();
		}
		if (e.getSource() == saveFile)
			saveIntsToFile(getFileName(true));
		if (e.getSource() == odeItem) {
			beats = Beat.beatsFromArray(getIntsFromFile("odeToJoy.txt"));
			drawBeats();
		}
		if (e.getSource() == maryItem) {
			beats = Beat.beatsFromArray(getIntsFromFile("maryHadALittleLamb.txt"));
			drawBeats();
		}
		if (e.getSource() == lightItem) {
			beats = Beat.beatsFromArray(getIntsFromFile("lightOfTheSevenGOT.txt"));
			drawBeats();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tempo && !tempo.getValueIsAdjusting()){
			bpm = tempo.getValue();
		}
	}

	private void drawBeats() {
		scrollPane.remove(panel);
		panel = new JPanel();
		panel.setLayout(new GridLayout(13, beats.size() + 1, 10, 10));

		for (int r = 0; r < 13; r++) {
			panel.add(new JLabel(noteNames[12 - r]));
			for (int c = 0; c < beats.size(); c++) {
				panel.add(beats.get(c).getButton(r));
			}
		}
		scrollPane.setViewportView(panel);
		// frame.setSize((beats.size()+1)*50, 650);
		scrollPane.revalidate();
		frame.revalidate();
	}

	public void run() {
		while (notStopped) {
			try {
				for (int b = 0; b < beats.size(); b++) {
					JToggleButton[] buttons = beats.get(b).getButtons();
					beats.get(b).select();
					for (int t = 0; t < buttons.length; t++) {
						if (buttons[t].isSelected()) {
							notes[t].stop();
							notes[t].play();
						}
					}
					timing.sleep(getTempo());
					beats.get(b).deselect();
				}
			} catch (Exception e) {
			}
		}
	}

	public String getFileName(boolean save) {
		JFileChooser jfc = new JFileChooser(
				FileSystemView.getFileSystemView().getParentDirectory(new File("Beat.java")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		jfc.setFileFilter(filter);

		int returnValue = (save) ? jfc.showSaveDialog(null) : jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath().toString();
		}

		else
			return "error.txt";
	}

	public int[][] getIntsFromFile(String fileName) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			String text, output = "";
			while ((text = input.readLine()) != null) {
				output += text + "~";
			}

			String[] lines = output.split("~");
			int[][] arr = new int[lines.length][lines[0].length()];
			for (int c = 0; c < lines[0].length(); c++) {
				for (int r = 0; r < lines.length; r++) {
					arr[r][c] = Integer.parseInt(lines[r].substring(c, c + 1));
				}
			}
			return arr;
		} catch (IOException ioe) {
			return new int[13][1];
		}
	}

	public void saveIntsToFile(String fileName) {
		try {
			BufferedWriter OutputStream = new BufferedWriter(new FileWriter(fileName));
			OutputStream.write(arrayToString(Beat.arrayFromBeats(beats)));
			OutputStream.close();
		} catch (IOException ioe) {
		}
	}

	public String arrayToString(int[][] arr) {
		String str = "";
		for (int r = 0; r < arr.length; r++) {
			for (int c = 0; c < arr[r].length; c++)
				str += arr[r][c] + "";
			str += "\n";
		}
		return str;
	}

	private int getTempo(){
		double d = bpm;
		d = 60/d;
		int x = (int)(d*10);
		return x*100;
	}

	public static void main(String args[]) {
		SoundMatrix app = new SoundMatrix();
	}
}