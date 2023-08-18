import React from 'react';

function BaseBtnWhite({ message, onClick }) {
  const click = () => {
    onClick();
  };
  return (
    <div className="flex justify-center ">
      <button type="button" onClick={click} className="py-2 px-4 h-10 rounded-lg flex justify-center items-center text-primary bg-white border-2 border-primary-3 hover:bg-primary_3 focus:ring-primary_3 focus:ring-offset-primary_3 w-max transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2  rounded-full ">
        <div className="mx-3 whitespace-nowrap">{message}</div>
      </button>
    </div>
  );
}

export default BaseBtnWhite;
