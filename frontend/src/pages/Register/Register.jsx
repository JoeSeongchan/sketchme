import React from 'react';
import { useNavigate } from 'react-router-dom';
import RegisterImg from '../../assets/RegisterImg.png';
import api from '../../utils/api';

function Register() {
  const navigate = useNavigate();

  const handleStartClick = async () => {
    try {
      await api.post('/api/artist/regist', {
      });
      navigate('/mypage');
    } catch (error) {
      // eslint-disable-next-line no-console
      console.error('작가 등록 실패', error);

      if (error.response && error.response.data && error.response.data.message === '이미 작가로 등록되었습니다.') {
        // 이미 작가로 등록된 경우 경고창 띄우기
        // eslint-disable-next-line no-alert
        alert(error.response.data.message);
        navigate('/');
      }
    }
  };

  return (
    <div className="flex flex-col mt-16 items-center h-[590px]">
      <div className="mt-[10px] text-2xl">
        <span className="font-bold text-primary">
          작가
        </span>
        로 활동하면
        <br />
        나의 그림을
        <span className="font-bold text-secondary">
          공유
        </span>
        하고
        <br />
        <span className="font-bold text-orange">
          즐거움
        </span>
        을 제공할 수 있습니다!
        <br />
        작가 프로필을 생성하고
        <br />
        작가로 활동을
        <span className="font-bold text-pink">
          시작
        </span>
        하세요!
      </div>
      <img src={RegisterImg} alt="" className="mx-auto block w-[400px] my-5" />
      <div className="flex justify-center">
        <button
          type="button"
          className="py-2 px-4 h-10  rounded-lg  flex justify-center items-center text-white bg-beige hover:opacity-70 focus:ring-beige focus:ring-offset-beige transition ease-in duration-200 text-center font-semibold shadow-md focus:outline-none focus:ring-2 focus:ring-offset-2"
          onClick={handleStartClick} // Call handleStartClick when the button is clicked
        >
          <div className="mx-3">시작하기</div>
        </button>
      </div>
    </div>
  );
}

export default Register;
