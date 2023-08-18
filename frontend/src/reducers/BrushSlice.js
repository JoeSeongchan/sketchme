/* eslint-disable comma-dangle */
/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';
// import CanvasSlice from './CanvasSlice';

const initialState = {
  brushMode: 'brush', // brush, eraser, paint, spoid
  brushSize: 25,
  brushColor: {
    r: 0,
    g: 0,
    b: 0,
    a: 1,
  },
  brushHex: '#000000',
  brushOpacity: 1,
  savedColors: ['#ffffff'],
  paintTolerance: 25,
  maxSaveCount: 24,
  prevX: undefined,
  prevY: undefined,
};
const BrushSlice = createSlice({
  name: 'BrushSlice',
  initialState,
  reducers: {
    updateBrushMode: (state, action) => {
      state.brushMode = action.payload;
    },
    updateBrushSize: (state, action) => {
      state.brushSize = action.payload;
    },

    updateBrushColor: (state, action) => {
      state.brushColor = action.payload;
    },
    updateBrushHex: (state, action) => {
      state.brushHex = action.payload;
    },
    updateBrushOpacity: (state, action) => {
      state.brushOpacity = action.payload;
    },
    addColor: (state, action) => {
      if (state.savedColors.length === state.maxSaveCount) return;
      const containsColor = (element) => {
        if (element === action.payload) return true;
        return false;
      };
      if (!state.savedColors.some(containsColor)) {
        state.savedColors.push(action.payload);
      }
    },
    deleteColor: (state, action) => {
      if (state.savedColors.length === 0) return; // 작동 테스트

      state.savedColors = state.savedColors.filter(
        (color) => color !== action.payload
      );
    },
    updatePrevX: (state, action) => {
      state.prevX = action.payload;
    },
    updatePrevY: (state, action) => {
      state.prevY = action.payload;
    },
  },
});

export default BrushSlice;
export const {
  updateBrushMode,
  updateBrushSize,
  updateBrushIndex,
  updateBrushColor,
  updateBrushHex,
  updateBrushOpacity,
  addColor,
  deleteColor,
  changeIsEraser,
  updatePrevX,
  updatePrevY,
} = BrushSlice.actions;
