import React, { useState } from 'react';
import GalleryIntroPage from '../../pages/Gallery/GalleryIntroPage';
import GalleryPaintingPage from '../../pages/Gallery/GalleryPaintingPage';

function GalleryTab({ memberID, artistID }) {
  // 현재 선택된 탭의 인덱스를 관리하는 상태
  const [activeTab, setActiveTab] = useState(0);

  // 탭을 클릭할 때 호출되는 함수
  const handleTabClick = (index) => {
    setActiveTab(index);
  };
  return (
    <div className="mb-40">
      {/* 탭 메뉴 */}
      <ul className="flex justify-center items-center flex-wrap mt-5">
        <li className="mr-4">
          <button
            type="button"
            className={`py-2 px-4 font-semibold ${activeTab === 0 ? 'inline-block text-primary hover:border-primary_2 rounded-t-lg py-4 px-4 text-sm font-bold text-center border-transparent border-b-2 active' : 'inline-block rounded-t-lg py-4 px-4 text-sm font-medium text-center  bg-gray-200 text-gray-700'}`}
            onClick={() => handleTabClick(0)}
          >
            소개
          </button>
        </li>
        <li className="mr-4">
          <button
            type="button"
            className={`py-2 px-4 font-semibold ${activeTab === 1 ? 'inline-block text-primary  hover:border-primary_2 rounded-t-lg py-4 px-4 text-sm font-bold text-center border-transparent border-b-2 active' : 'inline-block rounded-t-lg py-4 px-4 text-sm font-medium text-center  bg-gray-200 text-gray-700'}`}
            onClick={() => handleTabClick(1)}
          >
            그림
          </button>
        </li>
      </ul>

      {/* 각 탭에 해당하는 컨텐츠 */}
      {activeTab === 0 && <GalleryIntroPage memberID={memberID} artistID={artistID} />}
      {activeTab === 1 && <GalleryPaintingPage memberID={memberID} artistID={artistID} />}
    </div>
  );
}
export default GalleryTab;
