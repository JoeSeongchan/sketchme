import React, { useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import API from '../../utils/api';

function LeftChatting({ type, message }) {
  const { meetingID } = JSON.parse(message);
  const [meetingInfo, setMeetingInfo] = useState(null);

  // 예약 내역 가져오기
  const getMeetingInfo = async () => {
    try {
      const url = `/api/meeting/${meetingID}`;
      const data = await API.get(url);
      console.log(data.data.data);
      setMeetingInfo(data.data.data);
    } catch (error) {
      console.error('예약 내역을 가져오는데 실패했습니다.', error);
    }
  };

  // 수락하기
  const handleConfirm = async (status) => {
    try {
      const url = '/api/meeting';
      const body = {
        meetingID,
        statusDetermination: status,
      };
      const response = await API.put(url, body);
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error('예약수락 및 거절에 실패했습니다.', error);
      throw error;
    }
  };

  const showDetail = () => {
    Swal.fire({
      icon: 'info',
      title: meetingInfo.categoryName,
      html: `<div class="text-xl">
          <h2 class="font-semibold flex items-center">
            <span class="text-darkgrey flex-none text-right mr-10 w-2/5">
              고객명:
            </span>
            ${meetingInfo.userNickname}
          </h2>
          <h2 class="font-semibold flex items-center">
            <span class="text-darkgrey flex-none text-right mr-10 w-2/5">
              요청사항:
            </span>
            ${meetingInfo.content}
          </h2>
          <h2 class="font-semibold flex items-center">
            <span class="text-darkgrey flex-none text-right mr-10 w-2/5">
              예약일시:
            </span>
            ${meetingInfo.startDatetime.split('T')[0]} 
            ${meetingInfo.startDatetime.split('T')[1].substr(0, 5)}
          </h2>
          <h2 class="font-semibold flex items-center">
            <span class="text-darkgrey flex-none text-right mr-10 w-2/5">
              신청일시:
            </span>
            ${meetingInfo.createDatetime.split('T')[0]} 
            ${meetingInfo.createDatetime.split('T')[1].substr(0, 5)}
          </h2>
          <h2 class="font-semibold flex items-center">
            <span class="text-darkgrey flex-none text-right mr-10 w-2/5">
              가격:
            </span>
            ${meetingInfo.exactPrice}
            원
          </h2>
          <h2 class="mt-8 flex items-center text-xs justify-center text-center text-darkgrey">
          ${meetingInfo.open
    ? '작가 프로필에 사진이 공개되는 것에 동의합니다.'
    : '작가 프로필에 사진이 공개되는 것에 동의하지 않습니다.'
}
          </h2>
        </div>`,
    });
  };

  useEffect(() => {
    getMeetingInfo();
  }, []);

  return (
    <div className="flex justify-start ml-2 mb-4">
      <img
        src="img/Bot.png"
        className="object-cover h-8 w-8 rounded-full"
        alt=""
      />
      <div
        className={`${type === 'small' ? 'mr-20' : 'mr-10 md:mr-80'} ml-4 py-2 px-4 bg-grey rounded-2xl text-start font-semibold text-black`}
      >
        {meetingInfo ? (
          <>
            <div>[BOT] 예약 신청이 들어왔습니다</div>
            <div>
              예약일자 :
              {' '}
              {meetingInfo.startDatetime.split('T')[0]}
              {' '}
              {meetingInfo.startDatetime.split('T')[1].substr(0, 5)}
            </div>
            <div className="flex flex-row mt-5">
              <button type="button" onClick={showDetail} className={`${type === 'small' ? 'mx-1' : 'mx-2'} py-2 px-4 h-10 rounded-lg flex justify-center items-center text-black bg-white hover:bg-grey focus:ring-grey focus:ring-offset-grey w-max transition ease-in duration-200 text-center font-semibold shadow-md shadow-darkgrey focus:outline-none focus:ring-2 focus:ring-offset-2  rounded-full`}>
                상세보기
              </button>
              <button
                type="button"
                onClick={() => handleConfirm('APPROVED')}
                className={`${type === 'small' ? 'mx-1' : 'mx-2'} py-2 px-4 h-10 rounded-lg flex justify-center items-center text-black bg-primary_3 hover:bg-primary_2 focus:ring-primary_2 focus:ring-offset-primary_2 w-max transition ease-in duration-200 text-center font-semibold shadow-md shadow-darkgrey focus:outline-none focus:ring-2 focus:ring-offset-2  rounded-full
                ${meetingInfo.meetingStatus !== 'WAITING' ? 'disabled opacity-50 cursor-not-allowed' : ''}`}
                disabled={meetingInfo.meetingStatus !== 'WAITING'}
              >
                수락
              </button>
              <button
                type="button"
                onClick={() => handleConfirm('DENIED')}
                className={`${type === 'small' ? 'mx-1' : 'mx-2'} py-2 px-4 h-10 rounded-lg flex justify-center items-center text-black bg-grey hover:bg-lightgrey focus:ring-lightgrey focus:ring-offset-lightgrey w-max transition ease-in duration-200 text-center font-semibold shadow-md shadow-darkgrey focus:outline-none focus:ring-2 focus:ring-offset-2  rounded-full
                ${meetingInfo.meetingStatus !== 'WAITING' ? 'disabled opacity-50 cursor-not-allowed' : ''}`}
                disabled={meetingInfo.meetingStatus !== 'WAITING'}
              >
                거절
              </button>
            </div>
          </>
        ) : (
          <div>예약 신청을 불러오는 중입니다.</div>
        )}
      </div>
    </div>
  );
}
export default LeftChatting;
