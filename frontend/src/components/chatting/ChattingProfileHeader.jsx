import React from 'react';
import { ReactComponent as BackIcon } from '../../assets/icons/Back.svg';

function ChattingProfileHeader({
  type, profileImg, nickname, onClickBack,
}) {
  return (
    <div className="w-full p-2 flex justify-between border-b-2 border-grey shadow-2xl p-1">
      <div className="flex justify-start min-w-fit">
        {type === 'small'
          && (
          <button type="button" className="h-11 flex items-center cursor-pointer" onClick={onClickBack}>
            <span>{' '}</span>
            <BackIcon />
          </button>
          )}
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
    </div>
  );
}

export default ChattingProfileHeader;
