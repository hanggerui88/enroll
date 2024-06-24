package org.enroll.controller;

import org.enroll.service.interfaces.IExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    IExcelService excelService;

    @ResponseBody
    @RequestMapping("/uploadMajor")
    public ModelAndView uploadMajorExcel(MultipartFile file) throws IOException {
        excelService.ReadMajorExcel(file);
        ModelAndView modelAndView = new ModelAndView();
        if(true){
            modelAndView.setViewName("uploadSuccess");
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/uploadStudent")
    public ModelAndView uploadStudentExcel(MultipartFile file) throws IOException {
        excelService.ReadStudentExcel(file);
        ModelAndView modelAndView = new ModelAndView();
        if(true){
            modelAndView.setViewName("uploadSuccess");
        }
        return modelAndView;
    }

    @RequestMapping("/exportResult")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("录取结果", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        excelService.doExport(response.getOutputStream());
    }

    @RequestMapping("/exportExit")
    public void exportExit(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("退档结果", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        excelService.exportExitStudent(response.getOutputStream());
    }
}
