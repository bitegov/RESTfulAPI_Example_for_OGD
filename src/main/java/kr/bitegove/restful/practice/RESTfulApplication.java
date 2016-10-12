package kr.bitegove.restful.practice;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by jerry on 2016-09-27.
 */
@SpringBootApplication
public class RESTfulApplication {
    public static void main(String[] args) {
        SpringApplication.run(RESTfulApplication.class, args);
    }

    @RestController
    public static class EnterpriseController {

        @Autowired
        EnterpriseMapper enterpriseMapper;

        @RequestMapping(value = "enterprises",method = RequestMethod.GET)
        public HttpEntity<Enterprise> enterprises() {
           return enterprises(10);
        }

        //Using Request HTTP Method followed Basic concept.
        @RequestMapping(value = "enterprises",method = RequestMethod.GET, params = {"amount"})
        public HttpEntity<Enterprise> enterprises(@RequestParam("amount") int amount) {

           /* //Removing ; and Translating String to int
            int cleanAmount = Integer.parseInt(amount.replaceAll("[^0-9]", ""));*/
            System.out.println(amount);
            Enterprise enterprise = new Enterprise();
            List<EnModel> en = enterpriseMapper.findAll(amount);
            enterprise.setEnInfo(en);

            //Adding URI on the document level as Information
            enterprise.add(linkTo(EnterpriseController.class).withRel("endpoint"));
            enterprise.add(linkTo(methodOn(EnterpriseController.class).enterprises(amount)).withSelfRel());

            //Using Response HTTP Method followed Basic concept.
            return new ResponseEntity<Enterprise>(enterprise, HttpStatus.OK);
        }

        //Using Request HTTP Method followed Basic concept.
        //@Overload
        @RequestMapping(value = "enterprises",method = RequestMethod.GET, params = {"name"})
        public HttpEntity<Enterprise> enterprises(@RequestParam("name") String name) {

            Enterprise enterprise = new Enterprise();
            List<EnModel> en = enterpriseMapper.findByName(name);
            enterprise.setEnInfo(en);

            //Adding URI on the document level as Information
            enterprise.add(linkTo(EnterpriseController.class).withRel("endpoint"));
            enterprise.add(linkTo(methodOn(EnterpriseController.class).enterprises(name)).withSelfRel());

            //Using Response HTTP Method followed Basic concept.
            return new ResponseEntity<Enterprise>(enterprise, HttpStatus.OK);
        }

        @RequestMapping("enterprise/{id}")
        public HttpEntity<EnModel> findEnterpriseById(@PathVariable("id") Long id) {
            EnModel  enModel = enterpriseMapper.findById(id);
            ResponseEntity<EnModel> responseEntity;
            if(enModel.getCITY()==null){
                enModel = new EnModel();
            }
            return new ResponseEntity<EnModel>(enModel, HttpStatus.OK);
        }


        @Mapper
        interface EnterpriseMapper {
            @Select("SELECT ID, CITY, NAME FROM enterprises LIMIT #{amount}")
            List<EnModel> findAll(int amount);

            @Select("SELECT ID, CITY, NAME FROM enterprises WHERE ID = #{id}")
            EnModel findById(Long id);

            @Select("SELECT ID, CITY, NAME FROM enterprises WHERE NAME = #{name}")
            List<EnModel> findByName(String name);
        }
//// TODO: 2016-10-12 RDF 포맷지원
/*        @RequestMapping("enterprises")
        public HttpEntity<Enterprise> enterprises(@RequestParam("amount") String amount, @RequestParam("format") String format) {
            if (format.equals("rdf") || format.equals("RDF")) {
                HttpEntity<Enterprise> enterprises = enterprises(amount);
                enterprises.getBody();

                return enterprises;
            } else {
                return enterprises(amount);
            }
        }*/
    }

}
