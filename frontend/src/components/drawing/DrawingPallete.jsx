import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { AddCircle, DeleteForever } from '@mui/icons-material';

import { CirclePicker } from 'react-color';
import { IconButton } from '@mui/material';

import {
  addColor,
  deleteColor,
  updateBrushColor,
  updateBrushHex,
} from '../../reducers/BrushSlice';

function DrawingPallete() {
  const dispatch = useDispatch();
  const brushColor = useSelector((state) => state.brush.brushColor);
  const brushHex = useSelector((state) => state.brush.brushHex);
  const savedColors = useSelector((state) => state.brush.savedColors);

  const handleOnClick = (color) => {
    dispatch(updateBrushColor(color.rgb));
    dispatch(updateBrushHex(color.hex));
  };

  const handleClickAdd = () => {
    dispatch(addColor(brushHex));
  };

  const handleClickDelete = () => {
    dispatch(deleteColor(brushHex));
  };

  return (
    <div>
      <div className="relative">
        <img
          src="img/wood.jpg"
          alt="색상 팔레트"
          className="z-0 w-100 h-32 rounded-3xl"
        />
        <div className="flex absolute top-3 left-6 z-10 justify-center item-center">
          <div className="flex flex-col">
            <IconButton onClick={handleClickAdd}>
              <AddCircle />
            </IconButton>
            <IconButton onClick={handleClickDelete}>
              <DeleteForever />
            </IconButton>
          </div>
          <CirclePicker
            color={brushColor}
            onChange={handleOnClick}
            width="336px"
            colors={savedColors}
            circleSize={27}
            circleSpacing={12}
          />
        </div>
      </div>
    </div>
  );
}

export default DrawingPallete;
