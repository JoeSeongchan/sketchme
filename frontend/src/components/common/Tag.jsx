import React from 'react';

function BaseTag({ message }) {
  return (
    <div className="flex justify-center my-1">
      <div className="Cute1 py-2 px-1 h-2 text-sm font-medium flex justify-center items-center text-black bg-primary_4 rounded-lg ">
        <div className="mx-1">
          &#35;
          {' '}
          {message}
        </div>
      </div>
    </div>
  );
}

export default BaseTag;
