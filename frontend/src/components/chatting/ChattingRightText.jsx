import React from 'react';

// eslint-disable-next-line react/prop-types
function RightChatting({ profileImg, message }) {
  return (
    <div className="flex justify-end mb-4">
      <div
        className="mr-2 py-3 px-4 bg-primary_3 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-start font-semibold text-black"
      >
        {message}
      </div>
      <img
        src={profileImg}
        className="object-cover h-8 w-8 rounded-full"
        alt=""
      />
    </div>
  );
}
export default RightChatting;
