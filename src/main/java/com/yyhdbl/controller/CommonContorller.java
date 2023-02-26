package com.yyhdbl.controller;

import com.yyhdbl.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件的上传和下载   http://localhost:8080/backend/page/demo/upload.html 上传文件
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonContorller {
    @Value("${take_out.path}")
    private String basepath;


    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        //file 是个临时文件 需要转存起来不然断开链接请求结束后就没了

        //获取原始文件名
        String originFileName = file.getOriginalFilename();
        //截取文件名后缀
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));


        //使用uuid随机生成一个文件名 防止文件名重复被覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //如果转存的文件存储在了文件夹内  则需要新创建文件夹
        File dir = new File(basepath);
        if (!dir.exists()) {
            //目录不存在 需要创建
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basepath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }


    /**
     * 文件下载
     *
     * 流程：由于服务器就在本机上，所以上传文件就放在D盘下
     * upload方法完成后会返回给前端上传文件的名字
     * 前端会把文件名字记录下来 放在了imageurl里
     * 当前端向服务器发起请求想要展示图片时，会加上刚刚存储的文件名字发送get请求
     * 服务器收到请求后，按照前端要的文件地址，把文件转为流
     * 通过流送回前端 这样浏览器就能展示图片了
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流 通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basepath + name));

            //输出流，通过输出流协会浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
