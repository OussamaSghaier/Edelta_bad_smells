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
  
  public void checkRefactoringsCatalog(final EPackage epackage) {
    this.resolveDuplicatedFeatures(epackage);
    final Predicate<EClassifier> _function = (EClassifier it) -> {
      return true;
    };
    this.resolveDeadClassifiers(epackage, _function);
    this.resolveRedundantContainers(epackage);
    this.resolveClassificationByHierarchy(epackage);
    this.resolveConcreteAbstractMetaclass(epackage);
  }
  
  /**
   * Extracts superclasses in the presence of duplicate features
   * considering all the classes of the given package.
   * 
   * @param epackage
   */
  public void resolveDuplicatedFeatures(final EPackage epackage) {
    final Consumer<List<EStructuralFeature>> _function = (List<EStructuralFeature> it) -> {
      this.refactorings.extractSuperclass(it);
    };
    this.finder.findDuplicateFeatures(epackage).values().forEach(_function);
  }
  
  public void resolveDeadClassifiers(final EPackage epackage, final Predicate<EClassifier> shouldRemove) {
    final List<EClassifier> deadClassifiers = this.finder.findDeadClassifiers(epackage);
    final Consumer<EClassifier> _function = (EClassifier cl) -> {
      boolean _test = shouldRemove.test(cl);
      if (_test) {
        EcoreUtil.remove(cl);
      }
    };
    deadClassifiers.forEach(_function);
  }
  
  public void resolveRedundantContainers(final EPackage epackage) {
    this.refactorings.redundantContainerToEOpposite(
      this.finder.findRedundantContainers(epackage));
  }
  
  public void resolveClassificationByHierarchy(final EPackage epackage) {
    this.refactorings.classificationByHierarchyToEnum(
      this.finder.findClassificationByHierarchy(epackage));
  }
  
  public void resolveConcreteAbstractMetaclass(final EPackage epackage) {
    this.refactorings.concreteBaseMetaclassToAbstract(
      this.finder.findConcreteAbstractMetaclasses(epackage));
  }
  
  public void resolveAbstractConcreteMetaclass(final EPackage epackage) {
    this.refactorings.abstractBaseMetaclassToConcrete(
      this.finder.findAbstractConcreteMetaclasses(epackage));
  }
}
