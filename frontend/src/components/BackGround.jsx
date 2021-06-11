import React from 'react'
import styled from 'styled-components'

class BackGround extends React.Component {
  render() {
    return (
      <StBackGround/>
    )
  }
}

export default BackGround

const StBackGround = styled.div`
  position: fixed;
  z-index: -2;

  width: 100vw;
  height: 100vh;
  overflow: hidden;

  background-size: cover;
  -moz-background-size: cover;
  -webkit-background-size: cover;
  -o-background-size: cover;

  background: #e5e5e5;
`