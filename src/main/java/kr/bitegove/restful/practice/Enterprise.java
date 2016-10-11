package kr.bitegove.restful.practice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016-10-11.
 */
public class Enterprise extends ResourceSupport {

    private List<Map<String, Object>> enInfo;
    private String amount;

    @JsonCreator
    public Enterprise(@JsonProperty("amount") String amount){
        this.amount = amount;
    }

    public List<Map<String, Object>> getEnInfo() {
        return enInfo;
    }

    public void setEnInfo(List<Map<String, Object>> enInfo) {
        this.enInfo = enInfo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


}
