import React from 'react'
import styled from 'styled-components'

class Member extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return(
      <StMember>{this.props.name}</StMember>
    )
  }
}

export default Member

const StMember = styled.div`
  font-size: 1.1rem;
`

