import React from 'react'
import { BrowserRouter, Route } from 'react-router-dom'
import styled, { createGlobalStyle } from 'styled-components'
import Login from './pages/Login'
import Main from './pages/Main'
import GlobalFonts from './styles/fonts/fonts'

class App extends React.Component {
  render () {
    return (
      <StCont className="App">
        <GlobalStyles />
        <GlobalFonts />
          <BrowserRouter>
            <Route exact path="/" component={Main}/>
            <Route exact path="/login" component={Login}/>
          </BrowserRouter>
      </StCont>
    )
  }
}

export default App

const StCont = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 100vw;
  height: 100vh;
  
  background: #d8d8d8;
`

const GlobalStyles = createGlobalStyle`
  body {
    font-family: 'Hanna-Air';
  }
`
