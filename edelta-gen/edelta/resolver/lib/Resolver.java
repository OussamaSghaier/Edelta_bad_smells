package edelta.resolver.lib;

import edelta.badsmells.finder.lib.BadSmellsFinder;
import edelta.lib.AbstractEdelta;
import edelta.refactorings.lib.Refactorings;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

@SuppressWarnings("all")
public class Resolver extends AbstractEdelta {
  private Refactorings refactorings;
  
  private BadSmellsFinder finder;
  
  public Resolver() {
    refactorings = new Refactorings(this);
    finder = new BadSmellsFinder(this);
  }
  
  public Resolver(final AbstractEdelta other) {
    super(other);
  }
  
  public void checkRefactoringsCatalog(final EPackage ePackage) {
    this.resolveDuplicatedFeatures(ePackage);
    final Predicate<EClassifier> _function = (EClassifier it) -> {
      return true;
    };
    this.resolveDeadClassifiers(ePackage, _function);
    this.resolveRedundantContainers(ePackage);
    this.resolveClassificationByHierarchy(ePackage);
    this.resolveConcreteAbstractMetaclass(ePackage);
  }
  
  /**
   * Extracts superclasses in the presence of duplicate features
   * considering all the classes of the given package.
   * 
   * @param ePackage
   */
  public void resolveDuplicatedFeatures(final EPackage ePackage) {
    final Consumer<List<EStructuralFeature>> _function = (List<EStructuralFeature> it) -> {
      this.refactorings.extractSuperclass(it);
    };
    this.finder.findDuplicateFeatures(ePackage).values().forEach(_function);
  }
  
  public void resolveDeadClassifiers(final EPackage ePackage, final Predicate<EClassifier> shouldRemove) {
    final List<EClassifier> deadClassifiers = this.finder.findDeadClassifiers(ePackage);
    final Consumer<EClassifier> _function = (EClassifier cl) -> {
      boolean _test = shouldRemove.test(cl);
      if (_test) {
        EcoreUtil.remove(cl);
      }
    };
    deadClassifiers.forEach(_function);
  }
  
  public void resolveRedundantContainers(final EPackage ePackage) {
    this.refactorings.redundantContainerToEOpposite(
      this.finder.findRedundantContainers(ePackage));
  }
  
  public void resolveClassificationByHierarchy(final EPackage ePackage) {
    this.refactorings.classificationByHierarchyToEnum(
      this.finder.findClassificationByHierarchy(ePackage));
  }
  
  public void resolveConcreteAbstractMetaclass(final EPackage ePackage) {
    this.refactorings.concreteBaseMetaclassToAbstract(
      this.finder.findConcreteAbstractMetaclasses(ePackage));
  }
  
  public void resolveAbstractConcreteMetaclass(final EPackage ePackage) {
    this.refactorings.abstractBaseMetaclassToConcrete(
      this.finder.findAbstractConcreteMetaclasses(ePackage));
  }
}
