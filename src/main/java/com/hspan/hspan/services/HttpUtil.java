package com.hspan.hspan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpUtil {

    private final CloseableHttpClient httpClient;
    private final RequestConfig requestConfig;
    private ObjectMapper mapper;

    @Autowired
    public HttpUtil(CloseableHttpClient httpClient, RequestConfig requestConfig, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
        this.mapper = mapper;
    }

    public String doGet(String url, Map<String, Object> params) throws URISyntaxException, IOException {

        if (params != null) {
            URIBuilder builder = new URIBuilder(url);
            params.forEach((k, v) -> {
                builder.addParameter(k, v.toString());
            });
            url = builder.toString();
        }
        return doGet(url);
    }

    public String doGet(String url) throws IOException {
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        //设置请求参数
        httpGet.setConfig(requestConfig);
        try {
            //执行请求
//            System.out.println("client" + httpClient);
            System.out.println(url);
            response = httpClient.execute(httpGet);
//            System.out.println(response + "sadd");
            //判断返回状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }


    public String doPost(String url, Map<String, String> params) throws IOException {
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        //设置请求参数
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setConfig(requestConfig);
        List<NameValuePair> parameters = new ArrayList<>();
        params.forEach((k, v) -> parameters.add(new BasicNameValuePair(k, v)));
        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
        String js = mapper.writeValueAsString(params);
        StringEntity stringEntity = new StringEntity(js);
        // 将请求实体设置到httpPost对象中
        httpPost.setEntity(stringEntity);
        try {
            //执行请求
            response = httpClient.execute(httpPost);
            System.out.println(response);
            //判断返回状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}
