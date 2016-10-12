package kr.bitegove.restful.practice;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by jerry on 2016-10-12.
 */
public class EnModel extends ResourceSupport {

    private Long id;
    private String CITY;
    private String Name;

    public EnModel(){
        this.add(linkTo(RESTfulApplication.EnterpriseController.class).withRel("endpoint"));
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY+"156";
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(Long id) {
        this.id = id;
        this.add(linkTo(methodOn(RESTfulApplication.EnterpriseController.class).findEnterpriseById(this.id)).withSelfRel());
    }
}
