package edelta.refactorings.lib;

import edelta.lib.AbstractEdelta;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class Refactorings extends AbstractEdelta {
  public Refactorings() {
    
  }
  
  public Refactorings(final AbstractEdelta other) {
    super(other);
  }
  
  /**
   * Given a non empty list of {@link EStructuralFeature}, which are known to
   * appear in several classes as duplicates, extracts a new common superclass,
   * with the duplicate feature,
   * adds the extracted superclass to the classes with the duplicate
   * feature and removes the duplicate feature from each class.
   * 
   * @param duplicates
   */
  public void extractSuperclass(final List<? extends EStructuralFeature> duplicates) {
    final EStructuralFeature feature = IterableExtensions.head(duplicates);
    final EPackage containingEPackage = feature.getEContainingClass().getEPackage();
    String _firstUpper = StringExtensions.toFirstUpper(feature.getName());
    String _plus = (_firstUpper + "Element");
    final String superClassName = this.ensureEClassifierNameIsUnique(containingEPackage, _plus);
    final Consumer<EClass> _function = (EClass it) -> {
      it.setAbstract(true);
      EList<EStructuralFeature> _eStructuralFeatures = it.getEStructuralFeatures();
      EStructuralFeature _copy = EcoreUtil.<EStructuralFeature>copy(feature);
      _eStructuralFeatures.add(_copy);
    };
    final EClass superclass = this.lib.newEClass(superClassName, _function);
    EList<EClassifier> _eClassifiers = containingEPackage.getEClassifiers();
    _eClassifiers.add(superclass);
    for (final EStructuralFeature duplicate : duplicates) {
      {
        final EClass eContainingClass = duplicate.getEContainingClass();
        EList<EClass> _eSuperTypes = eContainingClass.getESuperTypes();
        _eSuperTypes.add(superclass);
        EList<EStructuralFeature> _eStructuralFeatures = eContainingClass.getEStructuralFeatures();
        _eStructuralFeatures.remove(duplicate);
      }
    }
  }
  
  /**
   * Ensures that the proposed classifier name is unique within the specified
   * package; if not, it appends an incremental index until the name
   * is actually unique
   */
  public String ensureEClassifierNameIsUnique(final EPackage epackage, final String proposedName) {
    String className = proposedName;
    final Function1<EClassifier, String> _function = (EClassifier it) -> {
      return it.getName();
    };
    final List<String> currentEClassifiersNames = IterableExtensions.<String>sort(ListExtensions.<EClassifier, String>map(epackage.getEClassifiers(), _function));
    int counter = 1;
    while (currentEClassifiersNames.contains(className)) {
      String _className = className;
      int _plusPlus = counter++;
      className = (_className + Integer.valueOf(_plusPlus));
    }
    return className;
  }
  
  /**
   * Fix all the passed redundant containers (in the shape of pairs)
   * by setting the eOpposite property.
   * 
   * That is, given the pair r1 -> r2, then r2 is set as the opposite
   * reference of r1 and viceversa.
   */
  public void redundantContainerToEOpposite(final Iterable<Pair<EReference, EReference>> redundantContainers) {
    for (final Pair<EReference, EReference> redundant : redundantContainers) {
      {
        EReference _key = redundant.getKey();
        _key.setEOpposite(redundant.getValue());
        EReference _value = redundant.getValue();
        _value.setEOpposite(redundant.getKey());
      }
    }
  }
  
  public void classificationByHierarchyToEnum(final Map<EClass, List<EClass>> classificationsByHierarchy) {
    final BiConsumer<EClass, List<EClass>> _function = (EClass superClass, List<EClass> subClasses) -> {
      final EPackage epackage = superClass.getEPackage();
      String _name = superClass.getName();
      String _plus = (_name + "Type");
      final String enumName = this.ensureEClassifierNameIsUnique(epackage, _plus);
      final Consumer<EEnum> _function_1 = (EEnum it) -> {
        final Procedure2<EClass, Integer> _function_2 = (EClass subClass, Integer index) -> {
          final String enumLiteralName = this.ensureEClassifierNameIsUnique(epackage, subClass.getName().toUpperCase());
          EList<EEnumLiteral> _eLiterals = it.getELiterals();
          EEnumLiteral _newEEnumLiteral = this.lib.newEEnumLiteral(enumLiteralName);
          final Procedure1<EEnumLiteral> _function_3 = (EEnumLiteral it_1) -> {
            it_1.setValue(((index).intValue() + 1));
          };
          EEnumLiteral _doubleArrow = ObjectExtensions.<EEnumLiteral>operator_doubleArrow(_newEEnumLiteral, _function_3);
          _eLiterals.add(_doubleArrow);
        };
        IterableExtensions.<EClass>forEach(subClasses, _function_2);
      };
      final EEnum enum_ = this.lib.newEEnum(enumName, _function_1);
      EList<EClassifier> _eClassifiers = epackage.getEClassifiers();
      _eClassifiers.add(enum_);
      EList<EStructuralFeature> _eStructuralFeatures = superClass.getEStructuralFeatures();
      String _lowerCase = superClass.getName().toLowerCase();
      String _plus_1 = (_lowerCase + "Type");
      EAttribute _newEAttribute = this.lib.newEAttribute(_plus_1);
      final Procedure1<EAttribute> _function_2 = (EAttribute it) -> {
        it.setEType(enum_);
      };
      EAttribute _doubleArrow = ObjectExtensions.<EAttribute>operator_doubleArrow(_newEAttribute, _function_2);
      _eStructuralFeatures.add(_doubleArrow);
      EcoreUtil.removeAll(subClasses);
    };
    classificationsByHierarchy.forEach(_function);
  }
  
  public void concreteBaseMetaclassToAbstract(final Iterable<EClass> concreteAbstractMetaclasses) {
    final Consumer<EClass> _function = (EClass it) -> {
      it.setAbstract(true);
    };
    concreteAbstractMetaclasses.forEach(_function);
  }
  
  public void abstractBaseMetaclassToConcrete(final Iterable<EClass> abstractConcreteMetaclasses) {
    final Consumer<EClass> _function = (EClass it) -> {
      it.setAbstract(false);
    };
    abstractConcreteMetaclasses.forEach(_function);
  }
}
