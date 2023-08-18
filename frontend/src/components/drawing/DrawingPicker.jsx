import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { SketchPicker, SliderPicker } from 'react-color';

import { updateBrushColor, updateBrushHex } from '../../reducers/BrushSlice';
import { updateMessage } from '../../reducers/LiveSlice';

function DrawingPallete() {
  const dispatch = useDispatch();
  const brushColor = useSelector((state) => state.brush.brushColor);

  const handleOnClick = (color) => {
    dispatch(updateBrushColor(color.rgb));
    dispatch(updateBrushHex(color.hex));
    dispatch(updateMessage(null));
  };

  return (
    <div className="flex flex-col justify-around min-w-[220px]">
      <SketchPicker color={brushColor} onChange={handleOnClick} />
      <SliderPicker
        color={brushColor}
        onChange={handleOnClick}
        className="w-[220px] h-10 min-h-fit"
      />
    </div>
  );
}

export default DrawingPallete;
