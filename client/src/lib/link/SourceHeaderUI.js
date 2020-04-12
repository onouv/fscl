import React from 'react'
import { Container, Navbar, Row, Col } from 'react-bootstrap'

const SourceHeader = ({entities}) => {

   let headerEntity
   let headerLine

   if(entities.length >= 0) {
      headerEntity = entities[0]
      if(entities.length > 1 ) {
         const numEntities = entities.length - 1
         headerLine = `${headerEntity.project} | ${headerEntity.entity} and ${numEntities} other`
      } else {
         headerLine = `${headerEntity.project} | ${headerEntity.entity}`
      }
   } else {
      headerLine="An Error occurred - there should be at least one source item"
   }

   return (
      <Container fluid>
         <Row>
            <Col lg={2}>
               <Navbar.Brand>Link Source: </Navbar.Brand>
            </Col>
            <Col>
               <Navbar.Brand>{headerLine}</Navbar.Brand>
            </Col>
         </Row>
         <hr/>
      </Container>
   )
}

export default SourceHeader
