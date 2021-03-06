package org.jenkinsci.plugins.jvctgl.config;

import static com.google.common.base.Strings.isNullOrEmpty;
import static se.bjurr.violations.lib.util.Utils.firstNonNull;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jenkinsci.plugins.jvctgl.ViolationsToGitLabGlobalConfiguration;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import se.bjurr.violations.lib.model.SEVERITY;

public class ViolationsToGitLabConfig extends AbstractDescribableImpl<ViolationsToGitLabConfig>
    implements Serializable {
  private static final long serialVersionUID = 4851568645021422528L;

  private boolean commentOnlyChangedContent;
  private boolean commentOnlyChangedFiles = true;
  private boolean createCommentWithAllSingleFileComments;
  private boolean createSingleFileComments;
  private List<ViolationConfig> violationConfigs;
  private String gitLabUrl;
  private String projectId;
  private String mergeRequestIid;
  @Deprecated private transient String mergeRequestId;
  private String apiTokenCredentialsId;
  private Boolean ignoreCertificateErrors;
  private Boolean apiTokenPrivate;
  private Boolean authMethodHeader;
  private SEVERITY minSeverity;
  private Boolean keepOldComments;
  private String commentTemplate;
  private Boolean shouldSetWip;
  private String proxyUri;
  private String proxyCredentialsId;
  private Boolean enableLogging;
  private Integer maxNumberOfViolations;

  @DataBoundConstructor
  public ViolationsToGitLabConfig(
      final String gitLabUrl,
      final String projectId,
      final String mergeRequestIid,
      final String apiTokenCredentialsId) {
    this.gitLabUrl = gitLabUrl;
    this.projectId = projectId;
    this.mergeRequestIid = mergeRequestIid;
    this.keepOldComments = true;
    this.shouldSetWip = false;
    this.enableLogging = false;
    this.maxNumberOfViolations = null;
    this.apiTokenCredentialsId = apiTokenCredentialsId;
  }

  public ViolationsToGitLabConfig(final ViolationsToGitLabConfig rhs) {
    this.violationConfigs = rhs.violationConfigs;
    this.createCommentWithAllSingleFileComments = rhs.createCommentWithAllSingleFileComments;
    this.createSingleFileComments = rhs.createSingleFileComments;
    this.commentOnlyChangedContent = rhs.commentOnlyChangedContent;
    this.commentOnlyChangedFiles = rhs.commentOnlyChangedFiles;
    this.gitLabUrl = rhs.gitLabUrl;
    this.projectId = rhs.projectId;
    this.mergeRequestIid = rhs.mergeRequestIid;
    this.apiTokenCredentialsId = rhs.apiTokenCredentialsId;
    this.ignoreCertificateErrors = rhs.ignoreCertificateErrors;
    this.apiTokenPrivate = rhs.apiTokenPrivate;
    this.authMethodHeader = rhs.authMethodHeader;
    this.minSeverity = rhs.minSeverity;
    this.keepOldComments = rhs.keepOldComments;
    this.commentTemplate = rhs.commentTemplate;
    this.shouldSetWip = rhs.shouldSetWip;
    this.proxyUri = rhs.proxyUri;
    this.proxyCredentialsId = rhs.proxyCredentialsId;
    this.enableLogging = rhs.enableLogging;
    this.maxNumberOfViolations = rhs.maxNumberOfViolations;
  }

  public String getCommentTemplate() {
    return commentTemplate;
  }

  @DataBoundSetter
  public void setCommentTemplate(final String commentTemplate) {
    this.commentTemplate = commentTemplate;
  }

  private Object readResolve() {
    if (isNullOrEmpty(mergeRequestIid)) {
      mergeRequestIid = mergeRequestId;
    }
    return this;
  }

  public ViolationsToGitLabConfig() {
    this.violationConfigs = new ArrayList<>();
  }

  public void applyDefaults(final ViolationsToGitLabGlobalConfiguration defaults) {
    if (defaults == null) {
      return;
    }

    if (isNullOrEmpty(this.gitLabUrl)) {
      this.gitLabUrl = defaults.getGitLabUrl();
    }
    if (isNullOrEmpty(this.apiTokenCredentialsId)) {
      this.apiTokenCredentialsId = defaults.getApiTokenCredentialsId();
    }
    if (ignoreCertificateErrors == null) {
      this.ignoreCertificateErrors = defaults.isIgnoreCertificateErrors();
    }
    if (apiTokenPrivate == null) {
      this.apiTokenPrivate = defaults.isApiTokenPrivate();
    }
    if (authMethodHeader == null) {
      this.authMethodHeader = defaults.isAuthMethodHeader();
    }
    if (this.minSeverity == null) {
      this.minSeverity = defaults.getMinSeverity();
    }
  }

  public String getGitLabUrl() {
    return gitLabUrl;
  }

  public boolean getCommentOnlyChangedContent() {
    return this.commentOnlyChangedContent;
  }

  public boolean getCreateCommentWithAllSingleFileComments() {
    return this.createCommentWithAllSingleFileComments;
  }

  public boolean getCreateSingleFileComments() {
    return createSingleFileComments;
  }

  public List<ViolationConfig> getViolationConfigs() {
    return this.violationConfigs;
  }

  @DataBoundSetter
  public void setIgnoreCertificateErrors(final Boolean ignoreCertificateErrors) {
    this.ignoreCertificateErrors = ignoreCertificateErrors;
  }

  @DataBoundSetter
  public void setApiTokenPrivate(final Boolean apiTokenPrivate) {
    this.apiTokenPrivate = apiTokenPrivate;
  }

  public void setMergeRequestIid(final String mergeRequestIid) {
    this.mergeRequestIid = mergeRequestIid;
  }

  public void setProjectId(final String projectId) {
    this.projectId = projectId;
  }

  @DataBoundSetter
  public void setAuthMethodHeader(final Boolean authMethodHeader) {
    this.authMethodHeader = authMethodHeader;
  }

  public String getProjectId() {
    return projectId;
  }

  public Boolean getAuthMethodHeader() {
    return authMethodHeader;
  }

  public Boolean getApiTokenPrivate() {
    return apiTokenPrivate;
  }

  public String getMergeRequestIid() {
    return mergeRequestIid;
  }

  public String getApiTokenCredentialsId() {
    return apiTokenCredentialsId;
  }

  public Boolean getIgnoreCertificateErrors() {
    return firstNonNull(ignoreCertificateErrors, false);
  }

  @DataBoundSetter
  public void setCreateCommentWithAllSingleFileComments(
      final boolean createCommentWithAllSingleFileComments) {
    this.createCommentWithAllSingleFileComments = createCommentWithAllSingleFileComments;
  }

  @DataBoundSetter
  public void setCreateSingleFileComments(final boolean createSingleFileComments) {
    this.createSingleFileComments = createSingleFileComments;
  }

  public SEVERITY getMinSeverity() {
    return minSeverity;
  }

  @DataBoundSetter
  public void setMinSeverity(final SEVERITY minSeverity) {
    this.minSeverity = minSeverity;
  }

  @DataBoundSetter
  public void setCommentOnlyChangedContent(final boolean commentOnlyChangedContent) {
    this.commentOnlyChangedContent = commentOnlyChangedContent;
  }

  @DataBoundSetter
  public void setCommentOnlyChangedFiles(final boolean commentOnlyChangedFiles) {
    this.commentOnlyChangedFiles = commentOnlyChangedFiles;
  }

  public boolean getCommentOnlyChangedFiles() {
    return commentOnlyChangedFiles;
  }

  @DataBoundSetter
  public void setViolationConfigs(final List<ViolationConfig> violationConfigs) {
    this.violationConfigs = violationConfigs;
  }

  public void setGitLabUrl(final String gitLabUrl) {
    this.gitLabUrl = gitLabUrl;
  }

  public void setApiTokenCredentialsId(final String apiTokenCredentialsId) {
    this.apiTokenCredentialsId = apiTokenCredentialsId;
  }

  @DataBoundSetter
  @Deprecated
  public void setApiToken(final String apiToken) {
    throw new RuntimeException(
        "Setting raw API token is removed, set the apiTokenCredentialsId with a secret text credential instead!");
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ViolationsToGitLabConfig that = (ViolationsToGitLabConfig) o;
    return commentOnlyChangedContent == that.commentOnlyChangedContent
        && commentOnlyChangedFiles == that.commentOnlyChangedFiles
        && createCommentWithAllSingleFileComments == that.createCommentWithAllSingleFileComments
        && createSingleFileComments == that.createSingleFileComments
        && Objects.equals(violationConfigs, that.violationConfigs)
        && Objects.equals(gitLabUrl, that.gitLabUrl)
        && Objects.equals(projectId, that.projectId)
        && Objects.equals(mergeRequestIid, that.mergeRequestIid)
        && Objects.equals(mergeRequestId, that.mergeRequestId)
        && Objects.equals(apiTokenCredentialsId, that.apiTokenCredentialsId)
        && Objects.equals(ignoreCertificateErrors, that.ignoreCertificateErrors)
        && Objects.equals(apiTokenPrivate, that.apiTokenPrivate)
        && Objects.equals(authMethodHeader, that.authMethodHeader)
        && minSeverity == that.minSeverity
        && Objects.equals(keepOldComments, that.keepOldComments)
        && Objects.equals(commentTemplate, that.commentTemplate)
        && Objects.equals(shouldSetWip, that.shouldSetWip)
        && Objects.equals(proxyUri, that.proxyUri)
        && Objects.equals(proxyCredentialsId, that.proxyCredentialsId)
        && Objects.equals(enableLogging, that.enableLogging);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        commentOnlyChangedContent,
        commentOnlyChangedFiles,
        createCommentWithAllSingleFileComments,
        createSingleFileComments,
        violationConfigs,
        gitLabUrl,
        projectId,
        mergeRequestIid,
        mergeRequestId,
        apiTokenCredentialsId,
        ignoreCertificateErrors,
        apiTokenPrivate,
        authMethodHeader,
        minSeverity,
        keepOldComments,
        commentTemplate,
        shouldSetWip,
        proxyUri,
        proxyCredentialsId,
        enableLogging);
  }

  @Override
  public String toString() {
    return "ViolationsToGitLabConfig{"
        + "commentOnlyChangedContent="
        + commentOnlyChangedContent
        + "commentOnlyChangedFiles="
        + commentOnlyChangedFiles
        + ", createCommentWithAllSingleFileComments="
        + createCommentWithAllSingleFileComments
        + ", createSingleFileComments="
        + createSingleFileComments
        + ", violationConfigs="
        + violationConfigs
        + ", gitLabUrl='"
        + gitLabUrl
        + '\''
        + ", projectId='"
        + projectId
        + '\''
        + ", mergeRequestIid='"
        + mergeRequestIid
        + '\''
        + ", mergeRequestId='"
        + mergeRequestId
        + '\''
        + ", apiTokenCredentialsId='"
        + apiTokenCredentialsId
        + '\''
        + ", ignoreCertificateErrors="
        + ignoreCertificateErrors
        + ", apiTokenPrivate="
        + apiTokenPrivate
        + ", authMethodHeader="
        + authMethodHeader
        + ", minSeverity="
        + minSeverity
        + ", keepOldComments="
        + keepOldComments
        + ", commentTemplate='"
        + commentTemplate
        + '\''
        + ", shouldSetWip="
        + shouldSetWip
        + '}';
  }

  public Boolean getKeepOldComments() {
    return keepOldComments;
  }

  public Boolean getShouldSetWip() {
    return shouldSetWip;
  }

  @DataBoundSetter
  public void setShouldSetWip(final Boolean shouldSetWip) {
    this.shouldSetWip = shouldSetWip;
  }

  @DataBoundSetter
  public void setKeepOldComments(final Boolean keepOldComments) {
    this.keepOldComments = keepOldComments;
  }

  @DataBoundSetter
  public void setProxyUri(final String proxyUri) {
    this.proxyUri = proxyUri;
  }

  @DataBoundSetter
  @Deprecated
  public void setProxyPassword(final String proxyPassword) {
    throw new RuntimeException(
        "Set proxyPassword is removed. Use proxyCredentialsId with a username/password credential");
  }

  @DataBoundSetter
  @Deprecated
  public void setProxyUser(final String proxyUser) {
    throw new RuntimeException(
        "Set proxyUser is removed. Use proxyCredentialsId with a username/password credential");
  }

  public String getProxyUri() {
    return proxyUri;
  }

  @DataBoundSetter
  public void setProxyCredentialsId(final String proxyCredentialsId) {
    this.proxyCredentialsId = proxyCredentialsId;
  }

  public String getProxyCredentialsId() {
    return proxyCredentialsId;
  }

  @DataBoundSetter
  public void setEnableLogging(final Boolean enableLogging) {
    this.enableLogging = enableLogging;
  }

  public Boolean getEnableLogging() {
    return enableLogging;
  }

  public Integer getMaxNumberOfViolations() {
    return maxNumberOfViolations;
  }

  @DataBoundSetter
  public void setMaxNumberOfViolations(final Integer maxNumberOfViolations) {
    this.maxNumberOfViolations = maxNumberOfViolations;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<ViolationsToGitLabConfig> {
    @NonNull
    @Override
    public String getDisplayName() {
      return "Violations To GitHub Server Config";
    }

    @Restricted(NoExternalUse.class)
    public ListBoxModel doFillMinSeverityItems() {
      final ListBoxModel items = new ListBoxModel();
      items.add("Default, Global Config or Info", "");
      for (final SEVERITY severity : SEVERITY.values()) {
        items.add(severity.name());
      }
      return items;
    }

    @SuppressWarnings("unused") // Used by stapler
    public ListBoxModel doFillProxyCredentialsIdItems(
        @AncestorInPath final Item item,
        @QueryParameter final String apiTokenCredentialsId,
        @QueryParameter final String gitLabUrl) {
      return CredentialsHelper.doFillUserNamePasswordCredentialsIdItems();
    }

    @SuppressWarnings("unused") // Used by stapler
    public ListBoxModel doFillApiTokenCredentialsIdItems(
        @AncestorInPath final Item item,
        @QueryParameter final String apiTokenCredentialsId,
        @QueryParameter final String gitLabUrl) {
      return CredentialsHelper.doFillApiTokenCredentialsIdItems(
          item, apiTokenCredentialsId, gitLabUrl);
    }

    @SuppressWarnings("unused") // Used by stapler
    public FormValidation doCheckApiTokenCredentialsId(
        @AncestorInPath final Item item,
        @QueryParameter final String value,
        @QueryParameter final String gitLabUrl) {
      return CredentialsHelper.doCheckApiTokenCredentialsId(item, value, gitLabUrl);
    }
  }
}
