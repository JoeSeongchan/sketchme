/* eslint-disable comma-dangle */
/* eslint-disable import/no-cycle */
import React, { useState, useRef, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import LayerList from './LayerList';
import DrawingPallete from './DrawingPallete';
import DrawingCanvas from './DrawingCanvas';
import DrawingPicker from './DrawingPicker';

import {
  addBackground,
  addRough,
  selectLayer,
} from '../../reducers/CanvasSlice';

import LiveInfoBox from '../Live/LiveInfoBox';

function DrawingBox({ showCanvas }) {
  const layersInfo = useSelector((state) => state.canvas.layersInfo);
  const maxLayerCount = useSelector((state) => state.canvas.maxLayerCount);

  const liveStatus = useSelector((state) => state.live.liveStatus);
  const dispatch = useDispatch();

  // 레이어 ref 저장용
  const [drawingRefs] = useState(
    Array(maxLayerCount + 2)
      .fill(null)
      .map(() => useRef(null))
  );

  useEffect(() => {
    if (layersInfo.length === 0) {
      dispatch(addBackground());
    }
    if (layersInfo.length === 1) {
      dispatch(addRough());
      dispatch(selectLayer(1));
    }
  }, [layersInfo]);
  return (
    <div className="flex gap-4">
      <div className="flex flex-col w-fit justify-center items-center gap-2">
        {/* 상담화면이면 예약정보 추가로 띄움 */}
        {liveStatus === 1 ? (
          <LiveInfoBox />
        ) : (
          <LayerList drawingRefs={drawingRefs} />
        )}

        <DrawingPicker />
      </div>
      <div className="flex flex-col">
        <DrawingCanvas drawingRefs={drawingRefs} showCanvas={showCanvas} />
        <DrawingPallete />
      </div>
    </div>
  );
}

export default DrawingBox;
