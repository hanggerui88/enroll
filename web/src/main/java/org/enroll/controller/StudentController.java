package org.enroll.controller;

import com.github.pagehelper.PageInfo;
import org.enroll.pojo.StudentResult;
import org.enroll.service.interfaces.IStudentService;
import org.enroll.utils.JsonResponse;
import org.enroll.utils.QueryResultOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService studentService;

    @RequestMapping("/doEnroll")
    public ModelAndView doEnroll(){
        studentService.doEnroll();
        ModelAndView modelAndView = new ModelAndView();
        if(true){
            modelAndView.setViewName("uploadSuccess");
        }
        return modelAndView;
    }

    @RequestMapping("/doAdjust")
    public ModelAndView doAdjust(){
        studentService.doAdjust();
        ModelAndView modelAndView = new ModelAndView();
        if(true){
            modelAndView.setViewName("uploadSuccess");
        }
        return modelAndView;
    }

    @RequestMapping("/getResult")
    public JsonResponse getResult(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                  @RequestParam(required = false, defaultValue = "false") boolean desc,
                                  QueryResultOption option){
        return new JsonResponse(JsonResponse.OK, studentService.getResult(currentPage, desc, option), null);
    }

    @RequestMapping("/reset")
    @Deprecated
    public JsonResponse reset(){
        studentService.reset();
        return new JsonResponse(JsonResponse.OK, null, null);
    }

    @RequestMapping("/areYouReady")
    @Deprecated
    public JsonResponse formalReady(){
        studentService.formallyReady();
        return new JsonResponse(JsonResponse.OK, null, null);
    }

    @RequestMapping("/searchStudent")
    @Deprecated
    public String searchStudent(@RequestParam(required = false, defaultValue = "1") int currentPage, String keyword) {
        JsonResponse response;
        // 使用正则表达式过滤输入，只保留字母、数字和部分特殊字符
        String filteredKeyword = keyword.replaceAll("[^a-zA-Z0-9\\s_\\-\\.]", "");
        if (filteredKeyword.isEmpty()) {
            response = new JsonResponse(JsonResponse.INVALID_REQUEST, null, null);
        } else {
            // 调用 service 层的方法进行学生搜索，返回 PageInfo 对象
            PageInfo<StudentResult> pageInfo = studentService.searchStudent(currentPage, filteredKeyword);
            // 从 PageInfo 对象中获取学生列表
            List<StudentResult> students = pageInfo.getList();

            // 构建响应对象
            response = new JsonResponse(JsonResponse.OK, students, null);
        }

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table border=\"1\">");
        htmlTable.append("<tr><th>考号</th><th>姓名</th><th>总成绩</th><th>排名</th><th>专业</th><th>学院</th></tr>");
        // 解析 Json 响应并生成 HTML 表格
        if (response.getCode() == JsonResponse.OK) {
            List<StudentResult> students = (List<StudentResult>) response.getData();
            for (StudentResult student : students) {
                htmlTable.append("<tr>");
                htmlTable.append("<td>").append(student.getCandidate()).append("</td>");
                htmlTable.append("<td>").append(student.getStudentName()).append("</td>");
                htmlTable.append("<td>").append(student.getTotalGrade()).append("</td>");
                htmlTable.append("<td>").append(student.getRank()).append("</td>");
                htmlTable.append("<td>").append(student.getMajorName()).append("</td>");
                htmlTable.append("<td>").append(student.getDepartmentName()).append("</td>");
                htmlTable.append("</tr>");
            }
        } else {
            htmlTable.append("<tr><td colspan=\"4\">Invalid Request</td></tr>");
        }
        htmlTable.append("</table>");
        return htmlTable.toString();
    }

    @RequestMapping("/searchStudentByCandidate")//考号
    @Deprecated
    public String searchStudentByCandidate(@RequestParam(required = false, defaultValue = "1") int currentPage, String keyword) {
        JsonResponse response;
        String filteredKeyword = keyword.replaceAll("[^a-zA-Z0-9\\s_\\-\\.]", "");
        if (filteredKeyword.isEmpty()) {
            response = new JsonResponse(JsonResponse.INVALID_REQUEST, null, null);
        } else {
            // 调用 service 层的方法进行学生搜索，返回 PageInfo 对象
            PageInfo<StudentResult> pageInfo = studentService.searchStudentByCandidate(currentPage, filteredKeyword);
            // 从 PageInfo 对象中获取学生列表
            List<StudentResult> students = pageInfo.getList();
            // 构建响应对象
            response = new JsonResponse(JsonResponse.OK, students, null);
        }

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table border=\"1\">");
        htmlTable.append("<tr><th>考号</th><th>姓名</th><th>总成绩</th><th>排名</th><th>专业</th><th>学院</th></tr>");

        // 解析 Json 响应并生成 HTML 表格
        if (response.getCode() == JsonResponse.OK) {
            List<StudentResult> students = (List<StudentResult>) response.getData();
            for (StudentResult student : students) {
                htmlTable.append("<tr>");
                htmlTable.append("<td>").append(student.getCandidate()).append("</td>");
                htmlTable.append("<td>").append(student.getStudentName()).append("</td>");
                htmlTable.append("<td>").append(student.getTotalGrade()).append("</td>");
                htmlTable.append("<td>").append(student.getRank()).append("</td>");
                htmlTable.append("<td>").append(student.getMajorName()).append("</td>");
                htmlTable.append("<td>").append(student.getDepartmentName()).append("</td>");
                htmlTable.append("</tr>");
            }
        } else {
            htmlTable.append("<tr><td colspan=\"4\">Invalid Request</td></tr>");
        }
        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}
