import React from 'react';
import ChattingProfileHeader from '../../components/chatting/ChattingProfileHeader';
import ChattingLeftText from '../../components/chatting/ChattingLeftText';
import ChattingRightText from '../../components/chatting/ChattingRightText';
import ChattingInputText from '../../components/chatting/ChattingInputText';

function ChattingDetailPage() {
  return (
    <div className="w-full max-h-full px-5 flex flex-col justify-between overflow-contain">
      <ChattingProfileHeader profileImg="https://source.unsplash.com/otT2199XwI8/600x600" nickname="가재주인" />
      <div className="flex flex-col mt-1 overflow-y-scroll">
        <div className="text-grey mt-5">오전 10:47</div>
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="우왕 메세지 화면 만드는 중이다~!!!" />
        <ChattingRightText profileImg="https://source.unsplash.com/otT2199XwI8/600x600" message="안녕 나는 너에게 그림을 의뢰하고 싶어~!!!!" />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="우왕 잘 가지나용?ㅎㅎ " />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="우왕 신기방기~~~~~~~!" />
        <ChattingRightText profileImg="https://source.unsplash.com/otT2199XwI8/600x600" message="우와 이거 구현 어떻게 하지 ~~~~~! 꺅 신나는데?? 우와아아아아아아아아아아아앙 긴 글씨는 어케 들어가? 우와아아아아아아아아아아아앙 긴 글씨는 어케 들어가?우와아아아아아아아아아아아앙 긴 글씨는 어케 들어가?" />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="그만 보내세요..." />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="시끄러죽겠네ㅡㅡ" />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="우왕 메세지 화면 만드는 중이다~!!!" />
        <ChattingRightText profileImg="https://source.unsplash.com/otT2199XwI8/600x600" message="안녕 나는 너에게 그림을 의뢰하고 싶어~!!!!" />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="우왕 잘 가지나용?ㅎㅎ " />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="우왕 신기방기~~~~~~~!" />
        <ChattingRightText profileImg="https://source.unsplash.com/otT2199XwI8/600x600" message="우와 이거 구현 어떻게 하지 ~~~~~! 꺅 신나는데?? 우와아아아아아아아아아아아앙 긴 글씨는 어케 들어가? 우와아아아아아아아아아아아앙 긴 글씨는 어케 들어가?우와아아아아아아아아아아아앙 긴 글씨는 어케 들어가?" />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="그만 보내세요..." />
        <ChattingLeftText profileImg="https://source.unsplash.com/vpOeXr5wmR4/600x600" message="시끄러죽겠네ㅡㅡ" />
      </div>
      <ChattingInputText />
    </div>
  );
}

export default ChattingDetailPage;
