/*
 * Copyright 2013 The authors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.struts2.dom.struts;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.spring.facet.SpringFacet;
import com.intellij.spring.facet.SpringFileSet;
import com.intellij.struts2.Struts2ProjectDescriptorBuilder;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Tests highlighting with Spring plugin.
 */
public class StrutsHighlightingSpringTest extends StrutsLightHighlightingTestCase {

  private static final LightProjectDescriptor SPRING = new Struts2ProjectDescriptorBuilder()
    .withStrutsLibrary().withStrutsFacet()
    .withLibrary("spring", "spring.jar")
    .withLibrary("struts2-spring-plugin", "struts2-spring-plugin-" + STRUTS2_VERSION + ".jar");

  @NonNls
  private static final String SPRING_XML = "spring.xml";

  @Override
  @NotNull
  protected String getTestDataLocation() {
    return "strutsXml/spring";
  }

  @NotNull
  @Override
  protected LightProjectDescriptor getProjectDescriptor() {
    return SPRING;
  }

  @Override
  protected void performTearDown() throws Exception {
    final SpringFacet springFacet = SpringFacet.getInstance(myModule);
    assertNotNull(springFacet);
    springFacet.removeFileSets();
  }

  public void testStrutsSpringHighlighting() throws Throwable {
    createSpringFileSet(SPRING_XML);

    performHighlightingTest("struts-spring.xml");
  }

  public void testStrutsSpringCompletionVariantsNoSpringFacet() throws Throwable {
    @NonNls final String strutsXml = "struts-completionvariants-spring.xml";
    createStrutsFileSet(strutsXml);

    final List<String> variants = myFixture.getCompletionVariants(strutsXml);
    assertNotNull(variants);
    assertTrue(variants.contains("MyClass"));
  }

  public void testStrutsSpringCompletionVariants() throws Throwable {
    @NonNls final String strutsXml = "struts-completionvariants-spring.xml";
    createStrutsFileSet(strutsXml);

    createSpringFileSet(SPRING_XML);

    // TODO <alias> does not appear here, see com.intellij.spring.impl.SpringModelImpl#myOwnBeans
    final List<String> variants = myFixture.getCompletionVariants(strutsXml);
    assert variants != null;

    List<String> strings = Arrays.asList("MyClass", "bean1", "bean2", "springInterceptor",
                                         "springResultType");
    for (String s : strings) {
      assertTrue(variants.contains(s));
    }
    assertFalse(ContainerUtil.intersects(variants, Arrays.asList("abstractBean")));
  }

  public void testStrutsSpringCompletionVariantsSubclass() throws Throwable {
    @NonNls final String strutsXml = "struts-completionvariants-subclass-spring.xml";
    createStrutsFileSet(strutsXml);

    createSpringFileSet(SPRING_XML);

    final List<String> variants = myFixture.getCompletionVariants(strutsXml);
    assertNotNull(variants);
    assertTrue(variants.contains("springInterceptor"));
  }

  // stuff below is Spring related ===============================================

  protected void createSpringFileSet(final String... springXmlPaths) {
    final SpringFacet springFacet = createSpringFacet();

    @NonNls final SpringFileSet fileSet = springFacet.addFileSet("", "default");
    for (final String springXmlPath : springXmlPaths) {
      myFixture.copyFileToProject(springXmlPath);
      final VirtualFile file = myFixture.getTempDirFixture().getFile(springXmlPath);
      assert file != null;
      fileSet.addFile(file);
    }

    springFacet.getConfiguration().setModified();
  }

  @NotNull
  protected SpringFacet createSpringFacet() {
    final SpringFacet springFacet = SpringFacet.getInstance(myModule);
    if (springFacet != null) {
      return springFacet;
    }

    return new WriteCommandAction<SpringFacet>(myFixture.getProject()) {
      @Override
      protected void run(final Result<SpringFacet> result) throws Throwable {
        final SpringFacet facet = FacetManager.getInstance(myModule)
          .addFacet(SpringFacet.getSpringFacetType(), "spring", null);
        result.setResult(facet);
      }
    }.execute().throwException().getResultObject();
  }
}