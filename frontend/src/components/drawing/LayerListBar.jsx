/* eslint-disable comma-dangle */
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { IconButton } from '@mui/material';
import { AddBox, DeleteForever, FileUpload } from '@mui/icons-material';

import { selectLayer, addLayer, deleteLayer } from '../../reducers/CanvasSlice';
import { updateMessage } from '../../reducers/LiveSlice';

function layerListBar({ drawingRefs }) {
  const liveStatus = useSelector((state) => state.live.liveStatus);

  // 캔버스 슬라이스 가져오기
  const thisWidth = useSelector((state) => state.canvas.canvasWidth);
  const thisHeight = useSelector((state) => state.canvas.canvasHeight);
  const layersInfo = useSelector((state) => state.canvas.layersInfo);
  const maxLayerCount = useSelector((state) => state.canvas.maxLayerCount);
  const activeLayerIndex = useSelector(
    (state) => state.canvas.activeLayerIndex
  );

  const dispatch = useDispatch();

  const handleClickAdd = () => {
    if (liveStatus === 2) {
      if (layersInfo.length === maxLayerCount + 2) {
        dispatch(updateMessage('레이어 최대 갯수에 도달했습니다'));
        return;
      }
      dispatch(updateMessage(''));
      dispatch(addLayer('layer'));
      return;
    }
    dispatch(updateMessage('상담화면에서는 레이어를 추가할 수 없습니다'));
  };

  const handleClickDelete = () => {
    if (liveStatus === 2) {
      dispatch(updateMessage(''));
      dispatch(deleteLayer());
    } else {
      dispatch(updateMessage('상담화면에서는 레이어를 삭제할 수 없습니다'));
    }
  };
  const changeLayer = (index1, index2) => {
    const layerRef1 = drawingRefs[index1];
    const layerRef2 = drawingRefs[index2];
    const ctx1 = layerRef1.current.getContext('2d');
    const ctx2 = layerRef2.current.getContext('2d');
    const imageData1 = ctx1.getImageData(0, 0, thisWidth, thisHeight);
    const imageData2 = ctx2.getImageData(0, 0, thisWidth, thisHeight);

    // ctx1.drawImage(layerRef2.current, 0, 0);
    ctx1.reset();
    ctx1.putImageData(imageData2, 0, 0);
    ctx2.reset();
    ctx2.putImageData(imageData1, 0, 0);
  };

  const handleClickUp = () => {
    if (activeLayerIndex < layersInfo.length - 1) {
      dispatch(updateMessage(''));
      changeLayer(activeLayerIndex, activeLayerIndex + 1);
      dispatch(selectLayer(activeLayerIndex + 1));
    } else {
      dispatch(updateMessage('더이상 레이어를 올릴 수 없습니다'));
    }
  };

  const handleClickDown = () => {
    if (activeLayerIndex > 2) {
      dispatch(updateMessage(''));
      changeLayer(activeLayerIndex, activeLayerIndex - 1);
      dispatch(selectLayer(activeLayerIndex - 1));
    } else {
      dispatch(updateMessage('더이상 레이어를 내릴 수 없습니다'));
    }
  };

  return (
    <div className="flex justify-end h-6">
      <IconButton onClick={handleClickAdd}>
        <AddBox />
      </IconButton>
      <IconButton onClick={handleClickDelete}>
        <DeleteForever />
      </IconButton>
      <IconButton onClick={handleClickUp}>
        <FileUpload />
      </IconButton>
      <IconButton onClick={handleClickDown}>
        <FileUpload style={{ transform: 'rotate(180deg)' }} />
      </IconButton>
    </div>
  );
}

export default layerListBar;
