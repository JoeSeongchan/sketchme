import React, { useState } from 'react';
import ChattingListPage from './ChattingListPage';
import ChattingDetailPage from './ChattingDetailPage';

function ChattingSmallPage() {
  const [showDetail, setShowDetail] = useState(false);
  const handleClick = () => {
    setShowDetail(!showDetail);
  };
  return (
    <div className="absolute fixed bottom-4 right-4 w-1/5 h-1/3 min-w-[450px] min-h-[500px] overscroll-hidden mx-auto bg-white shadow-xl rounded-xl">
      {showDetail ? (
        <ChattingDetailPage type="small" handleClick={handleClick} />
      ) : (
        <ChattingListPage type="small" handleClick={handleClick} />
      )}
    </div>
  );
}

export default ChattingSmallPage;
