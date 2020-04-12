import React from 'react';
import { ButtonGroup } from 'react-bootstrap';
import PropTypes from 'prop-types';
import HeaderButton from './HeaderButtonUI'

const HeaderButtonGroup = ({
      saveable,
      deletable,
      onSave=f=>f,
      onDelete=f=>f,
      onNew=f=>f
   }) =>
   <div className="header-btn-group">
      <ButtonGroup>
         <HeaderButton caption="Save" enabled={saveable} onClick={onSave}/>
         <HeaderButton caption="Delete" enabled={deletable} onClick={onDelete}/>
         <HeaderButton caption="New" enabled={true} onClick={onNew}/>
      </ButtonGroup>
   </div>

HeaderButtonGroup.propTypes = {
   saveable: PropTypes.bool.isRequired,
   deletable: PropTypes.bool.isRequired,
   project:  PropTypes.string,
   onSave: PropTypes.func.isRequired,
   onDelete: PropTypes.func.isRequired,
   onNew: PropTypes.func.isRequired

}

export default HeaderButtonGroup;
