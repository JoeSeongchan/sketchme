/* eslint-disable comma-dangle */
/* eslint-disable indent */
/* eslint-disable operator-linebreak */
/* eslint-disable array-callback-return */
import React, { useEffect, forwardRef } from 'react';
// import React, { useEffect, forwardRef, useContext } from 'react';

import { useSelector } from 'react-redux';
import API from '../../utils/api';

const MediaLayer = forwardRef(({ drawingRefs, showCanvas, brushRef }, ref) => {
  // 캔버스 리덕스 변수 연동시키기
  const thisWidth = useSelector((state) => state.canvas.canvasWidth);
  const thisHeight = useSelector((state) => state.canvas.canvasHeight);
  const thisStyle = useSelector((state) => state.canvas.canvasStyle);
  const layersInfo = useSelector((state) => state.canvas.layersInfo);
  const mediaLayerFPS = useSelector((state) => state.canvas.mediaLayerFPS);
  const sendImgFPS = useSelector((state) => state.canvas.sendImgFPS);
  const activeLayerIndex = useSelector(
    (state) => state.canvas.activeLayerIndex
  );

  // 라이브 리덕스 변수 연동시키기
  const thisMeetingId = useSelector((state) => state.live.meetingId);
  const localUserRole = useSelector((state) => state.live.localUserRole);
  const liveStatus = useSelector((state) => state.live.liveStatus);
  // 레이어 ref지정
  // const mediaRef = useContext(MediaRefContext);
  // const thisLayer = mediaRef.current;
  const thisLayer = ref.current;

  let zipCnt = 0;

  const sendImg = async () => {
    // 드로잉 화면이 아니면 return
    if (liveStatus !== 2 && thisLayer) {
      console.log('드로잉 중 아니므로 이미지 전송 안함');
      return;
    }

    if (thisLayer) {
      thisLayer.toBlob(async (blob) => {
        const formData = new FormData();
        const timestamp = Date.now();
        const imgFile = new File([blob], `${thisMeetingId}_${timestamp}`, {
          type: blob.type,
        });
        formData.append('multipartFiles', imgFile); // 'image'가 서버에서 사용할 필드 이름

        try {
          // 에러나는 부분
          const url = `api/live-picture?meetingId=${thisMeetingId}`;
          // 전송속도 느리면 await 없애고 response 체크 해제
          const response =
            liveStatus === 2
              ? API.post(url, formData, {
                  headers: {
                    // meetingId: thisMeetingId,
                    'Content-Type': 'multipart/form-data',
                  },
                })
              : null;
          console.log('업로드 요청함:', response);
        } catch (error) {
          console.error('업로드 요청 실패:', error.response);
        }
      }, 'image/png'); // 이미지 형식 지정
    }
  };

  // 레이어들을 하나의 이미지로 만드는 함수
  const zipLayers = () => {
    if (thisLayer) {
      const ctx = thisLayer.getContext('2d');
      ctx.reset();

      if (liveStatus === 1) {
        if (
          drawingRefs[0].current !== null &&
          drawingRefs[1].current !== null
        ) {
          ctx.drawImage(drawingRefs[0].current, 0, 0);
          ctx.drawImage(drawingRefs[1].current, 0, 0);
          ctx.drawImage(brushRef.current, 0, 0);
        }
      } else {
        drawingRefs.map((thisRef, index) => {
          if (
            // thisRef.current !== null
            thisRef.current !== null &&
            index !== 1 &&
            layersInfo[index] &&
            layersInfo[index].visible
          ) {
            ctx.drawImage(thisRef.current, 0, 0);
            if (activeLayerIndex === index) {
              ctx.drawImage(brushRef.current, 0, 0);
            }
          }
        });
        zipCnt += 1;
        if (zipCnt % mediaLayerFPS === sendImgFPS) sendImg();
      }
    }
  };

  // 1초에 mediaLayerFPS번 zipLayers 함수를 실행하는 interval 설정
  useEffect(() => {
    const interval =
      localUserRole === 'artist'
        ? setInterval(zipLayers, Math.floor(1000 / mediaLayerFPS))
        : null;
    return () => {
      if (localUserRole === 'artist') clearInterval(interval);
    };
  }, [liveStatus, thisLayer, layersInfo]);

  // // 1초에 mediaLayerFPS번 sendImg 함수를 실행하는 interval 설정
  // useEffect(() => {
  //   const intervalSend =
  //     liveStatus === 2
  //       ? setInterval(sendImg, Math.floor(1000 / sendImgFPS))
  //       : null;

  //   return () => {
  //     clearInterval(intervalSend);
  //   };
  // }, [liveStatus]);

  // 캔버스 방송
  useEffect(() => {
    if (localUserRole !== 'artist') return;
    showCanvas();
  }, []);

  return (
    // <div></div>
    <canvas
      // ref={mediaRef}
      ref={ref}
      width={thisWidth}
      height={thisHeight}
      style={{
        ...thisStyle,
        visibility: 'hidden',
        // pointerEvents: 'none', // 클릭 이벤트를 무시하도록 설정
        // zIndex: layerIndex,
      }}
    >
      캔버스가 지원되지 않는 브라우저입니다. 다른 브라우저를 사용해주세요.
    </canvas>
  );
});

export default MediaLayer;
