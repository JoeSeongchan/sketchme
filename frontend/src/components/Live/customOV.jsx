// /* eslint-disable no-shadow */
// import { OpenVidu } from 'openvidu-browser';
// import UserModel from './UserModel';

// function customOV() {
//   const [localUser, setLocalUser] = useState(undefined);
//   const [subscribers, setSubscribers] = useState([]);
//   const [sharedCanvas, setSharedCanvas] = useState(undefined);
//   const [OV, setOV] = useState();
//   const [session, setSession] = useState(undefined);

//   const makeThisUser = () => {

//     setLocalUser(new UserModel());
//   };

//   const initCustomOV = () => {
//     const newOV = new OpenVidu();
//     const newSession = newOV.initSession();

//     // 신호 보내기
//     const sendSignal = (data, type) => {
//       const signalOptions = {
//         data: JSON.stringify(data),
//         type, // 'userChanged',
//       };
//       if (session) session.signal(signalOptions);
//     };

//     // 세션의 스트림 생성시 실행
//     newSession.on('streamCreated', (e) => {
//       const subscriber = newSession.subscribe(e.stream, undefined);
//       subscriber.on('streamPlaying', (e) => {
//         subscriber.videos[0].video.parentElement.classList.remove(
//           'custom-class'
//         );
//       });
//       const newUser = UserModel();

//       newUser.connectionId = e.stream.connection.connectionId;
//       const nickname = e.stream.connection.data.split('%')[0];
//       newUser.nickname = JSON.parse(nickname).clientData;
//       newUser.streamManager = subscriber;
//       newUser.type = 'remote';
//       // newUser.role = localUser.role === 'artist' ? 'guest' : 'artist';

//       // 구독자 목록에 추가
//       setSubscribers([...subscribers, newUser]);
//       // if (localUserAccessAllowed) {
//       sendMySignalToSubscribers(); // 원본 코드에 없음. 임의추가
//       const data = {
//         audioActive: thisLocalUser.audioActive,
//         videoActive: thisLocalUser.videoActive,
//         nickname: thisLocalUser.nickname,
//         screenShareActive: thisLocalUser.screenShareActive,
//       };
//       sendSignal();

//       // }
//     });
//   };
// }
// export default customOV;
