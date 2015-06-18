package sms.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neerajakukday on 2/23/15.
 */
public class Polls {

    private String id;

    @NotBlank
    private String question;
    @JsonProperty("started_at")
    @NotBlank
    private String started_at;
    @JsonProperty("expired_at")
    @NotBlank
    private String expired_at;
    @NotEmpty
    private ArrayList<String> choice = new ArrayList<String>();

    private List<Integer> results = new ArrayList<Integer>();


    public String getId() {
        return id;
    }
    public void setId(String id) {

        this.id = id;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonProperty("started_at")
    public String getStartedAt() {
        return started_at;
    }
    @JsonProperty("started_at")
    public void setStartedAt(String started_at) {
        this.started_at = started_at;
    }

    @JsonProperty("expired_at")
    public String getExpiredAt() {
        return expired_at;
    }

    @JsonProperty("expired_at")
    public void setExpiredAt(String expired_at) {
        this.expired_at = expired_at;
    }


    public ArrayList getChoice() {
        return choice;
    }

    public void setChoice(ArrayList choice) {
        this.choice = choice;
    }


    public List getResults() {
        if(results != null)
        {
        return results;
        }
        else return null;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public Polls(){}

}
