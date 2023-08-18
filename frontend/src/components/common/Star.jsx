import React from 'react';
import { Rating } from '@mui/material';
import Box from '@mui/material/Box';

function Star() {
  return (
    <Box component="fieldset" mb={3} borderColor="transparent">
      <Rating />
    </Box>
  );
}

export default Star;
