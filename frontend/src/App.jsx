import React, { useEffect } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { connectWebSocket } from './reducers/ChatSlice';
import MainPage from './pages/Main/Main';
import LoginPage from './pages/Login/Login';
import KakaoLoginPage from './pages/Login/KakaoLogin';
import RegisterPage from './pages/Register/Register';
import SearchPage from './pages/Search/SearchPage';
import GalleryPage from './pages/Gallery/GalleryPage';
import ChattingPage from './pages/Chatting/ChattingBigPage';
import LivePage from './pages/Live/LivePage';
import MyPage from './pages/MyPage/MyPage';
import Header from './components/common/Header';
import ReservationPage from './pages/Reservation/ReservationPage';
import ReservationConfirmPage from './pages/Reservation/ReservationConfirmPage';
import ChatIcon from './components/chatting/ChattingIcon';
import './App.css';
import ReservationCheckPage from './pages/MyPage/ReservationCheckPage';

import DrawingTestPage from './pages/Live/DrawingTestPage';
import FavoriteArtistPage from './pages/MyPage/FavoriteArtistPage';
import PurchasedPage from './pages/MyPage/PurchasedPage';

function App() {
  const location = useLocation();
  // 현재 경로가 /chatting 인지 여부를 확인하여 변수로 저장
  const isChattingRoute = location.pathname === '/chatting';
  const isLoginOrSignup = location.pathname.startsWith('/login') || location.pathname.startsWith('/signup');
  const isLiveRoute = location.pathname.includes('/live/');
  const dispatch = useDispatch();
  useEffect(() => {
    // 웹소켓 연결을 위해 connectWebSocket 액션을 디스패치합니다.
    dispatch(connectWebSocket());
  }, []);
  return (
    <div className="h-screen overscroll-hidden Main2">
      {!isLoginOrSignup && !isLiveRoute && <Header />}
      <main>
        <Routes>
          <Route exact path="/" element={<MainPage />} />
          <Route exact path="/login" element={<LoginPage />} />
          <Route path="/login/kakao/*" element={<KakaoLoginPage />} />
          <Route exact path="/register" element={<RegisterPage />} />
          <Route path="/search/*" element={<SearchPage />} />
          <Route exact path="/gallery/*" element={<GalleryPage />} />
          <Route exact path="/chatting" element={<ChattingPage />} />
          <Route exact path="/live/*" element={<LivePage />} />
          <Route exact path="/reservation/*" element={<ReservationPage />} />
          <Route exact path="/reservationconfirm/*" element={<ReservationConfirmPage />} />
          <Route exact path="/mypage" element={<MyPage />} />
          <Route exact path="/mypage/reservationcheck" element={<ReservationCheckPage />} />
          <Route exact path="/mypage/favoriteArtists" element={<FavoriteArtistPage />} />
          <Route exact path="/mypage/myPictures" element={<PurchasedPage />} />
          <Route exact path="/test" element={<DrawingTestPage />} />
        </Routes>
      </main>
      {/* ChatIcon을 렌더링할 때 조건부 렌더링 사용 */}
      {!isChattingRoute && !isLiveRoute && <ChatIcon />}
      {' '}

    </div>
  );
}

export default App;
