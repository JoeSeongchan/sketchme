import React from 'react';
import ChattingListPage from './ChattingListPage';
import ChattingDetailPage from './ChattingDetailPage';

function ChattingBigPage() {
  return (
    <div className="flex flex-row h-screen overscroll-hidden justify-between bg-white">
      <div className="flex flex-col w-2/5 border-grey border-r-2 overflow-y-auto">
        <ChattingListPage />
      </div>
      <ChattingDetailPage />
    </div>
  );
}

export default ChattingBigPage;
