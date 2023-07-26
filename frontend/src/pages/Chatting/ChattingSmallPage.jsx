import React from 'react';
// import ChattingListPage from './ChattingListPage';
import ChattingDetailPage from './ChattingDetailPage';

function ChattingSmallPage() {
  return (
    <div className="absolute fixed bottom-4 right-4 w-1/5 h-1/3 min-w-[450px] min-h-[400px] min-h-[500px] overscroll-hidden p-4  mx-auto bg-white shadow-xl rounded-xl">
      <ChattingDetailPage />
    </div>
  );
}

export default ChattingSmallPage;
