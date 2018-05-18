package entities;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Pagotto Emanuele
 */
public class Persona {

    private int i_id;
    private String s_nome, s_cognome, s_sesso, s_email, s_password,s_dataNascita;

    public Persona(int i_id, String s_nome, String s_cognome, String s_sesso, String s_email, String s_password, String s_dataNascita) {
        this.i_id = i_id;
        this.s_nome = s_nome;
        this.s_cognome = s_cognome;
        this.s_sesso = s_sesso;
        this.s_email = s_email;
        this.s_password = s_password;
        this.s_dataNascita = s_dataNascita;
    }

    public Persona(ResultSet rs) throws SQLException {
        i_id = rs.getInt("IdPersona");
        s_nome = rs.getString("Nome");
        s_cognome = rs.getString("Cognome");
        s_email = rs.getString("Email");
        s_sesso = rs.getString("Sesso");
        s_dataNascita = rs.getString("DataNascita");
    }

    public int getI_id() {
        return i_id;
    }

    public void setI_id(int i_id) {
        this.i_id = i_id;
    }

    public String getS_nome() {
        return s_nome;
    }

    public void setS_nome(String s_nome) {
        this.s_nome = s_nome;
    }

    public String getS_cognome() {
        return s_cognome;
    }

    public void setS_cognome(String s_cognome) {
        this.s_cognome = s_cognome;
    }

    public String getS_sesso() {
        return s_sesso;
    }

    public void setS_sesso(String s_sesso) {
        this.s_sesso = s_sesso;
    }

    public String getS_dataNascita() {
        return s_dataNascita;
    }

    public void setS_dataNascita(String s_dataNascita) {
        this.s_dataNascita = s_dataNascita;
    }

    public String getS_email() {
        return s_email;
    }

    public void setS_email(String s_email) {
        this.s_email = s_email;
    }

    public String getS_password() {
        return s_password;
    }

    public void setS_password(String s_password) {
        this.s_password = s_password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Persona other = (Persona) obj;
        if (this.i_id != other.i_id) {
            return false;
        }
        return true;
    }

}
