import React from 'react';
import PropTypes from 'prop-types';
// import logo from '@/assets/Logo.png';
// import logo from '../../../public/favi/android-icon-192x192.png';

function TopBar({ status, productName }) {
  let message = '';
  if (status === 0) {
    message = '드로잉 상담';
  } else if (status === 1) {
    message = '드로잉 중';
  } else {
    message = '드로잉 결과';
  }
  const roomTitle = `${message}: ${productName}`;

  return (
    <div>
      로고위치
      {roomTitle}
    </div>
  );
}

TopBar.propTypes = {
  status: PropTypes.number.isRequired,
  productName: PropTypes.string.isRequired,
};

export default TopBar;
