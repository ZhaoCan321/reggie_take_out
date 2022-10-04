package com.zz.reggie.controller;

import com.zz.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("upload")
    public R<String> upload(MultipartFile file) {
//        file 是临时文件，需要做处理
        log.info(file.toString());
//        原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        使用 uuid 生成文件名，防止重复
        String newFileName = UUID.randomUUID().toString() + suffix;

//        判断是否存在目录，不存在需要重新创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + newFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(newFileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
//        输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        输出流，写回浏览器
    }
}
