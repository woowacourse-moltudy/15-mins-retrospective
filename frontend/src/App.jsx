import React from 'react'
import {BrowserRouter, Route} from 'react-router-dom'
import {createGlobalStyle} from 'styled-components'
import Login from './pages/Login'
import Main from './pages/Main'
import GlobalFonts from './styles/fonts/fonts'
import BackGround from './components/BackGround'

class App extends React.Component {
  render() {
    return (
      <div className="App">
        <GlobalStyles/>
        <GlobalFonts/>
        <BackGround/>
        <BrowserRouter>
          <Route exact path="/" component={Main}/>
          <Route exact path="/login" component={Login}/>
        </BrowserRouter>
      </div>
    )
  }
}

export default App

const GlobalStyles = createGlobalStyle`
  body {
    font-family: 'Hanna-Air';
    color: #303030;
  }
`
