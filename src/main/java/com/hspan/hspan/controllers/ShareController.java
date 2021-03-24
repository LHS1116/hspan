package com.hspan.hspan.controllers;


import com.hspan.hspan.data.models.Share;
import com.hspan.hspan.data.repos.ShareRepository;
import com.hspan.hspan.data.repos.UserRepository;
import com.hspan.hspan.dto.in.ShareBasicModel;
import com.hspan.hspan.dto.out.IdDto;
import com.hspan.hspan.dto.out.QShare;
import com.hspan.hspan.exception.BadRequestException;
import com.hspan.hspan.exception.NotFoundException;
import com.hspan.hspan.services.Auth;
import com.hspan.hspan.services.Snowflake;
import com.sun.xml.bind.v2.model.core.ID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/share")
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
    public IdDto creatShare(@RequestBody ShareBasicModel model) {
        var from = model.fromID;
        var to = model.toID;
        var isPublic = model.isPublic;
        var file = model.fileID;
        if(!isPublic && to == null) {
            throw new BadRequestException();
        }
        var sharInDb = shareRepository.findByFromIDAndToIDAndFileID(from, to, file);
        IdDto resID = sharInDb == null ? null :new IdDto(sharInDb.getId());

        if(sharInDb == null) {
            Share newShare = new Share(snowflake.nextId(), from, to, file, isPublic);
            shareRepository.save(newShare);
            resID = new IdDto(newShare.getId());
        }

        return resID;
    }

    @GetMapping("{id}")
    public QShare getShareByID(@PathVariable Long id) {
        var shareInDb = shareRepository.findById(id).orElse(null);
        if (shareInDb == null) {
            throw new NotFoundException();
        }

        return QShare.convert(shareInDb, modelMapper);
    }

    @GetMapping()
    public List<QShare> getShares(
            @RequestParam Long fromID,
            @RequestParam(required = false) Long toID,
            @RequestParam(required = false) int pageNo,
            @RequestParam(required = false) int pageSize
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
