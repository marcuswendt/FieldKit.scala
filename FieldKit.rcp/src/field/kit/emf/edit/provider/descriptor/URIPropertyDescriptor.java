package field.kit.emf.edit.provider.descriptor;

import java.io.File;
import java.net.URI;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.part.EditorPart;

/**
 * @author marcus
 */
public class URIPropertyDescriptor extends PropertyDescriptor {

	private EditorPart editor;

	public URIPropertyDescriptor(EditorPart editor, Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
		this.editor = editor;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		final IItemLabelProvider itemLabelProvider = itemPropertyDescriptor
				.getLabelProvider(object);
		return new LabelProvider() {
			@Override
			public Image getImage(Object object) {
				return ExtendedImageRegistry.getInstance().getImage(
						itemLabelProvider.getImage(object));
			}

			@Override
			public String getText(Object element) {
				return (element == null) ? "" : ((URI) element).toString();
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
				FileDialog fd = new FileDialog(cellEditorWindow.getShell(),
						SWT.OPEN);

				fd.setText(itemPropertyDescriptor.getDisplayName(object));

				// fd.setText("Open");
				// fd.setFilterPath("C:/");
				// String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
				// fd.setFilterExtensions(filterExt);

				String selected = fd.open();
				if (selected != null) {
					URIEditorInput input = (URIEditorInput) editor
							.getEditorInput().getPersistable();

					// create a java.net URI from the eclipse URI
					URI parent = new File(input.getURI().toFileString())
							.getParentFile().toURI();
					URI selectedURI = new File(selected).toURI();
					URI result = parent.relativize(selectedURI);

//					System.out.println("parent " + parent);
//					System.out.println("selected " + selectedURI);
//					System.out.println("result " + result);

					return valueHandler.toValue(result.toString());
				}
				return getValue();
			}
		};
	}
}
