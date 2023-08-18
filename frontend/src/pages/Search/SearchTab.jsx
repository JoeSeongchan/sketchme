/* eslint-disable max-len */
/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import {
  FormControl, InputLabel, MenuItem, Select,
} from '@mui/material';
import Card from '../../components/common/Card';
import cardHook from '../../utils/cardHook';
import Spinner from '../../components/common/Spinner';
import { removeAllSelectedButtons } from '../../reducers/SearchSlice';
import Reload from '../../assets/icons/Reload.jpg';

function SearchTab({ currentPage, setPage, priceButtons }) {
  // 현재 주소를 /search/{category}/{keyword}로 받아온다
  const { pathname, search } = useLocation();
  const dispatch = useDispatch();
  const [, category] = pathname.split('/').slice(1);
  const queryParams = new URLSearchParams(search);
  const keyword = queryParams.get('keyword');
  const [orderBy, setOrderBy] = useState(queryParams.get('orderBy') || 'recent');
  const orderByOptions = [
    { value: 'recent', label: '최신순' },
    { value: 'price', label: '가격순' },
    { value: 'rating', label: '평점순' },
  ];
  const selectedButtons = useSelector((state) => state.search.selectedButtons);
  const initialTab = category === 'artist' ? 1 : 0;

  const navigate = useNavigate();
  const handleOrderByChange = (event) => {
    const newOrderBy = event.target.value;
    setOrderBy(newOrderBy);

    // Update the URL with the new orderBy parameter
    queryParams.set('orderBy', newOrderBy);
    navigate(`/search/${category}/?keyword=${keyword}&orderBy=${newOrderBy}`);
  };

  let endpoint;
  if (category === 'drawing') {
    endpoint = `/api/search/drawing?keyword=${keyword}&orderBy=${orderBy}`;
  } else {
    endpoint = `/api/search/artist?keyword=${keyword}&orderBy=${orderBy}`;
  }

  // 커스텀 훅을 이용하여 api 요청
  const { Data: cards, Loading, Error } = cardHook(endpoint);
  const [activeTab, setActiveTab] = useState(initialTab);

  // 헤더에서 현재 탭과 다른 탭을 요청했을 때 탭 변경하게 하는 훅
  useEffect(() => {
    const newTab = category === 'artist' ? 1 : 0;
    setActiveTab(newTab);
  }, [category]);

  if (Loading) {
    // eslint-disable-next-line react/jsx-boolean-value
    return <Spinner />; // 또는 로딩 상태에 맞게 처리
  }

  if (Error) {
    return <div>검색결과 불러오기 실패</div>; // 또는 에러 상태에 맞게 처리
  }
  const ITEMS_PER_PAGE = 8; // 페이지당 아이템 개수

  // 선택한 태그들 and 조건으로 필터링
  let filteredCards = [...cards]; // 기본적으로 모든 카드를 포함하도록 설정

  // 테마, 분위기 필터링
  if (selectedButtons.length > 0) {
    filteredCards = cards.filter((card) => {
      if (card.hashtags) {
        const cardHashtags = card.hashtags.map((hashtag) => hashtag.name); // card의 해시태그 이름들을 모아놓은 배열 생성
        const allButtonsIncluded = selectedButtons.every((button) => (priceButtons.includes(button) ? true : cardHashtags.includes(button))); // 모든 버튼이 포함되는지 확인
        return allButtonsIncluded;
      }
      return false;
    });
  }
  // 가격 필터링
  const priceFilters = [];

  if (selectedButtons.includes('~ 1000')) {
    priceFilters.push((card) => card.price === null || card.price <= 1000);
  }
  if (selectedButtons.includes('1000 ~ 5000')) {
    priceFilters.push((card) => card.price >= 1000 && card.price <= 5000);
  }
  if (selectedButtons.includes('5000 ~ 10000')) {
    priceFilters.push((card) => card.price >= 5000 && card.price <= 10000);
  }
  if (selectedButtons.includes('10000 ~ 50000')) {
    priceFilters.push((card) => card.price >= 10000 && card.price <= 50000);
  }
  if (selectedButtons.includes('50000 ~')) {
    priceFilters.push((card) => card.price >= 50000);
  }

  if (priceFilters.length > 0) {
    filteredCards = filteredCards.filter((card) => priceFilters.some((filter) => filter(card)));
  }
  // 현재 페이지에 해당하는 아이템들을 계산하는 함수
  const getCurrentPageItems = () => {
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    return filteredCards.slice(startIndex, endIndex);
  };

  // Tab click handler
  const handleTabClick = (index) => {
    setActiveTab(index);
    setPage(1); // Reset to the first page when tab is clicked
  };
  const handleClearAll = () => {
    // Clear all selected buttons using dispatch
    dispatch(removeAllSelectedButtons());
  };
  return (
    <div>
      <div className="flex justify-between w-2/3 mx-auto">
        <div className="flex justify-end mt-4">
          <button type="button" onClick={handleClearAll} className="inline-flex items-center border font-semibold text-black pe-3 py-2 rounded min-h-10">
            <img src={Reload} alt="" className="w-6 inline mx-1 mb-1" />
            전체삭제
          </button>
        </div>
        {/* 탭 메뉴 */}
        <ul className="flex justify-center items-center flex-wrap">
          <li className="mr-4">
            <Link to={`/search/drawing/?keyword=${keyword}&orderBy=recent`}>
              <button
                type="button"
                className={`py-2 px-4 font-semibold ${activeTab === 0 ? 'inline-block text-primary hover:border-primary_2 rounded-t-lg py-4 px-4 text-sm font-bold text-center border-transparent border-b-2 active' : 'inline-block rounded-t-lg py-4 px-4 text-sm font-medium text-center  bg-gray-200 text-gray-700'}`}
                onClick={() => handleTabClick(0)}
              >
                그림
              </button>
            </Link>
          </li>
          <li className="mr-4">
            <Link to={`/search/artist/?keyword=${keyword}&orderBy=recent`}>
              <button
                type="button"
                className={`py-2 px-4 font-semibold ${activeTab === 1 ? 'inline-block text-primary  hover:border-primary_2 rounded-t-lg py-4 px-4 text-sm font-bold text-center border-transparent border-b-2 active' : 'inline-block rounded-t-lg py-4 px-4 text-sm font-medium text-center  bg-gray-200 text-gray-700'}`}
                onClick={() => handleTabClick(1)}
              >
                작가
              </button>
            </Link>
          </li>
        </ul>
        <div className="text-end mt-3">
          <FormControl variant="outlined">
            <InputLabel id="order-by-label">정렬 순서</InputLabel>
            <Select
              labelId="order-by-label"
              id="order-by"
              value={orderBy}
              onChange={handleOrderByChange}
              label="정렬 순서"
              classes={{ root: 'custom-select-root' }}
            >
              {orderByOptions.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </div>
      </div>
      <div className="flex mt-4 flex-wrap w-3/4 mx-auto">
        {getCurrentPageItems().map((card) => (
          <div key={uuidv4()} className="px-2 sm:w-1/2 md:w-1/2 lg:w-1/3 xl:w-1/4">
            <Card {...card} />
          </div>
        ))}
      </div>
      {/* Pagination */}
      <div className="flex justify-center mt-4">
        {Array.from({ length: Math.ceil(filteredCards.length / ITEMS_PER_PAGE) }, (_, index) => (
          <button
            type="button"
            key={index}
            className={`mx-2 px-4 py-2 font-medium rounded-full 
              ${currentPage === index + 1 ? 'bg-primary text-white' : 'bg-gray-300 text-gray-600'}`}
            onClick={() => setPage(index + 1)}
          >
            {index + 1}
          </button>
        ))}
      </div>
    </div>
  );
}

export default SearchTab;
