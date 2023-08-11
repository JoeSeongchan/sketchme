package com.dutaduta.sketchme.oidc.service;

import com.dutaduta.sketchme.oidc.dto.OIDCPublicKeyDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Log4j2
public class KakaoService {

    @Value("${kakao.redirect-url}")
    private String REDIRECT_URL;

    @Value("${kakao.openkey-url}")
    private String KAKAO_OPENKEY_URL;

    @Value("${kakao.token-url}")
    private String TOKEN_URL;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 인가 코드를 가지고서 id 토큰을 가져온다.
     *
     * @param code 인가 코드
     * @return id 토큰
     */
    public String getKakaoIdToken(String code, String REST_API_KEY) {

        String accessToken = "";
        String refreshToken = "";
        String idToken = "";
        try {
            URL url = new URL(TOKEN_URL);

            // 주어진 URL에 HTTP 연결을 열기
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // POST 요청을 위해서 doOutput 값을 true로 설정
            conn.setDoOutput(true);

            // POST 요청에 넣을 파라미터를 붙여서 스트림으로 전송
            try (BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream())))) {
                String sb = "grant_type=authorization_code" +
                        "&client_id=" + REST_API_KEY +
                        "&redirect_uri=" + REDIRECT_URL +
                        "&code=" + code;
                bw.write(sb);
                bw.flush();
            }

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            String responseData = getRequestResult(conn);
            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = JsonParser.parseString(responseData);

            // 카카오 인증 서버에서 받은 access 토큰을 파싱하여 읽는다.
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            log.info("access_token : " + accessToken);

            // 카카오 인증 서버에서 받은 refresh 토큰을 파싱하여 읽는다.
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
            log.info("refresh_token : " + refreshToken);

            // 카카오 인증 서버에서 받은 id 토큰을 파싱하여 읽는다.
            idToken = element.getAsJsonObject().get("id_token").getAsString();
            log.info("id_token : " + idToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idToken;
    }

    private String getRequestResult(HttpURLConnection conn) {
        String responseData = "";
        // HTTP 요청 후 응답 받은 데이터를 버퍼에 쌓는다
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }
            responseData = sb.toString();
            log.info("getRequestResult : " + responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    /**
     * 카카오의 공개키를 가져온다.
     * 단, 로그인 요청 때마다 매번 카카오에 요청을 보내는 것이 아니라 공개키를 redis에 캐싱해두고
     * 로그인 요청시 redis 캐시 저장소에서 꺼내온다.
     *
     * @return
     */
    public List<OIDCPublicKeyDTO> getKakaoOIDCOpenKeys(){

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        List<OIDCPublicKeyDTO> OIDCPublicKeys = null;

        try {
            // 주어진 URL에 HTTP 연결을 열기
            URL url = new URL(KAKAO_OPENKEY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // http 요청에 필요한 타입 정의 실시
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            // http 요청 실시
            conn.connect();

            String responseData = getRequestResult(conn);
            log.info("-------------Kakao OpenKey-----------------");
            log.info("responseData : " + responseData);

            // http 요청 응답 코드 확인 실시 - 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = JsonParser.parseString(responseData);

            // 카카오 인증 서버에서 받은 공개키를 파싱하여 읽는다.
            log.info("parsing : " + element.getAsJsonObject().get("keys"));
            String keys = element.getAsJsonObject().get("keys").toString();
            log.info("keys : " + keys);


            // 공개키 목록을 리스트로 담아서 반환
            if(StringUtils.isNoneBlank(keys)) {
                try {
                    OIDCPublicKeys = mapper.readValue(keys, new TypeReference<List<OIDCPublicKeyDTO>>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("OIDCPublicKeys : " + OIDCPublicKeys);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return OIDCPublicKeys;
    }

    /**
     * kakao에게 요청을 보내서 공캐키를 받아온다.
     * redis에 공개키가 캐싱되어 있지 않을 때만 실행된다.
     * @return
     */
    public String getOpenKeysFromKakaoOIDC() {

        final String reqURL = "https://kauth.kakao.com/.well-known/jwks.json";

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";

        try {
            // 주어진 URL에 HTTP 연결을 열기
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // http 요청에 필요한 타입 정의 실시
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            // http 요청 실시
            conn.connect();

            responseData = getRequestResult(conn);
            log.info("responseData : " + responseData);

            // http 요청 응답 코드 확인 실시 - 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}

