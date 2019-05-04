import javax.swing.JToggleButton;

public class Beat{

    private int[] notes;
    private JToggleButton[] buttons;

    //TODO: make notes optional, only if manually entered (i.e. from saved file) otherwise default to all buttons off

    public Beat(int notes[]){
        this.notes = notes;
        createButtons();
    }
    public Beat(){
        notes = new int[13];
        for(int i=0; i<notes.length-1; i++)
            notes[i] = 0;
        createButtons();
    }

    private void createButtons(){
        buttons = new JToggleButton[13];
        for(int i=0; i<notes.length; i++){
            buttons[i] = new JToggleButton();
        }
    }

    public JToggleButton[] getButtons(){
        return buttons;
    }

    public JToggleButton getButton(int i){
        return buttons[i];
    }
}