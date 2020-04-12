import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types'

import ComponentLinkSelector from  '../../function/ComponentLinkSelector/ComponentLinkSelector'
import FunctionLinkSelector from  '../../component/FunctionLinkSelector/FunctionLinkSelector'

const ModalComponents = {
   "ComponentLinkSelector" : ComponentLinkSelector,
   "FunctionLinkSelector" : FunctionLinkSelector
}


const ModalRoot = ({modalType, modalProps}) => {

   if(! modalType) {
      return null;
   }

   const ActualModal = ModalComponents[modalType]
   return <ActualModal {...modalProps} />
}

ModalRoot.propTypes = {
   modalType: PropTypes.string
}

const mapStateToProps = (state) => {
   const ret = {
      modalType: state.modal.modalType,
      modalProps: state.modal.modalProps
   }

   return ret
}

export default connect(mapStateToProps, null)(ModalRoot)
