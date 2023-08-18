import React from 'react';
import { useLocation } from 'react-router-dom';
import GalleryProfileCard from '../../components/gallery/GalleryProfileCard';
import GalleryTab from '../../components/gallery/GalleryTab';

function GalleryPage() {
  // 현재 주소를 /search/{category}/{keyword}/ 와 같은 식으로 해석하여 초기 탭 및 검색 키워드 설정
  const { pathname } = useLocation();
  // pathname을 '/'를 기준으로 분리하여 category와 keyword를 추출
  const [, memberID, artistID] = pathname.split('/').slice(1);
  return (
    <div>
      <GalleryProfileCard memberID={memberID} artistID={artistID} />
      <GalleryTab memberID={memberID} artistID={artistID} />
    </div>
  );
}

export default GalleryPage;
