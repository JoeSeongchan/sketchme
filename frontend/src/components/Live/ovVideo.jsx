import React, { useEffect, useRef } from 'react';
import { useSelector } from 'react-redux';

// import './StreamComponent.css';

function OvVideoComponent({ user }) {
  const videoRef = useRef(null);
  // const localUser = useSelector((state) => state.live.localUser);
  const audioActive = useSelector((state) => state.video.audioActive);

  useEffect(() => {
    if (user && user.streamManager && !!videoRef.current) {
      user.streamManager.addVideoElement(videoRef.current);
    }

    if (user && user.streamManager.session && !!videoRef.current) {
      user.streamManager.session.on('signal:userChanged', (e) => {
        const data = JSON.parse(e.data);
        if (data.isScreenShareActive !== undefined) {
          user.streamManager.addVideoElement(videoRef.current);
        }
      });
    }

    // The cleanup function to remove the event listener when the component unmounts
    return () => {
      if (user && user.streamManager.session && !!videoRef.current) {
        user.streamManager.session.off('signal:userChanged');
      }
    };
  }, [user, videoRef]);

  useEffect(() => {
    if (user && !!videoRef.current) {
      user.streamManager.addVideoElement(videoRef.current);
    }
  }, [user, videoRef]);

  return (
    // eslint-disable-next-line jsx-a11y/media-has-caption
    <video
      autoPlay
      id={`video-${user.streamManager.stream.streamId}`}
      ref={videoRef}
      muted={
        user.type === 'local' || user.role === 'canvas'
          ? true
          : !user.micActive || !audioActive
      }
      style={{
        visibility: user.videoActive ? 'visible' : 'hidden',
        // border: user.isSpeaking ? '4px solid green' : '0px solid black',
      }}
      className="w-full max-h-full"
    />
  );
}

export default OvVideoComponent;
