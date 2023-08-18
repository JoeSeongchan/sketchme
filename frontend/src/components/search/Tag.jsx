import React from 'react';

function Tag({ message, onClick }) {
  const handleTagClick = () => {
    onClick(message); // Pass the buttonInfo (string) to the onClick prop
  };

  return (
    <div className="flex items-center font-semibold drop-shadow text-xs px-1 my-2 min-w-60 h-8 me-4 bg-primary_4 rounded-lg text-black">
      &#35;
      {' '}
      {message}
      <button type="button" onClick={handleTagClick} className="text-red-500 ms-1">
        x
      </button>
    </div>
  );
}

export default Tag;
