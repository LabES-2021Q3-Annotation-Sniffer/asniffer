package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AM;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

public class TesteInnerAnnotationType {

    private static AMReport report;
    private static ClassModel classModel;

    //Fixture
    @BeforeClass
    public static void setUp() {
        String testFilePath = Paths.get(System.getProperty("user.dir") + "/annotationtest").toString();

        report = new AM().calculate(testFilePath, "project");
        classModel = report.getPackages()
                .stream()
                .filter(pk -> pk.getPackageName().equals("annotationtest"))
                .findFirst()
                .get()
                .getClassModel("annotationtest.InnerAnnotationTypeTest");

   }

    @Test
    public void testAC() {
        int ac = classModel.getClassMetric("AC");
        Assert.assertEquals(4, ac);
    }

    @Test
    public void testUac() {
        int uac = classModel.getClassMetric("UAC");
        Assert.assertEquals(4, uac);
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
        Assert.assertEquals(2,asc);
    }

    @Test
    public void testGetSchemaInnerAnnotation(){
        String schema = classModel.getAnnotationSchema("Foo-20");
        Assert.assertEquals("annotationtest",schema);
    }

    @Test
    public void testType(){
        String compilationUnitType = classModel.getType();
        Assert.assertEquals("class",compilationUnitType);
    }

    @Test
    public void testInnerAnnotationType(){
        String innerAnnotation = classModel.getElementReport("Foo").getType();
        Assert.assertEquals("annotation-declaration",innerAnnotation);
    }





}