package edelta.refactorings.lib.tests

import edelta.badsmells.finder.lib.BadSmellsFinder
import edelta.refactorings.lib.Refactorings
import edelta.resolver.lib.Resolver
import org.eclipse.emf.ecore.EEnum
import org.junit.Before
import org.junit.Test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer
import org.eclipse.emf.ecore.EcorePackage

class ImprovingRefactoringsTest extends AbstractTest {

	var BadSmellsFinder finder;
	var Refactorings refactorings;
	var Resolver resolver;

	@Before def void setup() {
		finder = new BadSmellsFinder
		refactorings = new Refactorings
		resolver = new Resolver
	}

	@Test def void test_findDuplicateFeatures_whenNoDuplicates() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = intDataType
				]
			]
		]
		val result = finder.findDuplicateFeatures(p)
		assertTrue("result: " + result, result.empty)
	}

	@Test def void test_findDuplicateFeatures_withDuplicates() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
		]
		val result = finder.findDuplicateFeatures(p)
		val expected = p.EClasses.map[EStructuralFeatures].flatten
		val actual = result.values.flatten
		assertIterable(actual, expected)
	}

	@Test def void test_findDuplicateFeatures_withDifferingAttributesByLowerBound() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 1 // different lowerbound from C2.A1
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 2 // different lowerbound from C1.A1
				]
			]
		]
		val result = finder.findDuplicateFeatures(p)
		assertTrue("result: " + result, result.empty)
	}

	@Test def void test_findDuplicateFeatures_withDifferingContainment() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEReference("r1") => [
					EType = eClassReference
					containment = true
				]
			]
			createEClass("C2") => [
				createEReference("r1") => [
					EType = eClassReference
					containment = false
				]
			]
		]
		val result = finder.findDuplicateFeatures(p)
		assertTrue("result: " + result, result.empty)
	}

	@Test def void test_findDuplicateFeatures_withCustomEqualityPredicate() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 1 // different lowerbound from C2.A1
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 2 // different lowerbound from C1.A1
				]
			]
		]
		// only check name and type, thus lowerBound is ignored
		// for comparison.
		val result = finder.
			findDuplicateFeaturesCustom(p) [
				f1, f2 | f1.name == f2.name && f1.EType == f2.EType
			]
		val expected = p.EClasses.map[EStructuralFeatures].flatten
		val actual = result.values.flatten
		assertIterable(actual, expected)
	}

	@Test def void test_extractSuperClass() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
		]
		// we know the duplicates, manually
		val duplicates = p.EClasses.map[EAttributes].flatten.toList
		refactorings.extractSuperclass(duplicates)
		val classifiersNames = p.EClassifiers.map[name]
		assertThat(classifiersNames, hasItems("C1", "C2", "A1Element"))
		assertThat(classifiersNames.size, is(3))
		val classes = p.EClasses
		assertThat(classes.get(0).EAttributes.size, is(0))
		assertThat(classes.get(1).EAttributes.size, is(0))
		assertThat(classes.get(2).EAttributes.size, is(1))
		val extracted = classes.get(2).EAttributes.head
		assertThat(extracted.name, is("A1"))
		assertThat(extracted.EAttributeType, is(stringDataType))
	}

	@Test def void test_extractSuperClassUnique() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C3") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 2
				]
			]
			createEClass("C4") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 2
				]
			]
		]
		val duplicates = finder.findDuplicateFeatures(p).values
		// two sets of duplicates
		assertThat(duplicates.size, is(2))
		refactorings.extractSuperclass(duplicates.get(0))
		refactorings.extractSuperclass(duplicates.get(1))
		val classifiersNames = p.EClassifiers.map[name]
		assertThat(classifiersNames, hasItems("C1", "C2", "C3", "C4", "A1Element", "A1Element1"))
		assertThat(classifiersNames.size, is(6))
		val classes = p.EClasses
		assertThat(classes.get(0).EAttributes.size, is(0)) // C1
		assertThat(classes.get(1).EAttributes.size, is(0)) // C2
		assertThat(classes.get(2).EAttributes.size, is(0)) // C3
		assertThat(classes.get(3).EAttributes.size, is(0)) // C4
		assertThat(classes.get(4).EAttributes.size, is(1)) // WithA1EString
		assertThat(classes.get(5).EAttributes.size, is(1)) // WithA1EString1
		val extractedA1NoLowerBound = classes.get(4).EAttributes.head
		assertThat(extractedA1NoLowerBound.name, is("A1"))
		assertThat(extractedA1NoLowerBound.EAttributeType, is(stringDataType))
		assertThat(extractedA1NoLowerBound.lowerBound, is(0))
		val extractedA1WithLowerBound = classes.get(5).EAttributes.head
		assertThat(extractedA1WithLowerBound.name, is("A1"))
		assertThat(extractedA1WithLowerBound.EAttributeType, is(stringDataType))
		assertThat(extractedA1WithLowerBound.lowerBound, is(2))
	}

	@Test def void test_resolveDuplicateFeatures() {
		val p = factory.createEPackage => [
			createEClass("C1") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C2") => [
				createEAttribute("A1") => [
					EType = stringDataType
				]
			]
			createEClass("C3") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 2
				]
			]
			createEClass("C4") => [
				createEAttribute("A1") => [
					EType = stringDataType
					lowerBound = 2
				]
			]
		]
		resolver.resolveDuplicatedFeatures(p)
		val classifiersNames = p.EClassifiers.map[name]
		assertThat(classifiersNames, hasItems("C1", "C2", "C3", "C4", "A1Element", "A1Element1"))
		assertThat(classifiersNames.size, is(6))
		val classes = p.EClasses
		assertThat(classes.get(0).EAttributes.size, is(0)) // C1
		assertThat(classes.get(1).EAttributes.size, is(0)) // C2
		assertThat(classes.get(2).EAttributes.size, is(0)) // C3
		assertThat(classes.get(3).EAttributes.size, is(0)) // C4
		assertThat(classes.get(4).EAttributes.size, is(1)) // WithA1EString
		assertThat(classes.get(5).EAttributes.size, is(1)) // WithA1EString1
		val extractedA1NoLowerBound = classes.get(4).EAttributes.head
		assertThat(extractedA1NoLowerBound.name, is("A1"))
		assertThat(extractedA1NoLowerBound.EAttributeType, is(stringDataType))
		assertThat(extractedA1NoLowerBound.lowerBound, is(0))
		val extractedA1WithLowerBound = classes.get(5).EAttributes.head
		assertThat(extractedA1WithLowerBound.name, is("A1"))
		assertThat(extractedA1WithLowerBound.EAttributeType, is(stringDataType))
		assertThat(extractedA1WithLowerBound.lowerBound, is(2))
	}

	@Test def void test_findRedundantContainers() {
		val p = factory.createEPackage => [
			val containedWithRedundant = createEClass("ContainedWithRedundant")
			val containedWithOpposite = createEClass("ContainedWithOpposite")
			val containedWithContained = createEClass("ContainedWithContained")
			val containedWithOptional = createEClass("ContainedWithOptional")
			val anotherClass = createEClass("AnotherClass")
			val containedWithUnrelated = createEClass("Unrelated")
			val container = createEClass("Container") => [
				createEReference("containedWithRedundant") => [
					EType = containedWithRedundant
					containment = true
				]
				createEReference("containedWithUnrelated") => [
					EType = containedWithUnrelated
					containment = true
				]
				createEReference("containedWithOpposite") => [
					EType = containedWithOpposite
					containment = true
				]
				createEReference("containedWithOptional") => [
					EType = containedWithOptional
					containment = true
				]
			]
			containedWithRedundant.createEReference("redundant") => [
				EType = container
				lowerBound = 1
			]
			containedWithUnrelated.createEReference("unrelated") => [
				EType = anotherClass
				lowerBound = 1
			]
			containedWithOpposite.createEReference("correctWithOpposite") => [
				EType = container
				lowerBound = 1
				EOpposite = container.EReferences.last
			]
			containedWithContained.createEReference("correctWithContainment") => [
				EType = container
				lowerBound = 1
				// this is correct since it's another contament relation
				containment = true
			]
			containedWithOptional.createEReference("correctNotRequired") => [
				EType = container
				// this is correct since it's not required
			]
		]
		val result = finder.findRedundantContainers(p)
		// we expect the pair
		// redundant -> containedWithRedundant
		val expected = p.EClasses.head.EReferences.head -> p.EClasses.last.EReferences.head
		val actual = result.head
		assertEquals(result.toString, 1, result.size)
		assertNotNull(expected)
		assertNotNull(actual)
		assertEquals(expected, actual)
	}

	@Test def void test_redundantContainerToEOpposite() {
		val p = factory.createEPackage => [
			val containedWithRedundant = createEClass("ContainedWithRedundant")
			val container = createEClass("Container") => [
				createEReference("containedWithRedundant") => [
					EType = containedWithRedundant
					containment = true
				]
			]
			containedWithRedundant.createEReference("redundant") => [
				EType = container
				lowerBound = 1
			]
		]
		val result = finder.findRedundantContainers(p)
		val redundant = p.EClasses.head.EReferences.head
		val opposite = p.EClasses.last.EReferences.head
		assertNull(redundant.EOpposite)
		assertNull(opposite.EOpposite)
		refactorings.redundantContainerToEOpposite(result)
		assertNotNull(redundant.EOpposite)
		assertSame(redundant.EOpposite, opposite)
		assertSame(opposite.EOpposite, redundant)
	}

	@Test def void test_findDeadClassifiers() {
		val p = factory.createEPackage => [
			createEClass("Unused1")
			val used1 = createEClass("Used1")
			val used2 = createEClass("Used2")
			createEClass("Unused2") => [
				createEReference("used1") => [
					EType = used1
					containment = true
				]
				createEReference("used2") => [
					EType = used2
					containment = false
				]
			]
		]
		val result = finder.findDeadClassifiers(p)
		assertIterable(result, #[p.EClasses.head])
	}

	@Test def void test_findClassificationByHierarchy() {
		val p = factory.createEPackage => [
			val base = createEClass("Base")
			createEClass("Derived1") => [
				ESuperTypes += base
			]
			createEClass("Derived2") => [
				ESuperTypes += base
			]
			createEClass("Derived2") => [
				ESuperTypes += base
				// not in result because has features
				createEAttribute("anAttribute") => [
					EType = stringDataType
				]
			]
			val referenced = createEClass("Derived3") => [
				// not in result because it's referenced
				ESuperTypes += base
			]
			val another = createEClass("Another") => [
				createEReference("aRef") => [
					EType = referenced
				]
			]
			createEClass("Derived4") => [
				ESuperTypes += base
				ESuperTypes += another
				// not in result because has several superclasses
			]
		]
		val result = finder.findClassificationByHierarchy(p)
		assertEquals(
			newHashMap(
				p.EClasses.head -> newArrayList(
					p.EClasses.get(1),
					p.EClasses.get(2)
				)
			),
			result
		)
	}

	@Test def void test_classificationByHierarchyToEnum() {
		val p = factory.createEPackage => [
			val base = createEClass("Base")
			createEClass("Derived1") => [
				ESuperTypes += base
			]
			createEClass("Derived2") => [
				ESuperTypes += base
			]
		]
		val result = finder.findClassificationByHierarchy(p)
		refactorings.classificationByHierarchyToEnum(result)
		assertEquals(2, p.EClassifiers.size)
		val enum = p.EClassifiers.last as EEnum
		assertEquals("BaseType", enum.name)
		val eLiterals = enum.ELiterals
		assertEquals(2, eLiterals.size)
		assertEquals("DERIVED1", eLiterals.get(0).name)
		assertEquals("DERIVED2", eLiterals.get(1).name)
		assertEquals(1, eLiterals.get(0).value)
		assertEquals(2, eLiterals.get(1).value)
		val c = p.EClassifiers.head as EClass
		val attr = findEAttribute(c, "baseType")
		assertSame(enum, attr.EType)
	}

	@Test def void test_CrossReferencerLearningTest() {
		val p = factory.createEPackage => [
			val base = createEClass("Base")
			val other = createEClass("Other")
			createEClass("Derived1") => [
				ESuperTypes += base
				createEReference("aRef") => [
					EType = other
				]
			]
		]
		var result = CrossReferencer.find(newArrayList(p.EClasses.head))
		assertTrue(result.toString, result.empty)
		result = CrossReferencer.find(newArrayList(p.EClasses.last))
			.filter[key, value| key instanceof EClassifier]
		assertEquals(2, result.size)
		assertTrue(result.containsKey(p.EClasses.get(0)))
		assertTrue(result.containsKey(p.EClasses.get(1)))
	}

	@Test def void test_hasNoReferenceInThisPackage() {
		val p = factory.createEPackage => [
			val base = createEClass("Base")
			val other = createEClass("Other")
			createEClass("Derived1") => [
				ESuperTypes += base
				createEReference("aRef") => [
					EType = other
				]
			]
			createEClass("HasReferenceToAnotherPackage") => [
				createEReference("aRef") => [
					EType = eClassReference
				]
			]
		]
		assertTrue(finder.hasNoReferenceInThisPackage(p.EClasses.get(0)))
		assertFalse(finder.hasNoReferenceInThisPackage(p.EClasses.get(2)))
		assertTrue(finder.hasNoReferenceInThisPackage(p.EClasses.get(3)))
	}

	@Test def void test_UsageCrossReferencerLearningTest() {
		val p = factory.createEPackage => [
			val base = createEClass("Base")
			val other = createEClass("Other")
			createEClass("Derived1") => [
				ESuperTypes += base
				createEReference("aRef") => [
					EType = other
				]
			]
		]
		var result = UsageCrossReferencer.find(p.EClasses.head, p)
			.filter[EStructuralFeature === EcorePackage.eINSTANCE.EClass_ESuperTypes]
		assertEquals(1, result.size)
		assertSame(p.EClasses.last, result.head.EObject)
		result = UsageCrossReferencer.find(p.EClasses.get(1), p)
			.filter[EStructuralFeature === EcorePackage.eINSTANCE.EClass_ESuperTypes]
		assertEquals(0, result.size)
	}

	@Test def void test_findConcreteAbstractMetaclasses() {
		val p = factory.createEPackage => [
			val base = createEClass("ConcreteAbstractMetaclass")
			val other = createEClass("CorrectAbstractMetaclass") => [
				abstract = true
			]
			val referred = createEClass("NonBaseClass")
			createEClass("Derived1") => [
				ESuperTypes += base
			]
			createEClass("Derived2") => [
				ESuperTypes += other
			]
			createEClass("Another") => [
				createEReference("aRef") => [
					EType = referred
				]
			]
		]
		var result = finder.findConcreteAbstractMetaclasses(p)
		assertIterable(result, #[p.EClasses.head])
	}

	@Test def void test_findAbstractConcreteMetaclasses() {
		val p = factory.createEPackage => [
			createEClass("AbstractConcreteMetaclass") => [
				abstract = true
			]
			val base = createEClass("AbstractMetaclass") => [
				abstract = true
			]
			createEClass("Derived1") => [
				ESuperTypes += base
			]
		]
		var result = finder.findAbstractConcreteMetaclasses(p)
		assertIterable(result, #[p.EClasses.head])
	}

	@Test def void test_concreteBaseMetaclassToAbstract() {
		val p = factory.createEPackage => [
			val base = createEClass("ConcreteAbstractMetaclass")
			createEClass("Derived1") => [
				ESuperTypes += base
			]
		]
		assertFalse(p.EClasses.head.abstract)
		refactorings.concreteBaseMetaclassToAbstract(finder.findConcreteAbstractMetaclasses(p))
		assertTrue(p.EClasses.head.abstract)
	}

	@Test def void test_abstractBaseMetaclassToConcrete() {
		val p = factory.createEPackage => [
			createEClass("AbstractConcreteMetaclass") => [
				abstract = true
			]
		]
		assertTrue(p.EClasses.head.abstract)
		refactorings.abstractBaseMetaclassToConcrete(finder.findAbstractConcreteMetaclasses(p))
		assertFalse(p.EClasses.head.abstract)
	}
}
