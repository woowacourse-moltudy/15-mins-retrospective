import React from 'react'
import styled from 'styled-components'
import EnrollButton from '../components/EnrollButton';
import {getMember} from '../apis/LoginApi';
import {getTimes} from '../apis/MainApi';

class Main extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      member: "",
      token: "",
      times: [],
      toggle: "none"
    }
  }

  setToken() {
    this.setState({
      token: localStorage.getItem('token')
    })
  }

  toggle = () => {
    if (this.state.toggle === 'none') {
      this.setState({
        toggle: 'block'
      })
    } else {
      this.setState({
        toggle: 'none'
      })
    }
  }

  logout = () => {
    localStorage.removeItem('token')
    this.props.history.push('/login')
  }

  async getTimes() {
    const _res = await getTimes(this.state.token)
    if (_res.status === 200) {
      let times = _res.data.timesResponse
      this.timeFormat(times);
      this.setState({
        times: _res.data.timesResponse
      })
    }
  }

  timeFormat(times) {
    for (let time of times) {
      time.conferenceTime = time.conferenceTime.substring(0, 2)
    }
  }

  async getMemberInfo() {
    const _res = await getMember(this.state.token)
    if (_res.status === 200) {
      this.setState({
        member: _res.data
      })
    }
    if (_res.status === 401) {
      localStorage.removeItem('token')
      this.props.history.push('/login')
    }
  }

  async componentDidMount() {
    await this.setToken()
    await this.getMemberInfo()
    await this.getTimes()
  }

  render() {
    const Conferences = this.state.times.map((time) => {
      return <EnrollButton
        key={time.id}
        time={time.conferenceTime}
        id={time.id}
        token={this.state.token}
        member={this.state.member}
      />
    })
    return (
      <StDiv>
        <StContainer>
          <StHeading>
            <StInfo>
              <StImg src="/logo.png"/>
              <StHead>15분 회고</StHead>
              <StName onClick={this.toggle}>{this.state.member.name}</StName>
              <StToggle onClick={this.logout} style={{display:this.state.toggle}}>로그아웃</StToggle>
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
            {Conferences}
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
  position: relative;
  width: 100%;
`

const StImg = styled.img`
  width: 2rem;
  position: absolute;
  left: 0;
`

const StHead = styled.div`
  font-size: 1.3rem;
  font-family: 'Hanna-Pro';
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
`

const StName = styled.div`
  font-size: 1.2rem;
  position: absolute;
  right: 0;
`

const StToggle = styled.button`
  font-family: 'Hanna-Air';
  font-size: 1.1rem;
  position: absolute;
  right: 0;
  top: 1.5rem;
  padding: 7px;

  background: #fdfdfd;
  border-radius: 5px;
  border: none;
  
  animation: swipe ease 1s;

  @keyframes swipe {
    0% {
      opacity: 0;
      transform: translateY(-0.4rem);
    }
    100% {
      opacity: 1;
      transform: translateX(0);
    }
  }
  
  :active {
    box-shadow: -1px -1px 1px 1px #b0c3d4, inset 1px 1px 2px 1px #cccccc, inset 0px 1px 2px 1px #cccccc;
  }
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