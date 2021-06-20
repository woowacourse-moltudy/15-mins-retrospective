import React from 'react'
import styled from 'styled-components'
import Enroll from './Enroll'
import PairMatch from './PairMatch';
import {addInMembers, deleteInMembers, getMembers, getPairs} from '../apis/MainApi';

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

  async checkTime() {
    const now = new Date()
    const target = new Date(now.getFullYear(), now.getMonth(), now.getDate(), this.props.time, 0, 0)
    if (now > target) {
      await this.setState({
        isEnd: true
      })
    }
  }

  async getPairs() {
    const _now = new Date()
    const _date = new Date(_now.getTime() - (_now.getTimezoneOffset() * 60000)).toJSON().substring(0, 10)
    const _res = await getPairs(this.props.id, _date, this.props.token)
    if (_res.status === 200) {
      this.setState({
        pairs: _res.data
      })
    }
  }

  async getMembers() {
    const _res = await getMembers(this.props.id, this.props.token)
    if (_res.status === 200) {
      this.setState({
        members: _res.data.members.members
      })
    }
  }

  handleEnroll = () => {
    const _ids = this.state.members.map((member) => member.id)
    if (_ids.includes(this.props.member.id)) {
      this.deleteInMembers()
    } else {
      this.addInMembers()
    }
  }

  async addInMembers() {
    const _res = await addInMembers(this.props.id, this.props.member.id, this.props.token)
    if (_res.status === 200) {
      this.setState({
        updateFlag: true
      })
    }
  }

  async deleteInMembers() {
    const _res = await deleteInMembers(this.props.id, this.props.member.id, this.props.token)
    if (_res.status === 204) {
      this.setState({
        updateFlag: true
      })
    }
  }

  async componentDidMount() {
    await this.checkTime()
    if (this.state.isEnd) {
      await this.getPairs()
    } else {
      await this.getMembers()
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