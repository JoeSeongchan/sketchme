import React from 'react';

function ChattingBotMessage({ type, message }) {
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
        <div>{message}</div>
      </div>
    </div>
  );
}
export default ChattingBotMessage;
