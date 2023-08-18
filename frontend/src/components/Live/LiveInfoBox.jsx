/* eslint-disable comma-dangle */
/* eslint-disable operator-linebreak */
import React, { useEffect, useState } from 'react';
// import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import API from '../../utils/api';

import { updateProductName } from '../../reducers/LiveSlice';

function LiveInfoBox() {
  const [isExist, setIsExist] = useState(false); // 정보 존재 여부
  const [artistNickname, setArtistNickname] = useState(null); // 작가 닉네임
  // const [artistEmail, setartistEmail] = useState(null); // 작가 이메일
  const [customerNickname, setCustomerNickname] = useState(null); // 구매자 닉네임
  // const [customerEmail, setCustomerEmail] = useState(null); // 구매자 이메일
  const [reserveDate, setReserveDate] = useState(null); // 예약 일자, 포맷: YYYY:MM:DD:HH:MM
  const [applyDate, setApplyDate] = useState(null); // 신청 일자, 포맷: YYYY:MM:DD
  // const [charge, setcharge] = useState(null); // 결제 금액

  const thisMeetingId = useSelector((state) => state.live.meetingId);

  const dispatch = useDispatch();
  const getMeetingInfo = async () => {
    const url = `api/meeting/${thisMeetingId}`;
    const response = await API.get(url);

    if (response.data) {
      setIsExist(true);
      dispatch(updateProductName(response.data.data.categoryName));
      setArtistNickname(response.data.data.artistNickname);
      setCustomerNickname(response.data.data.userNickname);

      const resDate = response.data.data.startDatetime.split('T');
      const resymd = resDate[0].split('-');
      const reshms = resDate[1].split(':');
      setReserveDate(
        `${resymd[0]}년  ${resymd[1]}월 ${resymd[2]}일 ${reshms[0]}시 ${reshms[1]}분`
      );

      const creDate = response.data.data.createDatetime.split('T');
      const creymd = creDate[0].split('-');
      const crehms = creDate[1].split(':');

      setApplyDate(
        `${creymd[0]}년  ${creymd[1]}월 ${creymd[2]}일 ${crehms[0]}시 ${crehms[1]}분`
      );
    }
  };

  useEffect(() => {
    getMeetingInfo();
  }, []);

  return (
    <div className="w-max max-w-[220px] flex flex-col justify-center items-center ">
      <div id="guideMessage">
        <div className="text-xl bg-primary_2 text-white p-2 rounded-[4px]">
          안내 사항
        </div>
        <div className="text-xs">
          상담 중 그린 밑그림은 드로잉시 작가의 화면에서 볼 수 있으나, 수정할 수
          없으며 타임랩스에 포함되지 않습니다.
        </div>
      </div>

      {isExist ? (
        <div id="meetingInfo" className="py-2">
          <div className="text-xl bg-primary_2 text-white p-2 rounded-[4px]">
            예약 정보
          </div>
          <div className="text-xs">
            <span>작가 닉네임 : </span>
            {artistNickname}
          </div>
          <div className="text-xs">
            <span>고객 닉네임 : </span>
            {customerNickname}
          </div>
          <div className="text-xs">
            <span>예약 일자 : </span>
            {reserveDate}
          </div>
          <div className="text-xs">
            <span>신청 일자 : </span>
            {applyDate}
          </div>
        </div>
      ) : (
        <div>예약 정보가 없습니다</div>
      )}
    </div>
  );
}

export default LiveInfoBox;
