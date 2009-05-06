package field.kit.emf.edit.provider.descriptor;

import java.util.Date;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import field.kit.*;

/**
 * based on the tutorial from
 * 
 * @see <a
 *      href="http://eclipser-blog.blogspot.com/2007/10/custom-property-source-for-emf.html">Custom
 *      property source for EMF</a>
 * @author marcus
 */
public class CustomPropertyDescriptor extends PropertyDescriptor {

	public CustomPropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	/**
	 * returns the label-name of the property
	 */
	@Override
	public String getDisplayName() {
		return itemPropertyDescriptor.getDisplayName(object);
	}

	/*
	 * @Override public ILabelProvider getLabelProvider() {
	 * 
	 * EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor
	 * .getFeature(object)).getEType();
	 * 
	 * // System.out.println("getLabelProvider for " + getDisplayName() // +
	 * " eType " + eType);
	 * 
	 * if (eType instanceof EDataType) { EDataType eDataType = (EDataType)
	 * eType;
	 * 
	 * // System.out.println("eDataType " + eDataType);
	 * 
	 * if (eDataType.getInstanceClassName() == Colour.class.getName()) { //
	 * Colour c = eDataType.
	 * 
	 * System.out.println("> colour " + object);
	 * 
	 * final IItemLabelProvider itemLabelProvider = itemPropertyDescriptor
	 * .getLabelProvider(object);
	 * 
	 * return new LabelProvider() { public String getText(Object object) {
	 * Colour c = (Colour) object; System.out.println("getText " + c); return
	 * "the Text"; // return (c == null) ? "null" : c.toString(); }
	 * 
	 * public Image getImage(Object object) { return null; } };
	 * 
	 * // if (eDataType.getInstanceClassName() == // String.class.getName()) {
	 * // System.out.println("colour "); } }
	 * 
	 * return null;
	 * 
	 * // final IItemLabelProvider itemLabelProvider = itemPropertyDescriptor //
	 * .getLabelProvider(object); // // return new LabelProvider() { // public
	 * String getText(Object object) { // return
	 * itemLabelProvider.getText(object); // } // // public Image
	 * getImage(Object object) { // return
	 * ExtendedImageRegistry.getInstance().getImage( //
	 * itemLabelProvider.getImage(object)); // } // }; }
	 */

	// public String getDescription() {
	// System.out.println("getDescription " +
	// itemPropertyDescriptor.getFeature(object));
	// return "";
	// }
	@Override
	public CellEditor createPropertyEditor(Composite composite) {

		CellEditor result = super.createPropertyEditor(composite);
		if (result == null)
			return result;

		EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor
				.getFeature(object)).getEType();

		// System.out.println("createPropertyEditor " + itemPropertyDescriptor
		// + " composite " + composite);

		if (eType instanceof EDataType) {
			final EDataType dataType = (EDataType) eType;

			System.out.println("CustomizedPropertyDescriptor "
					+ dataType.getInstanceClassName());

			// CustomizedPropertyDescriptor float

			//
			// Colour editor
			//
			if (dataType.getInstanceClassName() == Colour.class.getName()) {
				result = new ExtendedDialogCellEditor(composite,
						getEditLabelProvider()) {

					protected EDataTypeValueHandler valueHandler = new EDataTypeValueHandler(
							dataType);

					@Override
					protected Object openDialogBox(Control cellEditorWindow) {
						ColorDialog d = new ColorDialog(cellEditorWindow
								.getShell());
						RGB rgb = d.open();
						if (rgb == null) {
							return getValue();
						} else {
							Colour c = new Colour(rgb.red, rgb.green, rgb.blue);
							return valueHandler.toValue(c.toLabel());
						}
					}
				};
			}

			//
			// Date editor
			//
			if (dataType.getInstanceClass() == Date.class) {
				result = new ExtendedDialogCellEditor(composite,
						getEditLabelProvider()) {

					protected Object openDialogBox(Control cellEditorWindow) {
						final DateTime dateTime[] = new DateTime[1];
						final Date[] date = new Date[1];

						Dialog d = new Dialog(cellEditorWindow.getShell()) {
							protected Control createDialogArea(Composite parent) {
								Composite dialogArea = (Composite) super
										.createDialogArea(parent);
								dateTime[0] = new DateTime(dialogArea,
										SWT.CALENDAR);
								dateTime[0]
										.addSelectionListener(new SelectionAdapter() {
											@SuppressWarnings("deprecation")
											public void widgetSelected(
													SelectionEvent e) {
												Date dateValue = new Date();
												dateValue.setYear(dateTime[0]
														.getYear());
												dateValue.setMonth(dateTime[0]
														.getMonth());
												dateValue.setDate(dateTime[0]
														.getDay());
												date[0] = dateValue;
												super.widgetSelected(e);
											}
										});
								return dialogArea;
							}
						};
						d.setBlockOnOpen(true);
						d.open();
						if (d.getReturnCode() == Dialog.OK) {
							return date[0];
						}
						return null;
					}
				};
			}
		} else {
			System.out
					.println("CustomizedPropertyDescriptor: not an EDataType "
							+ eType);
		}
		return result;
	}
}
