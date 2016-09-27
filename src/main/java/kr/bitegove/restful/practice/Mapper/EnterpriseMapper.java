package kr.bitegove.restful.practice.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by jerry on 2016-09-27.
 */
@Mapper
public interface EnterpriseMapper {
    @Select("SELECT NAME FROM ENTERPRISES WHERE id = 30")
    public String testEn();
}
