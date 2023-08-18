/* eslint-disable no-unused-vars */
import React from 'react';
import { v4 as uuidv4 } from 'uuid';
import { Rating } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import ProgressiveImage from 'react-progressive-graceful-image';
import { addSelectedButton } from '../../reducers/SearchSlice';
import Tag from './Tag';
import { URL } from '../../utils/api';
import Spinner from './CardSpinner';

function Card({
  id, // 그림 : 그림 ID, 작가 : 작가 ID
  userID, // 작가 : 작가의 유저 ID
  artistID, // 그림 : 작가 ID
  categoryID,
  title,
  cardUrl,
  thumbnailUrl,
  writerUrl,
  description,
  writer,
  hashtags,
  price,
  rating,
  review,
  reviewWriter,
}) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const selectedButtons = useSelector((state) => state.search.selectedButtons);
  const baseURL = `${URL}/api/display?imgURL=`;
  const imgCss = 'w-[200px] h-[200px] flex rounded-2xl border-4 border-grey';
  const divCss = 'w-[200px] flex flex-col select-none mx-auto'; // Add 'flex-col' for vertical arrangement
  const wrapperCss2 = 'w-[200px] flex-wrap flex justify-start';
  const fallbackImageUrl = 'https://us.123rf.com/450wm/orla/orla1303/orla130300033/18437114-3d-%EC%82%AC%EB%9E%8C-%EC%82%AC%EB%9E%8C-%EC%82%AC%EB%9E%8C%EA%B3%BC-%EB%AC%BC%EC%9D%8C%ED%91%9C-%EC%82%AC%EC%97%85%EA%B0%80.jpg?ver=6';

  // description의 길이를 체크하여 10자 이상이면 줄여주는 함수
  const filterDescription = (text) => {
    if (text && text.length > 30) {
      return (
        <span className="cursor-pointer text-sm">
          {`${text.slice(0, 30)}...`}
          {' '}
          <button
            type="button"
            onClick={() => navigate(`/gallery/${userID}/${artistID}`)}
            className="font-semibold hover:underline text-xs hover:font-bold"
          >
            {' '}
            더보기

          </button>
        </span>
      );
    }
    return text;
  };

  const handleTagClick = (item) => {
    if (!selectedButtons.includes(item)) {
      // 이동 및 Redux 액션 디스패치
      dispatch(addSelectedButton(item));
    }
    navigate('/search/drawing/?keyword=&orderBy=recent');
  };
  const handleKeyPress = (event, item) => {
    if (event.key === 'Enter') {
      handleTagClick(item);
    }
  };
  // 리뷰가 비워져있다면 리뷰, 리뷰작성자, 별점이 렌더링되지 않는다.
  const renderReviewSection = () => {
    if (review !== null) {
      return (
        <>
          <div className="w-[200px]">
            <img src="img/DdaomStart.png" alt="" className="w-2 opacity-50" />
          </div>
          <div className={wrapperCss2}>
            <p>
              {review}
            </p>
          </div>
          <div className="w-[200px] flex justify-end">
            <img src="img/DdaomEnd.png" alt="" className="w-2 opacity-50" />
          </div>
          <div className="w-[200px] flex justify-start">
            <span className="me-3 text-xs text-darkgrey">{reviewWriter}</span>
            <Rating value={parseFloat(rating)} precision={0.1} readOnly />
            {rating}
          </div>
        </>
      );
    }
    return null;
  };

  return (
    <div className={divCss}>
      <div className="h-80" style={{ position: 'relative' }}>
        <ProgressiveImage
          src={`${baseURL}${cardUrl}`}
          placeholder={`${baseURL}${thumbnailUrl}`}
          onError={() => {
            // 이미지 로딩 에러 처리
          }}
        >
          {(src, loading) => (
            <div>
              {loading && (
                <div className="w-[200px] h-[200px] flex items-center justify-center absolute top-0 left-0">
                  <Spinner />
                </div>
              )}
              <img
                src={src}
                alt=""
                className={`${imgCss} ${loading ? 'loading' : ''}`} // 로딩 중일 때 클래스 추가
              />
            </div>
          )}
        </ProgressiveImage>
        <div className="flex items-center mt-1">
          <span>
            {writerUrl && (<img className="w-7 h-7 rounded-full flex-none" src={`${baseURL + writerUrl}`} alt="artistProfileImg" />)}
          </span>
          <span
            className="ml-3 text-sm flex-grow hover:font-semibold font-medium Main2"
            onClick={() => navigate(`/gallery/${userID}/${artistID}`)}
            onKeyDown={(event) => {
              if (event.key === 'Enter') {
                navigate(`/gallery/${userID}/${artistID}`);
              }
            }}
            tabIndex={0}
            role="button"
          >
            {writer}
          </span>
          {price !== 0 ? (
            <span className="ml-3 text-xs text-right">
              {price}
              {' '}
              원~
            </span>
          ) : (
            <span className="ml-3 text-xs text-right">
              무료~
            </span>
          )}
        </div>
        <div className="font-semibold text-sm my-2">
          <span
            role="button"
            className="hover: Gothic2"
            onClick={() => navigate(`/gallery/${userID}/${artistID}`)}
            onKeyDown={(event) => {
              if (event.key === 'Enter') {
                navigate(`/gallery/${userID}/${artistID}`);
              }
            }}
            tabIndex={0}
          >
            {title}
          </span>
        </div>
        <div>{filterDescription(description)}</div>
        <div className={wrapperCss2}>
          {hashtags.map((tag) => (
            <span role="button" className="me-1" key={uuidv4()} onClick={() => handleTagClick(tag.name)} onKeyDown={(event) => handleKeyPress(event, tag.name)} tabIndex={0}>
              <Tag message={tag.name} />
            </span>
          ))}
        </div>
      </div>
      {renderReviewSection()}
      <p className="mb-20"> </p>
    </div>
  );
}

Card.defaultProps = {
  id: null,
  categoryID: null,
  title: null,
  url: null,
  description: null,
  writer: null,
  hashtags: [],
  price: null,
  rating: null,
  review: null,
  reviewWriter: null,
};

export default Card;
