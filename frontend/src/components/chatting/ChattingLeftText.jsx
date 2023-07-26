import React from 'react';

// eslint-disable-next-line react/prop-types
function LeftChatting({ profileImg, message }) {
  return (
    <div className="flex justify-start mb-4">
      <img
        src={profileImg}
        className="object-cover h-8 w-8 rounded-full"
        alt=""
      />
      <div
        className="ml-2 py-3 px-4 bg-grey rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-start font-semibold text-black"
      >
        {message}
      </div>
    </div>
  );
}
export default LeftChatting;
