package kr.bitegove.restful.practice.Controller;

import kr.bitegove.restful.practice.Mapper.EnterpriseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jerry on 2016-09-27.
 */
@RestController
public class RESTfulAPI {

    @Autowired
    EnterpriseMapper enterpriseMapper;

    @RequestMapping("/")
    public String index(){
        String temp = enterpriseMapper.testEn();
        return temp;
    }
}
