import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import API from '../../utils/api';

function ReservationConfirmPage() {
  const location = useLocation();
  const { reservationId } = location.state;
  const [info, seTInfo] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const url = `/api/meeting/${reservationId}`;
        const data = await API.get(url);
        console.log(data.data.data);
        seTInfo(data.data.data);
      } catch (error) {
        console.error('예약 내역을 가져오는데 실패했습니다.', error);
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <h1 className="mx-auto text-3xl font-bold mt-28 md:mx-auto justify-center text-center">😊 예약이 완료되었습니다 😊</h1>
      <div className="w-3/5 bg-white mt-20 mb-20 shadow-2xl p-1 rounded-lg mx-auto md:mx-auto justify-center">
        <div className="w-full mx-auto mt-20 flex justify-center">
          {info
            && (
              <div className="text-xl">
                <h1 className="text-2xl font-semibold text-center mb-8">예약정보</h1>
                <h2 className="text-3xl font-semibold text-darkgrey text-center mb-20">
                  &lt;
                  {info.categoryName}
                  &gt;
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    작가 닉네임
                    {' '}
                  </span>
                  <span className="flex-grow">{info.artistNickname}</span>
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    작가 이메일
                    {' '}
                  </span>
                  <span className="flex-grow">{info.artistEmail}</span>
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    요청사항
                    {' '}
                  </span>
                  {info.content}
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    예약일시
                    {' '}
                  </span>
                  {info.startDatetime.split('T')[0]}
                  {' '}
                  {info.startDatetime.split('T')[1].substr(0, 5)}
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    신청일시
                    {' '}
                  </span>
                  {info.createDatetime.split('T')[0]}
                  {' '}
                  {info.createDatetime.split('T')[1].substr(0, 5)}
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span span className="text-darkgrey flex-none w-2/5">
                    가격
                    {' '}
                  </span>
                  {info.exactPrice}
                  원
                </h2>
                <h2 className="mt-14 flex items-center text-darkgrey">
                  {info.isOpen ? '작가 프로필에 사진이 공개되는 것에 동의합니다.' : '작가 프로필에 사진이 공개되는 것에 동의하지 않습니다.'}
                </h2>
              </div>
            )}
        </div>
        <div className="mt-10 mb-20 border-t-2 border-lightgrey" />
      </div>
    </div>
  );
}

export default ReservationConfirmPage;
