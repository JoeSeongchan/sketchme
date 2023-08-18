/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/no-noninteractive-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
/* eslint-disable jsx-a11y/label-has-associated-control */
import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
  setNowChatRoom,
  setInitChatRooms,
  setMemberType,
  setNewChatRoom,
} from '../../reducers/ChatSlice';
import ChattingListItem from '../../components/chatting/ChattingListItem';
import API from '../../utils/api';

function ChattingListPage({ type, handleClick }) {
  const [isArtist, setIsArtist] = useState(false);
  const toggleClass = ' transform translate-x-5';
  const dispatch = useDispatch();
  const newChatRoom = useSelector((state) => state.chatting.newChatRoom);
  const chatRooms = useSelector((state) => state.chatting.chatRooms);
  const handleChatRoomClick = (room) => {
    console.log(room);
    if (type != null && type === 'small') {
      handleClick();
    }
    // 현재 채팅방 변경
    dispatch(setNowChatRoom(room));
  };

  // 채팅방 목록을 가져오는 액션
  const getChatRooms = async (memberType) => {
    let data;
    try {
      console.log(memberType);
      const url = `/api/chatroom/list?memberType=${memberType}`;
      const response = await API.get(url);
      data = response.data;
      console.log(data);
    } catch (error) {
      console.error('채팅방 목록을 가져오는 데 실패했습니다.', error);
    }
    return data;
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const memberType = isArtist ? 'ARTIST' : 'USER';
        const data = await getChatRooms(memberType);

        // 기존 chatRooms와 새로운 채팅방 데이터를 합쳐서 업데이트
        dispatch(setInitChatRooms(data.data));
      } catch (error) {
        console.error('채팅방 목록을 가져오는데 실패했습니다.', error);
      }
    };
    fetchData();
  }, [isArtist]);

  useEffect(() => {
    console.log('새로운 채팅방이야');
    if (newChatRoom) {
      const fetchData = async () => {
        try {
          const memberType = isArtist ? 'ARTIST' : 'USER';
          const data = await getChatRooms(memberType);
          dispatch(setInitChatRooms(data.data));
          dispatch(setNewChatRoom(false));
        } catch (error) {
          console.error('채팅방 목록을 가져오는데 실패했습니다.', error);
        }
      };
      fetchData();
    }
  }, [newChatRoom]);

  useEffect(() => {
    const memberType = isArtist ? 'ARTIST' : 'USER';
    dispatch(setMemberType(memberType));
  }, [isArtist]);

  return (
    <div className="w-full h-full overflow-contain flex flex-col">
      {sessionStorage.getItem('artistID') && (
        <div className="mx-5 my-5 text-start font-bold text-primary text-xl flex flex-row flex-5">
          <div
            className="md:w-14 md:h-7 w-18 h-6 flex items-center border-primary_4 border-2 bg-lightgrey mr-4 rounded-full p-1 cursor-pointer"
            onClick={() => {
              setIsArtist(!isArtist);
            }}
          >
            <div
              className={`bg-primary_2 text-primary md:w-6 md:h-6 h-5 w-5 rounded-full shadow-md transform duration-300 ease-in-out${
                isArtist ? null : toggleClass
              }`}
            />
          </div>
          <div className="whitespace-nowrap">{isArtist ? '아티스트' : '고객'}</div>
        </div>
      )}
      {' '}
      {chatRooms.length > 0 ? (
        <div className="flex flex-col h-full overflow-auto ">
          {chatRooms.map((room) => (
            <ChattingListItem
              className="cursor-pointer"
              key={room.chatRoomID}
              item={room}
              onClickRoom={handleChatRoomClick}
            />
          ))}
        </div>
      ) : (
        <div className="text-xl mx-3 mt-5 p-5">채팅방 목록이 없습니다.</div>
      )}
    </div>
  );
}

export default ChattingListPage;
