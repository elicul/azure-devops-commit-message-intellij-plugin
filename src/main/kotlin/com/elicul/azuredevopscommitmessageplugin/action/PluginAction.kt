package com.elicul.azuredevopscommitmessageplugin.action

import com.elicul.azuredevopscommitmessageplugin.services.CommitMessageService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable
import git4idea.GitUtil

class PluginAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val currentProject = actionEvent.project

        if (currentProject != null) {
            val plugin = currentProject.service<CommitMessageService>()

            val repositoryManager = GitUtil.getRepositoryManager(currentProject)
            val branch = repositoryManager.repositories[0].currentBranch

            val newCommitMessage = plugin.getCommitMessageFromBranchName(branch?.name)
            getCommitPanel(actionEvent)?.setCommitMessage(newCommitMessage)
        }
    }

    private fun getCommitPanel(actionEvent: AnActionEvent): CommitMessageI? {
        val data = Refreshable.PANEL_KEY.getData(actionEvent.dataContext)

        if (data is CommitMessageI) {
            return data
        }

        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(actionEvent.dataContext)
    }
}