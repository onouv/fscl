import React from 'react';
import { Row, Col } from 'react-bootstrap';
const ItemHeaderUI = () =>
   <div>
      <Row>
         <Col xs md lg={1}>
            <b>Controls</b>
         </Col>
         <Col xs md lg={11}>
            <Row>
               <Col xs={2}>
                  <b>Code</b>
               </Col>
               <Col xs={4}>
                  <b>Name</b>
               </Col>
               <Col xs={6}>
                  <b>Description</b>
               </Col>
            </Row>
         </Col>
      </Row>

   <hr/>
   </div>

export default ItemHeaderUI
