import React, { useState, useEffect } from 'react';
// import { useLocation } from 'react-router-dom';
import ReservationCheckCard from '../../components/reservation/ReservationCheckCard';
import API from '../../utils/api';
import MyPageSideBar from '../../components/MyPage/MyPageSideBar';

function ReservationCheckPage() {
  // 현재 경로에서 id 추출 (현재 페이지에서는 현재 유저의 모든 예약 목록 가져옴)
  // const location = useLocation();
  // const { reservationId } = location.state;
  const [meetingListAsArtist, setMeetingListAsArtist] = useState([]);
  const [meetingListAsUser, setMeetingListAsUser] = useState([]);

  const fetchData = async () => {
    try {
      const url = '/api/meeting/list';
      const response = await API.get(url);
      console.log(response.data.data);
      console.log(response.data.data.meetingListAsArtist);
      console.log(response.data.data.meetingListAsUser);
      setMeetingListAsArtist(response.data.data.meetingListAsArtist);
      setMeetingListAsUser(response.data.data.meetingListAsUser);
    } catch (error) {
      console.error('예약 내역을 가져오는데 실패했습니다.', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, [setMeetingListAsArtist, setMeetingListAsUser]);

  return (
    <div>
      <MyPageSideBar current="reservation" />
      <div className="p-4 sm:ml-64">
        <div className="flex justify-center items-center text-center mb-8 mt-8">
          <h1 className="text-4xl font-bold underline decoration-wavy decoration-primary bg-primary_4">
            예약 관리
          </h1>
        </div>
        <div>
          {meetingListAsArtist.length > 0 && (
            <div>
              <div className="flex justify-center items-center text-center mb-8 mt-8">
                <h1 className="text-2xl font-bold underline">
                  😊 작가로 참여한 예약 😊
                </h1>
              </div>
              <div className="flex flex-wrap justify-around">
                {meetingListAsArtist.map((reservation, index) => (
                  <div
                    key={reservation.meetingID}
                    className={`w-[40%] mb-4 ${
                      index === meetingListAsArtist.length - 1 ? 'w-[40%] mb-4' : ''
                    }`}
                  >
                    <ReservationCheckCard
                      key={reservation.meetingID}
                      reservation={reservation}
                      className={`w-[50%] mb-4 ${
                        index === (meetingListAsArtist.length - 1) ? <div className="w-[50%] mb-4" /> : ''
                      }`}
                    />
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
        <div>
          {meetingListAsUser.length > 0 ? (
            <div>
              <div className="flex justify-center items-center text-center mb-8 mt-8">
                <h1 className="text-2xl font-bold tracking-wide">
                  😊 고객으로 참여한 예약 😊
                </h1>
              </div>
              <div className="flex flex-wrap justify-around">
                {meetingListAsUser.map((reservation, index) => (
                  <div
                    key={reservation.meetingID}
                    className={`w-[40%] mb-4 ${
                      index === meetingListAsUser.length - 1 ? 'w-[40%] mb-4' : ''
                    }`}
                  >
                    <ReservationCheckCard
                      key={reservation.meetingID}
                      reservation={reservation}
                      className={`w-[50%] mb-4 ${
                        index === (meetingListAsUser.length - 1) ? <div className="w-[50%] mb-4" /> : ''
                      }`}
                    />
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div>
              <h1>
                아직 참여한 예약이 없습니다. 마음에 드는 작가님께 예약 신청을 한
                후 실시간 드로잉을 받아보세요!
              </h1>
              <div className="flex flex-col mt-16 items-center h-[590px]">
                <div className="mt-[10px] text-2xl">
                  <span className="font-bold text-primary">작가</span>
                  로 활동하면
                  <br />
                  나의 그림을
                  <span className="font-bold text-secondary">공유</span>
                  하고
                  <br />
                  <span className="font-bold text-orange">즐거움</span>
                  을 제공할 수 있습니다!
                  <br />
                  작가 프로필을 생성하고
                  <br />
                  작가로 활동을
                  <span className="font-bold text-pink">시작</span>
                  하세요!
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ReservationCheckPage;
