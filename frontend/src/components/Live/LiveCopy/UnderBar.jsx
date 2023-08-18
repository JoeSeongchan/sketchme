import React from 'react';
// import PropTypes from 'prop-types';

import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Toolbar, IconButton, Button } from '@mui/material';
// import { AppBar, Toolbar, IconButton } from '@mui/material';
import {
  Mic,
  MicOff,
  VolumeUp,
  VolumeOff,
  Videocam,
  VideocamOff,

  // PictureInPicture,
  ScreenShare,
  StopScreenShare,
  MusicNote,
  MusicOff,
  Fullscreen,
  FullscreenExit,
} from '@mui/icons-material';

import {
  changeMic,
  changeAudio,
  changeVideo,
  changeScreenShare,
  changeBgm,
  changeFullScreen,
} from '../../reducers/VideoSlice';

import { addLiveStatus, resetLiveStatus } from '../../reducers/LiveSlice';

function UnderBar({ joinSession, leaveSession }) {
  const thisLiveStatus = useSelector((state) => state.live.liveStatus);
  const thisSessionId = useSelector((state)=>state.live.mySessionId);

  const isMic = useSelector((state) => state.video.micActive);
  const isAudio = useSelector((state) => state.video.audioActive);
  const isVideo = useSelector((state) => state.video.videoActive);
  const isScreenShare = useSelector((state) => state.video.screenShareActive);
  const isBgm = useSelector((state) => state.video.bgmActive);
  const isFullscreen = useSelector((state) => state.video.fullScreenActive);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  // 라이브 상태변수 핸들러
  const handleLiveStatusButtonClick = () => {
    if (thisLiveStatus === 0) {
      joinSession(thisSessionId);
    } else if (thisLiveStatus === 1 || thisLiveStatus === 2) {
      dispatch(addLiveStatus());
    } else if (thisLiveStatus === 3) {
      leaveSession();
      navigate('/');
      dispatch(resetLiveStatus());
    }
  };

  // 마이크 버튼 핸들러
  const handleMicButtonClick = () => {
    dispatch(changeMic());
  };

  // 오디오 버튼 핸들러
  const handleAudioButtonClick = () => {
    dispatch(changeAudio());
  };

  // 비디오 버튼 핸들러
  const handleVideoButtonClick = () => {
    dispatch(changeVideo());
  };

  // 화면공유 버튼 핸들러
  const handleScreenShareButtonClick = () => {
    dispatch(changeScreenShare());
  };
  // Bgm 버튼 핸들러
  const handleBgmButtonClick = () => {
    dispatch(changeBgm());
  };

  // 풀스크린 버튼 핸들러
  const handleFullScreenButtonClick = () => {
    dispatch(changeFullScreen());
  };

  return (
    <div>
      <Toolbar className="toolbar">
        <div className="buttonsContent">
          <IconButton
            color="inherit"
            className="navButton"
            id="navMicButton"
            onClick={handleMicButtonClick}
          >
            {isMic ? <Mic /> : <MicOff color="secondary" />}
          </IconButton>

          <IconButton
            color="inherit"
            className="navButton"
            id="navSpeakerButton"
            onClick={handleAudioButtonClick}
          >
            {isAudio ? <VolumeUp /> : <VolumeOff color="secondary" />}
          </IconButton>

          <IconButton
            color="inherit"
            className="navButton"
            id="navCamButton"
            onClick={handleVideoButtonClick}
          >
            {isVideo ? <Videocam /> : <VideocamOff color="secondary" />}
          </IconButton>

          <IconButton
            color="inherit"
            className="navButton"
            onClick={handleScreenShareButtonClick}
          >
            {isScreenShare ? (
              <ScreenShare />
            ) : (
              <StopScreenShare color="secondary" />
            )}
            {/* {isScreenShare ? <PictureInPicture /> : <ScreenShare />} */}
          </IconButton>

          <IconButton
            color="inherit"
            className="navButton"
            id="navBgmButton"
            onClick={handleBgmButtonClick}
          >
            {isBgm ? <MusicNote /> : <MusicOff color="secondary" />}
          </IconButton>

          {/* 카메라 전환 구현 안함 */}
          {/* <IconButton
            color="inherit"
            className="navButton"
            onClick={switchCamera}
          >
            <SwitchVideoIcon />
          </IconButton> */}

          <IconButton
            color="inherit"
            className="navButton"
            onClick={handleFullScreenButtonClick}
          >
            {isFullscreen ? <FullscreenExit /> : <Fullscreen />}
          </IconButton>
        </div>
      </Toolbar>
      <Button variant="contained" onClick={handleLiveStatusButtonClick}>
        {thisLiveStatus === 0 ? '상담 시작하기' : null}
        {thisLiveStatus === 1 ? '드로잉 시작하기' : null}
        {thisLiveStatus === 2 ? '드로잉 완성하기' : null}
        {thisLiveStatus === 3 ? '라이브 종료' : null}
      </Button>
    </div>
  );
}

export default UnderBar;
