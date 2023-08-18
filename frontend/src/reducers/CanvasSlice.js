/* eslint-disable comma-dangle */
/* eslint-disable no-alert */
/* eslint-disable no-unused-vars */
/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
// import useLayerModel from '../components/drawing/useLayerModel';
import LayerModel from '../components/drawing/LayerModel';
// import { React } from 'react';

const initialState = {
  layersInfo: [],
  activeLayerIndex: -1, // 현재 활성화된 레이어 인덱스. -1이면 활성화된 레이어 없음

  lastCreatedLayer: 0,
  canvasWidth: 400,
  canvasHeight: 400,
  canvasStyle: { border: '1px solid black' },
  maxLayerCount: 4,

  mediaLayerFPS: 30,
  sendImgFPS: 1, // 초당 캡쳐 갯수

  workLogBucket: [],
};

const CanvasSlice = createSlice({
  name: 'DrawingSlice',
  initialState,
  reducers: {
    initAll: (state) => {
      state = initialState;
    },
    addBackground: (state) => {
      const newLayer = LayerModel('배경', 0, 'background');
      state.layersInfo.splice(0, 0, newLayer);
    },
    addRough: (state) => {
      const newLayer = LayerModel('밑그림', 1, 'rough');
      state.layersInfo.splice(1, 0, newLayer);
    },
    addLayer: (state, action) => {
      if (state.layersInfo.length === state.maxLayerCount + 2) {
        window.alert('레이어 최대 갯수에 도달했습니다');
        return;
      }

      const newLayer = LayerModel(
        `레이어${state.lastCreatedLayer + 1}`,
        state.lastCreatedLayer + 2,
        action.payload
      );
      state.lastCreatedLayer += 1;
      state.layersInfo.splice(2, 0, newLayer);
    },
    deleteLayer: (state) => {
      if (state.activeLayerIndex === -1) return;
      if (state.activeLayerIndex === 0) return;
      if (state.activeLayerIndex === 1) return;

      if (!state.layersInfo[state.activeLayerIndex]) return;
      const targetId = state.layersInfo[state.activeLayerIndex].id;
      state.layersInfo = state.layersInfo.filter(
        (layer) => layer.id !== targetId
      );
    },
    selectLayer: (state, action) => {
      state.activeLayerIndex = action.payload;
    },
    updateRef: (state, action) => {
      const { index, newRef } = action.payload;
      state.layersInfo[index].ref = newRef;
    },
    changeVisible: (state, action) => {
      const { index, value } = action.payload;
      state.layersInfo[index].visible = value;
    },
    updateName: (state, action) => {
      const { index, value } = action.payload;
      state.layersInfo[index].name = value;
    },
    changeNeedDeleteRef: (state, action) => {
      state.needDeleteRef = action.payload;
    },
  },
});

export default CanvasSlice;
export const {
  initAll,
  addBackground,
  addRough,
  addLayer,
  deleteLayer,
  selectLayer,
  updateRef,
  changeVisible,
  updateName,
  changeNeedDeleteRef,
} = CanvasSlice.actions;
