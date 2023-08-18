import React, { useEffect, useState } from 'react';

import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Toolbar } from '@mui/material';
import {
  Mic,
  MicOff,
  VolumeUp,
  VolumeOff,
  Videocam,
  VideocamOff,
  // MusicNote,
  // MusicOff,
} from '@mui/icons-material';

import { addLiveStatus, resetLiveStatus } from '../../reducers/LiveSlice';
import { selectLayer } from '../../reducers/CanvasSlice';

import API from '../../utils/api';

function UnderBar({
  joinSession,
  leaveSession,
  sendMicSignal,
  sendAudioSignal,
  sendVideoSignal,
  sendSignalPageChanged,
  session,
}) {
  const thisLiveStatus = useSelector((state) => state.live.liveStatus);
  const localUserRole = useSelector((state) => state.live.localUserRole);
  const thisMeetingId = useSelector((state) => state.live.meetingId);
  // const thisSession = useSelector((state) => state.live.session);
  const isMic = useSelector((state) => state.video.micActive);
  const isAudio = useSelector((state) => state.video.audioActive);
  const isVideo = useSelector((state) => state.video.videoActive);
  // const isBgm = useSelector((state) => state.video.bgmActive);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [isDisabled, setIsDisabled] = useState(true);
  const [btnText, setbtnText] = useState(null);
  useEffect(() => {
    if (localUserRole === 'artist') {
      if (thisLiveStatus === 0) {
        setbtnText('상담 시작하기');
        setIsDisabled(false);
      }
      if (thisLiveStatus === 1) {
        setbtnText('드로잉 시작하기');
        setIsDisabled(false);
      }
      if (thisLiveStatus === 2) {
        setbtnText('드로잉 완성하기');
        setIsDisabled(false);
      }
      if (thisLiveStatus === 3) {
        setbtnText('타임랩스 생성중');
        setIsDisabled(true);
      }
      if (thisLiveStatus === 4) {
        setbtnText('라이브 종료');
        setIsDisabled(false);
      }
    }
    if (localUserRole === 'guest') {
      if (thisLiveStatus === 0) {
        setbtnText('상담 시작하기');
        setIsDisabled(false);
      }
      if (thisLiveStatus === 1) {
        setbtnText('상담 중');
        setIsDisabled(true);
      }
      if (thisLiveStatus === 2) {
        setbtnText('드로잉 중');
        setIsDisabled(true);
      }
      if (thisLiveStatus === 3) {
        setbtnText('타임랩스 생성중');
        setIsDisabled(true);
      }
      if (thisLiveStatus === 4) {
        setbtnText('라이브 종료');
        setIsDisabled(false);
      }
    }
  }, [thisLiveStatus]);

  const drawingEnd = async () => {
    try {
      // 타임랩스 생성 요청
      const makeURL = `api/timelapse/new?meetingId=${thisMeetingId}`;
      const makeResponse = await API.post(makeURL, {
        timeout: 120000, // 타임아웃 120초
      });
      console.log(makeResponse);

      // 드로잉 종료 요청
      // const endURL = `api/meeting/${thisMeetingId}/videoconferenceapi/live-picture`;
      // const endResponse = await API.delete(endURL);
      // console.log(endResponse);

      // 응답 데이터 있기만 하면 넘어가는 걸로 했는데, 성공실패 반환에 따라 다르게 할지 생각해볼 것
      // 종료 응답 받으면 결과화면 전환 및 데이터 전송
      // if (endResponse) {
      dispatch(addLiveStatus());
      sendSignalPageChanged(session);
      leaveSession();
      // }
    } catch (e) {
      console.log('드로잉 종료 에러: ', e);
      dispatch(addLiveStatus());
      sendSignalPageChanged(session);
      leaveSession();

      // if (e.response.request.status === 500) drawingEnd();
    }
  };

  // 버튼 클릭 핸들러
  const handleButtonClick = async () => {
    if (thisLiveStatus === 0) {
      joinSession(thisMeetingId);
      // const url = `api/meeting/${thisMeetingId}/reservation-info`;
      // const response = await API.get(url);

      // console.log(response);
    } else if (thisLiveStatus === 1) {
      if (localUserRole === 'artist') {
        sendSignalPageChanged(session);
        dispatch(addLiveStatus());
        dispatch(selectLayer(0));
      }
    } else if (thisLiveStatus === 2) {
      if (localUserRole === 'artist') {
        sendSignalPageChanged(session);
        dispatch(addLiveStatus());
        drawingEnd();
      }
    } else if (thisLiveStatus === 4) {
      leaveSession();
      navigate('/');
      dispatch(resetLiveStatus());
    }
    // } else if (thisLiveStatus === 3) { //타임랩스 대기중일 때는 넘어가지 않음
  };

  // 마이크 버튼 핸들러
  const handleMicButtonClick = () => {
    sendMicSignal(session);
  };

  // 오디오 버튼 핸들러
  const handleAudioButtonClick = () => {
    sendAudioSignal(session);
  };

  // 비디오 버튼 핸들러
  const handleVideoButtonClick = () => {
    sendVideoSignal(session);
  };

  // // Bgm 버튼 핸들러
  // const handleBgmButtonClick = () => {
  //   dispatch(changeBgm());
  // };

  return (
    <div className="w-full">
      <Toolbar
        variant="dense"
        className="toolbar h-16 flex flex-row justify-center bg-primary_3 align-middle "
      >
        <div className="buttonsContent grow flex justify-center item-center gap-x-4">
          <button
            type="button"
            onClick={handleMicButtonClick}
            className="py-2 px-4 h-10 rounded-lg  flex justify-center items-center hover:bg-shadowbg focus:ring-primary_3 focus:ring-offset-primary_3 text-center font-semibold focus:outline-none focus:ring-2 focus:ring-offset-2 "
          >
            <div className="flex flex-col sm:flex-row justify-center items-center">
              {isMic ? <Mic /> : <MicOff style={{ color: 'red' }} />}
              <div className="hidden sm:inline">
                {isMic ? '마이크 켜짐' : '마이크 꺼짐'}
              </div>
            </div>
          </button>

          <button
            type="button"
            onClick={handleAudioButtonClick}
            className="py-2 px-4 h-10 rounded-lg  flex justify-center items-center hover:bg-shadowbg focus:ring-primary_3 focus:ring-offset-primary_3 text-center font-semibold focus:outline-none focus:ring-2 focus:ring-offset-2 "
          >
            <div className="flex flex-col sm:flex-row justify-center items-center">
              {isAudio ? <VolumeUp /> : <VolumeOff style={{ color: 'red' }} />}
              <div className="hidden sm:inline">
                {isAudio ? '소리 켜짐' : '소리 꺼짐'}
              </div>
            </div>
          </button>

          <button
            type="button"
            onClick={handleVideoButtonClick}
            className="py-2 px-4 h-10 rounded-lg  flex justify-center items-center hover:bg-shadowbg focus:ring-primary_3 focus:ring-offset-primary_3 text-center font-semibold focus:outline-none focus:ring-2 focus:ring-offset-2 "
          >
            <div className="flex flex-col sm:flex-row justify-center items-center">
              {isVideo ? (
                <Videocam />
              ) : (
                <VideocamOff style={{ color: 'red' }} />
              )}
              <div className="hidden sm:inline">
                {isVideo ? '카메라 켜짐' : '카메라 꺼짐'}
              </div>
            </div>
          </button>

          {/* <button
            type="button"
            onClick={handleBgmButtonClick}
            className="py-2 px-4 h-10 rounded-lg  flex justify-center
            items-center hover:bg-shadowbg focus:ring-primary_3 focus:ring-offset-primary_3
            text-center font-semibold focus:outline-none focus:ring-2 focus:ring-offset-2 "
          >
            {isBgm ? (
              <div>
                <MusicNote />
                배경음악 켜짐
              </div>
            ) : (
              <div>
                <MusicOff style={{ color: 'red' }} />
                배경음악 꺼짐
              </div>
            )}
          </button> */}
        </div>

        <button
          type="button"
          onClick={handleButtonClick}
          className="bg-primary w-36 text-white rounded-[4px] px-2 py-2 hover:bg-primary_dark"
          disabled={isDisabled}
        >
          {btnText}
        </button>
      </Toolbar>
    </div>
  );
}

export default UnderBar;
