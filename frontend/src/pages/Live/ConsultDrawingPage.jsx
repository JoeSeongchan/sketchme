/* eslint-disable indent */
import React from 'react';
import { useSelector } from 'react-redux';

import StreamComponent from '../../components/Live/Stream';
// eslint-disable-next-line import/no-cycle
import DrawingBox from '../../components/drawing/DrawingBox';
import LiveInfoBox from '../../components/Live/LiveInfoBox';
// import ChatBox from '../../components/Live/ChatBox';

function ConsultDrawingPage({ localUser, subscribers, showCanvas }) {
  const liveStatus = useSelector((state) => state.live.liveStatus);
  const localUserRole = useSelector((state) => state.live.localUserRole);

  return (
    <div className="flex justify-center gap-10 items-center h-full w-full">
      <div id="rightBody" className="w-fit h-fit">
        {localUserRole === 'artist' ? (
          <DrawingBox showCanvas={showCanvas} />
        ) : (
          <div className="flex w-[60vw] justify-evenly">
            <LiveInfoBox />
            <div className="flex justify-center item-center w-[400px] h-fit border">
              {subscribers
                .filter((sub) => sub.role === 'canvas')
                .map((sub) => (
                  <StreamComponent user={sub} key={sub.connectionId} />
                ))}
            </div>
          </div>
        )}
      </div>

      <div id="rightSideBar" className="w-[30vw] h-full flex items-center">
        {liveStatus === 1 ? (
          // 상담 화면이면 상대, 나 순서대로 띄움
          <div className="flex flex-col justify-center gap-3" id="consultVideo">
            {subscribers.length !== 0 ? (
              subscribers
                .filter((sub) => sub.role !== 'canvas')
                .map((sub) => (
                  <StreamComponent user={sub} key={sub.connectionId} />
                ))
            ) : (
              <div>상대방을 기다리는 중 입니다</div>
            )}
            <StreamComponent user={localUser} />
          </div>
        ) : (
          // 드로잉 화면이면 게스트 띄움
          <div className="h-1/2" id="guestVideo">
            {localUserRole === 'artist' ? (
              subscribers
                .filter((sub) => sub.role === 'guest')
                .map((sub) => (
                  <StreamComponent user={sub} key={sub.connectionId} />
                ))
            ) : (
              <StreamComponent user={localUser} />
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default ConsultDrawingPage;
