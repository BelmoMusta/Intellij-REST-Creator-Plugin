package musta.belmo.plugins.restws.ast;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public class PsiAnnotationUtils {

    public static PsiAnnotation addAnnotation(PsiModifierListOwner parameter, String annotation) {
        PsiModifierList modifierList = parameter.getModifierList();
        PsiElementFactory elementFactory = JavaPsiFacade.getInstance(parameter.getProject()).getElementFactory();
        PsiAnnotation psiAnnotation = elementFactory.createAnnotationFromText(annotation, parameter);
        if (modifierList == null || modifierList.getFirstChild() == null) {
            parameter.addBefore(psiAnnotation, parameter.getFirstChild());
        } else {
            modifierList.addBefore(psiAnnotation, modifierList.getFirstChild());
        }
        JavaCodeStyleManager.getInstance(parameter.getProject()).shortenClassReferences(psiAnnotation.getParent());
        return psiAnnotation;
    }
}
