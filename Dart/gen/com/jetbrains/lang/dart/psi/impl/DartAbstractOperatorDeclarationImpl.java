// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.jetbrains.lang.dart.DartTokenTypes.*;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPsiImplUtil;

public class DartAbstractOperatorDeclarationImpl extends AbstractDartOperator implements DartAbstractOperatorDeclaration {

  public DartAbstractOperatorDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitAbstractOperatorDeclaration(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartFormalParameterList getFormalParameterList() {
    return findChildByClass(DartFormalParameterList.class);
  }

  @Override
  @Nullable
  public DartReturnType getReturnType() {
    return findChildByClass(DartReturnType.class);
  }

  @Override
  @Nullable
  public DartUserDefinableOperator getUserDefinableOperator() {
    return findChildByClass(DartUserDefinableOperator.class);
  }

}
