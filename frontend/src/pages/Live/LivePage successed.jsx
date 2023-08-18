/* eslint-disable object-curly-newline */
/* eslint-disable operator-linebreak */
/* eslint-disable prefer-template */
/* eslint-disable no-param-reassign */
/* eslint-disable no-shadow */
/* eslint-disable comma-dangle */
/* eslint-disable no-console */
/* eslint-disable no-unused-vars */
/* eslint-disable no-undef-init */
import React, { createContext, useEffect, useRef, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { OpenVidu } from 'openvidu-browser';
import axios from 'axios';
import TopBar from '../../components/Live/TopBar';
import UnderBar from '../../components/Live/UnderBar';
import WaitingPage from './WaitingPage';
// eslint-disable-next-line import/no-cycle
import ConsultDrawingPage from './ConsultDrawingPage';
import ResultPage from './ResultPage';
import UserModel from '../../components/Live/UserModel';
import API from '../../utils/api';

import {
  initAll,
  addLiveStatus,
  updateMySessionId,
  updateWaitingActive,
  changeLocalUserAccessAllowed,
} from '../../reducers/LiveSlice';

export const MediaRefContext = createContext();

function LivePage() {
  const dispatch = useDispatch();
  // 차후 우리 서버 연결시 재설정 및 수정될 예정
  // eslint-disable-next-line operator-linebreak
  const APPLICATION_SERVER_URL =
    process.env.NODE_ENV === 'production' ? '' : 'http://25.4.167.82:8000/';

  // 라이브 리덕스 변수 연동시키기
  const liveStatus = useSelector((state) => state.live.liveStatus);
  const mySessionId = useSelector((state) => state.live.mySessionId);
  const myUserName = useSelector((state) => state.live.myUserName);
  const localUserRole = useSelector((state) => state.live.localUserRole);
  const thisMeetingId = useSelector((state) => state.live.meetingId);
  // 비디오 리덕스 변수 연동시키기
  const isMic = useSelector((state) => state.video.micActive);
  const isAudio = useSelector((state) => state.video.audioActive);
  const isVideo = useSelector((state) => state.video.videoActive);

  // 미팅 변수 연동시키기
  // const meetingId = useSelector((state)=>state);
  const meetingId = null;

  const [localUser, setLocalUser] = useState(undefined);
  const [subscribers, setSubscribers] = useState([]);
  const [sharedCanvas, setSharedCanvas] = useState(undefined);
  const [OV, setOV] = useState(null);
  const [session, setSession] = useState(undefined);
  const [canvasSession, setCanvasSession] = useState(undefined);

  let thisOV = null;
  let thisSession = undefined;

  let thisLocalUser = undefined;
  let thisSubscribers = [];

  const mediaRef = useRef(null);

  // // 로컬 유저 객체 저장
  // dispatch(updateLocalUser(new UserModel()));

  // 초기화 함수
  const initLivePage = () => {
    console.log('라이브 페이지 초기화 실행됨');
    if (thisSession) thisSession.disconnect();
    initAll();
    thisOV = null;
    thisSession = undefined;
  };

  // 데이터 변화 신호 보내기
  const sendSignalUserChanged = (data) => {
    const signalOptions = {
      data: JSON.stringify(data),
      type: 'userChanged',
    };
    if (session) session.signal(signalOptions);
    else if (thisSession) thisSession.signal(signalOptions);
  };

  const sendMySignalToSubscribers = () => {
    if (thisSession && thisLocalUser) {
      sendSignalUserChanged({
        audioActive: thisLocalUser.audioActive,
        videoActive: thisLocalUser.videoActive,
        nickname: thisLocalUser.nickname,
        screenShareActive: thisLocalUser.screenShareActive,
      });
    }
  };

  const sendCanvasSignalToSubscribers = () => {
    if (thisSession && thisLocalUser) {
      sendSignalUserChanged({
        audioActive: false,
        videoActive: true,
        nickname: `${thisLocalUser.nickname}_canvas`,
        screenShareActive: false,
      });
    }
  };

  // 오픈비두 객체 생성 및 세션 설정
  const createOV = async () => {
    console.log('createOV 실행');

    const newOV = new OpenVidu();
    const newSession = newOV.initSession();

    // 세션의 스트림 생성시 실행
    newSession.on('streamCreated', (e) => {
      const subscriber = newSession.subscribe(e.stream, undefined);
      subscriber.on('streamPlaying', (e) => {
        subscriber.videos[0].video.parentElement.classList.remove(
          'custom-class'
        );
      });
      const newUser = UserModel();

      newUser.connectionId = e.stream.connection.connectionId;
      const nickname = e.stream.connection.data.split('%')[0];
      newUser.nickname = JSON.parse(nickname).clientData;
      newUser.streamManager = subscriber;
      newUser.type = 'remote';
      // newUser.role = localUser.role === 'artist' ? 'guest' : 'artist';

      thisSubscribers.push(newUser);
      // if (localUserAccessAllowed) {
      sendMySignalToSubscribers(); // 원본 코드에 없음. 임의추가
      // }
    });

    // 세션의 스트림 파괴시 실행
    newSession.on('streamDestroyed', (e) => {
      thisSubscribers = thisSubscribers.filter(
        (subs) => subs.streamManager !== e.stream.streamManager
      );
      e.preventDefault();
    });

    // 세션의 스트림 예외 발생시 실행
    newSession.on('exception', (exception) => {
      console.warn(exception);
    });

    // 세션의 스트림에서 유저정보 변화시 실행
    newSession.on('signal:userChanged', (e) => {
      const remoteUsers = thisSubscribers;
      remoteUsers.forEach((user) => {
        if (user.connectionId === e.from.connectionId) {
          const data = JSON.parse(e.data);
          console.log('EVENTO REMOTE: ', e.data);
          // 수신된 이벤트에 대해 처리
          if (data.micActive !== undefined) {
            user.micActive = data.micActive;
          }
          if (data.audioActive !== undefined) {
            user.audioActive = data.audioActive;
          }
          if (data.videoActive !== undefined) {
            user.videoActive = data.videoActive;
          }
        }
      });
      thisSubscribers = remoteUsers;
    });

    // OV 및 세션 정보 저장
    setOV(newOV);
    setSession(newSession);
    thisOV = newOV;
    thisSession = newSession;
  };

  // 미팅id에 따른 연결 생성 요청
  const getToken = async (targetMeetingId) => {
    console.log('getToken 실행');
    // meeting/{meetingId}/videoconference/get-into-room
    const url = `api/meeting/${targetMeetingId}/videoconference/get-into-room`;
    const response = await API.get(url);

    console.log(response);
    return response.data; // 토큰 반환
  };

  // 연결 실행
  const doConnect = async (token, clientName) => {
    console.log('연결하기 시작');
    console.log(token);
    console.log(thisSession);
    if (thisSession) {
      await thisSession.connect(token, { clientData: clientName });
      // await thisSession.connect(token, { clientData: clientName });
      console.log('연결 완료');
    } else if (session) {
      await session.connect(token, { clientData: clientName });
      console.log('연결 완료');
    } else console.log('세션 없음');
  };

  // 카메라를 스트림에 연결 및 퍼블리셔 지정
  const doConnectCam = async () => {
    console.log('카메라 연결 만들기 실행');
    const publisher = await thisOV.initPublisherAsync(undefined, {
      // 오디오소스 undefined시 기본 마이크, 비디오소스 undefined시 웹캠 디폴트
      audioSource: undefined,
      videoSource: undefined,
      publishAudio: true,
      publishVideo: true,
      resolution: '640x480',
      frameRate: 30,
      insertMode: 'APPEND',
      mirror: true,
    });
    // 이부분 원 코드 다시 볼 것
    if (thisSession.capabilities.publish) {
      publisher.on('accessAllowed', () => {
        thisSession.publish(publisher).then(() => {
          changeLocalUserAccessAllowed();
          sendMySignalToSubscribers();
        });
      });
    }

    // 디바이스 설정 확인 후 저장
    console.log('디바이스 설정 시작');
    await thisOV.getUserMedia({
      audioSource: undefined,
      videoSource: undefined,
    });
    const devices = await thisOV.getDevices();
    const videoDevices = devices.filter(
      (device) => device.kind === 'videoinput'
    );
    // const currentVideoDeviceId = publisher.stream
    //   .getMediaStream()
    //   .getVideoTracks()[0]
    //   .getSettings().deviceId;
    // const currentVideoDevice = videoDevices.find(
    //   (device) => device.deviceId === currentVideoDeviceId
    // );
    const newLocalUser = UserModel();

    newLocalUser.connectionId = thisSession.connection.connectionId;
    newLocalUser.micActive = isMic;
    newLocalUser.audioActive = isAudio;
    newLocalUser.videoActive = isVideo;
    newLocalUser.screenShareActive = false;
    newLocalUser.nickname = myUserName;
    newLocalUser.streamManager = publisher;
    newLocalUser.type = 'local';
    newLocalUser.role = localUserRole;
    // console.log(newLocalUser);
    setLocalUser(newLocalUser);
    thisLocalUser = newLocalUser;
    // dispatch(updateLocalUser(newLocalUser));
    // dispatch(updatePublisher(publisher));
    // dispatch(updateCurrentVideoDevice(currentVideoDevice));

    console.log(publisher);
    // console.log(currentVideoDevice);
  };

  // 마이크 변화 감지 시 신호 전송 실행
  const sendMicSignal = () => {
    if (localUser) {
      const prevLocalUser = localUser;
      prevLocalUser.micActive = isMic;
      setLocalUser(prevLocalUser);
      sendSignalUserChanged({ micActive: isMic });
    }
  };

  // 오디오 변화 감지 시 신호 전송 실행
  const sendAudioSignal = () => {
    if (localUser) {
      const prevLocalUser = localUser;
      prevLocalUser.audioActive = isAudio;
      setLocalUser(prevLocalUser);
      sendSignalUserChanged({ audioActive: isAudio });
    }
  };

  // 화면 변화 감지 시 신호 전송 실행
  const sendVideoSignal = () => {
    if (localUser) {
      const prevLocalUser = localUser;
      prevLocalUser.videoActive = isVideo;
      setLocalUser(prevLocalUser);
      sendSignalUserChanged({ videoActive: isVideo });
    }
  };

  // 작가가 추가로 캔버스를 방송
  const showCanvas = async () => {
    console.log('캔버스 연결 시작');
    const mediaLayer = mediaRef.current;
    const canvasStream = mediaLayer.captureStream();
    console.log(canvasStream);

    const res = await getToken(thisMeetingId);
    console.log(res);
    await doConnect(res.data.token, `${myUserName}_canvas`);
    const publisher = await OV.initPublisherAsync(undefined, {
      // 오디오소스 undefined시 기본 마이크, 비디오소스 undefined시 웹캠 디폴트
      audioSource: false,
      videoSource: canvasStream,
      publishAudio: false,
      publishVideo: true,
      resolution: '640x480',
      frameRate: 30,
      insertMode: 'APPEND',
      mirror: false,
    });
    console.log(publisher);

    // 세션에 퍼블리시 할 수 있으면
    if (session.capabilities.publish) {
      publisher.on('accessAllowed', () => {
        session.publish(publisher).then(() => {
          sendCanvasSignalToSubscribers();
        });
      });
    }

    const newCanvasUser = UserModel();
    console.log(session.connection);
    newCanvasUser.connectionId = session.connection.connectionId;
    newCanvasUser.micActive = false;
    newCanvasUser.audioActive = false;
    newCanvasUser.videoActive = true;
    newCanvasUser.screenShareActive = false;
    newCanvasUser.nickname = `${myUserName}_canvas`;
    newCanvasUser.streamManager = publisher;
    newCanvasUser.type = 'local';
    newCanvasUser.role = 'canvas';
    setSharedCanvas(newCanvasUser);
    console.log(newCanvasUser);
  };

  // 세션 참여
  const joinSession = async (meetingId) => {
    console.log('joinSession 실행');
    dispatch(updateWaitingActive(true));

    // OV 객체 및 세션 객체 생성 후 저장
    await createOV()
      .then(async () => {
        // 미팅 아이디로 연결용 토큰 요청
        const response = await getToken(meetingId);
        return response;
      })
      .then(async (res) => {
        // 수령한 토큰으로 연결 시작
        console.log('토큰 수령 후 연결 시작');
        await doConnect(res.data.token, myUserName);
      })
      .then(async () => {
        console.log('연결 완료 후 캠 연결 시작');
        await doConnectCam(thisOV, thisSession);
      })
      .then(() => {
        console.log('join 완료');
        setLocalUser(thisLocalUser);
        setSubscribers(thisSubscribers);
        dispatch(updateWaitingActive(false));
        dispatch(addLiveStatus());
      })
      .then(async () => {
        // if (localUserRole === 'artist') await showCanvas();
      })
      .catch((error) => {
        console.log(
          'There was an error connecting to the session:',
          error.code,
          error.message
        );
        dispatch(updateWaitingActive(false));
      });
  };

  // 세션 종료 알림 요청 (미구현)
  const endSession = async (targetMeetingId) => {
    console.log('세션 종료 알림 요청 실행');

    const url = `api/meeting/${targetMeetingId}/videoconference`;
    const data = { sessionId: mySessionId }; // 이따 질문할 것
    const response = await API.post(url, data);
  };

  // 세션 떠나기
  const leaveSession = () => {
    console.log('세션떠나기 실행됨');
    // if (session && subscribers) endSession(mySessionId);
    if (thisSession) thisSession.disconnect();

    // 페이지 초기화
    initLivePage();
  };

  // 컴포넌트 마운트될 때와 파괴 될 때 실행되는 useEffect
  useEffect(() => {
    initLivePage();
    return () => {
      initLivePage();
    };
  }, []);

  return (
    <div className="flex flex-col h-screen item-center justify-between">
      <TopBar status={liveStatus} productName="임시 상품명" />
      <div className="flex item-center justify-center">
        {liveStatus === 0 ? <WaitingPage /> : null}
        {liveStatus === 1 || liveStatus === 2 ? (
          <MediaRefContext.Provider value={mediaRef}>
            <ConsultDrawingPage
              localUser={localUser}
              subscribers={subscribers}
              sharedCanvas={sharedCanvas}
              showCanvas={showCanvas}
            />
          </MediaRefContext.Provider>
        ) : null}
        {liveStatus === 3 ? <ResultPage /> : null}
      </div>
      <UnderBar
        joinSession={joinSession}
        leaveSession={leaveSession}
        sendMicSignal={sendMicSignal}
        sendAudioSignal={sendAudioSignal}
        sendVideoSignal={sendVideoSignal}
        // endSession = {endSession}
      />
    </div>
  );
}

export default LivePage;
