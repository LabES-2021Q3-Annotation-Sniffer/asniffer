package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

public class TestInnerAnnotationType {

    private static ClassModel classModel;

    //Fixture
    @BeforeClass
    public static void setUp() {
        String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();

        AMReport report = AmFactory.createAm(testFilePath, "project").calculate();
        classModel = report.getPackages()
                .stream()
                .filter(pk -> pk.getPackageName().equals("annotationtest"))
                .findFirst()
                .get()
                .getFirstClassModel("annotationtest.InnerAnnotationTypeTest");

   }

    @Test
    public void testAC() {
        int ac = classModel.getClassMetric("AC");
        Assert.assertEquals(6, ac);
    }

    @Test
    public void testUac() {
        int uac = classModel.getClassMetric("UAC");
        Assert.assertEquals(5, uac);
    }

    @Test
    public void testNAEC() {
        int naec = classModel.getClassMetric("NAEC");
        Assert.assertEquals(3, naec);
    }

    @Test
    public void testNEC() {
        int nec = classModel.getClassMetric("NEC");
        Assert.assertEquals(6, nec);
    }

    @Test
    public void testASC(){
        int asc = classModel.getClassMetric("ASC");
        Assert.assertEquals(3,asc);
    }

    @Test
    public void testGetSchemaInnerAnnotation(){
        String schema = classModel.getAnnotationSchema("Foo-22");
        Assert.assertEquals("annotationtest",schema);
    }

    @Test
    public void testType(){
        CodeElementType compilationUnitType = classModel.getType();
        Assert.assertEquals(CodeElementType.CLASS,compilationUnitType);
    }

    @Test
    public void testInnerAnnotationType(){
        CodeElementType innerAnnotation = classModel.getFirstElementReport("Foo").getType();
        Assert.assertEquals(CodeElementType.ANNOTATION_DECLARATION,innerAnnotation);
    }





}