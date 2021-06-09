import React from 'react'
import styled from "styled-components";
import LoginInput from "../components/LoginInput";

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: ''
    }
  }

  handleInput = async (e) => {
    await this.setState({
      name: e.target.value,
    });
    console.log(this.state.name)
  };

  render() {
    return (
      <StDiv>
        <StContainer>
          <StHeading>
            똑똑! 로그인
          </StHeading>
          <LoginInput
            value={this.state.name}
            onChange={this.handleInput}
          />
          <StButton>입장</StButton>
        </StContainer>
      </StDiv>
    )
  }
}

export default Login

const StHeading = styled.div`
  font-size: 1.4rem;
  font-family: 'Hanna-Pro';
  margin-top: 4rem;
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
  justify-content: space-between;
  align-items: center;
  flex-direction: column;

  width: 25vw;
  height: 35vh;

  border-radius: 20px;
  background: #fdfdfd;
  box-shadow: 6px 7px 20px 0px #d9d9d9, -7px -6px 20px 0px #d9d9d9;
`

const StButton = styled.button`
  border: none;
  margin-bottom: 0.5rem;
  width: 95%;
  background: #d2e5f5;
  border-radius: 0 0 1rem 1rem;
  box-shadow: 0px 3px 6px 0px #b4c5d4;
  padding: 1rem;

  font-size: 1.1rem;
  font-family: 'Hanna-Air';

  /* ripple effect */
  position: relative;
  overflow: hidden;

  :after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 5px;
    height: 5px;
    background: rgba(253, 253, 253, 0.58);
    opacity: 0;
    border-radius: 50%;
    transform: scale(1, 1) translate(-50%);
    transform-origin: 50% 50%;
  }

  @keyframes ripple {
    0% {
      transform: scale(0, 0);
      opacity: 1;
    }
    20% {
      transform: scale(60, 50);
      opacity: 1;
    }
    100% {
      opacity: 0;
      transform: scale(100, 100);
    }
  }

  :focus:not(:active)::after {
    animation: ripple 2s ease-out;
  }
`