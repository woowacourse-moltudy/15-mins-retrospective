import React from 'react'
import styled from 'styled-components'
import Member from "./Member";

class Enroll extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      time: 0
    }
  }

  componentDidMount() {
    if (this.props.time > 12) {
      this.setState({
        time: this.props.time - 12
      })
    } else {
      this.setState({
        time: this.props.time
      })
    }
  }

  render() {
    const PrintMembers = this.props.members.map((member) => {
      return (
        <Member name={member} key={member}/>
      )
    })

    return (
      <StContainer>
        <StTime>{this.state.time}시</StTime>
        <StMembers>
          {PrintMembers}
        </StMembers>
      </StContainer>
    )
  }
}

export default Enroll

const StContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;

  width: 100%;
  height: 90%;
`

const StTime = styled.div`
  font-family: 'Hanna-Pro';
  font-size: 1.4rem;
  margin: 1rem;
`

const StMembers = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(8, 1fr);
  grid-gap: 5px;
  justify-content: center;
  align-items: center;
  
  width: 90%;
  height: 100%;
  padding: 0.5rem
`
