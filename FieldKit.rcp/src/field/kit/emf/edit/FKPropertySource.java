package field.kit.emf.edit;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import field.kit.Colour;
import field.kit.emf.edit.provider.descriptor.ColourPropertyDescriptor;

/**
 * based on the tutorial from
 * 
 * @see <a
 *      href="http://eclipser-blog.blogspot.com/2007/10/custom-property-source-for-emf.html">Custom
 *      property source for EMF</a>
 * @author marcus
 */
public class FKPropertySource extends PropertySource {

	public FKPropertySource(Object object,
			IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);
	}

	protected IPropertyDescriptor createPropertyDescriptor(
			IItemPropertyDescriptor itemPropertyDescriptor) {

		EClassifier eType = ((EStructuralFeature) itemPropertyDescriptor
				.getFeature(object)).getEType();

		if (eType instanceof EDataType) {
			EDataType dataType = (EDataType) eType;
			Class<?> dataClass = dataType.getInstanceClass();
			String dataName = dataType.getInstanceClassName();

			if (dataClass == Colour.class) {
				return new ColourPropertyDescriptor(object,
						itemPropertyDescriptor);
				//
				// } else if (dataClass == Vec3.class) {
				// return new Vec3PropertyDescriptor(object,
				// itemPropertyDescriptor);
				//
				// } else if (dataClass == Float.class) {
				// return new FloatPropertyDescriptor(object,
				// itemPropertyDescriptor);
				//
			} else if (dataName == "float") {
				// return new FloatPropertyDescriptor(object,
				// itemPropertyDescriptor);

			} else {
				System.out.println("unknown type createPropertyDescriptor "
						+ dataType.getInstanceClassName());
			}
		}

		return super.createPropertyDescriptor(itemPropertyDescriptor);
	}
}