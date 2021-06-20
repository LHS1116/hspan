package com.hspan.hspan.controllers;

import com.hspan.hspan.HsConfig;
import com.hspan.hspan.data.models.Access;
import com.hspan.hspan.data.models.HFile;
import com.hspan.hspan.data.repos.HFileRepository;
import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
@RequestMapping("/api/file/")
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
    UserRepository userRepository;

    @Autowired

    @GetMapping("test")
    @ResponseBody
    public String fileTest() {
        return "File test!";
    }


    @RequestMapping("upload")
    @ResponseBody
    public QResponse fileUpload(@RequestPart MultipartFile file, @RequestParam("name") String fileName,
                                @RequestParam("name") List<String> fileNames) throws IOException {

        String tmpPath = hsConfig.getFilePath() + fileName;
        String originName = hsConfig.getFilePath() + fileName.split("\\.")[0];
        String suffix = fileName.split("\\.")[1];
        int flag = 1;
        var currentUser = userRepository.findById(auth.userId()).orElse(null);
        if (currentUser == null) {
            return new QResponse("请先登录", -1L, "fail", false);
        }

        while(hFileRepository.findByFilePath(tmpPath) != null) {
            tmpPath = originName + "(" + flag + ")." + suffix;
            flag++;
        }
        try {
            System.out.println(tmpPath);

            File outFile = new File(tmpPath);
            if(!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdir();
            }
            file.transferTo(outFile);
//            outFile.isAbsolute()
            HFile hFile = new HFile(snowflake.nextId(), tmpPath, file.getSize(), fileName, Access.PRIVATE);
            System.out.println(hFile);
            currentUser.addFile(hFile.getId());
            hFile.addOwner(currentUser.getId());
            userRepository.save(currentUser);
            hFileRepository.save(hFile);

            return new QResponse("上传成功！", hFile.getId(), "ok", true);
//            return String.valueOf(hFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new QResponse("上传失败！", -1L, "fail", false);

    }

    @GetMapping("download/{id}")
    public QResponse downloadFile(@PathVariable("id") Long fileId, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if(!auth.isLoggedIn()) {
            response.sendRedirect("/api/login/test");
        }
        HFile hFile = hFileRepository.findById(fileId).orElse(null);
        if (hFile == null) {
            System.out.println(1);
            return QResponse.notFoundResponse();
        }

        try {
            File file = new File(hFile.getFilePath());
            if(!file.exists()) {
                return QResponse.notFoundResponse();
            }
            response.reset();
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition","attachment;fileName=" +
                    java.net.URLEncoder.encode(hFile.getFileName(), StandardCharsets.UTF_8));
            byte[] buffer = new byte[2048];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream();
//            request.getSession().removeAttribute("percent");

//            long writtenLen = 0;
//            long fileLen = file.length();
//            long percent = 0;

            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
//                writtenLen += i;
//                percent = writtenLen / fileLen;
//                request.getSession().setAttribute("percent", percent);
                i = bis.read(buffer);
            }
            bis.close();
            os.close();
        } catch (Exception e) {
             e.printStackTrace();
        }

        return new QResponse(null, hFile.getId(), "下载成功", true);
    }

    @GetMapping("info/{id}")
    @ResponseBody
    public QResponse getFileInfo(@PathVariable("id") Long fileId) {
        if(!auth.isLoggedIn()) {
            return QResponse.unauthorizedResponse();
        }
        var hFile = hFileRepository.findById(fileId).orElse(null);
        if (hFile == null) {
            return new QResponse(null, -1L, "文件不存在", false);
        }
        System.out.println(hFile);
        Map<String, Object> map = Map.of(
                "id", hFile.getId(),
                "name", hFile.getFileName(),
                "size", hFile.getSize(),
                "uploadDate", auth.formatDate(hFile.getCreatedAt()),
                "owners", hFile.getOwners());

        return new QResponse(map, hFile.getId(), "文件信息", true);
    }

    @GetMapping("save/{id}")
    @ResponseBody
    public QResponse saveFile(HttpServletRequest request, @PathVariable("id") Long id) {
        if (!auth.isLoggedIn()) {
            return QResponse.unauthorizedResponse();
        }
        var hFile = hFileRepository.findById(id).orElse(null);
        if (hFile == null) {
            return QResponse.notFoundResponse("File " + id);
        }
        var currentUser = userRepository.findById(auth.userId()).orElse(null);
        if (currentUser == null) {
            return QResponse.notFoundResponse("User " + auth.userId());
        }
        if (currentUser.getFiles().contains(id)) {
            return new QResponse(null, id, "ok", true);
        }
        hFile.addOwner(currentUser.getId());
        currentUser.addFile(hFile.getId());
        hFileRepository.save(hFile);
        userRepository.save(currentUser);
        return new QResponse(null, id, "ok", true);
    }

    @DeleteMapping("{id}")
    public QResponse deleteFile(@PathVariable("id") Long fileID) {
        if (!auth.isLoggedIn()) {
            return QResponse.unauthorizedResponse();
        }
        var hFile = hFileRepository.findById(fileID).orElse(null);
        var currentUser = userRepository.findById(auth.userId()).orElse(null);

        if (hFile == null || currentUser == null) {
            return QResponse.notFoundResponse();
        }

        hFile.removeOwner(auth.userId());
        currentUser.removeFile(fileID);
        System.out.println("Count:" + hFile.getOwnerCount());
        if (hFile.getOwnerCount() == 0) {
            hFileRepository.delete(hFile);
            File file = new File(hFile.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        }
        userRepository.save(currentUser);
        return new QResponse(null, fileID, "删除成功", true);
    }

}
