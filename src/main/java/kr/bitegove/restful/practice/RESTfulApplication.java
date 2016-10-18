package kr.bitegove.restful.practice;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
           return enterprisesAmount("10");
        }

        //Using Request HTTP Method followed Basic concept.
        @RequestMapping(value = "enterprises",method = RequestMethod.GET, params = {"amount"})
        public HttpEntity<Enterprise> enterprisesAmount(@RequestParam("amount") String amount) {
            //Removing ; and Translating String to int
            int cleanAmount = Integer.parseInt(amount.replaceAll("[^0-9]", ""));

            System.out.println(cleanAmount);
            Enterprise enterprise = new Enterprise();
            List<EnModel> en = enterpriseMapper.findAll(cleanAmount);
            enterprise.setEnInfo(en);

            //Adding URI on the document level as Information
            enterprise.add(linkTo(EnterpriseController.class).withRel("endpoint"));
            enterprise.add(linkTo(methodOn(EnterpriseController.class).enterprisesAmount(amount)).withSelfRel());

            //Using Response HTTP Method followed Basic concept.
            return new ResponseEntity<Enterprise>(enterprise, HttpStatus.OK);
        }

        //Using Request HTTP Method followed Basic concept.
        //@Overload
        @RequestMapping(value = "enterprises",method = RequestMethod.GET, params = {"name"})
        public HttpEntity<Enterprise> enterprisesName(@RequestParam("name") String name) {

            Enterprise enterprise = new Enterprise();
            List<EnModel> en = enterpriseMapper.findByName(name);
            enterprise.setEnInfo(en);

            //Adding URI on the document level as Information
            enterprise.add(linkTo(EnterpriseController.class).withRel("endpoint"));
            enterprise.add(linkTo(methodOn(EnterpriseController.class).enterprisesName(name)).withSelfRel());

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
        //HttpEntity의 제네릭 타입은 지정하지 않는다.
        @RequestMapping(value = "enterprises",method = RequestMethod.GET, params = {"amount","format"})
        public HttpEntity enterprises(@RequestParam("amount") String amount, @RequestParam("format") String format) {

            Enterprise enterprise = enterprisesAmount(amount).getBody();
            // create an empty Model
            Model model = ModelFactory.createDefaultModel();

            if (format.equals("rdf;") || format.equals("RDF;")) {
               for (int indexNum = 0;indexNum<enterprise.getEnInfo().size();indexNum++) {
                   // some definitions
                   EnModel enModel = enterprise.getEnInfo().get(indexNum);
                   String companyURI = enModel.getLink("self").getHref();
                   String name = enModel.getName();
                   String city = enModel.getCITY();

                   // create the resource
                   //   and add the properties cascading style
                            model.createResource(companyURI)
                           .addProperty(VCARD.Region, city)
                           .addProperty(VCARD.NAME, name);

                   //이런식으로 하위 등록자를 추가 가능하다.
                         /*  .addProperty(VCARD.N,
                                   model.createResource()
                                           .addProperty(VCARD.Given, givenName)
                                           .addProperty(VCARD.Family, city));*/
               }
                // Create a stream to hold the output
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                // Tell Java to use your special stream
                System.setOut(ps);
                // Print some output: goes to your special stream
                model.write(ps);
                String s = baos.toString();
                return new ResponseEntity<String>(s, HttpStatus.OK);
            } else {
                return enterprisesAmount(amount);
            }
        }
    }
    //문자열값이 숫자인지 문자인지 확인 + ex) 123;
    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+"+";");
    }
}
