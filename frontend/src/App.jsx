import React from 'react';
import logo from './assets/Logo.png';
import Main from './pages/Main/Main';
import Header from './components/common/Header';
import BaseIconBtnPurple from './components/common/BaseIconBtnPurple';
import BaseIconBtnWhite from './components/common/BaseIconBtnWhite';
import BaseBtnPurple from './components/common/BaseBtnPurple';
import BaseBtnWhite from './components/common/BaseBtnWhite';
import BaseIconBtnGrey from './components/common/BaseIconBtnGrey';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Sketch-ME^^!!!!
        </p>
        <Header />
      </header>
      <Main />
      <BaseIconBtnPurple icon="message" message="문의하기" />
      <BaseIconBtnWhite icon="message" message="문의하기" />
      <BaseBtnPurple message="예약하기" />
      <BaseBtnWhite message="예약하기" />
      <BaseIconBtnGrey icon="calendar" message="수정하기" />
    </div>
  );
}

export default App;
