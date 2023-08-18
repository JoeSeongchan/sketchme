import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import ChattingListPage from './ChattingListPage';
import ChattingDetailPage from './ChattingDetailPage';

function ChattingBigPage() {
  const [showDetail, setShowDetail] = useState(false);
  const chatRooms = useSelector((state) => state.chatting.chatRooms);

  useEffect(() => {
    if (chatRooms.length > 0) { setShowDetail(true); }
  }, [chatRooms]);

  return (
    <div className="flex flex-row h-[calc(100vh-5rem)] overscroll-hidden justify-between bg-white">
      <div className="flex flex-col h-full w-1/5 border-grey border-r-2 overflow-y-auto">
        <ChattingListPage />
      </div>
      {showDetail && <ChattingDetailPage type="big" />}
    </div>
  );
}

export default ChattingBigPage;
