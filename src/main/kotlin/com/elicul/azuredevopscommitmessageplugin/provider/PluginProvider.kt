package com.elicul.azuredevopscommitmessageplugin.provider

import com.elicul.azuredevopscommitmessageplugin.services.CommitMessageService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import git4idea.GitUtil

class PluginProvider : CommitMessageProvider {

    override fun getCommitMessage(forChangelist: LocalChangeList, project: Project): String? {
        val comment: String? = forChangelist.comment
        val commitMessageService = project.service<CommitMessageService>()

        val repositoryManager = GitUtil.getRepositoryManager(project)
        val branch = repositoryManager.repositories[0].currentBranch

        val newCommitMessage = commitMessageService.getCommitMessageFromBranchName(branch?.name)

        return if (newCommitMessage == "") {
            comment
        } else {
            newCommitMessage
        }
    }
}