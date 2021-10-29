package com.github.phillima.asniffer.metric;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.phillima.asniffer.interfaces.IClassMetricCollector;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.google.common.collect.ImmutableSet;

import java.util.*;

public class ASC extends VoidVisitorAdapter<Object> implements IClassMetricCollector {


	List<ImportDeclaration> imports = new ArrayList<>();
	HashMap<String, String> schemasMapper = new HashMap<>();
	CompilationUnit cu;

	//predefined java annotations
	private static Set<String> javaLangPredefined = ImmutableSet.of("Override","Deprecated","SuppressWarnings","SafeVarargs","FunctionalInterface");

	@Override
	public void visit(MarkerAnnotationExpr node, Object obj) {
		findSchema(node);
		super.visit(node, obj);
	}
	
	@Override
	public void visit(NormalAnnotationExpr node, Object obj) {
		findSchema(node);
		super.visit(node, obj);
	}
	
	@Override
	public void visit(SingleMemberAnnotationExpr node, Object obj) {
		findSchema(node);
		super.visit(node, obj);
	}

	@Override
	public void execute(CompilationUnit cu, ClassModel result, AMReport report) {
		findImports(cu);
		this.cu = cu;
		this.visit(cu, null);
	}

	@Override
	public void setResult(ClassModel result) {

		result.setSchemas(schemasMapper);
		result.addClassMetric("ASC", result.getAnnotationSchemas().size());
		
	}


	
	private void findSchema(AnnotationExpr annotation) {
		boolean isInlineQualified = annotation.getName().getQualifier().isPresent();
		int annotationLineNumber = annotation.getTokenRange().get().toRange().get().begin.line;
		String annotationName = annotation.getName().getIdentifier();
		String annotationNameAndLine = annotationName + "-" + annotationLineNumber;

		if (isInlineQualified) {			
			String schema = annotation.getName().getQualifier().get().asString();
			schemasMapper.put(annotationNameAndLine, schema);
			return;
		}
		
		//check if annotations was imported
		for (ImportDeclaration import_ : imports) {
			String schema = "";
			if (import_.getName().getIdentifier().equals(annotationName)){
				import_.getName().getQualifier().ifPresent(s -> {
					schemasMapper.put(annotationNameAndLine,s.toString());
				});
				return;
			}
		}

		String schema = "";
		//check if it is a java lang annotation
		if(javaLangPredefined.contains(annotation.getNameAsString()))
			schema = "java.lang";
		else //if not, the annotation was declared on the package being used
			schema = cu.getPackageDeclaration().get().getNameAsString();

		schemasMapper.put(annotationNameAndLine, schema);
	}
	
	private void findImports(CompilationUnit cu) {
		for (ImportDeclaration import_ : cu.getImports()) {
			if(!import_.isStatic())
				imports.add(import_);
		}
	}
}	
