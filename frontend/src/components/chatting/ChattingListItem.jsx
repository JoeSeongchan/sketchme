/* eslint-disable jsx-a11y/click-events-have-key-events */
/* eslint-disable jsx-a11y/no-static-element-interactions */
import React from 'react';
// eslint-disable-next-line react/prop-types
function ChattingListItem({
  item, onClickRoom,
}) {
  const handleClick = () => {
    onClickRoom(item); // 클릭 시 onClickRoom 함수에 room를 인자로 전달
  };
  console.log(item);
  return (
    <div className="flex flex-row py-4 px-2 items-center border-b-2 border-grey cursor-pointer shadow-md" onClick={handleClick}>
      <div className="w-1/4">
        <img
          src={item.chatPartnerImageURL ? `https://sketchme.ddns.net/api/display?imgURL=${item.chatPartnerImageURL}` : 'https://cdn.spotvnews.co.kr/news/photo/202301/580829_806715_1352.jpg'}
          className="object-cover h-12 w-12 rounded-full"
          alt=""
        />
      </div>
      <div className="w-full mx-2 text-start line-clamp-2">
        <div className="text-lg font-semibold">{item.chatPartnerName}</div>
        <span className="text-gray-500 ">{item.lastChat}</span>
      </div>
    </div>
  );
}
export default ChattingListItem;
