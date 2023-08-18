/* eslint-disable no-shadow */
/* eslint-disable react/no-array-index-key */
import React, { useState, useRef, useEffect } from 'react';
import Swal from 'sweetalert2';
import BaseIconBtnGrey from '../common/BaseIconBtnGrey';
import BaseTag from '../common/BaseTag';
import GalleryTag from './GalleryTag';
import API from '../../utils/api';

function GalleryPaintingCard({ category, memberID, onDeleted }) {
  // 원본 drawings 배열을 변환하여 새로운 배열을 생성합니다.
  const modifiedDrawings = category.drawings.map((drawing) => {
    // imgUrl 추출
    const { imgUrl } = drawing.pictureImgUrl;

    // 새로운 imgUrl 형식으로 저장된 객체 생성
    const newDrawing = {
      id: drawing.id,
      imgUrl: `https://sketchme.ddns.net/api/display?imgURL=${imgUrl}`,
    };
    return newDrawing;
  });
  const {
    categoryID,
    name,
    description,
    price,
    open,
    hashtags,
  } = category;

  const modifiedCategory = {
    categoryID,
    name,
    description,
    price,
    drawings: modifiedDrawings,
    open,
    hashtags,
  };
  const [tags, setTags] = useState(hashtags);

  // 원본 데이터와 수정 중인 데이터를 상태로 관리
  const [originalData, setOriginalData] = useState([]);
  const [currentData, setCurrentData] = useState([]);
  const imgInputRef = useRef(null);
  const [isEditing, setIsEditing] = useState(false);
  const [addedImages, setAddedImages] = useState([]);

  useEffect(() => {
    console.log(modifiedCategory);
    setOriginalData(modifiedCategory);
    setCurrentData(modifiedCategory);
  }, []);

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

  const handleTagChange = (newTags) => {
    setTags(newTags);
  };
  const handleEditClick = () => {
    setIsEditing(!isEditing);
  };

  // input 값이 변경되면 수정 중인 데이터를 업데이트
  const handleChange = (event) => {
    const { name, value } = event.target;
    if (name === 'title' && value.length > 15) {
      return;
    }
    if (name === 'intro' && value.length > 50) {
      return;
    }
    setCurrentData({
      ...currentData,
      [name]: value,
    });
  };

  // 수정한 내용을 취소하고 원본 데이터로 되돌림
  const handleCancel = () => {
    setCurrentData(originalData);
    handleEditClick();
  };

  const editCategory = async () => {
    Swal.fire({
      icon: 'warning',
      title: '카테고리를 수정 하시겠습니까? ',
      showCancelButton: true,
      confirmButtonText: '수정',
      cancelButtonText: '취소',
    }).then(async (res) => {
      if (res.isConfirmed) {
        try {
          const url = '/api/category';
          const body = new FormData();
          const json = JSON.stringify({
            categoryID,
            name: currentData.name,
            description: currentData.description,
            approximatePrice: currentData.price,
            hashtags: tags.map((tag) => tag.hashtagID),
            remainingPictureIDs: currentData.drawings.map((image) => image.id),
          });
          console.log(json);
          body.append(
            'categoryInfo',
            new Blob([json], { type: 'application/json' }),
          );

          for (let i = 0; i < addedImages.length; i += 1) {
            const blobImage = dataURLtoBlob(addedImages[i]);
            const timestamp = Date.now(); // 현재 타임스탬프를 가져옵니다.
            const uniqueFilename = `profile_${timestamp}_${i + 1}.jpg`;
            body.append('uploadFiles', new File([blobImage], uniqueFilename, { type: blobImage.type }));
          }

          const response = await API.put(url, body);
          console.log(response.data);
          return response.data;
        } catch (error) {
          console.log('Error data:', error.response);
          throw error;
        }
      } else {
        return null;
      }
    });
  };

  const handleComplete = () => {
    editCategory();
    setOriginalData(currentData);
    handleEditClick();
  };

  const deleteCategory = async () => {
    Swal.fire({
      icon: 'warning',
      title: '카테고리를 삭제 하시겠습니까? ',
      showCancelButton: true,
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
    }).then(async (res) => {
      if (res.isConfirmed) {
        try {
          const url = '/api/category';
          const body = {
            categoryID,
          };
          const response = await API.delete(url, { data: body });
          console.log(response.data);
          onDeleted();
          return response.data;
        } catch (error) {
          console.log('Error data:', error.response);
          throw error;
        }
      } else {
        return null;
      }
    });
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
      setAddedImages([...addedImages, reader.result]);
    };

    if (file) {
      // 파일이 존재하면 파일을 읽어옵니다.
      reader.readAsDataURL(file);
    }
  };

  const handleDeleteImage = (id) => {
    setCurrentData((prevData) => ({
      ...prevData,
      drawings: prevData.drawings.filter((drawing) => drawing.id !== id),
    }));
  };

  const handleDeleteAddedImage = (index) => {
    setAddedImages((images) => images.filter((_, i) => i !== index));
  };

  return (
    <div className="relative justify-center items-center p-10 mx-auto bg-white shadow-2xl p-1 rounded-lg mx-4 md:mx-auto min-w-1xl max-w-md md:max-w-5xl">
      <div className="flex w-full">
        {isEditing ? (
          <span className="w-2/5 bg-grey"><input name="name" className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm" value={currentData.name} type="text" onChange={handleChange} /></span>
        ) : (
          <h2 className="text-lg font-semibold text-black mt-1">{currentData.name}</h2>
        )}
      </div>
      <div className="flex w-full mt-2">
        {isEditing ? (
          <span className="w-3/5"><input name="description" className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm" value={currentData.description} type="text" onChange={handleChange} /></span>
        ) : (
          <div className="text-xs text-black">{currentData.description}</div>
        )}
      </div>
      <div className="flex mt-1 justify-between">
        <span className="flex ">
          {tags && tags.map((item) => (
            <span key={item.hashtagID} className="mr-2 flex mt-1">
              <span><BaseTag message={item.name} /></span>
            </span>
          ))}
        </span>
        <span className="w-fit flex">
          {isEditing ? (
            <span className="flex w-28">
              <input name="price" className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-2 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm" value={currentData.price} type="number" onChange={handleChange} />
              <span className="min-w-fit mt-2">원 ~</span>
            </span>
          )
            : (
              <div className="text-xs text-black">
                {currentData.price}
                원~
              </div>
            )}
        </span>
      </div>
      <div className="image-list flex">
        <span className="flex overflow-x-auto">
          {currentData.drawings && currentData.drawings.map((image) => (
            <div key={image.id} className="flex-shrink-0 m-2">
              <img src={image.imgUrl} className="w-32 h-32 object-cover rounded" alt={`${image}`} />
              {isEditing
                && (
                  <button type="button" onClick={() => handleDeleteImage(image.id)}>X</button>
                )}
            </div>
          ))}
          {addedImages && addedImages.map((image, index) => (
            <div key={index} className="flex-shrink-0 m-2">
              <img src={image} className="w-32 h-32 object-cover rounded" alt={`${image}`} />
              {isEditing
                && (
                  <button type="button" onClick={() => handleDeleteAddedImage(index)}>X</button>
                )}
            </div>
          ))}
        </span>
        <span className="flex items-center">
          {isEditing
            && (
              <BaseIconBtnGrey icon="pencil" message="그림 추가" onClick={handleImgBtnClick} />
            )}
          <input
            type="file"
            id="imageUpload"
            accept="image/*"
            className="hidden"
            ref={imgInputRef}
            onChange={handleImageUpload}
          />
        </span>
      </div>
      <div className="absolute top-10 right-4 hidden md:block">
        <div className="flex w-fit">
          <span className="mr-1">
            {isEditing && (
              <BaseIconBtnGrey icon="cancel" message="취소하기" onClick={handleCancel} />
            )}
          </span>
          <span className="mr-1">
            {isEditing ? (
              <BaseIconBtnGrey icon="check" message="완료" onClick={handleComplete} />
            ) : (
              sessionStorage.getItem('memberID') === memberID.toString() && (
                <BaseIconBtnGrey onClick={handleEditClick} icon="pencil" message="편집" />
              )
            )}
          </span>

          <span>
            {sessionStorage.getItem('memberID') === memberID.toString()
            && (<BaseIconBtnGrey icon="trash" message="카테고리삭제" onClick={deleteCategory} />
            )}
          </span>
        </div>
      </div>
      {isEditing && <GalleryTag tags={tags} onTagChange={handleTagChange} />}
    </div>
  );
}
export default GalleryPaintingCard;
