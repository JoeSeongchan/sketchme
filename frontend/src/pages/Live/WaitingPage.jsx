import React from 'react';
import { useSelector } from 'react-redux';

function WaitingPage() {
  const isWaiting = useSelector((state) => state.live.waitingActive);
  return (
    <div className="flex flex-col item-center justify-center content-center">
      <div className="text-base">
        {isWaiting ? (
          '접속 중 입니다. 잠시만 기다려주세요'
        ) : (
          <div>
            <div>
              대기화면 입니다. 아래의 툴바에서 접속 옵션을 설정해주세요
            </div>
            <div>
              카메라와 마이크가 모두 이용 가능해야 라이브에 접속할 수 있습니다
            </div>
          </div>
        )}
      </div>

      {isWaiting ? (
        <img
          src="img/logosketch.png"
          alt="palettimy"
          className="w-16 h-16 my-5 z-0 animate-pulse justify-self-center self-center"
        />
      ) : (
        <img
          src="img/logosketch.png"
          alt="palettimy"
          className="w-16 h-16 my-5 z-0 animate-slide justify-self-center self-center"
        />
      )}
    </div>
  );
}

export default WaitingPage;
