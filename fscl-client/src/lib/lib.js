import PropTypes from 'prop-types';

// true ->  false
// false ->  true
// (null | undefined | string | number ) ->  true
const toggle = (flag) => (flag === true) ? false: true;

const BaseTypes = {
   error: PropTypes.shape({
      name: PropTypes.string.isRequired,
      message: PropTypes.string
   }),
   code: PropTypes.exact({
      project: PropTypes.string.isRequired,
      entity: PropTypes.string.isRequired
   }),
   content: PropTypes.exact({
      name: PropTypes.string,
      description: PropTypes.string
   })
}

const FSCLPropTypes = {
   code: BaseTypes.code,
   content: BaseTypes.content,
   error: BaseTypes.error,
   entity: PropTypes.shape({
      self: BaseTypes.code.isRequired,
      parent: BaseTypes.code,
      children: PropTypes.arrayOf(PropTypes.shape),
      links: PropTypes.shape({
         components: PropTypes.arrayOf(PropTypes.shape),
      }),
      content: BaseTypes.content.isRequired,
      changed: PropTypes.bool.isRequired,
      pristine: PropTypes.bool.isRequired,
      error: BaseTypes.error
   }),
   format: PropTypes.shape({
      prefix: PropTypes.string.isRequired,
      separator: PropTypes.string.isRequired
   })
}

function exists(variable) {
   if((typeof variable === 'undefined') || (variable === null) ) {
      return false;
   }
   else {
      return true;
   }
}

function existsAs(variable, typeName) {
   if(exists(variable) && exists(typeName)) {
      if(typeof variable == typeName) {
         return true
      } else {
         return false
      }
   }
}


export default {
   propTypes: FSCLPropTypes,
   variable: {
      exists: exists,
      existsAs: existsAs,
      toggle: toggle
   }
}
