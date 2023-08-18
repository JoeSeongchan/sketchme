import React from 'react';

function ReservationCheckCard({ reservation }) {
  return (
    <div>
      <div className="bg-white mt-10 mb-10 shadow-2xl p-10 rounded-lg mx-auto md:mx-auto justify-center">
        <div className="w-full mx-auto mt-5 flex justify-center">
          {reservation
            && (
              <div className="text-xl">
                <h1 className="text-2xl font-semibold text-center mb-8">예약정보</h1>
                <h2 className="text-3xl font-semibold text-darkgrey text-center mb-20">
                  &lt;
                  {reservation.categoryName}
                  &gt;
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    작가 닉네임
                    {' '}
                  </span>
                  <span className="flex-grow">{reservation.artistNickname}</span>
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    예약자 닉네임
                    {' '}
                  </span>
                  <span className="flex-grow">{reservation.userNickname}</span>
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    요청사항
                    {' '}
                  </span>
                  {reservation.content}
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    예약일시
                    {' '}
                  </span>
                  {reservation.startDatetime.split('T')[0]}
                  {' '}
                  {reservation.startDatetime.split('T')[1].substr(0, 5)}
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span className="text-darkgrey flex-none w-2/5">
                    신청일시
                    {' '}
                  </span>
                  {reservation.createDatetime.split('T')[0]}
                  {' '}
                  {reservation.createDatetime.split('T')[1].substr(0, 5)}
                </h2>
                <h2 className="font-semibold flex items-center">
                  <span span className="text-darkgrey flex-none w-2/5">
                    가격
                    {' '}
                  </span>
                  {reservation.exactPrice}
                  원
                </h2>
                <h2 className="mt-14 flex items-center text-darkgrey">
                  {reservation.isOpen ? '작가 프로필에 사진이 공개되는 것에 동의합니다 ✅' : '작가 프로필에 사진이 공개되는 것에 동의하지 않습니다 ❌'}
                </h2>
              </div>
            )}
        </div>
        <div className="mt-10 mb-2 border-t-2 border-lightgrey" />
      </div>
    </div>
  );
}

export default ReservationCheckCard;
