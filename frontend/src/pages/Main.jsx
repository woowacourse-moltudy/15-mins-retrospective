import React from 'react'
import styled from 'styled-components'
import EnrollButton from "../components/EnrollButton";
import axios from "axios";

class Main extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      member: "",
      token: ""
    }
  }

  setToken() {
    this.setState({
      token: localStorage.getItem('token')
    })
  }

  async getMemberInfo() {
    const _res = await axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/member`,
      headers: {
        Authorization: `Bearer ${this.state.token}`
      }
    })

    if (_res.status === 200) {
      this.setState({
        member: _res.data
      })
    }
  }

  async componentDidMount() {
    await this.setToken()
    await this.getMemberInfo()
  }

  render() {
    return (
      <StDiv>
        <StContainer>
          <StHeading>
            <StInfo>
              <StImg src="/logo.png"/>
              <StHead>15분 회고</StHead>
              <StName>{this.state.member.name}</StName>
            </StInfo>
            <StRule>
              <StRuleItem>✔ ️오후 6시, 10시 중 원하는 시간을 선택</StRuleItem>
              <StRuleItem>✔ 매칭된 페어와 줌 또는 오프라인 진행</StRuleItem>
              <StRuleItem>✔ 월요일 ~ 일요일 중 4일 이상 참여</StRuleItem>
              <StRuleItem>✔ 참여 횟수 미달일 경우 벌금 5천원!</StRuleItem>
            </StRule>
            <div/>
          </StHeading>
          <StContents>
            <EnrollButton time={"18"}/>
            <EnrollButton time={"22"}/>
          </StContents>
        </StContainer>
      </StDiv>
    )
  }
}

export default Main

const StHeading = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;

  width: 90%;
  height: 30%;
  padding: 5%;

  background: #d2e5f5;
  border-radius: 15px 15px 0 0;
  box-shadow: 0px 3px 6px 0px #b4c5d4;
`

const StDiv = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  
  height: 100vh;
`

const StContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;

  width: 50vw;
  height: 95vh;
  padding: 0.4rem 0.5rem 0 0.5rem;

  border-radius: 20px;
  background: #fdfdfd;
  box-shadow: 6px 7px 20px 0px #d9d9d9, -7px -6px 20px 0px #d9d9d9;
`

const StContents = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  width: 90%;
  height: 60%;
  padding: 5%;
`

const StInfo = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
`

const StImg = styled.img`
  width: 2rem;
`

const StHead = styled.div`
  font-size: 1.3rem;
  font-family: 'Hanna-Pro';
`

const StName = styled.div`
  font-size: 1.2rem;
`

const StRule = styled.div`
  display: flex;
  justify-content: center;
  align-items: baseline;
  flex-direction: column;
  margin-top: 3rem;
`

const StRuleItem = styled.div`
  margin: 3px;
  font-size: 1.1rem;
`