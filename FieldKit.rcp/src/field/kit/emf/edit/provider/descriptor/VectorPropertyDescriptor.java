/**
 * 
 */
package field.kit.emf.edit.provider.descriptor;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author marcus
 * 
 */
public class VectorPropertyDescriptor extends PropertyDescriptor {
	private class FloatCellEditor extends CellEditor {
		private Spinner spinner;

		public FloatCellEditor(Composite parent) {
			super(parent);
		}

		@Override
		protected Control createControl(Composite parent) {
			spinner = new Spinner(parent, SWT.NULL);
			spinner.setDigits(8);
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
			info("FloatCellEditor setvalue " + value + " => "
					+ spinner.getSelection());
		}
	}
	
	// -------------------------------------------------------------------------
	
	public VectorPropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
		info("object: " + object + " itemPropertyDescriptor: "
				+ itemPropertyDescriptor);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new FloatCellEditor(parent);
	}

	private void info(Object o) {
		System.out.println(this.getClass().getSimpleName() + ": "
				+ o.toString());
	}
}
