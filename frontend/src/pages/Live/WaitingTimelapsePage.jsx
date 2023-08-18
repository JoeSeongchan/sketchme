import React from 'react';

function WaitingTimelapsePage() {
  return (
    <div className="flex flex-col item-center justify-center content-center">
      <div className="text-base">
        타임랩스를 생성중입니다. 잠시만 기다려주세요
      </div>
      <img
        src="img/logosketch.png"
        alt="palettimy"
        className="w-16 h-16 my-5 z-0 animate-pulse justify-self-center self-center"
      />
    </div>
  );
}

export default WaitingTimelapsePage;
