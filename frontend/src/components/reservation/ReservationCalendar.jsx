import React from 'react';
import Calendar from 'react-calendar';
import dayjs from 'dayjs';
import 'react-calendar/dist/Calendar.css'; // css import
import './ReservationCalendarStyle.css';

function ReservationCalendar({ selectedDate, handleDateChange }) {
  return (
    <div>
      <Calendar
        onChange={handleDateChange} // 달력에서 날짜를 선택했을 때 호출되는 콜백 함수를 설정합니다.
        value={selectedDate} // 선택한 날짜를 지정합니다.
        formatDay={(locale, date) => dayjs(date).format('DD')}
        minDetail="month" // 상단 네비게이션에서 '월' 단위만 보이게 설정
        maxDetail="month" // 상단 네비게이션에서 '월' 단위만 보이게 설정
        navigationLabel={null}
        showNeighboringMonth={false} //  이전, 이후 달의 날짜는 보이지 않도록 설정
        className="w-fit text-sm border-b"
        tileDisabled={({ date }) => dayjs(date).isBefore(dayjs(), 'day')} // 오늘 이전의 날짜는 선택 불가능하도록 설정 // 오늘 날짜 이전의 날짜는 선택 불가능하도록 설정
      />
    </div>
  );
}

export default ReservationCalendar;
