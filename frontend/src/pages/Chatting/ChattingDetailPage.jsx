/* eslint-disable no-nested-ternary */
/* eslint-disable max-len */
import React, { useState, useEffect, useRef } from 'react';
import { v4 as uuidv4 } from 'uuid';
import { useDispatch, useSelector } from 'react-redux';
import {
  sendMessage, addPagingMessages, setIsSocketConnected, setInitMessages,
} from '../../reducers/ChatSlice';
import ChattingProfileHeader from '../../components/chatting/ChattingProfileHeader';
import ChattingLeftText from '../../components/chatting/ChattingLeftText';
import ChattingRightText from '../../components/chatting/ChattingRightText';
import ChattingInputText from '../../components/chatting/ChattingInputText';
import ChattingBotReservation from '../../components/chatting/ChattingBotReservation';
import ChattingBotInfo from '../../components/chatting/ChattingBotInfo';
import ChattingBotMessage from '../../components/chatting/ChattingBotMessage';
import LoadingIcon from '../../assets/Loading.gif';
import API from '../../utils/api';

function ChattingDetailPage({ type, handleClick }) {
  const dispatch = useDispatch();
  const isSocketConnected = useSelector((state) => state.chatting.isSocketConnected);
  const memberType = useSelector((state) => state.chatting.memberType);
  const chatRoom = useSelector((state) => state.chatting.nowChatRoom);
  const userId = sessionStorage.getItem('memberID');
  const userProfileImg = sessionStorage.getItem('userProfileImg');
  const scrollRef = useRef();
  const [scrollH, setScrollH] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [hasMoreMessages, setHasMoreMessages] = useState(true);
  const messages = useSelector((state) => state.chatting.messages);
  const [pageNo, setPageNo] = useState(0);
  const messageSize = 30;

  const scrollToBottom = () => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = 0;
    }
  };

  // 채팅방 메세지 가져오는 액션
  const getMessages = async (roomID, pageNum) => {
    let data;
    try {
      const url = `/api/chat/data?userID=${userId}&roomID=${roomID}&pageNum=${pageNum}&memberType=${memberType}`;
      const response = await API.get(url);
      data = response.data;
      console.log('채팅 메세지 가져옴', data);
    } catch (error) {
      console.error('채팅 메세지를 가져오는 데 실패했습니다.', error);
    }
    return data;
  };

  const fetchMoreMessages = () => {
    if (isLoading || !hasMoreMessages) {
      return;
    }
    setIsLoading(true);
    setScrollH(scrollRef.current.scrollHeight);
    setTimeout(async () => {
      const data = await getMessages(chatRoom.chatRoomID, pageNo + 1);
      console.log(data);
      dispatch(addPagingMessages(data));
      setIsLoading(false);
      setPageNo(pageNo + 1);
      if (data.length < messageSize) {
        setHasMoreMessages(false);
      }
    }, 1000); // 1초동안 로딩 아이콘 표시
  };

  const handleScroll = (event) => {
    const { scrollTop, scrollHeight, clientHeight } = event.target;
    // 오차 범위 정의
    const errorMargin = 5; // 임의로 설정한 값
    if (Math.abs(-1 * scrollTop - (scrollHeight - clientHeight)) <= errorMargin) {
      fetchMoreMessages();
    }
  };

  const handleAddNewMessage = (content) => {
    const message = {
      senderID: parseInt(userId, 10),
      receiverID: memberType === 'USER' ? chatRoom.userIDOfArtist : chatRoom.userID,
      content,
      chatRoomID: chatRoom.chatRoomID,
      senderType: memberType,
    };
    // sendMessage 액션 디스패치
    dispatch(sendMessage(message));
  };

  useEffect(() => {
    console.log('메세지 변경', messages);
    if (scrollH) {
      // 더미 메시지를 추가한 이후에 해당 스크롤 높이로 스크롤합니다.
      scrollRef.current.scrollTop = (scrollH) * -1 + scrollRef.current.clientHeight / 2;
      setScrollH(null);
    } else {
      scrollToBottom();
    }
  }, [messages]);

  // chatRoom이 변경될 때마다 messages를 초기화하고 스크롤을 맨 아래로 이동
  useEffect(() => {
    dispatch(setIsSocketConnected(false));
    setPageNo(0);
    console.log('현재 채팅방', chatRoom);
    if (chatRoom != null) {
      const fetchData = async () => {
        try {
          const data = await getMessages(chatRoom.chatRoomID, 0, memberType);
          console.log('채팅 메세지 가져옴', data);
          setTimeout(() => {
            if (data && data.length > 0) {
              dispatch(setInitMessages(data));
            }
            scrollToBottom(); // 스크롤을 맨 아래로 이동
            dispatch(setIsSocketConnected(true));
          }, 500);
        } catch (error) {
          console.error('채팅 메세지를 가져오는데 실패했습니다.', error);
        }
      };
      fetchData();
    }
    scrollToBottom(); // 스크롤을 맨 아래로 이동
  }, [chatRoom]);

  const onClickBack = () => {
    handleClick();
  };

  return (
    <div className="w-full h-full flex flex-col justify-between overflow-auto">
      {isSocketConnected && chatRoom ? (
        <ChattingProfileHeader
          type={type}
          profileImg={chatRoom.chatPartnerImageURL ? `https://sketchme.ddns.net/api/display?imgURL=${chatRoom.chatPartnerImageURL}` : 'https://cdn.spotvnews.co.kr/news/photo/202301/580829_806715_1352.jpg'}
          nickname={chatRoom.chatPartnerName}
          onClickBack={onClickBack}
        />
      ) : (
        <div />
      )}
      <div className="flex flex-col-reverse mt-1  h-full overflow-x-hidden overflow-y-scroll" ref={scrollRef} onScroll={handleScroll}>
        <div className="flex flex-col">
          {chatRoom ? (
            isSocketConnected ? (
              [...messages].reverse().map((message) => (
                message.content !== '' && (
                  <React.Fragment key={uuidv4()}>
                    {message.senderType === 'BOT_RESERVATION' && memberType === 'ARTIST' && (
                      <ChattingBotReservation type={type} message={message.content} />
                    )}
                    {message.senderType === 'BOT_RESERVATION' && memberType === 'USER' && (
                      <ChattingBotMessage type={type} message="[BOT] 아티스트에게 예약을 신청했습니다." />
                    )}
                    {message.senderType === 'BOT_LIVE_INFO' && (
                      <ChattingBotInfo type={type} message={message.content} memberType={memberType} />
                    )}
                    {message.senderType === 'BOT_LIVE_STARTED' && (
                      <ChattingBotMessage type={type} message="[BOT] 라이브방이 시작됩니다." />
                    )}
                    {!message.senderType.startsWith('BOT') && (
                      (message.senderID.toString() === userId) ? (
                        <ChattingRightText
                          type={type}
                          profileImg={`https://sketchme.ddns.net/api/display?imgURL=${userProfileImg}`}
                          message={message.content}
                        />
                      ) : (
                        <ChattingLeftText
                          type={type}
                          profileImg={chatRoom.chatPartnerImageURL
                            ? `https://sketchme.ddns.net/api/display?imgURL=${chatRoom.chatPartnerImageURL}`
                            : 'https://cdn.spotvnews.co.kr/news/photo/202301/580829_806715_1352.jpg'}
                          message={message.content}
                        />
                      )
                    )}
                  </React.Fragment>
                )
              ))
            ) : <div className="flex items-center justify-center">Loading... </div>
          ) : (
            <div className="flex items-center justify-center">현재 채팅방이 존재하지 않습니다.</div>
          )}

        </div>
        {isLoading && (
          <div className="flex justify-center">
            <img src={LoadingIcon} alt="로딩" className="w-20 h-20" />
          </div>
        )}
      </div>
      {isSocketConnected && chatRoom
        ? (<ChattingInputText onEnter={handleAddNewMessage} />)
        : <div />}
    </div>
  );
}

export default ChattingDetailPage;
