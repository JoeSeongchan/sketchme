/* eslint-disable react/jsx-props-no-spreading */
/* eslint-disable eol-last */
/* eslint-disable react/jsx-closing-tag-location */
/* eslint-disable jsx-a11y/alt-text */
/* eslint-disable react/jsx-closing-bracket-location */
/* eslint-disable react/jsx-indent */
/* eslint-disable react/jsx-boolean-value */
/* eslint-disable react/jsx-curly-brace-presence */
/* eslint-disable react/jsx-indent-props */
/* eslint-disable import/no-unresolved */
import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { v4 as uuidv4 } from 'uuid';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';

// import required modules
import { Pagination, Navigation } from 'swiper/modules';

import Card from './Card';

function Carousel({ cards, text }) {
  const responsiveBreakpoints = {
    // 반응형 설정: 작은 화면에서 1개, 중간 화면에서 3개, 데스크톱 화면에서 5개
    200: {
      slidesPerView: 1,
    },
    600: {
      slidesPerView: 2,
    },
    1024: {
      slidesPerView: 3,
    },
    1280: {
      slidesPerView: 4,
    },
    1536: {
      slidesPerView: 5,
    },
  };

  // 최대 12개의 원소로 자르기
  const trimmedCards = cards.slice(0, 12);

  return (
    <div className="mx-auto w-3/4 mb-10">
      <h1 className="text-3xl font-bold mt-36 ml-5 mb-5 select-none Main2">
        {text}
      </h1>
      <Swiper
        spaceBetween={4} // 카드 사이 여백
        breakpoints={responsiveBreakpoints}
        pagination={{
          clickable: true,
        }}
        navigation={true}
        modules={[Pagination, Navigation]}
        className="mySwiper"
        autoHeight={true}
      >
        {trimmedCards.map((card) => (
          <SwiperSlide key={uuidv4()}>
            <Card {...card} />
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}

export default Carousel;
