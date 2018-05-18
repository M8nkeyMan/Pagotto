/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author pago1
 */
public class Hobby {

    private int i_idHobby;
    private String s_nomeHobby,s_descrizioneHobby;

    public Hobby(ResultSet rs) throws SQLException {
        i_idHobby = rs.getInt("IdHobby");
        s_nomeHobby = rs.getString("NomeHobby");
        s_descrizioneHobby = rs.getString("DescrizioneHobby");
    }

    public Hobby(int i_idHobby, String s_nomeHobby) {
        this.i_idHobby = i_idHobby;
        this.s_nomeHobby = s_nomeHobby;
    }

    public int getI_idHobby() {
        return i_idHobby;
    }

    public void setI_idHobby(int i_idHobby) {
        this.i_idHobby = i_idHobby;
    }

    public String getS_nomeHobby() {
        return s_nomeHobby;
    }

    public void setS_nomeHobby(String s_nomeHobby) {
        this.s_nomeHobby = s_nomeHobby;
    }

    public String getS_descrizioneHobby() {
        return s_descrizioneHobby;
    }

    public void setS_descrizioneHobby(String s_descrizioneHobby) {
        this.s_descrizioneHobby = s_descrizioneHobby;
    }
    
}
