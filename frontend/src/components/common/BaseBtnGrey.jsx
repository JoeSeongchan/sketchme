import React from 'react';

function BaseBtnGrey({ message, onClick }) {
  const click = () => {
    onClick();
  };
  return (
    <div className="flex justify-center ">
      <button type="button" onClick={click} className="py-2 px-4 h-10 shadow-2xl rounded-lg flex justify-center items-center text-white bg-lightgrey hover:bg-lightgrey focus:ring-grey focus:ring-offset-grey transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2 ">
        <div className="mx-3 whitespace-nowrap">{message}</div>
      </button>
    </div>
  );
}

export default BaseBtnGrey;
