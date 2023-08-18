import React, { useState } from 'react';

function ResevationTime({ handleTimeChange }) {
  const [selectedTime, setSelectedTime] = useState(null); // 선택된 시간 상태를 관리

  // 함수를 사용하여 오전 9시부터 오후 10시까지의 시간 목록 생성
  const generateTimeList = () => {
    const times = [];
    for (let hour = 9; hour <= 22; hour += 1) {
      for (let minute = 0; minute < 60; minute += 30) {
        const formattedTime = `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
        times.push(formattedTime);
      }
    }
    return times;
  };

  const handleButtonClick = (time) => {
    setSelectedTime(time); // 버튼이 클릭되면 선택된 시간을 업데이트
    handleTimeChange(time); // 선택된 시간을 상위 컴포넌트로 전달
  };

  return (
    <div className="w-100">
      <div className="flex flex-wrap">
        {generateTimeList().map((time) => (
          <button
            className={`w-1/5 m-1 rounded-sm ${selectedTime === time ? 'bg-primary text-white' : 'bg-lightgrey text-black'}`}
            type="button"
            key={time}
            onClick={() => handleButtonClick(time)}
          >
            {time}
          </button>
        ))}
      </div>
    </div>
  );
}

export default ResevationTime;
