import React from 'react'
import styled from 'styled-components'
import Enroll from './Enroll'
import PairMatch from './PairMatch';
import axios from "axios";

class EnrollButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      members: [],
      pairs: [],
      isEnd: false,
      updateFlag: false
    }
  }

  checkTime() {
    const now = new Date()
    const target = new Date(now.getFullYear(), now.getMonth(), now.getDate(), this.props.time, now.getMinutes(), now.getSeconds())
    if (now > target) {
      this.setState({
        isEnd: true
      })
    }
  }

  async getPairs() {
    const _now = new Date()
    const _date = _now.toJSON().substring(0, 10)
    const _res = await axios({
      method: 'get',
      url: `http://localhost:8080/pairs?date=${_date}&conferenceTimeId=${this.props.id}`,
      headers: {
        Authorization: `Bearer ${this.props.token}`
      }
    })
    this.setState({
      pairs: _res.data
    })
  }

  async getMembers() {
    const _res = await axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/time/${this.props.id}`,
      headers: {
        Authorization: `Bearer ${this.props.token}`
      }
    })
    this.setState({
      members: _res.data.members.members
    })
  }

  handleEnroll = (props) => {
    const _ids = this.state.members.map((member) => member.id)
    if (_ids.includes(this.props.member.id)) {
      this.deleteInMembers()
    } else {
      this.addInMembers()
    }
  }

  async addInMembers() {
    const _res = await axios({
      method: 'post',
      url: `${process.env.REACT_APP_BASE_URL}/time`,
      data: {
        conferenceTimeId: this.props.id,
        memberId: this.props.member.id
      },
      headers: {
        Authorization: `Bearer ${this.props.token}`
      }
    })
    if (_res.status === 200) {
      this.setState({
        updateFlag: true
      })
    }
  }

  async deleteInMembers() {
    const _res = await axios({
      method: 'delete',
      url: `${process.env.REACT_APP_BASE_URL}/time`,
      data: {
        conferenceTimeId: this.props.id,
        memberId: this.props.member.id
      },
      headers: {
        Authorization: `Bearer ${this.props.token}`
      }
    })
    if (_res.status === 204) {
      this.setState({
        updateFlag: true
      })
    }
  }

  componentDidMount() {
    this.checkTime()
    if (this.state.isEnd) {
      this.getPairs()
    } else {
      this.getMembers()
    }
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (this.state.updateFlag) {
      this.getMembers()
      this.setState({
        updateFlag: false
      })
    }
  }

  render() {
    return (
      <StContainer
        disabled={this.state.isEnd}
        onClick={this.handleEnroll}
      >
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