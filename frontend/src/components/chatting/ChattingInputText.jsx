import React from 'react';

function InputTextChatting() {
  return (
    <div className="py-5">
      <input
        className="w-full border-solid border-2 border-primary py-5 px-3 focus:outline-none focus:ring-primary_3 focus:ring-offset-primary focus:ring-2 focus:ring-offset-2 rounded-full"
        type="text"
        placeholder="메세지를 입력해주세요"
      />
    </div>
  );
}
export default InputTextChatting;
