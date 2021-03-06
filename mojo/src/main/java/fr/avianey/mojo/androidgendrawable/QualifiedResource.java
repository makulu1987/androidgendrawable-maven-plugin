package fr.avianey.mojo.androidgendrawable;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Preconditions;

import fr.avianey.mojo.androidgendrawable.Qualifier.Type;

public class QualifiedResource extends File {
    
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Density density;
    private final EnumMap<Type, String> typedQualifiers;
    
    public QualifiedResource(final File file, final String name, final EnumMap<Type, String> qualifiers) {
        super(file.getAbsolutePath());
        this.name = name;
        this.typedQualifiers = qualifiers;
        this.density = Density.valueOf(typedQualifiers.get(Type.density));
    }
    
    public File getOutputFor(final Density density, final File to, final Density fallback) {
        StringBuilder builder = new StringBuilder("drawable");
        EnumMap<Type, String> qualifiers = new EnumMap<Type, String>(typedQualifiers);
        qualifiers.remove(Type.density);
        if (fallback == null || !fallback.equals(density)) {
        	qualifiers.put(Type.density, density.name());
        }
        builder.append(Qualifier.toQualifiedString(qualifiers));
        return new File(to, builder.toString());
    }
    
    /**
     * Create a {@link QualifiedResource} from an input SVG file.
     * @param file
     * @return
     */
    public static final QualifiedResource fromFile(final File file) {
        Preconditions.checkNotNull(file);
        final String fileName = FilenameUtils.getBaseName(file.getAbsolutePath());
        Preconditions.checkArgument(fileName.length() > 0);
        Preconditions.checkArgument(fileName.indexOf("-") > 0);
        
        // unqualified name
        final String unqualifiedName = fileName.substring(0, fileName.indexOf("-"));
        Preconditions.checkArgument(unqualifiedName != null && unqualifiedName.matches("\\w+"));
        
        // qualifiers
        final EnumMap<Type, String> typedQualifiers = Qualifier.fromQualifiedString(fileName.substring(fileName.indexOf("-") + 1));
        
        // a density qualifier must be provided
        Preconditions.checkNotNull(typedQualifiers.get(Type.density));
        
        return new QualifiedResource(file, unqualifiedName, typedQualifiers);
    }

    public Map<Type, String> getTypedQualifiers() {
        return typedQualifiers;
    }

    public String getName() {
        return name;
    }
    
    public Density getDensity() {
        return density;
    }
    
    public String toString() {
    	return FilenameUtils.getName(getAbsolutePath());
    }
    
}