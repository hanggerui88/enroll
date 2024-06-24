package org.enroll.mapper;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.enroll.excel.pojo.ExcelStudent;
import org.enroll.pojo.StatisticsResult;
import org.enroll.pojo.StudentResult;
import org.enroll.utils.QueryResultOption;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface StudentMapper {

    void insertStudent(@Param("studentList") List<ExcelStudent> students);

    List<ExcelStudent> getStudentRaw();

    List<ExcelStudent> getExitStudentRaw();

    List<ExcelStudent> getStudentRawForEnroll(@Param("start") int start, @Param("size") int size);

    List<ExcelStudent> getStudentRawForAdjust(@Param("start") int start, @Param("size") int size);

    void updateAccepted(@Param("students") List<ExcelStudent> students);


    void updateAdjust(@Param("students") List<ExcelStudent> students);

    List<StudentResult> getStudent(@Param("desc") boolean desc, @Param("option") QueryResultOption option);


    List<StudentResult> getStudentForExport(@Param("start") int start, @Param("size") int size);

    List<ExcelStudent> getExitStudentForExport(@Param("start") int start, @Param("size") int size);

    void resetStudent();

    void resetTable();

    //单个查询

    List<StudentResult> searchStudentByCandidate(@Param("candidate") String candidate);

//    List<StudentResult> findByStudentId(@Param("studentId") int studentId);

    List<StudentResult> searchStudent(@Param("keyword") String keyword);}
