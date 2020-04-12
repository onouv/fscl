import React from 'react';
import { ButtonGroup, Button } from 'react-bootstrap';

const OkCancelButtons = ({ onOk, onCancel }) =>
   <div className="header-btn-group">
      <ButtonGroup>
         <Button onClick={onOk} variant="secondary">OK</Button>
         <Button onClick={onCancel} variant="secondary">Cancel</Button>
      </ButtonGroup>
   </div>

export default OkCancelButtons
