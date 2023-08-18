import React from 'react';
import { ReactComponent as MessageIcon } from '../../assets/icons/Message.svg';
import { ReactComponent as CalendarIcon } from '../../assets/icons/Calendar.svg';
import { ReactComponent as CancelIcon } from '../../assets/icons/Cancel.svg';
import { ReactComponent as CheckIcon } from '../../assets/icons/Check.svg';
import { ReactComponent as PencilIcon } from '../../assets/icons/Pencil.svg';
import { ReactComponent as TrashIcon } from '../../assets/icons/Trash.svg';

const iconComponents = {
  message: MessageIcon,
  calendar: CalendarIcon,
  cancel: CancelIcon,
  check: CheckIcon,
  pencil: PencilIcon,
  trash: TrashIcon,
};

// 아이콘명, 가로길이, 세로길이, 메시지를 props 로 받는다.
function BaseIconBtnGrey({ icon, message, onClick }) {
  const ActiveIconComponent = iconComponents[icon];
  const click = () => {
    onClick();
  };
  return (
    <div className="flex justify-center ">
      <button type="button" onClick={click} className="py-2 px-2 h-5 rounded-lg flex  justify-center items-center text-black border-2 border-grey hover:bg-primary_3 ">
        <ActiveIconComponent fill="white" width="8" height="8" />
        <div className="text-xs ml-1 min-w-fit w-fit whitespace-nowrap overflow-hidden overflow-ellipsis">{message}</div>
      </button>
    </div>
  );
}

export default BaseIconBtnGrey;
