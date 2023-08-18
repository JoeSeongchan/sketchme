/* eslint-disable comma-dangle */
import React from 'react';

import { IconButton } from '@mui/material';

import {
  Brush,
  CallToAction,
  Colorize,
  FormatColorFill,
  CleaningServicesRounded,
} from '@mui/icons-material';
import { useSelector, useDispatch } from 'react-redux';

import {
  updateBrushMode,
  updateBrushSize,
  // updateBrushColor,
} from '../../reducers/BrushSlice';

function DrawingToolBar({ drawingRefs }) {
  const dispatch = useDispatch();

  // 캔버스 슬라이스 가져오기
  const thisWidth = useSelector((state) => state.canvas.canvasWidth);
  const thisHeight = useSelector((state) => state.canvas.canvasHeight);

  const activeLayerIndex = useSelector(
    (state) => state.canvas.activeLayerIndex
  );

  // 브러시 슬라이스 가져오기
  const brushSize = useSelector((state) => state.brush.brushSize);
  const brushMode = useSelector((state) => state.brush.brushMode);

  const handleChangeSize = (e) => {
    dispatch(updateBrushSize(e.target.value));
  };
  const hangleWheel = (e) => {
    const delta = Math.sign(e.deltaY); // 양수: 업 스크롤, 음수: 다운 스크롤
    dispatch(updateBrushSize(brushSize - delta));
  };
  const handleClickEraser = () => {
    dispatch(updateBrushMode('eraser'));
  };
  const handleClickBrush = () => {
    // dispatch(updateBrushColor('#000000'));
    dispatch(updateBrushMode('brush'));
  };
  const handleClickSpoid = () => {
    dispatch(updateBrushMode('spoid'));
  };
  const handleClickPaint = () => {
    dispatch(updateBrushMode('paint'));
  };
  const handleClickClear = () => {
    const ctx = drawingRefs[activeLayerIndex].current.getContext('2d');
    if (activeLayerIndex === 0) {
      ctx.fillStyle = 'white';
      ctx.fillRect(0, 0, thisWidth, thisHeight);
    }
    if (activeLayerIndex >= 1) {
      ctx.reset();
    }
  };

  return (
    <div>
      <div>
        브러쉬 크기
        <input
          type="number"
          value={brushSize}
          min="1"
          onChange={handleChangeSize}
          onWheel={hangleWheel}
          className="mx-2 w-20 text-center outline outline-1 outline-black"
        />
        <IconButton
          color="inherit"
          className="toolPen"
          id="toolPenButton"
          onClick={handleClickBrush}
        >
          {brushMode === 'brush' ? (
            <Brush style={{ color: '#A77CC7' }} />
          ) : (
            <Brush />
          )}
        </IconButton>
        <IconButton
          color="inherit"
          className="toolEraser"
          id="toolEraserButton"
          onClick={handleClickEraser}
        >
          {brushMode === 'eraser' ? (
            <CallToAction style={{ color: '#A77CC7' }} />
          ) : (
            <CallToAction />
          )}
        </IconButton>
        <IconButton
          color="inherit"
          className="toolSpoid"
          id="toolSpoidButton"
          onClick={handleClickSpoid}
        >
          {brushMode === 'spoid' ? (
            <Colorize style={{ color: '#A77CC7' }} />
          ) : (
            <Colorize />
          )}
        </IconButton>
        <IconButton
          color="inherit"
          className="toolPainter"
          id="toolPainterButton"
          onClick={handleClickPaint}
        >
          {brushMode === 'paint' ? (
            <FormatColorFill style={{ color: '#A77CC7' }} />
          ) : (
            <FormatColorFill />
          )}
        </IconButton>
        <IconButton
          color="inherit"
          className="toolPainter"
          id="toolPainterButton"
          onClick={handleClickClear}
        >
          <CleaningServicesRounded />
        </IconButton>
      </div>
    </div>
  );
}

export default DrawingToolBar;
