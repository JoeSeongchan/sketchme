import React from 'react';
import { ReactComponent as MessageIcon } from '../../assets/icons/Message.svg';
import { ReactComponent as CalendarIcon } from '../../assets/icons/Calendar.svg';
import { ReactComponent as CancelIcon } from '../../assets/icons/Cancel.svg';
import { ReactComponent as CheckIcon } from '../../assets/icons/Check.svg';
import { ReactComponent as PencilIcon } from '../../assets/icons/Pencil.svg';

const iconComponents = {
  message: MessageIcon,
  calendar: CalendarIcon,
  cancel: CancelIcon,
  check: CheckIcon,
  pencil: PencilIcon,
};

// 아이콘명, 가로길이, 세로길이, 메시지를 props 로 받는다.
function BaseIconBtnPurple({ icon, message, onClick }) {
  const ActiveIconComponent = iconComponents[icon];
  return (
    <div className="flex justify-center ">
      <button type="button" onClick={onClick} className="py-2 px-2 h-8  rounded-lg flex justify-center items-center text-white bg-primary hover:bg-primary_3 focus:ring-primary_3 focus:ring-offset-primary_3 w-max transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2">
        <ActiveIconComponent fill="#7532A8" width="16" height="16" />
        <div className="mx-3">{message}</div>
      </button>
    </div>
  );
}

export default BaseIconBtnPurple;
