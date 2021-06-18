import React from 'react'
import styled from 'styled-components';
import Member from './Member';

class Pair extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const PrintPair = this.props.pair.pair.map((member) => {
      return <Member name={member.name} key={member.id}/>
    })
    return (
      <StDiv>
        {this.props.emoji}{PrintPair}
      </StDiv>
    );
  }
}

export default Pair

const StDiv = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-gap: 5px;
  justify-content: center;
  align-items: center;
`