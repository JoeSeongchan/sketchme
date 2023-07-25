package com.dutaduta.sketchme.oicd.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.dutaduta.sketchme.oicd.dto.KakaoUserInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@Log4j2
public class KakaoService {

    /**
     * 인가 코드를 가지고서 액세스 토큰을 가져온다.
     *
     * @param code 인가 코드
     * @return 액세스 토큰
     */
    public String getKakaoIdToken(String code, String REST_API_KEY) {
        // 인가 코드가 리다이렉트된 URI
        final String REDIRECT_URL = "http://localhost:8080/api/oidc/kakao";
        // ID 토큰을 요청하는 URL
        final String reqURL = "https://kauth.kakao.com/oauth/token";
        String accessToken = "";
        String refreshToken = "";
        String idToken = "";
        try {
            URL url = new URL(reqURL);

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
            log.info("result : " + responseData);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        return responseData;
    }

}

