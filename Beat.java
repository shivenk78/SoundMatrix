import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import java.awt.*;
import java.util.ArrayList;
public class Beat{

    private int[] notes;
    private JToggleButton[] buttons;

    final private static Dimension BUTTON_DIMENSION = new Dimension(30,30);

    public Beat(){
        notes = new int[13];
        for(int i=0; i<notes.length-1; i++)
            notes[i] = 0;
        createButtons();
        deselect();
    }
    public Beat(int notes[]){
        this.notes = notes;
        createButtons();
        for(int i=0; i<buttons.length; i++){
            if(notes[i] == 1)
                buttons[i].setSelected(true);
        }
        deselect();
    }

    private void createButtons(){
        buttons = new JToggleButton[13];
        for(int i=0; i<notes.length; i++){
            buttons[i] = new JToggleButton();
            buttons[i].setPreferredSize(BUTTON_DIMENSION);
        }
    }

    public void select(){
        for(JToggleButton b : buttons){
            b.setBorder(BorderFactory.createLineBorder(Color.RED));
            b.setPreferredSize(BUTTON_DIMENSION);
        }
    }

    public void deselect(){
        for(JToggleButton b : buttons){
            b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            b.setPreferredSize(BUTTON_DIMENSION);
        }
    }

    public JToggleButton[] getButtons(){
        return buttons;
    }

    public JToggleButton getButton(int i){
        return buttons[i];
    }

    public int[] getNotes(){
        for(int i=0; i<buttons.length; i++)
            notes[i] = (buttons[i].isSelected()) ? 1 : 0;
        return notes;
    }

    public int getNote(int i){
        getNotes();
        return notes[i];
    }

    public static ArrayList<Beat> beatsFromArray(int[][] array){
        ArrayList<Beat> beatList = new ArrayList<>();
        for(int c=0; c<array[0].length; c++){
            int[] noteArr = new int[array.length];
            for(int r=0; r<array.length; r++){
                noteArr[r] = array[r][c];
            }
            beatList.add(new Beat(noteArr));
        }

        return beatList;
    }

    public static int[][] arrayFromBeats(ArrayList<Beat> beats){
        int[][] arr = new int[13][beats.size()];
        for(int r=0; r<arr.length; r++)
            for(int c=0; c<arr[r].length; c++)
                arr[r][c] = beats.get(c).getNote(r);
        return arr;
    }
}