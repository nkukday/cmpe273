package sms.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by neerajakukday on 2/22/15.
 */
public class Moderator {

    private int id = 0;

    private String name;
    @NotBlank
    @Email
    private  String email;
    @NotBlank
    private String password;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Timestamp created_at;

    public int getId() {
        return id;
    }
    public void setId(int id) {

        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {

        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;

    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;

    }


    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    public Timestamp getCreated_at() {
        return created_at;
    }

    public Moderator(){
        this.created_at = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

    }


}
