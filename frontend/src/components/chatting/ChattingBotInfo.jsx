import React from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { updateMyUserName, updateMeetingId, updateLocalUserRole } from '../../reducers/LiveSlice';
import BaseBtnPurple from '../common/BaseBtnPurple';

function ChattingBotInfo({ type, message, memberType }) {
  // const { meetingID } = JSON.parse(message);
  const { meetingID, startDateTime } = JSON.parse(message);
  const name = memberType === 'USER' ? sessionStorage.getItem('userName') : sessionStorage.getItem('artistName');
  const navigate = useNavigate();
  const dispatch = useDispatch();
  console.log(name, meetingID, memberType);
  const goLive = () => {
    dispatch(updateMyUserName(name));
    dispatch(updateMeetingId(meetingID));
    dispatch(updateLocalUserRole(memberType === 'ARTIST' ? 'artist' : 'guest'));

    // 라이브 방으로 이동
    navigate(`/live/${name}/${meetingID}/${memberType}`);
  };

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
        <div>[BOT] 10분 후 라이브방이 열립니다.</div>
        {startDateTime && (
          <div>
            시작일시 :
            {' '}
            {startDateTime.split('T')[0]}
            {' '}
            {startDateTime.split('T')[1].substr(0, 5)}
          </div>
        )}

        <BaseBtnPurple message="라이브방 입장" onClick={goLive} />
      </div>
    </div>
  );
}
export default ChattingBotInfo;
