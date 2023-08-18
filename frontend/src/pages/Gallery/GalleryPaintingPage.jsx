/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from 'react';
import GalleryPaintingCard from '../../components/gallery/GalleryPaintingCard';
import { ReactComponent as PlusIcon } from '../../assets/icons/Plus.svg';
import GalleryNewCategoryCard from '../../components/gallery/GalleryNewCategoryCard';
import API from '../../utils/api';

function GalleryPaintingPage({ memberID, artistID }) {
  const [categories, setCategories] = useState([]);
  const [showNewCard, setShowNewCard] = useState(false);
  const handleNewCardBtnClick = () => {
    setShowNewCard(!showNewCard);
  };
  const fetchData = async () => {
    try {
      const url = `/api/category/list/${artistID}`;
      const response = await API.get(url);
      const { data } = response.data;
      console.log(data);
      setCategories(data);
    } catch (error) {
      console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
    }
  };
  useEffect(() => {
    fetchData();
  }, []);
  return (
    <div className="justify-center items-center mx-auto mt-10 mb-10 pt-10 pb-10 bg-white">
      {categories.map((category) => (
        <div key={category.categoryID} className="mb-5"><GalleryPaintingCard category={category} memberID={memberID} onDeleted={fetchData} /></div>
      ))}
      {showNewCard && (
      <GalleryNewCategoryCard
        onBtnClick={handleNewCardBtnClick}
        onAdded={fetchData}
      />
      )}
      {!showNewCard && sessionStorage.getItem('memberID') === memberID.toString() && (
        <button type="button" className="mx-auto flex justify-center" onClick={handleNewCardBtnClick}>
          <PlusIcon />
        </button>
      )}
    </div>
  );
}
export default GalleryPaintingPage;
