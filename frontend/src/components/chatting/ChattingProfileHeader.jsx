import React from 'react';
import BaseBtnPurple from '../common/BaseBtnPurple';

// eslint-disable-next-line react/prop-types
function ChattingProfileHeader({ profileImg, nickname }) {
  return (
    <div className="w-full flex justify-between border-b-2 border-grey">
      <div className="flex justify-start min-w-fit">
        <img
          src={profileImg}
          className="object-cover h-11 w-11 rounded-full my-auto"
          alt=""
        />
        <div
          className="ml-2 py-3 px-4 rounded-tr-3xl rounded-tl-xl text-start font-semibold text-black text-lg"
        >
          {nickname}
        </div>
      </div>
      <div className="my-auto">
        <BaseBtnPurple message="라이브방 입장" />
      </div>
    </div>
  );
}

export default ChattingProfileHeader;
