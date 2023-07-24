import logo from './assets/Logo.png'
import Main from './pages/Main/Main'
import Header from './components/common/Header'
import BaseIconButton from './components/common/BaseIconButton';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Sketch-ME^^!!!!
        </p>
        <Header/>
      </header>
      <Main/>
      <BaseIconButton icon="cancel" color="purple"/>
    </div>
  );
}

export default App;
