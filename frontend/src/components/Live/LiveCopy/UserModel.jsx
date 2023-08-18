import React, { useState } from 'react';

function UserModel() {
  const [connectionId, setConnectionId] = useState('');
  const [audioActive, setAudioActive] = useState(false);
  const [videoActive, setVideoActive] = useState(false);
  const [screenShareActive, setScreenShareActive] = useState(false);
  const [nickname, setNickname] = useState('');
  const [streamManager, setStreamManager] = useState(null);
  const [type, setType] = useState('local');

  const isAudioActive = () => {
    return audioActive;
  };

  const isVideoActive = () => {
    return videoActive;
  };

  const isScreenShareActive = () => {
    return screenShareActive;
  };

  const getConnectionId = () => {
    return connectionId;
  };

  const getNickname = () => {
    return nickname;
  };

  const getStreamManager = () => {
    return streamManager;
  };

  const isLocal = () => {
    return type === 'local';
  };

  const isRemote = () => {
    return !isLocal();
  };

  return {
    connectionId,
    audioActive,
    videoActive,
    screenShareActive,
    nickname,
    streamManager,
    type,
    isAudioActive,
    isVideoActive,
    isScreenShareActive,
    getConnectionId,
    getNickname,
    getStreamManager,
    isLocal,
    isRemote,
    setAudioActive,
    setVideoActive,
    setScreenShareActive,
    setStreamManager,
    setConnectionId,
    setNickname,
    setType,
  };
}

export default UserModel;
