import javax.swing.JToggleButton;

public class Beat{

    private int[] notes;
    private JToggleButton[] buttons;

    //TODO: make notes optional, only if manually entered (i.e. from saved file) otherwise default to all buttons off

    public Beat(int notes[]){
        this.notes = notes;
        createButtons();
    }
    public Beat(String str){
        notes = new int[str.length()];
        for(int i=0; i<str.length()-1; i++)
            notes[i] = (str.substring(i,i+1).equals("1")) ? 1 : 0;
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