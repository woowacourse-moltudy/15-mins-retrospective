import React from 'react'
import styled from "styled-components";
import LoginInput from "../components/LoginInput";
import axios from "axios";

class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: '',
      valid: false,
      validMessage: ""
    }
  }

  toggleValid(isValid) {
    this.setState({
      valid: isValid
    })
  }

  handleInput = async (e) => {
    if (e.target.value.length > 10) {
      this.toggleValid(false)
      this.setState({
        validMessage: "닉네임은 10글자 이하로 입력해주세요."
      })
    } else if (e.target.value.length < 1) {
      this.toggleValid(false)
    } else {
      this.toggleValid(true)
    }

    await this.setState({
      name: e.target.value.substring(0, 10).replaceAll(' ', ''),
    });
  };

  handleSubmit = async () => {
    const _data = {
      name: this.state.name
    }
    try {
      const _res = await axios({
        method: 'post',
        url: `${process.env.BASE_URL}/login`,
        data: _data
      })
      if (_res.status < 300) {
        localStorage.setItem('token', _res.data.token)
        this.props.history.push('/')
      }
    } catch (err) {
      this.toggleValid(false)
      this.setState({
        validMessage: err.message
      })
    }
  }

  render() {
    return (
      <StDiv>
        <StContainer>
          <Wrapper>
            <StHeading>
              똑똑! 로그인
            </StHeading>
            <LoginInput
              value={this.state.name}
              onChange={this.handleInput}
            />
          </Wrapper>
          {!this.state.valid && <StMessage>{this.state.validMessage}</StMessage>}
          <StButton
            onClick={this.handleSubmit}
            disabled={!this.state.valid}
          >
            입장
          </StButton>
        </StContainer>
      </StDiv>
    )
  }
}

export default Login

const StHeading = styled.div`
  font-size: 1.4rem;
  font-family: 'Hanna-Pro';
  margin: 4rem 0 2rem 0;
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

const StMessage = styled.div`
  font-size: 0.8rem;
  color: red;
  position: relative;
`

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  width: 100%;
`