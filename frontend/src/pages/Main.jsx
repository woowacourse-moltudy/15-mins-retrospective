import React from 'react'
import styled from 'styled-components'

class Main extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <StDiv>
        <StContainer>
          <StHeading>
            메인 페이지
          </StHeading>
        </StContainer>
      </StDiv>
    )
  }
}

export default Main

const StHeading = styled.h1`
  color: #303030;
`

const StDiv = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 100vw;
  height: 100vh;
`

const StContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 50vw;
  height: 95vh;

  border-radius: 20px;
  background: #fdfdfd;
  box-shadow: 6px 7px 20px 0px #d9d9d9, -7px -6px 20px 0px #d9d9d9;
`