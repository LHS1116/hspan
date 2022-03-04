package com.hspan.hspan.controllers;


import com.hspan.hspan.data.models.Share;
import com.hspan.hspan.data.repos.ShareRepository;
import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.ShareBasicModel;
import com.hspan.hspan.dto.out.QResponse;
import com.hspan.hspan.dto.out.QShare;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
@RestController
@RequestMapping("/api/share/")
public class ShareController {

    @Autowired
    Auth auth;

    @Autowired
    Snowflake snowflake;

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    @PostMapping
    public QResponse creatShare(@RequestBody ShareBasicModel model, HttpServletResponse response) throws IOException {
        var from = model.fromID;
        var to = model.toID;
        var isPublic = model.isPublic;
        var fileID = model.fileID;
//        var message = "success";
//        if(!isPublic && to == null) {
//            message = "错误的文件权限";
//            return new QResponse(message, -1L, "ok", false);
//        }

        var sharInDb = shareRepository.findByFromIDAndIsPublicAndFileID(from, isPublic, fileID);
        String code = null;
        Long resID = sharInDb == null ? null :sharInDb.getId();
        if (!isPublic) {
            String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            Random random=new Random();
            StringBuilder sb=new StringBuilder();
            for(int i = 0; i < 5; i++){
                int number=random.nextInt(62);
                sb.append(str.charAt(number));
            }
            code = sb.toString();
        }
        if(sharInDb == null) {
            Share newShare = new Share(snowflake.nextId(), from, to, fileID, isPublic, code);
            shareRepository.save(newShare);
            resID = newShare.getId();
            sharInDb = newShare;
        }

        return new QResponse(sharInDb.getCode(), resID, "ok", true);
    }

    @GetMapping("{id}")
    public QResponse getShareByID(@PathVariable("id") Long id) {
        var shareInDb = shareRepository.findById(id).orElse(null);
        if (shareInDb == null) {
            return QResponse.notFoundResponse("Share");
        }
        System.out.println(shareInDb);
        var qs = QShare.convert(shareInDb, modelMapper);
        System.out.println("QS1:" + qs.toString());
        return new QResponse(qs, id, "ok", true);
    }

    @GetMapping("check/{id}")
    public QResponse checkShareCode(@PathVariable("id") Long id, @PathParam("code") String code) {
        var shareInDb = shareRepository.findById(id).orElse(null);
        if (shareInDb == null) {
            return QResponse.notFoundResponse("Share");
        }
        if (shareInDb.getCode().equals(code)) {
            return new QResponse(null, shareInDb.getFileID(), "ok", true);
        }

        return new QResponse(null, -1L, "提取码错误", false);
    }

    @GetMapping()
    public List<QShare> getShares(
            @PathParam("from") Long fromID,
            @PathParam("to") Long toID,
            @PathParam("pagerNum") int pageNo,
            @PathParam("pageSize") int pageSize
    ) {
        int pNo = pageNo <= 0 ? 1 : pageNo;
        int pSize = pageSize <= 0 ? 10 : pageSize;

        var query = entityManager.createQuery(
                "select s from Share s" +
                        " where s.fromID = :fromID" +
                        " AND :toID is null OR s.toID = :toID")
                .setParameter("fromID", fromID)
                .setParameter("toID", toID)
                .setFirstResult(pNo * pSize)
                .setMaxResults(pSize);
        var resultList = query.getResultList();
        var res = new ArrayList<QShare>();

        for(Object share:resultList) {
            res.add(QShare.convert((Share) share,  modelMapper));
        }

        return res;
    }
}
