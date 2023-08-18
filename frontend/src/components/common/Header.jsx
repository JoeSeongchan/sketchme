import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SearchBar from '../main/SearchBar';
import HeaderDropdown from './HeaderDropdown';
import API, { URL } from '../../utils/api';
import { ReactComponent as Chat } from '../../assets/icons/Chat.svg';

function Header() {
  const [profileData, setProfileData] = useState(null);

  useEffect(() => {
    const accessToken = sessionStorage.getItem('access_token');
    if (!accessToken) {
      return;
    }
    const fetchData = async () => {
      try {
        const response = await API.get('/api/user/profile?member=user', {});
        setProfileData(response.data.data);
        const id = response.data.data.memberID;
        const profileImg = response.data.data.profileImgUrl;
        const name = response.data.data.nickname;
        sessionStorage.setItem('memberID', id);
        sessionStorage.setItem('userProfileImg', profileImg);
        sessionStorage.setItem('userName', name);
      } catch (error) {
        // eslint-disable-next-line no-console
        console.error('프로필 정보 가져오기 실패:', error);
      }
      try {
        const response = await API.get('/api/user/profile?member=artist', {});
        if (response.data && response.data.data) {
          const id = response.data.data.memberID;
          const profileImg = response.data.data.profileImgUrl;
          const name = response.data.data.nickname;
          sessionStorage.setItem('artistID', id);
          sessionStorage.setItem('artistProfileImg', profileImg);
          sessionStorage.setItem('artistName', name);
        }
      } catch (error) {
        // eslint-disable-next-line no-console
        console.error('헤더-유저 정보 가져오기 실패:', error);
      }
    };

    fetchData();
  }, []);

  const renderHeaderContent = () => {
    // Check if user is logged in
    const isLoggedIn = profileData && profileData.memberStatus === 'user' && profileData.logined;

    if (isLoggedIn) {
      return (
        <div className="md:flex block whitespace-nowrap items-center">
          <Link to="/mypage" className="flex items-center md:pr-4">
            {profileData.profileImgUrl && (
              <img
                className="w-16 min-w-[64px] h-16 rounded-full"
                src={`${URL}/api/display?imgURL=${profileData.profileImgUrl}`}
                alt=""
              />
            )}
          </Link>
          <HeaderDropdown
            name={profileData.nickname}
            setProfileData={setProfileData}
          />
        </div>
      );
    }
    return (
      <div className="px-8 whitespace-nowrap flex md:h-20 items-center">
        <Link to="/login">로그인</Link>
      </div>
    );
  };

  return (
    <div className="md:align-middle md:justify-around md:h-20 h-24 w-full flex justify-between sticky top-0 min-w-[372px] bg-white z-40 shadow-lg">
      <Link to="/" className="md:h-20 md:w-72 block whitespace-nowrap h-40 w-0">
        <img
          src="favi/ms-icon-310x310.png"
          alt=""
          className="h-16 inline-block absolute top-2 left-10"
        />
        <span id="LogoLetter" className="absolute left-24 top-4 ps-4">
          sketch me
        </span>
      </Link>
      <div className="hidden lg:flex lg:h-20 md:justify-end w-1/2 mt-16 md:mt-0 justify-items-start">
        <SearchBar />
        <ul className="flex item-center text-center items-center">
          <li className="flex-1 ">
            <Link to="/chatting">
              <Chat />
            </Link>
          </li>
          <li className="flex-1 w-20 mr-10">
            <Link to="/live">라이브</Link>
          </li>
        </ul>
      </div>
      {renderHeaderContent()}
    </div>
  );
}

export default Header;
