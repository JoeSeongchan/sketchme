import React from 'react';
import { useSelector } from 'react-redux';
// import logo from '@/assets/Logo.png';
// import logo from '../../../public/favi/android-icon-192x192.png';

function TopBar() {
  const liveStatus = useSelector((state) => state.live.liveStatus);
  const productName = useSelector((state) => state.live.productName);
  let message = '';
  if (liveStatus === 0) {
    message = '대기화면';
  } else if (liveStatus === 1) {
    message = '드로잉 상담';
  } else if (liveStatus === 2) {
    message = '드로잉 중';
  } else {
    message = '드로잉 결과';
  }
  const roomStatus = message;
  const roomTitle = productName ? `: ${productName}` : null;

  return (
    <div className="flex h-20 bg-primary_3 text-center align-center">
      <div>
        <img
          src="favi/ms-icon-310x310.png"
          alt="스케치미 로고"
          className="h-12 m-2"
        />
      </div>

      <div className="flex content-center justify-center text-center justify-items-center items-center text-2xl">
        {roomStatus}
        {roomTitle}
      </div>
    </div>
  );
}

export default TopBar;
