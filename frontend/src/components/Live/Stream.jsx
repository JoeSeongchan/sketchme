import React from 'react';

import { MicOff, VideocamOff, VolumeOff } from '@mui/icons-material';
import OvVideoComponent from './ovVideo';

function Stream({ user }) {
  return (
    <div className="w-full h-fit">
      {user !== undefined && user.streamManager !== undefined ? (
        <div className="w-full h-fit" id="stream">
          <div
            style={{
              border:
                user && user.videoActive
                  ? '0px solid black'
                  : '1px solid black',
            }}
          >
            <OvVideoComponent user={user} />
          </div>

          <div className="flex flex-row justify-between">
            <div className="nickname">
              {user.role !== 'canvas' ? user.nickname : null}
              {user.role === 'artist' ? ' 작가' : null}
              {user.role === 'guest' ? ' 고객' : null}
            </div>
            {user && user.role !== 'canvas' ? (
              <div id="statusIcons" className="flex flex-row justify-end">
                <MicOff
                  color="secondary"
                  style={{ visibility: user.micActive ? 'hidden' : 'visible' }}
                />
                <VolumeOff
                  color="secondary"
                  style={{
                    visibility: user.audioActive ? 'hidden' : 'visible',
                  }}
                />
                <VideocamOff
                  color="secondary"
                  style={{
                    visibility: user.videoActive ? 'hidden' : 'visible',
                  }}
                />
              </div>
            ) : null}
          </div>
        </div>
      ) : (
        <div>영상 로딩 중 입니다</div>
      )}
    </div>
  );
}

export default Stream;
