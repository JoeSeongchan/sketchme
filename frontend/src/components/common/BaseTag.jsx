import React from 'react';

function BaseTag({ message }) {
  return (
    <div className="flex justify-center ">
      <div className="py-3 px-1 h-6 whitespace-nowrap text-xs font-semibold flex justify-center items-center text-black bg-primary_4 rounded-lg ">
        <div className="mx-3">
          &#35;
          {' '}
          {message}
        </div>
      </div>
    </div>
  );
}

export default BaseTag;
