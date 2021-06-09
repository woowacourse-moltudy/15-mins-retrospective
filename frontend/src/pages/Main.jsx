import React from 'react'
import styled from 'styled-components'

class Main extends React.Component {
  render () {
    return (
      <div>
        <StHeading>
          메인 페이지
        </StHeading>
      </div>
    )
  }
}

export default Main

const StHeading = styled.h1`
  font-family: 'Hanna-Pro';
`