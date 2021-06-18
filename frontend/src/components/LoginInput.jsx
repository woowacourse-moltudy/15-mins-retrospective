import React from 'react';
import styled from 'styled-components';

class LoginInput extends React.Component {
  render() {
    return (
      <StInput
        type='text'
        value={this.props.value}
        onChange={this.props.onChange}
        autoFocus="autoFocus"
      />
    )
  }
}

export default LoginInput

const StInput = styled.input`
  width: 60%;
  font-size: 1.2rem;
  margin: 1rem 0 1rem 0;
  padding-bottom: 0.2rem;

  border: none;
  border-bottom: 1px solid #303030;
  outline: none;
  text-align: center;

  font-family: 'Hanna-Air';

  :focus {
    outline: none;
  }
`