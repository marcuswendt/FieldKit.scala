package field.kit.emf.edit.provider.descriptor;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author marcus
 * 
 */
public class FloatPropertyDescriptor extends PropertyDescriptor {

	protected class FloatCellEditor extends CellEditor {
		private Spinner spinner;

		public FloatCellEditor(Composite parent) {
			super(parent);
		}

		@Override
		protected Control createControl(Composite parent) {
			spinner = new Spinner(parent, SWT.NULL);
			spinner.setDigits(3);
			spinner.setMinimum(1);
			spinner.setMaximum(Integer.MAX_VALUE);
			spinner.setIncrement(10);
			spinner.setSize(200, 50);
			return spinner;
		}

		@Override
		protected Object doGetValue() {
			int selection = spinner.getSelection();
			int digits = spinner.getDigits();
			return selection / Math.pow(10, digits);
		}

		@Override
		protected void doSetFocus() {
			spinner.setFocus();
		}

		@Override
		protected void doSetValue(Object value) {
			int digits = spinner.getDigits();
			spinner
					.setSelection((int) (((Float) value) * Math.pow(10, digits)));
			System.out.println("setvalue " + value + " => "
					+ spinner.getSelection());
		}
	}

	public FloatPropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	@Override
	public LabelProvider getLabelProvider() {
		return new LabelProvider() {
			
		};
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor
		// .getFeature(object)).getEType();
		// final EDataType dataType = (EDataType) eType;

		return new FloatCellEditor(parent);
	}
}
