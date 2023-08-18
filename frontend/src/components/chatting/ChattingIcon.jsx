import React, { useState } from 'react';
import { ReactComponent as ChattingIcon } from '../../assets/icons/Chatting.svg';
import ChattingSmallPage from '../../pages/Chatting/ChattingSmallPage';

function ChatIcon() {
  const [isChatOpen, setIsChatOpen] = useState(false);

  const toggleChat = () => {
    setIsChatOpen(!isChatOpen);
  };

  return (
    <>
      <div className="fixed bottom-0 right-0 m-4 z-20">
        {/* 채팅 아이콘 이미지 등을 넣어주세요 */}
        <button type="button" onClick={toggleChat}>
          <ChattingIcon />
        </button>
      </div>
      {isChatOpen && (
        <div className="fixed bottom-0 right-0 m-4 z-10">
          {/* 채팅창을 구현해주세요 */}
          <div className="w-fit bg-primary">
            <ChattingSmallPage />
          </div>
        </div>
      )}
    </>
  );
}

export default ChatIcon;
