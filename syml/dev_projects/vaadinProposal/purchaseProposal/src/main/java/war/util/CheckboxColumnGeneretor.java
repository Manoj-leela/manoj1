package war.util;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@SuppressWarnings("serial")
public class CheckboxColumnGeneretor implements ColumnGenerator {

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Property<?> prop = source.getItem(itemId).getItemProperty(columnId);
        return new CheckBox(null, prop);
	}

}
