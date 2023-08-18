/* eslint-disable object-curly-newline */
/* eslint-disable implicit-arrow-linebreak */
/* eslint-disable comma-dangle */
/* eslint-disable operator-linebreak */
/* eslint-disable no-unused-expressions */
import React, { useState, useEffect, forwardRef } from 'react';

import { useSelector, useDispatch } from 'react-redux';

// import { updateRef } from '../../reducers/CanvasSlice';
import { updatePrevX, updatePrevY } from '../../reducers/BrushSlice';

const DrawingLayer = forwardRef(({ layerIndex, isVisible }, ref) => {
  // const [id, setId] = useState(layerIndex);
  // const [name, setName] = useState(layerName);
  // const [content, setContent] = useState();
  const [isDrawingMode, setIsDrawingMode] = useState(false);

  // 캔버스 슬라이스 가져오기
  const thisWidth = useSelector((state) => state.canvas.canvasWidth);
  const thisHeight = useSelector((state) => state.canvas.canvasHeight);
  const thisStyle = useSelector((state) => state.canvas.canvasStyle);
  const activeLayerIndex = useSelector(
    (state) => state.canvas.activeLayerIndex
  );

  // 브러시 슬라이스 가져오기
  const brushMode = useSelector((state) => state.brush.brushMode);
  const brushSize = useSelector((state) => state.brush.brushSize);
  const brushColor = useSelector((state) => state.brush.brushColor);
  const brushOpacity = useSelector((state) => state.brush.brushOpacity);
  const paintTolerance = useSelector((state) => state.brush.paintTolerance);
  const prevX = useSelector((state) => state.brush.prevX);
  const prevY = useSelector((state) => state.brush.prevY);

  // 레이어 ref지정
  const thisLayer = ref.current;

  const dispatch = useDispatch();

  useEffect(() => {
    if (thisLayer && layerIndex === 0) {
      const ctx = thisLayer.getContext('2d');
      ctx.fillStyle = 'white';
      ctx.fillRect(0, 0, thisWidth, thisHeight);
    }
  }, [thisLayer]);

  const downEraser = (e) => {
    if (layerIndex !== activeLayerIndex) return;
    console.log('down');

    setIsDrawingMode(true);
    const { offsetX, offsetY } = e.nativeEvent;
    console.log(offsetX, offsetY);
    dispatch(updatePrevX(offsetX));
    dispatch(updatePrevY(offsetY));

    const ctx = thisLayer.getContext('2d');

    ctx.globalCompositeOperation = 'destination-out';
    ctx.fillStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},1)`;
    // ctx.fillStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},${brushColor.a})`;
    ctx.globalAlpha = brushOpacity;
    ctx.beginPath();
    ctx.arc(offsetX, offsetY, brushSize / 2, 0, 2 * Math.PI);
    ctx.closePath();
    ctx.fill();
  };

  const moveEraser = (e) => {
    if (layerIndex !== activeLayerIndex) return;
    const ctx = thisLayer.getContext('2d');
    ctx.globalCompositeOperation = 'destination-out';
    ctx.lineJoin = 'round';
    ctx.lineCap = 'round';
    ctx.fillStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},${brushColor.a})`;
    ctx.globalAlpha = brushOpacity;
    ctx.lineWidth = brushSize;

    // 현재 위치 변수에 저장
    const { offsetX, offsetY } = e.nativeEvent;

    const midX = prevX + (offsetX - prevX) / 2;
    const midY = prevY + (offsetY - prevY) / 2;
    // 패스 시작
    ctx.moveTo(prevX, prevY);
    ctx.beginPath();

    // 중간 지점 거쳐서 curve
    ctx.quadraticCurveTo(prevX, prevY, midX, midY);
    ctx.quadraticCurveTo(midX, midY, offsetX, offsetY);

    // 라인 내 내용물 비우기
    ctx.strokeStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},1)`;
    ctx.globalCompositeOperation = 'destination-out';
    ctx.stroke();

    // 패스 종료
    ctx.closePath();

    // 이전 위치 변수에 현재위치 저장
    dispatch(updatePrevX(offsetX));
    dispatch(updatePrevY(offsetY));
  };

  const upEraser = () => {
    if (layerIndex !== activeLayerIndex) return;
    console.log('up');
    setIsDrawingMode(false);
    const ctx = thisLayer.getContext('2d');
    ctx.globalCompositeOperation = 'source-over';

    ctx.beginPath();
  };

  // 채우기 메서드
  const downPaint = (e) => {
    if (layerIndex !== activeLayerIndex) return;
    if (brushMode !== 'paint') return;
    console.log('paint');

    const { offsetX, offsetY } = e.nativeEvent;
    const ctx = thisLayer.getContext('2d');

    // bfs용 스택과 방문셋
    const pixelStack = [];
    const visited = new Set();
    pixelStack.push([offsetX, offsetY]);
    visited.add(`${offsetX},${offsetY}`);

    // 전체 이미지 뽑기
    const imageData = ctx.getImageData(0, 0, thisWidth, thisHeight);

    // 클릭한 곳 색상 뽑기
    const pixel = ctx.getImageData(offsetX, offsetY, 1, 1).data;
    const startColor = {
      r: pixel[0],
      g: pixel[1],
      b: pixel[2],
      a: pixel[3],
    };

    // 델타함수
    const delta = [
      { x: 0, y: 1 },
      { x: 0, y: -1 },
      { x: 1, y: 0 },
      { x: -1, y: 0 },
    ];

    // 같은 색상 판정 함수. 같색이면 true
    const isSameColor = (rgba1, rgba2) =>
      Math.abs(rgba1.r - rgba2.r) <= paintTolerance &&
      Math.abs(rgba1.g - rgba2.g) <= paintTolerance &&
      Math.abs(rgba1.b - rgba2.b) <= paintTolerance &&
      Math.abs(rgba1.a - rgba2.a) <= paintTolerance; // 색상 일치 허용 범위

    // 픽셀 인덱스 계산 함수
    const getPixelIndex = (ax, ay) => (ay * thisWidth + ax) * 4;

    while (pixelStack.length) {
      const poped = pixelStack.pop();
      const index = getPixelIndex(poped[0], poped[1]);
      // console.log('poped', poped[0], poped[1]);
      const [r, g, b, a] = [
        imageData.data[index],
        imageData.data[index + 1],
        imageData.data[index + 2],
        imageData.data[index + 3],
      ];

      if (isSameColor({ r, g, b, a }, startColor)) {
        // console.log('matched');
        imageData.data[index] = brushColor.r;
        imageData.data[index + 1] = brushColor.g;
        imageData.data[index + 2] = brushColor.b;
        imageData.data[index + 3] = brushColor.a * 255;

        for (let i = 0; i < 4; i += 1) {
          const nextX = poped[0] + delta[i].x;
          const nextY = poped[1] + delta[i].y;
          // console.log(nextX, nextY);
          // 경계조건
          if (
            nextX >= 0 &&
            nextX < thisWidth &&
            nextY >= 0 &&
            nextY < thisWidth
          ) {
            // 미방문시 스택 넣기 함수
            if (!visited.has(`${nextX},${nextY}`)) {
              pixelStack.push([nextX, nextY]);
              visited.add(`${nextX},${nextY}`);
            }
          }
        }
      }
    }
    ctx.putImageData(imageData, 0, 0);
  };

  const handleMouseDown = (e) => {
    if (brushMode === 'paint') downPaint(e);
    if (brushMode === 'eraser') downEraser(e);
  };

  const handleMouseMove = (e) => {
    if (!isDrawingMode) return;
    if (brushMode === 'paint') return;
    if (brushMode === 'eraser') moveEraser(e);
  };

  const handleMouseUp = () => {
    if (brushMode === 'paint') return;
    if (brushMode === 'eraser') upEraser();
  };

  // const clearAll = () => {
  //   const ctx = thisLayer.getContext('2d');
  //   ctx.reset();
  // };

  return (
    <canvas
      ref={ref}
      width={thisWidth}
      height={thisHeight}
      style={{
        ...thisStyle,
        visibility: isVisible ? 'visible' : 'hidden',
        pointerEvents:
          // 활성이고 브러쉬모드 스포이드모드가 아닐 때만 클릭 이벤트 듣도록 설정
          layerIndex === activeLayerIndex &&
          brushMode !== 'brush' &&
          brushMode !== 'spoid'
            ? 'auto'
            : 'none',
        // pointerEvents: layerIndex === activeLayerIndex ? 'auto' : 'none', // 클릭 이벤트를 무시하도록 설정
        zIndex: layerIndex,
      }}
      onMouseDown={handleMouseDown}
      onMouseMove={handleMouseMove}
      onMouseUp={handleMouseUp}
      onMouseOut={handleMouseUp}
      onBlur={handleMouseUp}
      onTouchStart={handleMouseDown}
      onTouchMove={handleMouseMove}
      onTouchEnd={handleMouseUp}
      className="absolute top-0 left-0"
    >
      캔버스가 지원되지 않는 브라우저입니다. 다른 브라우저를 사용해주세요.
    </canvas>
  );
});

export default DrawingLayer;
