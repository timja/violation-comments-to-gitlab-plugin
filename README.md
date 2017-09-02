# Violation Comments to GitLab Plugin

[![Build Status](https://ci.jenkins.io/job/Plugins/job/violation-comments-to-gitlab-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/violation-comments-to-gitlab-plugin)

This is a Jenkins plugin for [Violation Comments to GitLab Lib](https://github.com/tomasbjerre/violation-comments-to-gitlab-lib). This plugin will find report files from static code analysis and comment GitLab pull requests with the content.

You can have a look at [violations-test](https://gitlab.com/tomas.bjerre85/violations-test/merge_requests/1) to see what the result may look like.

Available in Jenkins [here](https://wiki.jenkins-ci.org/display/JENKINS/Violation+Comments+to+GitLab+Plugin).

You can get the variables you need from the [Generic Webhook Trigger plugin](https://github.com/jenkinsci/generic-webhook-trigger-plugin), [GitLab plugin](https://github.com/jenkinsci/gitlab-plugin) or [GitLab Merge Request Builder Plugin](https://github.com/timols/jenkins-gitlab-merge-request-builder-plugin).

You must perform the merge before doing the analysis for the lines to match the lines in the pull request.

```
Shell script build step
git clone $TOREPO
cd *
git reset --hard $TO
git status
git remote add from $FROMREPO
git fetch from
git merge $FROM
git --no-pager log --max-count=10 --graph --abbrev-commit

your build command here!
```

It supports:
 * [_AndroidLint_](http://developer.android.com/tools/help/lint.html)
 * [_Checkstyle_](http://checkstyle.sourceforge.net/)
   * [_Detekt_](https://github.com/arturbosch/detekt) with `--output-format xml`.
   * [_ESLint_](https://github.com/sindresorhus/grunt-eslint) with `format: 'checkstyle'`.
   * [_PHPCS_](https://github.com/squizlabs/PHP_CodeSniffer) with `phpcs api.php --report=checkstyle`.
 * [_CLang_](https://clang-analyzer.llvm.org/)
   * [_RubyCop_](http://rubocop.readthedocs.io/en/latest/formatters/) with `rubycop -f clang file.rb`
 * [_CodeNarc_](http://codenarc.sourceforge.net/)
 * [_CPD_](http://pmd.sourceforge.net/pmd-4.3.0/cpd.html)
 * [_CPPLint_](https://github.com/theandrewdavis/cpplint)
 * [_CPPCheck_](http://cppcheck.sourceforge.net/)
 * [_CSSLint_](https://github.com/CSSLint/csslint)
 * [_Findbugs_](http://findbugs.sourceforge.net/)
 * [_Flake8_](http://flake8.readthedocs.org/en/latest/)
   * [_AnsibleLint_](https://github.com/willthames/ansible-lint) with `-p`
   * [_Mccabe_](https://pypi.python.org/pypi/mccabe)
   * [_Pep8_](https://github.com/PyCQA/pycodestyle)
   * [_PyFlakes_](https://pypi.python.org/pypi/pyflakes)
 * [_FxCop_](https://en.wikipedia.org/wiki/FxCop)
 * [_Gendarme_](http://www.mono-project.com/docs/tools+libraries/tools/gendarme/)
 * [_GoLint_](https://github.com/golang/lint)
   * [_GoVet_](https://golang.org/cmd/vet/) Same format as GoLint.
 * [_JSHint_](http://jshint.com/)
 * _Lint_ A common XML format, used by different linters.
 * [_JCReport_](https://github.com/jCoderZ/fawkez/wiki/JcReport)
 * [_Klocwork_](http://www.klocwork.com/products-services/klocwork/static-code-analysis)
 * [_MyPy_](https://pypi.python.org/pypi/mypy-lang)
 * [_PerlCritic_](https://github.com/Perl-Critic)
 * [_PiTest_](http://pitest.org/)
 * [_PyDocStyle_](https://pypi.python.org/pypi/pydocstyle)
 * [_PyLint_](https://www.pylint.org/)
 * [_PMD_](https://pmd.github.io/)
   * [_Infer_](http://fbinfer.com/) Facebook Infer. With `--pmd-xml`.
   * [_PHPPMD_](https://phpmd.org/) with `phpmd api.php xml ruleset.xml`.
 * [_ReSharper_](https://www.jetbrains.com/resharper/)
 * [_SbtScalac_](http://www.scala-sbt.org/)
 * [_Simian_](http://www.harukizaemon.com/simian/)
 * [_StyleCop_](https://stylecop.codeplex.com/)
 * [_XMLLint_](http://xmlsoft.org/xmllint.html)
 * [_ZPTLint_](https://pypi.python.org/pypi/zptlint)



# Screenshots

![Example comment](https://github.com/jenkinsci/violation-comments-to-gitlab-plugin/blob/master/sandbox/mergerequest-onecomment.png)

## Job DSL Plugin

This plugin can be used with the Job DSL Plugin. Here is an example using [Generic Webhook Trigger plugin](https://github.com/jenkinsci/generic-webhook-trigger-plugin), [HTTP Request Plugin](https://wiki.jenkins-ci.org/display/JENKINS/HTTP+Request+Plugin) and [Conditional BuildStep Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Conditional+BuildStep+Plugin).

```
job('GitLab_MR_Builder') {
 concurrentBuild()
 quietPeriod(0)
 parameters {
  stringParam('MERGE_REQUEST_TO_URL', '')
  stringParam('MERGE_REQUEST_FROM_URL', '')
  stringParam('MERGE_REQUEST_TO_BRANCH', '')
  stringParam('MERGE_REQUEST_FROM_BRANCH', '')
 }
 scm {
  git {
   remote {
    name('origin')
    url('$MERGE_REQUEST_TO_URL')
   }
   remote {
    name('upstream')
    url('$MERGE_REQUEST_FROM_URL')
   }
   branch('$MERGE_REQUEST_FROM_BRANCH')
   extensions {
    mergeOptions {
     remote('upstream')
     branch('$MERGE_REQUEST_TO_BRANCH')
    }
   }
  }
 }
 triggers {
  genericTrigger {
   genericVariables {
    genericVariable {
     key("MERGE_REQUEST_TO_URL")
     value("\$.object_attributes.target.git_http_url")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MERGE_REQUEST_FROM_URL")
     value("\$.object_attributes.source.git_http_url")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MERGE_REQUEST_TO_BRANCH")
     value("\$.object_attributes.target_branch")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MERGE_REQUEST_FROM_BRANCH")
     value("\$.object_attributes.source_branch")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("PROJECT_ID")
     value("\$.object_attributes.target_project_id")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MERGE_REQUST_ID")
     value("\$.object_attributes.id")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MR_OBJECT_KIND")
     value("\$.object_kind")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MR_OLD_REV")
     value("\$.object_attributes.oldrev")
     expressionType("JSONPath")
     regexpFilter("")
    }
    genericVariable {
     key("MR_ACTION")
     value("\$.object_attributes.action")
     expressionType("JSONPath")
     regexpFilter("")
    }
   }
   regexpFilterText("\$MR_OBJECT_KIND \$MR_ACTION \$MR_OLD_REV")
   regexpFilterExpression("^merge_request\\supdate\\s.+")
  }
 }
 steps {
  httpRequest {
   url("http://gitlab:880/api/v3/projects/\$PROJECT_ID/merge_requests/\$MERGE_REQUST_ID/notes?private_token=AvAkp6HtUvzpesPypXSk")
   consoleLogResponseBody(true)
   httpMode("POST")
   requestBody('body=Building... %20\$BUILD_URL')
   }

  shell('./gradlew build')

  conditionalBuilder {
   runCondition {
    statusCondition {
     worstResult('SUCCESS')
     bestResult('SUCCESS')
    }
    runner {
     runUnstable()
    }
    conditionalbuilders {
     httpRequest {
      url('http://gitlab:880/api/v3/projects/\$PROJECT_ID/merge_requests/\$MERGE_REQUST_ID/notes?private_token=AvAkp6HtUvzpesPypXSk')
      consoleLogResponseBody(true)
      httpMode('POST')
      requestBody('body=SUCCESS%20\$BUILD_URL')
     }
    }
   }
  }

  conditionalBuilder {
   runCondition {
    statusCondition {
     worstResult('FAILURE')
     bestResult('FAILURE')
    }
    runner {
     runUnstable()
    }
    conditionalbuilders {
     httpRequest {
      url('http://gitlab:880/api/v3/projects/\$PROJECT_ID/merge_requests/\$MERGE_REQUST_ID/notes?private_token=AvAkp6HtUvzpesPypXSk')
      consoleLogResponseBody(true)
      httpMode('POST')
      requestBody('body=FAIL%20\$BUILD_URL')
     }
    }
   }
  }
 }
 publishers {
  violationsToGitLabRecorder {
   config {
    gitLabUrl("http://gitlab:880/")
    projectId("\$PROJECT_ID")
    mergeRequestId("\$MERGE_REQUST_ID")

    commentOnlyChangedContent(true)
    createCommentWithAllSingleFileComments(true)
    minSeverity('INFO')

    useApiToken(true)
    apiToken("AvAkp6HtUvzpesPypXSk")
    useApiTokenCredentials(false)
    apiTokenCredentialsId("")
    apiTokenPrivate(true)
    authMethodHeader(true)
    ignoreCertificateErrors(true)
    keepOldComments(false)
    shouldSetWip(false)

    violationConfigs {
     violationConfig {
      parser("FINDBUGS")
      reporter("Findbugs")
      pattern(".*/findbugs/.*\\.xml\$")
     }
     violationConfig {
      parser("CHECKSTYLE")
      reporter("Checkstyle")
      pattern(".*/checkstyle/.*\\.xml\$")
     }
    }
   }
  }
 }
}
```

## Pipeline Plugin

Here is an example pipeline that will merge, run unit tests, run static code analysis and finally report back to GitLab. It requires the [GitLab Plugin](https://github.com/jenkinsci/gitlab-plugin).

```
def commentMr(String comment) {
 // Not allowed to use java.net.URLEncoder.encode
 def body = comment
  .replaceAll(" ","%20")
  .replaceAll("/","%2F")
 sh "curl http://gitserver/api/v3/projects/project/merge_requests/$gitlabMergeRequestId/notes -H 'PRIVATE-TOKEN: 12398xasdasd987asda' -X POST -d \"body="+body+"\""
}
 
node {
 def mvnHome = tool 'Maven 3.3.9'
 deleteDir()
 currentBuild.description = "$gitlabMergeRequestTitle $gitlabSourceBranch http://gitserver/grp/proj/merge_requests/$gitlabMergeRequestIid"
 
 commentMr("Verifierar $gitlabSourceBranch... ${BUILD_URL}")
 
 stage('Merge') {
  sh "git init"
  sh "git fetch --no-tags --progress git@git:group/reponame.git +refs/heads/*:refs/remotes/origin/* --depth=200"
  sh "git checkout origin/${env.gitlabTargetBranch}"
  sh "git merge origin/${env.gitlabSourceBranch}"
  sh "git log --graph --abbrev-commit --max-count=10"
 }
 
 stage('Unit test') {
  sh "${mvnHome}/bin/mvn clean package -Dmaven.test.failure.ignore=false -Dcheckstyle.skip=true -Dfindbugs.skip=true -Dsurefire.skip=true -Dmaven.compile.fork=true -Dmaven.javadoc.skip=true"
 
  commentMr("Tester ok i $gitlabSourceBranch =) ${BUILD_URL}")
 }
 
 
 stage('Static code analysis') {
  sh "${mvnHome}/bin/mvn package -DskipTests -Dmaven.test.failure.ignore=false -Dsurefire.skip=true -Dmaven.compile.fork=true -Dmaven.javadoc.skip=true"

  step([
   $class: 'ViolationsToGitLabRecorder', 
   config: [
    gitLabUrl: 'http://gitserver/',
    projectId: "115",
    mergeRequestId: env.gitlabMergeRequestId,
    commentOnlyChangedContent: true,
    createCommentWithAllSingleFileComments: true, 
    minSeverity: 'INFO',
    useApiToken: true,
    apiToken: '12398xasdasd987asda',
    useApiTokenCredentials: false,
    apiTokenCredentialsId: 'id',
    apiTokenPrivate: true,
    authMethodHeader: true,
    ignoreCertificateErrors: true,
    keepOldComments: false,
    shouldSetWip: false,
    violationConfigs: [
     [ pattern: '.*/checkstyle-result\\.xml$', parser: 'CHECKSTYLE', reporter: 'Checkstyle' ], 
     [ pattern: '.*/findbugsXml\\.xml$', parser: 'FINDBUGS', reporter: 'Findbugs' ], 
     [ pattern: '.*/pmd\\.xml$', parser: 'PMD', reporter: 'PMD' ], 
    ]
   ]
  ])
 }
}
```

# Plugin development
More details on Jenkins plugin development is available [here](https://wiki.jenkins-ci.org/display/JENKINS/Plugin+tutorial).

There is a ```/build.sh``` that will perform a full build and test the plugin.

If you have release-permissions this is how you do a release:

```
mvn release:prepare release:perform
```
