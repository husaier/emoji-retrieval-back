package org.bupt.hse.retrieval.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-09
 */
@Component
public class RestTemplateUtil {

    private final static Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);

    @Autowired
    private RestTemplate restTemplate;

    public JSONObject get(String url, Map<String, Object> queryParams) throws IOException {
        return get(url, queryParams, new HashMap<>(1));
    }

    public JSONObject get(String url, Map<String, Object> queryParams, Map<String, String> headerParams) throws IOException {
        String tempUrl = setParamsByAppendUrl(queryParams, url);
        HttpHeaders headers = new HttpHeaders();
        headerParams.forEach(headers::add);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(tempUrl, HttpMethod.GET, httpEntity, String.class);
        return JSONObject.parseObject(response.getBody());
    }

    public JSONObject get2(String url, Map<String, Object> queryParams, Map<String, String> headerParams) throws IOException {
        String tempUrl = setParamsByPath(queryParams, url);
        HttpHeaders headers = new HttpHeaders();
        headerParams.forEach(headers::add);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(tempUrl, HttpMethod.GET, httpEntity, String.class, queryParams);
        return JSONObject.parseObject(response.getBody());
    }

    public JSONObject post(String url, String json, Map<String, String> headerParams) {
        HttpHeaders headers = new HttpHeaders();
        headerParams.forEach(headers::add);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return JSONObject.parseObject(response.getBody());
    }

    private String setParamsByPath(Map<String, Object> queryParams, String url) {
        // url?id={id}&name={name}
        if (queryParams == null || queryParams.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=").append("{").append(entry.getKey()).append("}");
            }
            if (!url.contains("?")) {
                sb.deleteCharAt(0).insert(0, "?");
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return url + sb;
    }

    private String setParamsByAppendUrl(Map<String, Object> queryParams, String url) {
        // url?id=1&name=zzc
        if (queryParams == null || queryParams.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=");
                sb.append(entry.getValue());
            }
            if (!url.contains("?")) {
                sb.deleteCharAt(0).insert(0, "?");
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return url + sb;
    }
}
