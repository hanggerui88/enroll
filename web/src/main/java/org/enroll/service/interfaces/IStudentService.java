package org.enroll.service.interfaces;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.enroll.pojo.StudentResult;
import org.enroll.utils.QueryResultOption;

import java.util.List;

public interface IStudentService {

    PageInfo getStudentRaw(int currentPage);


    PageInfo getExitStudentRaw(int currentPage);

    void doEnroll();

    void doAdjust();

    PageInfo getResult(int currentPage, boolean desc, QueryResultOption option);

    void reset();

    void formallyReady();

    PageInfo searchStudent(int currentPage, String keyword);

    PageInfo searchStudentByCandidate(int currentPage, String keyword);

//    List<StudentResult> findByStudentName(@Param("studentName") String studentName);

//    List<StudentResult> findByStudentId(@Param("studentId") int studentId);

}
