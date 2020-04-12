import React from 'react'
import { Container, Col, Row } from 'react-bootstrap'

const ItemHeader = () =>
   <Container fluid>
      <Row>
         <Col xs md lg={1}>
            <b>Links</b>
         </Col>
         <Col xs md lg={1}>
            <b>Flags</b>
         </Col>
         <Col xs md lg={10}>
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
   </Container>

export default ItemHeader
