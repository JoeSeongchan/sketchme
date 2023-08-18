import React from 'react';
import { Link } from 'react-router-dom';
import { ReactComponent as Kakao } from '../../assets/icons/KakaoTalk.svg';

import { URL } from '../../utils/api';

function Login() {
  const baseURL = 'https://kauth.kakao.com/oauth/authorize';
  const responseType = 'code';
  const clientID = 'f83fd946b4ea202b6411e9ebb1eb13c8';
  const redirectURI = `${URL}/api/oidc/kakao`;

  // 카카오로 시작하기 링크 생성
  const kakaoLoginLink = `${baseURL}?response_type=${responseType}&client_id=${clientID}&redirect_uri=${redirectURI}`;
  return (
    <div className="relative flex justify-center items-center">
      <img src="img/LoginWallPaper.jpg" alt="" className="w-full h-screen object-cover opacity-90" />
      <div className="w-[450px] h-[450px] bg-white flex flex-col items-center rounded-[30px] absolute">
        <Link to="/" className="mt-24 w-[180px]">
          <img src="favi/ms-icon-310x310.png" alt="" className="h-20 mx-auto" />
          <div id="LogoLetter" className="text-center">sketch me</div>
        </Link>
        <Link to={kakaoLoginLink} className="w-[240px] h-[48px] flex items-center p-2 mt-8 rounded-md hover:bg-gray-100 shadow-md border border-grey bg-kakao hover:border-black">
          <Kakao className="h-8 w-8 ms-1 me-5" />
          <div className="font-bold text-black">카카오로 시작하기</div>
        </Link>
        <div className="text-darkgrey text-sm mt-6 w-[240px]">
          Sketch me는 회원가입이 필요 없어요. 카카오계정으로 마음껏 즐기세요!
        </div>
      </div>
    </div>
  );
}

export default Login;
