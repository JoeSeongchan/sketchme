/* eslint-disable no-unused-vars */
import React, { useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';

import { Rating, TextField } from '@mui/material';
import API, { URL } from '../../utils/api';

function ResultPage() {
  // 라이브 리덕스 변수 연동시키기
  const localUserRole = useSelector((state) => state.live.localUserRole);
  const thisMeetingId = useSelector((state) => state.live.meetingId);

  const [rating, setRate] = useState(5);
  const [content, setContent] = useState('');

  const [imgURL, setImgURL] = useState(null);
  const [timelapseURL, setTimelapseURL] = useState(null);

  const [isPlaying, setIsPlaying] = useState(false);
  const textRef = useRef(null);

  const [isReviewed, setIsReviewed] = useState(true);

  // 그림 가져오기
  const getImg = async () => {
    const url = `api/final-picture?meetingId=${thisMeetingId}`;
    const resImg = await API.get(url);
    setImgURL(resImg.data.data.pictureUri);
  };

  // 타임랩스 가져오기
  const getTimelapse = async () => {
    const url = `api/timelapse?meetingId=${thisMeetingId}`;
    const resTimelapse = await API.get(url);
    setTimelapseURL(resTimelapse.data.data.timelapseUrl);
  };

  // 후기 등록 버튼 클릭 핸들러
  const handleRegistClick = async () => {
    if (textRef.current && !content) {
      textRef.current.focus();
      return;
    }
    try {
      const url = 'api/review';
      const data = { meetingID: thisMeetingId, rating, content };
      const response = await API.post(url, data);
      setIsReviewed(true);
    } catch (error) {
      textRef.current.focus();
      textRef.current.placeholder = '후기를 등록하지 못했습니다';
    }
  };

  // 최초 렌더링 시 그림, 타임랩스 가져오기
  useEffect(() => {
    getImg();
    getTimelapse();
  }, []);

  return (
    <div className="grow mx-16 h-full flex flex-col  gap-y-5 sm:gap-y-10 flex-nowrap">
      <div className="mt-4 md:mt-10">
        {localUserRole === 'guest' ? (
          <div className="flex flex-col justify-around items-center sm:flex-row gap-4">
            <div className="flex flex-row items-center sm:flex-col ">
              <Rating
                value={rating}
                precision={0.5}
                onChange={(e) => {
                  setRate(e.target.value);
                }}
                style={{ visibility: isReviewed ? 'hidden' : 'visible' }}
              />
              <div style={{ visibility: isReviewed ? 'hidden' : 'visible' }}>
                {`${rating || 0}점`}
              </div>
            </div>
            {!isReviewed ? (
              <textarea
                ref={textRef}
                placeholder="후기를 작성해주세요"
                rows="2"
                multiline
                defaultValue={content}
                onChange={(e) => {
                  setContent(e.target.value);
                }}
                className="w-1/2 min-w-half border rounded-md p-3 focus:border-2 focus:outline-none border-primary"
              />
            ) : (
              <div className=" text-center w-1/2 min-w-half border rounded-md p-3   border-primary">
                후기 등록 완료!
              </div>
            )}

            <button
              type="button"
              onClick={handleRegistClick}
              className="bg-primary w-36 text-white h-10 rounded-[4px] px-2 py-2 hover:bg-primary_dark"
              style={{ visibility: isReviewed ? 'hidden' : 'visible' }}
            >
              후기 등록하기
            </button>
          </div>
        ) : null}
      </div>
      <div className="text-center text-2xl">예쁜 작품이 완성되었어요!</div>
      <div className="flex justify-evenly max-h-[45vh] gap-4">
        <div className="">
          {timelapseURL ? (
            <img
              src={`${URL}/api/display?imgURL=${timelapseURL}`}
              alt="완성 타임랩스"
              // onClick={handleClickGIF}
              className="border w-auto max-h-full "
            />
          ) : (
            <div>타임랩스를 가져오지 못했습니다</div>
          )}
        </div>
        <div>
          {imgURL ? (
            <img
              src={`${URL}/api/display?imgURL=${imgURL}`}
              alt="완성 그림"
              className="border w-auto max-h-full "
            />
          ) : (
            <div>그림을 가져오지 못했습니다</div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ResultPage;
