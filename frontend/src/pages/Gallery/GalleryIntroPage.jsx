import React, { useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import API from '../../utils/api';
import BaseIconBtnGrey from '../../components/common/BaseIconBtnGrey';

/* eslint-disable react/react-in-jsx-scope */
function GalleryIntroPage({ memberID, artistID }) {
  const [isEditing, setIsEditing] = useState(false);
  const [originalData, setOriginalData] = useState('');
  const [currentData, setCurrentData] = useState(originalData);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const url = `/api/artist/desc/${artistID}`;
        const response = await API.get(url);
        const { data } = response;
        console.log(data);
        setOriginalData(data.data);
        setCurrentData(data.data);
      } catch (error) {
        console.error('작가 소개글을 가져오는 데 실패했습니다.', error);
      }
    };
    fetchData();
  }, [artistID]);

  const handleEditClick = () => {
    setIsEditing(!isEditing);
  };

  const handleComplete = () => {
    Swal.fire({
      icon: 'warning',
      title: '소개글을 수정 하시겠습니까? ',
      showCancelButton: true,
      confirmButtonText: '수정',
      cancelButtonText: '취소',
    }).then(async (res) => {
      if (res.isConfirmed) {
        try {
          const body = {
            description: currentData,
          };
          const response = await API.put('/api/artist/desc', body);
          setOriginalData(currentData);
          return response.data;
        } catch (error) {
          // eslint-disable-next-line no-console
          console.error('소개글 수정에 실패했습니다.', error);
          throw error;
        }
      } else {
        setCurrentData(originalData);
        return null;
      }
    });
    handleEditClick();
  };

  const handleChange = (event) => {
    const newValue = event.target.value;
    setCurrentData(newValue);
  };

  // 수정한 내용을 취소하고 원본 데이터로 되돌림
  const handleCancel = () => {
    setCurrentData(originalData);
    handleEditClick();
  };

  return (
    <div className="min-h-96 mb-40">
      <div className="flex justify-end flex justify-start items-center mx-auto pb-20 pt-10 bg-white mx-4 md:mx-auto min-w-1xl max-w-md md:max-w-5xl">
        <div className="flex">
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
              memberID.toString() === sessionStorage.getItem('memberID') && (
                <BaseIconBtnGrey
                  onClick={handleEditClick}
                  icon="pencil"
                  message="편집"
                />
              )
            )}
          </span>
        </div>
      </div>
      <div className="flex justify-start items-center mx-auto pb-40 bg-white mx-4 md:mx-auto min-w-1xl max-w-md md:max-w-5xl">
        {isEditing ? (
          <span className="w-full bg-grey">
            <textarea
              value={currentData}
              type="text"
              onChange={handleChange}
              className="placeholder:italic h-96 placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm"
            />
          </span>
        ) : (
          <div className="flex items-center justify-between text-lg font-semibold text-gray-900 mt-1 multiline">
            <div className="detail_content">
              {currentData.split('\n').map((line) => (
                <span>
                  {line}
                  <br />
                </span>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default GalleryIntroPage;
