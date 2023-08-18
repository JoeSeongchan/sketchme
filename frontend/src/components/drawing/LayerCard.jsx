/* eslint-disable object-curly-newline */
/* eslint-disable no-unused-expressions */
import { React, useState } from 'react';

import {
  CheckBox,
  CheckBoxOutlineBlank,
  Visibility,
  VisibilityOff,
} from '@mui/icons-material';
import { IconButton, TextField, Card } from '@mui/material';
import { useSelector, useDispatch } from 'react-redux';
import {
  selectLayer,
  changeVisible,
  updateName,
} from '../../reducers/CanvasSlice';
import { updateMessage } from '../../reducers/LiveSlice';

function LayerCard({ index, visible, name }) {
  const dispatch = useDispatch();

  const layersInfo = useSelector((state) => state.canvas.layersInfo);
  const activeIndex = useSelector((state) => state.canvas.activeLayerIndex);

  const [isEditing, setIsEditing] = useState(false);
  const [changedName, setChangedName] = useState(name);

  const handleCardClick = () => {
    if (layersInfo[index].name === '밑그림') return;
    index === activeIndex
      ? dispatch(selectLayer(-1))
      : dispatch(selectLayer(index));
    dispatch(updateMessage(null));
  };
  const handleNameDblClick = () => {
    if (layersInfo[index].name === '밑그림') return;
    if (!isEditing) setIsEditing(true);
    if (isEditing) {
      dispatch(updateName({ index, value: changedName }));
      setIsEditing(false);
    }
    dispatch(updateMessage(null));
  };
  const handleNameChange = (e) => {
    setChangedName(e.target.value);
  };
  const handleNameEnter = (e) => {
    if (!isEditing) return;

    if (e.keyCode === 13) {
      dispatch(updateName({ index, value: changedName }));
      setIsEditing(false);
    }
  };

  const handleClickEye = () => {
    dispatch(changeVisible({ index, value: !visible }));
  };

  return (
    <div>
      <Card className="flex flex-row justify-around m-1 h-6">
        <button
          type="button"
          onClick={handleCardClick}
          className="flex flex-row justify-start px-1 grow align-middle "
        >
          <span
            style={{
              visibility: index === 1 ? 'hidden' : 'visible',
            }}
            className="text-xs"
          >
            {index === activeIndex ? <CheckBox /> : <CheckBoxOutlineBlank />}
          </span>
          {isEditing ? (
            <TextField
              hiddenLabel
              id="outlined-size-small"
              defaultValue={name}
              variant="filled"
              size="small"
              onChange={handleNameChange}
              onKeyDown={handleNameEnter}
              onDoubleClick={handleNameDblClick}
            />
          ) : (
            <div
              onDoubleClick={handleNameDblClick}
              className="flex justify-center items-center text-sm"
            >
              {name}
            </div>
          )}
        </button>

        <IconButton onClick={handleClickEye}>
          {visible ? <Visibility /> : <VisibilityOff />}
        </IconButton>
      </Card>
    </div>
  );
}

export default LayerCard;
