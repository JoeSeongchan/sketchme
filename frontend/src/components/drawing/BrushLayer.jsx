/* eslint-disable array-callback-return */
/* eslint-disable comma-dangle */
/* eslint-disable operator-linebreak */
/* eslint-disable no-unused-expressions */
import React, { useState, forwardRef } from 'react';

import { useSelector, useDispatch } from 'react-redux';

// import { updateRef } from '../../reducers/CanvasSlice';
import {
  updateBrushColor,
  updatePrevX,
  updatePrevY,
} from '../../reducers/BrushSlice';

const BrushLayer = forwardRef(({ drawingRefs }, ref) => {
  // const [id, setId] = useState(layerIndex);
  // const [name, setName] = useState(layerName);
  // const [content, setContent] = useState();
  const [isDrawingMode, setIsDrawingMode] = useState(false);

  // 캔버스 슬라이스 가져오기
  const thisWidth = useSelector((state) => state.canvas.canvasWidth);
  const thisHeight = useSelector((state) => state.canvas.canvasHeight);
  const thisStyle = useSelector((state) => state.canvas.canvasStyle);
  // const maxLayerCount = useSelector((state) => state.canvas.maxLayerCount);
  const layersInfo = useSelector((state) => state.canvas.layersInfo);

  const activeLayerIndex = useSelector(
    (state) => state.canvas.activeLayerIndex
  );

  // 브러시 슬라이스 가져오기
  const brushMode = useSelector((state) => state.brush.brushMode);
  const brushSize = useSelector((state) => state.brush.brushSize);
  const brushColor = useSelector((state) => state.brush.brushColor);
  const brushOpacity = useSelector((state) => state.brush.brushOpacity);
  // const paintTolerance = useSelector((state) => state.brush.paintTolerance);
  const prevX = useSelector((state) => state.brush.prevX);
  const prevY = useSelector((state) => state.brush.prevY);

  // 레이어 ref지정
  const thisLayer = ref.current;

  const dispatch = useDispatch();

  // 레이어 내용 전체삭제
  const clearAll = () => {
    const ctx = thisLayer.getContext('2d');
    ctx.reset();
  };

  const downBrush = (e) => {
    if (brushMode !== 'brush') return;
    console.log('down');

    setIsDrawingMode(true);
    const { offsetX, offsetY } = e.nativeEvent;
    console.log(offsetX, offsetY);
    dispatch(updatePrevX(offsetX));
    dispatch(updatePrevY(offsetY));

    const ctx = thisLayer.getContext('2d');

    brushMode === 'brush'
      ? (ctx.globalCompositeOperation = 'source-over')
      : (ctx.globalCompositeOperation = 'destination-out');
    ctx.fillStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},${brushColor.a})`;
    ctx.globalAlpha = brushOpacity;
    ctx.beginPath();
    ctx.arc(offsetX, offsetY, brushSize / 2, 0, 2 * Math.PI);
    ctx.fill();
    ctx.closePath();
  };

  const moveBrush = (e) => {
    if (brushMode !== 'brush') return;
    const ctx = thisLayer.getContext('2d');
    ctx.globalCompositeOperation = 'source-over';
    // : (ctx.globalCompositeOperation = 'destination-out');
    ctx.lineJoin = 'round';
    ctx.lineCap = 'round';
    ctx.strokeStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},${brushColor.a})`;
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

    ctx.lineTo(offsetX, offsetY);

    // 라인 내 내용물 비우기
    ctx.strokeStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},1)`;
    ctx.globalCompositeOperation = 'destination-out';
    ctx.stroke();

    // 새 라인 채우기
    if (brushMode === 'brush') {
      ctx.strokeStyle = `rgba(${brushColor.r},${brushColor.g},${brushColor.b},${brushColor.a})`;
      ctx.globalCompositeOperation = 'lighter';
      ctx.stroke();
    }

    // 패스 종료
    ctx.closePath();

    // 이전 위치 변수에 현재위치 저장
    dispatch(updatePrevX(offsetX));
    dispatch(updatePrevY(offsetY));
  };

  const upBrush = () => {
    if (brushMode !== 'brush') return;
    console.log('up');

    setIsDrawingMode(false);
    const ctx = thisLayer.getContext('2d');

    // 활성 캔버스에 복사해서 얹기
    console.log(activeLayerIndex);
    console.log(drawingRefs[activeLayerIndex]);
    const ctxDraw = drawingRefs[activeLayerIndex].current.getContext('2d');
    ctxDraw.drawImage(thisLayer, 0, 0);

    // 복사했으니 현재 브러시 레이어 지우기
    clearAll();

    ctx.beginPath();
  };

  const downSpoid = (e) => {
    const { offsetX, offsetY } = e.nativeEvent;
    const ctx = thisLayer.getContext('2d');

    drawingRefs.map((thisRef, index) => {
      if (
        thisRef.current !== null &&
        layersInfo[index] &&
        layersInfo[index].visible
      ) {
        ctx.drawImage(thisRef.current, 0, 0);
      }
    });

    const pixel = ctx.getImageData(offsetX, offsetY, 1, 1).data;
    const selectedColor = {
      r: pixel[0],
      g: pixel[1],
      b: pixel[2],
      a: pixel[3] / 255,
    };

    dispatch(updateBrushColor(selectedColor));
    clearAll();
  };

  const handleMouseDown = (e) => {
    if (brushMode === 'brush') downBrush(e);
    if (brushMode === 'spoid') downSpoid(e);
  };

  const handleMouseMove = (e) => {
    if (!isDrawingMode) return;
    moveBrush(e);
  };

  const handleMouseUp = () => {
    upBrush();
  };

  return (
    <canvas
      ref={ref}
      width={thisWidth}
      height={thisHeight}
      style={{
        ...thisStyle,
        // visibility: isVisible ? 'visible' : 'hidden',
        pointerEvents:
          brushMode === 'brush' || brushMode === 'spoid' ? 'auto' : 'none', // 클릭 이벤트를 무시하도록 설정
        // pointerEvents: layerIndex === activeLayerIndex ? 'auto' : 'none', // 클릭 이벤트를 무시하도록 설정
        zIndex: activeLayerIndex,
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

export default BrushLayer;
