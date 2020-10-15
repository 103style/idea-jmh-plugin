package ru.artyushov.jmhPlugin.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

/**
 * User: nikart
 * Date: 16/07/14
 * Time: 00:09
 */
public class ConfigurationUtils {

    public static final String SETUP_ANNOTATION = "org.openjdk.jmh.annotations.Setup";
    public static final String TEAR_DOWN_ANNOTATION = "org.openjdk.jmh.annotations.TearDown";
    public static final String JMH_ANNOTATION_NAME = "org.openjdk.jmh.annotations.Benchmark";

    public static boolean hasBenchmarkAnnotation(@NotNull PsiMethod method) {
        return method.hasAnnotation(JMH_ANNOTATION_NAME);
    }

    public static boolean hasSetupOrTearDownAnnotation(@NotNull PsiMethod method) {
        return method.hasAnnotation(SETUP_ANNOTATION) ||
                method.hasAnnotation(TEAR_DOWN_ANNOTATION);
    }


    public static boolean isBenchmarkMethod(@NotNull PsiElement element) {
        if (!(element instanceof PsiIdentifier))
            return false;

        element = element.getParent();
        if (!(element instanceof PsiMethod))
            return false;

        return isBenchmarkMethod((PsiMethod) element);
    }

    private static boolean isBenchmarkMethod(@NotNull PsiMethod method) {
        return method.hasModifierProperty("public") && hasBenchmarkAnnotation(method);
    }

    public static boolean isBenchmarkClass(@NotNull PsiElement psiElement) {
        if (!(psiElement instanceof PsiIdentifier))
            return false;

        final PsiElement element = psiElement.getParent();

        if (!(element instanceof PsiClass)) {
            return false;
        }
        return containsBenchmarkMethod((PsiClass) element);
    }

    public static boolean containsBenchmarkMethod(@NotNull PsiClass aClass) {
        final PsiMethod[] methods = aClass.getMethods();
        for (final PsiMethod method : methods) {
            if (isBenchmarkMethod(method)) return true;
        }
        return false;
    }
}
