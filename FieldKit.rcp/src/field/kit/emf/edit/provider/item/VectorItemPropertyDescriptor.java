/**
 * 
 */
package field.kit.emf.edit.provider.item;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

/**
 * @author marcus
 *
 */
public class VectorItemPropertyDescriptor extends ItemPropertyDescriptor {
	public static final String ID_X = "X";
	public static final String ID_Y = "Y";
	public static final String ID_Z = "Z";
	
	private String propertyId;
	
	public VectorItemPropertyDescriptor(
			String propertyId,
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
		    String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category, filterFlags);
		this.propertyId = propertyId;
	}
	
	@Override
    public String getId(Object thisObject)
    {
      return displayName + propertyId;
    }	

	@Override
	public String getDisplayName(Object object) 
	{
		return displayName +" "+ propertyId;	
	}

	@Override
	public Object getPropertyValue(Object object) {
		System.out.println("FloatItemDescrpt getPropertyValue "+ object);
		return null;
	}
	
	@Override
	public void resetPropertyValue(Object object) {
		System.out.println("FloatItemDescrpt resetPropertyValue "+ object);	
		
	}

	@Override
	public void setPropertyValue(Object object, Object value) {
		System.out.println("FloatItemDescript setPropertyValue "+ object +" value "+ value);
	}
	
	@Override
	public boolean canSetProperty(Object object) {
		return isSettable;
	}

	@Override
	public String getCategory(Object object) {
		return category;
	}

	@Override
	public Collection<?> getChoiceOfValues(Object object) {
		return null;
	}

	@Override
	public String getDescription(Object object) {
		return description;
	}

	@Override
	public Object getFeature(Object object) {
		return feature;
	}

	@Override
	public String[] getFilterFlags(Object object) {
		return filterFlags;
	}

	@Override
	public Object getHelpContextIds(Object object) {
		return null;
	}

	@Override
	public IItemLabelProvider getLabelProvider(Object object) {
		return new IItemLabelProvider() {

			public Object getImage(Object object) {
				return null;
			}

			public String getText(Object object) {
				System.out.println("FloatItemDescript label "+ object);
				return "test";
			}
			
		};
	}

	@Override
	public boolean isCompatibleWith(Object object, Object anotherObject,
			IItemPropertyDescriptor anotherPropertyDescriptor) {
		return false;
	}

	@Override
	public boolean isMany(Object object) {
		return false;
	}

	@Override
	public boolean isMultiLine(Object object) {
		return false;
	}

	@Override
	public boolean isPropertySet(Object object) {
		return true;
	}

	@Override
	public boolean isSortChoices(Object object) {
		return false;
	}
}
