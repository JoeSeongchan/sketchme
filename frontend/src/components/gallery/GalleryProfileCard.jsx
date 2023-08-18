import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import Swal from 'sweetalert2';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { setNowChatRoom } from '../../reducers/ChatSlice';
import BaseIconBtnPurple from '../common/BaseIconBtnPurple';
import BaseIconBtnWhite from '../common/BaseIconBtnWhite';
import BaseIconBtnGrey from '../common/BaseIconBtnGrey';
import { ReactComponent as StarIcon } from '../../assets/icons/Star.svg';
import API from '../../utils/api';
import GalleryTag from './GalleryTag';
import BaseTag from '../common/BaseTag';

function GalleryProfileCard({ memberID, artistID }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const location = useLocation();
  const userId = sessionStorage.getItem('memberID');
  const [isEditing, setIsEditing] = useState(false);
  const [isProfileImgEdited, setIsProfileImgEdited] = useState(false);
  const imgInputRef = useRef(null);
  const [tags, setTags] = useState([]);
  const initData = {
    writer: '',
    price: '',
    rating: '',
    profileImg: '',
    like: false,
  };
  // 원본 데이터와 수정 중인 데이터를 상태로 관리
  const [originalData, setOriginalData] = useState(initData);
  const [currentData, setCurrentData] = useState(initData);
  const [like, setLike] = useState(false);
  const handleTagChange = (newTags) => {
    setTags(newTags);
  };
  const handleEditClick = () => {
    setIsEditing(!isEditing);
  };

  // Data URL을 Blob으로 변환
  const dataURLtoBlob = (dataURL) => {
    const arr = dataURL.split(',');
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n > 0) {
      n -= 1;
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], { type: mime });
  };

  const stopArtist = async () => Swal.fire({
    icon: 'warning',
    title: '작가를 그만두시겠습니까?',
    showCancelButton: true,
    confirmButtonText: '그만두기',
    cancelButtonText: '취소',
  }).then(async (res) => {
    if (res.isConfirmed) {
      try {
        const url = '/api/artist/deactivate';
        const response = await API.delete(url);
        console.log('비활성화 성공:', response.data);
        return response.data; // 값을 반환합니다
      } catch (error) {
        console.error('비활성화에 실패했습니다.', error);
        throw error; // 에러를 다시 던져서 Promise를 reject합니다
      }
    } else {
      return null; // 취소되었을 경우에도 값을 반환합니다
    }
  });

  const deactivateArtist = () => {
    Swal.fire({
      icon: 'warning',
      title: '작가 계정을 비활성화 하시겠습니까? ',
      text: '언제든지 돌아올 수 있습니다!',
      showCancelButton: true,
      confirmButtonText: '비활성화',
      cancelButtonText: '취소',
    }).then(async (res) => {
      if (res.isConfirmed) {
        try {
          const url = '/api/artist?isOpen=false';
          const response = await API.put(url);
          console.log(response.data);
          return response.data;
        } catch (error) {
          console.error('비활성화에 실패했습니다.', error);
          throw error; // 에러를 다시 던져서 Promise를 reject합니다
        }
      } else {
        return null; // 취소되었을 경우에도 값을 반환합니다
      }
    });
  };

  const toggleArtistLike = async () => {
    console.log('작가 좋아요 토글');
    try {
      const url = '/api/user/artist';
      const body = {
        artistID,
      };
      const result = await API.put(url, body);
      console.log(result);
      if (result.status === 200) {
        if (!like) {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: '관심 작가로 등록되었습니다.',
            showConfirmButton: false,
            timer: 1500,
          });
        } else {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: '관심 작가에서 해제되었습니다.',
            showConfirmButton: false,
            timer: 1500,
          });
        }
        setLike(!like);
      }
      console.log(like);
    } catch (error) {
      console.error('작가 좋아요 정보 업데이트 실패했습니다', error);
      throw error; // 에러를 다시 던져서 Promise를 reject합니다
    }
  };

  const editProfile = () => {
    Swal.fire({
      icon: 'warning',
      title: '프로필 정보를 수정 하시겠습니까? ',
      showCancelButton: true,
      confirmButtonText: '수정',
      cancelButtonText: '취소',
    }).then(async (res) => {
      if (res.isConfirmed) {
        try {
          const body = new FormData();
          const json = JSON.stringify({
            nickname: currentData.writer,
            hashtags: tags.map((tag) => tag.hashtagID),
          });
          body.append(
            'dto',
            new Blob([json], { type: 'application/json' }),
          );
          console.log(json);
          if (isProfileImgEdited) {
            const blobImage = dataURLtoBlob(currentData.profileImg);
            // 파일 추가
            body.append('uploadFile', new File([blobImage], 'profile.jpg', { type: blobImage.type }));
          }
          // API 호출
          const response = await API.put('/api/artist/info', body, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          setOriginalData(currentData);
          console.log(response.data);
          return response.data;
        } catch (error) {
          // eslint-disable-next-line no-console
          console.error('프로필 수정에 실패했습니다.', error);
          setCurrentData(originalData);
          throw error;
        }
      } else {
        setCurrentData(originalData);
        return null;
      }
    });
    setIsProfileImgEdited(false);
    handleEditClick();
  };

  // input 값이 변경되면 수정 중인 데이터를 업데이트
  const handleChange = (event) => {
    const { name, value } = event.target;
    setCurrentData({
      ...currentData,
      [name]: value,
    });
  };

  // 수정한 내용을 취소하고 원본 데이터로 되돌림
  const handleCancel = () => {
    setCurrentData(originalData);
    setIsProfileImgEdited(false);
    handleEditClick();
  };

  const handleComplete = () => {
    setOriginalData(currentData);
    editProfile();
    handleEditClick();
  };

  // 파일 선택 버튼을 클릭하면 input 태그를 클릭합니다.
  const handleImgBtnClick = () => {
    imgInputRef.current.click();
  };

  // 이미지 업로드 처리 함수
  const handleImageUpload = (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onloadend = () => {
      // 파일을 읽어서 이미지 URL을 상태에 업데이트합니다.
      setCurrentData({
        ...currentData,
        profileImg: reader.result,
      });
      setIsProfileImgEdited(true);
    };

    if (file) {
      // 파일이 존재하면 파일을 읽어옵니다.
      reader.readAsDataURL(file);
    }
  };

  const handleReservationBtnClick = () => {
    if (!sessionStorage.getItem('access_token')) {
      // 'access_token'이 없을 경우 로그인 페이지로 이동
      sessionStorage.setItem('Login_to_go', location.pathname);
      navigate('/login');
      return;
    }
    navigate(`/reservation/${artistID}`);
  };

  const goChatting = async () => {
    if (!sessionStorage.getItem('access_token')) {
      // 'access_token'이 없을 경우 로그인 페이지로 이동
      sessionStorage.setItem('Login_to_go', location.pathname);
      navigate('/login');
      return;
    }
    let data;
    try {
      const url = '/api/chatroom/get';
      const requestData = {
        requestUserID: userId.toString(),
        userIDOfArtist: memberID.toString(),
      };
      const response = await API.post(url, requestData);
      data = response.data.data;
      console.log('채팅방생성했어용~', data);
      const room = data;
      dispatch(setNowChatRoom(room));
      navigate('/chatting');
    } catch (error) {
      console.error('채팅방 생성에 실패했습니다.', error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const url = `/api/artist/info/${artistID}`;
        const response = await API.get(url);
        const { data } = response.data;
        console.log(data);
        setOriginalData((prevData) => ({
          ...prevData,
          writer: data.writer,
          price: data.price !== null ? data.price : 0,
          rating: data.rating !== null ? data.rating : 0,
          profileImg: `https://sketchme.ddns.net/api/display?imgURL=${data.imgUrlResponse.imgUrl}`,
        }));
        setCurrentData((prevData) => ({
          ...prevData,
          writer: data.writer,
          price: data.price !== null ? data.price : 0,
          rating: data.rating !== null ? data.rating : 0,
          profileImg: `https://sketchme.ddns.net/api/display?imgURL=${data.imgUrlResponse.imgUrl}`,
        }));
        setLike(data.like);
        setTags(data.hashtags);
      } catch (error) {
        console.error('작가 프로필을 가져오는 데 실패했습니다.', error);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="bg-white shadow-2xl p-1 rounded-lg mx-4 md:mx-auto min-w-1xl max-w-md md:max-w-5xl mx-auto ">
      <div className="relative justify-center items-center  ">
        <div className="flex w-full items-start px-4 py-4">
          <div className="w-1/5 h-50 mr-4 flex flex-col justify-end overflow-hidden">
            <img
              className="w-50 h-40 object-cover rounded-xs mb-2 shadow"
              src={currentData.profileImg}
              alt="avatar"
            />
            {isEditing && (
              <BaseIconBtnGrey
                icon="pencil"
                message="프로필 수정"
                onClick={handleImgBtnClick}
              />
            )}
            <input
              type="file"
              id="imageUpload"
              accept="image/*"
              className="hidden"
              ref={imgInputRef}
              onChange={handleImageUpload}
            />
          </div>
          <div className="">
            {isEditing ? (
              <span className="w-2/5 bg-grey">
                <input
                  name="writer"
                  className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm"
                  value={currentData.writer}
                  type="text"
                  onChange={handleChange}
                />
              </span>
            ) : (
              <h2 className="flex items-center justify-between text-lg font-semibold text-gray-900 mt-1">
                {currentData.writer}
              </h2>
            )}
            <div className="flex items-center hidden md:block">
              <div className="flex mr-2 text-gray-700 mr-3">
                <StarIcon />
                <span>
                  <div className="text-xs text-grey">{currentData.rating}</div>
                </span>
              </div>
            </div>
            <div className="absolute bottom-10 flex items-center mt-15 hidden md:block">
              <div className="text-start text-gray-700 mr-8 ">
                <div>
                  {currentData.price}
                  원~
                </div>
                <div className="flex flex-wrap text-xs text-black mr-40">
                  {tags && tags.map((item) => (
                    <span key={item.hashtagID} className="mr-2 flex mt-1">
                      <span>
                        <BaseTag message={item.name} />
                      </span>
                    </span>
                  ))}
                </div>
              </div>
            </div>
            <div className="flex absolute  top-4 right-4 hidden md:block">
              <div className="flex w-fit">
                <span className="mr-1">
                  {isEditing && (
                    <BaseIconBtnGrey
                      icon="cancel"
                      message="취소하기"
                      onClick={handleCancel}
                    />
                  )}
                </span>
                <span className="mr-1">
                  {isEditing ? (
                    <BaseIconBtnGrey
                      icon="check"
                      message="완료"
                      onClick={handleComplete}
                    />
                  ) : (
                    memberID.toString() === userId && (
                      <BaseIconBtnGrey
                        onClick={handleEditClick}
                        icon="pencil"
                        message="편집"
                      />
                    )
                  )}

                </span>
              </div>
              { memberID.toString() === userId && (
                <button
                  type="button"
                  className="flex ml-auto text-xs mt-1 hover:bg-gray-100"
                  onClick={stopArtist}
                >
                  작가 그만두기
                </button>
              )}
              { memberID.toString() === userId && (
              <button
                type="button"
                className="flex ml-auto text-xs hover:bg-gray-100"
                onClick={deactivateArtist}
              >
                작가 비활성화
              </button>
              )}
              { memberID.toString() !== userId && (like ? (
                <button
                  type="button"
                  className="flex ml-auto text-xs mt-1 hover:bg-gray-100"
                  onClick={toggleArtistLike}
                >
                  <FavoriteIcon fontSize="large" sx={{ color: '#7532A8' }} />
                </button>
              ) : (
                <button
                  type="button"
                  className="flex ml-auto text-xs mt-1 hover:bg-gray-100"
                  onClick={toggleArtistLike}
                >
                  <FavoriteBorderIcon fontSize="large" sx={{ color: '#7532A8' }} />
                </button>
              ))}
            </div>
            <div className="absolute bottom-4 right-4">
              <div className="mb-1">
                <BaseIconBtnPurple
                  icon="message"
                  message="문의하기"
                  onClick={goChatting}
                />
              </div>
              <div>
                <BaseIconBtnWhite
                  icon="calendar"
                  message="예약하기"
                  onClick={handleReservationBtnClick}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
      {isEditing && <GalleryTag tags={tags} onTagChange={handleTagChange} />}
    </div>
  );
}
export default GalleryProfileCard;
