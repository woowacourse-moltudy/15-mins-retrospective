import React from 'react'
import styled from 'styled-components';
import Pair from './Pair';

class PairMatch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      emoji: ["🍎", "🍊", "🥝", "🥑", "🥦", "🥕", "🥔", "🍆", "🥒", "🍍", "🍑", "🌽", "🍓", "🍇", "🍋", "🍠", "🥜"]
    }
  }

  render() {
    const PrintPairs = this.props.pairs.map((pair) => {
      const _emoji = this.state.emoji
      return <Pair pair={pair} key={pair} emoji={_emoji[Math.floor(Math.random() * _emoji.length)]}/>
    })

    return (
      <StContainer>
        <StHead>종료!</StHead>
        <StPairs>
          {PrintPairs}
        </StPairs>
      </StContainer>
    )
  }
}

export default PairMatch

const StContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;

  width: 100%;
  height: 90%;
`

const StHead = styled.div`
  font-family: 'Hanna-Pro';
  font-size: 1.4rem;
  margin: 1rem;
`

const StPairs = styled.div`
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(8, 1fr);
  grid-gap: 5px;
  justify-content: center;
  align-items: center;

  width: 90%;
  height: 100%;
  padding: 0.5rem
`