/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  micActive: true,
  audioActive: true,
  videoActive: true,
  screenShareActive: false,
  bgmActive: true,

  // fullScreenActive: false,
  // 스위치 카메라 생략. 나중에 필요하면 구현
};

const videoSlice = createSlice({
  name: 'videoSlice',
  initialState,
  reducers: {
    changeMic: (state, action) => {
      state.micActive = action.payload;
    },
    changeAudio: (state, action) => {
      state.audioActive = action.payload;
    },
    changeVideo: (state, action) => {
      state.videoActive = action.payload;
    },
    changeScreenShare: (state) => {
      state.screenShareActive = !state.screenShareActive;
    },
    changeBgm: (state, action) => {
      state.bgmActive = action.payload;
    },
    // changeFullScreen: (state) => {
    //   state.fullScreenActive = !state.fullScreenActive;
    // },
  },
});

export default videoSlice;
export const {
  changeMic,
  changeAudio,
  changeVideo,
  changeScreenShare,
  changeBgm,
  // changeFullScreen,
} = videoSlice.actions;
