/**
 * 
 */
package field.kit.emf.edit.provider.descriptor;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author marcus
 * 
 */
public class Vec3PropertyDescriptor extends PropertyDescriptor {

	public Vec3PropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	@Override
	public CellEditor createPropertyEditor(Composite composite) {
		//EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor.getFeature(object)).getEType();
		//final EDataType dataType = (EDataType) eType;

		return null;
		/*
		 * return new ExtendedDialogCellEditor(composite,
		 * getEditLabelProvider()) {
		 * 
		 * protected EDataTypeValueHandler valueHandler = new
		 * EDataTypeValueHandler( dataType);
		 * 
		 * @Override protected Object openDialogBox(Control cellEditorWindow) {
		 * 
		 * ColorDialog d = new ColorDialog(cellEditorWindow.getShell()); RGB rgb
		 * = d.open(); if (rgb == null) { return getValue(); } else { Colour c =
		 * new Colour(rgb.red, rgb.green, rgb.blue); return
		 * valueHandler.toValue(c.toLabel()); } } };
		 */
	}
}
