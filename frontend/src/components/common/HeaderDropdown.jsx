/* eslint-disable no-console */
/* eslint-disable react/prop-types */
/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/no-noninteractive-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../utils/api';

function DropdownMenu({ onSelectOption }) {
  return (
    <div className="absolute top-[56px] right-0 w-40 bg-white shadow-md rounded-md z-50">
      <ul className="py-2">
        <li className="px-4 py-2 cursor-pointer hover:bg-gray-100" onClick={() => onSelectOption('/mypage')}>
          마이페이지
        </li>
        <li className="px-4 py-2 cursor-pointer hover:bg-gray-100" onClick={() => onSelectOption('/gallery')}>
          갤러리
        </li>
        <li className="px-4 py-2 cursor-pointer hover:bg-gray-100" onClick={() => onSelectOption('/logout')}>
          로그아웃
        </li>
      </ul>
    </div>
  );
}

async function logout() {
  try {
    const accessToken = sessionStorage.getItem('access_token');
    const headers = {
      Authorization: `Bearer ${accessToken}`,
    };
    await api.post('/api/user/logout', null, { headers });
  } catch (error) {
    // eslint-disable-next-line no-console
    console.error('로그아웃 API 호출 실패:', error);
    throw error;
  }
}

function HeaderDropdown({ name, setProfileData }) {
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleDropdownClick = () => {
    setShowDropdown(!showDropdown);
  };

  const handleOptionSelect = async (option) => {
    if (option === '/logout') {
      try {
        logout();

        // 세션 스토리지의 토큰 비우기
        sessionStorage.removeItem('access_token');
        sessionStorage.removeItem('refresh_token');
        sessionStorage.removeItem('memberID');
        sessionStorage.removeItem('artistID');
        sessionStorage.removeItem('userProfileImg');
        sessionStorage.removeItem('artistProfileImg');
        sessionStorage.removeItem('userName');
        sessionStorage.removeItem('artistName');

        setProfileData(null);
        // 로그아웃 후 홈페이지로 이동
        navigate('/');
      } catch (error) {
        // eslint-disable-next-line no-alert
        alert('로그아웃 실패');
      }
    } else if (option === '/gallery') {
      const memberID = sessionStorage.getItem('memberID');
      const artistID = sessionStorage.getItem('artistID');

      if (memberID && artistID) {
        const galleryPath = `/gallery/${memberID}/${artistID}`;
        setShowDropdown(false);
        navigate(galleryPath);
      } else {
        // eslint-disable-next-line no-alert
        alert('작가로 등록되지 않은 유저입니다. 작가로 등록해주세요.');
      }
    } else {
      setShowDropdown(false);
      navigate(option);
    }
  };

  return (
    <div className="flex sticky top-0 bg-white z-40 items-center">
      <div className="flex items-center">
        <div className="md:pr-8 cursor-pointer" onClick={handleDropdownClick}>
          <span style={{ fontWeight: 'bold', fontSize: '20px' }}>{name}</span>
          님
          <span
            className={`ml-1 ${showDropdown ? 'rotate-180' : ''}`}
            style={{ display: 'inline-block' }}
          >
            ▼
          </span>
        </div>
        {showDropdown && <DropdownMenu onSelectOption={handleOptionSelect} />}
      </div>
    </div>
  );
}

export default HeaderDropdown;
