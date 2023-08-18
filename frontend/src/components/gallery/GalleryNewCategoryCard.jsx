/* eslint-disable react/no-array-index-key */
import React, { useState } from 'react';
import Swal from 'sweetalert2';
import BaseIconBtnPurple from '../common/BaseIconBtnPurple';
import BaseIconBtnWhite from '../common/BaseIconBtnWhite';
import BaseTag from '../common/BaseTag';
import GalleryTag from './GalleryTag';
import API from '../../utils/api';

function GalleryNewCategoryCard({ onBtnClick, onAdded }) {
  const initialData = {
    title: '',
    intro: '',
    price: '',
    tags: [],
  };

  // 원본 데이터와 수정 중인 데이터를 상태로 관리
  const [originalData] = useState(initialData);
  const [currentData, setCurrentData] = useState(initialData);

  // input 값이 변경되면 수정 중인 데이터를 업데이트
  const handleChange = (event) => {
    const { name, value } = event.target;
    if (name === 'title' && value.length > 15) {
      return;
    }
    if (name === 'intro' && value.length > 50) {
      return;
    }
    setCurrentData({
      ...currentData,
      [name]: value,
    });
  };

  const handleTagChange = (newTags) => {
    setCurrentData({
      ...currentData,
      tags: newTags,
    });
  };
  // 수정한 내용을 취소하고 원본 데이터로 되돌림
  const handleCancel = () => {
    setCurrentData(originalData);
  };
  const handleCancleBtnClick = () => {
    handleCancel();
    onBtnClick();
  };

  const editCategory = async () => {
    Swal.fire({
      icon: 'warning',
      title: '카테고리를 추가 하시겠습니까? ',
      showCancelButton: true,
      confirmButtonText: '추가',
      cancelButtonText: '취소',
    }).then(async (res) => {
      if (res.isConfirmed) {
        try {
          const url = '/api/category';
          const body = {
            name: currentData.title,
            description: currentData.intro,
            approximatePrice: parseInt(currentData.price, 10),
            hashtags: currentData.tags.map((tag) => tag.hashtagID),

          };
          const response = await API.post(url, body);
          console.log(response.data);
          onAdded();
          return response.data;
        } catch (error) {
          console.log('Error data:', error.response);
          throw error;
        }
      } else {
        return null;
      }
    });
  };

  const handleAddBtnClick = () => {
    onBtnClick();
    editCategory();
  };

  return (
    <div className="relative justify-center items-center p-10 mx-auto mb-5 bg-white shadow-2xl p-1 rounded-lg mx-4 md:mx-auto min-w-1xl max-w-md md:max-w-5xl">
      <div className="flex w-full">
        <span className="w-2/5"><input name="title" value={currentData.title} onChange={handleChange} className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm" placeholder="제목을 입력해주세요" type="text" /></span>
        <span className="mt-2 mx-5 text-xs font-grey">15자 이내로 입력해주세요</span>
      </div>
      <div className="flex w-full mt-2">
        <span className="w-3/5"><input name="intro" value={currentData.intro} onChange={handleChange} className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm" placeholder="카테고리 소개를 입력해주세요" type="text" /></span>
        <span className="mt-2 mx-5 text-xs font-grey align-middle">
          <div>50자 이내로 입력해주세요</div>
        </span>
      </div>
      <div className="flex w-full mt-2">
        <span className="w-1/5"><input name="price" value={currentData.price} onChange={handleChange} className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm" placeholder="가격을 입력해주세요" type="number" /></span>
        <span className="mt-2 mx-5 text-xs font-grey align-middle">
          <div>100,000원 이하로 입력해주세요</div>
        </span>
      </div>
      <div className="absolute bottom-4 right-10 hidden md:block">
        <div className="flex w-fit">
          <span className="mr-1">
            <BaseIconBtnWhite icon="cancel" message="취소" onClick={handleCancleBtnClick} />
          </span>
          <span className="mr-1">
            <BaseIconBtnPurple icon="check" message="추가" onClick={handleAddBtnClick} />
          </span>
        </div>
      </div>
      <span className="flex mt-5">
        {currentData.tags.map((item) => (
          <span key={item.hashtagID} className="mr-2 flex">
            <span><BaseTag message={item.name} /></span>
          </span>
        ))}
      </span>
      <GalleryTag tags={currentData.tags} onTagChange={handleTagChange} />
    </div>
  );
}
export default GalleryNewCategoryCard;
