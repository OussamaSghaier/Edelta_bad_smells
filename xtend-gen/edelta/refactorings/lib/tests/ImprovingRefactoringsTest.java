package edelta.refactorings.lib.tests;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import edelta.badsmells.finder.lib.BadSmellsFinder;
import edelta.refactorings.lib.Refactorings;
import edelta.refactorings.lib.tests.AbstractTest;
import edelta.resolver.lib.Resolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.MapExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("all")
public class ImprovingRefactoringsTest extends AbstractTest {
  private BadSmellsFinder finder;
  
  private Refactorings refactorings;
  
  private Resolver resolver;
  
  @Before
  public void setup() {
    BadSmellsFinder _badSmellsFinder = new BadSmellsFinder();
    this.finder = _badSmellsFinder;
    Refactorings _refactorings = new Refactorings();
    this.refactorings = _refactorings;
    Resolver _resolver = new Resolver();
    this.resolver = _resolver;
  }
  
  @Test
  public void test_findDuplicateFeatures_whenNoDuplicates() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.intDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Map<EStructuralFeature, List<EStructuralFeature>> result = this.finder.findDuplicateFeatures(p);
    Assert.assertTrue(("result: " + result), result.isEmpty());
  }
  
  @Test
  public void test_findDuplicateFeatures_withDuplicates() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Map<EStructuralFeature, List<EStructuralFeature>> result = this.finder.findDuplicateFeatures(p);
    final Function1<EClass, EList<EStructuralFeature>> _function_1 = (EClass it) -> {
      return it.getEStructuralFeatures();
    };
    final Iterable<EStructuralFeature> expected = Iterables.<EStructuralFeature>concat(IterableExtensions.<EClass, EList<EStructuralFeature>>map(this.EClasses(p), _function_1));
    final Iterable<EStructuralFeature> actual = Iterables.<EStructuralFeature>concat(result.values());
    this.<EStructuralFeature>assertIterable(actual, expected);
  }
  
  @Test
  public void test_findDuplicateFeatures_withDifferingAttributesByLowerBound() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(1);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(2);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Map<EStructuralFeature, List<EStructuralFeature>> result = this.finder.findDuplicateFeatures(p);
    Assert.assertTrue(("result: " + result), result.isEmpty());
  }
  
  @Test
  public void test_findDuplicateFeatures_withDifferingContainment() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "r1");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(this.eClassReference);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "r1");
        final Procedure1<EReference> _function_3 = (EReference it_2) -> {
          it_2.setEType(this.eClassReference);
          it_2.setContainment(false);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Map<EStructuralFeature, List<EStructuralFeature>> result = this.finder.findDuplicateFeatures(p);
    Assert.assertTrue(("result: " + result), result.isEmpty());
  }
  
  @Test
  public void test_findDuplicateFeatures_withCustomEqualityPredicate() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(1);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(2);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final BiPredicate<EStructuralFeature, EStructuralFeature> _function_1 = (EStructuralFeature f1, EStructuralFeature f2) -> {
      return (Objects.equal(f1.getName(), f2.getName()) && Objects.equal(f1.getEType(), f2.getEType()));
    };
    final Map<EStructuralFeature, List<EStructuralFeature>> result = this.finder.findDuplicateFeaturesCustom(p, _function_1);
    final Function1<EClass, EList<EStructuralFeature>> _function_2 = (EClass it) -> {
      return it.getEStructuralFeatures();
    };
    final Iterable<EStructuralFeature> expected = Iterables.<EStructuralFeature>concat(IterableExtensions.<EClass, EList<EStructuralFeature>>map(this.EClasses(p), _function_2));
    final Iterable<EStructuralFeature> actual = Iterables.<EStructuralFeature>concat(result.values());
    this.<EStructuralFeature>assertIterable(actual, expected);
  }
  
  @Test
  public void test_extractSuperClass() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Function1<EClass, EList<EAttribute>> _function_1 = (EClass it) -> {
      return it.getEAttributes();
    };
    final List<EAttribute> duplicates = IterableExtensions.<EAttribute>toList(Iterables.<EAttribute>concat(IterableExtensions.<EClass, EList<EAttribute>>map(this.EClasses(p), _function_1)));
    this.refactorings.extractSuperclass(duplicates);
    final Function1<EClassifier, String> _function_2 = (EClassifier it) -> {
      return it.getName();
    };
    final List<String> classifiersNames = ListExtensions.<EClassifier, String>map(p.getEClassifiers(), _function_2);
    MatcherAssert.<List<String>>assertThat(classifiersNames, CoreMatchers.<String>hasItems("C1", "C2", "A1Element"));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(classifiersNames.size()), CoreMatchers.<Integer>is(Integer.valueOf(3)));
    final Iterable<EClass> classes = this.EClasses(p);
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[0]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[1]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[2]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(1)));
    final EAttribute extracted = IterableExtensions.<EAttribute>head((((EClass[])Conversions.unwrapArray(classes, EClass.class))[2]).getEAttributes());
    MatcherAssert.<String>assertThat(extracted.getName(), CoreMatchers.<String>is("A1"));
    MatcherAssert.<EDataType>assertThat(extracted.getEAttributeType(), CoreMatchers.<EDataType>is(this.stringDataType));
  }
  
  @Test
  public void test_extractSuperClassUnique() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
      EClass _createEClass_2 = this.createEClass(it, "C3");
      final Procedure1<EClass> _function_3 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_4 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(2);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_4);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_2, _function_3);
      EClass _createEClass_3 = this.createEClass(it, "C4");
      final Procedure1<EClass> _function_4 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_5 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(2);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_5);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_3, _function_4);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Collection<List<EStructuralFeature>> duplicates = this.finder.findDuplicateFeatures(p).values();
    MatcherAssert.<Integer>assertThat(Integer.valueOf(duplicates.size()), CoreMatchers.<Integer>is(Integer.valueOf(2)));
    this.refactorings.extractSuperclass(((List<? extends EStructuralFeature>[])Conversions.unwrapArray(duplicates, List.class))[0]);
    this.refactorings.extractSuperclass(((List<? extends EStructuralFeature>[])Conversions.unwrapArray(duplicates, List.class))[1]);
    final Function1<EClassifier, String> _function_1 = (EClassifier it) -> {
      return it.getName();
    };
    final List<String> classifiersNames = ListExtensions.<EClassifier, String>map(p.getEClassifiers(), _function_1);
    MatcherAssert.<List<String>>assertThat(classifiersNames, CoreMatchers.<String>hasItems("C1", "C2", "C3", "C4", "A1Element", "A1Element1"));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(classifiersNames.size()), CoreMatchers.<Integer>is(Integer.valueOf(6)));
    final Iterable<EClass> classes = this.EClasses(p);
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[0]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[1]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[2]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[3]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[4]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(1)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[5]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(1)));
    final EAttribute extractedA1NoLowerBound = IterableExtensions.<EAttribute>head((((EClass[])Conversions.unwrapArray(classes, EClass.class))[4]).getEAttributes());
    MatcherAssert.<String>assertThat(extractedA1NoLowerBound.getName(), CoreMatchers.<String>is("A1"));
    MatcherAssert.<EDataType>assertThat(extractedA1NoLowerBound.getEAttributeType(), CoreMatchers.<EDataType>is(this.stringDataType));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(extractedA1NoLowerBound.getLowerBound()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    final EAttribute extractedA1WithLowerBound = IterableExtensions.<EAttribute>head((((EClass[])Conversions.unwrapArray(classes, EClass.class))[5]).getEAttributes());
    MatcherAssert.<String>assertThat(extractedA1WithLowerBound.getName(), CoreMatchers.<String>is("A1"));
    MatcherAssert.<EDataType>assertThat(extractedA1WithLowerBound.getEAttributeType(), CoreMatchers.<EDataType>is(this.stringDataType));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(extractedA1WithLowerBound.getLowerBound()), CoreMatchers.<Integer>is(Integer.valueOf(2)));
  }
  
  @Test
  public void test_resolveDuplicateFeatures() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "C1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_2 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "C2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_3 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
      EClass _createEClass_2 = this.createEClass(it, "C3");
      final Procedure1<EClass> _function_3 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_4 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(2);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_4);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_2, _function_3);
      EClass _createEClass_3 = this.createEClass(it, "C4");
      final Procedure1<EClass> _function_4 = (EClass it_1) -> {
        EAttribute _createEAttribute = this.createEAttribute(it_1, "A1");
        final Procedure1<EAttribute> _function_5 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
          it_2.setLowerBound(2);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_5);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_3, _function_4);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    this.resolver.resolveDuplicatedFeatures(p);
    final Function1<EClassifier, String> _function_1 = (EClassifier it) -> {
      return it.getName();
    };
    final List<String> classifiersNames = ListExtensions.<EClassifier, String>map(p.getEClassifiers(), _function_1);
    MatcherAssert.<List<String>>assertThat(classifiersNames, CoreMatchers.<String>hasItems("C1", "C2", "C3", "C4", "A1Element", "A1Element1"));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(classifiersNames.size()), CoreMatchers.<Integer>is(Integer.valueOf(6)));
    final Iterable<EClass> classes = this.EClasses(p);
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[0]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[1]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[2]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[3]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[4]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(1)));
    MatcherAssert.<Integer>assertThat(Integer.valueOf((((EClass[])Conversions.unwrapArray(classes, EClass.class))[5]).getEAttributes().size()), CoreMatchers.<Integer>is(Integer.valueOf(1)));
    final EAttribute extractedA1NoLowerBound = IterableExtensions.<EAttribute>head((((EClass[])Conversions.unwrapArray(classes, EClass.class))[4]).getEAttributes());
    MatcherAssert.<String>assertThat(extractedA1NoLowerBound.getName(), CoreMatchers.<String>is("A1"));
    MatcherAssert.<EDataType>assertThat(extractedA1NoLowerBound.getEAttributeType(), CoreMatchers.<EDataType>is(this.stringDataType));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(extractedA1NoLowerBound.getLowerBound()), CoreMatchers.<Integer>is(Integer.valueOf(0)));
    final EAttribute extractedA1WithLowerBound = IterableExtensions.<EAttribute>head((((EClass[])Conversions.unwrapArray(classes, EClass.class))[5]).getEAttributes());
    MatcherAssert.<String>assertThat(extractedA1WithLowerBound.getName(), CoreMatchers.<String>is("A1"));
    MatcherAssert.<EDataType>assertThat(extractedA1WithLowerBound.getEAttributeType(), CoreMatchers.<EDataType>is(this.stringDataType));
    MatcherAssert.<Integer>assertThat(Integer.valueOf(extractedA1WithLowerBound.getLowerBound()), CoreMatchers.<Integer>is(Integer.valueOf(2)));
  }
  
  @Test
  public void test_findRedundantContainers() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass containedWithRedundant = this.createEClass(it, "ContainedWithRedundant");
      final EClass containedWithOpposite = this.createEClass(it, "ContainedWithOpposite");
      final EClass containedWithContained = this.createEClass(it, "ContainedWithContained");
      final EClass containedWithOptional = this.createEClass(it, "ContainedWithOptional");
      final EClass anotherClass = this.createEClass(it, "AnotherClass");
      final EClass containedWithUnrelated = this.createEClass(it, "Unrelated");
      EClass _createEClass = this.createEClass(it, "Container");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "containedWithRedundant");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(containedWithRedundant);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
        EReference _createEReference_1 = this.createEReference(it_1, "containedWithUnrelated");
        final Procedure1<EReference> _function_3 = (EReference it_2) -> {
          it_2.setEType(containedWithUnrelated);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_1, _function_3);
        EReference _createEReference_2 = this.createEReference(it_1, "containedWithOpposite");
        final Procedure1<EReference> _function_4 = (EReference it_2) -> {
          it_2.setEType(containedWithOpposite);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_2, _function_4);
        EReference _createEReference_3 = this.createEReference(it_1, "containedWithOptional");
        final Procedure1<EReference> _function_5 = (EReference it_2) -> {
          it_2.setEType(containedWithOptional);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_3, _function_5);
      };
      final EClass container = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EReference _createEReference = this.createEReference(containedWithRedundant, "redundant");
      final Procedure1<EReference> _function_2 = (EReference it_1) -> {
        it_1.setEType(container);
        it_1.setLowerBound(1);
      };
      ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
      EReference _createEReference_1 = this.createEReference(containedWithUnrelated, "unrelated");
      final Procedure1<EReference> _function_3 = (EReference it_1) -> {
        it_1.setEType(anotherClass);
        it_1.setLowerBound(1);
      };
      ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_1, _function_3);
      EReference _createEReference_2 = this.createEReference(containedWithOpposite, "correctWithOpposite");
      final Procedure1<EReference> _function_4 = (EReference it_1) -> {
        it_1.setEType(container);
        it_1.setLowerBound(1);
        it_1.setEOpposite(IterableExtensions.<EReference>last(container.getEReferences()));
      };
      ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_2, _function_4);
      EReference _createEReference_3 = this.createEReference(containedWithContained, "correctWithContainment");
      final Procedure1<EReference> _function_5 = (EReference it_1) -> {
        it_1.setEType(container);
        it_1.setLowerBound(1);
        it_1.setContainment(true);
      };
      ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_3, _function_5);
      EReference _createEReference_4 = this.createEReference(containedWithOptional, "correctNotRequired");
      final Procedure1<EReference> _function_6 = (EReference it_1) -> {
        it_1.setEType(container);
      };
      ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_4, _function_6);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Iterable<Pair<EReference, EReference>> result = this.finder.findRedundantContainers(p);
    EReference _head = IterableExtensions.<EReference>head(IterableExtensions.<EClass>head(this.EClasses(p)).getEReferences());
    EReference _head_1 = IterableExtensions.<EReference>head(IterableExtensions.<EClass>last(this.EClasses(p)).getEReferences());
    final Pair<EReference, EReference> expected = Pair.<EReference, EReference>of(_head, _head_1);
    final Pair<EReference, EReference> actual = IterableExtensions.<Pair<EReference, EReference>>head(result);
    Assert.assertEquals(result.toString(), 1, IterableExtensions.size(result));
    Assert.assertNotNull(expected);
    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void test_redundantContainerToEOpposite() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass containedWithRedundant = this.createEClass(it, "ContainedWithRedundant");
      EClass _createEClass = this.createEClass(it, "Container");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "containedWithRedundant");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(containedWithRedundant);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
      };
      final EClass container = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EReference _createEReference = this.createEReference(containedWithRedundant, "redundant");
      final Procedure1<EReference> _function_2 = (EReference it_1) -> {
        it_1.setEType(container);
        it_1.setLowerBound(1);
      };
      ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Iterable<Pair<EReference, EReference>> result = this.finder.findRedundantContainers(p);
    final EReference redundant = IterableExtensions.<EReference>head(IterableExtensions.<EClass>head(this.EClasses(p)).getEReferences());
    final EReference opposite = IterableExtensions.<EReference>head(IterableExtensions.<EClass>last(this.EClasses(p)).getEReferences());
    Assert.assertNull(redundant.getEOpposite());
    Assert.assertNull(opposite.getEOpposite());
    this.refactorings.redundantContainerToEOpposite(result);
    Assert.assertNotNull(redundant.getEOpposite());
    Assert.assertSame(redundant.getEOpposite(), opposite);
    Assert.assertSame(opposite.getEOpposite(), redundant);
  }
  
  @Test
  public void test_findDeadClassifiers() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      this.createEClass(it, "Unused1");
      final EClass used1 = this.createEClass(it, "Used1");
      final EClass used2 = this.createEClass(it, "Used2");
      EClass _createEClass = this.createEClass(it, "Unused2");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "used1");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(used1);
          it_2.setContainment(true);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
        EReference _createEReference_1 = this.createEReference(it_1, "used2");
        final Procedure1<EReference> _function_3 = (EReference it_2) -> {
          it_2.setEType(used2);
          it_2.setContainment(false);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference_1, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final List<EClassifier> result = this.finder.findDeadClassifiers(p);
    EClass _head = IterableExtensions.<EClass>head(this.EClasses(p));
    this.<EClassifier>assertIterable(result, Collections.<EClass>unmodifiableList(CollectionLiterals.<EClass>newArrayList(_head)));
  }
  
  @Test
  public void test_findClassificationByHierarchy() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "Base");
      EClass _createEClass = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "Derived2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
      EClass _createEClass_2 = this.createEClass(it, "Derived2");
      final Procedure1<EClass> _function_3 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
        EAttribute _createEAttribute = this.createEAttribute(it_1, "anAttribute");
        final Procedure1<EAttribute> _function_4 = (EAttribute it_2) -> {
          it_2.setEType(this.stringDataType);
        };
        ObjectExtensions.<EAttribute>operator_doubleArrow(_createEAttribute, _function_4);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_2, _function_3);
      EClass _createEClass_3 = this.createEClass(it, "Derived3");
      final Procedure1<EClass> _function_4 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      final EClass referenced = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_3, _function_4);
      EClass _createEClass_4 = this.createEClass(it, "Another");
      final Procedure1<EClass> _function_5 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "aRef");
        final Procedure1<EReference> _function_6 = (EReference it_2) -> {
          it_2.setEType(referenced);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_6);
      };
      final EClass another = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_4, _function_5);
      EClass _createEClass_5 = this.createEClass(it, "Derived4");
      final Procedure1<EClass> _function_6 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
        EList<EClass> _eSuperTypes_1 = it_1.getESuperTypes();
        _eSuperTypes_1.add(another);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_5, _function_6);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Map<EClass, List<EClass>> result = this.finder.findClassificationByHierarchy(p);
    EClass _head = IterableExtensions.<EClass>head(this.EClasses(p));
    ArrayList<EClass> _newArrayList = CollectionLiterals.<EClass>newArrayList(
      ((EClass[])Conversions.unwrapArray(this.EClasses(p), EClass.class))[1], 
      ((EClass[])Conversions.unwrapArray(this.EClasses(p), EClass.class))[2]);
    Pair<EClass, ArrayList<EClass>> _mappedTo = Pair.<EClass, ArrayList<EClass>>of(_head, _newArrayList);
    Assert.assertEquals(
      CollectionLiterals.<EClass, ArrayList<EClass>>newHashMap(_mappedTo), result);
  }
  
  @Test
  public void test_classificationByHierarchyToEnum() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "Base");
      EClass _createEClass = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "Derived2");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Map<EClass, List<EClass>> result = this.finder.findClassificationByHierarchy(p);
    this.refactorings.classificationByHierarchyToEnum(result);
    Assert.assertEquals(2, p.getEClassifiers().size());
    EClassifier _last = IterableExtensions.<EClassifier>last(p.getEClassifiers());
    final EEnum enum_ = ((EEnum) _last);
    Assert.assertEquals("BaseType", enum_.getName());
    final EList<EEnumLiteral> eLiterals = enum_.getELiterals();
    Assert.assertEquals(2, eLiterals.size());
    Assert.assertEquals("DERIVED1", eLiterals.get(0).getName());
    Assert.assertEquals("DERIVED2", eLiterals.get(1).getName());
    Assert.assertEquals(1, eLiterals.get(0).getValue());
    Assert.assertEquals(2, eLiterals.get(1).getValue());
    EClassifier _head = IterableExtensions.<EClassifier>head(p.getEClassifiers());
    final EClass c = ((EClass) _head);
    final EAttribute attr = this.findEAttribute(c, "baseType");
    Assert.assertSame(enum_, attr.getEType());
  }
  
  @Test
  public void test_CrossReferencerLearningTest() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "Base");
      final EClass other = this.createEClass(it, "Other");
      EClass _createEClass = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
        EReference _createEReference = this.createEReference(it_1, "aRef");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(other);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    Map<EObject, Collection<EStructuralFeature.Setting>> result = EcoreUtil.CrossReferencer.find(CollectionLiterals.<EClass>newArrayList(IterableExtensions.<EClass>head(this.EClasses(p))));
    Assert.assertTrue(result.toString(), result.isEmpty());
    final Function2<EObject, Collection<EStructuralFeature.Setting>, Boolean> _function_1 = (EObject key, Collection<EStructuralFeature.Setting> value) -> {
      return Boolean.valueOf((key instanceof EClassifier));
    };
    result = MapExtensions.<EObject, Collection<EStructuralFeature.Setting>>filter(EcoreUtil.CrossReferencer.find(CollectionLiterals.<EClass>newArrayList(IterableExtensions.<EClass>last(this.EClasses(p)))), _function_1);
    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.containsKey(((Object[])Conversions.unwrapArray(this.EClasses(p), Object.class))[0]));
    Assert.assertTrue(result.containsKey(((Object[])Conversions.unwrapArray(this.EClasses(p), Object.class))[1]));
  }
  
  @Test
  public void test_hasNoReferenceInThisPackage() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "Base");
      final EClass other = this.createEClass(it, "Other");
      EClass _createEClass = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
        EReference _createEReference = this.createEReference(it_1, "aRef");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(other);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "HasReferenceToAnotherPackage");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "aRef");
        final Procedure1<EReference> _function_3 = (EReference it_2) -> {
          it_2.setEType(this.eClassReference);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_3);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    Assert.assertTrue(this.finder.hasNoReferenceInThisPackage(((EClassifier[])Conversions.unwrapArray(this.EClasses(p), EClassifier.class))[0]));
    Assert.assertFalse(this.finder.hasNoReferenceInThisPackage(((EClassifier[])Conversions.unwrapArray(this.EClasses(p), EClassifier.class))[2]));
    Assert.assertTrue(this.finder.hasNoReferenceInThisPackage(((EClassifier[])Conversions.unwrapArray(this.EClasses(p), EClassifier.class))[3]));
  }
  
  @Test
  public void test_UsageCrossReferencerLearningTest() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "Base");
      final EClass other = this.createEClass(it, "Other");
      EClass _createEClass = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
        EReference _createEReference = this.createEReference(it_1, "aRef");
        final Procedure1<EReference> _function_2 = (EReference it_2) -> {
          it_2.setEType(other);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_2);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    final Function1<EStructuralFeature.Setting, Boolean> _function_1 = (EStructuralFeature.Setting it) -> {
      EStructuralFeature _eStructuralFeature = it.getEStructuralFeature();
      EReference _eClass_ESuperTypes = EcorePackage.eINSTANCE.getEClass_ESuperTypes();
      return Boolean.valueOf((_eStructuralFeature == _eClass_ESuperTypes));
    };
    Iterable<EStructuralFeature.Setting> result = IterableExtensions.<EStructuralFeature.Setting>filter(EcoreUtil.UsageCrossReferencer.find(IterableExtensions.<EClass>head(this.EClasses(p)), p), _function_1);
    Assert.assertEquals(1, IterableExtensions.size(result));
    Assert.assertSame(IterableExtensions.<EClass>last(this.EClasses(p)), IterableExtensions.<EStructuralFeature.Setting>head(result).getEObject());
    final Function1<EStructuralFeature.Setting, Boolean> _function_2 = (EStructuralFeature.Setting it) -> {
      EStructuralFeature _eStructuralFeature = it.getEStructuralFeature();
      EReference _eClass_ESuperTypes = EcorePackage.eINSTANCE.getEClass_ESuperTypes();
      return Boolean.valueOf((_eStructuralFeature == _eClass_ESuperTypes));
    };
    result = IterableExtensions.<EStructuralFeature.Setting>filter(EcoreUtil.UsageCrossReferencer.find(((EObject[])Conversions.unwrapArray(this.EClasses(p), EObject.class))[1], p), _function_2);
    Assert.assertEquals(0, IterableExtensions.size(result));
  }
  
  @Test
  public void test_findConcreteAbstractMetaclasses() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "ConcreteAbstractMetaclass");
      EClass _createEClass = this.createEClass(it, "CorrectAbstractMetaclass");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        it_1.setAbstract(true);
      };
      final EClass other = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      final EClass referred = this.createEClass(it, "NonBaseClass");
      EClass _createEClass_1 = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
      EClass _createEClass_2 = this.createEClass(it, "Derived2");
      final Procedure1<EClass> _function_3 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(other);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_2, _function_3);
      EClass _createEClass_3 = this.createEClass(it, "Another");
      final Procedure1<EClass> _function_4 = (EClass it_1) -> {
        EReference _createEReference = this.createEReference(it_1, "aRef");
        final Procedure1<EReference> _function_5 = (EReference it_2) -> {
          it_2.setEType(referred);
        };
        ObjectExtensions.<EReference>operator_doubleArrow(_createEReference, _function_5);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_3, _function_4);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    Iterable<EClass> result = this.finder.findConcreteAbstractMetaclasses(p);
    EClass _head = IterableExtensions.<EClass>head(this.EClasses(p));
    this.<EClass>assertIterable(result, Collections.<EClass>unmodifiableList(CollectionLiterals.<EClass>newArrayList(_head)));
  }
  
  @Test
  public void test_findAbstractConcreteMetaclasses() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "AbstractConcreteMetaclass");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        it_1.setAbstract(true);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
      EClass _createEClass_1 = this.createEClass(it, "AbstractMetaclass");
      final Procedure1<EClass> _function_2 = (EClass it_1) -> {
        it_1.setAbstract(true);
      };
      final EClass base = ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_1, _function_2);
      EClass _createEClass_2 = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_3 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass_2, _function_3);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    Iterable<EClass> result = this.finder.findAbstractConcreteMetaclasses(p);
    EClass _head = IterableExtensions.<EClass>head(this.EClasses(p));
    this.<EClass>assertIterable(result, Collections.<EClass>unmodifiableList(CollectionLiterals.<EClass>newArrayList(_head)));
  }
  
  @Test
  public void test_concreteBaseMetaclassToAbstract() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      final EClass base = this.createEClass(it, "ConcreteAbstractMetaclass");
      EClass _createEClass = this.createEClass(it, "Derived1");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        EList<EClass> _eSuperTypes = it_1.getESuperTypes();
        _eSuperTypes.add(base);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    Assert.assertFalse(IterableExtensions.<EClass>head(this.EClasses(p)).isAbstract());
    this.refactorings.concreteBaseMetaclassToAbstract(this.finder.findConcreteAbstractMetaclasses(p));
    Assert.assertTrue(IterableExtensions.<EClass>head(this.EClasses(p)).isAbstract());
  }
  
  @Test
  public void test_abstractBaseMetaclassToConcrete() {
    EPackage _createEPackage = this.factory.createEPackage();
    final Procedure1<EPackage> _function = (EPackage it) -> {
      EClass _createEClass = this.createEClass(it, "AbstractConcreteMetaclass");
      final Procedure1<EClass> _function_1 = (EClass it_1) -> {
        it_1.setAbstract(true);
      };
      ObjectExtensions.<EClass>operator_doubleArrow(_createEClass, _function_1);
    };
    final EPackage p = ObjectExtensions.<EPackage>operator_doubleArrow(_createEPackage, _function);
    Assert.assertTrue(IterableExtensions.<EClass>head(this.EClasses(p)).isAbstract());
    this.refactorings.abstractBaseMetaclassToConcrete(this.finder.findAbstractConcreteMetaclasses(p));
    Assert.assertFalse(IterableExtensions.<EClass>head(this.EClasses(p)).isAbstract());
  }
}
