package entities;

/**
 * @author Pagotto Emanuele
 */
public class Result {
    
    private int i_id;
    private String s_text_main, s_text_secondary;
    
    public Result(Persona p){
        this.i_id = p.getI_id();
        this.s_text_main = p.getS_nome()+" "+p.getS_cognome();
        this.s_text_secondary = p.getS_email();
    }
    
    public Result(Hobby h){
        this.i_id = h.getI_idHobby();
        this.s_text_main = h.getS_nomeHobby();
        this.s_text_secondary = h.getS_descrizioneHobby();
    }

    public Result() {
    }

    public Result(int i_id, String s_text_main, String s_text_secondary) {
        this.i_id = i_id;
        this.s_text_main = s_text_main;
        this.s_text_secondary = s_text_secondary;
    }

    public int getI_id() {
        return i_id;
    }

    public void setI_id(int i_id) {
        this.i_id = i_id;
    }

    public String getS_text_main() {
        return s_text_main;
    }

    public void setS_text_main(String s_text_main) {
        this.s_text_main = s_text_main;
    }

    public String getS_text_secondary() {
        return s_text_secondary;
    }

    public void setS_text_secondary(String s_text_secondary) {
        this.s_text_secondary = s_text_secondary;
    }
}
