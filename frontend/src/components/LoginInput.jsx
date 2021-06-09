import React from "react";
import styled from "styled-components";

class LoginInput extends React.Component {
  render() {
    return (
      <StInput
        type='text'
        value={this.props.value}
        onChange={this.props.onChange}
      />
    )
  }
}

export default LoginInput

const StInput = styled.input`
  width: 60%;
  font-size: 1.2rem;
  margin: 1rem 0 1rem 0;
  
  border: none;
  border-bottom: 1px solid black;
  outline: none;
  
  font-family: 'Hanna-Air';
  
  :focus {
    outline: none;
  }
`