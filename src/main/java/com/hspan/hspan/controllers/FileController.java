package com.hspan.hspan.controllers;

import com.hspan.hspan.HsConfig;
import com.hspan.hspan.data.models.Access;
import com.hspan.hspan.data.models.HFile;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.exception.BadRequestException;
import com.hspan.hspan.exception.NotFoundException;
import com.hspan.hspan.exception.UnauthorizedException;
import com.hspan.hspan.data.repos.HFileRepository;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("test")
    @ResponseBody
    public String fileTest() {
        return "File test!";
    }


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

    @GetMapping("download")
    public QResponse downloadFile(@RequestParam("id") Long fileId, HttpServletResponse response, HttpServletRequest request) {
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
            request.getSession().removeAttribute("percent");

            long writtenLen = 0;
            long fileLen = file.length();
            long percent = 0;

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                writtenLen += i;
                percent = writtenLen / fileLen;
                request.getSession().setAttribute("percent", percent);
                i = bis.read(buffer);
            }
            bis.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new QResponse(null, hFile.getId(), "下载成功", true);
    }

    @GetMapping("percent")
    @ResponseBody
    public Map<String, Object> getPercent(HttpServletRequest request) {
        if (request.getSession().getAttribute("percent") == null) {
            throw new BadRequestException();
        }
        int percent = (int)request.getSession().getAttribute("percent");

        Map<String, Object> res = new HashMap<>();
        res.put("percent", percent);
        return res;
    }
}
