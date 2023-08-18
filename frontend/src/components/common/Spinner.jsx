import React, { useState, useEffect } from 'react';
import './Spinner.css'; // 스피너 스타일을 포함하는 CSS 파일

function Spinner() {
  const [imageLoaded, setImageLoaded] = useState(false);
  useEffect(() => {
    const img = new Image();
    img.src = 'img/logosketch.png';
    img.onload = () => {
      setImageLoaded(true);
    };
  }, []);
  return (
    <div className="w-full h-screen flex flex-col item-center justify-center content-center ">
      {imageLoaded && (
        <img
          src="img/logosketch.png"
          alt=""
          className="w-16 h-16 my-5 z-0 animate-slide justify-self-center self-center"
        />
      )}
      <div className="text-xl text-center">
        로딩중입니다.
      </div>
      <div className="spinner-overlay">
        <div className="spinner">
          { }
        </div>
      </div>
    </div>
  );
}

export default Spinner;
