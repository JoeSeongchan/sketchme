import React, { useEffect, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

function ovVideo({ streamManager, mutedSound }) {
  const videoRef = useRef(null);

  const isAudio = useSelector((state) => state.video.audioActive);
  console.log(isAudio);
  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  return <video autoPlay={true} ref={videoRef} muted={!isAudio} />;
}

ovVideo.propTypes = {
  streamManager: PropTypes.any.isRequired,
  mutedSound: PropTypes.bool.isRequired,
};

export default ovVideo;
