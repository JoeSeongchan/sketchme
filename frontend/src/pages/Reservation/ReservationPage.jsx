/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import dayjs from 'dayjs';
import ReservationCalendar from '../../components/reservation/ReservationCalendar';
import ResevationTime from '../../components/reservation/ReservationTime';
import ReservationInputForm from '../../components/reservation/ReservationInputForm';
import API from '../../utils/api';

function ReservationPage() {
  const { pathname } = useLocation();
  const [, artistID] = pathname.split('/').slice(1);
  const [categories, setCategories] = useState([]);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [selectedTime, setSelectedTime] = useState(null);

  const fetchData = async () => {
    try {
      const url = `/api/category/list/${artistID}`;
      const response = await API.get(url);
      const modifiedData = [];
      response.data.data.forEach((item) => {
        const extractedData = {
          value: item.categoryID,
          label: item.name,
          hashtags: item.hashtags,
        };
        modifiedData.push(extractedData);
      });

      console.log(modifiedData);

      setCategories(modifiedData);
    } catch (error) {
      console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // handleDateChange 콜백 함수를 통해 선택한 날짜를 업데이트합니다.
  const handleDateChange = (date) => {
    setSelectedDate(date);
  };

  // 시간 선택 시 실행되는 핸들러 함수
  const handleTimeChange = (time) => {
    setSelectedTime(time);
  };
  return (
    <div className="w-4/5 bg-white mt-10 mb-20 shadow-2xl p-1 rounded-lg mx-auto md:mx-auto justify-center">
      <div className="w-full mx-auto mt-20 flex justify-center">
        <div style={{ flex: 1, maxWidth: '50%', margin: '0 5%' }}>
          <ReservationCalendar selectedDate={selectedDate} handleDateChange={handleDateChange} />
        </div>
        <div style={{ flex: 1, maxWidth: '50%', margin: '0 5%' }}>
          <h2 className="font-semibold">
            날짜 :
            {' '}
            {dayjs(selectedDate).locale('ko').format('YYYY-MM-DD')}
          </h2>
          <h2 className="font-semibold">
            선택한 시간 :
            {' '}
            {selectedTime || '시간을 선택하세요'}
          </h2>
          <ResevationTime handleTimeChange={handleTimeChange} />
        </div>
      </div>
      <div className="mt-10 border-t-2 border-lightgrey" />
      <div className="w-full mx-auto mt-20 flex justify-center">
        <div style={{ flex: 1, maxWidth: '70%', margin: '0 10px' }}>
          <ReservationInputForm
            selectedDate={selectedDate}
            selectedTime={selectedTime}
            categories={categories}
            artistID={artistID}
          />
        </div>
      </div>
    </div>
  );
}

export default ReservationPage;
