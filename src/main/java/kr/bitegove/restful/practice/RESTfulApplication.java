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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


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

        @RequestMapping("enterprises")
        public HttpEntity<Enterprise> enterprises(@RequestParam("amount") String amount) {
            //뒤에 불필요하게 붙는 ; 제거 및 int형으로 변환
            int cleanAmount = Integer.parseInt(amount.replaceAll("[^0-9]", ""));
            System.out.println(cleanAmount);
            Enterprise enterprise = new Enterprise(amount);
            List<Map<String, Object>> en =enterpriseMapper.findAll(cleanAmount);
            enterprise.setEnInfo(en);
            //링크를 추가 함.
            enterprise.add(linkTo(EnterpriseController.class).withRel("endpoint"));
            enterprise.add(linkTo(methodOn(EnterpriseController.class).enterprises(amount)).withSelfRel());
            return new ResponseEntity<Enterprise>(enterprise, HttpStatus.OK);
        }

        @RequestMapping("enterprises/{id}")
        public Map<String, Object> member(@PathVariable("id") Long id) {
            return enterpriseMapper.findById(id);
        }
    }

    @Mapper
    interface EnterpriseMapper {
        @Select("SELECT CITY, NAME FROM enterprises LIMIT #{amount}")
        List<Map<String, Object>> findAll(int amount);

        @Select("SELECT CITY, NAME, PHONE FROM MEMBER WHERE ID = #{id}")
        Map<String, Object> findById(Long id);
    }


}
