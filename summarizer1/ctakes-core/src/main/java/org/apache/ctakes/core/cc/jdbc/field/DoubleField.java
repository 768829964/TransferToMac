package org.apache.ctakes.core.cc.jdbc.field;


import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author SPF , chip-nlp
 * @version %I%
 * @since 3/12/2019
 */
final public class DoubleField extends AbstractJdbcField<Double> {

   public DoubleField( final String name, final int index ) {
      super( name, index );
   }

   public void addToStatement( final PreparedStatement statement, final Double value ) throws SQLException {
      statement.setDouble( getIndex(), value );
   }

}
