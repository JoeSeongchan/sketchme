import logo from './assets/Logo.png'
import Main from './pages/Main/Main'
import Header from './components/Header'
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
    </div>
  );
}

export default App;
