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

    private List<EnModel> enInfo;

   /* private String amount;
    @JsonCreator
    public Enterprise(@JsonProperty("amount") String amount){
        this.amount = amount;
    }*/

    public List<EnModel> getEnInfo() {
        return enInfo;
    }

    public void setEnInfo(List<EnModel> enInfo) {
        this.enInfo = enInfo;
    }




}
