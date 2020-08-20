import React from 'react';
import { ButtonGroup, Col } from 'react-bootstrap';
import { useHistory } from "react-router-dom";
import PropTypes from 'prop-types';

import LinkButton from '../../lib/ui/LinkButtonUI'
import links from '../../lib/links.js'


const LinkSection =
   ({
      projectCode,
      active
   }) => {

      const history = useHistory();

      return (
         active
         ?  <Col xs md lg={1}>
               <ButtonGroup name="links">
                  <LinkButton
                     mark="F"
                     entity={projectCode}
                     enabled={true}
                     isEntityHome={true}
                     onClick={
                        (project) => {
                           history.push(links.functions.url(project))
                        }
                     }
                  />
                  <LinkButton
                     mark="S"
                     entity={projectCode}
                     enabled={false}
                     onClick={
                        (project) => {
                           history.push(links.systems.url(project))
                        }
                     }
                  />
                  <LinkButton
                     mark="C"
                     entity={projectCode}
                     enabled={true}
                     isEntityHome={true}
                     onClick={
                        (project) => {
                           history.push(links.components.url(project))
                        }
                     }
                  />
                  <LinkButton
                     mark="L"
                     entity={projectCode}
                     enabled={false}
                     onClick={
                        (project) => {
                           history.push(links.locations.url(project))
                        }
                     }
                  />
               </ButtonGroup>
            </Col>
         :  <Col xs md lg={1}>
               <ButtonGroup name="links">
                  <LinkButton mark = "F" enabled={false}/>
                  <LinkButton mark = "S" enabled={false}/>
                  <LinkButton mark = "C" enabled={false}/>
                  <LinkButton mark = "L" enabled={false}/>
               </ButtonGroup>
            </Col>
      )
};


LinkSection.propTypes = {
   projectCode: PropTypes.string,
   active: PropTypes.bool.isRequired
}

export default LinkSection;
