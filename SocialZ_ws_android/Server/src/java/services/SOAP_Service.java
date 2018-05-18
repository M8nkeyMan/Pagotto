/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import SQLhelp.DAO;
import entities.Hobby;
import entities.Persona;
import entities.Result;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author pago1
 */
@WebService(serviceName = "SOAP_Service")
public class SOAP_Service {

    /**
     * Web service operation
     *
     * @param id
     * @return
     */
    @WebMethod(operationName = "getPersonaById")
    public Persona getPersonaById(@WebParam(name = "id") int id) {
        return DAO.Person.getPersonById(id);
    }

    @WebMethod(operationName = "getHobbies")
    public ArrayList<Result> getHobbies() {
        ArrayList<Hobby> hobbies = DAO.Pastime.getHobbies();
        ArrayList<Result> ris = new ArrayList<>();
        for (Hobby hobby : hobbies) {
            ris.add(new Result(hobby));
        }
        return ris;
    }

    @WebMethod(operationName = "getMailingList")
    public ArrayList<Result> getMailingList(@WebParam(name = "idHobby") int idHobby) {
        ArrayList<Persona> people = DAO.Person.getPersonByHobby(idHobby);
        ArrayList<Result> ris = new ArrayList<>();
        for(Persona person:people){
            ris.add(new Result(person));
        }
        return ris;
    }
}
