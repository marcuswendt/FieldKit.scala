package field.kit.emf.edit;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.ui.views.properties.IPropertySource;


/**
 * hooked into the editor in
 * 
 * @see ConfigurationEditor#getPropertySheetPage
 * @see <a href="http://eclipser-blog.blogspot.com/2007/10/custom-property-source-for-emf.html">Custom property source for EMF</a>
 * @author marcus
 */
public class FKAdapterFactoryContentProvider extends
		AdapterFactoryContentProvider {

	public FKAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	protected IPropertySource createPropertySource(Object object,
			IItemPropertySource itemPropertySource) {
		return new FKPropertySource(object, itemPropertySource);
	}
}
