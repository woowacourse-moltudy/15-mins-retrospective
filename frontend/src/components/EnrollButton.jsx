import React from 'react'
import styled from 'styled-components'
import Enroll from './Enroll'
import PairMatch from './PairMatch';

class EnrollButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      members: ["웨지", "다니", "샐리", "손너잘", "피카", "현구막", "소롱", "파즈", "김김", "코다", "포모", "나봄", "에드", "삭정", "춘식", "파피", "오즈"],
      pairs: [
        ["웨지", "다니"],
        ["샐리", "손너잘"],
        ["피카", "현구막"],
        ["소롱", "파즈"],
        ["김김", "코다", "포모"]
      ],
      isEnd: false
    }
  }

  checkTime() {
    const now = new Date()
    const target = new Date(now.getFullYear(), now.getMonth(), now.getDate(), this.props.time, 0, 0)
    if (now > target) {
      this.setState({
        isEnd: true
      })
    }
  }

  componentDidMount() {
    this.checkTime()
  }


  render() {
    return (
      <StContainer disabled={this.state.isEnd}>
        {this.state.isEnd
          ? <PairMatch
            pairs={this.state.pairs}
          />
          : <Enroll
            time={this.props.time}
            members={this.state.members}
          />
        }
      </StContainer>
    )
  }
}

export default EnrollButton

const StContainer = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 45%;
  height: 100%;

  border: none;
  background: none;
  font-family: 'Hanna-Air';
  color: #303030;

  border-radius: 20px;
  box-shadow: 0 1px 10px 0 #ffffff,
  1px 2px 4px 0 #828282,
    inset -4px -2px 20px 0px #e0e0e0,
  inset 8px 15px 20px 0px #ffffff,
  2px 3px 3px 0 #afafaf;

  :enabled:active {
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