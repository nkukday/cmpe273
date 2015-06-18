package SMSVoting.Model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Document
@JsonAutoDetect
public class Moderator {
    @Id
    private long id;
    private String name;
    @NotBlank @NotNull @Email
    private String email;
    @NotBlank @NotNull
    private String password;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String created_at;

    private ArrayList<String> polls=new ArrayList<String>();



    public Moderator() {
    }

    public Moderator(@JsonProperty("name") String name,
                     @JsonProperty("email") String email,
                     @JsonProperty("password") String password) {

        this.name = name;
      this.email = email;
      this.password = password;
   }

    public Moderator(long id, String name, String email, String password, String created_At) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_At;
    }
    //    /**
     /*
     * @return
     * The id
     */
    @JsonProperty("id")
    public long getId() {
        return id;
    }

   /**
    *
    * @param id
    * The id
    */
    @JsonProperty("id")
    public void setId(long id) {
      this.id = id;
   }

   /**
    * 
    * @return
    * The name
    */
    @JsonProperty("name")
    public String getName() {
      return name;
   }

   /**
    * 
    * @param name
    * The name
    */
    @JsonProperty("name")
    public void setName(String name) {
      this.name = name;
   }

   /**
    * 
    * @return
    * The email
    */
    @JsonProperty("email")
    public String getEmail() {
      return email;
   }

   /**
    * 
    * @param email
    * The email
    */
    @JsonProperty("email")
    public void setEmail(String email) {
      this.email = email;
   }

   /**
    * 
    * @return
    * The password
    */
    @JsonProperty("password")
    public String getPassword() {
      return password;
   }

   /**
    * 
    * @param password
    * The password
    */
    @JsonProperty("password")
    public void setPassword(String password) {
      this.password = password;
   }

   /**
    *
    * @return
    * The created_At
    */
    @JsonProperty("created_at")
    public String getCreatedAt() {
      return created_at;
   }

   /**
    *
    * @param createdAt
    * The created_at
    */
    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
      this.created_at = createdAt;
   }

    public ArrayList<String> getPolls() {
        return polls;
    }

    public void addPoll(String pollId)
    {
        polls.add(pollId);
    }

    public void removePoll(String pollId) {
        polls.remove(pollId);
    }

    public Map<String,Object> toMapWithOutResult(){

        Map<String,Object> map = new HashMap<>();

        map.put("id",getId());
        map.put("name",getName());
        map.put("email",getEmail());
        map.put("password",getPassword());
        map.put("created_at",getCreatedAt());
        return map;

    }
}

