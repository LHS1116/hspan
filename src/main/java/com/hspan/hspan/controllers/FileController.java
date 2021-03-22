package com.hspan.hspan.controllers;

import com.hspan.hspan.HsConfig;
import com.hspan.hspan.data.models.Access;
import com.hspan.hspan.data.models.HFile;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.exception.NotFoundException;
import com.hspan.hspan.exception.UnauthorizedException;
import com.hspan.hspan.data.repos.HFileRepository;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;

@Transactional
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    HsConfig hsConfig;

    @Autowired
    Snowflake snowflake;

    @Autowired
    Auth auth;

    @Autowired
    HFileRepository hFileRepository;

    @Autowired


    @PostMapping("upload")
    public String fileUpload(@RequestParam("file") MultipartFile file) {
        String path = hsConfig.getFilePath() + file.getName();
        String tmpPath = path;
        int flag = 1;
        while(hFileRepository.findByFilePath(tmpPath) != null) {
            tmpPath = path + "(" + flag + ")";
            flag++;
        }
        try {
            File outFile = new File(tmpPath);
            if(!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdir();
            }
            file.transferTo(outFile);
            HFile hFile = new HFile(snowflake.nextId(), tmpPath, file.getSize(), Access.PRIVATE);
            hFileRepository.save(hFile);
            return String.valueOf(hFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping
    public QResponse downloadFile(@RequestParam("id") Long fileId, HttpServletResponse response) {
        if(!auth.isLoggedIn()) {
            throw new UnauthorizedException();
        }
        HFile hFile = hFileRepository.findById(fileId).orElse(null);
        if (hFile == null) {
            throw new NotFoundException();
        }

        try {
            File file = new File(hFile.getFilePath());
            if(!file.exists()) {
                throw new NotFoundException();
            }

            byte[] buffer = new byte[2048];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream();

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            bis.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new QResponse(null, hFile.getId(), "下载成功", true);


    }
}
