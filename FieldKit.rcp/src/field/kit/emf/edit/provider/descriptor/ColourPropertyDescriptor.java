package field.kit.emf.edit.provider.descriptor;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import field.kit.*;

/**
 * A <code>PropertyDescriptor</code> for a <code>Colour</code> property editor
 * cell
 * 
 * @author marcus
 */
public class ColourPropertyDescriptor extends PropertyDescriptor {

	private Image labelImage;
	private Device device;

	public ColourPropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);

		int width = 32;
		int height = 32;
		device = Display.getCurrent();
		labelImage = new Image(device, width, height);
		labelImage.setBackground(new Color(device, 0, 0, 0));
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return new LabelProvider() {

			public Image getImage(Object element) {
				Colour c = element == null ? new Colour() : (Colour) element;

				Color color = new Color(device, (int) (c.r() * 255), (int) (c
						.g() * 255), (int) (c.b() * 255));

				GC gc = new GC(labelImage);
				gc.setBackground(color);
				gc.fillRectangle(0, 0, labelImage.getBounds().width, labelImage
						.getBounds().height);
				gc.dispose();

				return labelImage;
			}

			public String getText(Object element) {
				Colour c = element == null ? new Colour() : (Colour) element;
				return c.toLabel();
			}
		};
	}

	@Override
	public CellEditor createPropertyEditor(Composite composite) {
		EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor
				.getFeature(object)).getEType();
		final EDataType dataType = (EDataType) eType;

		return new ExtendedDialogCellEditor(composite, getEditLabelProvider()) {

			protected EDataTypeValueHandler valueHandler = new EDataTypeValueHandler(
					dataType);

			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				ColorDialog d = new ColorDialog(cellEditorWindow.getShell());
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
}
