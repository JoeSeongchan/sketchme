/* eslint-disable no-param-reassign */
import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  selectedButtons: [],
};

const searchSlice = createSlice({
  name: 'search',
  initialState,
  reducers: {
    addSelectedButton: (state, action) => {
      state.selectedButtons.push(action.payload);
    },
    removeSelectedButton: (state, action) => {
      state.selectedButtons = state.selectedButtons.filter(
        (button) => button !== action.payload,
      );
    },
    removeAllSelectedButtons: (state) => {
      state.selectedButtons = [];
    },
  },
});

export const {
  addSelectedButton,
  removeSelectedButton,
  removeAllSelectedButtons,
} = searchSlice.actions;

export default searchSlice.reducer;
