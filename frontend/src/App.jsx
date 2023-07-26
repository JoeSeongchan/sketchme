import React from 'react';
// import ChattingBigPage from './pages/Chatting/ChattingBigPage';
import ChattingSmallPage from './pages/Chatting/ChattingSmallPage';
import './App.css';

function App() {
  return (
    <div className="App h-screen overflow-y-hidden">
      <ChattingSmallPage />
    </div>
  );
}

export default App;
