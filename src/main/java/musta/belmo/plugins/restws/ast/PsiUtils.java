package musta.belmo.plugins.restws.ast;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import musta.belmo.plugins.restws.action.DfsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PsiUtils {
    public static void addImport(final PsiJavaFile file, final String qualifiedName) {
        final Project project = file.getProject();
        Optional<PsiClass> possibleClass = Optional.ofNullable(JavaPsiFacade.getInstance(project)
                .findClass(qualifiedName, GlobalSearchScope.everythingScope(project)));
        Consumer<PsiClass> psiClassConsumer = psiClass -> JavaCodeStyleManager.getInstance(project)
                .addImport(file, psiClass);
        possibleClass.ifPresent(psiClassConsumer);
    }
    public static List<PsiElement> getAllJavaFiles(PsiElement dir) {
        DfsBuilder<PsiElement> dfsBuilder = new DfsBuilder<>();
        dfsBuilder.root(dir);
        dfsBuilder.nodePredicate(element -> element instanceof PsiDirectory);
        dfsBuilder.leafPredicate(element -> element instanceof PsiJavaFile);
        dfsBuilder.directChildrenGetter(psiElement -> Arrays.asList(psiElement.getChildren()));
        return dfsBuilder.toList();
    }

}
