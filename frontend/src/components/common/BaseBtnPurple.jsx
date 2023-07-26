import React from 'react';

// 아이콘명, 가로길이, 세로길이, 메시지를 props 로 받는다.
// eslint-disable-next-line react/prop-types
function BaseBtnPurple({ message }) {
  return (
    <div className="flex justify-center ">
      <button type="button" className="py-2 px-4 h-10  rounded-lg  flex justify-center items-center text-white bg-primary hover:bg-primary_3 focus:ring-primary_3 focus:ring-offset-primary_3 transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 ">
        <div className="mx-3">{message}</div>
      </button>
    </div>
  );
}

export default BaseBtnPurple;
