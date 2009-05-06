package field.kit.emf.edit.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

import field.kit.emf.edit.provider.item.VectorItemPropertyDescriptor;
import field.kit.math.Vec2;

/**
 * intended to be used as an alternative super class for ItemProviders
 * automatically detects FieldKit types and creates property items for them
 * e.g. Vec2 => X: VectorComponentPropertyDescriptor and Y: VectorComponentPropertyDescriptor
 * 
 * @author marcus
 */
public class FKItemProviderAdapter extends org.eclipse.emf.edit.provider.ItemProviderAdapter {
	public FKItemProviderAdapter(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	protected ItemPropertyDescriptor createItemPropertyDescriptor(
		    AdapterFactory adapterFactory,
		    ResourceLocator resourceLocator,
		    String displayName,
		    String description,
		    EStructuralFeature feature,
		    boolean isSettable,
		    boolean multiLine,
		    boolean sortChoices,
		    Object staticImage,
		    String category,
		    String[] filterFlags)
	{
		EClassifier type = feature.getEType();

		if (type instanceof EDataType) {
			EDataType dataType = (EDataType) type;
			Class<?> dataClass = dataType.getInstanceClass();
			
			if (dataClass == Vec2.class) {				
				VectorItemPropertyDescriptor x = new VectorItemPropertyDescriptor(
						VectorItemPropertyDescriptor.ID_X,
					      adapterFactory,
					      resourceLocator,
					      displayName,
					      description,
					      feature,
					      isSettable,
					      multiLine,
					      sortChoices,
					      staticImage,
					      category,
					      filterFlags);
				
				VectorItemPropertyDescriptor y = new VectorItemPropertyDescriptor(
						VectorItemPropertyDescriptor.ID_Y,
					      adapterFactory,
					      resourceLocator,
					      displayName,
					      description,
					      feature,
					      isSettable,
					      multiLine,
					      sortChoices,
					      staticImage,
					      category,
					      filterFlags);
				
				itemPropertyDescriptors.add(x);
				return y;
			}
		}
		
		// otherwise use default descriptor
		return super.createItemPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category, filterFlags);
	}
}
