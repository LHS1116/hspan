package com.hspan.hspan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hspan.hspan.dto.out.MResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CasServer {

    private HttpUtil httpUtil;

    private ObjectMapper objectMapper;


    @Autowired
    public CasServer(HttpUtil httpUtil, ObjectMapper objectMapper) {
        this.httpUtil = httpUtil;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param casToken
     * @return if casToken is valid return username else null
     * @throws IOException
     * @throws URISyntaxException
     */
    public MResponse casTokenVerify(String casToken) throws IOException, URISyntaxException {
        String resp =  httpUtil.doGet("http://127.0.0.1:8087/api/sso/cas/verify", new HashMap<>() {{
            put("casToken", casToken);
        }});
        if (resp == null) {
            return null;
        }
        return objectMapper.readValue(resp, MResponse.class);
    }

    /**
     *
     * @param params
     * @return casToken if params is valid else null
     * @throws IOException
     */
    public MResponse casLogin(Map<String, String> params) throws IOException {
        String resp =  httpUtil.doPost("http://127.0.0.1:8087/api/sso/cas/in", params);
        if (resp == null) {
            return null;
        }
        return objectMapper.readValue(resp, MResponse.class);

    }
}
