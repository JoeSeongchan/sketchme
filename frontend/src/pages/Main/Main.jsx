/* eslint-disable import/no-unresolved */
/* eslint-disable no-console */
/* eslint-disable react/style-prop-object */
import React from 'react';
import { Link } from 'react-router-dom';
import { Swiper, SwiperSlide } from 'swiper/react'; // autoplay 모듈 불러오기
import 'swiper/css'; // swiper의 기본 스타일 불러오기
import { Autoplay } from 'swiper/modules'; // swiper 불러오기
import Carousel from '../../components/common/Carousel';
import { ReactComponent as MainFoot } from '../../assets/icons/MainFooter.svg';
import cardHook from '../../utils/cardHook';
import Spinner from '../../components/common/Spinner';

function Main() {
  const { Data: recentPictures, Loading: recentPicturesLoading, Error: recentPicturesError } = cardHook('/api/search/drawing?keyword=&orderBy=recent');
  const { Data: artists, Loading: artistsLoading, Error: artistsError } = cardHook('/api/search/artist?keyword=&orderBy=recent');
  if (recentPicturesLoading || artistsLoading) {
    return (
      <Spinner />
    ); // 또는 로딩 상태에 맞게 처리
  }

  if (recentPicturesError || artistsError) {
    return <div>그림 또는 작가 불러오기 실패</div>; // 또는 에러 상태에 맞게 처리
  }
  return (
    <div>
      <div className="relative">
        <Swiper
          spaceBetween={0}
          centeredSlides
          autoplay={{
            delay: 5000,
            disableOnInteraction: false,
          }}
          modules={[Autoplay]}
          speed={1000}
          loop
          className="mySwiper"
        >
          <SwiperSlide>
            <img src="img/WallPaper.jpg" alt="" className="opacity-70 z-0" />
          </SwiperSlide>
          <SwiperSlide>
            <img src="img/WallPaper2.jpg" alt="" className="opacity-70 z-0" />
          </SwiperSlide>
          <SwiperSlide>
            <img src="img/WallPaper3.jpg" alt="" className="opacity-70 z-0" />
          </SwiperSlide>
          <SwiperSlide>
            <img src="img/WallPaper4.jpg" alt="" className="opacity-70 z-0" />
          </SwiperSlide>
        </Swiper>
        <div className="absolute top-1/3 left-1/4 flex z-10">
          <div className="top-1/3 left-1/4 flex z-10 md:text-6xl text-black Cute1 font-bold select-none">
            <div className="">
              순간을 그림으로
              <br />
              남기세요
              <br />
              <div className="hidden md:block md:text-5xl pl-1 pt-7 Cute1 font-normal">
                반려동물, 친구와 함께!
              </div>
            </div>
          </div>
        </div>
      </div>
      <br />
      {recentPictures && <Carousel cards={recentPictures} text="최근 그림" />}
      <hr className="w-3/4 ml-44 my-14 opacity-30" />
      <br />
      {artists && <Carousel cards={artists} text="작가" />}
      <div className="flex justify-around w-100% h-[330px] bg-darkgrey">
        <span className="mt-20 text-white font-semibold Gothic2 text-4xl">
          <div className="mb-3">
            <span className="text-beige font-bold">캐리커처 작가</span>
            로 활동하면서 수익을 만들어보세요!
          </div>
          <div className="text-3xl font-medium">
            <span id="LogoLetter" className="text-primary_3">
              Sketch Me
            </span>
            {' '}
            에서는 누구나 작가로 활동할 수 있어요!
          </div>
          <div className="mb-10 text-3xl font-medium">
            캐리커처를 기다리는 사람, 여기 다 모여있어요.
          </div>
          <Link to="/register" className="hover:underline hover:cursor-pointer font-semibold text-3xl">
            작가 등록하기 →
          </Link>
        </span>
        <MainFoot className="w-60" style={{ height: 'auto' }} />
      </div>
    </div>
  );
}

export default Main;
