package edelta.refactorings.lib.tests;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

@SuppressWarnings("all")
public class AbstractTest {
  protected EcoreFactory factory = EcoreFactory.eINSTANCE;
  
  protected EDataType stringDataType = EcorePackage.eINSTANCE.getEString();
  
  protected EDataType intDataType = EcorePackage.eINSTANCE.getEInt();
  
  protected EClass eClassReference = EcorePackage.eINSTANCE.getEClass();
  
  protected EClass createEClass(final EPackage epackage, final String name) {
    EClass _createEClass = this.factory.createEClass();
    final Procedure1<EClass> _function = (EClass it) -> {
      it.setName(name);
    };
    final EClass c = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function);
    EList<EClassifier> _eClassifiers = epackage.getEClassifiers();
    _eClassifiers.add(c);
    return c;
  }
  
  protected EAttribute createEAttribute(final EClass eclass, final String name) {
    EAttribute _createEAttribute = this.factory.createEAttribute();
    final Procedure1<EAttribute> _function = (EAttribute it) -> {
      it.setName(name);
    };
    final EAttribute a = ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function);
    EList<EStructuralFeature> _eStructuralFeatures = eclass.getEStructuralFeatures();
    _eStructuralFeatures.add(a);
    return a;
  }
  
  protected EReference createEReference(final EClass eclass, final String name) {
    EReference _createEReference = this.factory.createEReference();
    final Procedure1<EReference> _function = (EReference it) -> {
      it.setName(name);
    };
    final EReference a = ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function);
    EList<EStructuralFeature> _eStructuralFeatures = eclass.getEStructuralFeatures();
    _eStructuralFeatures.add(a);
    return a;
  }
  
  protected Iterable<EClass> EClasses(final EPackage p) {
    return Iterables.<EClass>filter(p.getEClassifiers(), EClass.class);
  }
  
  protected <T extends ENamedElement> void assertIterable(final Iterable<T> actual, final Iterable<? extends T> expected) {
    for (final T item : expected) {
      Assert.<Iterable<T>>assertThat(actual, CoreMatchers.<T>hasItem(item));
    }
    final Function1<T, String> _function = (T it) -> {
      return it.getName();
    };
    Assert.<Integer>assertThat(
      IterableExtensions.join(IterableExtensions.<T, String>map(actual, _function), ", "), 
      Integer.valueOf(IterableExtensions.size(actual)), CoreMatchers.<Integer>is(Integer.valueOf(IterableExtensions.size(expected))));
  }
  
  protected EClassifier findEClassifier(final EPackage p, final String byName) {
    final Function1<EClassifier, Boolean> _function = (EClassifier it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equal(_name, byName));
    };
    return IterableExtensions.<EClassifier>findFirst(p.getEClassifiers(), _function);
  }
  
  protected EAttribute findEAttribute(final EClass c, final String byName) {
    final Function1<EAttribute, Boolean> _function = (EAttribute it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equal(_name, byName));
    };
    return IterableExtensions.<EAttribute>findFirst(c.getEAttributes(), _function);
  }
}
