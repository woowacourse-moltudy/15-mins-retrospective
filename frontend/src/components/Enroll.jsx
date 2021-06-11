import React from 'react'
import styled from 'styled-components'

class Enroll extends React.Component {
  render() {
    return (
      <StContainer>여기서 참여할거야</StContainer>
    )
  }
}

export default Enroll

const StContainer = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 45%;
  height: 100%;

  border: none;
  background: none;
  font-family: 'Hanna-Air';

  border-radius: 20px;
  box-shadow: 0 1px 10px 0 #ffffff,
    1px 2px 4px 0 #828282,
    inset -4px -2px 20px 0px #e0e0e0,
    inset 8px 15px 20px 0px #ffffff,
    2px 3px 3px 0 #afafaf;

  :active {
    -ms-transform: translateY(0.5px) translateX(0.5px);
    -webkit-transform: translateY(0.5px) translateX(0.5px);;
    transform: translateY(0.5px) translateX(0.5px);;
    box-shadow: 0 1px 10px 0 #ffffff,
      1px 2px 4px 0 #828282,
      inset -4px -2px 20px 0px #e0e0e0,
      inset 8px 15px 20px 0px #ffffff,
      1px 1px 1px 0 #afafaf;
  }
`