import React, { useState } from 'react';

function InputTextChatting({ onEnter }) {
  const [inputText, setInputText] = useState('');

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      const trimmedInput = inputText.trim(); // 입력된 값에서 앞뒤 공백을 제거한 후 가져오기
      if (trimmedInput !== '') {
        onEnter(trimmedInput); // 입력된 텍스트를 인자로 넘겨줌
        setInputText(''); // 입력된 텍스트를 처리한 후, input 값 초기화      }
      }
    }
  };

  const handleChange = (e) => {
    setInputText(e.target.value); // input 값 변경 시, 상태 업데이트
  };

  return (
    <div className="py-5 mx-5">
      <input
        className="w-full border-solid border-2 border-primary_2 py-2 px-5 focus:outline-none focus:ring-primary_3 focus:ring-offset-primary focus:ring-2 focus:ring-offset-2 rounded-full"
        type="text"
        placeholder="메세지를 입력해주세요"
        value={inputText}
        onChange={handleChange}
        onKeyDown={handleKeyDown}
      />
    </div>
  );
}
export default InputTextChatting;
