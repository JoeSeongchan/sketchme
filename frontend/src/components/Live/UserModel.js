function UserModel() {
  return {
    connectionId: '',
    micActive: true,
    audioActive: true,
    videoActive: true,
    isSpeaking: false,
    nickname: '',
    streamManager: null,
    type: null, // 'remote' | 'local'
    role: null, // 'artist' | 'guest' | 'canvas'
  };
}

export default UserModel;
