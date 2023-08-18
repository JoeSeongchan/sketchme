/* eslint-disable import/no-cycle */
import React, { createContext, useEffect, useRef } from 'react';

// import { useSelector } from 'react-redux';
import DrawingBox from '../../components/drawing/DrawingBox';

export const MediaRefContext = createContext();

function DrawingTestPage() {
  const mediaRef = useRef(null);

  useEffect(() => {
  }, [mediaRef]);

  return (
    <div>
      드로잉 테스트 페이지 입니다.
      {/* {layersInfo[0]} */}
      <MediaRefContext.Provider value={mediaRef}>
        <DrawingBox />
      </MediaRefContext.Provider>
    </div>
  );
}

export default DrawingTestPage;
