import React from 'react';
// eslint-disable-next-line react/prop-types
function ChattingListItem({ profileImg, nickname, message }) {
  return (
    <div className="flex flex-row py-4 px-2 items-center border-b-2 border-grey">
      <div className="w-1/4">
        <img
          src={profileImg}
          className="object-cover h-12 w-12 rounded-full"
          alt=""
        />
      </div>
      <div className="w-full mx-2 text-start line-clamp-2">
        <div className="text-lg font-semibold">{nickname}</div>
        <span className="text-gray-500 ">{message}</span>
      </div>
    </div>
  );
}
export default ChattingListItem;
