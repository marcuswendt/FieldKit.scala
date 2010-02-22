package field.kit.emf.edit;

import java.net.URI;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import field.kit.Colour;
import field.kit.emf.edit.provider.descriptor.ColourPropertyDescriptor;
import field.kit.emf.edit.provider.descriptor.URIPropertyDescriptor;
import field.kit.emf.edit.provider.descriptor.VectorPropertyDescriptor;
import field.kit.math.Vec2;

/**
 * based on the tutorial from
 * 
 * @see <a
 *      href="http://eclipser-blog.blogspot.com/2007/10/custom-property-source-for-emf.html">Custom
 *      property source for EMF</a>
 * @author marcus
 */
public class FKPropertySource extends PropertySource {

	private EditorPart editor;

	public FKPropertySource(EditorPart editor,
			Object object, IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);
		this.editor = editor;
	}

	protected IPropertyDescriptor createPropertyDescriptor(
			IItemPropertyDescriptor itemPropertyDescriptor) {

		EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor
				.getFeature(object)).getEType();

		if (eType instanceof EDataType) {
			EDataType dataType = (EDataType) eType;
			Class<?> dataClass = dataType.getInstanceClass();
			// String dataName = dataType.getInstanceClassName();

			if (dataClass == Colour.class) {
				return new ColourPropertyDescriptor(object,
						itemPropertyDescriptor);

			} else if (dataClass == Vec2.class) {
				return new VectorPropertyDescriptor(object,
						itemPropertyDescriptor);

			} else if (dataClass == URI.class) {
				return new URIPropertyDescriptor(editor, object,
						itemPropertyDescriptor);

				// } else if (dataClass == Float.class) {
				// return new FloatPropertyDescriptor(object,
				// itemPropertyDescriptor);
				//
				// } else if (dataName == "float") {
				// return new FloatPropertyDescriptor(object,
				// itemPropertyDescriptor);

				// } else {
				// System.out.println("unknown type createPropertyDescriptor "
				// + dataType.getInstanceClassName());
			}
		}

		return super.createPropertyDescriptor(itemPropertyDescriptor);
	}
}