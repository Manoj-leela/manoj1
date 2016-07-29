package war.vaadin;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class NameGeneratedColumn implements ColumnGenerator {

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		// TODO Auto-generated method stub
		return null;
	}
	 @SuppressWarnings("rawtypes")
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
     TextField tx = new TextField();
     tx.focus();
     tx.setWidth("90%");
     return tx;
 }
}
