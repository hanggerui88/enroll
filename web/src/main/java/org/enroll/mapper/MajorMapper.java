package org.enroll.mapper;

import org.apache.ibatis.annotations.Param;
import org.enroll.excel.pojo.ExcelMajor;
import org.enroll.pojo.Major;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorMapper {

    void insertMajor( @Param("majorList") List<ExcelMajor> list);
    //插入一组ExcelMajor对象到数据库中

    List<ExcelMajor> getMajorPlanForEnroll();

    List<ExcelMajor> getMajorPlanForAdjust();

    void updateStudentCount(@Param("majors") List<ExcelMajor> majors);

    void resetMajor();

    void resetTable();

}
