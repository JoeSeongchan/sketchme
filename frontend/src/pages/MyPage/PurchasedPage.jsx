/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from 'react';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import MyPageSideBar from '../../components/MyPage/MyPageSideBar';
import BaseIconBtnPurple from '../../components/common/BaseIconBtnPurple';
import API from '../../utils/api';

function PurchasedPage() {
  const [myPictures, setMyPictures] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const url = '/api/my-drawings';
        const response = await API.get(url);
        setMyPictures(response.data.data);
        console.log(response.data.data);
      } catch (error) {
        console.error('내가 구매한 그림 목록을 가져오는데 실패했습니다.', error);
      }
    };
    fetchData();
  }, []);

  const handleDownloadBtnClick = (picture) => {
    console.log('다운로드 ', picture);
    console.log(picture.pictureImgUrl.imgUrl);
    const downloadUrl = `https://sketchme.ddns.net/api/download?imgURL=${picture.pictureImgUrl.thumbnailUrl}`;

    //
    const anchor = document.createElement('a');
    anchor.href = downloadUrl;
    console.log('downloadUrl : ', downloadUrl);

    document.body.appendChild(anchor);
    console.log(anchor);
    setTimeout(() => {
      anchor.click();
      document.body.removeChild(anchor);
    }, 100);
    // const fetchData = async () => {
    //   try {
    //     await API.get(url);
    //   } catch (error) {
    //     console.error('그림 다운로드에 실패했습니다.', error);
    //   }
    // };
    // fetchData();
  };

  return (
    <div>
      <MyPageSideBar current="myPictures" />
      <div className="p-4 sm:ml-64">
        <div className="flex justify-center items-center text-center mb-8 mt-8">
          <h1 className="text-4xl font-bold underline decoration-wavy decoration-primary bg-primary_4">내가 구매한 그림</h1>
        </div>
        {myPictures ? (
          <div className="flex w-full items-start px-4 py-4">
            {myPictures.map((picture) => (
              <div className="w-[300px] mr-8">
                <div className="relative rounded-lg overflow-hidden bg-primary_4 shadow-md p-4">
                  <img
                    src={`https://sketchme.ddns.net/api/display?imgURL=${picture.pictureImgUrl.thumbnailUrl}`}
                    alt=""
                    className="object-contain w-full h-64"
                    style={{ margin: '0 auto' }}
                  />
                </div>
                <div className="mt-3 flex justify-center">
                  <a href={`https://sketchme.ddns.net/api/download?imgURL=${picture.pictureImgUrl.imgUrl}`} className="py-2 px-2 h-8  rounded-lg flex justify-center items-center text-white bg-primary hover:bg-primary_3 focus:ring-primary_3 focus:ring-offset-primary_3 w-max transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2">
                    <FileDownloadIcon />
                    이미지 다운로드
                  </a>
                </div>
                <div className="mt-3 flex justify-center">
                  <a href={`https://sketchme.ddns.net/api/download?imgURL=${picture.pictureTimelapseUrl.timelapseUrl}`} className="py-2 px-2 h-8  rounded-lg flex justify-center items-center text-white bg-primary hover:bg-primary_3 focus:ring-primary_3 focus:ring-offset-primary_3 w-max transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2">
                    <FileDownloadIcon />
                    타임랩스 다운로드
                  </a>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <h1>아직 구매한 그림이 없습니다🙂 그림 구매를 원하실 경우, 마음에 드는 작가님께 예약 신청을 한 후 실시간 드로잉을 받아보세요!</h1>
        )}
      </div>
    </div>
  );
}

export default PurchasedPage;
