package SMSVoting.Model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@JsonAutoDetect
public class Poll
{
    @Id
    @JsonProperty("id")
    String id;
    @NotBlank @NotNull
    @JsonProperty("question")
    private String question;
    @NotBlank @NotNull
    @JsonProperty("started_at")
    private String startedAt;
    @NotBlank @NotNull
    @JsonProperty("expired_at")
    private String expiredAt;
    @NotNull
    @JsonProperty("choice")
    private List<String> choice = new ArrayList<String>();
    @JsonProperty("results")
    private List<Integer> results = new ArrayList<Integer>();

    private boolean resultProduced;


    public Poll() {
    }

    public Poll(String id,String question, String startedAt, String expiredAt, List<String> choice) {
        this.id=id;
        this.question = question;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.choice = choice;
        for(String a : choice)
            results.add(0);

        resultProduced =  false;
    }

    public Poll(    @JsonProperty("question") String question,
                    @JsonProperty("started_at") String startedAt,
                    @JsonProperty("expired_at") String expiredAt,
                    @JsonProperty("choice") List<String> choice) {
        this.question = question;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.choice = choice;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }
    /**

     *
     * @return
     * The question
     */
    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    /**
     *
     * @param question
     * The question
     */
    @JsonProperty("question")
    public void setQuestion(String question) {
        this.question = question;
    }


    /**
     *
     * @return
     * The startedAt
     */
    @JsonProperty("started_at")
    public String getStartedAt() {
        return startedAt;
    }

    /**
     *
     * @param startedAt
     * The started_at
     */
    @JsonProperty("started_at")
    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }



    /**
     *
     * @return
     * The expiredAt
     */
    @JsonProperty("expired_at")
    public String getExpiredAt() {
        return expiredAt;
    }

    /**
     *
     * @param expiredAt
     * The expired_at
     */
    @JsonProperty("expired_at")
    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }



    /**
     *
     * @return
     * The choice
     */
    @JsonProperty("choice")
    public List<String> getChoice() {
        return choice;
    }

    /**
     *
     * @param choice
     * The choice
     */
    @JsonProperty("choice")
    public void setChoice(List<String> choice) {
        this.choice = choice;
    }

    @JsonProperty("results")
    public List<Integer> getResults() {
        return results;
    }
    @JsonProperty("results")
    public void setResults(List<Integer> results) {
        this.results = results;
    }


    public Map<String,Object> toMapWithResult(){

        Map<String,Object> map = new HashMap<>();

        map.put("id",getId());
        map.put("question",getQuestion());
        map.put("started_at",getStartedAt());
        map.put("expired_at",getExpiredAt());
        map.put("choice",getChoice());
        map.put("results",getResults());

        return map;

    }

    public Map<String,Object> toMapWithoutResult(){

        Map<String,Object> map = new HashMap<>();

        map.put("id",getId());
        map.put("question",getQuestion());
        map.put("started_at",getStartedAt());
        map.put("expired_at", getExpiredAt());
        map.put("choice", getChoice());

        return map;

    }


    public void voteChoice(int choiceno) {
        int value = results.get(choiceno);
        value++;
        results.remove(choiceno);

        results.add(choiceno, value);

    }



    public boolean isResultProduced() {
        return resultProduced;
    }

    public void setResultProduced(boolean resultProduced) {
        this.resultProduced = resultProduced;
    }





    public String getchoiceResultPairs(){

        StringBuilder choiceResult = new StringBuilder();
        for(int i=0;i<choice.size();i++)
        {
            choiceResult.append(choice.get(i)+"="+results.get(i));

            if(i<choice.size()-1)
                choiceResult.append(",");

        }

        return choiceResult.toString();
    }


}
