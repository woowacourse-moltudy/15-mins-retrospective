import { createGlobalStyle } from 'styled-components'
import BMHANNAAir_otf from './BMHANNAAir_otf.otf'
import BMHANNAAir_ttf from './BMHANNAAir_ttf.ttf'
import BMHANNAPro_otf from './BMHANNAProOTF.otf'
import BMHANNAPro_ttf from './BMHANNAPro.ttf'

export default createGlobalStyle`
  @font-face {
      font-family: 'Hanna-Pro';
      src: url(${BMHANNAPro_ttf}),
      url(${BMHANNAPro_otf});
  };
    
  @font-face {
    font-family: 'Hanna-Air';
    src: url(${BMHANNAAir_ttf}),
    url(${BMHANNAAir_otf});
  };
`