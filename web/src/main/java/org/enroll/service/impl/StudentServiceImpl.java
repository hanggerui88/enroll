package org.enroll.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.enroll.excel.pojo.ExcelMajor;
import org.enroll.excel.pojo.ExcelStudent;
import org.enroll.mapper.MajorMapper;
import org.enroll.mapper.StatusMapper;
import org.enroll.mapper.StudentMapper;
import org.enroll.pojo.EnrollStatus;
import org.enroll.pojo.StudentResult;
import org.enroll.service.interfaces.IStudentService;
import org.enroll.utils.QueryResultOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private StatusMapper statusMapper;


    @Override
    public PageInfo getStudentRaw(int currentPage) {
        PageHelper.startPage(currentPage, 50);
        return new PageInfo<>(studentMapper.getStudentRaw());
    }

    @Override
    public PageInfo getExitStudentRaw(int currentPage){
        PageHelper.startPage(currentPage, 50);
        return new PageInfo<>(studentMapper.getExitStudentRaw());
    }

    @Override
    public void doEnroll() {
        Integer status = statusMapper.getStatus();
        if (status == null || status != EnrollStatus.FILE_READY.ordinal()){
            throw new RuntimeException("这个状态不能录取");
        }
        List<ExcelMajor> majors = majorMapper.getMajorPlanForEnroll();
        Map<String,ExcelMajor> map = new HashMap<>();
        for (ExcelMajor major : majors) {
            map.put(major.getMajorId(),major);
        }
        int current = 0, size = 200;
        while(true){
            List<ExcelStudent> students = studentMapper.getStudentRawForEnroll(current, size);
            if (students.size() == 0)
                break;
            for (ExcelStudent student : students) {
                if(doEnroll(map.get(student.getWill1()))){
                    student.setAcceptedType(1);
                    student.setAcceptedMajorId(student.getWill1());
                } else if(doEnroll(map.get(student.getWill2()))){
                    student.setAcceptedType(2);
                    student.setAcceptedMajorId(student.getWill2());
                } else if(doEnroll(map.get(student.getWill3()))){
                    student.setAcceptedType(3);
                    student.setAcceptedMajorId(student.getWill3());
                }else if(doEnroll(map.get(student.getWill4()))){
                    student.setAcceptedType(4);
                    student.setAcceptedMajorId(student.getWill4());
                }else if(doEnroll(map.get(student.getWill5()))){
                    student.setAcceptedType(5);
                    student.setAcceptedMajorId(student.getWill5());
                }else if(doEnroll(map.get(student.getWill6()))){
                    student.setAcceptedType(6);
                    student.setAcceptedMajorId(student.getWill6());
                } else {
                    if(student.getAdjust() != 1)
                        student.setAcceptedType(-1);
                    else {
                        student.setAcceptedType(0);
                    }
                }
            }
            studentMapper.updateAccepted(students);
            current = current + size;
        }
        majorMapper.updateStudentCount(majors);

        statusMapper.addLog("录取完成", EnrollStatus.ENROLLED.ordinal());

    }

    private boolean doEnroll(ExcelMajor major){
        if (major != null && major.getPlanStudentCount() > major.getRealisticStudentCount()){
            major.setRealisticStudentCount(major.getRealisticStudentCount()+1);
            return true;
        }
        return false;
    }

    @Override
    public void doAdjust(){
        Integer status = statusMapper.getStatus();
        if (status == null || status != EnrollStatus.ENROLLED.ordinal()){
            throw new RuntimeException("这个状态不能调剂");
        }
        List<ExcelMajor> majors = majorMapper.getMajorPlanForAdjust();
        int start = 0, size = 100, index = 0;
        while(true){
            List<ExcelStudent> students = studentMapper.getStudentRawForAdjust(start, size);
            if(students.size() == 0)
                break;
            for (int i = 0;i<students.size();) {
                ExcelStudent student = students.get(i);
                if(index < majors.size()){
                    ExcelMajor major = majors.get(index);
                    if (major.getRealisticStudentCount() < major.getPlanStudentCount()){
                        student.setAcceptedType(7);
                        student.setAcceptedMajorId(major.getMajorId());
                        major.setRealisticStudentCount(major.getRealisticStudentCount()+1);
                        i++;
                    } else {
                        index++;
                    }
                } else {
                    student.setAcceptedType(-1);
                    i++;
                }
            }
            studentMapper.updateAdjust(students);
            //不能改变start
        }
        majorMapper.updateStudentCount(majors);
        statusMapper.addLog("调剂完成", EnrollStatus.ADJUSTED.ordinal());
    }

    @Override
    public PageInfo getResult(int currentPage, boolean desc, QueryResultOption option) {
        PageHelper.startPage(currentPage,50);
        return new PageInfo<>(studentMapper.getStudent(desc, option));
    }

    @Override
    @Deprecated
    public void reset(){
        Integer status = statusMapper.getStatus();
        if (status == null || status != EnrollStatus.PRE_ENROLL.ordinal() && status != EnrollStatus.PRE_ADJUST.ordinal()){
            throw new RuntimeException("这个状态不能重置");
        }
        majorMapper.resetMajor();
        studentMapper.resetStudent();
        statusMapper.addLog("重置成功", EnrollStatus.FILE_READY.ordinal());
    }

    @Override
    public void formallyReady(){
        Integer status = statusMapper.getStatus();
        if (status == null || status != EnrollStatus.PRE_ADJUST.ordinal()){
            throw new RuntimeException("这个状态不能准备录取");
        }
        majorMapper.resetMajor();
        studentMapper.resetStudent();
        statusMapper.addLog("准备录取", EnrollStatus.READY.ordinal());
    }

    @Override
    public PageInfo searchStudent(int currentPage, String keyword){
        PageHelper.startPage(currentPage, 50);
        return new PageInfo<>(studentMapper.searchStudent(keyword));
    }


    @Override
    public PageInfo searchStudentByCandidate(int currentPage, String keyword){
        PageHelper.startPage(currentPage, 50);
        return new PageInfo<>(studentMapper.searchStudentByCandidate(keyword));
    }

}
