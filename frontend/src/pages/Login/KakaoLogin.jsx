import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function KakaoLogin() {
  const navigate = useNavigate(); // useNavigate 훅을 사용하여 navigate 객체 가져오기

  useEffect(() => {
    // 현재 URL에서 쿼리 파라미터를 읽어옵니다.
    const urlSearchParams = new URLSearchParams(window.location.search);
    const accessToken = urlSearchParams.get('access_token');
    const refreshToken = urlSearchParams.get('refresh_token');
    // access token과 refresh token을 session storage에 저장합니다.
    if (accessToken && refreshToken) {
      sessionStorage.setItem('access_token', accessToken);
      sessionStorage.setItem('refresh_token', refreshToken);

      // 특정 페이지에서 왔었다면 그곳으로 리디렉션
      const toGo = sessionStorage.getItem('Login_to_go');
      if (toGo) {
        sessionStorage.removeItem('Login_to_go');
        navigate(toGo);
      } else {
        // 메인 페이지로 리다이렉션
        navigate('/');
      }
    }
  }, []); // navigate 객체가 의존성으로 추가됨

  return (
    <div>
      로딩중
    </div>
  );
}

export default KakaoLogin;
